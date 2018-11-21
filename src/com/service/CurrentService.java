package com.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bean.CurrentDay;
import com.bean.CurrentMonth;
import com.dao.CurrentDao;

@Service
public class CurrentService {

	@Resource
	CurrentDao currentDao;
	
	public List<CurrentMonth> queryCurrentMonth(String serviceId,String from,String to) throws Exception{
		return currentDao.queryCurrentMonth(serviceId, from, to);
	}
	
	public List<CurrentDay> queryCurrentDay(String serviceId,String from,String to) throws Exception{
		return currentDao.queryCurrentDay(serviceId, from, to);
	}

	public CurrentDao getCurrentDao() {
		return currentDao;
	}

	public void setCurrentDao(CurrentDao currentDao) {
		this.currentDao = currentDao;
	}
	
	
}
