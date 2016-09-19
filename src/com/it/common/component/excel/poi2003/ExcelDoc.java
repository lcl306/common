package com.it.common.component.excel.poi2003;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;

public class ExcelDoc {
	private HSSFSheet sheet;
	private HSSFWorkbook workbook;
	protected Map<String,HSSFCellStyle> styleMap=new HashMap<String,HSSFCellStyle>();
	HSSFDataFormat format;
	
	// 定制日期格式   
    protected  String dateFormat = "YYYY/MM/DD";
    // 定制浮点数格式   
    protected  String doubleFormat = " #,##0.00";
    protected  String intFormat = " #,##0";
    protected  Font defaultFont;
    protected int startRow=0;//默认从第0行开始
    protected int maxRow=0;
    
    protected short height = 400;
	
    public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	public HSSFSheet getSheet() {
		return sheet;
	}
	
	/**
	 * pageSize 比如：HSSFPrintSetup.A4_ROTATED_PAPERSIZE
	 * */
	public void setSheet(short pageSize, double topMargin, double rightMargin, double bottomMargin, double leftMargin){
		HSSFPrintSetup printSetup= sheet.getPrintSetup();
		printSetup.setPaperSize(pageSize);
		sheet.setPrintGridlines(false);
		sheet.setMargin(HSSFSheet.TopMargin,topMargin); // 上边距
		sheet.setMargin(HSSFSheet.BottomMargin,bottomMargin); // 下边距
		sheet.setMargin(HSSFSheet.LeftMargin,leftMargin); // 左边距
		sheet.setMargin(HSSFSheet.RightMargin,rightMargin); // 右边距 
	}

	public int getMaxRow() {
		return maxRow;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	protected ExcelDoc(HSSFSheet sheet, HSSFWorkbook workbook){
		this.sheet=sheet;
		this.workbook = workbook;
		this.format=sheet.getWorkbook().createDataFormat();
		sheet.setDefaultRowHeight(height);
	}
    
	public short getDateFormat() {
		return HSSFDataFormat.getBuiltinFormat(dateFormat);
	}
	
	public short getFormat(String formatStr){
		return format.getFormat(formatStr);
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public short getDoubleFormat() {
		return format.getFormat(doubleFormat);
	}

	public void setDoubleFormat(String doubleFormat) {
		this.doubleFormat = doubleFormat;
	}

	public short getIntFormat() {
		return format.getFormat(intFormat);
	}

	public void setIntFormat(String intFormat) {
		this.intFormat = intFormat;
	}

	public Font getDefaultFont() {
		return defaultFont;
	}

	public void setDefaultFont(Font defaultFont) {
		this.defaultFont = defaultFont;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	
	
	public void addCell(ExcelCell cell){
    	cell.setHSSFCellStyle();
    	cell.mergedCells();//合并单元格
    }
	
	public void addCell(ExcelCell cell,HSSFCellStyle style){
    	cell.setHSSFCellStyle(style);
    	cell.mergedCells();//合并单元格
    }
    
    
    private int colCellNum;
    public void setCellWidth(int[] width){
		int colIdx=0;
		for(int w:width){
			sheet.setColumnWidth(colIdx, w*256);
			colIdx++;
		}
		colCellNum=width.length;
    }
  
    private HSSFRow row;

    public ExcelCell createCell(int col){
    	ExcelCell cell=new ExcelCell(row, col,this);
    	if(defaultFont!=null) cell.setFont(defaultFont);
    	return cell;
    }
    
    public ExcelCell createCell(int rowNum,int col){
    	HSSFRow rowt=sheet.getRow(rowNum);
    	if(rowt==null) rowt=this.createHSSFRow(rowNum);
    	ExcelCell cell=new ExcelCell(rowt, col,this);
    	if(defaultFont!=null) cell.setFont(defaultFont);
    	return cell;
    }
    
    private int currentCol=-1;
    private int currentRow=0;
    
    public ExcelCell createCell(){
    	ExcelCell cell=null;
    	while(cell==null || cell.hasMerged() ){
    		currentCol++;
        	if(this.row==null) this.row = this.createHSSFRow(currentRow);//初始化创建第一行
        	if(currentCol==colCellNum){//换行
        		currentCol=0;
        		currentRow++;
        		this.row = this.createHSSFRow(currentRow);
        	}
        	cell=this.createCell(currentCol);
    	}
    	if(defaultFont!=null) cell.setFont(defaultFont);
    	return cell;
    }    
    
    public Font createFont(){
    	return sheet.getWorkbook().createFont();
    }
    
    public HSSFCellStyle createHSSFCellStyle(){
		return sheet.getWorkbook().createCellStyle();
	}
    
    public HSSFDataFormat createHSSFDataFormat(){
    	return sheet.getWorkbook().createDataFormat();
    }
    
    /**  
     * 增加一行    
     * @param index  行号  
     */  
    public HSSFRow createHSSFRow(int index) {   
    	HSSFRow row = this.sheet.createRow(index);   
    	row.setHeight(height);
    	return row;
    }
    
    public HSSFCell createHSSFCell(HSSFRow row,int colIdx) {   
        return row.createCell(colIdx);   
    }
    
/*********************************  read excel  ***********************************************************/
    /**
     * @param coordinate 例如A1，H3这种坐标，一定字母在前
     * @return
     */
    public ExcelCell readCell(String coordinate){
    	int[] rowCol=ExcelUtil.getCellCoordinate(coordinate);
    	int rowIdx=rowCol[0];
    	int colIdx=rowCol[1];
    	return this.readCell(rowIdx, colIdx);
    }
    
    public ExcelCell readCell(int rowIdx,int colIdx){
    	HSSFRow rowCon = sheet.getRow(rowIdx); 
    	if(rowCon==null) return null;
    	int totColNum = rowCon.getPhysicalNumberOfCells();
    	if(colIdx>totColNum) return null;
    	return new ExcelCell(rowCon, colIdx,this);
    }
    
    public String[] readLine(){
    	String[] strs=null;
    	if(currentRow<this.maxRow){
    		strs=this.readLine(currentRow);
    		currentRow++;
    	}else{
    		strs=null;
    	}
    	return strs;
    }
    
    
    public String[]  readLine(int row){
    	HSSFRow rowCon = sheet.getRow(row);
    	int cellNum=rowCon.getPhysicalNumberOfCells();
    	String[] strs=new String[cellNum];
    	for(int i=0;i<cellNum;i++){
    		ExcelCell cell=this.readCell(row, i);
    		if(cell!=null) 
    			strs[i]=cell.getStringValue();
    		else
    			strs[i]="";
    	}
    	return strs;
    }
    
    public ExcelCell[]  readCellLine(int row){
    	HSSFRow rowCon = sheet.getRow(row);
    	int cellNum=rowCon.getPhysicalNumberOfCells();
    	ExcelCell[] cells=new ExcelCell[cellNum];
    	for(int i=0;i<cellNum;i++){
    		cells[i]=this.readCell(i, row);
    	}
    	return cells;
    }
    
    
    
}
