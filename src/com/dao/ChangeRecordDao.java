package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bean.ChangeRecord;
import com.common.BaseDao;

@Repository
public class ChangeRecordDao extends BaseDao{


	
	public ChangeRecordDao() throws Exception {
		super();
	}
	
	public List<ChangeRecord> queryNumberChangeRecord(String serviceid) throws Exception {
		List<ChangeRecord> result = new ArrayList<ChangeRecord>();
		
		String sql = ""
				+ "SELECT A.ORDERID, A.OLDVALUE, A.NEWVALUE, A.COMPLETEDATE time, C.SERVICEID, C.SERVICECODE, C.STATUS  "
				+ "FROM SERVICEPARAMVALUECHANGEORDER A, SERVICEORDER B, SERVICE C "
				+ "WHERE A.ORDERID=B.ORDERID AND B.SERVICEID=C.SERVICEID "
				+ "AND A.PARAMETERVALUEID=3792 AND C.SUBSIDIARYID=59 "
				+ "AND C.SERVICEID = "+serviceid+" "
				+ "ORDER BY A.COMPLETEDATE DESC ";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConn1();
			st = conn.createStatement();
			System.out.println(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ChangeRecord c = new ChangeRecord();
				c.setNewValue(processData(rs.getString("NEWVALUE")));
				c.setOldValue(processData(rs.getString("OLDVALUE")));
				c.setTime(rs.getString("time"));
				result.add(c);
			} 
		} finally {
			closeConnection(conn, st, ps, rs);
		}
		return result;
	}
	
	public List<ChangeRecord> queryCardChangeRecord(String serviceid) throws Exception {
		List<ChangeRecord> result = new ArrayList<ChangeRecord>();
		
		String sql = "SELECT * FROM ( "
				+ "SELECT c.serviceid, c.servicecode, '' OldIMSI, a.fieldvalue NewIMSI, to_char(b.COMPLETEDATE,'yyyy/MM/dd hh24:mi:ss') time, b.orderid, 'New' OrderType "
				+ "FROM NEWSERVICEORDERINFO a, serviceorder b, service c "
				+ "WHERE a.fieldid=3713 AND a.orderid=b.orderid "
				+ "AND b.serviceid=c.serviceid "
				+ "AND TO_CHAR(c.dateactivated,'yyyymmdd')>='20070205' "
				+ "AND TO_CHAR(b.completedate,'yyyymmdd')>='20070205' "
				+ "AND c.serviceid = "+serviceid+" "
				+ "UNION ALL "
				+ "SELECT c.SERVICEID, C.servicecode, a.oldvalue OldIMSI, A.NEWVALUE NewIMSI, to_char(A.COMPLETEDATE,'yyyy/MM/dd hh24:mi:ss') time , b.orderid, 'Change' OrderType "
				+ "FROM SERVICEINFOCHANGEORDER A, SERVICEORDER B, SERVICE C "
				+ "WHERE A.FIELDID=3713 AND A.ORDERID=B.ORDERID "
				+ "AND B.SERVICEID=C.SERVICEID "
				+ "AND TO_CHAR(c.dateactivated,'yyyymmdd')>='20070205' "
				+ "AND TO_CHAR(a.completedate,'yyyymmdd')>='20070205' "
				+ "AND a.oldvalue <> a.newvalue "
				+ "AND c.serviceid = "+serviceid+" "
				+ ") ORDER BY TIME DESC ";
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConn1();
			st = conn.createStatement();
			System.out.println(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ChangeRecord c = new ChangeRecord();
				c.setNewValue(rs.getString("NewIMSI"));
				c.setOldValue(processData(rs.getString("OldIMSI")));
				c.setOrderType(rs.getString("OrderType"));
				c.setTime(rs.getString("time"));
				result.add(c);
			} 
		} finally {
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}
	
	

}
