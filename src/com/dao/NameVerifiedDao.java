package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.bean.NameVerified;
import com.common.BaseDao;

@Repository
public class NameVerifiedDao extends BaseDao {

	public NameVerifiedDao() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public List<NameVerified> queryNameVarifiedData(String input,String type) throws Exception{
		List<NameVerified> result = new ArrayList<NameVerified>();
		
		String sql = "select A.SERVICEID,A.NAME,A.ID,A.TYPE,A.REMARK,DATE_FORMAT(A.create_time,'%Y/%m/%d %H:%i:%s') TIME,A.send_date,A.msisdn,A.vln,A.status,IFNULL(B.cou,0) cou "
				+ "from CRM_DB.CRM_NAME_VERIFIED A left join  "
				+ "			(	select id,count(vln) cou "
				+ "				from CRM_DB.CRM_NAME_VERIFIED A "
				+ "				where status = 1 "
				+ "				group by id ) B on A.id = B.id "
				+ ("serviceid".equals(type)?"where A.SERVICEID = ? ":" ")
				+ ("name".equals(type)?"where A.NAME like concat('%',?,'%') ":" ")
				+ ("id".equals(type)?"where A.ID like concat('%',?,'%') ":" ")
				+ ("type".equals(type)?"where A.TYPE like concat('%',?,'%') ":" ")
				+ ("date".equals(type)?"where A.send_date like concat('%',?,'%') ":" ")
				+ ("msisdn".equals(type)?"where A.msisdn like concat('%',?,'%') ":" ")
				+ ("vln".equals(type)?"where A.vln like concat('%',?,'%') ":" ")
				+ "order by status desc,TIME desc ";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn =getConn3();
			System.out.println(sql);
			ps = conn.prepareStatement(sql);
			ps.setString(1, input.trim());
			rs = ps.executeQuery();
			while(rs.next()){
				NameVerified c = new NameVerified();
				c.setServiceid(processData(rs.getString("SERVICEID")));
				c.setName(processData(rs.getString("NAME")));
				c.setId(processData(rs.getString("ID")));
				c.setType(processData(rs.getString("TYPE")));
				c.setRemark(processData(rs.getString("REMARK")));
				c.setCreateTime(processData(rs.getString("TIME")));
				c.setSendDate(processData(rs.getString("send_date")));
				c.setVln(processData(rs.getString("vln")));
				c.setMsisdn(processData(rs.getString("msisdn")));
				c.setStatus(rs.getString("status"));
				c.setUsedCount(rs.getString("cou"));
				result.add(c);
			}
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		return result;
	}
	
	
	
	public String  addNameVarifiedData(NameVerified c) throws Exception{

		Set<String> vlns = new HashSet<String>();
		vlns.add(c.getVln());
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//認證已供裝未認證的門號
			conn = getConn1();
			String sql = "select VLN "
					+ "from VLNNUMBER "
					+ "where status = 1 and vln like '86%' "
					+ "and serviceid = "+c.getServiceid()+" ";
			
			st =  conn.createStatement();
			System.out.println("sql : "+sql);
			rs = st.executeQuery(sql);
			while(rs.next()) {
				vlns.add(rs.getString("VLN"));
			}
		} finally {
			closeConnection(conn, st, ps, rs);
		}

		try {
			
			String sql,sendDate = null;
			conn =getConn3();
			st = conn.createStatement();
			for(String vln : vlns) {
				//Step 1 如果是換中華號，因vln與個資認證不需變更，所以找出已認證的日期
				sql = ""
						+ "select A.send_date DATE "
						+ "from CRM_DB.CRM_NAME_VERIFIED A "
						+ "where A.id = '"+c.getId()+"' "
						+ "and A.name = '"+c.getName()+"' "
						+ "and A.msisdn <> '"+c.getMsisdn()+"' "
						+ "and A.vln = '"+vln+"' "
						+ "and status = '1' ";
				
				rs = null;
				System.out.println("sql : "+sql);
				rs =  st.executeQuery(sql);
				if(rs.next()){
					sendDate = rs.getString("DATE");
				}
				
				
				
				//將vln舊資料變更為歷史
				sql = ""
						+ "update CRM_DB.CRM_NAME_VERIFIED A "
						+ "set status = '0' "
						+ "where A.vln = '"+vln+"' "
						+ "and A.status = '1' ";
				System.out.println("sql : "+sql);
				st.executeUpdate(sql);
				
				
				//新增資料
				sql = ""
						+ "insert into CRM_DB.CRM_NAME_VERIFIED"
						+ "(serviceid,name,id,type,remark,msisdn,vln,send_date) "
						+ "values("+c.getServiceid()+",'"+c.getName()+"','"+c.getId()+"','"+c.getType()+"','"+processData(c.getRemark()).trim()+"','"+c.getMsisdn()+"','"+vln+"',"+(sendDate==null?null:" '"+sendDate+"' ")+")";
				System.out.println("sql : "+sql);
				st.executeUpdate(sql);
			}

		}finally{
			closeConnection(conn, st, ps, rs);
		}

		return "SUCCESS";
	}
	
	public String  updateNameVarifiedData(NameVerified c) throws Exception{
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql ;
		try {
			conn =getConn3();
			st = conn.createStatement();
			sql = ""
					+ "update CRM_DB.CRM_NAME_VERIFIED "
					+ "set serviceid = "+c.getServiceid()+",name='"+c.getName()+"',id='"+c.getId()+"',type='"+c.getType()+"',remark='"+processData(c.getRemark()).trim()+"',msisdn='"+c.getMsisdn()+"'  "
					+ "where vln='"+c.getVln()+"'  and status = '1' ";
			
			System.out.println("sql : "+sql);
			st.executeUpdate(sql);
		}finally{
			closeConnection(conn, st, ps, rs);
		}

		return "SUCCESS";
	}
}
