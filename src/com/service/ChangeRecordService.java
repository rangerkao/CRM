package com.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bean.ChangeRecord;
import com.dao.ChangeRecordDao;

@Service
public class ChangeRecordService {

	@Resource
	ChangeRecordDao changeRecordDao;
	
	public List<ChangeRecord> queryNumberChangeRecord(String serviceid) throws Exception {
		return changeRecordDao.queryNumberChangeRecord(serviceid);
	}
	
	public List<ChangeRecord> queryCardChangeRecord(String serviceid) throws Exception {
		return changeRecordDao.queryCardChangeRecord(serviceid);
	}

	public ChangeRecordDao getChangeRecordDao() {
		return changeRecordDao;
	}

	public void setChangeRecordDao(ChangeRecordDao changeRecordDao) {
		this.changeRecordDao = changeRecordDao;
	}
	
}
