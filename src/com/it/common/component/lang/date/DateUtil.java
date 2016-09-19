package com.it.common.component.lang.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author liu
 * */
public class DateUtil {
	
	/**
	 * get Calendar from string by format
	 * */
	public static Calendar getDate(String value, String format) {
		Calendar rtn = new GregorianCalendar();
		if (value == null || format == null || value.trim().equals("")
				|| value.length() != format.length()) {
			return rtn;
		}
		DateFormat df = new SimpleDateFormat(format);
		Date dt = null;
		try {
			dt = df.parse(value);
		} catch (ParseException e) {
			return rtn;
		}
		rtn = new java.util.GregorianCalendar();
		rtn.setTime(dt);
		return rtn;
	}

	/**
	 * get string from Calendar by format
	 * */
	public static String getDate(Calendar calendar, String format) {
		String rtn = "";
		if (calendar != null && format != null && format.length() > 0) {
			DateFormat tf = new SimpleDateFormat(format);
			rtn = tf.format(calendar.getTime());
		}
		return rtn;
	}
	
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * @return preserve month first day
	 * eg yyyy=2013 mm=03  return 20130201
	 * */
	public static String preMMFirstDay(String yyyy, String mm){
		Calendar date = Calendar.getInstance();
		try{
			date.set(Calendar.YEAR, Integer.parseInt(yyyy));
			date.set(Calendar.MONTH, Integer.parseInt(mm)-1);
			date.add(Calendar.MONTH, -1);
			date.set(Calendar.DAY_OF_MONTH, 1);
		}catch(Exception e){
		}
		return getDate(date, "yyyyMMdd");
	}
	
	/**
	 * @return preserve month last day
	 * eg yyyy=2013 mm=03  return 20130228
	 * */
	public static String preMMLastDay(String yyyy, String mm){
		Calendar date = Calendar.getInstance();
		try{
			date.set(Calendar.YEAR, Integer.parseInt(yyyy));
			date.set(Calendar.MONTH, Integer.parseInt(mm)-1);
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.DAY_OF_YEAR, -1);
		}catch(Exception e){
		}
		return getDate(date, "yyyyMMdd");
	}
	
	/**
	 * @return another year by yNum
	 * eg date=20130201 yNum=2  return 20150201
	 * */
	public static String changeYear(String date, int yNum){
		String rtn = date;
		try{
			Calendar d = getDate(date, "yyyyMMdd");
			d.add(Calendar.YEAR, yNum);
			rtn = getDate(d, "yyyyMMdd");
		}catch(Exception e){
			return rtn;
		}
		return rtn;
	}
	
	/**
	 * @return another month by mNum
	 * eg date=20130201 yNum=2  return 20130401
	 * */
	public static String changeMonth(String date, int mNum){
		String rtn = date;
		try{
			Calendar d = getDate(date, "yyyyMMdd");
			d.add(Calendar.MONTH, mNum);
			rtn = getDate(d, "yyyyMMdd");
		}catch(Exception e){
			return rtn;
		}
		return rtn;
	}
	
	/**
	 * @return another day by dNum
	 * eg date=20130201 yNum=7  return 20130208
	 * */
	public static String changeDay(String date, int dNum){
		String rtn = date;
		try{
			Calendar d = getDate(date, "yyyyMMdd");
			d.add(Calendar.DAY_OF_YEAR, dNum);
			rtn = getDate(d, "yyyyMMdd");
		}catch(Exception e){
			return rtn;
		}
		return rtn;
	}
	
	/**
	 * @param dayNames: GlobalName.DAY_NAMES  or  GlobalName.S_DAY_NAMES
	 * eg date=20130701 dayNames=GlobalName.DAY_NAMES  return 月曜日
	 * */
	public static String getDayOfWeek(String date, String[] dayNames){
		if(date==null || date.trim().length()==0) return "";
		if("00000000".equals(date) || "000000".equals(date)) return "";
		String rtn = "";
		try{
			Calendar d = getDate(date, "yyyyMMdd");
			int dayOfWeek = d.get(Calendar.DAY_OF_WEEK);
			return dayNames[dayOfWeek-1];
		}catch(Exception e){
			return rtn;
		}
	}

	/**
	 * eg date1=20130505 date2=20130501 return 4
	 * */
	public static int getSubDay(String date1, String date2){
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyyMMdd");
		int sub = 0;
		try{
		    java.util.Date d1= myFormatter.parse(date1);
		    java.util.Date d2= myFormatter.parse(date2);
		    Long day=(d1.getTime()-d2.getTime())/(24*60*60*1000);
		    sub = day.intValue();
		}catch(Exception e){}
	    return sub;
	}

	/**
	 * eg date=20130205 return 28
	 * */
	public static int totalDayInMonth(String date){
		Calendar cal = getDate(date, "yyyyMMdd");
	    int month = cal.get(Calendar.MONTH)+1;
	    int allday = 30;
	    int year = cal.get(Calendar.YEAR);
	    switch(month){
	    	case 1:
	    	case 3:
	    	case 5:
	    	case 7:
	    	case 8:
	    	case 10:
	    	case 12:
	    	 allday=31;break;
	    	case 2:
	    	if(year%4==0&&year%100!=0||year%400==0){
	    	  allday=29;
	    	}else{
	    	  allday=28;
	    	}
	    	break;
	    	default:
	    	  allday=30;break;
	    }
		return allday;
	}


}
