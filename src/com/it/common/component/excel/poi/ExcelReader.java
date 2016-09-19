package com.it.common.component.excel.poi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelReader {

	    private Workbook wb;    
	    private Sheet sheet;
	    private int maxRow;
	    
	    public ExcelReader(){}
	    
	    /**
	     * 加载Excel
	     */
	    public void initExcel(InputStream is, String excelType){
	    	try {
	            wb = WorkbookFactory.createWorkbook(is, excelType);
	        } catch (IOException e) {    
	            e.printStackTrace();    
	        }    
	        sheet = wb.getSheetAt(0); 
	        maxRow=sheet.getLastRowNum();
	    }
	    
	    public void readExcel(String fileName){
	    	FileInputStream in=null;
			try {
				in = new FileInputStream(fileName);
				this.initExcel(in, WorkbookFactory.getExcelType(fileName));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	    }
	    
	    public ExcelDoc createExcelDoc(){
	    	ExcelDoc doc=new ExcelDoc(sheet,wb);
	    	doc.maxRow=maxRow;
	    	return doc;
	    }

}
