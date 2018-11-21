package com.common;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.cache.CacheAction;

public class BaseDao{

	protected Properties props =null;
	/*protected Connection conn=null;
	protected Statement st = null;
	protected PreparedStatement ps = null;
	protected ResultSet rs = null;*/
	//private static Date requestTime;
	
	public BaseDao() throws Exception{

	}	
	public boolean conn1State = false;
	protected Connection getConn1() throws Exception{
		return CacheAction.getConn1();
	}
	protected Connection getConn2() throws Exception{
		return CacheAction.getConn2();
	}
	protected Connection getConn3() throws Exception{
		return CacheAction.getConn3();
	}
	protected void closeConnection(Connection conn,Statement st,PreparedStatement ps ,ResultSet rs){
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
		
		try {
			if(ps!=null)
				ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(rs!=null)
				rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Close connect!("+new Date()+")");
	}
	
	public String processData(String data){
		return processData(data,null,null);
	}
	public String processData(String data,String sCharSet,String dCharSet){
		if(data==null)
			return " ";
		
		try {
			
			if(sCharSet!=null && !"".equals(sCharSet) && dCharSet!=null && !"".equals(dCharSet)) {
				data = new String(data.getBytes(sCharSet),dCharSet);
			}
		} catch (UnsupportedEncodingException e) {
			
		}
		
		return data;
	}
	
	
	public String FormatDouble(Double value) throws Exception{
		return FormatDouble(value,null);
	}
	public String FormatDouble(Double value,String form) throws Exception{
		
		if(value == null)
			throw new Exception("Input could't be null.");
		
		if(form==null || "".equals(form)){
			form="0.00";
		}
		return new DecimalFormat(form).format(value);
	}
	
	public List<Map<String,String>> mergeSortByServiceid(List<Map<String,String>> list,String compareValue){
		
		int total = list.size();
		int middle = (int) Math.floor(total/2);
		
		if(total <= 1){
			return list;
		}
		
		List<Map<String,String>> left = mergeSortByServiceid(list.subList(0, middle),compareValue);
		List<Map<String,String>> right = mergeSortByServiceid(list.subList(middle,total),compareValue);
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();

		int i = 0,j=0;
		while(i<left.size()&&j<right.size()){
			long servicei = Long.parseLong(left.get(i).get(compareValue) == null ? "0"	: (String) left.get(i).get(compareValue));
			long servicej = Long.parseLong(right.get(j).get(compareValue) == null ? "0"	: (String) right.get(j).get(compareValue));
			if(servicei<=servicej){
				result.add(left.get(i));
				i++;
			}else{
				result.add(right.get(j));
				j++;
			}
		}
		
		for(;i<left.size();i++){
			result.add(left.get(i));
		}
		for(;j<right.size();j++){
			result.add(right.get(j));
		}
		
		return result;
	}

	protected String hideData(String data){
		if(data==null || "".equals(data))
			return "";
		
		if(data.length()<=3) return "***";
		
		//return data.substring(0,2)+"*****"+data.substring(data.length()-1,data.length());
		return data;
	}
}

