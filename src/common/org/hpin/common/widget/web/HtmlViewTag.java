package org.hpin.common.widget.web;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.util.HtmlUtils;

public class HtmlViewTag extends TagSupport {

	private String value = null;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int doStartTag() throws JspException {
		value = value.replaceAll("'", "&quot");
		value = value.replaceAll("\n", "</br>");
		value = value.replaceAll("\r", "</br>");
		if (null != value) {
			value = HtmlUtils.htmlUnescape(value);
		}
		try {
			pageContext.getOut().print(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

}
