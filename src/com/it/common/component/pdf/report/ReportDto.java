package com.it.common.component.pdf.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.it.common.component.lang.str.StrUtil;

/**
 * @author liu
 * */
public class ReportDto {
	
	public static enum TYPE{PDF,EXCEL,HTML,RTF};
	
	/**
	 * eg: "com/sunrex/test/report/nohinSho.jrxml"
	 * */
	protected String jxmlFileName;
	
	protected String jasperFileName;
	
	protected String outputFileName;
	
	protected Enum<TYPE> type = TYPE.PDF;
	
	protected String screenCd;
	
	protected boolean autoPrint;
	
	protected HashMap<String, Object> parameter = new HashMap<String, Object>();
	
	protected JRDataSource datasource;
	
	protected List<?> datalist = new ArrayList<Object>();

	public String getJxmlFileName() {
		return jxmlFileName;
	}

	public void setJxmlFileName(String jxmlFileName) {
		this.jxmlFileName = jxmlFileName;
	}

	public String getJasperFileName() {
		return jasperFileName;
	}

	public void setJasperFileName(String jasperFileName) {
		this.jasperFileName = jasperFileName;
	}

	public String getOutputFileName() {
		return outputFileName;
	}
	
	public void setOutputFileName(String outputFileName, String screenCd) {
		this.outputFileName = outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		String screenCd = "";
		if(StrUtil.isNotEmpty(outputFileName)){
			int pos = outputFileName.indexOf(".");
			if(pos!=-1){
				screenCd = outputFileName.substring(0,pos);
			}else{
				screenCd = outputFileName;
			}
		}
		setOutputFileName(outputFileName, screenCd);
	}

	public HashMap<String, Object> getParameter() {
		return parameter;
	}

	public void setParameter(HashMap<String, Object> parameter) {
		this.parameter = parameter;
	}

	public JRDataSource getDatasource() {
		if(datasource==null && datalist!=null){
			datasource = new JRBeanCollectionDataSource(datalist);
		}
		return datasource;
	}

	public void setDatasource(JRDataSource datasource) {
		this.datasource = datasource;
	}

	public List<?> getDatalist() {
		return datalist;
	}

	public void setDatalist(List<?> datalist) {
		this.datalist = datalist;
	}

	public String getScreenCd() {
		return screenCd;
	}

	public void setScreenCd(String screenCd) {
		this.screenCd = screenCd;
	}

	public boolean isAutoPrint() {
		return autoPrint;
	}

	public void setAutoPrint(boolean autoPrint) {
		this.autoPrint = autoPrint;
	}

	public Enum<TYPE> getType() {
		return type;
	}

	public void setType(Enum<TYPE> type) {
		this.type = type;
	}

}
