package com.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bean.NameVerified;
import com.bean.NameVerifiedSet;
import com.common.BaseAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.NameVerifiedService;

@Controller
public class NameVerifiedAction extends BaseAction{

	@Resource
	NameVerifiedService nameVerifiedService;
	
	
	//傳送一個Json字串，接收Json字串為@RequestBody
	@ResponseBody
	@RequestMapping(value="/queryNameVarifiedData", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	//@ModelAttribute 會自動把參數對應到的綁定
	public  String queryNameVarifiedData(@ModelAttribute NameVerified c) {
		try {
			NameVerifiedSet result = nameVerifiedService.queryNameVarifiedData(c);
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
	@RequestMapping(value="/addNameVerifiedData", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String addNameVerifiedData(@ModelAttribute NameVerified c) {
		try {
			String result = nameVerifiedService.addNameVarifiedData(c);
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
	@RequestMapping(value="/updateNameVerifiedData", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String updateNameVerifiedData(@ModelAttribute NameVerified c) {
		try {
			String result = nameVerifiedService.updateNameVarifiedData(c);
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
	@RequestMapping(value="/searchNameVarifiedData", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String searchNameVarifiedData(@RequestParam String input,@RequestParam String type) {
		try {
			System.out.println(input+":"+type);
			List<NameVerified> result = nameVerifiedService.searchNameVarifiedData(input, type);
			return setSuccess(result);
		} catch (Exception e) {
			try {
				return errorHandle(e);
			} catch (JsonProcessingException e1) {
				return null;
			}
		}
	}
	
	

	public NameVerifiedService getNameVerifiedService() {
		return nameVerifiedService;
	}

	public void setNameVerifiedService(NameVerifiedService nameVerifiedService) {
		this.nameVerifiedService = nameVerifiedService;
	}
	
	
}
