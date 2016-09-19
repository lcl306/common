package com.it.common.component.check.base;

import com.it.common.component.lang.str.StrUtil;

/**
 * @author liu
 * */
public class StringCheck {

	public static boolean chkEmpty(String value){
		if(value==null || value.trim().length()==0)
			return false;
		return true;
	}
	
	public static boolean chkZeroEmpty(String value){
		if(value==null || value.trim().length()==0)
			return false;
		else{
			char[] cs = value.toCharArray();
			for(int i=0; i<cs.length; i++){
				if(cs[i]!='0')
					return true;
			}
		}
		return false;
	}

	public static boolean chkHan(String value){
		boolean rtn = true;
		if (value != null && value.length() != 0) {
			try {
				if (value.length()!=StrUtil.byteLength(value))
					rtn = false;
				else{
					if(Character.getType(value.charAt(0))==28 ||
							Character.getType(value.charAt(value.length()-1))==28)
						rtn = false;
				}
			} catch (Exception e) {
				rtn = false;
			}
		}
		return rtn;
	}

	public static boolean chkKin(String value){
		boolean rtn = true;
		if (value == null || value.length() == 0) {
		   rtn = true;
		} else {
		   value = value.trim();
		   char c;
		   for (int i = 0; i < value.length(); i++) {
			   c = value.charAt(i);
			   if (c == '\'' || c == '"' || c == '>' || c == '<' || c == '\\')
				   rtn = false;
		   }
		}
		return rtn;
	}

	public static boolean chkZen(String value){
		boolean rtn = true;
		if (value == null || value.length() == 0)
			rtn = true;
		else{
			try {
				value = value.trim();
				if (value.length() == StrUtil.byteLength(value))
					rtn = false;
			} catch (Exception e) {
				rtn = false;
			}
		}
		return rtn;
	}

}
