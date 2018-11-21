package com.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.bean.VIP;
import com.cache.CacheAction;
import com.dao.VIPDao;

@Service
public class VIPService {

	@Resource
	VIPDao vipDao;
	
	public VIP queryVIP(String serviceid) throws Exception{
		return vipDao.queryVIP(serviceid);
	}
	
	public VIP addVIP(String serviceid) throws Exception{
		vipDao.addVIP(serviceid);
		return vipDao.queryVIP(serviceid);
	}
	
	public VIP deleteVIP(String serviceid) throws Exception{
		vipDao.deleteVIP(serviceid);
		return vipDao.queryVIP(serviceid);
	}
	
	public void sendVIP(String serviceid,String msisdn,String msg) throws Exception{
		msg = msg.replace("{{customerService}}", CacheAction.queryCustmerServicePhone(serviceid));
		CacheAction.sendSMS(msg, msisdn, "VIP");
	}

	public VIPDao getVipDao() {
		return vipDao;
	}

	public void setVipDao(VIPDao vipDao) {
		this.vipDao = vipDao;
	}

}
