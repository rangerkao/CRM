package com.action;

import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cache.CacheAction;
import com.service.LogService;

@Controller
public class LogAction {
	
	@Resource
	LogService logService;
	
	
	@RequestMapping(value= {"", "/", "index"})
	public ModelAndView index() {
 		return new ModelAndView("index");
	}

	@RequestMapping(value="/login", method = RequestMethod.POST)
	public ModelAndView login(@RequestParam("account") String account,@RequestParam("password") String password,HttpSession session) {
		System.out.println(account+":"+password);
		
		try {
			logService.checkAccount(session, account, password);
			return new ModelAndView("subscriber");
		} catch (Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			return new ModelAndView("index", "message", message);
		}
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session) {
		String message = "已登出";
		try {
			session.invalidate();
			return new ModelAndView("index", "message", message);
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("index", "message", message);
		}
	}

	
	public void actionLog(String account,String params,String function,String result) {
		try {
			logService.actionLog(account, params, function,result);
		} catch (SQLException e) {
		} catch (Exception e) {
		}
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}
}
