package com.it.common.component.lang.sys;

import java.util.Collection;

import com.it.common.component.log.LogPrint;

public class SyncUtil {
	
	public final static <T> void setElement(Collection<T> collection, T element){
		synchronized(collection){
			try{
				while(sameElement(collection, element)){
					collection.wait();
				}
				collection.add(element);
			}catch(InterruptedException e){
				e.printStackTrace();
				LogPrint.error(e.getMessage());
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public final static <T> void removeElement(Collection<T> collection, T element){
		synchronized(collection){
			try{
				collection.remove(element);
			}finally{
				collection.notifyAll();
			}
		}
	}
	
	private final static <T> boolean sameElement(Collection<T> collection, T element){
		if(collection.contains(element)){
			return true;
		}
		return false;
	}

}

