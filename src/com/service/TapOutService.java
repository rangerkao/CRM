package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.bean.TabOutData;
import com.cache.Excel;
import com.dao.TapOutDao;

@Service
public class TapOutService {

	@Resource
	TapOutDao tapOutDao;
	
	public List<TabOutData> queryTapoutData(String from ,String to ,String serviceid,String type) throws Exception{
		return tapOutDao.queryTapoutData(from, to, serviceid, type);
	}
	
	public  Workbook createTapoutExcel(String from ,String to ,String serviceid,String type) throws Exception{
		List<TabOutData> result = tapOutDao.queryTapoutData(from, to, serviceid, type);
		
		List<Map<String,String>> data = new ArrayList<Map<String,String>>();
		for(TabOutData t : result) {
			Map<String,String> m = new HashMap<String,String>();
			m.put("startDate", t.getStartDate());
			m.put("location", t.getLocation());
			m.put("phonenumber", t.getPhonenumber());
			m.put("type", t.getType());
			m.put("startTime", t.getStartTime());
			m.put("endTime", t.getEndTime());
			m.put("unit", t.getUnit());
			m.put("amount", t.getAmount());
			m.put("totalCharge", t.getTotalCharge());
			m.put("discountCharge", t.getDiscountCharge());
			m.put("finalCharge", t.getFinalCharge());
			data.add(m);
		}
		List<Map<String,String>> head = new ArrayList<Map<String,String>>();
		head.add(Excel.mapSetting("始話日期","startDate"));
		head.add(Excel.mapSetting("漫遊網","location"));
		head.add(Excel.mapSetting("發話號碼/收話號碼","phonenumber"));
		head.add(Excel.mapSetting("通話種類","type"));
		head.add(Excel.mapSetting("始話時刻","startTime"));
		head.add(Excel.mapSetting("終話時刻","endTime"));
		head.add(Excel.mapSetting("使用量(秒/則/Bytes)","unit"));
		head.add(Excel.mapSetting("漫遊費用","amount"));
		head.add(Excel.mapSetting("原始費用","totalCharge"));
		head.add(Excel.mapSetting("優惠費用","discountCharge"));
		head.add(Excel.mapSetting("結果費用","finalCharge"));

		HSSFWorkbook wb = (HSSFWorkbook) Excel.createExcel("xls", head, data);

        return wb;
	}
	

	public TapOutDao getTapOutDao() {
		return tapOutDao;
	}

	public void setTapOutDao(TapOutDao tapOutDao) {
		this.tapOutDao = tapOutDao;
	}

	
}
