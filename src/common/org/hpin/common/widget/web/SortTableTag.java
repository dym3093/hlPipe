/*
 * Created on 2008-5-19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.hpin.common.widget.web;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hpin.common.log.util.HpinLog;

/**
 * @author wangzhiyong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SortTableTag  extends TagSupport {


	// table元素的id
	protected String id = "";
	
	// 可以排序的列
	protected String sortProperty = "{}";
	
	// 系列默认第一次排序类型
	protected String defaultSortType = "{}";
	
	public int doEndTag() {
		JSONObject json = JSONObject.fromObject(sortProperty);
		String property = "",sortType = "";
		Set<Map.Entry<String, String>> elems = json.entrySet();
		for(Map.Entry<String, String> elem :elems){
			property = elem.getValue();
			sortType = (String)pageContext.getRequest().getAttribute(property);
			if(StringUtils.isNotBlank(sortType)){
				break;
			}
		}
		
		if(sortType == null){
			sortType = "";
		}
		StringBuffer sb = new StringBuffer();
		String path = ((HttpServletRequest)pageContext.getRequest()).getContextPath();
		sb.append("<script type=\"text/javascript\" src=\"").append(path).append("/scripts/dynamicTable.js\"></script>");
		sb.append("<script type=\"text/javascript\">");
		sb.append("$(document).ready(function(){");
		sb.append("var options = ").append(sortProperty).append(";");
		sb.append("var defaultSort = ").append(defaultSortType).append(";");
		sb.append("initSortTable('").append(id).append("',options,defaultSort,'").append(property).append("','").append(sortType).append("');");
		sb.append("});");
		sb.append("</script>");
		
		try {
			pageContext.getOut().println(sb);
		} catch (IOException e) {
			HpinLog.error(this, e.getMessage());
		} 
		
		return EVAL_PAGE;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSortProperty() {
		return sortProperty;
	}

	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}

	public String getDefaultSortType() {
		return defaultSortType;
	}

	public void setDefaultSortType(String defaultSortType) {
		this.defaultSortType = defaultSortType;
	}

	
}
