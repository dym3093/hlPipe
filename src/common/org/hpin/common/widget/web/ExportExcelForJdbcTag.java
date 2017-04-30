package org.hpin.common.widget.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;


/**
 * 
 * <p>@desc : 通用导出Excel的JDBC版本</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : 2013-8-31 上午10:27:14</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p>
 */
public class ExportExcelForJdbcTag extends TagSupport {

	/**
	 * 查询form的ID
	 */
	protected String pagerFormId ;
	
	/**
	 * 查询Action的全路径
	 */
	protected String actionAllUrl ;
	
	/**
	 * 分页方法
	 */
	protected String pagerMethodName ;
	
	/**
	 * 视图名称
	 */
	protected String viewName ;
	
	/**
	 * 报表最终框架报表标示
	 */
	protected String configId ;
	
	/**
	 * 转取导出
	 */
	protected String field ;
	
	/**
	 * 最终执行导出的url
	 */
	protected String doExecuteUrl = "/system/universalExportExcelForJdbc.action" ;
	
	/**
	 * 数据列表显示的表格id
	 */
	protected String informationTableId ;
	
	/**
	 * 导出按钮的代码
	 */
	protected String exportButtonCode ;
	
	/**
	 * 导出的Excel的名称
	 */
	protected String fileName ;
	
	protected String tempTableName ;
	
	public String getPagerFormId() {
		return pagerFormId ;
	}
	
	public void setPagerFormId(String pagerFormId) {
		this.pagerFormId = pagerFormId ;
	}
	
	public String getActionAllUrl() {
		return actionAllUrl ;
	}
	
	public void setActionAllUrl(String actionAllUrl) {
		this.actionAllUrl = actionAllUrl ;
	}
	
	public String getPagerMethodName() {
		return pagerMethodName ;
	}
	
	public String getDoExecuteUrl() {
		return doExecuteUrl ;
	}
	
	public void setDoExecuteUrl(String doExecuteUrl) {
		this.doExecuteUrl = doExecuteUrl ;
	}

	public void setPagerMethodName(String pagerMethodName) {
		this.pagerMethodName = pagerMethodName ;
	}
	
	public String getInformationTableId() {
		return informationTableId ;
	}
	
	public void setInformationTableId(String informationTableId) {
		this.informationTableId = informationTableId ;
	}
	
	public String getExportButtonCode() {
		return exportButtonCode ;
	}
	
	public void setExportButtonCode(String exportButtonCode) {
		this.exportButtonCode = exportButtonCode ;
	}

	public String getFileName() {
		return fileName ;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName ;
	}

	public String getViewName() {
		return viewName ;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName ;
	}

	public String getTempTableName() {
		return tempTableName ;
	}

	public void setTempTableName(String tempTableName) {
		this.tempTableName = tempTableName ;
	}
	
	public String getConfigId() {
		return configId ;
	}
	
