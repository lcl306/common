package com.it.common.component.barcode;

import org.krysalis.barcode4j.impl.AbstractBarcodeBean;

/**
 * @author liu
 * */
public class BarcodeDto {

	/**
	 * barcode dpi
	 * */
	private int dpi;

	/**
	 * code value
	 * */
	private String code;

	/**
	 * barcode bean
	 * */
	AbstractBarcodeBean bean;

	public BarcodeDto(AbstractBarcodeBean bean){
		this.bean = bean;
	}

	public AbstractBarcodeBean getBean() {
		return bean;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getDpi() {
		return dpi;
	}

	public void setDpi(int dpi) {
		this.dpi = dpi;
	}



}
