package com.it.common.component.excel.poi;

import java.util.Calendar;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

public class ExcelCell {
	
	private ExcelStyle style=new ExcelStyle();
	private Cell cell;
	private Map<String, CellStyle> styleMap;
	private ExcelDoc document;
	private CellRangeAddress region;
	
	protected ExcelCell(Row row,int col,ExcelDoc doc) {
		this.cell = row.getCell(col);
		if(this.cell==null)	this.cell=row.createCell(col);
		this.styleMap=doc.styleMap;
		this.document=doc;
	}
	
	/**
	 * 判断当前这个cell是不是在一个合并单元格中
	 * @return
	 */
	public boolean hasMerged(){
		return this.document.mergeRegions.containsKey(cell.getRowIndex()+"_"+cell.getColumnIndex())?true:false;
		//return ExcelUtil.hasMerged(cell.getRowIndex(),cell.getColumnIndex(),cell.getSheet());
	}
	
	public Comment getComment(){
		return cell.getCellComment();
	} 
	
	public void setComment(String remark, int colEnd, int rowEnd){
		setComment(remark, "", colEnd, rowEnd);
	}
	
	public void setComment(String remark, String author, int colEnd, int rowEnd){
		setComment(remark, author, cell.getColumnIndex(), cell.getColumnIndex()+colEnd, cell.getRowIndex(), cell.getRowIndex()+rowEnd);
	}
	
	public void setComment(String remark, String author, int col1, int col2, int row1, int row2){
		CreationHelper factory = document.getWorkbook().getCreationHelper();  
	    Drawing d=document.getSheet().createDrawingPatriarch();
	    ClientAnchor anchor = factory.createClientAnchor();  
	    anchor.setCol1(col1);
	    anchor.setCol2(col2);
	    anchor.setRow1(row1);
	    anchor.setRow2(row2);
	    Comment comment=d.createCellComment(anchor);
	    //输入批注信息
	    RichTextString str = factory.createRichTextString(remark);  
	    comment.setString(str);
	    //添加作者
	    comment.setAuthor(author);
	    //将批注添加到单元格对象中
	    cell.setCellComment(comment);
	}

	/************************设置单元格样式***************************************/
	/**
	 * 直接使用POI的Style进行设置
	 * @param style
	 */
	protected void setCellStyle(CellStyle style){
		cell.setCellStyle(style);
	}
	/**
	 * 使用经过包装的style进行单元格样式设置
	 */
	protected void setCellStyle(){
		style.setCellStyle(cell,styleMap);
	}
	/**
	 * 设置单元格字体
	 * @param font
	 */
	public void setFont(Font font){
		  /*// 创建字体对象   
	      Font ztFont = doc.createFont();   
	      ztFont.setItalic(true);                         // 设置字体为斜体字   
	      ztFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//字体加粗
	      ztFont.setColor(Font.COLOR_RED);               // 将字体设置为“红色”   
	      ztFont.setFontHeightInPoints((short)9);         // 将字体大小设置为18px   
	      ztFont.setFontName("宋体");                      // 将字体应用到当前单元格上   
	      ztFont.setUnderline(Font.U_DOUBLE);         // 添加（Font.U_SINGLE单条下划线/Font.U_DOUBLE双条下划线）   
	      ztFont.setStrikeout(true);                 // 是否添加删除线
		   */		
		style.setFont(font);  
	}
	
	/**
	 * 如果多个cell共用一个style，如果想某个cell改变颜色，其它cell不变，调用该方法
	 * */
	public void setColor(short color){
		if(cell!=null){
			CellStyle style = document.getWorkbook().createCellStyle();
			style.cloneStyleFrom(cell.getCellStyle());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFillForegroundColor(color);
			cell.setCellStyle(style);
		}
	}
	
