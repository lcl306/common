package com.it.common.component.lang.date;

import java.io.Serializable;
import java.sql.Timestamp;

import com.it.common.component.db.BaseDao;

/**
 * get time from database
 * @author liu
 * */
public class DBTime implements Serializable{
	
	private static final long serialVersionUID = 7716279398510284807L;

	public Timestamp tt;
	
	public String date;
	
	public static final String format = "yyyyMMddHHmmss";
	
	public static DBTime getInstance(){
		return BaseDao.getInstance().getDBTime();
	}
	
	public String getDay(){
		return date.substring(0, 8);
	}
	
	public String getTime(){
		return date.substring(8, 14);
	}
	
	public String getYYYY(){
		return date.substring(0, 4);
	}
	
	public String getMM(){
		return date.substring(4, 6);
	}
	
	public String getDD(){
		return date.substring(6, 8);
	}
	
	public String getHH(){
		return date.substring(8, 10);
	}
	
	public String getMI(){
		return date.substring(10, 12);
	}
	
	public String getSS(){
		return date.substring(12, 14);
	}

}
