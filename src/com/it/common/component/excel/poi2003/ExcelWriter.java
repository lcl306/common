package com.it.common.component.excel.poi2003;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public abstract class ExcelWriter {
	//设置cell编码解决中文高位字节截断   
    //private static short XLS_ENCODING = HSSFWorkbook.;   
  
    private String xlsFileName;    
    private HSSFWorkbook workbook;     
  
    public ExcelWriter() {
    	this.workbook = new HSSFWorkbook();   
    }
    
    /**  
     * 初始化Excel    
     * @param fileName  导出文件名  
     */
    public ExcelWriter(String fileName) {
    	this();
    	this.xlsFileName = fileName;
    }
    
    public ExcelDoc createExcelDoc(){
    	return createExcelDoc("sheet1");
    }
    
    public ExcelDoc createExcelDoc(String sheetName){
    	HSSFSheet sheet = workbook.createSheet(sheetName); 
    	return new ExcelDoc(sheet, workbook);
    }
    
    protected abstract void buildXLS();
    
    public void exportXLS(OutputStream out) throws Exception {   
        try {   
            this.buildXLS();
            workbook.write(out);
        } catch (IOException e) {   
        	throw new Exception(" 生成导出Excel文件出错! ", e);   
        }
    }
    
    public void exportXLS() throws Exception {
    	exportXLS(xlsFileName);
    }
    
    public void exportXLS(String xlsFileName) throws Exception { 
    	FileOutputStream fOut = new FileOutputStream(xlsFileName); 
    	exportXLS(fOut);
    	fOut.flush();   
        fOut.close(); 
    }
    
    

 
}
