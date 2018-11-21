package com.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bean.Subscriber;
import com.cache.XlsView;
import com.common.BaseAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.SubscriberService;


@Controller
public class SubscriberAction extends BaseAction{

	public String input;
	
	@Resource
	SubscriberService subscriberService;
	
	//produces 宣告，避免後端傳到前端時變亂碼
	@ResponseBody
	@RequestMapping(value="/querySubscriber", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String querySubscriber(@RequestParam String type, @RequestParam String input){
		
		System.out.println(type+":"+input);
		List<Subscriber> result = null;
		
		try {
			if("id".equalsIgnoreCase(type)) {
				result = subscriberService.queryListById(input);
			}else if("name".equalsIgnoreCase(type)) {
				result = subscriberService.queryListByName(input);
			}else if("s2tm".equalsIgnoreCase(type)) {
				result = subscriberService.queryListByS2tMisidn(input);
			}else if("home".equalsIgnoreCase(type)) {
				result = subscriberService.queryListByHomeMsisdn(input);
			}else if("vln".equalsIgnoreCase(type)) {
				result = subscriberService.queryListByVLN(input);
			}else if("imsi".equalsIgnoreCase(type)) {
				result = subscriberService.queryListByS2tIMSI(input);
			}else if("psid".equalsIgnoreCase(type)) {
				result = subscriberService.queryListByPassPortId(input);
			}
			return setSuccess(result);
		} catch (Exception e) {
			try {
				return errorHandle(e);
			} catch (JsonProcessingException e1) {
				return null;
			}
		}
	}
	
	//produces 宣告，避免後端傳到前端時變亂碼
		@ResponseBody
		@RequestMapping(value="/querySubscribersData", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
		public String querySubscribersData(@RequestParam String idTaxid){
			
			System.out.println("ID:"+idTaxid);
			Subscriber result = null;
			try {
				result = subscriberService.querySubscribersData(idTaxid);
				return setSuccess(result);
			} catch (Exception e) {
				try {
					return errorHandle(e);
				} catch (JsonProcessingException e1) {
					return null;
				}
			}
		}
	
	@ResponseBody
	@RequestMapping(value="/addOrUpdate", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String addOrUpdate(@ModelAttribute Subscriber s){
		
		try {
			Subscriber result = subscriberService.addOrUpdate(s);
			return setSuccess(result);
		} catch (Exception e) {
			try {
				return errorHandle(e);
			} catch (JsonProcessingException e1) {
				return null;
			}
		}
	}
	
	@RequestMapping(value = "/downloadSubscriberExcel", method = RequestMethod.GET,produces ="application/json;charset=UTF-8")  
    @ResponseBody  
    public ModelAndView report(ModelMap model,HttpServletRequest request, HttpServletResponse response,
    		@RequestParam String dateS,@RequestParam String dateE) {  
		XlsView xlsView = new XlsView();
		Workbook wb = null;
		Map m = new HashMap();
		try {
			wb = subscriberService.createSubscribersExcel(dateS, dateE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {    
			m.put("name", "customer_"+dateS+".xls");
			
			xlsView.buildExcelDocument(m, wb, request, response);    
	          } catch (Exception e) {    
	              e.printStackTrace();    
	          }    
		
		return new ModelAndView(xlsView,model);    
    }  


	/******************************************************************/
	public SubscriberService getSubscriberService() {
		return subscriberService;
	}

	public void setSubscriberService(SubscriberService subscriberService) {
		this.subscriberService = subscriberService;
	}	
	
	
}
