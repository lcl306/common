package com.it.common.component.log;

import org.springframework.aop.ThrowsAdvice;

/**
 * used for spring log
 * @author liu
 * */
public class LogThrowsAdvisor implements ThrowsAdvice {
	
	public void afterThrowing(Throwable t){
		StringBuffer sb = new StringBuffer();
		sb.append(t.getMessage()).append("\r\n");
		StackTraceElement[] elems = t.getStackTrace();
		for(int i=0; i<elems.length; i++){
			sb.append("	at ").append(elems[i]);
			if(i!=elems.length-1) sb.append("\r\n");
		}
		LogPrint.error(sb.toString());
    }

}
