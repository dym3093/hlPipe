package org.hpin.common.widget.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

import org.hpin.common.log.util.HpinLog;

public class DynamicTableTag extends TagSupport
{
	// table元素的id
	protected String id = null;
	
	// 以表格的第startRow行为模板添加新行
	protected Integer startRow = 0;
	
	// 触发添加行事件的元素id，如按钮id
	protected String handler = "";
	
	// 至少保留N行,前N行没有删除操作
	protected Integer saveRow = 1;
  
  protected String templateId = "";
  
  protected String callback = "";

  public int doEndTag()
  {
    StringBuffer sb = new StringBuffer();
    String path = ((HttpServletRequest)this.pageContext.getRequest()).getContextPath();
   // sb.append("<script type=\"text/javascript\" src=\"").append(path).append("/scripts/base/jquery.js\"></script>");
    sb.append("<script type=\"text/javascript\" src=\"").append(path).append("/scripts/dynamicTable.js\"></script>");
    sb.append("<script type=\"text/javascript\">");
    sb.append("$(document).ready(function(){");
    sb.append("new DynamicTable(\"").append(this.id).append("\",").append(this.startRow).append(",").append(this.saveRow).append(",\"").append(this.handler).append("\",\"").append(this.templateId).append("\",\"").append(callback).append("\")");
    sb.append("});");
    sb.append("</script>");
    sb.append("<style type=\"text/css\">#formTable a{text-decoration:none;}</style>");
    try
    {
      this.pageContext.getOut().println(sb);
    } catch (IOException e) {
      HpinLog.error(this, e.getMessage());
    }

    return 6;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getStartRow() {
    return this.startRow;
  }

  public void setStartRow(Integer startRow) {
    this.startRow = startRow;
  }

  public String getHandler() {
    return this.handler;
  }

  public void setHandler(String handler) {
    this.handler = handler;
  }

  public Integer getSaveRow() {
    return this.saveRow;
  }

  public void setSaveRow(Integer saveRow) {
    this.saveRow = saveRow;
  }


public String getCallback() {
	return callback ;
}


public void setCallback(String callback) {
	this.callback = callback ;
}


public String getTemplateId() {
	return templateId ;
}


public void setTemplateId(String templateId) {
	this.templateId = templateId ;
}
  
}