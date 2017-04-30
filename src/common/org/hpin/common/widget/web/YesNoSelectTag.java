package org.hpin.common.widget.web;

import javax.servlet.jsp.JspException;

import org.hpin.common.util.StrUtils;

/**
 * 是否下拉框
 * 
 * @author thinkpad
 * @data 2009-7-24
 */
@SuppressWarnings("serial")
public class YesNoSelectTag extends BaseSelectTag {
	private String aliasYes = null;
	private String aliasNo = null;

	private String defaultValue = null;

	public int doStartTag() throws JspException {
		html = new StringBuffer();
		// System.out.println(value+"aaaaaaaa"+defaultValue);
		if (StrUtils.isNullOrBlank(value)
				&& StrUtils.isNotNullOrBlank(defaultValue)) {
			value = defaultValue;
		}
		html.append("<select ");
		super.loadCommonPropertys(html);
		html.append(">");
		if (hasDefault) {
			html.append("<option></option>");
		}

		for (int i = 1; i >= 0; i--) {
			String isSelect = "";
			if (value != null && value.equals(new Integer(i).toString())) {
				isSelect = "selected";
			}
			html.append("<option value=\"" + i + "\" " + isSelect + ">");
			if (i == 1) {
				if (StrUtils.isNotNullOrBlank(aliasYes)) {
					html.append(aliasYes);
				} else {
					html.append("是");
				}
			} else {
				if (StrUtils.isNotNullOrBlank(aliasNo)) {
					html.append(aliasNo);
				} else {
					html.append("否");
				}
			}
			html.append("</option>");
		}
		html.append("</select>");
		try {
			pageContext.getOut().println(html.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.initProperty();
		aliasYes = null;
		aliasNo = null;
		return EVAL_PAGE;
	}

	public String getAliasYes() {
		return aliasYes;
	}

	public void setAliasYes(String aliasYes) {
		this.aliasYes = aliasYes;
	}

	public String getAliasNo() {
		return aliasNo;
	}

	public void setAliasNo(String aliasNo) {
		this.aliasNo = aliasNo;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
