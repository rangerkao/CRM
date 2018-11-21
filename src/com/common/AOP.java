package com.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.action.LogAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
@Aspect
public class AOP {
	
	@Resource
	LogAction logAction;
	
	//AOP Advice Types
	//@Before,@After(finally),@AfterReturning,@AfterThrowing@Around
	
	//JoinPoint使用方式
	
	//方法一  直接使用方法標示
	//@Before("execution(* com.journaldev.spring.service.*.get*())")
	//@AfterThrowing("within(com.journaldev.spring.model.Employee)")
	
	//方法二 使用pointcut
	//@Pointcut("within(com.journaldev.spring.service.*)")
	//public void allMethodsPointcut(){}
	//
	//@Before(value = "allMethodsPointcut()")
	
	//Proceedingjoinpoint 繼承JoinPoint，基於JoinPoint暴露Proceed，只能使用在Around
	
	//AspectJ切入点语法
	//*：匹配任何数量字符；
    //..：匹配任何数量字符的重复，如在类型模式中匹配任何数量子包；而在方法参数模式中匹配任何数量参数。
    //+：匹配指定类型的子类型；仅能作为后缀放在类型模式后边。
	//例如java.lang.Number+ ,匹配java.lang包下的任何Number的自类型； 如匹配java.lang.Integer，也匹配java.math.BigInteger
	
	//AspectJ使用 且（&&）、或（||）、非（！）来组合切入点表达式。
	//例如 execution(* com.action.*.*(..)) && !execution(* com.action.LogAction.*(..))
	
	//@AfterReturning(value = "actionCut()", returning= "result") //可添加輸入參數returning
	// @AfterThrowing(value = "actionCut()", throwing= "error")//可添加輸入參數throwing
	
	@Pointcut(value = "execution(* com.action.SubscriberAction.addOrUpdate(..)) || "
			+ "execution(* com.action.NameVerifiedAction.addNameVerifiedData(..)) ||"
			+ "execution(* com.action.NameVerifiedAction.updateNameVerifiedData(..)) ||"
			+ "execution(* com.action.SMSAction.sendDataOpenSMS(..)) ||"
			+ "execution(* com.action.VIPAction.addVIP(..)) ||"
			+ "execution(* com.action.VIPAction.deleteVIP(..)) ||"
			+ "execution(* com.action.VIPAction.sendVIP(..))"
			+ "")
	public void actionCut() {}

	
	@Before("execution(* com.action.*.*(..))")
	public void beforeAction(JoinPoint joinpoint) throws JsonProcessingException {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		
		//設定Cache 時限
		response.setHeader("Cache-Control","max-age=0");
		System.out.println("set Cache Time");
	}
	
	@Around("actionCut()") //定義環繞通知
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		

		Object account = null;
		if(request.getSession()!=null)
			account = request.getSession().getAttribute("s2t.account");

		
		
		//驗證失敗
		if(account==null) {
			String msg = "please login!";
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("error", msg);
			return new ObjectMapper().writeValueAsString(map);
		}{
			String params = "";
			//加上.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);使遇到null或空值時不會出錯
			ObjectMapper mapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			for(Object s:pjp.getArgs()) {
				params += mapper.writeValueAsString(s)+",";
			}
			if(!"".equals(params))
				params=params.substring(0,params.length()-1);
			
			String function = pjp.getStaticPart().toString();
			
			//繼續進行
			Object object = pjp.proceed();
			
			String result = (object==null ?"":object.toString());
			logAction.actionLog(account.toString(), params, function, result);
			return object;
		}
	}

	
	
	public LogAction getLogAction() {
		return logAction;
	}

	public void setLogAction(LogAction logAction) {
		this.logAction = logAction;
	}
}
