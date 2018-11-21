package com.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.bean.User;
import com.dao.LogDao;


@Service
public class LogService{

	
	
	public LogService() throws Exception{
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Resource
	LogDao loginDao ;
	
	public void checkAccount(HttpSession session,String account,String password) throws Exception{
		User user = loginDao.queryUser(account);
		
		if(user==null)
			throw new Exception("Without this account!");
		
		String md5Password = md5Encode(password);
		String DBPassWord = user.getPassword();
		if(md5Password.equals(DBPassWord)){
			session.setAttribute("s2t.account", user.getAccount());
			//session.put("s2t.password", user.getPassword());
			session.setAttribute("s2t.role", user.getRole());
			session.setMaxInactiveInterval(-1);
		}else{
			throw new Exception("Password Error!");
		}
	}
	
	public void actionLog(String account,String params,String function,String result) throws SQLException, Exception {
		loginDao.actionLog(account, params, function, result);
	}

	//md5 EncodeTest
	public String md5Encode(String source) throws NoSuchAlgorithmException{
		String input=source;
		 MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        return byteToString1(messageDigest);
	}
	
	public String byteToString1(byte[] source){
		BigInteger number = new BigInteger(1, source);
        String hashtext = number.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
	}
	
	public LogDao getLoginDao() {
		return loginDao;
	}
	public void setLoginDao(LogDao loginDao) {
		this.loginDao = loginDao;
	}
	
	

}
