package com.it.common.share;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * get spring bean
 * @author liu
 * */
public class SpringBeanStore implements ServletContextListener {
	
	static WebApplicationContext ctx = null;

	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		ctx = WebApplicationContextUtils.getWebApplicationContext(sc);
	}

	public void contextDestroyed(ServletContextEvent event) {
		ctx = null;
	}
	
	/**
	 * get spring bean from web
	 * */
	public static Object getBean(String beanName){
		return ctx.getBean(beanName);
	}
	
	static ClassPathXmlApplicationContext ptx = null;
	
	/**
	 * get spring bean from xml path
	 * */
	public static Object getPathBean(String beanName){
		if(ptx==null){
			ptx = new ClassPathXmlApplicationContext(new String[]{"com/it/context/base/*.xml"});
		}
		return ptx.getBean(beanName);
	}

}
