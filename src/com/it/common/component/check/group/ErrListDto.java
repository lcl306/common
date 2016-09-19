package com.it.common.component.check.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.it.common.component.json.JsonUtil;

/**
 * @author liu
 * */
public class ErrListDto implements Serializable{
	
	private static final long serialVersionUID = -2447878279549107699L;

	/**
	 * max error count
	 * */
	protected int maxErrCount = 99;
	
	private int linno = 0;
	
	protected List<String> errList = new ArrayList<String>();
	
	///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * set error line
	 * */
	public void setLinno(int linno) {
		this.linno = linno;
	}

	/**
	 * get error line
	 * */
	public int getLinno() {
		return linno;
	}
	
	/**
	 * add error message
	 * */
	public void add(BaseErrorDto err){
		if(err!=null){
			if(maxErrCount==0 || errList.size()<maxErrCount){
				StringBuilder b = new StringBuilder();
				if (linno > 0) b.append(BaseErrMessage.LIN_START).append(linno).append(BaseErrMessage.LIN_END);
				errList.add(b.append(err.toStr()).toString());
			}else{
				errList.add(BaseErrMessage.ERR_OVER_START +maxErrCount +BaseErrMessage.ERR_OVER_END);
			}
		}
	}
	
	/**
	 * add error message
	 * */
	public void add(String code, String... replaces){
		add(BaseErrMessage.getErrMessage().getError(code, replaces));
	}
	
	/**
	 * has error message or not
	 * */
	public boolean hasError() {
		Iterator<String> it = errList.iterator();
		if (it.hasNext()) {
			return true;
		}
		return false;
	}
	
	/**
	 * clear error message
	 * */
	public void clear() {
		errList.clear();
	}
	
	/**
	 * error message count
	 * */
	public int count(){
		return errList.size();
	}
	
	/**
	 * get one error message by index
	 * */
	public String getError(int index) {
		String rtn = "";
		if(errList!=null && !errList.isEmpty() && errList.size()>index){
			rtn = errList.get(index);
		}
		return rtn;
	}
	
	/**
	 * all error messages to json
	 * */
	public String errs2json(){
		return JsonUtil.toJsons(errList);
	}
	
	/**
	 * all error messages to html
	 * */
	public String errs2str(String flag){
		StringBuilder rtn = new StringBuilder();
		for (int i = 0; i < this.count(); i++) {
			String errLine = this.getError(i);
			if (errLine!=null && !"".equals(errLine.trim()))
				rtn.append(errLine).append(flag);
		}
		return rtn.toString();
	}

	public void setMaxErrCount(int maxErrCount) {
		this.maxErrCount = maxErrCount;
	}

	public int getMaxErrCount() {
		return maxErrCount;
	}

}
