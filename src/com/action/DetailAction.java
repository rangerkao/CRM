package com.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bean.Detail;
import com.common.BaseAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.DetailService;

@Controller
public class DetailAction extends BaseAction{
	
	@Resource
	DetailService detailService;
	
	@ResponseBody
	@RequestMapping(value="/queryDetail", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String queryDetail(@RequestParam String serviceid) {
		try {
			Detail result = detailService.queryDetail(serviceid);
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
	@RequestMapping(value="/queryWhetherAppliedCHNA", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String queryWhetherAppliedCHNA(@RequestParam String serviceid) {
		try {
			String result = detailService.queryWhetherAppliedCHNA(serviceid);
			return setSuccess(result);
		} catch (Exception e) {
			try {
				return errorHandle(e);
			} catch (JsonProcessingException e1) {
				return null;
			}
		}
	}

	public DetailService getDetailService() {
		return detailService;
	}

	public void setDetailService(DetailService detailService) {
		this.detailService = detailService;
	}
	
	

}
