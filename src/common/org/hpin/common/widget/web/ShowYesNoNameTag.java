package org.hpin.common.widget.web;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.hpin.common.util.StrUtils;

/**
 * 显示是否名称
 * 
 * @author thinkpad
 * @data Feb 22, 2010
 */
public class ShowYesNoNameTag extends TagSupport {

	private String value = null;// 值

	private String aliasYes = null;// yes别名

	private String aliasNo = null;// no别名

	public int doStartTag() throws JspException {
		String name = null;
		if ("1".equals(value)) {
			if (StrUtils.isNotNullOrBlank(aliasYes)) {
				name = aliasYes;
			} else {
				name = "是";
			}
		} else {
			if (StrUtils.isNotNullOrBlank(aliasNo)) {
				name = aliasNo;
			} else {
				name = "否";
			}
		}
		try {
			pageContext.getOut().print(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

}
