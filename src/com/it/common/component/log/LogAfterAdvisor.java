package com.it.common.component.log;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;

/**
 * used for spring log
 * @author liu
 * */
public class LogAfterAdvisor implements AfterReturningAdvice{

	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		StringBuffer info = new StringBuffer();
		info.append(target.getClass().getName()).append(".").append(method.getName()).append(" end ");
		LogPrint.debug(info.toString());
	}

}
