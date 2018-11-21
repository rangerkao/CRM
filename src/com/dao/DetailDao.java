package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bean.AddonService;
import com.bean.Application;
import com.bean.Detail;
import com.common.BaseDao;

@Repository
public class DetailDao extends BaseDao {

	
	public DetailDao() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public Detail queryDetail(String serviceid) throws Exception {
		
		Detail d = new Detail();
		
		//GPRS 狀態
		d.setGprsStatus(getGPRSStatus(serviceid));
		
		//AddonService
		d.setAddonservices(queryAddonService(serviceid));
		
		//Application
		d.setApplication(queryApplication(serviceid));
		
		return d;
	}
	
	/**
	 * 取得數據狀態
	 * @param msisdn
	 * @return
	 * @throws Exception
	 */
	public String getGPRSStatus(String serviceid) throws Exception{
		String result = null;
		String sql = "SELECT nvl(PDPSUBSID,0) as status "
				+ "FROM BASICPROFILE "
				+ "WHERE serviceid = '"+serviceid+"'";
				
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		conn = getConn1();
		try {
			
			st = conn.createStatement();
			//System.out.println("sql:"+sql);
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				result = rs.getString("status");
			}
		} finally{
			closeConnection(conn, st, ps, rs);
		}
		return result;
	}
	
	/**
	 * 查詢華人上網包資料
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */
	public List<AddonService> queryAddonService(String serviceId) throws Exception{
		List<AddonService> result = new ArrayList<AddonService>(); 

		String sql = "SELECT A.SERVICEID,A.SERVICECODE,A.STATUS,A.STARTDATE,A.ENDDATE "
				+ "FROM ADDONSERVICE_N A "
				+ "WHERE A.SERVICEID = '"+serviceId+"' "
				+ "ORDER BY A.STARTDATE DESC";
				
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = getConn1();
		try {
			
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				AddonService a = new AddonService();
				a.setServiceId(rs.getString("SERVICEID"));
				a.setServiceCode(rs.getString("SERVICECODE"));
				a.setStatus(rs.getString("STATUS"));
				a.setStartDate(rs.getString("STARTDATE"));
				a.setEndDate(rs.getString("ENDDATE"));
				result.add(a);
			}
		} finally{
			closeConnection(conn, st, ps, rs);
		}
		return result;
	}
	
	/**
	 * 查詢申請書資料
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */
	public Application queryApplication(String serviceid) throws Exception{
		Application result = new Application(); 

		String sql = "select DATE_FORMAT(VERIFIED_DATE,'%Y/%m/%d %H:%m:%s') VERIFIED_DATE,CREATETIME,TYPE "
				+ "from CRM_APPLICATION "
				+ "where type = '供裝' "
				+ "and serviceid = '"+serviceid+"' "
				//+ "and order by VERIFIED_DATE DESC "
				+ "";
				
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = getConn3();
		try {
			
			st = conn.createStatement();
			System.out.println("Execute sql:"+sql);
			rs = st.executeQuery(sql);
			
			if(rs.next()){
				result.setServiceid(serviceid);
				result.setDateTime(rs.getString("VERIFIED_DATE"));
				result.setType(rs.getString("TYPE"));
			}
		} finally{
			closeConnection(conn, st, ps, rs);
		}
		return result;
	}
	
	public String queryWhetherAppliedCHNA(String serviceId) throws Exception{
		String sql = "";
		String s2tIMSI = null,chtMsisdn = null;
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = getConn1();
		try {
				
			sql = "select a.imsi,c.PARTNERMSISDN "
					+ "from IMSI A,service B,AVAILABLEMSISDN c "
					+ "where A.serviceid = B.serviceid and B.servicecode = c.s2tMsisdn and a.serviceid = "+serviceId+" ";
			st = conn.createStatement();		
			rs = st.executeQuery(sql);
			while(rs.next()){
				s2tIMSI = rs.getString("IMSI");
				chtMsisdn = rs.getString("PARTNERMSISDN");
			}
			rs = null;
			sql = "select instr(A.content,'CHNA') CD,to_char(A.REQTIME,'MM/dd') EF "
					+ "from PROVLOG A "
					+ "where (A.CONTENT like '%TWNLD_MSISDN="+chtMsisdn+"%' or A.CONTENT like '%S2T_IMSI="+s2tIMSI+"%'  )"
					+ "AND (A.CONTENT like '%CHNA%' or A.CONTENT like '%CHND%') "
					+ "order by A.REQTIME desc ";
			rs = st.executeQuery(sql);
			
			//只取最後一筆
			if(rs.next()){
				int cd = rs.getInt("CD");
				String ef = rs.getString("EF");
				if(cd!=0)
					return "＊"+ef+"已申請中國固定號";
			}
		} finally{
			closeConnection(conn, st, ps, rs);
		}
		return null;
	}

}
