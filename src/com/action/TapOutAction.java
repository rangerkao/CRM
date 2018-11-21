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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bean.SMS;
import com.bean.TabOutData;
import com.cache.XlsView;
import com.common.BaseAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.TapOutService;

@Controller
public class TapOutAction extends BaseAction{

	@Resource
	TapOutService tapOutService;
	
	@ResponseBody
	@RequestMapping(value="/queryTapoutData", method = RequestMethod.POST,produces ="application/json;charset=UTF-8")
	public  String queryTapoutData(@RequestParam String from,@RequestParam String to,@RequestParam String serviceid,@RequestParam String type) {
		try {
			List<TabOutData> result = tapOutService.queryTapoutData(from, to, serviceid, type);
			return setSuccess(result);
		} catch (Exception e) {
			try {
				return errorHandle(e);
			} catch (JsonProcessingException e1) {
				return null;
			}
		}
	}
	
	@RequestMapping(value = "/downloadTapOutrExcel", method = RequestMethod.GET,produces ="application/json;charset=UTF-8")  
    @ResponseBody  
    public ModelAndView report(ModelMap model,HttpServletRequest request, HttpServletResponse response,
    		@RequestParam String from,@RequestParam String to,@RequestParam String serviceid,@RequestParam String type) {  
		XlsView xlsView = new XlsView();
		Workbook wb = null;
		Map m = new HashMap();
		try {
			wb = tapOutService.createTapoutExcel(from, to, serviceid, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {    
			m.put("name","TapOut_"+from+"-"+to+".xls");
			
			xlsView.buildExcelDocument(m, wb, request, response);    
	          } catch (Exception e) {    
	              e.printStackTrace();    
	          }    
		
		return new ModelAndView(xlsView,model);    
    }  

	public TapOutService getTapOutService() {
		return tapOutService;
	}

	public void setTapOutService(TapOutService tapOutService) {
		this.tapOutService = tapOutService;
	}
	
}
