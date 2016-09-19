package com.it.common.component.log;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

/**
 * used for spring log
 * @author liu
 * */
public class LogBeforeAdvisor implements MethodBeforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		StringBuffer info = new StringBuffer();
		info.append(target.getClass().getName()).append(".").append(method.getName()).append(" start ");
		LogPrint.debug(info.toString());
	}

}
