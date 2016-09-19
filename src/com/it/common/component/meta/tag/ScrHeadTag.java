package com.it.common.component.meta.tag;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.it.common.component.lang.sys.SysUtil;
import com.it.common.component.log.LogPrint;
import com.it.common.share.GlobalName;

/**
 * print html head to screen
 * @author liu
 * */
public class ScrHeadTag extends TagSupport {
	
	private static final long serialVersionUID = 8808011165589568289L;
	
	static ResourceBundle resources = ResourceBundle.getBundle("com/it/context/base/jsCssUrl");
	
	private String title = "";
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	protected String getSrc(String webRoot){
		StringBuilder src = new StringBuilder();
		src.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset="+GlobalName.CODE+"\" />");
		src.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		
		FileInputStream in = null;
		BufferedReader reader = null;
		try {
			in = new FileInputStream(SysUtil.getClassRootPath()+"com/it/context/base/jsCssUrl.properties");
			reader = new BufferedReader(new InputStreamReader(in, GlobalName.CODE));
			String line = "";
			while((line=reader.readLine())!=null){
				if(line.indexOf("=")!=-1){
					src.append(getCssJsUrl(webRoot, line.split("=")[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return src.toString();
	}
	
	protected String getRoot(String webRoot){
		return webRoot;
	}
	
	private StringBuilder getCssJsUrl(String webRoot, String value){
		StringBuilder url = new StringBuilder("");
		if(value!=null && value.endsWith(".css")){
			url.append("<link href=\"").append(getRoot(webRoot)).append("/").append(value).append("\" rel=\"stylesheet\">");
		}
		if(value!=null && value.endsWith(".js")){
			url.append("<script src='").append(getRoot(webRoot)).append("/").append(value).append("' type=\"text/javascript\"></script>");
		}
		return url;
	}
	
	protected String getScript(){
		return "";
	}

	public int doStartTag(){
		JspWriter out = this.pageContext.getOut();
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		String webRoot = request.getContextPath();
		try {
			out.print("<head>" +getSrc(webRoot)+
					  "<title>"+getTitle()+"</title>"+getScript());
		} catch (IOException e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		}
		return TagSupport.EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag()throws JspException{
		JspWriter out = this.pageContext.getOut();
		try {
			out.print("</head>");
		} catch (IOException e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		}
		return TagSupport.EVAL_PAGE;
	}


}
