package com.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bean.DataOpenSMS;
import com.bean.VIP;
import com.cache.CacheAction;
import com.common.BaseAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.VIPService;

@Controller
public class VIPAction extends BaseAction{
	
	@Resource
	VIPService vipService;
	
	@ResponseBody
	@RequestMapping(value="/queryVIP", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public String queryVIP(@RequestParam String serviceid) throws Exception{
		try {
			VIP result = vipService.queryVIP(serviceid);
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
	@RequestMapping(value="/addVIP", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public String addVIP(@RequestParam String serviceid) throws Exception{
		try {
			VIP result = vipService.addVIP(serviceid);
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
	@RequestMapping(value="/deleteVIP", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public String deleteVIP(@RequestParam String serviceid) throws Exception{
		try {
			VIP result = vipService.deleteVIP(serviceid);
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
	@RequestMapping(value="/sendVIP", method = RequestMethod.POST,produces ="application/json;charset=BIG5")
	public String sendVIP(@RequestParam String serviceid,@RequestParam String msisdn,@RequestParam String msg) throws Exception{
		try {
			CacheAction.logger.info("Send VIP SMS to "+msisdn);
			vipService.sendVIP(serviceid,msisdn,msg);
			return setSuccess("SUCCESS");
		} catch (Exception e) {
			try {
				return errorHandle(e);
			} catch (JsonProcessingException e1) {
				return null;
			}
		}
	}

	public VIPService getVipService() {
		return vipService;
	}

	public void setVipService(VIPService vipService) {
		this.vipService = vipService;
	}
}
