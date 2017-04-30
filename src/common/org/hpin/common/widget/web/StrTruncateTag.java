package org.hpin.common.widget.web;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.util.HtmlUtils;

public class StrTruncateTag extends TagSupport {

	private Integer length = 100;

	private String field = null;

	public int doStartTag() throws JspException {
		field = HtmlUtils.htmlUnescape(field);
		field=field.replaceAll("<[^>]*>","");    
		if (field != null && field.length() > (length + 3)) {
			field = field.substring(0, length) + "...";
		}
		try {
			pageContext.getOut().print(field);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}
