package com.it.common.component.excel.poi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.it.common.share.GlobalName;

public abstract class ExcelWriter {
	// 设置cell编码解决中文高位字节截断
	// private static short XLS_ENCODING = HSSFWorkbook.;
	protected String fileName;
//	private Workbook[] workbooks =new Workbook[] { new HSSFWorkbook(), new XSSFWorkbook() };
	protected Workbook workbook;
	
	protected Set<ExcelDoc> excelDocs = new HashSet<ExcelDoc>();
	
	protected int workbookSize = 1000;

	public void setWorkbookSize(int workbookSize) {
		this.workbookSize = workbookSize;
	}

	/**
	 * 初始化Excel
	 * @param fileName 导出文件名
	 */
	public ExcelWriter(String fileName) {
		this.workbook = WorkbookFactory.createWorkbook(fileName);
		this.fileName = fileName;
	}

	public ExcelDoc createExcelDoc() {
		return createExcelDoc("sheet1");
	}

	public ExcelDoc createExcelDoc(String sheetName) {
		Sheet sheet = workbook.createSheet(sheetName);
		ExcelDoc doc = new ExcelDoc(sheet, workbook);
		excelDocs.add(doc);
		return doc;
	}

	protected abstract void buildXLS();

	private void docCallback(){
		for(ExcelDoc doc : excelDocs){
			doc.addRegionStyle();
		}
		excelDocs.clear();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void exportXLS(HttpServletResponse response) throws Exception{
		response.setContentType("application/msexcel;charset="+GlobalName.CODE);
		response.addHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes(GlobalName.CODE), "ISO8859-1"));
		exportXLS(response.getOutputStream());
	}
	
	public void exportXLS(OutputStream out) throws Exception {
		try {
			this.buildXLS();
			docCallback();
			workbook.write(out);
		} catch (IOException e) {
			throw new Exception(" 生成导出Excel文件出错! ", e);
		}
	}

	public void exportXLS() throws Exception {
		exportXLS(fileName);
	}

	public void exportXLS(String xlsxFileName) throws Exception {
		FileOutputStream fOut = new FileOutputStream(xlsxFileName);
		exportXLS(fOut);
		fOut.flush();
		fOut.close();
	}

}
