package com.it.common.component.lang.num;


/**
 * @author liu
 * */
public class NumFormat {
	
	/**
	 * eg len=5 return 00000
	 * */
	public static final String fillCode(int len){
		if(len>0) return fmCode("0", len);
		else return "";
	}
	
	/**
	 * eg len=5 code=2 return 22222
	 * */
	public static final String fillCode(String code, int len){
		String rtn = code;
		for(int i=0; i<len-1; i++){
			rtn += code;
		}
		return rtn;
	}
	
	/**
	 * @return 999999
	 * */
	public static final String fmCode(String code, int len){
		try{
			long temp = Long.parseLong(code.trim());
			return String.format("%1$0"+len+"d", temp);
		}catch(Exception e){
			return code;
		}
	}
	
	/**
	 * @return ZZZZZZ9
	 * */
	public static final String fmCode(String code){
		if(code==null) return code;
		code = code.trim();
		char[] chs = code.toCharArray();
		if(chs!=null && chs.length>0){
			int i=0;
			while(chs[i]=='0'){
				i++;
			}
			return code.substring(i);
		}else{
			return code;
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * @return ----,---,--9.ZZ
	 * */
	public static final String fmNumZ(String num, int dlen){
		try{
			int ilen = NumUtil.getIntPart(num).length();
			return fmNumZ(num, ilen, dlen);
		}catch(Exception e){
			return num;
		}
	}
	
	/**
	 * @return ----,---,--9.ZZ
	 * */
	public static final String fmNumZ(String num, int ilen, int dlen){
		try{
			num = fmNum(num, ilen, dlen);
			return fmTextNumZ(num, ilen, dlen);
		}catch(Exception e){
			return num;
		}
	}
	
	/**
	 * @return ---------9.ZZ
	 * */
	public static final String fmTextNumZ(String num, int dlen){
		try{
			int ilen = NumUtil.getIntPart(num).length();
			return fmTextNumZ(num, ilen, dlen);
		}catch(Exception e){
			return num;
		}
	}
	
	/**
	 * @return ---------9.ZZ
	 * */
	public static final String fmTextNumZ(String num, int ilen, int dlen){
		try{
			int pos = num.indexOf(".");
			if(pos!=-1){
				num = num.replaceAll("0+?$", "");
		        if(num.endsWith(".")) num = num.substring(0,num.length()-1);
			}
			return num;
		}catch(Exception e){
			return num;
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * @return ----,---,--9.99
	 * */
	public static final String fmNum(String num, int dlen){
		try{
			int ilen = NumUtil.getIntPart(num).length();
			return fmNum(num, ilen, dlen);
		}catch(Exception e){
			return num;
		}
	}
	
	/**
	 * @return ----,---,--9.99
	 * */
	public static final String fmNum(String num, int ilen, int dlen){
		try{
			double temp = Double.parseDouble(num);
			int alen = ilen;
			if(dlen>0) alen += dlen+1;
			return String.format("%1$,"+alen+"."+dlen+"f", temp).trim();
		}catch(Exception e){
			return num;
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * @return -999999999.99
	 * */
	public static final String fmTextNum(String num, int dlen){
		try{
			int ilen = NumUtil.getIntPart(num).length();
			return fmTextNum(num, ilen, dlen);
		}catch(Exception e){
			return num;
		}
	}
	
	/**
	 * @return -999999999.99
	 * */
	public static final String fmTextNum(String num, int ilen, int dlen){
		try{
			double temp = Double.parseDouble(num);
			int alen = ilen;
			if(dlen>0) alen += dlen+1;
			return String.format("%1$ 0"+alen+"."+dlen+"f", temp);
		}catch(Exception e){
			return num;
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @return ---------9.99
	 * */
	public static String fmCvsNum(String num, int ilen, int dlen){
		try{
			if(num==null||num.trim().length()==0) num = fillCode(ilen);
			double temp = Double.parseDouble(num);
			int alen = ilen;
			if(dlen>0) alen += dlen+1;
			return String.format("%1$"+alen+"."+dlen+"f", temp);
		}catch(Exception e){
			return num;
		}
	}
	
	/**
	 * @param pdfDecimalLen 
	 * 
	 * @return ---------9.99              -------9.00  => ----9__   ; ----9.90  =>  ---99_
	 * */
	public static String fmPDFNum(String num, int ilen, int dlen, int pdfDecimalLen){
		try{
			if(pdfDecimalLen>0)
				dlen=pdfDecimalLen;
			String rtn="";
			if(num==null||num.trim().length()==0) num = fillCode(ilen);
			double temp = Double.parseDouble(num);
			int alen = ilen;
			if(dlen>0) alen += dlen+1;
			rtn= String.format("%1$"+alen+"."+dlen+"f", temp);
			if(pdfDecimalLen>0){
				rtn=rtn.trim();
				StringBuilder str=new StringBuilder("");
				int dotIdx=rtn.indexOf(".");
				if(dotIdx>0){
					String seisu=rtn.substring(0, dotIdx);
					String shosu=rtn.substring(dotIdx+1, rtn.length());
					while(shosu.length()>0 && shosu.charAt(shosu.length()-1)=='0'){
						str.append("　");
						shosu=shosu.substring(0, shosu.length()-1);
					}
					rtn=seisu+shosu+str.toString();
				}
			}
		    return rtn;
		}catch(Exception e){
			return num;
		}
	}
	
}
