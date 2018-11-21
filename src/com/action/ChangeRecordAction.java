package com.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bean.ChangeRecord;
import com.common.BaseAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.ChangeRecordService;

@Controller
public class ChangeRecordAction extends BaseAction{
	
	@Resource
	ChangeRecordService changeRecordService;

	@ResponseBody
	@RequestMapping(value="/queryNumberChangeRecord", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public String queryNumberChangeRecord(@RequestParam String serviceid) throws Exception {
		try {
			List<ChangeRecord> result = changeRecordService.queryNumberChangeRecord(serviceid);
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
	@RequestMapping(value="/queryCardChangeRecord", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public String queryCardChangeRecord(@RequestParam String serviceid) throws Exception {
		try {
			List<ChangeRecord> result = changeRecordService.queryCardChangeRecord(serviceid);
			return setSuccess(result);
		} catch (Exception e) {
			try {
				return errorHandle(e);
			} catch (JsonProcessingException e1) {
				return null;
			}
		}
	}
}
