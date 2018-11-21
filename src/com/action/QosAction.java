package com.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bean.Detail;
import com.bean.QosBean;
import com.common.BaseAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.QosService;

@Controller
public class QosAction extends BaseAction{
	
	@Resource
	QosService qosService;
	
	@ResponseBody
	@RequestMapping(value="/queryQos", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String queryQos(@RequestParam String imsi,@RequestParam String msisdn,@RequestParam String activatedDate,@RequestParam String canceledDate) {
		try {
			List<QosBean> result = qosService.queryQosList(imsi, msisdn, activatedDate, canceledDate);
			return setSuccess(result);
		} catch (Exception e) {
			try {
				return errorHandle(e);
			} catch (JsonProcessingException e1) {
				return null;
			}
		}
	}

	public QosService getQosService() {
		return qosService;
	}

	public void setQosService(QosService qosService) {
		this.qosService = qosService;
	}
	
	

}
