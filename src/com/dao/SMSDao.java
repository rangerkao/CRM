package com.dao;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bean.DataOpenSMS;
import com.bean.SMS;
import com.common.BaseDao;


@Repository
public class SMSDao extends BaseDao{

	public SMSDao() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public List<SMS> querySMS(String S2TMSISDN,String CHTMSISDN,String startDate,String endDate,String activatedDate,String canceledDate) throws Exception, ParseException{
		
		String sql;
		List<SMS> result = new ArrayList<SMS>();
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConn1();
			st = conn.createStatement();
			sql = "SELECT SCLASS,SEND_NUMBER,MSG,TO_CHAR(SENDTIME,'yyyyMMdd hh24:mi:ss') SENDTIME " + "FROM( "
			//DVRS 比對香港號與中華號
					+ "			SELECT 'DVRS' SCLASS ,A.SEND_NUMBER,A.MSG,A.SEND_DATE  SENDTIME "
					+ "			FROM HUR_SMS_LOG A " + "			WHERE (A.SEND_NUMBER = '" + S2TMSISDN
					+ "' OR A.SEND_NUMBER = '" + CHTMSISDN + "' ) "
					//TWNLD
					+ "		UNION "
					+ "			SELECT 'TWNLD' SCLASS,B.PHONENUMBER SEND_NUMBER,B.CONTENT MSG,B.CREATETIME SENDTIME "
					+ "			FROM S2T_BL_SMS_LOG B " + "			WHERE (B.PHONENUMBER = '" + CHTMSISDN + "' ) "
					+ "		UNION "
					+ "			SELECT 'Landing' SCLASS,C.SEND_NUMBER,C.CONTENT MSG,C.SEND_TIME SENDTIME "
					+ "			FROM LANDING_SMS_LOG C " + "			WHERE C.SEND_NUMBER = '" + S2TMSISDN + "' "
					+ ") " + "WHERE 1=1 "
					+ (startDate == null || "".equals(startDate) ? "": "AND SENDTIME >= to_date('" + startDate + "','yyyy/MM/dd') ")
					+ (endDate == null || "".equals(endDate) ? "": "AND SENDTIME <= to_date('" + endDate + "','yyyy/MM/dd')+1 ")
					+ (activatedDate == null || "".equals(activatedDate) ? "": "AND SENDTIME >= to_date('" + activatedDate + "','yyyy/MM/dd hh24:mi:ss') ")
					+ (canceledDate == null || "".equals(canceledDate) ? ""	: "AND SENDTIME <= to_date('" + canceledDate + "','yyyy/MM/dd hh24:mi:ss') ")
					+ " ORDER BY SENDTIME DESC " + "";
			System.out.println(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				SMS r = new SMS();
				r.setSmsclass(rs.getString("SCLASS"));
				r.setPhoneno(rs.getString("SEND_NUMBER"));

				if ("Landing".equalsIgnoreCase(rs.getString("SCLASS")))
					r.setContent(processData(rs.getString("MSG"), "ISO-8859-1", "UTF-8"));
				else
					r.setContent(processData(rs.getString("MSG"), "ISO-8859-1", "BIG5"));

				r.setSendTime(rs.getString("SENDTIME"));
				result.add(r);
			} 
		} finally {
			closeConnection(conn, st, ps, rs);
		}
		
