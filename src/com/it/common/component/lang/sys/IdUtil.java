package com.it.common.component.lang.sys;

import com.it.common.component.db.BaseDao;

public class IdUtil {
	
	public static final String LOCKID_SEQ = "SEQ_LOCKID";
	
	public static final String ID_SEQ = "SEQ_ID";
	
	public static Long getNextVal(String seq){
		return BaseDao.getInstance().getNextVal(seq);
	}
	
	public static Long getLockId(){
		return getNextVal(LOCKID_SEQ);
	}
	
	public static Long getId(){
		return getNextVal(ID_SEQ);
	}

}
