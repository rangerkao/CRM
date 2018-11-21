package com.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.bean.PricePlanID;
import com.bean.Subscriber;
import com.cache.CacheAction;
import com.common.BaseDao;

@Repository
public class UniversalDao extends BaseDao{

	public UniversalDao() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Subscriber queryServiceData(String serviceid) throws Exception {
		Subscriber result = new Subscriber();
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			
			String sql = "select serviceid,datecanceled,priceplanid "
					+ "from service "
					+ "where serviceid = '"+serviceid+"' ";
			System.out.println("sql : "+sql);
			rs = null;
			rs =  st.executeQuery(sql);
			if(rs.next()){
				result.setServiceId(rs.getString("serviceid"));
				result.setCanceledDate(rs.getString("datecanceled"));
				PricePlanID pricePlanId = new PricePlanID();
				pricePlanId.setId(rs.getString("priceplanid"));
				result.setPricePlanId(pricePlanId );
			}

		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	public Set<String> queryAddonService(String serviceid) throws Exception {
		Set<String> result = new HashSet<String>();
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			
			String sql = "select SERVICECODE "
					+ "from ADDONSERVICE_N "
					+ "where STATUS = 'A' "
					+ "AND serviceid = "+serviceid+" ";
			System.out.println("sql : "+sql);
			rs = null;
			rs =  st.executeQuery(sql);
			while(rs.next()){
				result.add(rs.getString("SERVICECODE"));
			}

		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	public boolean queryCHPReserve(String serviceid) throws Exception {
		boolean result = false;
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			
			String sql = "select RESERVE_DATE "
					+ "from HUR_ADDONSERVICE_RESERVE "
					+ "where ACTIVATION_TIME is null and CANCELED_TIME is null "
					+ "and SERVICEID = "+serviceid+" ";
			System.out.println("sql : "+sql);
			rs = null;
			rs =  st.executeQuery(sql);
			if(rs.next()){
				if(rs.getString("RESERVE_DATE")!=null) {
					result = true;
				}
			}

		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	public boolean addCHPReserve(String serviceid,int type) throws Exception {
		boolean result = false;
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			
			String sql = "insert into HUR_ADDONSERVICE_RESERVE(SERVICEID,RESERVE_DATE,TYPE,SOURCE) "
					+ "values ("+serviceid+",to_char(sysdate,'yyyyMMdd),"+type+",'S2T')";
			System.out.println("sql : "+sql);
			st.executeUpdate(sql);
			result= true;
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	public boolean addVolumePocket(String serviceid,String startDate,String endDate,Long limit,String imsi) throws Exception {
		boolean result = false;
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			
			String pid = null;
			String sql = "select HUR_VOLUME_POCKET_SEQ.nextval PID  from dual ";
			System.out.println("sql : "+sql);
			rs = st.executeQuery(sql);
			if(rs.next()) {
				pid = rs.getString("PID");
			}
			
			if(pid!=null) {
				sql = "insert into HUR_VOLUME_POCKET(SERVICEID,START_DATE,END_DATE,MCC,PID,TYPE,LIMIT,ID,IMSI) "
						+ "values('"+serviceid+"','"+startDate+"','"+endDate+"','def',"+pid+",6,"+limit+",'S2T',"+imsi+")";
				System.out.println("sql : "+sql);
				st.executeUpdate(sql);
				result= true;
			}
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	public boolean addAddonService(String chtImsi,String chtMsisdn,String s2tImsi,String s2TMsisdn,String addonCode,String action) throws Exception {
		boolean result = false;
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			String sql = "INSERT INTO ADDONSERVICE (REQUESTDATETIME,MNOSUBCODE,MNOIMSI,MNOMSISDN,S2TIMSI,S2TMSISDN,ADDONCODE,ADDONACTION,SENDDATETIME,DONEDATETIME) "
					+ "VALUES (SYSDATE" + ",'950','" + chtImsi + "','"+ chtMsisdn + "','" + s2tImsi + "','" + s2TMsisdn + "','"+ addonCode + "','"+action+"',null,null)";
			System.out.println("sql : "+sql);
			st.executeUpdate(sql);
			result= true;
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	public boolean addAddonService_N(String serviceid,String chtImsi,String chtMsisdn,String s2tImsi,String s2TMsisdn,String addonCode) throws Exception {
		boolean result = false;
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			
			String seqId = null;
			String sql = "SELECT S2T_SQ_ADDONSERVICE_N.nextval ab from dual ";
			System.out.println("sql : "+sql);
			rs = st.executeQuery(sql);
			if(rs.next()) {
				seqId = rs.getString("ab");
			}
			
			if(seqId!=null) {
				sql = "INSERT INTO ADDONSERVICE_N(SEQ,MNOIMSI,MNOMSISDN,S2TIMSI,S2TMSISDN,SERVICECODE,STATUS,STARTDATE,SERVICEID) "
				+ "VALUES("+ seqId + ",'"+ chtImsi+ "','"+ chtMsisdn+ "','"+ s2tImsi+ "','"+ s2TMsisdn+ "','"+ addonCode+ "','A',SYSDATE," + serviceid + ")";
				System.out.println("sql : "+sql);
				st.executeUpdate(sql);
				result= true;
			}
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	public boolean delService_N(String serviceid,String addonCode) throws Exception {
		boolean result = false;
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			
			String seqId = null;
			String sql = "SELECT MIN(SEQ) seq from ADDONSERVICE_N where serviceid = '"+serviceid+" and SERVICECODE = '"+addonCode+"' ";
			System.out.println("sql : "+sql);
			rs = st.executeQuery(sql);
			if(rs.next()) {
				seqId = rs.getString("seq");
			}
			
			if(seqId!=null) {
				sql = "UPDATE ADDONSERVICE_N A "
						+ "SET A.STATUS ='D',A.ENDDATE = SYSDATE "
						+ "WHERE A.ENDDATE IS NULL and A.seq ='"+seqId+ "' ";
				System.out.println("sql : "+sql);
				st.executeUpdate(sql);
				result= true;
			}
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	public String getAddonServiceLastDate(String serviceid,String addonCode) throws Exception {
		String result = null;
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			String sql = "select to_char(MAX(ENDDATE),'yyyyMMdd') da "
					+ "from ADDONSERVICE_N "
					+ "where SERVICECODE in ("+addonCode+") "
					+ "AND SERVICEID = "+serviceid+" "  
					+ "group by serviceid";
			System.out.println("sql : "+sql);
			rs = st.executeQuery(sql);
			if(rs.next()) {
				result = rs.getString("da");
			}
			
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	public Long getCHPVolume(String serviceid,int type) throws Exception {
		Long result = null;
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			String sql = "select sum(b.volume) vo "
					+ "from HUR_VOLUME_POCKET A, HUR_CURRENT_DAY B "
					+ "where A.SERVICEID = B.SERVICEID "
					+ "AND A.START_DATE<=B.DAY AND A.END_DATE>=B.DAY "
					+ (type==6?"AND (B.MCCMNC like '454%' or B.MCCMNC like '460%') ":" ")
					+ "AND A.TYPE = "+type+" "
					+ "AND A.TERMINATE = 0 "
					+ "AND A.SERVICEID = '"+serviceid+"'";
			System.out.println("sql : "+sql);
			rs = st.executeQuery(sql);
			if(rs.next()) {
				result = rs.getLong("vo");
			}
			
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	public String getLastVolumePocket(String serviceid,int type) throws Exception {
		String result = null;
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			String sql = "select PID,SERVICEID,START_DATE,END_DATE "
					+ "from HUR_VOLUME_POCKET "
					+ "where 1 = 1  "
					+ "type = "+type+" "
					+ "and SERVICEID in ("+serviceid+") "
					+ "order by END_DATE desc";
			System.out.println("sql : "+sql);
			rs = st.executeQuery(sql);
			if(rs.next()) {
				result = rs.getString("END_DATE");
			}
			
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	public String queryRoammingRestriction(String serviceid) throws Exception {
		String result = null;
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn2();
			st = conn.createStatement();
			String sql = "SELECT A.SERVICEID, B.COVERAGEID, B.COVERAGENAME "
					+ "FROM PARAMETERVALUE A, ROAMINGCOVERAGE B "
					+ "WHERE A.PARAMETERVALUEID=3795  "
					+ "AND A.VALUE=B.COVERAGEID "
					+ "AND A.SERVICEID="+serviceid+" ";
			System.out.println("sql : "+sql);
			rs = st.executeQuery(sql);
			if(rs.next()) {
				result = rs.getString("COVERAGENAME");
			}
			
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	
	public String querySMSContent(String msgid) throws Exception {
		String result = null;
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			String sql = "SELECT A.ID,A.CONTENT,A.CHARSET "
					+ "FROM HUR_SMS_CONTENT A "
					+ "WHERE A.START_DATE<= to_char(sysdate,'yyyyMMdd') "
					+ "AND (A.END_DATE IS NULL OR A.END_DATE>to_char(sysdate,'yyyyMMdd'))  "
					+ "AND A.ID = '"+msgid+"' "
					+ "order by A.ID "
					+ "";
			System.out.println("sql : "+sql);
			rs = st.executeQuery(sql);
			if(rs.next()) {
				result = rs.getString("CONTENT");
			}
			
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	


}
