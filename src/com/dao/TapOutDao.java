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

import com.bean.TabOutData;
import com.common.BaseDao;

@Repository
public class TapOutDao extends BaseDao{

	public TapOutDao() throws Exception {
		super();
	}

	public List<TabOutData> queryTapoutData(String from ,String to ,String serviceid,String type) throws Exception{
		List<TabOutData> result = new ArrayList<TabOutData>();
		
		String condition = ""
				+ (from==null || "".equals(from)?"":"and a.STARTTIME>= to_date('"+from+"','yyyy/mm/dd') ")
				+ (to==null || "".equals(to)?"":"and a.STARTTIME<= to_date('"+to+"','yyyy/mm/dd')+1 ")
				+ "and a.serviceid = "+serviceid+" ";
		
		String sql = "";
		
		//voice,MT 受話,MO 發話
		if("all".equalsIgnoreCase(type) || "voice".equalsIgnoreCase(type)) {
			sql+= "select to_char(STARTTIME,'yyyy/mm/dd') startDate, " + 
					"location, " + 
					"case direction when 'T' then caller when 'O' then a.callee else '' end phonenumber, " + 
					"case when location = 'FWTH' then 'RE' else " + 
					"     case direction when 'T' then Case when caller like '886%' then 'RC' else 'RA' end  " + 
					"                      when 'O' then 'R8' else '' end " + 
					"     end type, " + 
					"to_char(STARTTIME, 'hh24:mi:ss') startTime, " + 
					"to_char(STARTTIME+a.duration/86400, 'hh24:mi:ss') endTime, " + 
					"duration unit, " + 
					"round(AMOUNT,2) amount,   " + 
					"TOTAL_CHARGE,DISCOUNT_CHARGE,FINAL_CHARGE  " + 
					"from ( " + 
					"		select A.SERVICEID,A.IMSI,A.LOCATION,A.DIRECTION,A.CALLER,A.CALLEE,A.STARTTIME,A.DURATION,A.AMOUNT,B.TOTAL_CHARGE,B.DISCOUNT_CHARGE,B.FINAL_CHARGE " + 
					"		from TAPOUTFILEVOICEUSAGE A,(select * from TAPOUT_CDR where SERVICE_TYPE in  (0,78,79) )B " + 
					"		where A.serviceid = B.serviceid(+) " + 
					"		AND A.TELESERVICECODE is null " + 
					"		AND to_char(A.STARTTIME,'yyyyMMddhh24miss') = B.START_TIME(+) " + condition +
					"	union  " + 
					"		select A.SERVICEID,A.IMSI,A.LOCATION,A.DIRECTION,A.CALLER,A.CALLEE,A.STARTTIME,A.DURATION,A.AMOUNT,B.TOTAL_CHARGE,B.DISCOUNT_CHARGE,B.FINAL_CHARGE " + 
					"		from TAPOUTFILEVOICEUSAGE A,(select * from TAPOUT_CDR where SERVICE_TYPE in  (0) )B " + 
					"		where A.serviceid = B.serviceid(+) " + 
					"		AND A.TELESERVICECODE = 'T10' " + 
					"		AND to_char(A.STARTTIME,'yyyyMMddhh24miss') = B.START_TIME(+) " + condition +
					"	union " + 
					"		select A.SERVICEID,A.IMSI,A.LOCATION,A.DIRECTION,A.CALLER,A.CALLEE,A.STARTTIME,A.DURATION,A.AMOUNT,B.TOTAL_CHARGE,B.DISCOUNT_CHARGE,B.FINAL_CHARGE " + 
					"		from TAPOUTFILEVOICEUSAGE A,(select * from TAPOUT_CDR where SERVICE_TYPE in  (78,79) )B " + 
					"		where A.serviceid = B.serviceid(+) " + 
					"		AND A.TELESERVICECODE = 'T11' " + 
					"		AND to_char(A.STARTTIME,'yyyyMMddhh24miss') = B.START_TIME(+) "  + condition +
					"	) a WHERE 1 = 1 " + 
					"" ;
		}
		//簡訊
		if("all".equalsIgnoreCase(type) || "sms".equalsIgnoreCase(type)) {
			sql += (!"".equals(sql)?" UNION ":"");
			sql += "select to_char(a.STARTTIME,'yyyy/mm/dd') startDate," + 
					"a.location," + 
					"case a.direction when 'T' then a.caller when 'O' then a.callee else '' end phonenumber," + 
					"'MJ' type," + 
					"to_char(a.STARTTIME, 'hh24:mi:ss') startTime," + 
					"to_char(a.STARTTIME, 'hh24:mi:ss')  endTime," + 
					"1 unit," + 
					"round(a.AMOUNT,2) amount, " +
					"B.TOTAL_CHARGE,B.DISCOUNT_CHARGE,B.FINAL_CHARGE " +
					"from TAPOUTFILESMSUSAGE a, (select * from TAPOUT_CDR where SERVICE_TYPE in (3) ) B  " + 
					"where 1=1 " + 
					"AND A.SERVICEID = B.SERVICEID(+) AND to_char(A.STARTTIME,'yyyyMMddhh24miss') = B.START_TIME(+)  " +
					condition ;
			
		}
		//數據
		if("all".equalsIgnoreCase(type) || "data".equalsIgnoreCase(type)) {
			sql += (!"".equals(sql)?" UNION ":"");
			sql += "select to_char(a.STARTTIME,'yyyy/mm/dd') startDate," + 
					"a.location," + 
					"'' phonenumber," + 
					"'PC' type," + 
					"to_char(a.STARTTIME, 'hh24:mi:ss') startTime," + 
					"'' endTime," + 
					"a.chargeunit unit," + 
					"a.AMOUNT amount, " + 
					"B.TOTAL_CHARGE,B.DISCOUNT_CHARGE,B.FINAL_CHARGE " +
					"from TAPOUTFILEDATAUSAGE a, (select * from TAPOUT_CDR where SERVICE_TYPE in (53) ) B  " + 
					"where 1=1 " + 
					"AND A.SERVICEID = B.SERVICEID(+) AND to_char(A.STARTTIME,'yyyyMMddhh24miss') = B.START_TIME(+) " +
					condition ;
		}
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =  getConn1();
			st = conn.createStatement();
			rs = null;

			String countSql = "select count(1) cd from ("+sql+") ";
			
			System.out.println("SQL:"+countSql);
			rs=st.executeQuery(countSql);
			
			//避免筆數過大造成結果呈現緩慢，設定為2萬筆以下

			rs.next();
			if(rs.getInt("cd")>20000) {
				throw new Exception("請縮小範圍");
			}else if(rs.getInt("cd")==0) {
				return result;
			}
			
			//先撈取城市Map
			Map<String,String> tadigList = queryMccmnc();

			
			String querySql = "select * from ( "+sql+") order by startDate desc,startTime desc ";
			
			rs = null;
			System.out.println("SQL:"+querySql);
			rs=st.executeQuery(querySql);
			
			while(rs.next()){
				TabOutData c = new TabOutData();
				c.setStartDate(rs.getString("startDate"));
				c.setStartTime(rs.getString("startTime"));
				c.setEndTime(processData(rs.getString("endTime")));
				c.setPhonenumber(processData(rs.getString("phonenumber")));
				
				String location = rs.getString("location");
				c.setLocation((tadigList.get(location)!=null?tadigList.get(location):location));
				c.setType(rs.getString("type"));
				c.setUnit(rs.getString("unit"));
				c.setAmount(FormatDouble(rs.getDouble("amount"),"0.00"));
				
				//20180319 add
				if(rs.getString("TOTAL_CHARGE")!=null) {
					c.setTotalCharge(FormatDouble(rs.getDouble("TOTAL_CHARGE"),"0.00"));
					c.setDiscountCharge(FormatDouble(rs.getDouble("DISCOUNT_CHARGE"),"0.00"));
					c.setFinalCharge(FormatDouble(rs.getDouble("FINAL_CHARGE"),"0.00"));
				}else {
					c.setTotalCharge(" ");
					c.setDiscountCharge(" ");
					c.setFinalCharge(" ");
				}
				result.add(c);
			}
		} finally {
			closeConnection(conn, st, ps, rs);
		}
		
		
		
		return result;
	}
	
	public Map<String,String> queryMccmnc() throws Exception {
		Map<String,String> tadigList = new HashMap<String,String>();
		String sql = "select a.TADIG,a.COUNTRY " + 
				"from HUR_MCCMNC a ";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConn1();
			st = conn.createStatement();
			System.out.println("SQL:" + sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				tadigList.put(rs.getString("TADIG"), rs.getString("COUNTRY"));
			} 
		} finally {
			closeConnection(conn, st, ps, rs);
		}
		return tadigList;
	}
	
}
