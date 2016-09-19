package com.it.common.component.barcode;

import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.tools.UnitConv;

import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.PdfWriter;
import com.it.common.component.log.LogPrint;

/**
 * @author liu
 * */
public class EAN13Creator {
	
	/**
	 * @param code code is the value input
	 */
	public static Image createEAN13(PdfWriter writer, String code){
		BarcodeEAN codeEAN = new BarcodeEAN();  
		int checksum=calCheckSum(code);
		code=code+checksum;			
		codeEAN.setCodeType(BarcodeEAN.EAN13);
		codeEAN.setCode(code); 
		codeEAN.setGuardBars(false);  
		codeEAN.setChecksumText(true);
		codeEAN.setTextAlignment(Element.ALIGN_RIGHT);
		codeEAN.setStartStopText(false);
		return codeEAN.createImageWithBarcode(writer.getDirectContent(), null, null);
	}
	
	 /**
		 * @param code code is the value input
		 */
	public static BarcodeDto createEAN13Bean(String code){
		return createEAN13Bean(1.4, code);
	}
	
	 /**
 	 * @param barHeight barHeight is height of barCode, must >  10mm
	 * @param code code is the value input
	 */
	public static BarcodeDto createEAN13Bean(Double barHeight, String code){
		return createEAN13Bean(600, barHeight, code);
	}
	
	public static BarcodeDto createEAN13Bean(int dpi, Double barHeight, String code){
		return createEAN13Bean(dpi, true, barHeight, null, code);
	}
	
	public static BarcodeDto createEAN13Bean(int dpi, boolean isFitDpi, Double barHeight, Double fontSize, String code){
		try{
			EAN13Bean bean = new EAN13Bean();
			//default is "bean.createLogicImpl().getChecksumMode()"
			//bean.setChecksumMode(bean.createLogicImpl().getChecksumMode());
			if(isFitDpi) bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
			if(barHeight!=null) bean.setBarHeight(barHeight);
			if(fontSize!=null && fontSize!=0d){
				bean.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
				bean.setFontSize(fontSize);
				bean.setFontName("HeiseiKakuGo-W5");
			}else{
				bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
			}
			bean.setQuietZone(0.5);
			BarcodeDto codeBean = new BarcodeDto(bean);
			codeBean.setCode(code);
			codeBean.setDpi(dpi);
			return codeBean;
		}catch(Exception e){
			e.printStackTrace();
			LogPrint.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * number of code is even, same as new EAN13Bean().createLogicImpl().calcChecksum(code)
	 * */
	protected static int calCheckSum(String code){    	
		char[] chars = code.toCharArray();
		int[] ean13 = {1,3};
		int sum = 0;
		for(int i = 0 ; i<chars.length; i++){
			sum += Character.getNumericValue(chars[i]) * ean13[i%2];
		}
		int checksum = 10 - sum%10;
		if(checksum == 10){
			checksum = 0;
		}
		return checksum;
	}

}
