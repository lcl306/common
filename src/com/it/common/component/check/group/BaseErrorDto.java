package com.it.common.component.check.group;

import java.io.Serializable;

/**
 * @author liu
 * */
public class BaseErrorDto implements Serializable{
	
	private static final long serialVersionUID = 8412868126480966830L;

	protected String errCode;
	
	protected String errMsg;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	public String toStr(){
		StringBuilder sb = new StringBuilder();
		return sb.append(errMsg).append("(").append(errCode).append(")").toString();
	}
	

}
