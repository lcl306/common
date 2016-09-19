package com.it.common.component.pdf.itext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.it.common.component.log.LogPrint;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author liu
 * */
public abstract class PdfDoc {
	
	protected abstract void buildDoc()throws IOException, DocumentException;
	
	protected abstract Document createDocument();
	
	private Document document;
	
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
	OutputStream out;
	
	private PdfWriter writer;
		
	protected boolean autoPrint = true;
		
	private String fileName;

	boolean isAutoPrint() {
		return autoPrint;
	}

	/**
	 * set auto print by javascript
	 * */
	public void setAutoPrint(boolean autoPrint) {
		this.autoPrint = autoPrint;
	}
	
	public String getFileName() {
		return fileName;
	}

	/**
	 * if pdf to file, set file name
	 * */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * get pdf document
	 * */
	public Document getDocument() {
		if(document==null){
			Document doc = this.createDocument();
			this.document = doc;
		}
		return document;
	}
	
	protected PdfWriter getWriter() throws DocumentException{
		if(writer==null){
			writer = PdfWriter.getInstance(document, buffer);	
		}
		return writer;
	}
	
	void printJs(){
		if(isAutoPrint()){
			PdfAction jAction = PdfAction.javaScript("this.print(true);\r",writer);
			writer.addJavaScript(jAction);
		}
	}
	
	void build()throws IOException, DocumentException{
		try{
			getDocument();
			getWriter();
			document.open();
			printJs();
			buildDoc();
		}catch(DocumentException e){
			e.printStackTrace();
			LogPrint.error(e.getMessage());
		}finally{
			document.close();
		}
	}
	
	protected void flush() throws IOException{
		writer.flush();  
		buffer.writeTo(out);  
		buffer.reset();  
		out.flush();
	}
	
	void close()throws IOException{
		try{
			flush();
		}finally{
			buffer.close();
		}
	}	

}
