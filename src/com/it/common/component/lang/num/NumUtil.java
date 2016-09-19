package com.it.common.component.lang.num;

import java.math.BigDecimal;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.it.common.component.log.LogPrint;
import com.it.common.share.GlobalName;

/**
 * @author liu
 * */
public class NumUtil {
	
	private static ScriptEngine engine;
	
	static{
		ScriptEngineManager factory = new ScriptEngineManager();
		engine = factory.getEngineByName("JavaScript");
	}
	
	/**
	 * add two number
	 * */
	public static <T> String add(T one, T two) {
		String rtn;
		try{
			BigDecimal b1 = new BigDecimal(one.toString().trim());
			BigDecimal b2 = new BigDecimal(two.toString().trim());
			rtn = b1.add(b2).toString();
		}catch(Exception e){
			rtn = "";
		}
		return rtn;
	}
	
	/**
	 * multiply two number
	 * */
	public static <T> String multiply(T one, T two) {
		String rtn;
		try{
			BigDecimal b1 = new BigDecimal(one.toString().trim());
			BigDecimal b2 = new BigDecimal(two.toString().trim());
			rtn = b1.multiply(b2).toString();
		}catch(Exception e){
			rtn = "";
		}
		return rtn;
	}
	
	/**
	 * subtract two number
	 * */
	public static <T> String subtract(T one, T two) {
		String rtn;
		try{
			BigDecimal b1 = new BigDecimal(one.toString().trim());
			BigDecimal b2 = new BigDecimal(two.toString().trim());
			rtn = b1.subtract(b2).toString();
		}catch(Exception e){
			rtn = "";
		}
		return rtn;
	}
	
	/**
	 * divide two number
	 * */
	public static <T> String divide(T one, T two) {
		String rtn;
		try{
			BigDecimal b1 = new BigDecimal(one.toString().trim());
			BigDecimal b2 = new BigDecimal(two.toString().trim());
			rtn = b1.divide(b2).toString();
		}catch(Exception e){
			rtn = "";
		}
		return rtn;
	}
	
	/**
	 * @param scale is dot length
	 * @param type FLOOR HALF_UP CEILING
	 * */
	public static <T> String num(T num, int scale, String type){
		BigDecimal tmp = null;
		try {
			tmp = new BigDecimal(num.toString().trim());
			if (GlobalName.FLOOR.equals(type)) {
				if (tmp.compareTo(new BigDecimal(0)) >= 0) {
					tmp = tmp.setScale(scale, BigDecimal.ROUND_FLOOR);
				} else {
					tmp = tmp.setScale(scale, BigDecimal.ROUND_CEILING);
				}
			}else if(GlobalName.HALF_UP.equals(type)){
				tmp = tmp.setScale(scale, BigDecimal.ROUND_HALF_UP);
			}else if(GlobalName.CEILING.equals(type)){
				if (tmp.compareTo(new BigDecimal(0)) >= 0) {
					tmp = tmp.setScale(scale, BigDecimal.ROUND_CEILING);
				} else {
					tmp = tmp.setScale(scale, BigDecimal.ROUND_FLOOR);
				}
			}
		}catch(Exception e){
			return num!=null?num.toString():"";
		}
		return tmp.toString();
	}
	
	/**
	 * calculate the expression
	 * */
	public static Object cal(String exp){
		try {
			return engine.eval(exp);
		} catch (ScriptException e) {
			LogPrint.error(e.getMessage());
			return null;
		}
	}
	
	/**
	 * get integer part of num;
	 * */
	public static String getIntPart(String num){
		try{
			int pos = num.lastIndexOf(".");
			if(pos!=-1) return num.substring(0, pos);
			else return num;
		}catch(Exception e){
			return num;
		}
	}
	
	/**
	 * get integer part of num;
	 * */
	public static String getDotPart(String num){
		try{
			int pos = num.lastIndexOf(".");
			if(pos!=-1) return num.substring(pos+1);
			else return "";
		}catch(Exception e){
			return num;
		}
	}
	

}
