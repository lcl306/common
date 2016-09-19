package com.it.common.component.meta.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.it.common.component.log.LogPrint;

public class ScrHtmlTag extends TagSupport{

	private static final long serialVersionUID = 3084126810518475212L;
	
	private String html5;
	
	private String html4;
	
	public String getHtml5() {
		return html5;
	}

	public void setHtml5(String html5) {
		this.html5 = html5;
	}

	public String getHtml4() {
		return html4;
	}

	public void setHtml4(String html4) {
		this.html4 = html4;
	}

	public int doStartTag(){
		JspWriter out = this.pageContext.getOut();
		try {
			if(html4==null || html4.trim().length()==0){
				if(html5==null || html5.trim().length()==0){
					out.print("" +
						"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
						"<html xmlns=\"http://www.w3.org/1999/xhtml\">");
				}else{
					out.print("<!DOCTYPE HTML><html>");
				}
			}else{
				out.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"><html>");
			}
		} catch (IOException e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		}
		return TagSupport.EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag()throws JspException{
		JspWriter out = this.pageContext.getOut();
		try {
			out.print("</html>");
		} catch (IOException e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		}
		return TagSupport.EVAL_PAGE;
	}
	
	

}
