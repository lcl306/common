package com.it.common.component.excel.poi2003;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelReader {
	  	private POIFSFileSystem fs;    
	    private HSSFWorkbook wb;    
	    private HSSFSheet sheet;
	    private int maxRow;
	    
	    public ExcelReader(){}
	    
	    /**
	     * 加载Excel
	     */
	    public void initExcel(InputStream is){
	    	try {    
	            fs = new POIFSFileSystem(is);    
	            wb = new HSSFWorkbook(fs);    
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
				this.initExcel(in);
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
	    

		public static void main(String[] args) {
			String fileName="F:\\testExcel.xls";
			ExcelReader er=new ExcelReader();
			er.readExcel(fileName);
			ExcelDoc doc=er.createExcelDoc();
			String[] strs=null;
			while((strs=doc.readLine())!=null){
				for(String str: strs){
					System.out.print(str+"     ");
				}
				System.out.println("");
			}
			
			/*FileInputStream in=null;
			try {
				in = new FileInputStream(fileName);
				er.initExcel(in);
				ExcelDoc doc=er.createExcelDoc();
				String[] strs=null;
				while((strs=doc.readLine())!=null){
					for(String str: strs){
						System.out.print(str+"     ");
					}
					System.out.println("");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}*/
		}
		
		

}
