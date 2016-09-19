package com.it.common.component.socket;

import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;
import com.it.common.component.lang.str.StrUtil;
import com.it.common.component.log.LogPrint;
import com.it.common.share.GlobalName;

public class RemoteUtil {
	
	private String appUrl;

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	
	/**
	 * 获得远程对象
	 * */
	public <T> T getRemoteDto(Class<T> cls){
		String url = appUrl +StrUtil.firstLowerCase(cls.getSimpleName());
		return getRemoteObj(cls, url);
	}
	
	@SuppressWarnings("unchecked")
    protected static <T> T getRemoteObj(Class<T> cls, String url){
		T t = null;
		HessianProxyFactory factory = new HessianProxyFactory();
	    factory.setConnectTimeout(GlobalName.CONNECTION_TIMEOUT);
	    factory.setReadTimeout(GlobalName.SO_TIMEOUT);
	    try {
	    	t = (T)factory.create(cls, url);
	    } catch (MalformedURLException e) {  
	        LogPrint.error("Hession 数据获得异常");
	        e.printStackTrace();
	    }  
		return t;
	}

}
