package com.it.common.component.cache;

import com.it.common.component.cache.memcache.MemcacheBo;

public class CacheBoFactory {
	
	protected static <T extends CacheBo> T getInstance(Class<T> cls){
		try {
			return cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static CacheBo getInstance(){
		return getInstance(MemcacheBo.class);
	}

}
