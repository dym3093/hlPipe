package org.hpin.common.log.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>@desc : 定义可以标注在方法和属性上的注解，标注此注解的属性不需要进行日志记录</p>
 * <p>@see : 自定义注解</p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : Apr 22, 2013 5:47:25 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
@Target({java.lang.annotation.ElementType.METHOD , java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogTransient {
}

