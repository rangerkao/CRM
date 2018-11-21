package com.dao;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cache.CacheAction;
import com.common.BaseDao;

@Repository
public class ProvisionDao extends BaseDao{
	
	public ProvisionDao() throws Exception {
		super();
		Thread t = new Thread(new CheckProvisionStatus());
		t.start();
	}

	Connection conn=null;
	Statement st = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	Logger logger = null;
	
	SimpleDateFormat reQueSdf = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sDateSdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	
	public void changeRoammingRestriction(String s2tIMSI,String s2tMsisdn,String rrCode) throws Exception{
		try {
			conn = getConn1();
			conn.setAutoCommit(false);
			st =conn.createStatement();
			String MNOSubCode = "950";
			doSuspend(s2tIMSI, s2tMsisdn, MNOSubCode);
			dochangeRoammingRestriction(s2tIMSI, s2tMsisdn, MNOSubCode,rrCode);
			doResume(s2tIMSI, s2tMsisdn, MNOSubCode);
		} finally {
			conn.setAutoCommit(true);
			closeConnection(conn, st, ps, rs);
		}
	}
	
	public void dochangeRoammingRestriction(String s2tIMSI,String s2tMsisdn,String MNOSubCode,String rrCode) throws Exception{
		//TODO 進行停話，避免用戶使用到舊號
		try {
			System.out.println("do suspend....");
			
			String sMNOSubCode = MNOSubCode;
			
			Date now = new Date();
			//請求日期 "yyyyMMdd"
			String reqDate = reQueSdf.format(now);
			String sCount = getTWNLDsCount(sMNOSubCode,reqDate);
			String fileId = getFileId();
			String workOrderNBR = getWorkOrderNBR();
			String cServiceOrderNBR = getServiceOrderNBR();
			String c910SEQ = reqDate + sCount;
			String fileName = "S2TCI" + c910SEQ + "." + sMNOSubCode;
			String ticketNumber = "P"+reqDate+sCount;
			
			String sdate = sDateSdf.format(new Date());//請求時間 "MM/dd/yyyy HH:mm:ss"
			
			doSyncFile(fileId, fileName, reqDate, sCount, sMNOSubCode);
			
			String request = "13";
			doSyncFileDTL(fileId, c910SEQ, sdate, ticketNumber, cServiceOrderNBR, workOrderNBR, s2tIMSI, s2tMsisdn,request);

			String subCode = "202";
			doServiceOrderItem(cServiceOrderNBR,subCode);
			
			//service order nbr , step No, type code,data type,value
			String[][] dtls  = {
					{cServiceOrderNBR,"1","0","0","1"},
					{cServiceOrderNBR,"1","81","1","TWNLD"},
					{cServiceOrderNBR,"1","83","1","TWNLD"},
					{cServiceOrderNBR,"1","86","0","59"},
					{cServiceOrderNBR,"1","87","0","3"},
					{cServiceOrderNBR,"1","106","1","91"},

					{cServiceOrderNBR,"1","1011","0","268"},
					{cServiceOrderNBR,"1","1903","1","PRC"},
					{cServiceOrderNBR,"1","1916","0","1"},
					{cServiceOrderNBR,"1","1917","0",rrCode},
					{cServiceOrderNBR,"1","1926","1","1"},
					{cServiceOrderNBR,"1","1945","0","1"},
					{cServiceOrderNBR,"1","1946","1","GPRS1"},
					
					{cServiceOrderNBR,"1","9016","0","1"},
					{cServiceOrderNBR,"1","9019","0","1"},
					{cServiceOrderNBR,"1","9020","0",rrCode},
					
	
					{cServiceOrderNBR,"1","2","1",s2tMsisdn},
			};
			doServiceOrderItemDTL(dtls);
			doServiceOrder(cServiceOrderNBR, workOrderNBR, s2tIMSI, s2tMsisdn,request);
			conn.commit();

			Map<String,String> m = new HashMap<String,String>();
			m.put("serviceOrderNBR", cServiceOrderNBR);
			checkList.add(m);
		}finally {
			
		}
	}
	
	
	public void doSuspend(String s2tIMSI,String s2tMsisdn,String MNOSubCode) throws Exception{
		//TODO 進行停話，避免用戶使用到舊號
		try {
			System.out.println("do suspend....");
			
			String sMNOSubCode = MNOSubCode;
			
			Date now = new Date();
			//請求日期 "yyyyMMdd"
			String reqDate = reQueSdf.format(now);
			String sCount = getTWNLDsCount(sMNOSubCode,reqDate);
			String fileId = getFileId();
			String workOrderNBR = getWorkOrderNBR();
			String cServiceOrderNBR = getServiceOrderNBR();
			String c910SEQ = reqDate + sCount;
			String fileName = "S2TCI" + c910SEQ + "." + sMNOSubCode;
			String ticketNumber = "P"+reqDate+sCount;
			
			/*prepareProvision();
			provision01(s2tIMSI, s2tMsisdn);*/
			String request = "01";
			String sdate = sDateSdf.format(new Date());//請求時間 "MM/dd/yyyy HH:mm:ss"
			
			doSyncFile(fileId, fileName, reqDate, sCount, sMNOSubCode);
			doSyncFileDTL(fileId, c910SEQ, sdate, ticketNumber, cServiceOrderNBR, workOrderNBR, s2tIMSI, s2tMsisdn,request);

			String subCode = "004";
			doServiceOrderItem(cServiceOrderNBR,subCode);
			
			//service order nbr , step No, type code,data type,value
			String[][] dtls  = {
					{cServiceOrderNBR,"1","122","1","0"},
					{cServiceOrderNBR,"1","2","1",s2tMsisdn},
					{cServiceOrderNBR,"1","72","0","0"},
					{cServiceOrderNBR,"1","37","0","999999998"},
			};
			doServiceOrderItemDTL(dtls);
			doServiceOrder(cServiceOrderNBR, workOrderNBR, s2tIMSI, s2tMsisdn,request);
			conn.commit();

			Map<String,String> m = new HashMap<String,String>();
			m.put("serviceOrderNBR", cServiceOrderNBR);
			checkList.add(m);
		}finally {
			
		}
	}
	
