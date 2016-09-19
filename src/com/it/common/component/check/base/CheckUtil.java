package com.it.common.component.check.base;

/**
 * @author liu
 * */
public class CheckUtil {

	static String intZero2trim(String value){
		char[] intPart = value.toCharArray();
		int pos = 0;
		char[] numFlag = CheckName.NUM_FLAG;
		outer:for(int i=0; i<intPart.length; i++){
			if(i==0){
				for(int j=0; j<numFlag.length; j++){
					if(intPart[i]==numFlag[j]){
						pos++;
						continue outer;
					}
				}
			}
			if(intPart[i]!='0') break;
			if(intPart[i]=='0') pos+=1;
		}
		if(pos==intPart.length) return "0";
		char[] copy = new char[intPart.length-pos];
		for(int i=0; i<copy.length; i++){
			copy[i] = intPart[pos+i];
		}
		return new String(copy);
	}

	static String dotZero2trim(String value){
		char[] dotPart = value.toCharArray();
		int pos = dotPart.length;
		for(int i=dotPart.length-1; i>=0; i--){
			if(dotPart[i]!='0') break;
			if(dotPart[i]=='0') pos-=1;
		}
		if(pos==0) return "0";
		char[] copy = new char[pos];
		for(int i=0; i<pos; i++){
			copy[i] = dotPart[i];
		}
		return new String(copy);
	}

	static String getPart(String value, boolean isInt){
		if(value==null || value.trim().length()==0) return "";
		String temp = "";
		int pos = value.indexOf(".");
		if(pos!=-1){
			if(isInt) temp = value.substring(0, pos);
			else temp = value.substring(pos+1);
		}else if(isInt){
			temp = value;
		}
		return temp;
	}

	static String zero2trim(String value){
		try{
			String part = CheckUtil.intZero2trim(CheckUtil.getPart(value, true));
			String dotPart = CheckUtil.dotZero2trim(CheckUtil.getPart(value, false));
			if(!dotPart.equals("0")){
				part = part +"." +dotPart;
			}
			return part;
		}catch(Exception e){
			return value;
		}
	}

	/*public static String getStringByCut(String value, Integer midstr, Integer midlen){
		if(value!=null){
			if(midlen!=null && midlen==0) return "";
			try{
				if(midstr!=null && midlen!=null && midstr>=0 && midlen>0){
					byte[] valuebyte = value.getBytes(GlobalName.SHORT_CODE);
					if( valuebyte.length>=(midstr+midlen-1)){
						byte[] copy = new byte[midlen];
						if(midstr==0) midstr = 1;
						for(int i=0; i<copy.length; i++){
							copy[i] = valuebyte[midstr-1+i];
						}
						value = new String(copy, GlobalName.SHORT_CODE);
					}
				}
			}catch(Exception e){
				return value;
			}
		}
		return value;
	}*/

}
