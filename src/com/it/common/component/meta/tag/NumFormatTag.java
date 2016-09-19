package com.it.common.component.meta.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.it.common.component.lang.num.NumFormat;


public class NumFormatTag extends BodyTagSupport {

	private static final long serialVersionUID = -1135541910292607994L;
	
	private String dotNum;
	
	private String type= "fz";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int doAfterBody() throws JspTagException {
		try {
			BodyContent body = getBodyContent();
			String bodyContent = body.getString().trim();
			String content = bodyContent;
			String startFlag = "value=\"", endFlag="\"";
			String startPart = "";
			String endPart = "";
			if(bodyContent.indexOf(startFlag)!=-1){
				String[] contents = bodyContent.split(startFlag);
				startPart = contents[0]+startFlag;
				int endPos = contents[1].indexOf(endFlag);
				content = contents[1].substring(0, endPos);
				endPart = contents[1].substring(endPos);
			}
			JspWriter out = body.getEnclosingWriter();
			String fmContent = content;
			dotNum = dotNum==null||dotNum.trim().length()==0?"0":dotNum.trim();
			try{
				int dotFig = Integer.parseInt(dotNum);
				type = type.toLowerCase();
				switch (type) {
				case "fz":
					fmContent = NumFormat.fmNumZ(content, dotFig);
					break;
				case "z":
					fmContent = NumFormat.fmTextNumZ(content, dotFig);
					break;
				case "f9":
					fmContent = NumFormat.fmNum(content, dotFig);
					break;
				case "9":
					fmContent = NumFormat.fmTextNum(content, dotFig);
					break;
				default:
					break;
				}
			}catch(Exception e){
				out.print(bodyContent);
			}
			out.print(startPart+fmContent+endPart);
		} catch (IOException e) {
			throw new JspTagException(e.toString());
		}
	    return SKIP_BODY;
	}
	
	public int doEndTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public String getDotNum() {
		return dotNum;
	}

	public void setDotNum(String dotNum) {
		this.dotNum = dotNum;
	}

}