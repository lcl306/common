package com.it.common.component.page;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;


/**
 *
 * */
/**
 * <p>Title: </p>
 * <p>Description: this class is a java program of the divTag that used in jsp page, it can be used to view result by divided page </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: sunrex</p>
 * @author liuchangliang
 * @version 3.0
 */
public class PageTag extends TagSupport {

	private static final long serialVersionUID = 3768163798286811771L;

	private String action;
	
	private String formIndex = "0";
	
	//private String pageRows = PageUtil.DEFAULT_ROW+"";
    
	private String unit = "条";
	
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	/*public String getPageRows() {
		return pageRows;
	}

	public void setPageRows(String pageRows) {
		this.pageRows = pageRows;
	}*/

	public String getFormIndex() {
		return formIndex;
	}

	public void setFormIndex(String formIndex) {
		this.formIndex = formIndex;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * when the tag is end, this method is use, it will give you dividing page function
	 * @return continue view the page document
	 * */
	public int doEndTag()throws JspException{
		JspWriter out = this.pageContext.getOut();
		
		PageDto page=PageUtil.get();
		if(page==null){
			PageUtil.prepare(PageUtil.DEFAULT_PAGENUMER, PageUtil.DEFAULT_ROW);
			page = PageUtil.get();
			page.setTotalRow(1);
			page.flush();
		}
		int pageRows = page.getPageRow();
	    String flag = this.getAction().indexOf("?")!=-1?"&":"?";
    	String append = this.getAction() +flag +"pageRows=" +pageRows +"&currPage=";
    	try{
    		out.print("<input type=\"hidden\" name=\"pageRows\" value=\"" +pageRows +"\" />");
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	
	    if(page.getTotalRow()>page.getPageRow()){
		try{
			out.print("<input type=\"hidden\" id=\"inPage\" value=\"" +page.getPageNumber() +"\" />");
			out.print("<input type=\"hidden\" id=\"lastPage\" value=\"" +page.getPageNumber() +"\" />");
			out.print("<script language=javascript>"
  					+" function disableKeyFunc(){if(event.keyCode==13){ event.keyCode=0;}}"
  					+" function isInteger(str){ if(str==parseInt(str, 10)){return true;}else{return false;}}"
  					+" function printPageErr(){ $('font[name=pageError]').html('页码指定有误！');}"
  					+" function setLastPage(lastPage){" +
  							"var inputPages = document.getElementsByName('inputPage');" +
  							"document.getElementById('inPage').value=lastPage;" +
  							"for(var i=0; i<inputPages.length; i++){" +
  								"inputPages[i].value=lastPage;" +
  							"}"+
  					   "}"
  					+" function goPage(totalPage){"
  							+"var lastPage = document.getElementById('lastPage').value;"
  							+"var inputPage = document.getElementById('inPage').value;"
  							+"if(isInteger(inputPage)){ " +
  								"if(inputPage>0 && inputPage<=totalPage){" +
  									"toNextPage();" +
  								"}else{printPageErr();" +
  									"setLastPage(lastPage);" +
  								"}" +
  							"}else{printPageErr();" +
  								" setLastPage(lastPage);" +
  							"}" +
  					   "}"
  					+"function onPageBlur(pagevalue){document.getElementById('inPage').value=pagevalue;}"
  					+" function toNextPage(){ var page=document.getElementById('inPage').value; var f = $('form').get(" +formIndex +"); if(f){f.action='" +append +"'+page; f.submit();}else{document.location.href='"+append+ "'+page;} }"
   					+" function toUpPage(){  var f = $('form').get(" +formIndex +");  if(f){f.action='"+append +(page.getPageNumber()-1) + "';  f.submit();}else{document.location.href='"+append +(page.getPageNumber()-1) +"';}}"
   					+" function toDownPage(){ var f = $('form').get(" +formIndex +"); if(f){f.action='"+append +(page.getPageNumber()+1) + "';  f.submit();}else{document.location.href='"+append +(page.getPageNumber()+1) +"';}}"
  			 +"</script>");
			
			out.print("<div align=\"right\" style=\"margin-top:10px\">");
			out.print("<span><font name='pageError' color='red'></font></span>");
			out.print("" + (page.getStartRow()+1) +"－" + page.getEndRow() + this.getUnit() + " &nbsp;( 共" + page.getTotalRow() + this.getUnit()+" ) &nbsp;");
			
			String pre = page.getPageNumber() == 1 ?"上页":"<a href=\"javascript:toUpPage();\">上页</a>";
			out.print(pre+"&nbsp;&nbsp;");

			String aft = page.getPageNumber() >= page.getTotalPage() ?"下页":"<a href=\"javascript:toDownPage();\">下页</a>";;
			out.print(aft+"&nbsp;&nbsp;&nbsp;");
			
			out.print("<input type=\"text\" style=\"height:24px;width:30px;\" name=\"inputPage\" value=" + page.getPageNumber()
					+" onKeyUp=\"disableKeyFunc();\" onKeyDown=\"disableKeyFunc();\" onKeyPress=\"disableKeyFunc();\" onBlur=\"onPageBlur(this.value);\"> ");
		  	out.print("&nbsp;<a href=\"javascript:goPage(" +page.getTotalPage() +");\">前往</a>");
		  	out.print("</div>");
		  	
		}catch(IOException e){
			e.printStackTrace();
		}
	   }
		return TagSupport.EVAL_PAGE;
	}
	
	
}
