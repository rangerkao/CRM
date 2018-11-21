package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bean.CurrentDay;
import com.bean.CurrentMonth;
import com.common.BaseDao;

@Repository
public class CurrentDao extends BaseDao{
	
	public CurrentDao() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<CurrentMonth> queryCurrentMonth(String serviceId,String from,String to) throws Exception{
		List<CurrentMonth> list = new ArrayList<CurrentMonth>();
		
		String sql=
				"SELECT A.MONTH,A.SERVICEID,A.CHARGE,A.VOLUME,A.SMS_TIMES,A.LAST_ALERN_THRESHOLD,A.LAST_ALERN_VOLUME,A.EVER_SUSPEND,A.LAST_FILEID "
				+ ",TO_CHAR(A.LAST_DATA_TIME,'yyyy/MM/dd hh24:mi:ss') LAST_DATA_TIME,TO_CHAR(A.UPDATE_DATE,'yyyy/MM/dd hh24:mi:ss') UPDATE_DATE,TO_CHAR(A.CREATE_DATE,'yyyy/MM/dd hh24:mi:ss') CREATE_DATE "
				+ "FROM HUR_CURRENT A where 1=1 "
				+ (from!=null &&!"".equals(from)?"AND A.MONTH >=  "+from+" ":"")
				+ (to!=null &&!"".equals(to)?"AND A.MONTH <=  "+to+" ":"")
				//+ (suspend!=null && !"".equals(suspend)?"AND A.EVER_SUSPEND="+suspend+" ":"")
				+ (serviceId!=null && !"".equals(serviceId)? "AND A.SERVICEID in "+serviceId+" ":"")
				+ "ORDER BY A.LAST_DATA_TIME DESC ";

		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		conn = getConn1();
		try{
			st = conn.createStatement();
			System.out.println("query execute : "+sql);
			rs = st.executeQuery(sql);

				while(rs.next()){
	
				CurrentMonth c = new CurrentMonth();
				c.setMonth(rs.getString("MONTH"));
				//c.setImsi(imsi);
				c.setCharge(FormatDouble(rs.getDouble("CHARGE"), "0.0000"));
				c.setVolume(rs.getDouble("VOLUME"));
				c.setSmsTimes(rs.getInt("SMS_TIMES"));
				c.setLastAlertThreshold(rs.getDouble("LAST_ALERN_THRESHOLD"));
				c.setLastAlertVolume(rs.getDouble("LAST_ALERN_VOLUME"));
				c.setEverSuspend(("0".equals(rs.getString("EVER_SUSPEND"))?false:true));
				c.setLastFileId(rs.getInt("LAST_FILEID"));
				c.setLastDataTime(rs.getString("LAST_DATA_TIME"));
				c.setUpdateDate(rs.getString("UPDATE_DATE"));
				c.setCreateDate(rs.getString("CREATE_DATE"));
				list.add(c);
			}
			
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
			
		return list;
	}
	
	public List<CurrentDay> queryCurrentDay(String serviceId,String from,String to) throws Exception{

		List<CurrentDay> list = new ArrayList<CurrentDay>();

		String sql=
				"SELECT A.DAY,SUBSTR(MCCMNC,4)||'('||(case when B.NAME is not null then  B.NAME else substr(A.MCCMNC,0,3) end)||')' MCCMNC,A.SERVICEID,A.CHARGE,A.VOLUME,A.ALERT,A.LAST_FILEID  "
				+ ",TO_CHAR(A.LAST_DATA_TIME,'yyyy/MM/dd hh24:mi:ss') LAST_DATA_TIME,TO_CHAR(A.UPDATE_DATE,'yyyy/MM/dd hh24:mi:ss') UPDATE_DATE,TO_CHAR(A.CREATE_DATE,'yyyy/MM/dd hh24:mi:ss') CREATE_DATE "
				+ "FROM HUR_CURRENT_DAY A, HUR_CUSTOMER_SERVICE_PHONE B "
				+ "where substr(A.MCCMNC,0,3) = B.CODE(+) "
				+ (from!=null &&!"".equals(from)?"AND A.DAY >=  "+from+" ":"")
				+ (to!=null &&!"".equals(to)?"AND A.DAY <=  "+to+" ":"")
				//20150505 add
				+ (serviceId!=null && !"".equals(serviceId)?"AND A.SERVICEID in "+serviceId+" " : "")
				+ "ORDER BY A.LAST_DATA_TIME DESC ";

		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		conn = getConn1();
		try{
			st = conn.createStatement();
			System.out.println("query execute : "+sql);
			rs = st.executeQuery(sql);

			while(rs.next()){
				CurrentDay c = new CurrentDay();
				c.setDay(rs.getString("DAY"));
				c.setMccmnc(rs.getString("MCCMNC"));
				//c.setImsi(imsi);
				c.setCharge(FormatDouble(rs.getDouble("CHARGE"), "0.0000"));
				c.setVolume(rs.getDouble("VOLUME"));
				c.setAlert(("0".equals(rs.getString("ALERT"))?false:true));
				c.setLastFileId(rs.getInt("LAST_FILEID"));
				c.setLastDataTime(rs.getString("LAST_DATA_TIME"));
				c.setUpdateDate(rs.getString("UPDATE_DATE"));
				c.setCreateDate(rs.getString("CREATE_DATE"));
				list.add(c);
			}
			
		}finally{
			closeConnection(conn, st, ps, rs);
		}	
		return list;
		
	}

}
