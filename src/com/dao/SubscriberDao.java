package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.bean.PricePlanID;
import com.bean.Subscriber;
import com.common.BaseDao;

@Repository
public class SubscriberDao extends BaseDao{

	public SubscriberDao() throws Exception {
		super();
	}
	
	/**
	 * 以CRM ID查詢客戶清單
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Set<String> queryListByIdTaxid(String id) throws Exception{
		Set<String> serviceids = new HashSet<String>();
		//CRM DB
		String sql ="SELECT  A.SUBS_ID_TAXID ID,A.SUBS_NAME,A.SUBS_PERMANENT_ADDRESS,B.SERVICEID "
				+ "FROM  CRM_DB.CRM_SUBSCRIBERS A LEFT JOIN CRM_DB.CRM_SUBSCRIPTION B ON A.SEQ =B.SEQ "
				+ "WHERE A.SUBS_ID_TAXID = ? ";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			conn = getConn3();
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			
			while(rs.next()){
				serviceids.add(rs.getString("SERVICEID"));
			}		
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		return serviceids;
	}
	
	/**
	 * 以香港號查詢客戶清單
	 * @param s2tMsisdn
	 * @return
	 * @throws Exception
	 */
	public List<Subscriber> queryListByS2tMisidn(String s2tMsisdn) throws Exception{
		List<Subscriber> result = new ArrayList<Subscriber>();

		String serviceid = queryServiceIdbyS2tMsisdn(s2tMsisdn,false);
		if(serviceid!= null &&!"".equals(serviceid))
			result.add(queryDataByServiceId(serviceid));
		return result;
	}
	
	/**
	 * 以香港IMSI查詢客戶清單
	 * @param s2tIMSI
	 * @return
	 * @throws Exception
	 */
	public List<Subscriber> queryListByS2tIMSI(String s2tIMSI) throws Exception{
		List<Subscriber> result = new ArrayList<Subscriber>();
		String serviceid = queryServiceIdbyS2tImsi(s2tIMSI);
		if(serviceid!= null &&!"".equals(serviceid))
			result.add(queryDataByServiceId(serviceid));
		return result;
	}
	
	/**
	 * 以VLN查詢客戶清單
	 * @param VLN
	 * @return
	 * @throws Exception
	 */
	public List<Subscriber> queryListByVLN(String VLN) throws Exception{
		List<Subscriber> result = new ArrayList<Subscriber>();

		String serviceId = queryServiceidByVLN(VLN);

			if(serviceId != null &&!"".equals(serviceId))
				result.add(queryDataByServiceId(serviceId));

		return result;
	}
	
	/**
	 * 以VLN查詢Serviceid
	 * @param VLN
	 * @return
	 * @throws Exception
	 */
	
	public String queryServiceidByVLN(String VLN) throws Exception{
		
		String sql = "SELECT A.SERVICEID, A.VLN "
				+ "FROM VLNNUMBER A "
				+ "WHERE A.vlnid <> A.serviceid AND A.VLN = ? ";
		String serviceid = null;

		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			conn = getConn1();
			ps = conn.prepareStatement(sql);
			ps.setString(1, VLN);
			rs = ps.executeQuery();
			
			while(rs.next()){
				serviceid = rs.getString("SERVICEID");
			}
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		return serviceid;
	}
	
	
	
	/**
	 * 以中華號查詢客戶清單
	 * @param chtMsisdn
	 * @return
	 * @throws Exception
	 */
	public List<Subscriber> queryListByHomeMsisdn(String homeMsisdn) throws Exception{
		List<Subscriber> result = new ArrayList<Subscriber>();
		
			Subscriber s = queryServiceIdbyHomeMsisdn(homeMsisdn);
			//String serviceid = queryServiceIdbyChtMsisdn(chtMsisdn);
			if(s.getServiceId()!= null &&!"".equals(s.getServiceId())) {
				Subscriber s2 = queryDataByServiceId(s.getServiceId());
				if(s2.getChtMsisdn()==null||"".equals(s2.getChtMsisdn().trim()))
					s2.setChtMsisdn(s.getChtMsisdn());
				
				if(s2.getHomeIMSI()==null||"".equals(s2.getHomeIMSI().trim()))
					s2.setHomeIMSI(s.getHomeIMSI());

				if(s2.getS2tIMSI()==null||"".equals(s2.getS2tIMSI().trim()))
					s2.setS2tIMSI(s.getS2tIMSI());
				
				if(s2.getS2tMsisdn()==null||"".equals(s2.getS2tMsisdn().trim()))
					s2.setS2tMsisdn(s.getS2tMsisdn());
				result.add(s2);
			}
		return result;
	}
	
	
	
	//***********************************************************************************//
	
