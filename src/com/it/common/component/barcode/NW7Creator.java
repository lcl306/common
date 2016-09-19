package com.it.common.component.barcode;

import org.krysalis.barcode4j.impl.codabar.CodabarBean;
import org.krysalis.barcode4j.tools.UnitConv;



/**
 * @author lcl
 * @date 2009-4-3
 * */
public class NW7Creator {

	/**
	 * @param dpi
	 *      moduleWidth(X) is the width of narrow bar, >0.191mm, so if isFitDip is true, dpi =< 130
	 * @param widthFactor
	 * 		widthFactor <= 3.0  and widthFactor >= 2.2
	 * @param barHeight
	 * 		barHeight is height of barcode, must > (length of barcode) * 15% and 5mm
	 * @param fontSize
	 * 		fontSize is the size of code under bar
	 * @param code
	 * 		code is the value input
	 * @return barcode4j.BarcodeBean
	 *
	 * @author lcl
	 * @date 2009-4-13
	 * */
	public static BarcodeDto createNw7Bean(int dpi, boolean isFitDpi, Double widthFactor,
			Double barHeight, Double fontSize, String code){
		CodabarBean bean = new CodabarBean();
		if(isFitDpi) bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
		if(widthFactor!=null) bean.setWideFactor(widthFactor);
		if(barHeight!=null) bean.setBarHeight(barHeight);
		if(fontSize!=null) bean.setFontSize(fontSize);
		bean.setFontName("HeiseiKakuGo-W5");
		bean.setQuietZone(0.07);
		BarcodeDto codeBean = new BarcodeDto(bean);
		codeBean.setCode(code);
		codeBean.setDpi(dpi);
		return codeBean;
	}

	/**
	 * @author lcl
	 * @date 2009-4-13
	 * @see  createNw7Bean(int, boolean, Double, Double, Double, String)
	 * */
	public static BarcodeDto createNw7Bean(Double barHeight, Double fontSize, String code){
		return createNw7Bean(95, true, 3.0, barHeight, fontSize, code);
	}

	/**
	 * @author lcl
	 * @date 2009-4-13
	 * @see  createNw7Bean(int, boolean, Double, Double, Double, String)
	 * */
	public static BarcodeDto createNw7Bean(String code){
		return createNw7Bean(8.0, 0.0, code);
	}
	
	//gaohan20101230
	public static BarcodeDto createNw7BeanForKenshu(String code){
		return createNw7Bean(10.0, 0.0, code);
	}

}
