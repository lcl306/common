package com.it.common.component.check.base;

import com.it.common.component.lang.str.StrUtil;
import com.it.common.share.GlobalName;


/**
 * @author liu
 * */
public class FigureCheck {

	public static boolean chkEqual(String value, int figsu, String codeName){
		boolean rtn = true;
		try{
		  if (value!=null && value.length()>0) {
			  if(GlobalName.SJIS_CODE.equals(codeName) && value.getBytes(codeName).length != figsu){
				  rtn = false;
			  }else if(GlobalName.UTF8_CODE.equals(codeName) && StrUtil.byteLength(value)!=figsu){
				  rtn = false;
			  }else if (value.length() != figsu) {
				  rtn = false;
			  }
		  }
		} catch (Exception e) {
		    rtn = false;
		}
	    return rtn;
	}

	public static boolean chkOver(String value,int figsu, String codeName){
		boolean rtn = true;
		try{
		  if (value!=null && value.length()>0) {
			  if(GlobalName.SJIS_CODE.equals(codeName) && value.getBytes(codeName).length > figsu){
				  rtn = false;
			  }else if(GlobalName.UTF8_CODE.equals(codeName) && StrUtil.byteLength(value)>figsu){
				  rtn = false;
			  }else if (value.length() > figsu) {
				  rtn = false;
			  }
		  }
		} catch (Exception e) {
			rtn = false;
		}
		return rtn;
	}
	
	public static boolean chkLess(String value ,int figsu, String codeName){
		boolean rtn = true;
		try{
		  if (value!=null && value.length()>0) {
			  if(GlobalName.SJIS_CODE.equals(codeName) && value.getBytes(codeName).length < figsu){
				  rtn = false;
			  }else if(GlobalName.UTF8_CODE.equals(codeName) && StrUtil.byteLength(value)<figsu){
				  rtn = false;
			  }else if (value.length() < figsu) {
				  rtn = false;
			  }
		  }
		} catch (Exception e) {
			rtn = false;
		}
		return rtn;
	}

	public static boolean chkIntFig(String value, int figsu, boolean isEqualChk){
		boolean rtn = true;
		String temp = CheckUtil.getPart(value, true);
		if(isEqualChk) rtn = chkEqual(temp, figsu, null);
		else rtn = chkOver(temp, figsu, null);
		return rtn;
	}

	public static boolean chkDotFig(String value, int figsu, boolean isEqualChk){
		boolean rtn = true;
		String temp = CheckUtil.getPart(value, false);
		if(isEqualChk) rtn = chkEqual(temp, figsu, null);
		else rtn = chkOver(temp, figsu, null);
		return rtn;
	}

	public static boolean chkTrimIntFig(String value, int figsu, boolean isEqualChk){
		String temp = CheckUtil.zero2trim(value);
		return chkIntFig(temp, figsu, isEqualChk);
	}

	public static boolean chkTrimDotFig(String value, int figsu, boolean isEqualChk){
		value = value!=null?value.trim():null;
		String temp = CheckUtil.zero2trim(value);
		return chkDotFig(temp, figsu, isEqualChk);
	}

}
