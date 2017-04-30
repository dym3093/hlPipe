package org.hpin.common.widget.web;

import javax.servlet.jsp.tagext.TagSupport;

import org.hpin.common.util.StrUtils;

public class HtmlTag extends TagSupport {
	private static final long serialVersionUID = -3681759781098812779L;

	protected String id = null;

	protected String name = null;

	protected String title = null;

	protected String value = null;

	protected String lang = null;

	protected String style = null;

	/**
	 * onchange函数
	 */
	protected String onchange = null;

	/**
	 * onclick函数
	 */
	protected String onclick = null;
	
	/**
	 * ommouseover事件
	 */
	protected String onmouseover = null ;

	/**
	 * 是否启用
	 */
	protected String disabled = null ;
	
	protected StringBuffer html = null;

	/**
	 * 组装公共属性集合
	 * 
	 * @param sb
	 */
	protected void loadCommonPropertys(StringBuffer sb) {
		if (null != id) {
			loadProperty(sb, "id", id);
		} else if (null != name) {
			loadProperty(sb, "id", name);
		}
		loadProperty(sb, "name", name);
		loadProperty(sb, "lang", lang);
		loadProperty(sb, "style", style);
		loadProperty(sb, "title", title);
		loadProperty(sb, "onchange", onchange);
		loadProperty(sb, "onclick", onclick);
		loadProperty(sb , "disabled" , disabled) ;
		loadProperty(sb , "onmouseover" , onmouseover) ;
	}

	/**
	 * 组装属性
	 * 
	 * @param sb
	 */
	protected void loadProperty(StringBuffer sb, String name, Object value) {
		if (StrUtils.isNotNullOrBlank(value)) {
			sb.append(" " + name + "=\"" + value + "\" ");
		}
	}

	/**
	 * 还原所有属性
	 */
	protected void initProperty() {
		id = null;
		name = null;
		title = null;
		value = null;
		lang = null;
		style = null;
		html = null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public StringBuffer getHtml() {
		return html;
	}

	public void setHtml(StringBuffer html) {
		this.html = html;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	
	public String getOnmouseover() {
		return onmouseover ;
	}

	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover ;
	}
}
