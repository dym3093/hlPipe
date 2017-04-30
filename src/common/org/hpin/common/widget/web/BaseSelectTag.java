package org.hpin.common.widget.web;

/**
 * 页面元素标签基础类
 * 
 * @author thinkpad
 * @data 2009-7-24
 */
public class BaseSelectTag extends HtmlTag {

	private static final long serialVersionUID = -6500572951010383123L;

	/**
	 * 是否多选
	 */
	protected String multiple = null;

	/**
	 * 是否有个默认的选择框
	 */
	protected boolean hasDefault = false;

	protected String size = null;
	
	protected String Class = null ;

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	/**
	 * 组装公共属性集合
	 * 
	 * @param sb
	 */
	protected void loadCommonPropertys(StringBuffer sb) {
		super.loadCommonPropertys(sb);
		loadProperty(sb, "multiple", multiple);
		loadProperty(sb, "size", size);

	}

	/**
	 * 还原所有属性
	 */
	protected void initProperty() {
		super.initProperty();
		multiple = null;
		hasDefault = false;
	}

	public boolean getHasDefault() {
		return hasDefault;
	}

	public void setHasDefault(boolean hasDefault) {
		this.hasDefault = hasDefault;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
