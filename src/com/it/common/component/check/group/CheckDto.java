package com.it.common.component.check.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * check meta
 * @author liu
 * **/
public class CheckDto implements Serializable{
	
	private static final long serialVersionUID = -312804599615231904L;

	///////////////////////////////////////////////check information/////////////////////////////////////
	
	protected Map<String, String> checkType = new HashMap<String, String>();
	
	protected Integer checkLength;
	
	protected Integer checkDotLength;
	
	protected List<String> checkMatch = new ArrayList<String>();
	
	protected String checkRegex;
	
	protected String checkMstSql;
	
	protected String empty;
	
	protected String dateCheckType;
	
	protected String figureType;
	
	protected String mstType;
	
	protected String range = "";
	
	protected String errCode;
	
	protected String errName;
	
	protected boolean isDay6;
	
	protected String matchStart;
	
	protected String matchEnd;
	
	protected int dateFormat;
	
	protected String byteCode;
	
	/**
	 * trim blank or not
	 * */
	protected boolean trimBlank = true;
	
	protected Map<String, ?> sqlParams;
	
	public CheckDto(){
		dateFormat = 1;
	}

	public boolean isTrimBlank() {
		return trimBlank;
	}

	public void setTrimBlank(boolean trimBlank) {
		this.trimBlank = trimBlank;
	}

	public Map<String, ?> getSqlParams() {
		return sqlParams;
	}

	public void setSqlParams(Map<String, ?> sqlParams) {
		this.sqlParams = sqlParams;
	}

	public String getMatchStart() {
		return matchStart;
	}

	public void setMatchStart(String matchStart) {
		this.matchStart = matchStart;
	}

	public String getMatchEnd() {
		return matchEnd;
	}

	public void setMatchEnd(String matchEnd) {
		this.matchEnd = matchEnd;
	}

	public Map<String, String> getCheckType() {
		return checkType;
	}

	/**
	 * checkType      H:  半角以外無効
	 *                Z:  全角以外無効
	 *                I:  整数
	 *                S:  小数
	 *                FI: 符号 + 整数
	 *                FS: 符号 + 小数
	 *                D:  日期
	 *                K:  禁止文字
	 * */
	public void setCheckType(String... checkType){
		if(checkType!=null && checkType.length>0){
			for(String c : checkType){
				this.getCheckType().put(c, "1");
			}
		}
	}
	
	public void setCheckType(Map<String, String> checkType) {
		this.checkType = checkType;
	}

	public Integer getCheckLength() {
		return checkLength;
	}

	/**
	 *  length of item
	 * */
	public void setCheckLength(Integer checkLength) {
		this.checkLength = checkLength;
	}

	public Integer getCheckDotLength() {
		return checkDotLength;
	}

	/**
	 *  dot length of item
	 * */
	public void setCheckDotLength(Integer checkDotLength) {
		this.checkDotLength = checkDotLength;
	}

	public List<String> getCheckMatch() {
		return checkMatch;
	}
	
	/**
	 *  item match, eg: enable match "0" or "1"
	 * */
	public void setCheckMatch(List<String> checkMatch) {
		this.checkMatch = checkMatch;
	}
	
	public String getCheckRegex() {
		return checkRegex;
	}

	/**
	 *  item match regular expression
	 * */
	public void setCheckRegex(String checkRegex) {
		this.checkRegex = checkRegex;
	}

	public String getCheckMstSql() {
		return checkMstSql;
	}

	/**
	 *  sql for check item exist or not exist in database table
	 * */
	public void setCheckMstSql(String checkMstSql) {
		this.checkMstSql = checkMstSql;
	}

	public String getEmpty() {
		return empty!=null?empty.trim():"";
	}
	
	/**
	 * empty "" or "0":  value ""  pass
	 *       "1"      :  value ""  error
	 *       "2"      :  value "0" error
	 * */
	public void setEmpty(String empty) {
		this.empty = empty;
	}

	public String getDateCheckType() {
		return dateCheckType!=null?dateCheckType.trim():null;
	}

	/**
	 *   date check type  d:   date                   pass
	 *                    zd:  date or zero           pass
	 *                    nd:  date or nine           pass
	 *                    znd: date or zero or nine   pass
	 *                    t:   time                   pass
	 * */
	public void setDateCheckType(String dateCheckType) {
		this.dateCheckType = dateCheckType;
	}

	public String getFigureType() {
		return figureType!=null?figureType.trim():null;
	}

	/**
	 *  figure type e :  value =  length,  pass
	 *              l :  value <= length, pass
	 *              o :  value >= length, pass
	 *              il:  valid value <= length, pass   eg: value=000123, length = 5 pass;  value = 222123, length = 5, error
	 * */
	public void setFigureType(String figureType) {
		this.figureType = figureType;
	}

	public String getMstType() {
		return mstType!=null?mstType.trim():"n";
	}

	/***
	 *  mst type n :  does not database check
	 *           ex:  database table has item value      pass
	 *           ne:  database table has not item value  pass
	 * */
	public void setMstType(String mstType) {
		this.mstType = mstType;
	}

	public boolean isDay6() {
		return isDay6;
	}

	public void setDay6(boolean isDay6) {
		this.isDay6 = isDay6;
	}

	public int getDateFormat() {
		return dateFormat;
	}

	/**
	 * 0 is yyyyddmm    yyddmm     hhmiss
	 * 1 is yyyy/dd/mm  yy/dd/mm   hh:mi:ss
	 * */
	public void setDateFormat(int dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getErrCode() {
		return errCode!=null?errCode.trim():"";
	}

	/***
	 *  error message use: item code
	 * */
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrName() {
		return errName!=null?errName.trim():"";
	}

	/***
	 *  error message use: item name
	 * */
	public void setErrName(String errName) {
		this.errName = errName;
	}

	public String getRange() {
		return range!=null?range.trim():"";
	}

	/***
	 *  （開始） or （終了）
	 * */
	public void setRange(String range) {
		this.range = range;
	}

	public String getByteCode() {
		return byteCode;
	}

	public void setByteCode(String byteCode) {
		this.byteCode = byteCode;
	}

}
