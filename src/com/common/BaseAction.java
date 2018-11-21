package com.common;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static Map<String, Object> session;
	protected static String result;
	protected static String needLogin= "{\"error\":\"Please Login First!\"}";
	
	public BaseAction(){
	}
	
	public String setSuccess(Object data) throws JsonProcessingException{
		

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("data", data);		

		return new ObjectMapper().writeValueAsString(map);
	}
	
	public static String errorHandle(Exception e) throws JsonProcessingException{
		e.printStackTrace();
		StringWriter s = new StringWriter();
		e.printStackTrace(new PrintWriter(s));
		//CacheAction.sendMail("k1988242001@gmail.com","DVRS Exception Error!",new Date()+"\n"+s);
		return setFail(s.toString());
	}
	
	public static String setFail(String msg) throws JsonProcessingException{
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("error", msg);
		return new ObjectMapper().writeValueAsString(map);
	}

	public void reflectSet(Object bean, Map<String, String> valMap) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Class<?> cls = bean.getClass();  
		for (Field field : cls.getDeclaredFields()) {
			String fieldSetName = parSetName(field.getName());  
            if (!checkSetMet(cls.getDeclaredMethods(), fieldSetName)) {  
                continue;  
            }  
            Method fieldSetMet = cls.getMethod(fieldSetName, field  
                    .getType());  
			String value = valMap.get(field.getName());  
			
			if (null != value && !"".equals(value)) {  
                  String fieldType = field.getType().getSimpleName();  
                  if ("String".equals(fieldType)) {  
                      fieldSetMet.invoke(bean, value);  
                  } else if ("Date".equals(fieldType)) {  
                     /* Date temp = parseDate(value);  
                      fieldSetMet.invoke(bean, temp); */ 
                  } else if ("Integer".equals(fieldType)|| "int".equals(fieldType)) {  
                      Integer intval = Integer.parseInt(value);  
                      fieldSetMet.invoke(bean, intval);  
                  } else if ("Long".equalsIgnoreCase(fieldType)) {  
                      Long temp = Long.parseLong(value);  
                      fieldSetMet.invoke(bean, temp);  
                  } else if ("Double".equalsIgnoreCase(fieldType)) {  
                      Double temp = Double.parseDouble(value);  
                      fieldSetMet.invoke(bean, temp);  
                  } else if ("Boolean".equalsIgnoreCase(fieldType)) {  
                      Boolean temp = Boolean.parseBoolean(value);  
                      fieldSetMet.invoke(bean, temp);  
                  } else {  
                      System.out.println("not supper type" + fieldType);  
                  }  
              }  
		}
	}
	
	 public static boolean checkSetMet(Method[] methods, String fieldSetMet) {		 
	        for (Method met : methods) {  
	            if (fieldSetMet.equals(met.getName())) {  
	                return true;  
	            }  
	        }  
	        return false;  
	    }  
	 public static String parSetName(String fieldName) {  
	        if (null == fieldName || "".equals(fieldName)) {  
	            return null;  
	        }  
	        return "set" + fieldName.substring(0, 1).toUpperCase()  
	                + fieldName.substring(1);  
	    } 
	

	
	
}
