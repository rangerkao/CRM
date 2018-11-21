package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bean.VIP;
import com.common.BaseDao;

@Repository
public class VIPDao extends BaseDao{

	public VIPDao() throws Exception {
		super();
	}
	
	//查詢列表
	public VIP queryVIP(String serviceid) throws Exception{
		
		VIP result = new VIP();
		
		String sql=
				"select SERVICEID,"
				+ "		to_char(CREATE_DATE,'yyyy/MM/dd hh24:mi:ss') CREATE_DATE,"
				+ "		to_char(CANCEL_DATE,'yyyy/MM/dd hh24:mi:ss') CANCEL_DATE,"
				+ "		REMARK "
				+ "from HUR_GPRS_THRESHOLD "
				+ "where SERVICEID = "+serviceid+" "
				+ "order by create_date desc";
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = getConn1();
		try {
			st = conn.createStatement();
			System.out.println("Execute SQL :"+sql);
			rs = st.executeQuery(sql);
			List<Map<String,String>> l = new ArrayList<Map<String,String>>();
			while(rs.next()){
				Map<String,String> m = new HashMap<String,String>();
				m.put("serviceid", rs.getString("SERVICEID"));
				m.put("createtime", rs.getString("CREATE_DATE"));
				m.put("canceledtime", processData(rs.getString("CANCEL_DATE")));
				m.put("remark", processData(rs.getString("REMARK")));
				l.add(m);
			}
			result.setList(l);
			
			
			
			sql = ""
					+ "select CONTENT "
					+ "from HUR_SMS_CONTENT "
					+ "where to_date(start_date,'yyyyMMdd')<=sysdate "
					+ "and (end_date is null or to_date(end_date,'yyyyMMdd')>=sysdate) "
					+ "and id = (select value from HUR_DVRS_CONFIG where name = 'SET_VIP_SMS') ";
			
			rs = null;
			System.out.println("Execute SQL :"+sql);
			rs = st.executeQuery(sql);
			
			if(rs.next()) {
				result.setMsg(new String(processData(rs.getString("CONTENT")).getBytes("ISO8859-1"),"BIG5"));
			}

		} finally{
			closeConnection(conn, st, ps, rs);
		}
		return result;
	}
	
	//查詢列表
	public void addVIP(String serviceid) throws Exception{
		
		String sql=	"insert into HUR_GPRS_THRESHOLD(serviceid) VALUES("+serviceid+") ";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = getConn1();
		try {
			st = conn.createStatement();
			System.out.println("Execute SQL :"+sql);
			int result = st.executeUpdate(sql);
			
			if(result!=1) 
				throw new Exception("新增失敗");

		} finally{
			closeConnection(conn, st, ps, rs);
		}

	}
	
	//查詢列表
	public void deleteVIP(String serviceid) throws Exception{
		
		String sql=""
				+ "update HUR_GPRS_THRESHOLD "
				+ "set CANCEL_DATE = sysdate "
				+ "where SERVICEID = "+serviceid+" and CANCEL_DATE is null ";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = getConn1();
		conn.setAutoCommit(false);
		try {
			st = conn.createStatement();
			System.out.println("Execute SQL :"+sql);
			int result = st.executeUpdate(sql);
	
			if(result!=1) {
				conn.rollback();
				throw new Exception("取消失敗");
			}
		} finally{
			conn.setAutoCommit(true);
			closeConnection(conn, st, ps, rs);
		}
	}
}
