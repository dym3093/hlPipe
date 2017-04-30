package org.hpin.common.annotation.vo;

/**
 * <p>@desc : 注解辅助POJO，标识注解的字段的某些特定属性</p>
 * <p>@see : 自定义注解</p>
 *
 * <p>@author : sky</p>
 * <p>@createDate : Jul 10, 2012 4:41:17 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class ClassFieldAnnotationVo {

	/**
	 * 属性名
	 */
	private String fieldName ;
	
	/**
	 * 属性标示
	 */
	private String fieldSign ;
	
	/**
	 * 属性类型全拼
	 */
	private String fieldTypeFullName ;
	
	/**
	 * 如果属性为数据字典，则标识数据字典的identifier
	 */
	private String fieldIdentifier ;

	
	public String getFieldName() {
		return fieldName ;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName ;
	}
	
	public String getFieldSign() {
		return fieldSign ;
	}
	
	public void setFieldSign(String fieldSign) {
		this.fieldSign = fieldSign ;
	}
	
	public String getFieldTypeFullName() {
		return fieldTypeFullName ;
	}

	public void setFieldTypeFullName(String fieldTypeFullName) {
		this.fieldTypeFullName = fieldTypeFullName ;
	}

	
	public String getFieldIdentifier() {
		return fieldIdentifier ;
	}

	
	public void setFieldIdentifier(String fieldIdentifier) {
		this.fieldIdentifier = fieldIdentifier ;
	}
}

