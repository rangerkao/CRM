package com.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bean.NameVerified;
import com.bean.NameVerifiedSet;
import com.dao.NameVerifiedDao;

@Service
public class NameVerifiedService {
	
	@Resource
	NameVerifiedDao nameVerifiedDao;
	
	
	
	public NameVerifiedSet queryNameVarifiedData(NameVerified c) throws Exception{
		NameVerifiedSet result = new NameVerifiedSet();
		List<NameVerified> history = new ArrayList<NameVerified>();
		//搜尋此vln的認證狀態
		List<NameVerified> byVln = searchNameVarifiedData(c.getVln(), "vln"); 
		for(int i = 0 ;i<byVln.size();i++) {
			if("1".equals(byVln.get(i).getStatus()) && c.getMsisdn().equals(byVln.get(i).getMsisdn())) {
				result.setCurrent(byVln.remove(i));
				break;
			}
		}

		history.addAll(byVln);

		//歷史加入同門號
		List<NameVerified> byMsisdn = searchNameVarifiedData(c.getMsisdn(), "msisdn");
		Iterator<NameVerified> it = byMsisdn.iterator();
		while(it.hasNext()) {
			NameVerified n = it.next();
			if(c.getVln().equals(n.getVln())){
				it.remove();
			}
		}
		
		history.addAll(byMsisdn);
		result.setHistory(history);
		
		return result;
	}
	
	public String  addNameVarifiedData(NameVerified c) throws Exception{
		return nameVerifiedDao.addNameVarifiedData(c);
	}
	
	public String  updateNameVarifiedData(NameVerified c) throws Exception{
		return nameVerifiedDao.updateNameVarifiedData(c);
	}
	
	public List<NameVerified> searchNameVarifiedData(String input,String type) throws Exception{
		return nameVerifiedDao.queryNameVarifiedData(input, type);
	}
	
	
	

	public NameVerifiedDao getNameVerifiedDao() {
		return nameVerifiedDao;
	}

	public void setNameVerifiedDao(NameVerifiedDao nameVerifiedDao) {
		this.nameVerifiedDao = nameVerifiedDao;
	}
	
	
}
