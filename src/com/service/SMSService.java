package com.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.naming.spi.DirStateFactory.Result;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.bean.DataOpenSMS;
import com.bean.SMS;
import com.cache.CacheAction;
import com.dao.SMSDao;

@Service
public class SMSService {

	@Resource
	SMSDao sMSDao ;
	
	public List<SMS> querySMS(String S2TMSISDN,String CHTMSISDN,String startDate,String endDate,String activatedDate,String canceledDate) throws Exception, ParseException{
		return sMSDao.querySMS(S2TMSISDN, CHTMSISDN, startDate, endDate, activatedDate, canceledDate);
	}
	
	public DataOpenSMS queryDataOpenSMS() throws Exception {
		return sMSDao.queryDataOpenSMS();
	}
	
	public String sendDataOpenSMS(String msisdn,List<String> msgs) throws Exception {
		for(String msg:msgs) {
			msg = msg.replaceAll("\\{dot\\}", ",");
			
			CacheAction.sendSMS(msg, msisdn, "GPRS_ON");
		}
		return "Success";
	}

	public SMSDao getsMSDao() {
		return sMSDao;
	}

	public void setsMSDao(SMSDao sMSDao) {
		this.sMSDao = sMSDao;
	}
	
	
}