	public void doResume(String s2tIMSI,String s2tMsisdn,String MNOSubCode) throws Exception{
		try {
			System.out.println("do resume....");
			//prepareProvision();
			//provision02(s2tIMSI, s2tMsisdn);
			String sMNOSubCode = MNOSubCode;
			Date now = new Date();
			//請求日期 "yyyyMMdd"
			String reqDate = reQueSdf.format(now);
			String sCount = getTWNLDsCount(sMNOSubCode,reqDate);
			String fileId = getFileId();
			String workOrderNBR = getWorkOrderNBR();
			String cServiceOrderNBR = getServiceOrderNBR();
			String c910SEQ = reqDate + sCount;
			String fileName = "S2TCI" + c910SEQ + "." + sMNOSubCode;
			String ticketNumber = "P"+reqDate+sCount;
			
			String request = "02";
			//請求時間 "MM/dd/yyyy HH:mm:ss"
			String sdate = sDateSdf.format(new Date());
			
			doSyncFile(fileId, fileName, reqDate, sCount, sMNOSubCode);
			doSyncFileDTL(fileId, c910SEQ, sdate, ticketNumber, cServiceOrderNBR, workOrderNBR, s2tIMSI, s2tMsisdn,request);

			String subCode = "005";
			doServiceOrderItem(cServiceOrderNBR,subCode);
			
			//service order nbr , step No, type code,data type,value
			String[][] dtls  = {
					{cServiceOrderNBR,"1","122","1","0"},
					{cServiceOrderNBR,"1","2","1",s2tMsisdn},
					{cServiceOrderNBR,"1","37","0","999999998"},
			};
			doServiceOrderItemDTL(dtls);
			doServiceOrder(cServiceOrderNBR, workOrderNBR, s2tIMSI, s2tMsisdn,request);
			conn.commit();
			
			Map<String,String> m = new HashMap<String,String>();
			m.put("serviceOrderNBR", cServiceOrderNBR);
			checkList.add(m);
		} finally {
			
		}
	}
	
	public String getTWNLDsCount(String sMNOSubCode,String reqDate) throws SQLException {
		String sCount="";
		int j = 0,iCount;
		String sql = "select currentseq,count(currentseq) as ab from seqrec where MNOSUBCODE='" + sMNOSubCode + "' and currentdate='" + reqDate	+ "' group by currentseq";
		System.out.println("Execut SQL:"+sql);
		rs = st.executeQuery(sql);
		while (rs.next()) {
			sCount = rs.getString("currentseq");
			j = rs.getInt("ab");
		}
		if(j>0){
			iCount = Integer.parseInt(sCount);
			iCount += 1;
			sCount = Integer.toString(iCount);
			sql =  "update seqrec set currentseq=" + sCount+ " where MNOSUBCODE='" + sMNOSubCode + "'";
			System.out.println("Execut SQL:"+sql);
			st.executeUpdate(sql);
		}else{
			iCount = 1;
			sql = "delete seqrec where MNOSUBCODE='" + sMNOSubCode + "'";
			System.out.println("Execut SQL:"+sql);
			st.executeUpdate(sql);
			sql = "insert into seqrec(MNOSUBCODE,currentdate,currentseq) values('"+ sMNOSubCode+ "','"+ reqDate	+ "',"+ Integer.toString(iCount) + ")";
			System.out.println("Execut SQL:"+sql);
			st.executeUpdate(sql);
			sCount = Integer.toString(iCount);
		}
		while(sCount.length()<5){
			sCount="0"+sCount;
		}
		
		return sCount;
	}
	
