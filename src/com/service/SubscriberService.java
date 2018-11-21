package com.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.bean.Subscriber;
import com.cache.Excel;
import com.cache.XlsView;
import com.dao.SubscriberDao;

@Service
public class SubscriberService{
	
	
	public SubscriberService() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Resource
	SubscriberDao subscriberDao;
	
	public List<Subscriber> queryListById(String idTaxid) throws Exception{
		System.out.println("queryListById");
		Set<String>  serviceids =  subscriberDao.queryListByIdTaxid(idTaxid);
		List<Subscriber> result = new ArrayList<Subscriber>();
		for(String serviceid:serviceids){
			if(!"".equals(serviceid))
				result.add(queryDataByServiceId(serviceid));
		}
		return result;
	}
	
	public Subscriber querySubscribersData(String idTaxid) throws Exception{
		System.out.println("querySubscribersData");
		Set<String>  serviceids =  subscriberDao.queryListByIdTaxid(idTaxid);
		Subscriber result = new Subscriber();
		for(String serviceid:serviceids){
			if(!"".equals(serviceid)) {
				result = queryDataByServiceId(serviceid);
				break;
			}
		}
		return result;
	}
	
	public List<Subscriber> queryListByS2tMisidn(String s2tMsisdn) throws Exception{
		System.out.println("queryListByS2tMisidn");
		List<Subscriber> result = subscriberDao.queryListByS2tMisidn(s2tMsisdn);
		return result;
	}
	
	public List<Subscriber> queryListByS2tIMSI(String s2tIMSI) throws Exception{
		System.out.println("queryListByS2tIMSI");
		List<Subscriber> result = subscriberDao.queryListByS2tIMSI(s2tIMSI);
		return result;
	}
	
	public List<Subscriber> queryListByVLN(String VLN) throws Exception{
		System.out.println("queryListByVLN");
		List<Subscriber> result = subscriberDao.queryListByVLN(VLN);
		return result;
	}
	
	public List<Subscriber> queryListByHomeMsisdn(String homeMsisdn) throws Exception{
		System.out.println("queryListByHomeMsisdn");
		List<Subscriber> result = subscriberDao.queryListByHomeMsisdn(homeMsisdn);
		return result;
	}
	
	public List<Subscriber> queryListByPassPortId(String id) throws Exception{
		System.out.println("queryListByPassPortId");
		List<Subscriber> result = subscriberDao.queryListByPassPortId(id);
		return result;
	}
	
	public List<Subscriber> queryListByName(String name) throws Exception{
		System.out.println("queryListByName");
		Set<String>  serviceids =  subscriberDao.queryListByName(name);
		List<Subscriber> result = new ArrayList<Subscriber>();
		for(String serviceid:serviceids){
			if(!"".equals(serviceid))
				result.add(queryDataByServiceId(serviceid));
		}
		return result;
	}

	public Subscriber queryDataByServiceId(String id) throws Exception{
		System.out.println("queryDataByServiceId");
		Subscriber result = subscriberDao.queryDataByServiceId(id);
		return result;
	}
	
	public Subscriber addOrUpdate(Subscriber s) throws Exception {
		return subscriberDao.addOrUpdate(s);
	}

	public  Workbook createSubscribersExcel(String dateS,String dateE) throws Exception{
		List<Map<String,String>> data = subscriberDao.querySubscribersExcel(dateS,dateE);
		
		List<Map<String,String>> head = new ArrayList<Map<String,String>>();
		head.add(Excel.mapSetting("審核日","VERIFIED_DATE"));
		head.add(Excel.mapSetting("中華主號","PARTNERMSISDN"));
		head.add(Excel.mapSetting("香港號碼","SERVICECODE"));
		head.add(Excel.mapSetting("ServiceID(帳務用)","SERVICEID"));
		head.add(Excel.mapSetting("開通時間","DATEACTIVATED"));
		head.add(Excel.mapSetting("退租時間","DATECANCELED"));
		head.add(Excel.mapSetting("客戶名稱","SUBS_NAME"));
		head.add(Excel.mapSetting("統一編號/身分證字號","SUBS_ID_TAXID"));
		head.add(Excel.mapSetting("公司戶負責人名稱","CHAIRMAN"));
		head.add(Excel.mapSetting("公司戶負責人身分證號","CHAIRMAN_ID"));
		head.add(Excel.mapSetting("聯絡電話","SUBS_PHONE"));
		head.add(Excel.mapSetting("生日","SUBS_BIRTHDAY"));
		head.add(Excel.mapSetting("戶籍地址","SUBS_PERMANENT_ADDRESS"));
		head.add(Excel.mapSetting("帳單地址","SUBS_BILLING_ADDRESS"));
		head.add(Excel.mapSetting("E-Mail","SUBS_EMAIL"));
		head.add(Excel.mapSetting("代辦處代號","AGENCY_ID"));
		head.add(Excel.mapSetting("其他備註","REMARK"));
		head.add(Excel.mapSetting("資費方案","PRICEPLANID_ALIASES"));

		HSSFWorkbook wb = (HSSFWorkbook) Excel.createExcel("xls", head, data);

        return wb;
	}

	protected SubscriberDao getSubscriberDao() {
		return subscriberDao;
	}
	protected void setSubscriberDao(SubscriberDao subscriberDao) {
		this.subscriberDao = subscriberDao;
	}	
	
	
	
}
