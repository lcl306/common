package com.it.common.component.excel.poi2003;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;

public class ExcelStyle {
	private Font font;//单元格字体
	private Short fillForegroundColor;
	private Short fillPattern;
	
	private Short alignment;//单元内容的水平对齐方式
	private Short verticalAlignment;//单元格内容垂直对其方式   
	
	private Boolean wrapText;
	private Short dataFormat;//单元格数据类型
	
	private Short borderTop;
	private Short topBorderColor;
	private Short borderBottom;
	private Short bottomBorderColor;
	private Short borderLeft;
	private Short leftBorderColor;
	private Short borderRight;
	private Short rightBorderColor;
	
	
	public void setHSSFCellStyle(HSSFCell cell,Map<String,HSSFCellStyle> styleMap){
		Field[] fields=this.getClass().getDeclaredFields();
		boolean hasSet=false;
		for(Field f:fields){
			String name=f.getName().substring(0,1).toUpperCase()+f.getName().substring(1);
			try {
				Method m = this.getClass().getMethod("get"+name);
				if(m.invoke(this)!=null){
	            	 hasSet=true;
	            	 break;
	             }
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		if(!hasSet) return;
		
		if(styleMap!=null){
			HSSFCellStyle style=styleMap.get(this.getExcelStyleKey());
			if(style==null){
				style=this.setStyle(cell.getSheet().getWorkbook());
				styleMap.put(this.getExcelStyleKey(), style);
			}
			cell.setCellStyle(style); // 样式应用到该单元格上
		}
	}
	
	public String getExcelStyleKey(){
		StringBuilder sb=new StringBuilder();
		Field[] fields=this.getClass().getDeclaredFields();
		for(Field f:fields){
			String name=f.getName().substring(0,1).toUpperCase()+f.getName().substring(1);
			try {
				Method m = this.getClass().getMethod("get"+name);
				Object value=m.invoke(this);
				if(value!=null){
					sb.append(name);
					if(value instanceof Short)
						sb.append((short)value+",");
					else if(value instanceof Integer)
						sb.append((int)value+",");
					else if(value instanceof Long)
						sb.append((long)value+",");
					else if(value instanceof Boolean)
						sb.append((boolean)value+",");
					else
						sb.append(value.toString()+",");
	             }
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	private HSSFCellStyle setStyle(HSSFWorkbook book){
		HSSFCellStyle style=book.createCellStyle();
		Field[] fields=this.getClass().getDeclaredFields();
		for(Field f:fields){
			String name=f.getName().substring(0,1).toUpperCase()+f.getName().substring(1);
			try {
				Method m = this.getClass().getMethod("get"+name);
				Object value=m.invoke(this);
				if(value!=null){
					Class<?> type=f.getType();
					if(type==Short.class) type=short.class;
					if(type==Integer.class) type=int.class;
					if(type==Long.class) type=long.class;
					if(type==Boolean.class) type=boolean.class;
					if(type==Double.class) type=double.class;
					Method m2 = style.getClass().getMethod("set"+name,type);
					m2.invoke(style,value);
	             }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return style;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Short getFillForegroundColor() {
		return fillForegroundColor;
	}

	public void setFillForegroundColor(Short fillForegroundColor) {
		this.fillForegroundColor = fillForegroundColor;
	}

	public Short getFillPattern() {
		return fillPattern;
	}

	public void setFillPattern(Short fillPattern) {
		this.fillPattern = fillPattern;
	}

	public Short getAlignment() {
		return alignment;
	}

	public void setAlignment(Short alignment) {
		this.alignment = alignment;
	}

	public Short getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(Short verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	public Boolean getWrapText() {
		return wrapText;
	}

	public void setWrapText(Boolean wrapText) {
		this.wrapText = wrapText;
	}

	public Short getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(Short dataFormat) {
		this.dataFormat = dataFormat;
	}

	public Short getBorderTop() {
		return borderTop;
	}

	public void setBorderTop(Short borderTop) {
		this.borderTop = borderTop;
	}

	public Short getTopBorderColor() {
		return topBorderColor;
	}

	public void setTopBorderColor(Short topBorderColor) {
		this.topBorderColor = topBorderColor;
	}

	public Short getBorderBottom() {
		return borderBottom;
	}

	public void setBorderBottom(Short borderBottom) {
		this.borderBottom = borderBottom;
	}

	public Short getBottomBorderColor() {
		return bottomBorderColor;
	}

	public void setBottomBorderColor(Short bottomBorderColor) {
		this.bottomBorderColor = bottomBorderColor;
	}

	public Short getBorderLeft() {
		return borderLeft;
	}

	public void setBorderLeft(Short borderLeft) {
		this.borderLeft = borderLeft;
	}

	public Short getLeftBorderColor() {
		return leftBorderColor;
	}

	public void setLeftBorderColor(Short leftBorderColor) {
		this.leftBorderColor = leftBorderColor;
	}

	public Short getBorderRight() {
		return borderRight;
	}

	public void setBorderRight(Short borderRight) {
		this.borderRight = borderRight;
	}

	public Short getRightBorderColor() {
		return rightBorderColor;
	}

	public void setRightBorderColor(Short rightBorderColor) {
		this.rightBorderColor = rightBorderColor;
	}
	
	

	
}
