package org.hpin.common.widget.web;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.util.HtmlUtils;

/**
 * 详细页面
 * 
 * @author thinkpad
 * 
 */
public class DetailTag extends TagSupport {

	private String url = null;

	private String value = null;

	private Integer length = null;

	private String width = "900";

	private String height = "450";

	private String title = null;

	private boolean isParent = false;

	public int doStartTag() throws JspException {
		if (null != value) {
			value = HtmlUtils.htmlUnescape(value);
			value = value.replaceAll("<[^>]*>", "");
		}
		if (length != null && value.length() > (length + 3)) {
			value = value.substring(0, length) + "...";
			length = null;
		}

		try {
			String html = null;
			if (isParent) {
				html = "<nobr><a href=\"javascript:parent.openDetailWin('"
						+ url + "'," + width + "," + height + ",'" + title
						+ "')\">" + value + "</a></nobr>";
			} else {
//				html = "<nobr><a href=\"javascript:openDetailWin('" + url
//						+ "'," + width + "," + height + ",'" + title + "')\">"
//						+ value + "</a></nobr>";
				html = "<nobr><a href=\"" + url + "\" target=\"_blank\">"
						+ value + "</a></nobr>";
			}
			pageContext.getOut().println(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
