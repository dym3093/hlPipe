package org.hpin.common.log.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>@desc : 日志框架注解，标识日志父表与子表信息</p>
 * <p>@see : 自定义注解</p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : Apr 22, 2013 5:35:14 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLogAnnotation {

	/**
	 * 日志父表的实体类映射名
	 * @return 父表的实体类全路径
	 */
	public abstract String parentClass() ;
	
	/**
	 * 日志子表的实体类映射名
	 * @return 子表的实体类全路径
	 */
	public abstract String childClass() ;
	
}

