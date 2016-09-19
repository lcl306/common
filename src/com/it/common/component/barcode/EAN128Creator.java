package com.it.common.component.barcode;

import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Constants;
import org.krysalis.barcode4j.impl.code128.EAN128Bean;
import org.krysalis.barcode4j.tools.UnitConv;

import com.it.common.component.log.LogPrint;


public class EAN128Creator {
	
	private String template = "(10)n28+d6+n8";
	
	public void setTemplate(String template) {
		this.template = template;
	}

	public BarcodeDto createEAN128Bean(String code){
		return createEAN128Bean(3.0, code);
	}
	
	 /**
 	 * @param barHeight
	 * 		barHeight is height of barCode, must >  10mm
	 * @param code
	 * 		code is the value input
	 */
	public BarcodeDto createEAN128Bean(Double barHeight, String code){
		return createEAN128Bean(600, barHeight, code);
	}
	
    /**
 	 * @param barHeight
	 * 		barHeight is height of barCode, must >  10mm
	 * @param code
	 * 		code is the value input
	 */
	public BarcodeDto createEAN128Bean(int dpi, Double barHeight, String code){
		return createEAN128Bean(dpi, true, barHeight, null, code);
	}
	
	public BarcodeDto createEAN128Bean(int dpi, boolean isFitDpi, 
			Double barHeight, Double fontSize, String code){
		//Code128Bean bean = new Code128Bean();
		try{
			EAN128Bean bean = new EAN128Bean();
			bean.setCodeset(Code128Constants.CODESET_C);
			//bean.setChecksumMode(ChecksumMode.CP_AUTO);
			bean.setTemplate(template);
			if(isFitDpi) bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
			if(barHeight!=null) bean.setBarHeight(barHeight);
			if(fontSize!=null && fontSize!=0l){
				bean.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
				bean.setOmitBrackets(false);
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

}
