package com.it.common.component.cache;


public abstract class CacheBo {
	
	public abstract boolean set(String key, Object value);
	
	public abstract void setWithNoReply(String key, Object value);
	
	public abstract boolean add(String key, Object value);
	
	public abstract boolean replace(String key, Object value);
	
	public abstract boolean delete(String key);
	
	public abstract <T> T get(String key);

}
