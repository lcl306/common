package com.it.common.component.lang.str;

import java.io.UnsupportedEncodingException;


/**
 * @author liu
 * */
public class StrFormat {
	/**
	 * eg str=abc len=5 return=00abc;
	 * */
	public static final String addPreZero(String str, int len){
		try {
			if(str!=null && str.getBytes("Shift_JIS").length<len){
					return String.format("%1$0"+(len-str.getBytes("Shift_JIS").length)+"d",0)+str;				
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * eg str=abc len=5 return=abc00  ;
	 * */
	public static final String addTailZero(String str, int len){
		try {
			if(str!=null && str.getBytes("Shift_JIS").length<len){
					return str+String.format("%1$0"+(len-str.getBytes("Shift_JIS").length)+"d",0);
			}
		} catch (UnsupportedEncodingException e) {		
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * eg str=abc len=5 return=  abc;
	 * */
	public static final String addPreBlank(String str, int len){
		try {
			if(str!=null && str.getBytes("Shift_JIS").length<len){			
					return String.format("%1$"+(len-str.getBytes("Shift_JIS").length)+"s", "")+str;			
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * eg str=abc len=5 return=abc  ;
	 * */
	public static final String addTailBlank(String str, int len){
		try {
			if(str!=null && str.getBytes("Shift_JIS").length<len){
					return str+String.format("%1$-"+(len-str.getBytes("Shift_JIS").length)+"s", "");
			}
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * eg len=2 return=  ;
	 * */
	public static final String fillBlank(int len){
		if(len>0) return addPreBlank("", len);
		else return "";
	}
	
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * trim 半角and全角空格
	 * */
	public static final String trimHan(String str){
		if(str==null) return str;
		str = str.replaceAll("^[　| ]+", "");
		str = str.replaceAll("[　| ]+$", "");
		return str;
	}
	
	/**
	 * trim string then cut
	 * eg str=00123 len=3 return=123
	 * */
	public static String cutPre(String str, int len){
		return cutPre(str, len, true);
	}
	
	/**
	 * trim string then cut
	 * eg str=00123 len=3 return=001
	 * */
	public static String cutTail(String str, int len){
		return cutTail(str, len, true);
	}
	
	/**
	 * @param isTrim: true-->trim  false-->no
	 * eg str=00123  len=3 return=123
	 * */
	public static String cutPre(String str, int len, boolean isTrim){
		String rtn = "";
		if(str != null ){
			if(isTrim) str=trimHan(str);
			if(str.length()>len) rtn=str.substring(str.length()-len);
			else rtn = str;
		}
		return rtn;
	}
	
	/**
	 * @param isTrim: true-->trim  false-->no
	 * eg str=00123 len=3 return=001
	 * */
	public static String cutTail(String str, int len, boolean isTrim){
		String rtn = "";
		if (str != null ) {
			if(isTrim) str=trimHan(str);
		    if(str.length()>len) rtn=str.substring(0,len);
		    else rtn = str;
		}
		return rtn;
	}
	
	/////////////////////////////////////////////////////////////////////////////
	
	/**
	 * trim string then cut
	 * eg str=00123 len=3 return="123"
	 * */
	public static String fmCvsStr(String str, int len){
		return fmCvsStr(str, len, true);
	}
	
	/**
	 * trim string then cut
	 * eg str=00123 len=3 return="001"
	 * */
	public static String fmCvsPreStr(String str, int len){
		return fmCvsPreStr(str, len, true);
	}
	
	/**
	 * @param isTrim: true-->trim  false-->no
	 * eg str=00123  len=3 return="123"
	 * */
	public static String fmCvsStr(String str, int len, boolean isTrim){
		String rtn = cutPre(str, len, isTrim);
		return "\""+rtn+"\"";
	}
	
	/**
	 * @param isTrim: true-->trim  false-->no
	 * eg str=00123 len=3 return="001"
	 * */
	public static String fmCvsPreStr(String str, int len, boolean isTrim){
		String rtn = cutTail(str, len, isTrim);
		return "\""+rtn+"\"";
	}
	
	public static final String addPrefix(String str,String prefix, int len){
		String rtn=str;
		if(str!=null){
			StringBuilder sb=new StringBuilder(len+str.length());		
			for(int i=0;i<len; i++){
				sb.append(prefix);				
			}
			sb.append(str);
			rtn=sb.toString();
		}
		return rtn;
	}
	
	public static final String addSubfix(String str,String subfix, int len){
		String rtn=str;
		if(str!=null){
			StringBuilder sb=new StringBuilder(len+str.length());
			sb.append(str);
			for(int i=0;i<len; i++){
				sb.append(subfix);				
			}
			rtn=sb.toString();
		}
		return rtn;
	}
	
	public static String parseStr(String formatVal){
		  if(formatVal==null) return null;
		  String[] strArray = formatVal.split(",");
		  StringBuffer buffer = new StringBuffer();
		  for(int i=0; i<strArray.length; i++){
			  buffer.append(strArray[i]);
		  }
		  return buffer.toString();
	  }
}
