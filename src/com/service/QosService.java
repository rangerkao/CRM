package com.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bean.QosBean;
import com.dao.QosDao;

@Service
public class QosService {

	@Resource
	QosDao qosDao;
	
	//查詢列表
	public List<QosBean> queryQosList(String imsi,String msisdn,String activatedDate,String canceledDate) throws Exception{
		return qosDao.queryQosList(imsi, msisdn.replaceAll("^852", ""), activatedDate, canceledDate);
	}

	public QosDao getQosDao() {
		return qosDao;
	}

	public void setQosDao(QosDao qosDao) {
		this.qosDao = qosDao;
	}
	
	
}
