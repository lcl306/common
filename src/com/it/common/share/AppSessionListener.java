package com.it.common.share;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.it.common.component.log.LogPrint;

/**
 * httpSession listener
 * @author liu
 * */
public class AppSessionListener implements HttpSessionListener, HttpSessionAttributeListener{
	
	
	public static ThreadLocal<String> keys = new ThreadLocal<String>();
	
	public static String SESSION_ATRRIBUTE_MAP_NAME = "session_attribute_map";

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		LogPrint.info("session created");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		event.getSession().invalidate();
		LogPrint.info("session destroyed");
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		String key = keys.get()!=null?keys.get():"";
		getSessionAtrributeMap(event.getSession()).put(event.getName()+key, event.getValue());
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		String key = keys.get()!=null?keys.get():"";
		getSessionAtrributeMap(event.getSession()).remove(event.getName()+key);
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		this.attributeAdded(event);
	}
	
	public static Object atrributeGet(HttpSession session, String name){
		String key = keys.get()!=null?keys.get():"";
		return getSessionAtrributeMap(session).get(name+key);
	}
	
	public static Map<String, Object> getSessionAtrributeMap(HttpSession session){
		try{
			@SuppressWarnings("unchecked")
			Map<String, Object> attrMap = (Map<String, Object>)session.getAttribute(SESSION_ATRRIBUTE_MAP_NAME);
			if(attrMap==null){
				attrMap = new HashMap<String, Object>();
				session.setAttribute(SESSION_ATRRIBUTE_MAP_NAME, attrMap);
			}
			return attrMap;
		}catch(Exception e){
			return new HashMap<String, Object>();
		}
	}

}
