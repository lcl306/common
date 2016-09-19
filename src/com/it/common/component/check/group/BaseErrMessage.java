package com.it.common.component.check.group;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.it.common.component.lang.sys.SysUtil;
import com.it.common.component.log.LogPrint;
import com.it.common.share.GlobalName;

/**
 * @author liu
 * */
public class BaseErrMessage {
	
	protected static String RIGHT = "00";
	
	protected static String EMPTY_ERR = "01";
	
	protected static String CODE_ERR = "02";
	
	protected static String HAN_ERR = "03";
	
	protected static String NUM_ERR = "04";
	
	protected static String FIG_EQ_ERR = "05_1";
	
	protected static String FIG_OVER_ERR = "05_2";
	
	protected static String FIG_INTFIG_ERR = "05_3";
	
	protected static String FIGTEXT_EQ_ERR = "05_4";
	
	protected static String FIGTEXT_OVER_ERR = "05_5";
	
	protected static String DOT_FIG_EQ_ERR = "06_1";
	
	protected static String DOT_FIG_OVER_ERR = "06_2";
	
	protected static String DATE_ERR = "07";
	
	protected static String KIN_ERR = "08";
	
	protected static String EXIST_MST_ERR = "09";
	
	protected static String NO_MST_ERR = "10";
	
	protected static String REGEX_ERR = "12";
	
	protected static String ZEN_ERR = "20";
	
	protected static String MATCH_ERR = "90";
	
	protected static String COMPARISON_ERR = "99991";
	
	protected static final String LIN_START = "第";
	protected static final String LIN_END = "行　";
	protected static final String ERR_OVER_START = "错误超过";
	protected static final String ERR_OVER_END = "个。";
	protected static final String MATCH_FLAG = "、";
	
	public static final String ERR_01 = "%1%2不能为空。";		
	public static final String ERR_02 = "%1%2数字以外无效。";
	public static final String ERR_03 = "%1%2半角以外无效。";
	public static final String ERR_04 = "%1数值以外无效。";
	public static final String ERR_05_1 = "%1的整数位数不等于%2位。";
	public static final String ERR_05_2 = "%1的整数位数超过%2位。";
	public static final String ERR_05_3 = "%1的有效位数不等于%2位。";
	public static final String ERR_05_4 = "%1的位数不等于%2位。";
	public static final String ERR_05_5 = "%1的位数超过%2位。";
	public static final String ERR_06_1 = "%1的小数位数不等于%2位。";
	public static final String ERR_06_2 = "%1的小数位数超过%2位。";
	public static final String ERR_07 = "%1%2的日期指定有误。";
	public static final String ERR_08 = "%1%2包含禁止文字。";
	public static final String ERR_09 = "该%1的数据已存在。";
	public static final String ERR_10 = "该%1的数据不存在。";
	public static final String ERR_12 = "%1%2的指定有误。";
	public static final String ERR_20 = "%1%2全角以外无效。";
	public static final String ERR_90 = "%1中，%2以外无效。";
	
	public static final String START = "（开始）";
	public static final String END = "（结束）";
	
	///////////////////////////////////////////////////////////////////////////////
	
	protected static String errBeanName = BaseErrorDto.class.getName();
	
	protected static String errMessageName;
	
	public static final String BASE_ERROR_CONTEXT_PATH = "com/it/context/errorMessageBase.txt";
	
	protected static Set<String> errorContextPaths = new HashSet<String>();
	
	protected static Map<String, String> errMap = new ConcurrentHashMap<String, String>();
	
	protected static Set<String> itemNameContextPaths = new HashSet<String>();
	
	protected static Map<String, String> itemNameMap = new ConcurrentHashMap<String, String>();
	
	static{
		String errBeanName = GlobalName.appResources.getString("errBeanName");
		if(errBeanName!=null && errBeanName.trim().length()>0) BaseErrMessage.errBeanName = errBeanName;
		String errMessageName = GlobalName.appResources.getString("errMessageName");
		if(errMessageName!=null && errMessageName.trim().length()>0) BaseErrMessage.errMessageName = errMessageName;
	}
	
	protected static void setErrorContext(){
		errorContextPaths.add(BASE_ERROR_CONTEXT_PATH);
	}

