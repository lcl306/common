package com.it.common.component.lang.date;

import java.util.Calendar;

import com.it.common.share.GlobalName;

/**
 * format date
 * @author liu
 * */
public class DateFormat {

	public static String getYYYY(String date){
		return getDatePart(date, 0 ,4);
	}

	public static String getMM(String date){
		return getDatePart(date, 4, 6);
	}

	public static String getDD(String date){
		return getDatePart(date, 6, 8);
	}
	
	public static String getHH(String time){
		return getDatePart(time, 0, 2);
	}
	
	public static String getMI(String time){
		return getDatePart(time, 2, 4);
	}
	
	public static String getSS(String time){
		return getDatePart(time, 4, 6);
	}
	
	/**
	 * to HH:mi:ss
	 * */
	public static String fmTime(String time) {
		String vtime = time;
		try{
			vtime = time.substring(0, 2) + GlobalName.TIME_FLAG + time.substring(2, 4) + GlobalName.TIME_FLAG+ time.substring(4);
		}catch(Exception e){}
		return vtime;
	}
	
	/**
	 * yymmdd --> yyyymmdd
	 * */
	public static String toYear8(String date){
		if(date!=null && date.trim().length()>0 && date.length()==6) 
			date = GlobalName.PRE_YEAR+date;
		return date;
	}
	
	/**
	 * yyyymmdd  --> yymmdd
	 * */
	public static String toYear6(String date){
		if(date!=null && date.trim().length()>0 && date.length()==8) 
			date = date.substring(2);
		return date;
	}
	
	/**
	 * yyyymmdd  --> mmdd
	 * */
	public static String toDate4(String date){
		if(date!=null && date.trim().length()>0 && date.length()==8) 
			date = date.substring(4);
		return date;
	}
	
	/**
	 * yyyymm  --> yyyy/mm
	 * yymm    --> yy/mm
	 * */
	public static String fmYm(String date){
		String vdate;
		try{
			if(date!=null && date.indexOf(GlobalName.DATE_FLAG)==-1){ 
				if(date.length()==6){
					vdate = date.substring(0, 4) + GlobalName.DATE_FLAG + date.substring(4);
				}else if(date.length()==4){
					vdate = date.substring(0,2) +GlobalName.DATE_FLAG +date.substring(2);
				}else{
					vdate = date;
				}
			}else{
				vdate = date;
			}
		}catch(Exception e){
			vdate = date;
		}
		return vdate;
	}

	/**
	 * yyyymmdd --> yyyy/mm/dd
	 * yymmdd --> yy/mm/dd
	 * mmdd   --> mm/dd
	 * */
	public static String fmDate(String date) {
		String vdate;
		try{
			if(date!=null && date.indexOf(GlobalName.DATE_FLAG)==-1){ 
				if(date.length()==8){
					vdate = date.substring(0, 4) + GlobalName.DATE_FLAG + date.substring(4, 6) +GlobalName.DATE_FLAG+ date.substring(6);
				}else if(date.length()==6){
					vdate = date.substring(0,2) +GlobalName.DATE_FLAG +date.substring(2,4)+GlobalName.DATE_FLAG+date.substring(4);
				}else if(date.length()==4){
					vdate = date.substring(0,2) +GlobalName.DATE_FLAG +date.substring(2);
				}else{
					vdate = date;
				}
			}else{
				vdate = date;
			}
		}catch(Exception e){
			vdate = date;
		}
		return vdate;
	}
	
	/**
	 * yyyy/mm  --> yyyymm
	 * yy/mm    --> yymm
	 * */
	public static String parseYm(String date){
		String vdate;
		try{
			if(date!=null && date.indexOf(GlobalName.DATE_FLAG)!=-1){
				if(date.length()==7)
					vdate = date.substring(0, 4)+date.substring(5);
				else if(date.length()==5)
					vdate = date.substring(0,2)+date.substring(3);
				else
					vdate = date;
			}else{
				vdate = date;
			}
		}catch(Exception e){
			vdate = date;
		}
		return vdate;
	}
	
	/**
	 * yyyy/mm/dd  --> yyyymmdd
	 * yy/mm/dd    --> yymmdd
	 * mm/dd       --> yyyymmdd
	 * */
	public static String parseDate(String date){
		String vdate;
		try{
			if(date!=null && date.indexOf(GlobalName.DATE_FLAG)!=-1){
				if(date.length()==10)
					vdate = date.substring(0, 4)+date.substring(5,7)+date.substring(8);
				else if(date.length()==8)
					vdate = date.substring(0,2)+date.substring(3,5)+date.substring(6);
				else if(date.length()==5)
					vdate = Calendar.getInstance().get(Calendar.YEAR)+date.substring(0,2)+date.substring(3);
				else
					vdate = date;
			}else{
				vdate = date;
			}
		}catch(Exception e){
			vdate = date;
		}
		return vdate;
	}
	
	/**
	 * hh:mi:ss  --> hhmiss
	 * hh:mi     --> hhmi
	 * */
	public static String parseTime(String time){
		String vtime;
		try{
			if(time!=null && time.indexOf(GlobalName.TIME_FLAG)!=-1){
				if(time.length()==8)
					vtime = time.substring(0, 2)+time.substring(3,5)+time.substring(6);
				else if(time.length()==5)
					vtime = time.substring(0,2)+time.substring(3);
				else
					vtime = time;
			}else{
				vtime = time;
			}
		}catch(Exception e){
			vtime = time;
		}
		return vtime;
	}
	
	static String getDatePart(String date, int start, int end){
		String part = "";
		try{
			if(end>date.length())
				part = date.substring(start);
			else
				part = date.substring(start, end);
		}catch(Exception e){
			return date;
		}
		return part;
	}

}
