package org.hpin.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>@desc : 自定义注解，标注实体各个属性</p>
 * <p>@see : 自定义注解</p>
 *
 * <p>@author : sky</p>
 * <p>@createDate : Jul 10, 2012 12:30:47 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassFieldAnnotation {

	/**
	 * 属性中文名
	 * @return
	 */
	public abstract String fieldName() ;
	
	/**
	 * 属性标志，与对应的属性名一致
	 * @return
	 */
	public abstract String fieldSign() ;
	
	/**
	 * 属性类型简称
	 * @return
	 */
	public abstract String fieldTypeFullName() ;
	
	/**
	 * 属性的数据字典identifier
	 * @return
	 */
	public abstract String fieldIdentifier() ;
	
}

