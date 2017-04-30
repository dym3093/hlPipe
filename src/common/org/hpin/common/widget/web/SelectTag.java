package org.hpin.common.widget.web;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.hpin.common.util.StrUtils;

/**
 * 下拉框Tag
 * 
 * @author thinkpad
 * @Apr 1, 2010
 */
public class SelectTag extends BaseSelectTag {

	/**
	 * 集合
	 */
	private List items = null;
	
	/**
	 * sel是否显示title
	 */
	private Boolean isShowSelTitle = false ;
	
	/**
	 * option是否显示title
	 */
	private Boolean isShowOptionTitle = false ;

	/**
	 * ID显示名称
	 */
	private String idTag = null;

	/**
	 * 内容显示名称
	 */
	private String nameTag = null;
	
	/**
	 * 是否只在列表中显示选中项，单选有效
	 */
	private Boolean isShowOneSelOnly = false ;

	public int doStartTag() throws JspException {
		if (StrUtils.isNullOrBlank(idTag)) {
			idTag = "id";
		}
		if (StrUtils.isNullOrBlank(nameTag)) {
			nameTag = "name";
		}
		html = new StringBuffer();
		Method method = null;
		Object object = null;
		String idValue = null;
		String nameValue = null;
		html.append("<select ");
		super.loadCommonPropertys(html);
		html.append(">");
		if (hasDefault) {
			html.append("<option></option>");
		}
		if (null != items) {
			for (int i = 0; i < items.size(); i++) {
				String isSelect = "";
				object = items.get(i);
				// 获取select ID值
				try {
					method = object.getClass().getMethod(
							"get" + idTag.substring(0, 1).toUpperCase()
									+ idTag.substring(1));
					idValue = method.invoke(object) != null ? method.invoke(
							object).toString() : null;
					method = object.getClass().getMethod(
							"get" + nameTag.substring(0, 1).toUpperCase()
									+ nameTag.substring(1));
					nameValue = method.invoke(object) != null ? method.invoke(
							object).toString() : null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 判断是否默认选中
				if (value != null) {
					if (null != multiple && multiple.equals("multiple")) {
						String[] valueArray = value.split(",");
						for (int j = 0; j < valueArray.length; j++) {
							if (valueArray[j].equals(idValue)) {
								isSelect = "selected";
							}
						}
					} else {
						if (value.toString().equals(idValue.toString())) {
							isSelect = "selected";
						}
					}
				}
				if(isShowOneSelOnly){
					//是否在下拉框中只显示选中项
					if(!value.toString().equals(idValue.toString())) continue ;
					html.append("<option value=\"" + idValue + "\" ");
					if (StrUtils.isNotNullOrBlank(isSelect)) {
						html.append(isSelect);
					}
					if(isShowOptionTitle){
						html.append(" title = \"").append(nameValue).append("\" ") ;
					}
					html.append(">");
					html.append(nameValue);
					html.append("</option>");
				}else{
					html.append("<option value=\"" + idValue + "\" ");
					if (StrUtils.isNotNullOrBlank(isSelect)) {
						html.append(isSelect);
					}
					if(isShowOptionTitle){
						html.append(" title = \"").append(nameValue).append("\" ") ;
					}
					html.append(">");
					html.append(nameValue);
					html.append("</option>");
				}
			}
		}
		html.append("</select>");
		try {
			pageContext.getOut().println(html.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.initProperty();
		items = null;
		idTag = null;
		nameTag = null;
		return EVAL_PAGE;
	}

	public List getItems() {
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}

	public String getIdTag() {
		return idTag;
	}

	public void setIdTag(String idTag) {
		this.idTag = idTag;
	}

	public String getNameTag() {
		return nameTag;
	}

	public void setNameTag(String nameTag) {
		this.nameTag = nameTag;
	}

	public Boolean getIsShowSelTitle() {
		return isShowSelTitle ;
	}

	public void setIsShowSelTitle(Boolean isShowSelTitle) {
		this.isShowSelTitle = isShowSelTitle ;
	}

	public Boolean getIsShowOptionTitle() {
		return isShowOptionTitle ;
	}

	public void setIsShowOptionTitle(Boolean isShowOptionTitle) {
		this.isShowOptionTitle = isShowOptionTitle ;
	}

	
	public Boolean getIsShowOneSelOnly() {
		return isShowOneSelOnly ;
	}

	
	public void setIsShowOneSelOnly(Boolean isShowOneSelOnly) {
		this.isShowOneSelOnly = isShowOneSelOnly ;
	}

}
