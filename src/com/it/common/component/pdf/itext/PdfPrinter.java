package com.it.common.component.pdf.itext;

import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.it.common.share.GlobalName;

/**
 * @author liu
 * */
public abstract class PdfPrinter {
	
	/**
	 * print pdf to response
	 * */
	public static void printToResponse(HttpServletResponse response, PdfDoc pdfDoc){
		if(pdfDoc.getFileName()!=null && pdfDoc.getFileName().trim().length()>0){
			response.setContentType("application/octet-stream; charset="+GlobalName.CODE);
			response.setHeader("Content-Disposition", "attachment;filename=" +pdfDoc.getFileName());
		}else{
			response.setContentType("application/pdf");
		}
		ServletOutputStream out = null;
		try{
			out = response.getOutputStream();
			pdfDoc.out = out;
			pdfDoc.build();
			// response.setContentLength(buffer.size());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				pdfDoc.close();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * print pdf to file
	 * */
	public static void printToFile(String fileName, PdfDoc pdfDoc){
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(fileName);
			printToOutputStream(fos, pdfDoc);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * print pdf to buffer
	 * */
	public static void printToOutputStream(OutputStream out, PdfDoc pdfDoc){
		try{
			pdfDoc.out = out;
			pdfDoc.build();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				pdfDoc.close();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