	public void setConfigId(String configId) {
		this.configId = configId ;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	/**
	 * 获取Request对象
	 * @return
	 */
	private HttpServletRequest getRequest(){
		HttpServletRequest request = null ;
		try{
			request = (HttpServletRequest)pageContext.getRequest() ;
		}catch(Exception e){
			e.printStackTrace() ;
		}
		return request ;
	}
	
	/**
	 * 获取Web上下文
	 * @return
	 */
	private String getWebContext(){
		HttpServletRequest request = getRequest() ;
		return request.getContextPath() ;
	}
	
	/**
	 * 创建JavaScript脚本
	 * @return
	 */
	private String createJavaScript(){
		StringBuffer scriptBuffer = new StringBuffer() ;
		scriptBuffer.append("<script type = \"text/javascript\">") ;
		scriptBuffer.append(" function createExportForm(){") ;
		scriptBuffer.append(" 	var _form = $(\"#").append(pagerFormId).append("\",navTab.getCurrentPanel()) ;") ;
		scriptBuffer.append(" 	var oldAction = \"").append(actionAllUrl).append("\" ;")  ;
		scriptBuffer.append(" 	_form.attr(\"action\" , \"").append(getWebContext()).append(doExecuteUrl).append("\") ;") ;
		scriptBuffer.append("	_form = _form[0] ;") ;
		
		/*
		 * 下面的代码是将页面的查询表单最终提交的action进行封装，以便根据查询条件获取相对应的结果集
		 */
		scriptBuffer.append("	var hiddenInput = document.createElement(\"input\") ;") ;
		scriptBuffer.append("	hiddenInput.type = \"hidden\" ;") ;
		scriptBuffer.append("	hiddenInput.name = \"oldAction\" ;") ;
		scriptBuffer.append("	hiddenInput.id = \"oldAction\" ;") ;
		scriptBuffer.append(" 	hiddenInput.value = oldAction ;") ;
		scriptBuffer.append("	_form.appendChild(hiddenInput) ;") ;
		
		scriptBuffer.append("	var hiddenInput1 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("	hiddenInput1.type = \"hidden\" ;") ;
		scriptBuffer.append("	hiddenInput1.name = \"fileName\" ;") ;
		scriptBuffer.append("	hiddenInput1.id = \"fileName\" ;") ;
		scriptBuffer.append("	hiddenInput1.value = \"").append(fileName).append("\" ;") ;
		scriptBuffer.append("	_form.appendChild(hiddenInput1) ;") ;
		
		scriptBuffer.append(" 	var hiddenInput2 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("	hiddenInput2.type = \"hidden\" ;") ;
		scriptBuffer.append("	hiddenInput2.name = \"methodName\" ;") ;
		scriptBuffer.append("	hiddenInput2.id = \"methodName\" ;") ;
		scriptBuffer.append("	hiddenInput2.value = \"").append(pagerMethodName).append("\" ;") ;
		scriptBuffer.append("	_form.appendChild(hiddenInput2) ;") ;
		
		scriptBuffer.append(" 	var hiddenInput3 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("	hiddenInput3.type = \"hidden\" ;") ;
		scriptBuffer.append("	hiddenInput3.name = \"viewName\" ;") ;
		scriptBuffer.append("	hiddenInput3.id = \"viewName\" ;") ;
		scriptBuffer.append("	hiddenInput3.value = \"").append(viewName).append("\" ;") ;
		scriptBuffer.append("	_form.appendChild(hiddenInput3) ;") ;
		
		scriptBuffer.append(" 	var hiddenInput4 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("	hiddenInput4.type = \"hidden\" ;") ;
		scriptBuffer.append("	hiddenInput4.name = \"tempTableName\" ;") ;
		scriptBuffer.append("	hiddenInput4.id = \"tempTableName\" ;") ;
		scriptBuffer.append("	hiddenInput4.value = \"").append(tempTableName).append("\" ;") ;
		scriptBuffer.append("	_form.appendChild(hiddenInput4) ;") ;
		
		scriptBuffer.append(" 	var hiddenInput5 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("	hiddenInput5.type = \"hidden\" ;") ;
		scriptBuffer.append("	hiddenInput5.name = \"configId\" ;") ;
		scriptBuffer.append("	hiddenInput5.id = \"configId\" ;") ;
		scriptBuffer.append("	hiddenInput5.value = \"").append(configId).append("\" ;") ;
		scriptBuffer.append("	_form.appendChild(hiddenInput5) ;") ;
		
		scriptBuffer.append(" 	var hiddenInput6 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("	hiddenInput6.type = \"hidden\" ;") ;
		scriptBuffer.append("	hiddenInput6.name = \"field\" ;") ;
		scriptBuffer.append("	hiddenInput6.id = \"field\" ;") ;
		scriptBuffer.append("	hiddenInput6.value = \"").append(field).append("\" ;") ;
		scriptBuffer.append("	_form.appendChild(hiddenInput6) ;") ;
		
		/*
		 * 下面的代码是根据数据列表的配置，获取最终所需的excel的表头信息
		 */
		scriptBuffer.append("	var infoTable = $(\"#").append(informationTableId).append("\",navTab.getCurrentPanel()) ;") ;
		scriptBuffer.append(" 	var tr = infoTable.children(\"thead\").eq(0).children(\"tr\") ;") ;
		scriptBuffer.append("	var ths = tr.eq(tr.length-1).children(\"th\") ;") ;
		scriptBuffer.append("	var n = 0 ;") ;
		scriptBuffer.append("	for(var i = 0 ; i < ths.length ; i ++){") ;
		scriptBuffer.append("		var th = ths.eq(i) ;") ;
		scriptBuffer.append("		if(th.attr(\"export\") && \"true\" == th.attr(\"export\")){") ;
		scriptBuffer.append("			var hiddenIn1 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("			hiddenIn1.type = \"hidden\" ;") ;
		scriptBuffer.append("			hiddenIn1.name = \"excelJdbcVoList[\" + n + \"].columnChName\" ; ") ;
		scriptBuffer.append("			hiddenIn1.id = \"excelJdbcVoList[\" + n + \"].columnChName\" ;") ;
		scriptBuffer.append("			hiddenIn1.value = th.attr(\"columnChName\") ? th.attr(\"columnChName\") : \"\" ;") ;	
		
		scriptBuffer.append("			var hiddenIn2 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("			hiddenIn2.type = \"hidden\" ;") ;
		scriptBuffer.append("			hiddenIn2.name = \"excelJdbcVoList[\" + n + \"].columnIndex\" ; ") ;
		scriptBuffer.append("			hiddenIn2.id = \"excelJdbcVoList[\" + n + \"].columnIndex\" ;") ;
		scriptBuffer.append("			hiddenIn2.value = th.attr(\"columnIndex\") ? th.attr(\"columnIndex\") : -1 ;") ;
		
		scriptBuffer.append("			var hiddenIn3 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("			hiddenIn3.type = \"hidden\" ;") ;
		scriptBuffer.append("			hiddenIn3.name = \"excelJdbcVoList[\" + n + \"].id2NameClassName\" ; ") ;
		scriptBuffer.append("			hiddenIn3.id = \"excelJdbcVoList[\" + n + \"].id2NameClassName\" ;") ;
		scriptBuffer.append("			hiddenIn3.value = th.attr(\"id2NameClassName\") ? th.attr(\"id2NameClassName\") : \"\" ;") ;
		
		scriptBuffer.append("			var _h1 = document.getElementById(\"excelJdbcVoList[\" + n + \"].columnChName\") ;")  ;
		scriptBuffer.append("			try{") ;
		scriptBuffer.append("				_form.removeChild(_h1) ;") ;
		scriptBuffer.append("			}catch(e){}") ;
		
		scriptBuffer.append("			var _h2 = document.getElementById(\"excelJdbcVoList[\" + n + \"].columnIndex\") ;")  ;
		scriptBuffer.append("			try{") ;
		scriptBuffer.append("				_form.removeChild(_h2) ;") ;
		scriptBuffer.append("			}catch(e){}") ;
		
		scriptBuffer.append("			var _h3 = document.getElementById(\"excelJdbcVoList[\" + n + \"].id2NameClassName\") ;")  ;
		scriptBuffer.append("			try{") ;
		scriptBuffer.append("				_form.removeChild(_h3) ;") ;
		scriptBuffer.append("			}catch(e){}") ;
		
		scriptBuffer.append("			_form.appendChild(hiddenIn1) ;") ;
		scriptBuffer.append("			_form.appendChild(hiddenIn2) ;") ;
		scriptBuffer.append("			_form.appendChild(hiddenIn3) ;") ;
		
		scriptBuffer.append("			n ++ ;") ;
		
		scriptBuffer.append("		}") ;
		scriptBuffer.append("	}") ;
		
		scriptBuffer.append("	_form.submit() ;") ;
		scriptBuffer.append("}") ;
		scriptBuffer.append("</script>") ;
		return scriptBuffer.toString() ;
	}
	
	private String getEscapeStr(String str){
		str = str.replaceAll("&lt;" , "<") ;
		str = str.replaceAll("&gt;" , ">") ;
		return str ;
	}
	
	@Override()
	public int doStartTag() throws JspException{
		StringBuffer htmlBuffer = new StringBuffer() ;
		htmlBuffer.append(createJavaScript()) ;
		if(StringUtils.isNotBlank(exportButtonCode)){
			htmlBuffer.append(getEscapeStr(exportButtonCode)) ;
		}else{
			htmlBuffer.append("<li><a class=\"edit\" href=\"#2\" onclick = \"createExportForm()\"><span>导出Excel</span></a></li>") ;
		}
		try{
			pageContext.getOut().println(htmlBuffer.toString()) ;
		}catch (Exception e) {
			e.printStackTrace() ;
		}
		return EVAL_PAGE ;
	}
	
	@Override
	public int doEndTag() throws JspException {
		destroyAllElement() ;
		return EVAL_PAGE ;
	}
	
	/**
	 * 销毁所有的的元素和值，加速GC
	 */
	private void destroyAllElement(){
		pagerFormId = null ;
		actionAllUrl = null ;
		pagerMethodName = null ;
		informationTableId = null ;
		exportButtonCode = null ;
		viewName = null ;
		tempTableName = null ;
		configId = null ;
	}
	
}

