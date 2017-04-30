package org.hpin.common.widget.web;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class DoubleFormatTag extends TagSupport {
	private Double d = null;

	private String format = "0.0";

	public int doStartTag() throws JspException {
		String returnStr = "";
		DecimalFormat df = new DecimalFormat(format);
		returnStr = df.format(d);
		if (returnStr.indexOf(".") > 0 && returnStr.endsWith("0")) {
			returnStr = returnStr.substring(0, returnStr.length() - 2);
		}
		try {
			pageContext.getOut().println(returnStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		format = "0.0";
		return EVAL_PAGE;
	}

	public Double getD() {
		return d;
	}

	public void setD(Double d) {
		this.d = d;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}
