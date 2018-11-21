package com.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bean.Detail;
import com.dao.DetailDao;

@Service
public class DetailService {
	
	@Resource
	DetailDao detailDao;
	
	public Detail queryDetail(String serviceid) throws Exception {
		return detailDao.queryDetail(serviceid);
	}
	
	public String queryWhetherAppliedCHNA(String serviceId) throws Exception {
		return detailDao.queryWhetherAppliedCHNA(serviceId);
	}

	public DetailDao getDetailDao() {
		return detailDao;
	}

	public void setDetailDao(DetailDao detailDao) {
		this.detailDao = detailDao;
	}
	
	

}
