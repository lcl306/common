package com.it.common.component.cache.memcache;

import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.exception.MemcachedException;

import com.it.common.component.cache.CacheBo;
import com.it.common.component.log.LogPrint;

public class MemcacheBo extends CacheBo{
	
	/**
	 * 如果EXPIRED_DAY=0，表示过期时间为最大存在时间=60*60*24*30（一个月）
	 * */
	public static int EXPIRED_DAY = 60*60*24;
	
	public boolean set(String key, Object value){
		try {
			return BaseMemcache.getMemcachedClient().set(key, EXPIRED_DAY, value);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	public void setWithNoReply(String key, Object value){
		try {
			BaseMemcache.getMemcachedClient().setWithNoReply(key, EXPIRED_DAY, value);
		} catch (InterruptedException | MemcachedException e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public boolean add(String key, Object value){
		try {
			return BaseMemcache.getMemcachedClient().add(key, EXPIRED_DAY, value);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean replace(String key, Object value){
		try {
			return BaseMemcache.getMemcachedClient().replace(key, EXPIRED_DAY, value);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean delete(String key){
		try {
			return BaseMemcache.getMemcachedClient().delete(key);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	public <T> T get(String key){
		try {
			return BaseMemcache.getMemcachedClient().get(key);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