	/**
	 * 设置单元格填充颜色
	 * @param bkColor
	 */
	public void setCellFillForegroundColor(Short bkColor){
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(bkColor);//64是没有格式
	}
	/**
	 * 设置单元格边框样式 
	 * @param border_type 边框线条样式:CellStyle.BORDER_DOUBLE(双边线),CellStyle.BORDER_THIN(细边线),CellStyle.BORDER_MEDIUM(中等边线),
	 * 								  CellStyle.BORDER_DASHED(虚线边线),CellStyle.BORDER_HAIR(小圆点虚线边线),CellStyle.BORDER_THICK(粗边线)
	 * @param border_color 边框颜色
	 * @param rectangle 具体设置的边框：Rectangle.TOP+Rectangle.Bottom+Rectangle.LEFT+Rectangle.RIGHT
	 */
	public void setCellBorder(int rectangle){
		this.setCellBorder(CellStyle.BORDER_THIN, HSSFColor.BLACK.index, rectangle);
	}
	
	public void setCellBorder(short border_type,int rectangle){
		this.setCellBorder(border_type, HSSFColor.BLACK.index, rectangle);
	}
	
	public void setCellBorder(short border_type,short border_color,int rectangle){
		switch(rectangle){
			case 1:{
				style.setBorderTop(border_type); 
				style.setTopBorderColor(border_color);
				break;
			}
			case 2:{
				style.setBorderBottom(border_type); 
				style.setBottomBorderColor(border_color);
				break;
			}
			case 3:{
				style.setBorderTop(border_type); 
				style.setTopBorderColor(border_color);
				style.setBorderBottom(border_type); 
				style.setBottomBorderColor(border_color);
				break;
			}
			case 4:{
				style.setBorderLeft(border_type); 
				style.setLeftBorderColor(border_color);
				break;
			}
			case 5:{
				style.setBorderTop(border_type); 
				style.setTopBorderColor(border_color);
				style.setBorderLeft(border_type); 
				style.setLeftBorderColor(border_color);
				break;
			}
			case 6:{
				style.setBorderBottom(border_type); 
				style.setBottomBorderColor(border_color);
				style.setBorderLeft(border_type); 
				style.setLeftBorderColor(border_color);
				break;
			}
			case 7:{
				style.setBorderTop(border_type); 
				style.setTopBorderColor(border_color);
				style.setBorderBottom(border_type); 
				style.setBottomBorderColor(border_color);
				style.setBorderLeft(border_type); 
				style.setLeftBorderColor(border_color);
				break;
			}
			case 8:{
				style.setBorderRight(border_type); 
				style.setRightBorderColor(border_color);
				break;
			}
			case 9:{
				style.setBorderTop(border_type); 
				style.setTopBorderColor(border_color);
				style.setBorderRight(border_type); 
				style.setRightBorderColor(border_color);
				break;
			}
			case 10:{
				style.setBorderBottom(border_type); 
				style.setBottomBorderColor(border_color);
				style.setBorderRight(border_type); 
				style.setRightBorderColor(border_color);
				break;
			}
			case 11:{
				style.setBorderTop(border_type); 
				style.setTopBorderColor(border_color);
				style.setBorderBottom(border_type); 
				style.setBottomBorderColor(border_color);
				style.setBorderRight(border_type); 
				style.setRightBorderColor(border_color);
				break;
			}
			case 12:{
				style.setBorderLeft(border_type); 
				style.setLeftBorderColor(border_color);
				style.setBorderRight(border_type); 
				style.setRightBorderColor(border_color);
				break;
			}
			case 13:{
				style.setBorderTop(border_type); 
				style.setTopBorderColor(border_color);
				style.setBorderLeft(border_type); 
				style.setLeftBorderColor(border_color);
				style.setBorderRight(border_type); 
				style.setRightBorderColor(border_color);
				break;
			}
			case 14:{
				style.setBorderBottom(border_type); 
				style.setBottomBorderColor(border_color);
				style.setBorderLeft(border_type); 
				style.setLeftBorderColor(border_color);
				style.setBorderRight(border_type); 
				style.setRightBorderColor(border_color);
				break;
			}
			default :{
				style.setBorderTop(border_type); 
				style.setTopBorderColor(border_color);
				style.setBorderBottom(border_type); 
				style.setBottomBorderColor(border_color);
				style.setBorderLeft(border_type); 
				style.setLeftBorderColor(border_color);
				style.setBorderRight(border_type); 
				style.setRightBorderColor(border_color);
				break;
			}
		}

	}
	
