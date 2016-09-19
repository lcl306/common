package com.it.common.component.lang.str;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import com.it.common.share.GlobalName;

/**
 * @author liu
 * */
public class StrUtil {
	
	public static String trim(String value){
		return value!=null&&value.length()>0?value.trim():value;
	}
	
	public static String trimB(String value){
		if(value==null) return "";
		return trim(value);
	}
	
	/**
	 * is empty or not
	 * */
	public static <T> boolean isNotEmpty(T t){
		boolean rtn = true;
		if(t==null) rtn = false;
		if(t instanceof String){
			String s = (String)t;
			if(s.trim().length()==0) rtn = false;
		}
		return rtn;
	}
	
	/**
	 * is empty zero or not
	 * */
	public static <T> boolean isNotZero(T t){
		boolean rtn = true;
		if(t==null) rtn = false;
		if(t instanceof String){
			String s = (String)t;
			if(s.trim().length()==0) rtn = false;
			try{
				Double d = Double.parseDouble(s);
				if(d==0){
					rtn = false;
				}
			}catch(Exception e){
				rtn = false;
			}
		}else if(t instanceof Number){
			Double d = ((Number)t).doubleValue();
			if(d.equals(0)){
				rtn = false;
			}
		}
		return rtn;
	}
	
	public static Long toLong(String value){
		Long l = null;
		if(value!=null && value.trim().length()>0)
		try{
			l = Long.parseLong(value.trim());
		}catch(Exception e){}
		return l;
	}
	
	public static Integer toInteger(String value){
		Integer l = null;
		if(value!=null && value.trim().length()>0)
		try{
			l = Integer.parseInt(value.trim());
		}catch(Exception e){}
		return l;
	}
	
	public static Short toShort(String value){
		Short l = null;
		if(value!=null && value.trim().length()>0)
		try{
			l = Short.parseShort(value.trim());
		}catch(Exception e){}
		return l;
	}
	
	public static Byte toByte(String value){
		Byte l = null;
		if(value!=null && value.trim().length()>0)
		try{
			l = Byte.parseByte(value.trim());
		}catch(Exception e){}
		return l;
	}
	
	public static Double toDouble(String value){
		Double l = null;
		if(value!=null && value.trim().length()>0)
		try{
			l = Double.parseDouble(value.trim());
		}catch(Exception e){}
		return l;
	}
	
	public static Float toFloat(String value){
		Float l = null;
		if(value!=null && value.trim().length()>0)
		try{
			l = Float.parseFloat(value.trim());
		}catch(Exception e){}
		return l;
	}
	
	public static String toStr(Number n){
		if(n!=null){
			return String.valueOf(n);
		}
		return "";
	}
	
	public static String toStr(String... strings){
		if (strings==null) return null;
		if (strings.length==0) return "";
		StringBuilder result=new StringBuilder();
		for (String s : strings) {
			result.append(",").append(s);
		}
		return result.substring(1);
	}
	
	public static String toStr(List<String> strings){
		if (strings==null) return null;
		if (strings.isEmpty()) return "";
		StringBuilder result=new StringBuilder();
		for (String s : strings) {
			result.append(",").append(s);
		}
		return result.substring(1);
	}
	
	/**
	 * 首字母小写
	 * */
	public static String firstLowerCase(String str){
		if(str!=null && str.trim().length()>0){
			char[] cs=str.toCharArray();
	        cs[0]+=32;
	        str = String.valueOf(cs);
		}
		return str;
	}
	
	/**
	 * 首字母大写
	 * */
	public static String firstUpperCase(String str){
		if(str!=null && str.trim().length()>0){
			char[] cs=str.toCharArray();
	        cs[0]-=32;
	        str = String.valueOf(cs);
		}
		return str;
	}
	
	/**
	 * return byte of string
	 * ascii --> 1
	 * 半角    --> 1
	 * 全角    --> 2
	 * */
	public static int byteLength(String value){
		if(value==null) return 0;
		int i = 0;
		int bytes = 0;
		int uFF61 = new BigInteger("FF61", 16).intValue();
		int uFF9F = new BigInteger("FF9F", 16).intValue();
		int uFFE8 = new BigInteger("FFE8", 16).intValue();
		int uFFEE = new BigInteger("FFEE", 16).intValue();
		while (i < value.length()){
			int c = value.charAt(i);
			if (c < 256) {
				bytes = bytes + 1;
			}else{
				if ((uFF61 <= c) && (c <= uFF9F)) {
					bytes = bytes + 1;
				}else if ((uFFE8 <= c) && (c <= uFFEE)) {
					bytes = bytes + 1;
				}else{
					bytes = bytes + 2;
				}
			}
			i = i + 1;
		}
		return bytes;
	}
	
	/**
	 * judge two object equal or not
	 * if object is string, trim then judge
	 * */
	public static boolean equals(Object obj1, Object obj2){
		return equals(obj1, obj2, true);
	}
	
	/**
	 * judge two object equal or not
	 * @param isTrim: true-->trim  false-->no
	 * */
	public static boolean equals(Object obj1,Object obj2, boolean isTrim){		
		if(obj1==null && obj2!=null)
			return false;
		if(obj1!=null && obj2==null)
			return false;
		if(obj1==null && obj2==null)
			return true;
		if(isTrim){
			if(obj1 instanceof String) obj1 = ((String)obj1).trim();
			if(obj2 instanceof String) obj2 = ((String)obj2).trim();
		}
		try {						
			BigDecimal temp1=null;
			if(obj1 instanceof Float)
				temp1=new BigDecimal(""+(Float)obj1);
			else if(obj1 instanceof Double)
				temp1=new BigDecimal(""+(Double)obj1);
			else if(obj1 instanceof Integer)
				temp1=new BigDecimal((Integer)obj1);
			else if(obj1 instanceof BigDecimal)
				temp1=(BigDecimal)obj1;
			else if(obj1 instanceof Long)
				temp1=new BigDecimal((Long)obj1);
			else
				temp1=new BigDecimal((String)obj1);
			
			BigDecimal temp2=null;
			if(obj2 instanceof Float)
				temp2=new BigDecimal(""+(Float)obj2);
			else if(obj2 instanceof Double)
				temp2=new BigDecimal(""+(Double)obj2);
			else if(obj2 instanceof Integer)
				temp2=new BigDecimal((Integer)obj2);
			else if(obj2 instanceof BigDecimal)
				temp2=(BigDecimal)obj2;
			else if(obj2 instanceof Long)
				temp2=new BigDecimal((Long)obj2);
			else 
				temp2=new BigDecimal((String)obj2);
						
			if(temp1!=null && temp2!=null && temp1.compareTo(temp2)==0)
				return true;
		} catch (Exception e) {		
			if(obj1 instanceof String && obj2 instanceof String){
				return obj1.equals(obj2);
			}
		}
		return false;
	}
	
	/**
	 * string --> md5
	 * */
	public static String md5Digest(String src){
		if(src==null) return src;
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest(src.getBytes(GlobalName.CODE));
			StringBuilder sb = new StringBuilder(); 
			for (int i = 0; i < b.length; i++) {
				 String s = Integer.toHexString(b[i] & 0xFF); 
				 if (s.length() == 1) {
					 sb.append("0");
				 }
				 sb.append(s.toUpperCase());
			}                          
			return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
			return src;
		}
	}

}
