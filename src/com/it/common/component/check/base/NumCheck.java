package com.it.common.component.check.base;

/**
 * @author liu
 * */
public class NumCheck {

	public static boolean chkCode(String value){
		boolean rtn = true;
		if (value!=null && value.length()>0){
		  char c;
		  for (int i = 0; i < value.length(); i++) {
			c = value.charAt(i);
			if ( (c < '0' || c > '9'))
			  return false;
		 }
		}
		return rtn;
	}

	public static boolean chkNum(String value, boolean hasDot, boolean hasNumflag){
		if(value==null || value.length() == 0) return true;
		if(value.trim().length() == 0) return false;
		if(!value.startsWith(" ") && value.trim().length()!=value.length()) return false;
		if(value.startsWith(" ") && value.length()-value.trim().length()>1) return false;
		return chkTrimNum(value, hasDot, hasNumflag);
	}

	public static boolean chkTrimNum(String value, boolean hasDot, boolean hasNumflag){
		boolean rtn = true;
		if(value==null || value.trim().length() == 0) return true;
		if(value.trim().startsWith(".") || value.trim().endsWith(".")) return false;
		try{
			if(hasDot) Double.valueOf(value);
			else Long.valueOf(value.trim());
			boolean isFirstFlag = isFirstFlag(value);
			if(isFirstFlag && value.length()>=2){
				char c = value.charAt(1);
				if (c < '0' || c > '9') rtn = false;
			}
			if(!hasNumflag){
				rtn = !isFirstFlag;
			}
		}catch(Exception e){
			rtn = false;
		}
		return rtn;
	}

	private static boolean isFirstFlag(String value){
		boolean rtn = false;
		char[] numflag = CheckName.NUM_FLAG;
		for(int i=0; i<numflag.length; i++){
			if(value.charAt(0)==numflag[i]){
				rtn = true;
				break;
			}
		}
		return rtn;
	}

}