	/**
	 * 设为true，如果内容单元格放不下，则在单元格内换行显示
	 * */
	public void setWrapText(boolean wrapText){
		style.setWrapText(wrapText);
	}
	
	/**
	 * 设为true，单元格使用“缩小字体填充”
	 * */
	public void setShrinkToFit(boolean shrinkToFit){
		style.setWrapText(false);
		style.setShrinkToFit(shrinkToFit);
	}
	
	/**
	 * 设置单元内容的水平对齐方式   
	 * @param horizontalAlign CellStyle.ALIGN_CENTER,CellStyle.ALIGN_LEFT,CellStyle.ALIGN_RIGHT,CellStyle.SQUARES(分散对齐)
	 */
	public void setCellHorizontalAlign(short horizontalAlign){
		style.setAlignment(horizontalAlign);   
	}
	/**
	 * 设置单元格内容垂直对其方式   
	 * @param verticalAlign:CellStyle.VERTICAL_TOP,CellStyle.VERTICAL_CENTER,CellStyle.VERTICAL_BOTTOM
	 */
	public void setCellVerticalAlign(short verticalAlign){ 
		style.setVerticalAlignment(verticalAlign);   
	}
	/**
	 * 设置单元格内容是否自动换行   
	 */
	public void setCellAutoChangeLine(){
		style.setWrapText(true);                
	}
	
	/**
	 * 单元格合并
	 * @param cols 没有行数默认为1，当前行
	 */
	private int mergeCol=1;
	private int mergeRow=1;
	
	public void mergedCells(){
		if(mergeCol!=1||mergeRow!=1){
			this.region = ExcelUtil.mergedCells(cell, mergeCol, mergeRow);
			document.mergeCells.add(this);
			setRegionColRows();
		}
	}
	
	private void setRegionColRows(){
		for(int i=this.region.getFirstRow(); i<=this.region.getLastRow(); i++){
			for(int j=this.region.getFirstColumn(); j<=this.region.getLastColumn(); j++){
				document.mergeRegions.put(i+"_"+j, new int[]{i,j});
			}
		}
	}
	
	protected void setRegionBorder(){
		if(this.region!=null){
			if(style.getBorderBottom()!=null) RegionUtil.setBorderBottom(style.getBorderBottom(),region, document.getSheet(), document.getWorkbook());  
	        if(style.getBorderLeft()!=null) RegionUtil.setBorderLeft(style.getBorderLeft(),region, document.getSheet(), document.getWorkbook());  
	        if(style.getBorderRight()!=null) RegionUtil.setBorderRight(style.getBorderRight(),region, document.getSheet(), document.getWorkbook());  
	        if(style.getBorderTop()!=null) RegionUtil.setBorderTop(style.getBorderTop(),region, document.getSheet(), document.getWorkbook());
		}
	}
	
	public void setColCells(int cols){
		this.mergeCol=cols;
	}
	
	public void setRowCells(int rows){
		this.mergeRow=rows;
	}
	
	public void setColAndRowCells(int cols,int rows){
		this.mergeCol=cols;
		this.mergeRow=rows;
	}
	
//	public void setCellEnCoding(){
//	.setEncoding(Cell.ENCODING_UTF_16);  
//}

	/************************给单元格設值********************************/
	public void setValue(String value){
		cell.setCellType(Cell.CELL_TYPE_STRING);    
		cell.setCellValue(value); 
	}
	
