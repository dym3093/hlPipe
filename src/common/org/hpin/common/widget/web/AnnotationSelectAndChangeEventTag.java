package org.hpin.common.widget.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.hpin.common.annotation.util.AnnotationUtil;
import org.hpin.common.annotation.vo.ClassFieldAnnotationVo;

/**
 * <p>@desc : 注解下拉框-onChange事件标签</p>
 * <p>@see : 自定义注解</p>
 *
 * <p>@author : sky</p>
 * <p>@createDate : Jul 10, 2012 5:38:50 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class AnnotationSelectAndChangeEventTag extends TagSupport {
	
	/**
	 * 选择框td的id
	 */
	private String selectTdId ;
	
	/**
	 * 原始值显示框所在td的id
	 */
	private String inputTd1Id ;
	
	/**
	 * 新值显示框所在td的id
	 */
	private String inputTd2Id ;
	
	/**
	 * 选择框id
	 */
	private String selectId ;
	
	/**
	 * 选择框名称
	 */
	private String selectName ;
	
	/**
	 * 选择框验证规则
	 */
	private String selectLang ;
	
	/**
	 * 选择框onchange事件
	 */
	private String selectOnchange ;
	
	/**
	 * 选择框值
	 */
	private String selectValue ;
	
	/**
	 * 输入框id
	 */
	private String inputId ;
	
	/**
	 * 输入框名称
	 */	
	private String inputName ;
	
	/**
	 * 输入框验证规则
	 */
	private String inputLang ;
	
	/**
	 * 输入框onchange事件
	 */
	private String inputOnchange ;
	
	/**
	 * 输入框onBlur事件
	 */
	private String inputOnBlur ;
	
	/**
	 * 输入框onFocus事件
	 */
	private String inputOnFocus ;
	
	/**
	 * 输入框值
	 */
	private String inputValue ;
	
	/**
	 * 输入框是否只读
	 */
	private Boolean inputReadonly = true ; 

	/**
	 * 类全名
	 */
	private String classFullName = null ;
	
	/**
	 * 注解名称
	 */
	private String annotationName = "ClassFieldAnnotation" ;
	
	/**
	 * 获取Web工程上下文
	 * @return
	 */
	private String getWebContext(){
		HttpServletRequest request = getRequest() ;
		return request.getContextPath() ;
	}
	

	/**
	 * 获取HttpServletRequest对象
	 * @return
	 */
	private HttpServletRequest getRequest() {
		HttpServletRequest request = null ;
		try {
			request = (HttpServletRequest)pageContext.getRequest();
		} catch (Exception e) {
			e.printStackTrace() ;
		}
		return request ;
	}
	
	/**
	 * 创建联动获取注解标示属性值的Javascirpt脚本
	 * @return
	 */
	private String createJavaScript(String webContext){
		StringBuffer scriptBuffer = new StringBuffer() ;
		scriptBuffer.append("<script type = 'text/javascript'") ;
		scriptBuffer.append(" src = '" + webContext + "/scripts/widgets/annotationSelectedLink.js'>") ;
		scriptBuffer.append("</script>") ;
		return scriptBuffer.toString() ;
	}
	
	@Override
	public int doStartTag() throws JspException {
		if(StringUtils.isBlank(classFullName) || StringUtils.isBlank(annotationName)) throw new JspException("参数不能为空！") ;
		StringBuffer htmlBuffer = new StringBuffer() ;
		//htmlBuffer.append(createJavaScript(getWebContext())) ;
		htmlBuffer.append("<td id = '").append(selectTdId).append("'>").append(createAnnotationSelect()).append("</td>") ;
		htmlBuffer.append("<td id = '").append(inputTd1Id).append("'>").append(createAnnotationFieldValueInput()).append("</td>") ;
		htmlBuffer.append("<td id = '").append(inputTd2Id).append("'>").append(createNewValueInput()).append("</td>") ;
		try {
			pageContext.getOut().println(htmlBuffer.toString());
			System.out.println("HTML=======" + htmlBuffer.toString()) ;
		} catch (Exception e) {
			e.printStackTrace();
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
		selectTdId = null ;
		inputTd1Id = null ;
		inputTd2Id = null ;
		selectId = null ;
		selectName = null ;
		selectLang = null ;
		selectValue = null ;
		selectOnchange = null ;
		inputReadonly = true ;
		inputId = null ;
		inputName = null ;
		inputLang = null ;
		inputOnBlur = null ;
		inputOnchange = null ;
		inputOnFocus = null ;
		inputValue = null ;
		classFullName = null ;
		annotationName = "ClassFieldAnnotation" ;
	}
	
	/**
	 * 构造注解列表下拉框
	 * @return
	 * @throws JspException
	 */
	private String createAnnotationSelect() throws JspException{
		Class clazz = null ;
		try{
			clazz = Class.forName(classFullName) ;
		}catch (Exception e) {
			throw new JspException("传入的类全路径不能被加载！") ;
		}
		String path = getWebContext() ;
		StringBuffer htmlBuffer = new StringBuffer() ;
		htmlBuffer.append("<select id = '").append(selectId).append("' name = '").append(selectName).append("' ") ;
		if(StringUtils.isNotBlank(selectLang)){
			htmlBuffer.append(" lang = '").append(selectLang).append("' ") ;
		}
		if(StringUtils.isNotBlank(selectOnchange)){
			htmlBuffer.append(" onchange = '").append(selectOnchange).append("' ") ;
		}else{
			htmlBuffer.append(" onchange = \"selectField(this , \'" + path + "\')\" ") ;
		}
		htmlBuffer.append(">") ;
		htmlBuffer.append("<option></option>") ;
		List<ClassFieldAnnotationVo> voList = AnnotationUtil.getAllFieldNamesByAnnotation(clazz , annotationName) ;
		for(ClassFieldAnnotationVo vo : voList){
			htmlBuffer.append("<option value = '").append(vo.getFieldSign()).
							append("-aceHWYace-(").append(vo.getFieldTypeFullName()).append(")-aceHWYace-").
							append(vo.getFieldName()).append("-aceHWYace-").append(vo.getFieldIdentifier()).append("' ") ;
			if((vo.getFieldSign() + "-aceHWYace-(" + vo.getFieldTypeFullName() + ")-aceHWYace-" + vo.getFieldName() + "-aceHWYace-" + vo.getFieldIdentifier()).equals(selectValue)){
				htmlBuffer.append(" selected") ;
			}
			htmlBuffer.append(">").append(vo.getFieldName()).append("</option>") ;
		}
		htmlBuffer.append("</select>") ;
		return htmlBuffer.toString() ;
	}
	
	/**
	 * 构造注解标示属性值的显示input输入框
	 * @return
	 */
	public String createAnnotationFieldValueInput(){
		StringBuffer inputBuffer = new StringBuffer() ;
		inputBuffer.append("<input type = 'text' id = '").append(inputId).append("' name = '").append(inputName).append("' ") ;
		if(StringUtils.isNotBlank(inputLang)) inputBuffer.append(" lang = '").append(inputLang).append("' ") ;
		if(StringUtils.isNotBlank(inputOnBlur)) inputBuffer.append(" onblur = '").append(inputOnBlur).append("' ") ;
		if(StringUtils.isNotBlank(inputOnchange)) inputBuffer.append(" onchange = '").append(inputOnchange).append("' ") ;
		if(StringUtils.isNotBlank(inputOnFocus)) inputBuffer.append(" onfocus = '").append(inputOnFocus).append("' ") ;
		if(inputReadonly) inputBuffer.append(" readonly = 'readonly' ") ;
		if(StringUtils.isNotBlank(inputValue)) inputBuffer.append(" value = '").append(inputValue).append("' ") ;
		inputBuffer.append(" />") ;
		return inputBuffer.toString() ;
	}
	
	/**
	 * 构造新的属性值输入框
	 * @return
	 */
	public String createNewValueInput(){
		StringBuffer htmlBuffer = new StringBuffer() ;
		htmlBuffer.append("<input type = 'text' name = 'newValue' id = 'newValue'/>") ;
		return htmlBuffer.toString() ;  
	}

	public String getClassFullName() {
		return classFullName ;
	}

	
	public void setClassFullName(String classFullName) {
		this.classFullName = classFullName ;
	}

	
	public String getAnnotationName() {
		return annotationName ;
	}

	
	public void setAnnotationName(String annotationName) {
		this.annotationName = annotationName ;
	}


	
	public String getSelectId() {
		return selectId ;
	}


	
	public void setSelectId(String selectId) {
		this.selectId = selectId ;
	}


	
	public String getSelectName() {
		return selectName ;
	}


	
	public void setSelectName(String selectName) {
		this.selectName = selectName ;
	}


	
	public String getSelectLang() {
		return selectLang ;
	}


	
	public void setSelectLang(String selectLang) {
		this.selectLang = selectLang ;
	}


	
	public String getSelectOnchange() {
		return selectOnchange ;
	}


	
	public void setSelectOnchange(String selectOnchange) {
		this.selectOnchange = selectOnchange ;
	}


	
	public String getSelectValue() {
		return selectValue ;
	}


	
	public void setSelectValue(String selectValue) {
		this.selectValue = selectValue ;
	}


	
	public String getInputId() {
		return inputId ;
	}


	
	public void setInputId(String inputId) {
		this.inputId = inputId ;
	}


	
	public String getInputName() {
		return inputName ;
	}


	
	public void setInputName(String inputName) {
		this.inputName = inputName ;
	}


	
	public String getInputLang() {
		return inputLang ;
	}


	
	public void setInputLang(String inputLang) {
		this.inputLang = inputLang ;
	}


	
	public String getInputOnchange() {
		return inputOnchange ;
	}


	
	public void setInputOnchange(String inputOnchange) {
		this.inputOnchange = inputOnchange ;
	}


	
	public String getInputOnBlur() {
		return inputOnBlur ;
	}


	
	public void setInputOnBlur(String inputOnBlur) {
		this.inputOnBlur = inputOnBlur ;
	}


	
	public String getInputOnFocus() {
		return inputOnFocus ;
	}


	
	public void setInputOnFocus(String inputOnFocus) {
		this.inputOnFocus = inputOnFocus ;
	}


	
	public String getInputValue() {
		return inputValue ;
	}


	
	public void setInputValue(String inputValue) {
		this.inputValue = inputValue ;
	}


	
	public Boolean getInputReadonly() {
		return inputReadonly ;
	}


	
	public void setInputReadonly(Boolean inputReadonly) {
		this.inputReadonly = inputReadonly ;
	}


	
	public String getSelectTdId() {
		return selectTdId ;
	}


	
	public void setSelectTdId(String selectTdId) {
		this.selectTdId = selectTdId ;
	}


	
	public String getInputTd1Id() {
		return inputTd1Id ;
	}


	
	public void setInputTd1Id(String inputTd1Id) {
		this.inputTd1Id = inputTd1Id ;
	}


	
	public String getInputTd2Id() {
		return inputTd2Id ;
	}


	
	public void setInputTd2Id(String inputTd2Id) {
		this.inputTd2Id = inputTd2Id ;
	}
	
}

