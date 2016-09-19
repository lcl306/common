package com.it.common.component.check.base;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.it.common.share.GlobalName;

/**
 * @author liu
 * */
public class DateCheck {

	public static boolean chkZnDate(String value, boolean isDay6){
		if(isDay6) value = getDate(value);
		if (value != null && (value.equals(CheckName.Z_DATE) || value.equals(CheckName.N_DATE)))
			return true;
		return chkDate(value, false);
	}

	public static boolean chkZDate(String value, boolean isDay6){
		if(isDay6) value = getDate(value);
		if (value != null && value.equals(CheckName.Z_DATE))
			return true;
		return chkDate(value, false);
	}

	public static boolean chkNDate(String value, boolean isDay6){
		if(isDay6) value = getDate(value);
		if (value != null && value.equals(CheckName.N_DATE))
			return true;
		return chkDate(value, false);
	}

	public static boolean chkDate(String value, boolean isDay6){
		boolean rtn = true;
		if(value!=null && value.length()!=0){
			if((!isDay6 && value.length()!=8) || (isDay6 && value.length()!=6)){
				rtn = false;
			}
			if(isDay6) value = getDate(value);
			if(chkRealDate(value)){
				if (value.compareTo(CheckName.MIN_DATE) < 0 || value.compareTo(CheckName.MAX_DATE) > 0) {
					rtn = false;
				}
			}else{
				rtn = false;
			}
		}
		return rtn;
	}

	private static String getDate(String value){
		if(value!=null && value.length()==6){
			if(CheckName.SZ_DATE.equals(value)) return CheckName.Z_DATE;
			if(CheckName.SN_DATE.equals(value)) return CheckName.N_DATE;
			value = GlobalName.PRE_YEAR +value;
		}
		return value;
	}

	private static boolean chkRealDate(String value){
		try{
			Calendar cal = new GregorianCalendar();
			cal.setLenient(false);
			cal.set(Integer.parseInt(value.substring(0, 4)), Integer
					.parseInt(value.substring(4, 6)) - 1, Integer
					.parseInt(value.substring(6, 8)));
			cal.getTime();
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public static boolean chkTime(String value){
		boolean rtn = true;
		if(value!=null && value.length()>0){
			if(value.length()!=6){
				rtn = false;
			}else{
				try {
					int hourOfDay = Integer.parseInt(value.substring(0, 2));
					int minute = Integer.parseInt(value.substring(2, 4));
					int second = Integer.parseInt(value.substring(4, 6));
					if(hourOfDay<0 || hourOfDay > 23) rtn = false;
					if(minute<0 || minute>59) rtn = false;
					if(second<0 || second>59) rtn = false;
				} catch (Exception e) {
					rtn = false;
				}
			}
		}
		return rtn;
	}

}
