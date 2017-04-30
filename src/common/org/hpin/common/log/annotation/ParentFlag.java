package org.hpin.common.log.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>@desc : 在保存日志记录的时候，标识parent对象</p>
 * <p>@see : 自定义注解</p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : Apr 22, 2013 5:44:09 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParentFlag {

}

