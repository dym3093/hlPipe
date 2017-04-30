package org.hpin.common.widget.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;


/**
 * <p>@desc : Excel通用导出标签</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : 2013-6-10 下午12:37:26</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class ExportExcelTag extends TagSupport {

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
	 * 最终执行导出的url
	 */
	protected String doExecuteUrl = "/system/universalExportExcel.action" ;
	
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
	
	/**
	 * 业务流程专用
	 */
	protected String cycleName ;
	
	/**
	 * 业务流程专用
	 */
	protected String beanName ;
	
	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

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
		scriptBuffer.append("	hiddenInput3.name = \"cycleName\" ;") ;
		scriptBuffer.append("	hiddenInput3.id = \"cycleName\" ;") ;
		scriptBuffer.append("	hiddenInput3.value = \"").append(cycleName).append("\" ;") ;
		scriptBuffer.append("	_form.appendChild(hiddenInput3) ;") ;   
		
		scriptBuffer.append(" 	var hiddenInput4 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("	hiddenInput4.type = \"hidden\" ;") ;
		scriptBuffer.append("	hiddenInput4.name = \"beanName\" ;") ;
		scriptBuffer.append("	hiddenInput4.id = \"beanName\" ;") ;
		scriptBuffer.append("	hiddenInput4.value = \"").append(beanName).append("\" ;") ;
		scriptBuffer.append("	_form.appendChild(hiddenInput4) ;") ;
		
		/*
		 * 下面的代码是根据数据列表的配置，获取最终所需的excel的表头信息
		 */
		scriptBuffer.append("	var infoTable = $(\"#").append(informationTableId).append("\",navTab.getCurrentPanel()) ;") ;
		scriptBuffer.append(" 	var tr = infoTable.children(\"thead\").eq(0).children(\"tr\") ;") ;
		scriptBuffer.append("	var ths = tr.eq(0).children(\"th\") ;") ;
		scriptBuffer.append("	var n = 0 ;") ;
		scriptBuffer.append("	for(var i = 0 ; i < ths.length ; i ++){") ;
		scriptBuffer.append("		var th = ths.eq(i) ;") ;
		scriptBuffer.append("		if(th.attr(\"export\") && \"true\" == th.attr(\"export\")){") ;
		scriptBuffer.append("			var hiddenIn1 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("			hiddenIn1.type = \"hidden\" ;") ;
		scriptBuffer.append("			hiddenIn1.name = \"excelVoList[\" + n + \"].columnEnName\" ; ") ;
		scriptBuffer.append("			hiddenIn1.id = \"excelVoList[\" + n + \"].columnEnName\" ;") ;
		scriptBuffer.append("			hiddenIn1.value = th.attr(\"columnEnName\") ;") ;	
		
		scriptBuffer.append("			var hiddenIn2 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("			hiddenIn2.type = \"hidden\" ;") ;
		scriptBuffer.append("			hiddenIn2.name = \"excelVoList[\" + n + \"].columnChName\" ; ") ;
		scriptBuffer.append("			hiddenIn2.id = \"excelVoList[\" + n + \"].columnChName\" ;") ;
		scriptBuffer.append("			hiddenIn2.value = th.attr(\"columnChName\") ;") ;
		
		scriptBuffer.append("			var hiddenIn3 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("			hiddenIn3.type = \"hidden\" ;") ;
		scriptBuffer.append("			hiddenIn3.name = \"excelVoList[\" + n + \"].id2NameClassName\" ; ") ;
		scriptBuffer.append("			hiddenIn3.id = \"excelVoList[\" + n + \"].id2NameClassName\" ;") ;
		scriptBuffer.append("			hiddenIn3.value = th.attr(\"id2NameBeanId\") ;") ;
		
		scriptBuffer.append("			var hiddenIn4 = document.createElement(\"input\") ;") ;
		scriptBuffer.append("			hiddenIn4.type = \"hidden\" ;") ;
		scriptBuffer.append("			hiddenIn4.name = \"excelVoList[\" + n + \"].id2FieldClassName\" ; ") ;
		scriptBuffer.append("			hiddenIn4.id = \"excelVoList[\" + n + \"].id2FieldClassName\" ;") ;
		scriptBuffer.append("			hiddenIn4.value = th.attr(\"id2FieldBeanId\") ;") ;
		
		scriptBuffer.append("			var _h1 = document.getElementById(\"excelVoList[\" + n + \"].columnEnName\") ;")  ;
		scriptBuffer.append("			try{") ;
		scriptBuffer.append("				_form.removeChild(_h1) ;") ;
		scriptBuffer.append("			}catch(e){}") ;
		
		scriptBuffer.append("			var _h2 = document.getElementById(\"excelVoList[\" + n + \"].columnChName\") ;")  ;
		scriptBuffer.append("			try{") ;
		scriptBuffer.append("				_form.removeChild(_h2) ;") ;
		scriptBuffer.append("			}catch(e){}") ;
		
		scriptBuffer.append("			var _h3 = document.getElementById(\"excelVoList[\" + n + \"].id2NameClassName\") ;")  ;
		scriptBuffer.append("			try{") ;
		scriptBuffer.append("				_form.removeChild(_h3) ;") ;
		scriptBuffer.append("			}catch(e){}") ;
		
		scriptBuffer.append("			var _h4 = document.getElementById(\"excelVoList[\" + n + \"].id2FieldClassName\") ;")  ;
		scriptBuffer.append("			try{") ;
		scriptBuffer.append("				_form.removeChild(_h4) ;") ;
		scriptBuffer.append("			}catch(e){}") ;
		
		scriptBuffer.append("			_form.appendChild(hiddenIn1) ;") ;
		scriptBuffer.append("			_form.appendChild(hiddenIn2) ;") ;
		scriptBuffer.append("			_form.appendChild(hiddenIn3) ;") ;
		scriptBuffer.append("			_form.appendChild(hiddenIn4) ;") ;
		scriptBuffer.append("			n ++ ;") ;
		
		scriptBuffer.append("		}") ;
		scriptBuffer.append("	}") ;
		
		scriptBuffer.append("	_form.submit() ;") ;
		scriptBuffer.append("}") ;
		scriptBuffer.append("</script>") ;
		return scriptBuffer.toString() ;
	}
	
	@Override()
	public int doStartTag() throws JspException{
		StringBuffer htmlBuffer = new StringBuffer() ;
		htmlBuffer.append(createJavaScript()) ;
		if(StringUtils.isNotBlank(exportButtonCode)){
			htmlBuffer.append(exportButtonCode) ;
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
	}
	
}

