package org.hpin.common.annotation.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hpin.common.annotation.ClassFieldAnnotation;
import org.hpin.common.annotation.vo.ClassFieldAnnotationVo;


/**
 * <p>@desc : 注解工具包，提供注解一些常用方法</p>
 * <p>@see : 自定义注解工具包，获取一个Entity实体类中所有的注解，生成注解下拉列表</p>
 *
 * <p>@author : sky</p>
 * <p>@createDate : Jul 10, 2012 4:37:06 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class AnnotationUtil {

	/**
	 * 获取所有指定注解的AnnotationVo对象
	 * @param clazz
	 * @param annotationName
	 * @return
	 */
	public static List<ClassFieldAnnotationVo> getAllFieldNamesByAnnotation(Class clazz , String annotationName){
		List<ClassFieldAnnotationVo> voList = new ArrayList<ClassFieldAnnotationVo>() ;
		Field[] fields = clazz.getDeclaredFields() ;
		for(Field field : fields){
			ClassFieldAnnotation _annotation = field.getAnnotation(ClassFieldAnnotation.class) ;
			if(_annotation != null && annotationName.equals(_annotation.annotationType().getSimpleName())){
				ClassFieldAnnotationVo vo = new ClassFieldAnnotationVo() ;
				vo.setFieldName(_annotation.fieldName()) ;
				vo.setFieldSign(_annotation.fieldSign()) ;
				vo.setFieldTypeFullName(_annotation.fieldTypeFullName()) ;
				vo.setFieldIdentifier(_annotation.fieldIdentifier()) ;
				voList.add(vo) ;
			}
		}
		return voList ;
	}
	
}

