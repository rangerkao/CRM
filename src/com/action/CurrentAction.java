package com.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bean.CurrentDay;
import com.bean.CurrentMonth;
import com.bean.Detail;
import com.common.BaseAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.CurrentService;

@Controller
public class CurrentAction extends BaseAction{

	@Resource
	CurrentService currentService;
	
	@ResponseBody
	@RequestMapping(value="/queryCurrentMonth", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String queryCurrentMonth(@RequestParam String serviceid,@RequestParam String startDate,@RequestParam String endDate) {
		try {
			List<CurrentMonth> result = currentService.queryCurrentMonth(serviceid, startDate, endDate);
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
	@RequestMapping(value="/queryCurrentDay", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String queryCurrentDay(@RequestParam String serviceid,@RequestParam String startDate,@RequestParam String endDate) {
		try {
			List<CurrentDay> result = currentService.queryCurrentDay(serviceid, startDate, endDate);
			return setSuccess(result);
		} catch (Exception e) {
			try {
				return errorHandle(e);
			} catch (JsonProcessingException e1) {
				return null;
			}
		}
	}

	public CurrentService getCurrentService() {
		return currentService;
	}

	public void setCurrentService(CurrentService currentService) {
		this.currentService = currentService;
	}
}
