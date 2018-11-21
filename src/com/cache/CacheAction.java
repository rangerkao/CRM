package com.cache;
 
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class CacheAction{ 
	
	public static Properties props =new Properties();
	public static Logger logger;
	
	static Connection conn = null;
	static Statement st = null;
	static ResultSet rs = null;
	
	static int maxConnection = 30;
	
	static Long ConnectionTime1;
	
	static SimpleConnectionPoolDataSource CPD1 ;
	static SimpleConnectionPoolDataSource CPD2 ;
	static SimpleConnectionPoolDataSource CPD3 ;
	
	static boolean threadExit= false;
	static long connectionCloseTime = 1000*60*20;

	static {
		try {
			loadProperties();
			CPD1 = new SimpleConnectionPoolDataSource(
					props.getProperty("Oracle.DriverClass"), 
					props.getProperty("Oracle.URL")
					.replace("{{Host}}", props.getProperty("Oracle.Host"))
					.replace("{{Port}}", props.getProperty("Oracle.Port"))
					.replace("{{ServiceName}}", 	(props.getProperty("Oracle.ServiceName")!=null?props.getProperty("Oracle.ServiceName"):""))
					.replace("{{SID}}", (props.getProperty("Oracle.SID")!=null?props.getProperty("Oracle.SID"):"")), 
					props.getProperty("Oracle.UserName"), 
					props.getProperty("Oracle.PassWord"),
					maxConnection);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			CPD2 = new SimpleConnectionPoolDataSource(
					props.getProperty("mBOSS.DriverClass"), 
					props.getProperty("mBOSS.URL")
					.replace("{{Host}}", props.getProperty("mBOSS.Host"))
					.replace("{{Port}}", props.getProperty("mBOSS.Port"))
					.replace("{{ServiceName}}", (props.getProperty("mBOSS.ServiceName")!=null?props.getProperty("mBOSS.ServiceName"):""))
					.replace("{{SID}}", (props.getProperty("mBOSS.SID")!=null?props.getProperty("mBOSS.SID"):"")), 
					props.getProperty("mBOSS.UserName"), 
					props.getProperty("mBOSS.PassWord"),
					maxConnection);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			CPD3 = new SimpleConnectionPoolDataSource(
					props.getProperty("CRMDB.DriverClass"), 
					props.getProperty("CRMDB.URL")
					.replace("{{Host}}", props.getProperty("CRMDB.Host"))
					.replace("{{Port}}", props.getProperty("CRMDB.Port"))
					.replace("{{DBName}}", props.getProperty("CRMDB.DBName")), 
					props.getProperty("CRMDB.UserName"), props.getProperty("CRMDB.PassWord"),
					maxConnection);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Timer t = new Timer();
		long pTime = 1000*60*5;
		t.schedule(new connCtrol(),pTime,pTime);
	}
	
	static class connCtrol extends TimerTask{

		@Override
		public void run() {
			Long currentTime = System.currentTimeMillis();
			long timeLimit = 1000*60*10; //10分鐘
			
			//System.out.println("Check CPD1");
			check(CPD1,currentTime,timeLimit);

			//System.out.println("Check CPD2");
			check(CPD2,currentTime,timeLimit);

			//System.out.println("Check CPD3");
			check(CPD3,currentTime,timeLimit);
		}
		
		private void check(SimpleConnectionPoolDataSource CPD,long currentTime,long timeLimit) {
			try {
				List<Connection> conns = CPD.getConns();
				//System.out.println("目前數量"+conns.size());
				if(conns.size()>0) {
					Iterator<Connection> ci = conns.iterator();
					synchronized (conns) {
					ConnectionWrapper conn = null;
						while(ci.hasNext()) {
							conn = (ConnectionWrapper) ci.next();
							//閒置超過10分鐘進行回收
							if(currentTime - conn.getExecuteTime()>=timeLimit) {
								ci.remove();
								try {
									conn.doClose();
								} catch (SQLException e) {}
								//System.out.println("關閉 1，剩下"+conns.size());
							}
						}
					}
				}
				//System.out.println("最後數量"+conns.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	//Load Properties
	static void loadProperties() throws FileNotFoundException, IOException{
	    String path = CacheAction.class.getClassLoader().getResource("").toString().replace("file:", "").replace("%20", " ");
		props.load(new FileInputStream(path+ "/log4j.properties"));
		PropertyConfigurator.configure(props);
		System.out.println("loadProperties success!");
		logger =Logger.getLogger("CRM");
	}
	
	public static Connection getConn1() throws ClassNotFoundException, SQLException{
		Connection con = CPD1.getConnection();
		return con;
	}
	
	public static Connection getConn2() throws ClassNotFoundException, SQLException{
		Connection con = CPD2.getConnection();
		return con;
	}
	
	public static Connection getConn3() throws ClassNotFoundException, SQLException{
		Connection con = CPD3.getConnection();
		return con;
	}
	
	public static void CloseConnection() {
		try {
			if(rs!=null)
				rs.close();
		} catch (SQLException e) {
		}
		try {
			if(st!=null)
				st.close();
		} catch (SQLException e) {
		}
		try {
			if(conn!=null)
				conn.close();
		} catch (SQLException e) {
		}
	}
	
	
	public static void sendSMS(String msg,String phone,String type) throws IOException, SQLException, ClassNotFoundException{

		msg = new String(msg.getBytes("BIG5"),"ISO8859-1");
		
		String PhoneNumber=phone,Text=msg,charset="BIG5",InfoCharCounter=null,PID=null,DCS=null;
		String param =
				"PhoneNumber=+{{PhoneNumber}}&"
				+ "Text={{Text}}&"
				+ "charset={{charset}}&"
				+ "InfoCharCounter={{InfoCharCounter}}&"
				+ "PID={{PID}}&"
				+ "DCS={{DCS}}&"
				+ "Submit=Submit";
		
		if(PhoneNumber==null)PhoneNumber="";
		if(Text==null)Text="";
		if(charset==null)charset="";
		if(InfoCharCounter==null)InfoCharCounter="";
		if(PID==null)PID="";
		if(DCS==null)DCS="";
		
		param=param.replace("{{PhoneNumber}}",PhoneNumber );
		param=param.replace("{{Text}}",Text );
		param=param.replace("{{charset}}",charset );
		param=param.replace("{{InfoCharCounter}}",InfoCharCounter );
		param=param.replace("{{PID}}",PID );
		param=param.replace("{{DCS}}",DCS );		
		
		String result = HttpPost("http://10.42.200.100:8800/Send%20Text%20Message.htm", param,"");
		
		logSendSMS(PhoneNumber, msg, result, type);
		 
	}
	
	static String HttpPost(String url,String param,String charset) throws IOException{
		URL obj = new URL(url);
		
		if(charset!=null && !"".equals(charset))
			param=URLEncoder.encode(param, charset);
		
		
		HttpURLConnection con =  (HttpURLConnection) obj.openConnection();
 
		//add reuqest header
		/*con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");*/
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(param);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		CacheAction.logger.info("\nSending 'POST' request to URL : " + url);
		CacheAction.logger.info("Post parameters : " + new String(param.getBytes("ISO8859-1"),"BIG5"));
		CacheAction.logger.info("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		return(response.toString());
	}
	
	public static void sendMail(String mailSubject,String mailContent,String mailSender,String mailReceiver){
		String [] cmd=new String[3];
		cmd[0]="/bin/bash";
		cmd[1]="-c";
		cmd[2]= "/bin/echo \""+mailContent+"\" | /bin/mail -s \""+mailSubject+"\" -r "+mailSender+" "+mailReceiver;

		try{
			Process p = Runtime.getRuntime().exec (cmd);
			p.waitFor();
			System.out.println("send mail cmd:"+cmd);
		}catch (Exception e){
			System.out.println("send mail fail:"+mailContent);
		}
	}
	
	public static void logSendSMS(String phone,String msg,String res,String type) throws SQLException, ClassNotFoundException{
		String sql="INSERT INTO HUR_SMS_LOG"
				+ "(ID,SEND_NUMBER,MSG,SEND_DATE,RESULT,TYPE) "
				+ "VALUES(DVRS_SMS_ID.NEXTVAL,'"+phone+"','"+msg+"',sysdate,'"+(res.contains("Message Submitted")?"Success":"failed")+"','"+type+"')";
		
		Connection conn = null;
		Statement st = null ;
		
		try {
			conn = getConn1();
			st = conn.createStatement();
			st.executeUpdate(sql);
		} finally {
			try {
				if(conn!=null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				if(st!=null)
					st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String queryCustmerServicePhone(String serviceid) throws SQLException, ClassNotFoundException{
		String cphone="";
		String VLN=queryVLR(serviceid);
		
		if(VLN!=null && !"".equals(VLN)){
			Map<String,String> tadigMap = queryTADIG();
			
			if(tadigMap.size()>0){
				String tadig=null;
				for(int i=VLN.length();i>0;i--){
					tadig=tadigMap.get(VLN.substring(0,i));
					if(tadig!=null &&!"".equals(tadig)){
						break;
					}
				}
				if(tadig!=null &&!"".equals(tadig)){
					String mccmnc=queryMccmnc(tadig);
					if(mccmnc!=null &&!"".equals(mccmnc)){
						cphone=queryCustomerServicePhone(mccmnc,true);
					}
				}
			}
		}
		return cphone;
	}
	
	public static String queryVLR(String serviceid) throws SQLException, ClassNotFoundException{
		String VLN=null;
		
		String sql="SELECT VLR_NUMBER FROM UTCN.BASICPROFILE WHERE serviceid ='"+serviceid+"'";
		try {
			conn = getConn2();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				VLN = rs.getString("VLR_NUMBER");
			} 
		} finally {
			CloseConnection();
		}
		return VLN;
	}
	
	public static Map<String,String> queryTADIG() throws SQLException, ClassNotFoundException{
		Map<String,String> map = new HashMap<String,String>();
		
		String sql=" SELECT B.REALMNAME TADIG, A.CHARGEAREACODE VLR FROM CHARGEAREACONFIG A, REALM B "
				+ "WHERE A.AREAREFERENCE=B.AREAREFERENCE ";
		try {
			conn = getConn2();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				map.put(rs.getString("VLR"), rs.getString("TADIG"));
			} 
		} finally {
			CloseConnection();
		}
		return map;
	}
	public static String queryMccmnc(String tadig) throws SQLException, ClassNotFoundException{
		String mccmnc=null;
				
		String sql=" SELECT MCCMNC FROM HUR_MCCMNC WHERE TADIG='"+tadig+"'";
		
		try {
			conn = getConn1();
			st = conn.createStatement();
			rs = null;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				mccmnc = rs.getString("MCCMNC");
			} 
		} finally {
			CloseConnection();
		}
		return mccmnc;		
	}
	
	public static String queryCustomerServicePhone(String mccmnc,Boolean processor) throws SQLException, ClassNotFoundException{
		String cPhone=null;
		String subcode=mccmnc.substring(0,3);
		String sql=" SELECT "+(processor?"CHT_PHONE":"S2T_PHONE")+" PHONE FROM HUR_CUSTOMER_SERVICE_PHONE A WHERE A.CODE ='"+subcode+"'";
		try {
			conn = getConn1();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				cPhone = rs.getString("PHONE");
			} 
		} finally {
			CloseConnection();
		}
		return cPhone;		
	}
	
	

}
