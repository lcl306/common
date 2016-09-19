package com.it.common.component.pdf.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import com.it.common.component.lang.sys.SysUtil;
import com.it.common.component.log.LogPrint;
import com.it.common.share.GlobalName;

/**
 * @author liu
 * */
public class ReportBo {
	
	/**
	 * print jprint to response by jxmlFile or japserFile
	 * */
	public static void printToResponse(HttpServletResponse response, ReportDto dto){
		setResponse(response,dto);
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			//JasperExportManager.exportReportToPdfStream(print,  outputStream);
			print(outputStream, dto);
		} catch (IOException e) {
			e.printStackTrace();
			LogPrint.info(e.getMessage());
		}finally{
			try {
				if(outputStream!=null){
					outputStream.flush();
					outputStream.close(); 
				}
			} catch (IOException e) {
				e.printStackTrace();
				LogPrint.info(e.getMessage());
			}
		}
	}
	
	/**
	 * print jprint to file by jxmlFile or japserFile
	 * */
	public static void printToFile(ReportDto dto){
		try {
			OutputStream out = new FileOutputStream(dto.getOutputFileName());
			print(out, dto);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			LogPrint.info(e.getMessage());
		}
	}
	
	/**
	 * print jprint to outputStream by jxmlFile or japserFile
	 * */
	public static void print(OutputStream outputStream, ReportDto dto){
		try {
			JasperPrint print = fillReport(dto);
			if(dto.getType()==ReportDto.TYPE.PDF)
				exportReportToPdf(outputStream, print, dto);
			else if(dto.getType()==ReportDto.TYPE.EXCEL)
				exportReportToExcel(outputStream, print, dto);
			else if(dto.getType()==ReportDto.TYPE.RTF)
				exportReportToRtf(outputStream, print, dto);
			else if(dto.getType()==ReportDto.TYPE.HTML)
				exportReportToHtml(outputStream, print, dto);
		} catch (JRException e) {
			e.printStackTrace();
			LogPrint.info(e.getMessage());
		}
	}
	
	/**
	 * need itext
	 * */
	private static void exportReportToPdf(OutputStream outputStream, JasperPrint jasperPrint, ReportDto dto){   
        try {
        	JRPdfExporter exporter = new JRPdfExporter(DefaultJasperReportsContext.getInstance());
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
    		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
    		if(dto.isAutoPrint())
    			exporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT, "this.print(true);\r");
    		//exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,dto.getOutputFileName());
            exporter.exportReport();
        } catch (JRException e) {
        	LogPrint.info(e.getMessage());
            e.printStackTrace();   
        }     
    }
	
	/**
	 * need poi
	 * */
	private static void exportReportToExcel(OutputStream outputStream, JasperPrint jasperPrint, ReportDto dto){
		try {
			JRXlsExporter exporter = new JRXlsExporter(DefaultJasperReportsContext.getInstance());  
	        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
	        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,outputStream);  
	        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE); 
	        // true is one page one sheet
	        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);  
	        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
	        exporter.exportReport();
        } catch (JRException e) {
        	LogPrint.info(e.getMessage());
            e.printStackTrace();   
        }
	}
	
	/**
	 * need poi
	 * */
	private static void exportReportToRtf(OutputStream outputStream, JasperPrint jasperPrint, ReportDto dto){
		try {
			JRRtfExporter exporter = new JRRtfExporter(DefaultJasperReportsContext.getInstance());  
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);  
            exporter.exportReport();
        } catch (JRException e) {
        	LogPrint.info(e.getMessage());
            e.printStackTrace();   
        }
	}
	
	private static void exportReportToHtml(OutputStream outputStream, JasperPrint jasperPrint, ReportDto dto){
		try {
			 JRHtmlExporter exporter = new JRHtmlExporter();  
	         exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
	         exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	         exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,GlobalName.CODE);
	         exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,outputStream);
	         exporter.exportReport();
        } catch (JRException e) {
        	LogPrint.info(e.getMessage());
            e.printStackTrace();   
        }
	}
	
	private static JasperPrint fillReport(ReportDto dto) throws JRException{
		JasperPrint jasperPrint = null;
		if(dto.getJasperFileName()==null){	
			// JasperReport is binary file in memory, is *.jasper file
			JasperReport jasperReport = JasperCompileManager.compileReport(SysUtil.getClassRootPath()+dto.getJxmlFileName());
			if(dto.getType()==ReportDto.TYPE.EXCEL || dto.getType()==ReportDto.TYPE.HTML)
				prepareReport(jasperReport);
			// JasperPrint is jasper instance in memory, is *.jrprint file
			jasperPrint = JasperFillManager.fillReport( jasperReport, dto.getParameter(), dto.getDatasource());
		}else{
			jasperPrint = JasperFillManager.fillReport(SysUtil.getClassRootPath()+dto.getJasperFileName(), dto.getParameter(), dto.getDatasource());
		}
		return jasperPrint;
	}
	
	private static void setResponse(HttpServletResponse response, ReportDto dto){
		String fileName = "";
		try {
			fileName= java.net.URLEncoder.encode(dto.getOutputFileName(),GlobalName.CODE);
			fileName=fileName.replaceAll("\\+", " ");
		} catch (UnsupportedEncodingException e) {			
		}
		response.setContentType("application/octet-stream; charset="+GlobalName.CODE);
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		if(dto.getType()==ReportDto.TYPE.PDF)
			response.setContentType("application/pdf");
		else if(dto.getType()==ReportDto.TYPE.EXCEL)
			response.setContentType("application/vnd.ms-excel");
		else if(dto.getType()==ReportDto.TYPE.RTF)
			response.setContentType("application/msword");
		else if(dto.getType()==ReportDto.TYPE.HTML)
			response.setContentType("text/html"); 
	} 
	
	private static void prepareReport(JasperReport jasperReport) {  
		try {  
            Field margin = JRBaseReport.class.getDeclaredField("leftMargin");  
            margin.setAccessible(true);
            margin.setInt(jasperReport, 0);
            margin = JRBaseReport.class.getDeclaredField("topMargin");  
            margin.setAccessible(true);  
            margin.setInt(jasperReport, 0);  
            margin = JRBaseReport.class.getDeclaredField("bottomMargin");  
            margin.setAccessible(true);  
            margin.setInt(jasperReport, 0);  
            Field pageHeight = JRBaseReport.class.getDeclaredField("pageHeight");  
            pageHeight.setAccessible(true); 
            // user max height, not divide page
            pageHeight.setInt(jasperReport, 2147483647);  
       } catch (Exception e) {  
    	   LogPrint.info(e.getMessage());
           e.printStackTrace();
       }  
   }  
	
}