	public String getFileId() throws SQLException {
		//get File ID
		System.out.println("get file Id...");
		String fileId = "";    
		rs = null;
		rs = st.executeQuery("select S2T_SQ_FILE_CNTRL.NEXTVAL as ab from dual");
		while(rs.next()){
			fileId = rs.getString("ab");
		}
		return fileId;
	}
	public String getWorkOrderNBR() throws SQLException {
		//get work order NBR
		System.out.println("get workOrderNBR...");
		String workOrderNBR = "";
		rs = null;
		rs = st.executeQuery("select S2T_SQ_WORK_ORDER.nextval as ab from dual");
		while(rs.next()){
			workOrderNBR = rs.getString("ab");
		}
		return workOrderNBR;
	}
	
	public String getServiceOrderNBR() throws SQLException {
		System.out.println("get cServiceOrderNBR...");
		String cServiceOrderNBR = "";
		rs =null;
		rs = st.executeQuery("select S2T_SQ_SERVICE_ORDER.nextval as ab from dual");
		while(rs.next()){
			cServiceOrderNBR = rs.getString("ab");
		}
		return cServiceOrderNBR;
	}
	
	public void doSyncFile(String fileId,String fileName,String reqDateSub,String c910SEQSub,String MNOSubCode) throws SQLException{
		System.out.println("Insert syncFile...");
    	//INSERT INTO S2T_TB_TYPEB_WO_SYNC_FILE (FILE_ID,FILE_NAME,FILE_SEND_DATE,FILE_SEQ,CMCC_BRANCH_ID,FILE_CREATE_DATE,STATUS)   	 VALUES ([cFileID],'[cFileName]','[dReqDate.substring(0, 8)]','[c910SEQ.substring(8, 11) ]','950',sysdate,'[sSFStatus]');
    	String sql = "INSERT INTO S2T_TB_TYPEB_WO_SYNC_FILE (FILE_ID,FILE_NAME,FILE_SEND_DATE,FILE_SEQ,CMCC_BRANCH_ID,FILE_CREATE_DATE,STATUS)  "
    			+ "									VALUES ("+fileId+",'"+fileName+"','"+reqDateSub+"','"+c910SEQSub+"','"+MNOSubCode+"',sysdate,'V')";
    	System.out.println("Execut SQL:"+sql);
    	st.executeUpdate(sql);
	}
	
