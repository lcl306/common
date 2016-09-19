package com.it.common.component.excel.poi;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelDoc {
	private Sheet sheet;
	private Workbook workbook;
	protected Map<String,CellStyle> styleMap=new HashMap<String,CellStyle>();
	protected Set<ExcelCell> mergeCells = new HashSet<ExcelCell>();
	DataFormat format;
	
	// 定制日期格式   
    protected  String dateFormat = "YYYY/MM/DD";
    // 定制浮点数格式   
    protected  String doubleFormat = " #,##0.00";
    protected  String intFormat = " #,##0";
    protected  Font defaultFont;
    protected int startRow=0;//默认从第0行开始
    protected int maxRow=0;
    
    protected short height = 400;
    
    protected Map<String, int[]> mergeRegions = new HashMap<String, int[]>();
	
    public Workbook getWorkbook() {
		return workbook;
	}

	public Sheet getSheet() {
		return sheet;
	}
	
	/**
	 * pageSize 比如：PrintSetup.A4_ROTATED_PAPERSIZE
	 * */
	public void setSheet(short pageSize, double topMargin, double rightMargin, double bottomMargin, double leftMargin){
		PrintSetup printSetup= sheet.getPrintSetup();
		printSetup.setPaperSize(pageSize);
		sheet.setPrintGridlines(false);
		sheet.setMargin(Sheet.TopMargin,topMargin); // 上边距
		sheet.setMargin(Sheet.BottomMargin,bottomMargin); // 下边距
		sheet.setMargin(Sheet.LeftMargin,leftMargin); // 左边距
		sheet.setMargin(Sheet.RightMargin,rightMargin); // 右边距 
	}
	
	/**
	 * 分页时，设置该区域总是打印
	 * */
	public void setRepeatingRows(int rowStart, int rowEnd, int colStart, int colEnd){
		sheet.setRepeatingRows(new CellRangeAddress(rowStart, rowEnd, colStart, colEnd));
	}
	
	/**
	 * 设置页脚右侧信息
	 * */
	public void setRightFoot(String message){
		sheet.getFooter().setRight(message);
	}
	
	/**
	 * 设置页脚左侧信息，页面信息可以是：第&P页 共&N页
	 * */
	public void setCenterFoot(String message){
		sheet.getFooter().setRight(message);
	}
	
	/**
	 * 设置页脚左侧信息，页面信息可以是：第&P页 共&N页
	 * */
	public void setLeftFoot(String message){
		sheet.getFooter().setLeft(message);
	}
	
	/**
	 * 设置页脚右侧页信息
	 * */
	public void setRightFootPage(){
		this.setRightFoot("第&P页 共&N页");
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

	protected ExcelDoc(Sheet sheet, Workbook workbook){
		this.sheet=sheet;
		this.workbook = workbook;
		this.format=sheet.getWorkbook().createDataFormat();
		sheet.setDefaultRowHeight(height);
	}
    
	public short getDateFormat() {
		
		return workbook.getCreationHelper().createDataFormat().getFormat(dateFormat);
	}
	
	public short getFormat(String formatStr){
		return workbook.getCreationHelper().createDataFormat().getFormat(formatStr);
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
    	cell.setCellStyle();
    	cell.mergedCells();//合并单元格
    }
	
	public void addCell(ExcelCell cell,CellStyle style){
    	cell.setCellStyle(style);
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
    
    /**
     * 有-0.01~0.09的误差
     * */
    public void setCellWidth(float[] width){
		int colIdx=0;
		for(float w:width){
			sheet.setColumnWidth(colIdx, new BigDecimal((w+0.7)*256+w+8).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
			colIdx++;
		}
		colCellNum=width.length;
    }
  
    private Row row;

    public ExcelCell createCell(int col){
    	ExcelCell cell=new ExcelCell(row, col,this);
    	if(defaultFont!=null) cell.setFont(defaultFont);
    	return cell;
    }
    
    public ExcelCell createCell(int rowNum,int col){
    	Row rowt=sheet.getRow(rowNum);
    	if(rowt==null) rowt=this.createRow(rowNum);
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
        	if(this.row==null) this.row = this.createRow(currentRow);//初始化创建第一行
        	if(currentCol==colCellNum){//换行
        		currentCol=0;
        		currentRow++;
        		this.row = this.createRow(currentRow);
        	}
        	cell=this.createCell(currentCol);
    	}
    	if(defaultFont!=null) cell.setFont(defaultFont);
    	return cell;
    }    
    
    public Font createFont(){
    	return sheet.getWorkbook().createFont();
    }
    
    public CellStyle createCellStyle(){
		return sheet.getWorkbook().createCellStyle();
	}
    
    public DataFormat createDataFormat(){
    	return sheet.getWorkbook().createDataFormat();
    }
    
    /**  
     * 增加一行    
     * @param index  行号  
     */  
    public Row createRow(int index) {   
    	Row row = this.sheet.createRow(index);   
    	row.setHeight(height);
    	return row;
    }
    
    public Cell createCell(Row row,int colIdx) {   
        return row.createCell(colIdx);   
    }
    
    protected void addRegionStyle(){
    	for(ExcelCell cell : this.mergeCells){
    		cell.setRegionBorder();
    	}
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
    	Row rowCon = sheet.getRow(rowIdx); 
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
    	Row rowCon = sheet.getRow(row);
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
    	Row rowCon = sheet.getRow(row);
    	int cellNum=rowCon.getPhysicalNumberOfCells();
    	ExcelCell[] cells=new ExcelCell[cellNum];
    	for(int i=0;i<cellNum;i++){
    		cells[i]=this.readCell(i, row);
    	}
    	return cells;
    }
    
    
    
}