		try {
			conn = getConn2();
			st = conn.createStatement();
			sql = "SELECT SCLASS,SEND_NUMBER,MSG,TO_CHAR(SENDTIME,'yyyyMMdd hh24:mi:ss') SENDTIME " + "FROM( "
					+ "		SELECT 'LocalNumber' SCLASS,A.SERVICECODE SEND_NUMBER,A.MSGCONTENT MSG,A.LASTSUCCESSTIME SENDTIME "
					+ "		FROM MSSENDINGTASK A " + "		WHERE (A.SERVICECODE = '" + S2TMSISDN
					+ "'  OR A.SERVICECODE = '" + CHTMSISDN + "' ) " + "		UNION "
					+ "		SELECT 'LocalNumber' SCLASS,C.SERVICECODE SEND_NUMBER,C.MSGCONTENT MSG,C.LASTSUCCESSTIME SENDTIME "
					+ "		FROM MESSAGETASK C " + "		WHERE (C.SERVICECODE = '" + S2TMSISDN
					+ "' OR C.SERVICECODE = '" + CHTMSISDN + "' )  " + ") " + "WHERE 1=1 "
					+ (startDate == null || "".equals(startDate) ? ""
							: "AND SENDTIME >= to_date('" + startDate + "','yyyy/MM/dd') ")
					+ (endDate == null || "".equals(endDate) ? ""
							: "AND SENDTIME <= to_date('" + endDate + "','yyyy/MM/dd') ")
					+ (activatedDate == null || "".equals(activatedDate) ? ""
							: "AND SENDTIME >= to_date('" + activatedDate + "','yyyy/MM/dd hh24:mi:ss') ")
					+ (canceledDate == null || "".equals(canceledDate) ? ""
							: "AND SENDTIME <= to_date('" + canceledDate + "','yyyy/MM/dd hh24:mi:ss') ")
					+ " ORDER BY SENDTIME DESC ";
			System.out.println(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				SMS r = new SMS();
				r.setSmsclass(rs.getString("SCLASS"));
				r.setPhoneno(rs.getString("SEND_NUMBER"));
				r.setContent(processData(rs.getString("MSG"), "ISO-8859-1", "BIG5"));
				r.setSendTime(rs.getString("SENDTIME"));
				result.add(r);
			} 
		} finally {
			closeConnection(conn, st, ps, rs);
		}
			sort(result);

		return result;
	}
	
	
	public DataOpenSMS queryDataOpenSMS() throws Exception {
		DataOpenSMS result = new DataOpenSMS();
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConn1();
			st = conn.createStatement();
			String sql = "SELECT case A.id "
					+ "			when 201 then 'A' "
					+ "			when 202 then 'B' "
					+ "			when 203 then 'CA' "
					+ "			when 204 then 'CI' "
					+ "			END ID,A.CONTENT "
					+ "FROM HUR_SMS_CONTENT A  "
					+ "where A.ID in ('201','202','203','204') "
					+ "and to_date(A.START_DATE,'yyyyMMddhh24miss')<=sysdate "
					+ "and (A.END_DATE is null or to_date(A.END_DATE,'yyyyMMddhh24miss')>sysdate)  "
					+ "";
			
			System.out.println(sql);
			rs = st.executeQuery(sql);
			
			Map<String,String> typeC = new HashMap<String,String>();
			while(rs.next()) {
				String id = rs.getString("ID");
				String content = processData(rs.getString("CONTENT"),"ISO8859-1","BIG5");
				
				if("A".equalsIgnoreCase(id)) {
					result.setTypeA(content);
				}else if("B".equalsIgnoreCase(id)) {
					result.setTypeB(content);
				}else if("CA".equalsIgnoreCase(id)) {
					typeC.put("android", content);
				}else if("CI".equalsIgnoreCase(id)) {
					typeC.put("iphone", content);
				}
			}
			result.setTypeC(typeC);
		}finally {
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}

	public void sort(List<SMS> l) throws ParseException{
		int size = l.size();
		for(int i = 0 ; i<size;i++){
			for(int j = 1 ;j<size;j++){
				swap(l,j-1,getTimeValue(l.get(j-1).getSendTime(),"yyyyMMdd HH:mm:ss"),j,getTimeValue(l.get(j).getSendTime(),"yyyyMMdd HH:mm:ss"));
			}
		}
	}


	public void swap(List<SMS> l,int i ,long vi,int j,long vj){
		
		if(vi<vj){
			SMS io = l.get(i);
			SMS jo = l.get(j);
			l.set(i, jo);
			l.set(j, io);
		}
	}
	
	public long getTimeValue(String s) throws ParseException{
		return getTimeValue(s,"yyyyMMddHHmmss");
	}
	
	public long getTimeValue(String s,String format) throws ParseException{
		Long value = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		value=sdf.parse(s).getTime();
		return value;
	}
	

}