	public void doSyncFileDTL(String fileId,String c910SEQ,String sdate, String ticketNumber,String cServiceOrderNBR,String workOrderNBR,String s2tIMSI,String s2tMsisdn,String request) throws SQLException{
		System.out.println("Insert syncFileDTL...");
		String sql = "INSERT INTO S2T_TB_TYPB_WO_SYNC_FILE_DTL (WORK_ORDER_NBR,WORK_TYPE, FILE_ID, SEQ_NO, CMCC_OPERATIONDATE, ORIGINAL_CMCC_IMSI,ORIGINAL_CMCC_MSISDN, S2T_IMSI, S2T_MSISDN, FORWARD_TO_HOME_NO,FORWARD_TO_S2T_NO_1, IMSI_FLAG, STATUS, SERVICE_ORDER_NBR, SUBSCR_ID,S2T_OPERATIONDATE) "
                + "VALUES ("+workOrderNBR+",'"+request+"',"+fileId+",'"+c910SEQ+"',to_date('"+sdate+"','MM/dd/yyyy hh24:mi:ss'),'"+s2tIMSI+"','"+s2tMsisdn+"','"+s2tIMSI+"','"+s2tMsisdn+"','"+s2tMsisdn+"','"+s2tMsisdn+"', '1', 'V','"+cServiceOrderNBR+"','"+ticketNumber+"',sysdate)";
		System.out.println("Execut SQL:"+sql);
		st.executeUpdate(sql);
	}
	public void doServiceOrder(String cServiceOrderNBR,String workOrderNBR,String s2tIMSI,String s2tMsisdn,String request) throws SQLException{
		System.out.println("Insert serviceOrder...");
    	//INSERT INTO S2T_TB_SERVICE_ORDER (SERVICE_ORDER_NBR,WORK_TYPE, S2T_MSISDN, SOURCE_TYPE, SOURCE_ID, STATUS, CREATE_DATE) VALUES ('[cServiceOrderNBR]','[cReqStatus ]','[cS2TMSISDN]','B_TYPE',[cWorkOrderNBR ], '', sysdate);
    	String sql = "INSERT INTO S2T_TB_SERVICE_ORDER (SERVICE_ORDER_NBR,WORK_TYPE, S2T_MSISDN, SOURCE_TYPE, SOURCE_ID, STATUS, CREATE_DATE) "
    			+ "								VALUES ('"+cServiceOrderNBR+"','"+request+"','"+s2tMsisdn+"','B_TYPE',"+workOrderNBR+", 'N', sysdate)";
    	System.out.println("Execut SQL:"+sql);
    	st.executeUpdate(sql);
	}
	public void doServiceOrderItem(String cServiceOrderNBR,String subCode) throws SQLException{
		System.out.println("Insert ServiceOrderItem...");
    	String sql = "Insert into S2T_TB_SERVICE_ORDER_ITEM (SERVICE_ORDER_NBR,STEP_NO, SUB_CODE, IDENTIFIER, STATUS, SEND_DATE) "
    			+ "									Values ("+cServiceOrderNBR+",1,'"+subCode+"',S2T_SQ_SERVICE_ORDER_ITEM.nextval, 'N', sysdate)";
    	System.out.println("Execut SQL:"+sql);
    	st.executeUpdate(sql);
	}
	public void doServiceOrderItemDTL(String[][] dtls) throws SQLException{
		System.out.println("Insert ServiceOrderItemDtl...");
		String sql;
		for(int i = 0 ; i<dtls.length;i++){
			sql = "Insert into S2T_TB_SERVICE_ORDER_ITEM_DTL (SERVICE_ORDER_NBR, STEP_NO, TYPE_CODE, DATA_TYPE, VALUE) "
					+ "VALUES ("+dtls[i][0]+","+dtls[i][1]+","+dtls[i][2]+","+dtls[i][3]+",'"+dtls[i][4]+"')";
			System.out.println("Execut SQL:"+sql);
	    	st.executeUpdate(sql);
		}
	}
	
	
	static List<Map<String,String>> checkList = new ArrayList<Map<String,String>>();
	
	class CheckProvisionStatus implements Runnable{
		boolean exit = false;
		@Override
		public void run() {
			
			while(!exit) {
				if(checkList.size()>0) {
					Connection connS = null;
					Statement stS = null;
					try {
						connS = getConn1();
						stS = connS.createStatement();
						synchronized (checkList) {
							Iterator<Map<String, String>> it = checkList.iterator();
							while(it.hasNext()) {
								
								Map<String,String> m = it.next();
								String serviceOrderNBR = m.get("serviceOrderNBR");
								String errorCode;
								
									errorCode = queryProvisionResult(connS,stS,serviceOrderNBR);
									
									if(!"0".equals(errorCode)&&!"501".equals(errorCode)){
										CacheAction.sendMail("CRM Provision Error:"+serviceOrderNBR, "CRM Provision Error:"+serviceOrderNBR, "CRM_ERROR", "ranger.kao@sim2travel.com");
									}
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {}
							}
							checkList.clear();
						}
						try {
							Thread.sleep(60*1000);
						} catch (InterruptedException e) {}
				
					} catch (Exception e) {
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						CacheAction.sendMail("CRM Provision Error:"+e.getMessage(), "CRM Provision Error:"+e.toString(), "CRM_ERROR", "ranger.kao@sim2travel.com");

					}finally {
						closeConnection(connS, stS, null, null);
					}
				}
					
			}
		}
		
		String queryProvisionResult(Connection connS,Statement stS,String cServiceOrderNBR) throws SQLException{
			String status=null,errorCode=null,sql;
			int watingTimes = 10;
			ResultSet rs =null;
			for(int i = 0 ; i <watingTimes ; i++){
				sql = "select A.status,A.error_cde from S2T_TB_SERVICE_ORDER_ITEM A where A.SERVICE_ORDER_NBR = "+cServiceOrderNBR+" ";
				rs = stS.executeQuery(sql);
				
				if(rs.next()){
					status = rs.getString("status");
					errorCode = rs.getString("error_cde");
				}
				System.out.println("query result "+(i+1)+" "+status);
				if(!"N".equals(status.toUpperCase())&&!"S".equals(status.toUpperCase()))
					break;
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
			}
			
			return errorCode;
		}
		
	}

	public static void main(String[] args) throws Exception {
		new ProvisionDao().changeRoammingRestriction("454120260302537", "85266404002","208");
	}
	
}