	public void setValue(double value){
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);   
		cell.setCellValue(value);
	}
	
	public void setValue(int value){
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);   
		cell.setCellValue(value);   
	}

	public void setValue(long value){
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);   
		cell.setCellValue(value);   
	}
	
	public void setValue(Calendar value){
		cell.setCellValue(value.getTime());
		style.setDataFormat(document.getDateFormat()); 
	}
	
	public void setValue(Calendar value, String formatStr){
		cell.setCellValue(value.getTime());   
		cell.setCellValue(document.getFormat(formatStr)); 
	}
	
	public void setValueWithDefaultFormat(double value){
		setValue(value);    
        style.setDataFormat(document.getDoubleFormat()); // 设置cell样式为定制的浮点数格式   
	}
	
	public void setValue(double value, String formatStr){
		setValue(value);
        style.setDataFormat(document.getFormat(formatStr));
	}
	
	public void setValueWithDefaultFormat(long value){
		setValue(value);
	    style.setDataFormat(document.getIntFormat()); // 设置cell样式为定制的格式   
	}
	
	public void setValue(long value, String formatStr){
		setValue(value);
        style.setDataFormat(document.getFormat(formatStr));
	}
	
	public void setValueWithDefaultFormat(int value){
		setValue(value);
	    style.setDataFormat(document.getIntFormat()); // 设置cell样式为定制的格式   
	}
	
	public void setValue(int value, String formatStr){
		setValue(value);
        style.setDataFormat(document.getFormat(formatStr));
	}
	
	/**
	 * 设置cell的类型
	 * Cell.CELL_TYPE_BLANK
	 * Cell.CELL_TYPE_BOOLEAN
	 * Cell.CELL_TYPE_NUMERIC
	 * Cell.CELL_TYPE_FORMULA   函数类型
	 * Cell.CELL_TYPE_STRING
	 * Cell.CELL_TYPE_ERROR
	 * */
	public void setCellType(int CellCELL_TYPE){
		cell.setCellType(CellCELL_TYPE);
	}

	/**
	 * 设置函数公式
	 * eg：formula="SUM("+"A1"+","+"A2"+","+"A3"+")"; 
	 *     A1 A2为一个region，可以是单个cell， 也可以是 一个合并过的单元格
	 * */
	public void setCellFormula(String formula){
		cell.setCellFormula(formula);;
	}
	
//**************************************** 获取值*************************//	
	public String getStringValue(){
		if (cell == null) return "";    
        String strCell = "";
        	switch (cell.getCellType()) {    
		        case Cell.CELL_TYPE_STRING:    
		            strCell = cell.getStringCellValue();    
		            break;    
		        case Cell.CELL_TYPE_NUMERIC:
		        	/*if(XSSFDateUtil.isCellDateFormatted(cell)){
		        		Date d=cell.getDateCellValue();
		        		DateFormat df = DateFormat.getDateInstance();
		        		strCell=df.format(d);
		        	}else
		        		strCell = String.valueOf(cell.getNumericCellValue());*/
		        	strCell = String.valueOf(cell.getNumericCellValue());
		            break;    
		        case Cell.CELL_TYPE_BOOLEAN:    
		            strCell = String.valueOf(cell.getBooleanCellValue());    
		            break;    
		        case Cell.CELL_TYPE_BLANK:    
		            strCell = "";    
		            break;    
		        default:    
		            strCell = "";    
		            break;    
	        }    
	    if (strCell == null)   return "";    
	    return strCell;
	}
	
	//获取单元格的填充色
    public String getCellFillForegroundColor(){
    	String defaultColor="64";//64是没有格式
    	if(cell==null) return defaultColor;
    	CellStyle style = cell.getCellStyle();  
    	return style.getFillForegroundColor()+"";
    }
	
	
//******************************************** test used ****************************************//
	public String getCellRowIndex(){
		return cell.getRowIndex()+"";
	}
	
	public String getCellColIndex(){
		return cell.getColumnIndex()+"";
	}
	

}
