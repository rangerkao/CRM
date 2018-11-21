package com.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bean.DataOpenSMS;
import com.bean.QosBean;
import com.bean.SMS;
import com.cache.CacheAction;
import com.common.BaseAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.SMSService;

@Controller
public class SMSAction extends BaseAction{

	@Resource
	SMSService sMSService;
	
	
	@ResponseBody
	@RequestMapping(value="/querySMS", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String queryQos(@RequestParam String s2tMsisdn,@RequestParam String chtMsisdn,@RequestParam String activatedDate,@RequestParam String canceledDate,@RequestParam String startDate,@RequestParam String endDate) {
		try {
			List<SMS> result = sMSService.querySMS(s2tMsisdn, chtMsisdn, startDate, endDate, activatedDate, canceledDate);
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
	@RequestMapping(value="/queryDataOpenSMS", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String queryDataOpenSMS() {
		try {
			DataOpenSMS result = sMSService.queryDataOpenSMS();
			return setSuccess(result);
		} catch (Exception e) {
			try {
				return errorHandle(e);
			} catch (JsonProcessingException e1) {
				return null;
			}
		}
	}
	
	//**因為DB以big5與iso8859-1，這邊以big5接收
	@ResponseBody
	@RequestMapping(value="/sendDataOpenSMS", method = RequestMethod.POST,produces ="application/json;charset=BIG5")
	public  String sendDataOpenSMS(@RequestParam String msisdn,@RequestParam List<String> msgs) {
		try {
			CacheAction.logger.info("Send Data Open SMS to "+msisdn);
			return setSuccess(sMSService.sendDataOpenSMS(msisdn, msgs));
		} catch (Exception e) {
			try {
				return errorHandle(e);
			} catch (JsonProcessingException e1) {
				return null;
			}
		}
	}

	public SMSService getsMSService() {
		return sMSService;
	}

	public void setsMSService(SMSService sMSService) {
		this.sMSService = sMSService;
	}
	
	
	
}
