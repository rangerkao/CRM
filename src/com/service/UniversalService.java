package com.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bean.Subscriber;
import com.cache.CacheAction;
import com.dao.ProvisionDao;
import com.dao.SubscriberDao;
import com.dao.UniversalDao;

@Service
public class UniversalService {

	@Resource
	UniversalDao universalDao;
	
	@Resource
	SubscriberDao subscriberDao;
	
	@Resource
	ProvisionDao provisionDao;
	
	public Subscriber checkNumber(String number) throws Exception {
		//取得serviceid
		Subscriber s = subscriberDao.queryServiceIdbyHomeMsisdn(number);
		
		if(s!=null && s.getServiceId()!=null && !"".equals(s.getServiceId())) {
			//取得用戶資料
			s = subscriberDao.queryDataByServiceId(s.getServiceId());
		}
		
		return s;
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public String provisionCHP(Subscriber s,String type) throws Exception{
		
		String serviceid = s.getServiceId();
		
		Set<String> addonServicies = universalDao.queryAddonService(serviceid);
		
		if("CHP00".equals(type)) {
			boolean result = false;
			//轉日租型，隔日生效，這邊進行退SX007或是SX008的動作
			if(addonServicies.contains("SX007")){
				result = universalDao.delService_N(serviceid, "SX007");
			}else if (addonServicies.contains("SX008")) {
				result = universalDao.delService_N(serviceid, "SX008");
			}
			
			if(result) {
				String  endDate = universalDao.getLastVolumePocket(serviceid, 6);
				//今天是否在流量包中
				//No->恢復漫遊區域
				if(endDate == null || (sdf.parse(endDate).getTime()+24*60*60*1000)<new Date().getTime() ) {
					String roammingRestriction = universalDao.queryRoammingRestriction(s.getServiceId());
					if("CHT-O-no-CNHK".equalsIgnoreCase(roammingRestriction)) {
						try {
							provisionDao.changeRoammingRestriction(s.getS2tIMSI(), s.getS2tMsisdn(),"8");
						} catch (Exception e) {
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
							CacheAction.sendMail("CRM Provision Error:"+e.getMessage(), "CRM Provision Error:"+e.toString(), "CRM_ERROR", "ranger.kao@sim2travel.com");
							return "600";
						}
					}
				}
				return "000";
			}else {
				return "000";
			}
		}else //轉輕量型
			if("CHP01".equals(type) || "CHP03".equals(type)) {
				
				int t = 0;
				long limit = 0;
				String limitG = null;
				String addonCode = null;
				if("CHP01".equals(type)) {
					t = 1;
					limit = 1*1024*1024*1024;
					limitG = "1G";
					addonCode = "SX007";
				}else {
					t = 2;
					limit = 3*1024*1024*1024;
					limitG = "3G";
					addonCode = "SX008";
				}
				
				
				if(addonServicies.contains("SX007")||addonServicies.contains("SX008")) {
					//已經有輕量包
					//是否已經用到70%
					Long volume = universalDao.getCHPVolume(serviceid, 6);
					
					
					//無流量記錄
					if(volume==null) {
						Date today = new Date();
						//插入包裝
						String startDate = sdf.format(today);
						String endDate = sdf.format(today.getTime()+7*24*60*60*1000);
						
						//建立流量生效記錄
						if(universalDao.addVolumePocket(serviceid, startDate, endDate, limit, s.getS2tIMSI())) {
							String msg = universalDao.querySMSContent("118");
							msg = msg.replace("{{date_start}}", startDate)
									.replace("{{date_end}}", endDate)
									.replace("{{limit}}", limitG);
							CacheAction.sendSMS(msg, s.getS2tMsisdn(), type);

							//回覆漫遊區域
							String roammingRestriction = universalDao.queryRoammingRestriction(s.getServiceId());
							if("CHT-O-no-CNHK".equalsIgnoreCase(roammingRestriction)) {
								try {
									provisionDao.changeRoammingRestriction(s.getS2tIMSI(), s.getS2tMsisdn(),"8");
								} catch (Exception e) {
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									CacheAction.sendMail("CRM Provision Error:"+e.getMessage(), "CRM Provision Error:"+e.toString(), "CRM_ERROR", "ranger.kao@sim2travel.com");
									return "600";
								}
							}
							return "000";
						}else {
							return "600";
						}

					}else //是否已經達到70%
						if(volume>=limit*0.7) {
						//如果沒有預約則進行下一次的預約
						if(!universalDao.queryCHPReserve(serviceid)) {
							if(universalDao.addCHPReserve(serviceid, t)) {
								return "000";
							}else {
								return "600";
							}
						}else {
							return "4321";
						}
					}else {
						return "4322";
					}
				}else {
					Date today = new Date();
					if(addonServicies.contains("SX001")||addonServicies.contains("SX002")||	addonServicies.contains("SX005")) {
						//如果還有月租服務，必須請用戶退租後再申請輕量包
						return "431";
					}else {
						//新增AddonService紀錄
						universalDao.addAddonService(s.getHomeIMSI(), s.getChtMsisdn(), s.getS2tIMSI(), s.getS2tMsisdn(), addonCode,"A");
						universalDao.addAddonService_N(serviceid, s.getHomeIMSI(), s.getChtMsisdn(), s.getS2tIMSI(), s.getS2tMsisdn(), addonCode);

						
						String lastServiceDate = universalDao.getAddonServiceLastDate(serviceid, "'SX001','SX0002','SX004','SX005','SX007','SX008'");
						if(sdf.format(today).equals(lastServiceDate)) {
							//如果今天才退月租，由月租轉輕量，延後一天生效
							today = new Date(today.getTime()+1*24*60*60*1000);
						}else {
							//日租轉輕量直接生效，當日用量併入輕量包計算
						}
						//插入包裝
						String startDate = sdf.format(today);
						String endDate = sdf.format(today.getTime()+7*24*60*60*1000);
						
						//建立生效記錄
						if(universalDao.addVolumePocket(serviceid, startDate, endDate, limit, s.getS2tIMSI())) {
							String msg = universalDao.querySMSContent("118");
							msg = msg.replace("{{date_start}}", startDate)
									.replace("{{date_end}}", endDate)
									.replace("{{limit}}", limitG);
							CacheAction.sendSMS(msg, s.getS2tMsisdn(), type);
							
							return "000";
						}else {
							return "600";
						}
					}	
				}

		}else {
			return "423";
		}
		
	}
	

	public UniversalDao getUniversalDao() {
		return universalDao;
	}

	public void setUniversalDao(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}


	public SubscriberDao getSubscriberDao() {
		return subscriberDao;
	}


	public void setSubscriberDao(SubscriberDao subscriberDao) {
		this.subscriberDao = subscriberDao;
	}


	public ProvisionDao getProvisionDao() {
		return provisionDao;
	}


	public void setProvisionDao(ProvisionDao provisionDao) {
		this.provisionDao = provisionDao;
	}
	
}
