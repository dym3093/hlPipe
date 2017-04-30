package org.hpin.common.util;


/**
 * <p>@desc : 数据变更辅助对象</p>
 * <p>@see : 数据变更---反射</p>
 *
 * <p>@author : sky</p>
 * <p>@createDate : Jul 18, 2012 4:23:10 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class DataChangeVo {

	private String fieldName ;
	
	private String fieldType ;
	
	private Object fieldValue ;

	
	public String getFieldName() {
		return fieldName ;
	}

	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName ;
	}

	
	public String getFieldType() {
		return fieldType ;
	}

	
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType ;
	}

	
	public Object getFieldValue() {
		return fieldValue ;
	}

	
	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue ;
	}
	
	
}

