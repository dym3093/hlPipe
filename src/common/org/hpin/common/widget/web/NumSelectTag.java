package org.hpin.common.widget.web;

import javax.servlet.jsp.JspException;

/**
 * 数字Select
 * 
 * @author thinkpad
 * 
 */
public class NumSelectTag extends BaseSelectTag {
	private Integer start = null;

	private Integer end = null;

	private Integer increment = null;

	private String prefix = null;

	public int doStartTag() throws JspException {
		html = new StringBuffer();
		html.append("<select ");
		super.loadCommonPropertys(html);
		html.append(">");
		if (hasDefault) {
			html.append("<option></option>");
		}
		start = start == null ? 1 : start;
		end = end == null ? 10 : end;
		increment = increment == null ? 1 : increment;
		prefix = prefix == null ? "" : prefix;
		for (int i = start; i <= end; i = i + increment) {
			html
					.append("<option value='" + i + "'>" + i + prefix
							+ "</option>");
		}
		html.append("</select>");
		try {
			pageContext.getOut().println(html.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.initProperty();
		return EVAL_PAGE;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public Integer getIncrement() {
		return increment;
	}

	public void setIncrement(Integer increment) {
		this.increment = increment;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
