package org.hpin.common.widget.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * @author thinkpad
 * 
 */
public class LocationTag extends TagSupport {
	private String value = null;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		StringBuffer html = new StringBuffer();
		html.append("<div class='navigation-img'>");
		html
				.append("<img src='"
						+ request.getContextPath()
						+ "/images/navigation.gif' width='16' height='16' align='left' />");
		html.append("</div>");
		html.append("<div class='navigation-font'>");
		html.append("<b>当前位置:&nbsp;</b>");
		String[] locations = value.split("-");
		for (int i = 0; i < locations.length; i++) {
			if (i == locations.length - 1) {
				html.append("<span class='navigation-end'>" + locations[i]
						+ "</span>");
			} else {
				html.append("<span class='navigation-front'>" + locations[i]
						+ "</span>-&gt;");
			}
		}
		html.append("</div>");
		try {
			pageContext.getOut().println(html.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
}