	public String queryServiceIdbyS2tMsisdn(String s2tMsisdn,boolean isTeminated) throws Exception{
		
		String serviceid = null;
		
		String sql1 = " SELECT MAX(SERVICEID) SERVICEID from service where SERVICECODE = ? "
				+ (isTeminated?"and DateCanceled is not null":"and DateCanceled is null ");
		
		
		String sql2 = "SELECT MAX(A.SERVICEID) SERVICEID "
					+ "FROM SERVICE A "
					+ "where A.SERVICECODE = ? ";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		

		try {
			conn = getConn1();
			ps = conn.prepareStatement(sql1);
			ps.setString(1, s2tMsisdn);
			rs = ps.executeQuery();
			
			while(rs.next()){
				serviceid = rs.getString("SERVICEID");
			}
			
			if(serviceid==null||"".equals(serviceid)){
				rs = null;
				
				ps = conn.prepareStatement(sql2);
				ps.setString(1, s2tMsisdn);
				rs = ps.executeQuery();
				
				while(rs.next()){
					serviceid = rs.getString("SERVICEID");
				}
			}

		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return serviceid;
	}

	public String queryServiceIdbyS2tImsi(String s2tImsi) throws Exception{
		String serviceid = null;
		
		String sql = null;
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConn1();
			sql = "select MAX(A.SERVICEID)  SERVICEID "
					+ "from imsi A "
					+ "where imsi = ? ";

			ps = conn.prepareStatement(sql);
			ps.setString(1, s2tImsi);
			rs = ps.executeQuery();

			while(rs.next()){
				serviceid = rs.getString("SERVICEID");
			}
			
			if(serviceid == null){
				
				rs.close();
				
				sql = "Select  A.S2T_MSISDN,A.S2T_IMSI "
						+ "from S2T_TB_TYPB_WO_SYNC_FILE_DTL A "
						+ "where A.S2T_IMSI like '%"+s2tImsi+"%' "
						+ "AND trim(A.S2T_MSISDN) is not null "
						+ "order by A.WORK_ORDER_NBR ";
				
				st = conn.createStatement();
				System.out.println("sql:"+sql);
				rs = st.executeQuery(sql);
				
				String s2tMsisdn = null;
				while(rs.next()){
					s2tMsisdn = rs.getString("S2T_MSISDN");
				}
				
				serviceid = queryServiceIdbyS2tMsisdn(s2tMsisdn,true);
			}
	
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		return serviceid;
	}
	
	public Subscriber queryServiceIdbyHomeMsisdn(String homeMsisdn) throws Exception{
		Subscriber s = new Subscriber();
		
		if(homeMsisdn.matches("^886.+")) {
			System.out.println("886配對成功");
			s.setChtMsisdn(homeMsisdn); 
		}
		
		String serviceid = null;

		//查詢現行資料
		String sql = "select A.SERVICEID "
					+ "from FOLLOWMEDATA A "
					+ "where A.FOLLOWMENUMBER = ? ";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		
		try {
			conn = getConn1();
			st = conn.createStatement();
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, homeMsisdn);
			rs = ps.executeQuery();
			
			while(rs.next()){
				serviceid = rs.getString("SERVICEID");
				s.setServiceId(serviceid);
			}
			
			if(serviceid == null){
	
				String time = null;
				String workType = null;

				//20180118 add
				sql = "select to_char(REQTIME,'yyyyMMddhh24miss') TIME,"
						+ "SUBSTR(A.content,instr(content,'S2T_IMSI=')+length('S2T_IMSI='),15) S2T_IMSI,"
						+ "SUBSTR(A.content,instr(content,'TWNLD_IMSI=')+length('TWNLD_IMSI='),15) TWNLD_IMSI,"
						+ "SUBSTR(A.content,instr(content,'Req_Status=')+length('Req_Status='),2) WORK_TYPE " 
						+ "from provlog A "
						+ "where content like '%TWNLD_MSISDN='||?||'%' "
						+ "order by REQTIME DESC";
				String s2tMsisdn = null;
				String s2tIMSI = null;
				String homeIMSI = null;
				
				rs = null;
				
				ps = conn.prepareStatement(sql);
				ps.setString(1, homeMsisdn);
				rs = ps.executeQuery();
	
				//PROVLOG 查的到資料，環球卡用戶
				if(rs.next()){
					System.out.println("IS CHT USER.");
					time = rs.getString("TIME");
					workType = rs.getString("WORK_TYPE");
					s2tIMSI = rs.getString("S2T_IMSI");
					homeIMSI = rs.getString("TWNLD_IMSI");
					
					System.out.println("workType:"+workType);
					//20170921 確認是否換過號
					int count = 0;
					while(!"99".equals(workType)&&!"0".equals(workType)&&count<=10) {
						count++; //防無限迴圈
						rs = null;
						workType = "0";
						//查詢是否同IMSI之後是否有05換中華號
						//20180118 add
						sql = "select to_char(REQTIME,'yyyyMMddhh24miss') TIME,"
								+ "SUBSTR(A.content,instr(content,'S2T_IMSI=')+length('S2T_IMSI='),15) S2T_IMSI,"
								+ "SUBSTR(A.content,instr(content,'Req_Status=')+length('Req_Status='),2) WORK_TYPE, " 
								+ "SUBSTR(A.content,instr(content,'TWNLD_MSISDN=')+length('TWNLD_MSISDN='),12) TWNLD_MSISDN "
								+ "from provlog A "
								+ "where content like '%"+s2tIMSI+"%' "
								+ "and A.REQTIME > to_date('"+time+"','yyyyMMddhh24miss')" 
								+ "order by REQTIME ";
						
						rs = st.executeQuery(sql);

						if(rs.next()) {
							time = rs.getString("TIME");
							workType = rs.getString("WORK_TYPE");
							homeMsisdn = rs.getString("TWNLD_MSISDN");
						}
						System.out.println("workType:"+workType);
						//如果同imsi之後的第一筆為05，表示有換號，重新查詢是否退租
						if("05".equals(workType)) {
							rs = null;
							workType = "0";
							//20180118 add
							sql = "select to_char(REQTIME,'yyyyMMddhh24miss') TIME,"
									+ "SUBSTR(A.content,instr(content,'S2T_IMSI=')+length('S2T_IMSI='),15) S2T_IMSI,"
									+ "SUBSTR(A.content,instr(content,'Req_Status=')+length('Req_Status='),2) WORK_TYPE " 
									+ "from provlog A "
									+ "where content like '%'||?||'%' "
									+ "and A.REQTIME > to_date('"+time+"','yyyyMMddhh24miss')" 
									+ "order by REQTIME DESC";
							
							ps = conn.prepareStatement(sql);
							ps.setString(1, homeMsisdn);
							rs = ps.executeQuery();
							
							
							if(rs.next()){
								time = rs.getString("TIME");
								workType = rs.getString("WORK_TYPE");
								s2tIMSI = rs.getString("S2T_IMSI");
							}
						}
					}
					//查詢在最後一張工單之後所獲得的S2TMSISDN
					rs = null;
					
					//20180118 add 查詢Service
					String sql2 = "Select  A.S2T_MSISDN,A.S2T_IMSI,to_char(A.CMCC_OPERATIONDATE,'yyyyMMddhh24miss') TIME,A.WORK_TYPE,A.ORIGINAL_CMCC_MSISDN " + 
							"from S2T_TB_TYPB_WO_SYNC_FILE_DTL A " + 
							"where A.S2T_IMSI = '"+s2tIMSI+"' " + 
							"and A.CMCC_OPERATIONDATE <= to_date('"+time+"','yyyyMMddhh24miss')" + 
							"order by A.WORK_ORDER_NBR DESC";
					
					System.out.println("sql:"+sql2);
					rs = st.executeQuery(sql2);
					
					if(rs.next()) {
						s2tMsisdn = rs.getString("S2T_MSISDN");
					}
					
					if(s2tMsisdn!=null) {
						rs = null;
						
						sql = "select A.SERVICEID "
								+ "from service A "
								+ "where A.servicecode = '"+s2tMsisdn+"' "
								+ "and A.DATEACTIVATED<=to_date('"+time+"','yyyyMMddhh24miss') "
								+ "and "+("99".equals(workType)?"A.DATECANCELED>=to_date('"+time+"','yyyyMMddhh24miss')" : "A.DATECANCELED is null");				
						System.out.println(sql);
						rs = st.executeQuery(sql);
						
						while(rs.next()){
							serviceid = rs.getString("SERVICEID");
							
						}
					}
				//非環球卡用戶	
				}else {
					System.out.println("NOT CHT USER.");
					serviceid = queryServiceidByVLN(homeMsisdn);
				}
				s.setServiceId(serviceid);
			}

		}finally{
			closeConnection(conn, st, ps, rs);
		}
		return s;
	}
	
