package org.hpin.common.log.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>@desc : 定义可以标注在属性或方法名上的注解，获取某个需要记录日志的字段的中文含义</p>
 * <p>@see : 自定义注解</p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : Apr 22, 2013 5:40:17 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
@Target({java.lang.annotation.ElementType.METHOD , java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAnnotation {

	/**
	 * 标识字段的中文含义
	 * @return 字段的中文含义
	 */
	public abstract String name() ;
	
	/**
	 * 如果是数据字典，标识数据字典id2Name所在类的全路径
	 * @return
	 */
	public abstract String id2NameClass() default "" ;
	
}

