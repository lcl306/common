package com.it.common.component.check.base;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liu
 * */
public class MatchCheck {
	
	public static boolean chkRegex(String regex, String value){
		if(value==null || value.length()==0) return true;
		boolean rtn = true;
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(value);
		if(!m.matches()){
			rtn=false;
		}
		return rtn;
	}
	
	public static boolean chkMatchRange(String value, String start, String end){
		boolean rtn = true;
		if(value!=null && value.length()>0){
			start = start!=null?start.trim():"";
			end = end!=null?end.trim():"";
			try{
				long v = Long.parseLong(value);
				if(!"".equals(start)){
					long s = Long.parseLong(start);
					if(v<s) rtn = false;
				}
				if(!"".equals(end)){
					long e = Long.parseLong(end);
					if(v>e) rtn = false;
				}
			}catch(Exception e){
				if((!"".equals(start) && value.compareTo(start)<0) || (!"".equals(end) && value.compareTo(end.trim())>0)){
					rtn = false;
				}
			}
		}
		return rtn;
	}
	
	public static boolean chkMatchs(Object value1, List<?> value2){
		if(value1==null) return true;
		if(value1 instanceof String){
			String s = (String)value1;
			if(s.length()==0) return true;
		}
		boolean rtn = false;
		if(value2!=null && value2.size()>0){
			for(Object v : value2){
				rtn = chkMatch(value1, v);
				if(rtn) break;
			}
		}else{
			rtn = true;
		}
		return rtn;
	}

	public static boolean chkMatch(Object value1, Object value2){
		return chkMatch(value1, value2, true);
	}

	private static boolean chkMatch(Object value1, Object value2, boolean isTrim){
		if(value1==null && value2==null){
			return true;
		}
		if(value1==null || value2==null){
			return false;
		}
		if(value1 instanceof String && value2 instanceof String){
			String str1 = (String) value1;
			String str2 = (String) value2;
			if(isTrim) return str1.trim().equals(str2.trim());
			else return str1.equals(str2);
		}
		if(value1 instanceof Number && value2 instanceof Number){
			Number num1 = (Number) value1;
			Number num2 = (Number) value2;
			return num1.equals(num2);
		}
		if(value1 instanceof BigDecimal && value2 instanceof BigDecimal){
			BigDecimal dec1 = (BigDecimal) value1;
			BigDecimal dec2 = (BigDecimal) value2;
			return dec1.equals(dec2);
		}
		return false;
	}

}
