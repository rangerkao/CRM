package com.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bean.Subscriber;
import com.cache.CacheAction;
import com.common.BaseAction;
import com.service.UniversalService;

@Controller
public class UniversalAction extends BaseAction{

	@Resource
	UniversalService universalService;
	
	//簡訊申請
	@ResponseBody
	@RequestMapping(value="/SMSCRoute")
	public String SMSCRoute(HttpServletRequest request) {
		try {
	
			CacheAction.logger.info("Get SMSCRoute data:"+request.getQueryString());
			
			String number = request.getParameter("Sender");
			//把加號移除
			number = number.replace("%2B", "");
			String text = request.getParameter("Text");
			String code = null;
			if(text!=null) {
				text = text.toUpperCase();
				Pattern t = Pattern.compile("CHP\\d+");
				Matcher m = t.matcher(text);
				if(m.find()) 
					code = m.group();
			}
			
			
			if(number != null) {
				String resMsg = "";
				Subscriber s = universalService.checkNumber(number);
				
				if(s==null || s.getServiceId()==null) {
					resMsg = "無效的號碼或查無用戶資料。";
				}else if(!"139".equals(s.getPricePlanId().getId())) {
					resMsg = "此服務僅限環球卡用戶申辦。";
				}else if(s.getCanceledDate()!=null && !"".equals(s.getCanceledDate())) {
					resMsg = "你已退租環球卡服務，請重新申辦。";
				}else {
					if(code==null) {
						resMsg = "無效的內容。";
					/*}else if("CHP00".equals(code)) {
							resMsg = universalService.provisionCHP(s, "CHP00");*/
					}else if("CHP01".equals(code)) {
							resMsg = universalService.provisionCHP(s, "CHP01");
					}else if("CHP03".equals(code)) {
							resMsg = universalService.provisionCHP(s, "CHP03");
					}else {
						resMsg = "無效的內容。";
					}
				}
				
				if(resMsg != null && !"".equals(resMsg)) {
					
					if("000".equals(resMsg)) {
						resMsg = "申請成功";
					}else if("423".equals(resMsg)) {
						resMsg = "無效的代碼";
					}else if("4231".equals(resMsg)) {
						resMsg = "已經有預約項目";
					}else if("4232".equals(resMsg)) {
						resMsg = "用量達70％已上才能預約";
					}else if("431".equals(resMsg)) {
						resMsg = "請先退月租型服務後再行申請";
					}else if("600".equals(resMsg)) {
						resMsg = "申請失敗，請洽客服";
					}
					CacheAction.sendSMS(resMsg, number, "SMSCRoute");
				}
				
			}else {
				CacheAction.logger.info("號碼無效");
			}
			
			

			return "Success Get request String : "+request.getQueryString();
		} catch (Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			
			CacheAction.sendMail("CRM ERROR", "At SMSCRoute occured error.", "CRM_SERVICE", "ranger.kao@sim2travel.com");
			return "供裝失敗，"+message;
		}
	}
	
	//中華供裝
	@ResponseBody
	@RequestMapping(value="/TWNLDRoute")
	public String TWNLDRoute(HttpServletRequest request) {
		try {
	
			CacheAction.logger.info("Get TWNLDRoute data:"+request.getQueryString());
			
			String addoncode = request.getParameter("addoncode");
			String addonaction = request.getParameter("addonaction");
			String phonenumber = request.getParameter("phonenumber");
			Subscriber s = universalService.checkNumber(phonenumber);
			String resMsg = null;
			if("D".equalsIgnoreCase(addonaction)) {
				resMsg = universalService.provisionCHP(s, "CHP00");
			}else if("SX007".equalsIgnoreCase(addoncode)) {
				resMsg = universalService.provisionCHP(s, "CHP01");
			}else if("SX008".equalsIgnoreCase(addoncode)) {
				resMsg = universalService.provisionCHP(s, "CHP03");
			}

			return resMsg;
		} catch (Exception e) {
			e.printStackTrace();
			CacheAction.logger.error(e.getMessage());
			return "600";
		}
	}

	//官網申請
	@ResponseBody
	@RequestMapping(value="/WEBRoute")
	public String WEBRoute(HttpServletRequest request) {
		try {
	
			CacheAction.logger.info("Get rounting data:"+request.getQueryString());

			return "Success Get request String : "+request.getQueryString();
		} catch (Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			return "Failed! "+message;
		}
	}

	public UniversalService getUniversalService() {
		return universalService;
	}

	public void setUniversalService(UniversalService universalService) {
		this.universalService = universalService;
	}
}