	protected static void fillErrMap(){
		for(String errPath : errorContextPaths){
			fillMap(errMap, errPath);
		}
	}
	
	protected static void fillItemMap(){
		for(String itemNamePath : itemNameContextPaths){
			fillMap(itemNameMap, itemNamePath);
		}
	}
	
	protected static void fillMap(Map<String, String> errMap, String errorContextName){
		FileInputStream in = null;
		BufferedReader reader = null;
		try {
			in = new FileInputStream(SysUtil.getClassRootPath()+errorContextName);
			reader = new BufferedReader(new InputStreamReader(in, GlobalName.CODE));
			String line = "";
			while((line=reader.readLine())!=null){
				if(line.indexOf("=")!=-1){
					String[] info = line.split("=");
					if(!errMap.containsKey(info[0]))
						errMap.put(info[0], info[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static BaseErrorDto getErrorBean(){
		BaseErrorDto errBean = null;
		try {
			errBean = (BaseErrorDto)Class.forName(errBeanName).newInstance();
		} catch (Exception e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		} 
		return errBean;
	}
	
	static BaseErrMessage getErrMessage(){
		BaseErrMessage errMessage = null;
		try {
			if(errMessageName!=null) errMessage = (BaseErrMessage)Class.forName(errMessageName).newInstance();
		} catch (Exception e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		} 
		return errMessage;
	}
	
	protected BaseErrorDto getError(String code, String per2, CheckDto chk){
		BaseErrorDto err = null;
		if(code!=RIGHT){
			err = getErrorBean();
			try {
				if(itemNameMap.isEmpty()) fillItemMap();
				String errStr = (String) BaseErrMessage.class.getField("ERR_"+code).get(null);
				if(chk.getErrName()==null || chk.getErrName().trim().length()==0){
					String errName = itemNameMap.get(chk.getErrCode());
					if(errName==null){
						fillItemMap();
						errName = itemNameMap.get(chk.getErrCode());
					}
					chk.setErrName(errName);
				}
				String itemName = chk.getErrName();
				errStr = getRealError(errStr, 1, itemName);
				if(per2!=null) errStr = getRealError(errStr, 2, per2);
				err.setErrMsg(errStr);
				String c2 = code.split("_")[0];
				String c = chk.getErrCode()+c2;
				err.setErrCode(c);
			}catch (Exception e) {
				e.printStackTrace();
				LogPrint.error(e.getMessage());
			} 
		}
		return err;
	}
	
	public BaseErrorDto getError(String code, String... replaces){
		BaseErrorDto err = getErrorBean();
		err.setErrCode(code.split("_")[0]);
		try {
			if(errMap.isEmpty()){
				fillErrMap();
			}
			String errStr = errMap.get("ERR_"+code);
			if(errStr==null){
				fillErrMap();
				errStr = errMap.get("ERR_"+code);
			}
			err.setErrMsg(getRealErrors(errStr, replaces));
		} catch (Exception e) {
			e.printStackTrace();
			LogPrint.error(e.getMessage());
		} 
		return err;
	}
	
	static String getMatchMsg(List<String> matchs){
		String rtn = "";
		if(matchs!=null){
			for(int i=0; i<matchs.size(); i++){
				if(i<matchs.size()-1)
					rtn += (matchs.get(i) +MATCH_FLAG);
				else
					rtn += matchs.get(i);
			}
		}
		return rtn;
	}
	
	static String getRangeMsg(String start, String end){
		start = start!=null&&start.trim().length()>0?start.trim():" ";
		end = end!=null&&end.trim().length()>0?end.trim():" ";
		//if("".equals(start)) start = FmNumber.fillCode(end.length());
		//if("".equals(end)) end = FmNumber.fillCode("9", start.length());
		return start +"-" +end;
	}
	
	static String getRealError(String srcErr, int idx, String replace){
		return srcErr.replace("%"+idx, replace);
	}
	
	static String getRealErrors(String srcErr, String... replaces){
		if(replaces!=null && replaces.length>0){
			for(int i=0; i<replaces.length; i++){
				String r = replaces[i];
				srcErr = srcErr.replace("%"+(i+1), r);
			}
		}
		return srcErr;
	}

}