	/**
	 * 以護照ID查詢客戶清單
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Subscriber> queryListByPassPortId(String id) throws Exception{
		List<Subscriber> result = new ArrayList<Subscriber>();
		Set<String> serviceids = new HashSet<String>();
		//CRM DB
		String sql ="SELECT  A.SUBS_ID_TAXID ID,A.SUBS_NAME,A.SUBS_PERMANENT_ADDRESS,B.SERVICEID "
				+ "FROM  CRM_DB.CRM_SUBSCRIBERS A LEFT JOIN CRM_DB.CRM_SUBSCRIPTION B ON A.SEQ =B.SEQ "
				+ "WHERE B.PASSPORT_ID = ? ";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			conn = getConn3();
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			
			while(rs.next()){
				serviceids.add(rs.getString("SERVICEID"));
			}		
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		for(String serviceid:serviceids){
			if(!"".equals(serviceid))
				result.add(queryDataByServiceId(serviceid));
		}
	
		return result;
	}
	
	/**
	 * 以名稱查詢客戶清單
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Set<String>  queryListByName(String name) throws Exception{
		
		Set<String> serviceids = new HashSet<String>();
		//CRM DB
		String sql1 ="SELECT  A.SUBS_ID_TAXID ID,A.SUBS_NAME,A.SUBS_PERMANENT_ADDRESS,B.SERVICEID "
				+ "FROM  CRM_DB.CRM_SUBSCRIBERS A LEFT JOIN CRM_DB.CRM_SUBSCRIPTION B ON A.SEQ =B.SEQ "
				+ "WHERE A.SUBS_NAME LIKE '%"+name+"%' ";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			conn = getConn3();
			st = conn.createStatement();
			//System.out.println("query serviceids:"+sql1);
			rs = st.executeQuery(sql1);
			
			while(rs.next()){
				serviceids.add(rs.getString("SERVICEID"));
			}		
		}finally{
			closeConnection(conn, st, ps, rs);
		}

		return serviceids;
	}
	
	/**
	 * 以ServiceID查詢客戶資料，主ServiceID
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Subscriber queryDataByServiceId(String id) throws Exception{
		
		if(id==null || "".equals(id)) 
			return null;
		
		Subscriber result = new Subscriber(); 
		//設定Serviceid
		result.setServiceId(id);

		//查詢Subscriber
		querySubscriberData(result);
		//查詢Service
		queryServiceData(result);
		String priceplanid = result.getPricePlanId().getId();
		boolean isCanceled = result.getCanceledDate()!=null;
	
		//查詢詳細的Priceplan
		if(priceplanid!=null) {
			result.setPricePlanId(queryPricePlanId(priceplanid));
		}
			
		//查詢實際的退租時間
		if(isCanceled) {
			result.setCanceledDate(queryRealEndTime(result));
		}else {
			result.setCanceledDate("");
		}
		
	
	
		//20180621 add 避免複雜，只有環球卡顯示中華號
		if("139".equals(priceplanid)) {
			String chtMsisdn = queryCHTMsisdn(result);
			if(chtMsisdn != null && !"".equals(chtMsisdn))
				result.setChtMsisdn(chtMsisdn);
		}

		//取得記錄的門號有沒有被使用
		result.setNowS2tActivated(queryS2TMsisdnNowStatus(result.getS2tMsisdn()));
		//取得VLN
		result.setVlns(queryVLN(id));

		return result;
	}
	
	private Subscriber querySubscriberData(Subscriber result) throws Exception{
		String sql;
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = getConn3();
			st = conn.createStatement();
			//從CRM_DB查詢用戶資料
			sql="SELECT A.SUBS_ID_TAXID ID,A.SUBS_NAME,A.SUBS_BIRTHDAY,A.SUBS_PHONE,A.SUBS_EMAIL,A.SEQ, "
					+ "A.SUBS_PERMANENT_ADDRESS,A.SUBS_BILLING_ADDRESS,A.AGENCY_ID,A.REMARK, "
					+ "DATE_FORMAT(A.CREATETIME,'%Y/%m/%d %H:%m:%s') CREATETIME,DATE_FORMAT(A.UPDATETIME,'%Y/%m/%d %H:%m:%s') UPDATETIME, "
					+ "A.SUBS_TYPE,B. SERVICEID,E.CHAIRMAN,E.CHAIRMAN_ID,B.PASSPORT_ID,B.PASSPORT_NAME "
					+ "FROM CRM_DB.CRM_SUBSCRIBERS A left join CRM_DB.CRM_SUBSCRIPTION B on A.seq =B.seq left join CRM_DB.CRM_CHAIRMAN E on A.seq = E.seq "
					+ "WHERE B.SERVICEID = "+result.getServiceId()+" ";
			
			rs= null;
			System.out.println("SQL:"+sql);
			rs = st.executeQuery(sql);
			while(rs.next()){

				result.setName(rs.getString("SUBS_NAME"));
				result.setIdTaxid(processData(rs.getString("ID")));
				result.setBirthday(processData(rs.getString("SUBS_BIRTHDAY")));
				result.setPhone(processData(rs.getString("SUBS_PHONE")));
				result.setEmail(processData(rs.getString("SUBS_EMAIL")));
				result.setPermanentAddress(rs.getString("SUBS_PERMANENT_ADDRESS"));
				result.setBillingAddress(rs.getString("SUBS_BILLING_ADDRESS"));
				result.setAgency(rs.getString("AGENCY_ID"));
				result.setRemark(rs.getString("REMARK"));
				result.setCreatetime(processData(rs.getString("CREATETIME")));
				result.setUpdatetime(processData(rs.getString("UPDATETIME")));
				result.setType(processData(rs.getString("SUBS_TYPE")));
	
				result.setChair(rs.getString("CHAIRMAN"));
				result.setChairId(rs.getString("CHAIRMAN_ID"));
				
				//20170317 add
				result.setPassportId(rs.getString("PASSPORT_ID"));
				result.setPassportName(rs.getString("PASSPORT_NAME"));
	
				result.setSeq(rs.getString("SEQ"));
			}
		} finally{
			closeConnection(conn, st, ps, rs);
		}
	
		return result;
	}
	
	private Subscriber queryServiceData(Subscriber result) throws Exception{
		String sql;
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			
			sql = ""
					+ "SELECT C.SERVICEID, "
					+ " 				(CASE C.STATUS WHEN '1' THEN 'normal'  "
					+ "					WHEN '3' THEN 'suspended' "
					+ "					WHEN '4' THEN 'terminated' "
					+ "					WHEN '10' THEN 'waiting' "
					+ "					ELSE 'else' END) STAUS, "
					+ "					C. SERVICECODE S2TMSISDN,C.PRICEPLANID, "
					+ "					to_char(C.DATECREATED,'yyyy/MM/dd hh24:mi:ss') DATEACTIVATED,nvl(to_char(C.DATECANCELED,'yyyy/MM/dd hh24:mi:ss'),'') DATECANCELED,F.HOMEIMSI,F.IMSI "
					+ "FROM SERVICE C,IMSI F "
					+ "WHERE C.SERVICEID = F.SERVICEID(+) AND C.SERVICEID = "+result.getServiceId()+" ";
			conn = getConn1();
			st = conn.createStatement();
			//System.out.println("Execute SQL:"+sql);
			rs = null;
			rs = st.executeQuery(sql);
			
			
			while(rs.next()){
				//result.setServiceId(rs.getString("SERVICEID"));
				result.setS2tMsisdn(rs.getString("S2TMSISDN"));
				result.setS2tIMSI(rs.getString("IMSI"));
				//result.setChtMsisdn(rs.getString("CHTMSISDN"));
				//princePlanId= rs.getString("PRICEPLANID");
				
				PricePlanID priceplanid = new PricePlanID();
				priceplanid.setId(rs.getString("PRICEPLANID"));
				result.setPricePlanId(priceplanid);				
				result.setStatus(rs.getString("STAUS"));
				
				result.setActivatedDate(rs.getString("DATEACTIVATED"));
				//result.setCanceledDate(rs.getString("DATECANCELED"));
				//20180202
				result.setCanceledDate(rs.getString("DATECANCELED"));
				
				result.setHomeIMSI(rs.getString("HOMEIMSI"));
			}
		} finally{
			closeConnection(conn, st, ps, rs);
		}
	
		return result;
	}
	
	
	private String queryRealEndTime(Subscriber result) throws Exception{
		String sql;
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String datecanceled = null;
		
		//20170110
		sql = "SELECT A.SERVICEID, A.SERVICECODE, A.STATUS, "
				+ "to_char(A.DATEACTIVATED,'yyyy/MM/dd hh24:mi:ss') DATEACTIVATED,nvl(to_char(B.COMPLETEDATE,'yyyy/MM/dd hh24:mi:ss'),'') DATECANCELED "
				+ "FROM SERVICE A, TERMINATIONORDER B "
				+ "WHERE A.SERVICEID=B.TERMOBJID and serviceid = "+result.getServiceId()+" ";
		try {
			conn = getConn2();
			st = conn.createStatement();
			rs = null;
			rs = st.executeQuery(sql);
			System.out.println("Execute SQL:"+sql);
			if(rs.next()){
				//只有已退租才會記錄
				//result.setActivatedDate(rs.getString("DATEACTIVATED"));
				datecanceled = rs.getString("DATECANCELED");
			}
		} finally{
			closeConnection(conn, st, ps, rs);
		}
	
		return datecanceled;
	}
	
	
	private String queryCHTMsisdn(Subscriber result) throws Exception{
		String sql;
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String chtMsisdn = null;
		try {
			
			//20170310 add 查詢CHTMSISDN
			//20170510 mod 以環球卡Table查詢
			
			sql = "select count(1) cd from FOLLOWMEDATA where serviceid = "+result.getServiceId()+" AND Followmenumber like '886%' ";
			conn = getConn1();
			st = conn.createStatement();
			
			rs = null;
			
			System.out.println("Execute SQL:"+sql);
			rs = st.executeQuery(sql);
			boolean onlyOne886FollowMeNumber = false;
			if(rs.next()) {
				onlyOne886FollowMeNumber = rs.getInt("cd")==1;
			}
			
			if(onlyOne886FollowMeNumber) {
				rs = null;
				sql = "select Followmenumber from FOLLOWMEDATA A where serviceid = "+result.getServiceId()+" AND Followmenumber like '886%' ";

				
				System.out.println("Execute SQL:"+sql);
				rs = st.executeQuery(sql);
				
				if(rs.next()){
					chtMsisdn = rs.getString("Followmenumber");
				}
			}
			
			
			
			if(chtMsisdn == null || "".equals(chtMsisdn)){
				sql = "select Replace(nvl(A.FORWARD_TO_HOME_NO,A.FORWARD_TO_S2T_NO_1),'+','') MSISDN "
						+ "from S2T_TB_TYPB_WO_SYNC_FILE_DTL A "
						+ "where A.S2T_MSISDN = '"+result.getS2tMsisdn()+"' "
						+ "and A.S2T_OPERATIONDATE > to_date('"+result.getActivatedDate()+"','yyyy/MM/dd hh24:mi:ss') "
						+ (result.getCanceledDate() == null || "".equals(result.getCanceledDate() ) ? "" : "AND A.S2T_OPERATIONDATE < to_date('"+result.getCanceledDate()+"','yyyy/MM/dd hh24:mi:ss') " )
						+ "order by A.S2T_OPERATIONDATE DESC";
				
				rs = null;
				System.out.println("Execute SQL:"+sql);
				rs = st.executeQuery(sql);
				if(rs.next()){
					chtMsisdn = rs.getString("MSISDN");
				}
			}		
			
		} finally{
			closeConnection(conn, st, ps, rs);
		}
	
		return chtMsisdn;
	}
	
	private String queryS2TMsisdnNowStatus(String S2TMsisdn) throws Exception{
		String result = "0"; 
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =getConn1();
			st = conn.createStatement();
			
			String sql = "select count(1) CD from service where servicecode='"+S2TMsisdn+"' and datecanceled is null ";
			System.out.println("sql : "+sql);
			rs = null;
			rs =  st.executeQuery(sql);
			if(rs.next()){
				result = rs.getString("CD");
			}

		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return result;
	}	
	
	
	public PricePlanID queryPricePlanId(String id) throws SQLException, Exception{
		PricePlanID p = new PricePlanID();
		p.setId(id);
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = getConn1();
		String sql = "SELECT A.EFFECTIVITY,  A.PRICEPLANID,  A.NAME,  A.ALIASES,  A.PRODUCTION,  A.DESCRIPTION "
				+ "FROM PRICEPLAN_DETAIL A "
				+ "WHERE A.PRICEPLANID = "+id+" ";
		try {
						
			st = conn.createStatement();
			System.out.println("query serviceId detail:"+sql);
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				p.setEffectivity(rs.getString("EFFECTIVITY"));
				p.setAliases(processData(rs.getString("ALIASES"),"ISO-8859-1","BIG5"));
				p.setDesc(processData(rs.getString("DESCRIPTION"),"ISO-8859-1","BIG5"));
				
				p.setName(processData(rs.getString("NAME"),"ISO-8859-1","BIG5"));
				p.setProduction(processData(rs.getString("PRODUCTION"),"ISO-8859-1","BIG5"));
			}
		} finally{
			closeConnection(conn, st, ps, rs);
		}
		
		return p;
	}

	/**
	 * 查詢VLN
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */
	public List<String> queryVLN(String serviceId) throws Exception{
		List<String> result = new ArrayList<String>(); 

		String sql = "SELECT A.VLN||'('||(case A.vlntype when '1' then 'static' when '0' then 'dynamic' else '' end)||')' VLN "
				+ "FROM VLNNUMBER A "
				+ "WHERE A.status = 1 AND A.SERVICEID = '"+serviceId+"' ";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConn1();
			st = conn.createStatement();
			//System.out.println("sql:"+sql);
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				result.add(rs.getString("VLN"));
			}
		} finally{
			closeConnection(conn, st, ps, rs);
		}
		return result;
	}
	
	
	public Subscriber addOrUpdate(Subscriber s) throws Exception {
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConn3();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			
			updateSubscriber(s);
			//主要是更新護照資料
			updateSubscription(s);
			
			if("E".equalsIgnoreCase(s.getType())) {
				updateChair(s);
			}else if("P".equalsIgnoreCase(s.getType())){
				deleteChair(s);
			}
			
		} finally {
			conn.setAutoCommit(true);
			closeConnection(conn, st, ps, rs);
		}
		
		return queryDataByServiceId(s.getServiceId());
	}
	
	public void addSubscriber(Subscriber s) throws Exception {
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql = "SELECT MAX(A.seq)+1 SEQ "
				+ "FROM CRM_DB.CRM_SUBSCRIBERS A ";
		
		try {
			conn = getConn3();
			st = conn.createStatement();
			System.out.println("sql:" + sql);
			rs = st.executeQuery(sql);
			if (rs.next()) {
				s.setSeq(rs.getString("SEQ"));
			}
			if (s.getSeq() == null || "".equals(s.getSeq()))
				throw new Exception("無法取得Subscriber SEQ");
			sql = "INSERT INTO " + "CRM_DB.CRM_SUBSCRIBERS("
					+ "SUBS_NAME, SUBS_BIRTHDAY, SUBS_ID_TAXID, SUBS_PHONE, SUBS_EMAIL,"
					+ "SUBS_PERMANENT_ADDRESS,SUBS_BILLING_ADDRESS,AGENCY_ID,REMARK," + "CREATETIME,SUBS_TYPE,SEQ"
					+ ") " + "VALUES(" + "'" + s.getName() + "','" + s.getBirthday() + "','" + s.getIdTaxid() + "','"
					+ s.getPhone() + "','" + s.getEmail() + "'," + "'" + s.getPermanentAddress() + "','"
					+ s.getBillingAddress() + "','" + s.getAgency() + "','" + s.getRemark() + "'," + "now(),'"
					+ s.getType() + "','" + s.getSeq() + "' " + ") ";
			System.out.println("sql:" + sql);
			int eRow = st.executeUpdate(sql);
			if (eRow != 1)
				throw new Exception("Insert Subscriber fail! InsertSubscriber " + eRow);
			
			
		} finally {
			closeConnection(conn, st, ps, rs);
		}

	}
	
	public void addSubscription(Subscriber s) throws Exception {
		String sql = "INSERT INTO "
				+ "CRM_DB.CRM_SUBSCRIPTION("
				+ "SERVICEID,SEQ,CREATETIME"
				+ ",PASSPORT_ID,PASSPORT_NAME"
				+ ") "
				+ "VALUES("
				+ " '"+s.getServiceId()+"','"+s.getSeq()+"',now()"
				+",'"+s.getPassportId()+"','"+s.getPassportName()+"' "
				+ ") ";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//System.out.println("sql:"+sql);
			conn = getConn3();
			st = conn.createStatement();
			int eRow = st.executeUpdate(sql);
			if (eRow != 1)
				throw new Exception("insert Subscription fail!  " + eRow);
			
			addApplication(s);
		} finally {
			closeConnection(conn, st, ps, rs);
		}
	}
	
	public void addChair(Subscriber s) throws Exception {
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "INSERT INTO CRM_DB.CRM_CHAIRMAN(SEQ,CHAIRMAN,CHAIRMAN_ID,CREATETIME) "
				+ "VALUES('"+s.getSeq()+"','"+s.getChair()+"','"+s.getChairId()+"',now())";

		try {
			conn = getConn3();
			st = conn.createStatement();
			System.out.println("sql:" + sql);
			st.executeUpdate(sql);
		} finally {
			closeConnection(conn, st, ps, rs);
		}
	}
	
	public void addApplication(Subscriber s) throws Exception {
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "INSERT INTO CRM_APPLICATION(SERVICEID,VERIFIED_DATE,CREATETIME,TYPE) "
				+ "VALUES("+s.getServiceId()+",now(),now(),'供裝')";
		try {
			conn = getConn3();
			st =conn.createStatement();
			System.out.println("sql:"+sql);
			int r = st.executeUpdate(sql);
		} finally{
			closeConnection(conn, st, ps, rs);
		}
	}
	
	public void updateSubscriber(Subscriber s) throws Exception {
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql = "UPDATE CRM_DB.CRM_SUBSCRIBERS A "
				+ "SET A.SUBS_ID_TAXID='"+s.getIdTaxid()+"',A.SUBS_NAME='"+s.getName()+"',A.SUBS_BIRTHDAY='"+s.getBirthday()+"'  "
				+ ",A.SUBS_PHONE='"+s.getPhone()+"',A.SUBS_EMAIL='"+s.getEmail()+"'  "
				+ ",A.SUBS_PERMANENT_ADDRESS='"+s.getPermanentAddress()+"',A.SUBS_BILLING_ADDRESS='"+s.getBillingAddress()+"'  "
				+ ",A.AGENCY_ID='"+s.getAgency()+"',A.REMARK='"+s.getRemark()+"',A.UPDATETIME=now() ,A.SUBS_TYPE = '"+s.getType()+"'  "
				+ "WHERE A.SEQ ='"+s.getSeq()+"'";

		try {
			conn = getConn3();
			st = conn.createStatement();
			System.out.println("sql:" + sql);
			int eRow = st.executeUpdate(sql);
			if (eRow == 0 )
				addSubscriber(s);
		} finally {
			closeConnection(conn, st, ps, rs);
		}
			
	}
	
	public void updateSubscription(Subscriber s) throws Exception {
		
		String sql = "UPDATE CRM_DB.CRM_SUBSCRIPTION A "
				+ "SET A.SEQ = '"+s.getSeq()+"',A.UPDATETIME = now() "
				+ ",A.PASSPORT_ID = '"+s.getPassportId()+"',A.PASSPORT_NAME = '"+s.getPassportName()+"'  "
				+ "WHERE A.SERVICEID = '"+s.getServiceId()+"' ";
		
		
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConn3();
			st = conn.createStatement();
			System.out.println("sql:" + sql);
			int eRow = st.executeUpdate(sql);
			if (eRow == 0)
				addSubscription(s);
		} finally {
			closeConnection(conn, st, ps, rs);
		}
	}
	
	public void updateChair(Subscriber s) throws Exception {
		String sql = "UPDATE CRM_DB.CRM_CHAIRMAN A "
				+ "SET A.CHAIRMAN = '"+s.getChair()+"', A.CHAIRMAN_ID = '"+s.getChairId()+"' "
				+ "WHERE A.SEQ = '"+s.getSeq()+"'";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConn3();
			st = conn.createStatement();
			System.out.println("sql:" + sql);
			int eRow = st.executeUpdate(sql);
			if (eRow == 0)
				addChair(s);
			else if (eRow != 1)
				throw new Exception("Update data fail! updated ChairMain " + eRow);
		} finally {
			closeConnection(conn, st, ps, rs);
		}
	}
	
	public void deleteChair(Subscriber s) throws Exception {
		String sql = "DELETE FROM CRM_DB.CRM_CHAIRMAN "
				+ "WHERE SEQ = '"+s.getSeq()+"'";
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConn3();
			st = conn.createStatement();
			System.out.println("sql:" + sql);
			int eRow = st.executeUpdate(sql);
		} finally {
			closeConnection(conn, st, ps, rs);
		}
		
	}
	
	
	public List<Map<String,String>>  querySubscribersExcel(String dateS,String dateE) throws Exception{
		Connection conn=null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
	
		//主Service Table，以供裝時間為撈取條件
		Set<String> serviceidSet = new HashSet<String>();
		Map<String, Map<String,String>> servicedMap = new HashMap<String,Map<String,String>>();
		try{
			System.out.println("query service data!");
			
			String sql = "SELECT F.PARTNERMSISDN,E.SERVICECODE,E.SSERVICECODE,E.DATEACTIVATED,E.DATECANCELED DC,E.SERVICEID,E.PRICEPLANID ,nvl(to_char(G.COMPLETEDATE,'yyyy/MM/dd hh24:mi:ss'),'') DATECANCELED  "
					+ "FROM AVAILABLEMSISDN F, "
					+ "		(	SELECT A.SERVICEID,A.SERVICECODE ||(CASE WHEN A.STATUS=4 or A.STATUS=10 THEN '  (TERMINATED)' END) SSERVICECODE,A.SERVICECODE, "
					+ "						A.DATEACTIVATED,A.DATECANCELED,A.STATUS s,A.PRICEPLANID  "
					+ "			FROM SERVICE A , "
					+ "					(	SELECT B.SERVICEID,MAX(B.DATEACTIVATED) DATEACTIVATED  "
					+ "						FROM SERVICE B GROUP BY B.SERVICEID) B  "
					+ "						WHERE A.SERVICEID = B.SERVICEID AND A.DATEACTIVATED = B.DATEACTIVATED AND length(SERVICECODE)<=11 ) E , TERMINATIONORDER G "
					+ "WHERE E.SERVICECODE = F.S2TMSISDN(+) AND E.SERVICEID=G.TERMOBJID(+) "
					+ (dateS != null && !"".equals(dateS) ? "AND to_char(E.DATEACTIVATED,'yyyyMM') >= '"+dateS+"'" : "" ) 
					+ (dateE != null && !"".equals(dateE) ? "AND to_char(E.DATEACTIVATED,'yyyyMM') <= '"+dateE+"'" : "" )
					+ "";

			conn = getConn1();
			st = conn.createStatement();
			System.out.println("Execute SQL :"+sql);
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				String serviceid = rs.getString("SERVICEID");
				Map<String,String>m = new HashMap<String,String>();
				m.put("SERVICEID", serviceid);
				m.put("PARTNERMSISDN", rs.getString("PARTNERMSISDN"));
				m.put("SERVICECODE", rs.getString("SSERVICECODE"));
				m.put("DATEACTIVATED", rs.getString("DATEACTIVATED"));
				m.put("DATECANCELED", rs.getString("DATECANCELED"));
				m.put("PRICEPLANID", rs.getString("PRICEPLANID"));
				servicedMap.put(serviceid, m);
				serviceidSet.add(serviceid);
			}
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
	
		//取SubScribers
		Map<String,Map<String, String>> subscribersMap = new HashMap<String,Map<String, String>>();
		
		try {
			String sql=
					"SELECT DATE_FORMAT(D.VERIFIED_DATE,'%Y/%m/%d') VERIFIED_DATE,B.SERVICEID, "
					+ "A.SUBS_NAME,A.SUBS_ID_TAXID,C.CHAIRMAN,C.CHAIRMAN_ID,A.SUBS_PHONE,A.SUBS_BIRTHDAY, "
					+ "A.SUBS_PERMANENT_ADDRESS,A.SUBS_BILLING_ADDRESS,A.SUBS_EMAIL,A.AGENCY_ID,A.REMARK "
					+ "FROM CRM_DB.CRM_SUBSCRIBERS A inner join "
					+ "		CRM_DB.CRM_SUBSCRIPTION B on A.SEQ=B.SEQ left join "
					+ "		CRM_DB.CRM_CHAIRMAN C on A.SEQ = C.SEQ left join "
					+ "		(	SELECT MAX(VERIFIED_DATE) VERIFIED_DATE,SERVICEID "
					+ "			FROM CRM_DB.CRM_APPLICATION "
					+ "			GROUP BY SERVICEID) D ON B.SERVICEID = D.SERVICEID "
					+ "WHERE 1=1 "
					//+ (year!=null && month != null ? "AND DATE_FORMAT(A.CREATETIME,'%Y%m') >= '"+year+month+"' ":"")
					+ "";
	
			conn = getConn3();
			st = conn.createStatement();
			
			int i = 0;
			String serviceids = "";
			for(String serviceid : serviceidSet) {
				if(!"".equals(serviceids)) serviceids += ",";
				serviceids += serviceid;
				
				i++;
				
				if( i == 1000) {
					i = 0;
					
					rs = null;
					System.out.println("Execute SQL :"+sql);
					rs = st.executeQuery(sql+" AND B.serviceid in ("+serviceids+") ");
			
					while(rs.next()){
						Map<String, String> m = new HashMap<String,String>();
						m.put("VERIFIED_DATE", rs.getString("VERIFIED_DATE"));
						m.put("SUBS_NAME", rs.getString("SUBS_NAME"));
						m.put("SUBS_ID_TAXID", hideData(rs.getString("SUBS_ID_TAXID")));
						m.put("CHAIRMAN", rs.getString("CHAIRMAN"));
						m.put("CHAIRMAN_ID", hideData(rs.getString("CHAIRMAN_ID")));
						m.put("SUBS_PHONE", hideData(rs.getString("SUBS_PHONE")));
						m.put("SUBS_BIRTHDAY", rs.getString("SUBS_BIRTHDAY"));
						m.put("SUBS_PERMANENT_ADDRESS", hideData(rs.getString("SUBS_PERMANENT_ADDRESS")));
						m.put("SUBS_BILLING_ADDRESS", hideData(rs.getString("SUBS_BILLING_ADDRESS")));
						m.put("SUBS_EMAIL", rs.getString("SUBS_EMAIL"));
						m.put("AGENCY_ID", rs.getString("AGENCY_ID"));
						m.put("REMARK", rs.getString("REMARK"));
						subscribersMap.put(rs.getString("SERVICEID"), m);
					}
					serviceids = "";
				}
			}
			
			if( i > 0) {
				rs = null;
				System.out.println("Execute SQL :"+sql);
				rs = st.executeQuery(sql+" AND B.serviceid in ("+serviceids+") ");
		
				while(rs.next()){
					Map<String, String> m = new HashMap<String,String>();
					m.put("VERIFIED_DATE", rs.getString("VERIFIED_DATE"));
					m.put("SUBS_NAME", rs.getString("SUBS_NAME"));
					m.put("SUBS_ID_TAXID", hideData(rs.getString("SUBS_ID_TAXID")));
					m.put("CHAIRMAN", rs.getString("CHAIRMAN"));
					m.put("CHAIRMAN_ID", hideData(rs.getString("CHAIRMAN_ID")));
					m.put("SUBS_PHONE", hideData(rs.getString("SUBS_PHONE")));
					m.put("SUBS_BIRTHDAY", rs.getString("SUBS_BIRTHDAY"));
					m.put("SUBS_PERMANENT_ADDRESS", hideData(rs.getString("SUBS_PERMANENT_ADDRESS")));
					m.put("SUBS_BILLING_ADDRESS", hideData(rs.getString("SUBS_BILLING_ADDRESS")));
					m.put("SUBS_EMAIL", rs.getString("SUBS_EMAIL"));
					m.put("AGENCY_ID", rs.getString("AGENCY_ID"));
					m.put("REMARK", rs.getString("REMARK"));
					subscribersMap.put(rs.getString("SERVICEID"), m);
				}
			}
		} finally{
			closeConnection(conn, st, ps, rs);
		}

		//20180119 查詢Priceplan 資料
		
		st = null;
		rs = null;
		
		Map<String, String> pricaplanMap = new HashMap<String,String>();
		try{
			String sql = "SELECT A.EFFECTIVITY,  A.PRICEPLANID,  A.NAME,  A.ALIASES,  A.PRODUCTION,  A.DESCRIPTION " + 
					"FROM PRICEPLAN_DETAIL A  "
					+ "WHERE A.PRICEPLANID in (select priceplanid from service group by priceplanid)";

			conn = getConn1();
			st = conn.createStatement();
			System.out.println("Execute SQL :"+sql);
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				pricaplanMap.put(rs.getString("PRICEPLANID"), processData(rs.getString("ALIASES"), "ISO-8859-1", "BIG5"));
			}
		}finally{
			closeConnection(conn, st, ps, rs);
		}
		
		//20180727 add
		
		for(String serviceid:servicedMap.keySet()) {
			Map<String,String> m = servicedMap.get(serviceid);
			if(subscribersMap.containsKey(serviceid))
				m.putAll(subscribersMap.get(serviceid));
			m.put("PRICEPLANID_ALIASES", pricaplanMap.get(m.get("PRICEPLANID")));
			result.add(m);
		}
		

		//System.out.println("data query finished");
		//closeConnection(conn, st, ps, rs);
		return sortByServiceid(result);		
	}

	//20160223 add 
	public List<Map<String,String>> sortByServiceid(List<Map<String,String>> list){
		List<Map<String,String>> result = null;
		try {
			result = mergeSortByServiceid(list,"SERVICEID");
		} catch (Exception e) {
		}
		return result;

	}
	
}
