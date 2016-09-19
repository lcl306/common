package com.it.common.component.meta.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.it.common.component.check.group.ErrListDto;
import com.it.common.component.log.LogPrint;
import com.it.common.share.GlobalName;

/**
 * print log and error
 * @author liu
 * */
public class ScrBodyTag extends TagSupport{

	private static final long serialVersionUID = -1261453298776008923L;
	
	private String errDiv;
	
	private String onload;
	
	private String showEmpty;
	
	private String docFocus;
	
	private String firstFocus;

	public String getDocFocus() {
		return docFocus;
	}

	public void setDocFocus(String docFocus) {
		this.docFocus = docFocus;
	}

	public String getFirstFocus() {
		return firstFocus;
	}

	public void setFirstFocus(String firstFocus) {
		this.firstFocus = firstFocus;
	}

	public String getErrDiv() {
		return errDiv;
	}

	public void setErrDiv(String errDiv) {
		this.errDiv = errDiv;
	}

	public String getOnload() {
		return onload;
	}

	public void setOnload(String onload) {
		this.onload = onload;
	}

	public String getShowEmpty() {
		return showEmpty;
	}

	public void setShowEmpty(String showEmpty) {
		this.showEmpty = showEmpty;
	}

	public int doStartTag(){
		printBodyStart();
		if(showEmpty!=null && showEmpty.trim().length()>0){
			this.pageContext.setAttribute("app_showEmpty", showEmpty);
		}
		return TagSupport.EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag()throws JspException{
		if("true".equals(docFocus) || ((docFocus==null || docFocus.equals("")) && "true".equals(GlobalName.DOC_FOCUS))){
			addFocus();
		}
		printErr();
		printBodyEnd();
		return TagSupport.EVAL_PAGE;
	}
	
	private void printBodyStart(){
		JspWriter out = this.pageContext.getOut();
		try{
			String onloadStr = "";
			if(onload!=null && onload.trim().length()>0){
				onloadStr = "onload=\""+onload+"\"";
			}
 			out.print("<body " +onloadStr +">");
 		}catch(IOException e){
 			LogPrint.error(e.getMessage());
 			e.printStackTrace();
 		}
	}
	
	private void printBodyEnd(){
		JspWriter out = this.pageContext.getOut();
		try{
 			out.print("</body>");
 		}catch(IOException e){
 			LogPrint.error(e.getMessage());
 			e.printStackTrace();
 		}
	}
	
	private void addFocus(){
		JspWriter out = this.pageContext.getOut();
		StringBuilder rtn = new StringBuilder("<script language=\"JavaScript\">$.docFocus();");
		if(firstFocus!=null && firstFocus.trim().length()>0){
			rtn.append("$(\"#"+firstFocus+"\").focus();");
		}else{
			rtn.append("$(function(){firstFocus();});");
		}
		rtn.append("</script>");
		try{
 			out.print(rtn.toString());
 		}catch(IOException e){
 			LogPrint.error(e.getMessage());
 			e.printStackTrace();
 		}
	}
	
	private void printErr(){
		HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
		ErrListDto errs = (ErrListDto)request.getAttribute(GlobalName.ERRLISTDTO_NAME);
		JspWriter out = this.pageContext.getOut();
		/*StringBuilder rtn = new StringBuilder("<script language=\"JavaScript\">$.errContext=$(\"#"+errDiv+"\").errMsg();");
		if(errs!=null){
			rtn.append(" $.errContext.printErr('"+errs.errs2str("<br/>")+"');");
		}*/
		StringBuilder rtn = new StringBuilder("<script language=\"JavaScript\">");
		if(errs!=null){
			rtn.append("$(function(){alertErr('"+errs.errs2str("<br/>")+ "')}); ");
		}
		rtn.append("</script>");
		try{
 			out.print(rtn.toString());
 		}catch(IOException e){
 			LogPrint.error(e.getMessage());
 			e.printStackTrace();
 		}
	}
	
	

}
