package org.hpin.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.hpin.common.log.annotation.LogAnnotation;
import org.hpin.common.log.annotation.LogTransient;

public class ReflectionUtils {
	private final static String default_date_pattern = "yyyy-MM-dd";
	/**
	 * 转换字符串到相应类型.
	 * 
	 * @param value
	 *            待转换的字符串
	 * @param toType
	 *            转换目标类型
	 */
	public static Object convertStringToObject(Object value, Class toType) {
		if (toType.getSimpleName().equals("Date")) {
			Date date = null;
			try {
				if (value.toString().length() == 10) {
					date = DateUtils.convertDate(value.toString(), "yyyy-MM-dd");
				}
				else {
					if( value.equals("null") || value==null){
						date =null;
					}else{
				    	date = DateUtils.convertDate(value.toString(), "yyyy-MM-dd hh:mm:ss");
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date;
		} else {
			return ConvertUtils.convert(value.toString(), toType);
		}
	}
	
	/**
	 * 转换字符串到相应类型.
	 * 
	 * @param value
	 *            待转换的字符串
	 * @param toType
	 *            转换目标类型
	 */
	public static Object convertString2Object(Object value, Class toType) {
		if (toType.getSimpleName().equals("Date")) {
			Date date = null;
			try {
				date = DateUtils.coalitionDateStr2(value.toString(),"/");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return date;
		} else {
			return ConvertUtils.convert(value.toString(), toType);
		}
	}
	
	/**
	 * 转换字符串到相应类型.
	 * 
	 * @param value
	 *            待转换的字符串
	 * @param toType
	 *            转换目标类型
	 */
	public static Object convertString2Object2(Object value, Class toType) {
		if (toType.getSimpleName().equals("Date")) {
			Date date = null;
			try {
				date = DateUtils.coalitionDateStr2(value.toString(),"-");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return date;
		} else {
			return ConvertUtils.convert(value.toString(), toType);
		}
	}

	/**
	 * 将源对象中的所有非空属性赋值到target中
	 * 
	 * @param target
	 *            目标对象
	 * @param source
	 *            源对象
	 */
	public static void copyPropertiesForHasValue(Object target, Object source) {
		// 获取
		List<Field> filedList = ReflectionUtils.getAllFieldAndSuperclass(source.getClass());
		Field field = null;
		try {
			if(!source.getClass().getName().equals(target.getClass().getName())){
				//如果两个对象不是由同一个类派生
				Class srcClazz = source.getClass();
				Method[] srcMethods = srcClazz.getDeclaredMethods();
				for(Method m:srcMethods){
					String methodName = m.getName();
					if(methodName.startsWith("set")){
						String getMethodName = "get" + methodName.substring(methodName.indexOf("set") + 3);
						Object value = invokeMethod(source , getMethodName , new Object[]{});
						if (value instanceof java.sql.Timestamp) {
							java.sql.Timestamp aa = (java.sql.Timestamp)value;
							java.util.Date bb = new java.util.Date(aa.getTime());
							value = bb;
						}
						if(value != null && !value.toString().equals("-1")){
							invokeMethod(target , methodName , new Object[]{value});
						}
					}
				}
				
			}else{
				//同一类派生出的两个对象进行拷贝
				for (int i = 0; i < filedList.size(); i++) {
					field = filedList.get(i);
					field.setAccessible(true);
					Object value = field.get(source);
					if (null != value && !value.toString().equals("-1")) {
						org.springframework.util.ReflectionUtils.setField(field,
								target, value);
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 对比属性值做日志记录
	 * 
	 * @param target 目标对象
	 * @param source 源对象
	 */
	public static String getChangePropertiesForHasValue(Object target, Object source,String party) {
		StringBuffer changeVal = new StringBuffer(party+":<br/> ");
		// 获取属性
		Field[] fileds = source.getClass().getDeclaredFields();
		try {
				// 循环成员变量进行记录处理
				for (Field field:fileds){
					String fieldName = field.getName();
					if ("id".equals(fieldName))// id无记录
						continue;
					if (Modifier.isStatic(field.getModifiers()))// 静态成员变量无记录
						continue;
					if (field.getAnnotation(LogTransient.class) != null)// 注释了Transient无记录
						continue;
					field.setAccessible(true);// 设置成员变量的访问属性
					String sourceValue = ObjectUtils.toString(field.get(source));
					if(null!=target){
						String targetValue = ObjectUtils.toString(field.get(target));
						if (field.get(source) instanceof Date) {
							sourceValue = DateFormatUtils.format((Date) field.get(source) , default_date_pattern);
						}
						if (field.get(target) instanceof Date) {
							targetValue = DateFormatUtils.format((Date) field.get(target) , default_date_pattern);
						}
						if(!(sourceValue.trim()).equals(targetValue.trim())){
							LogAnnotation _logAnnotation = field.getAnnotation(LogAnnotation.class);
							String _annotaionName = (_logAnnotation == null ? null : _logAnnotation.name());
							changeVal.append(_annotaionName+": 由  "+targetValue+"   变更为："+sourceValue+"  <br/> ");
						}
					}else{
						if (field.get(source) instanceof Date) {
							sourceValue = DateFormatUtils.format((Date) field.get(source) , default_date_pattern);
						}
						if(StrUtils.isNotNullOrBlank(sourceValue)){
							LogAnnotation _logAnnotation = field.getAnnotation(LogAnnotation.class);
							String _annotaionName = (_logAnnotation == null ? null : _logAnnotation.name());
							changeVal.append(_annotaionName+": 由    变更为："+sourceValue+"  <br/> ");
						}
	
					}
					
				}
			
		}catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return changeVal.toString();
	}
	
	/**
	 * 将源对象中的非空属性值复制到目标对象中，排除ignores中的属性
	 * @param target
	 * @param source
	 * @param ignores
	 */
	public static void copyPropertiesForHasValueIgnore(Object target, Object source , String[] ignores){
		List<Field> filedList = ReflectionUtils.getAllFieldAndSuperclass(source
				.getClass());
		Field field = null;
		try {
			for (int i = 0; i < filedList.size(); i++) {
				field = filedList.get(i);
				if(StringUtils.isNotBlank(field.getName()) && fieldNameIsExistInFields(ignores , field.getName())) continue ;
				field.setAccessible(true);
				Object value = field.get(source);
				if (null != value && !value.toString().equals("-1")) {
					org.springframework.util.ReflectionUtils.setField(field,
							target, value);
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将源对象的非空属性值复制到目标对象中，排除"serialVersionUID"对象
	 * @param target
	 * @param source
	 */
	public static void copyPropertiesForHasValueIgnoreSerialVersionUID(Object target, Object source){
		String[] ignores = new String[]{"serialVersionUID"} ;
		copyPropertiesForHasValueIgnore(target , source , ignores) ;
	}
	
	/**
	 * 查找当前的属性名是否在排除复制的列表中存在
	 * @param ignores
	 * @param fieldName
	 * @return
	 */
	private static boolean fieldNameIsExistInFields(String[] ignores , String fieldName){
		boolean flag = false ;
		for(String str : ignores){
			if(StringUtils.isBlank(str)) continue ;
			if(str.equals(fieldName)){
				flag = true ; 
				break ;
			}
		}
		return flag ;
	}

	/**
	 * 获取一个类里面的属性
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<Field> getAllFieldAndSuperclass(Class clazz) {
		Field[] fileds = null;
		List filedList = new ArrayList();
		while (true) {
			if (null == clazz) {
				break;
			}
			fileds = clazz.getDeclaredFields();
			for (int i = 0; i < fileds.length; i++) {
				filedList.add(fileds[i]);
			}
			clazz = clazz.getSuperclass();
		}
		return filedList;
	}
	
	/**
	 * 获取一个类及其父类中所有的方法
	 * @param clazz
	 * @return
	 */
	public static List<Method> getAllMethodAndSuperClass(Class clazz){
		Method[] methods = null ;
		List methodList = new ArrayList() ;
		while(true){
			if(null == clazz){
				break ;
			}
			methods = clazz.getDeclaredMethods() ;
			for(Method method : methods){
				methodList.add(method) ;
			}
			clazz = clazz.getSuperclass() ;
		}
		return methodList ;
	}
	
	/**
	 * 获取属性的setter方法
	 * @param fieldName
	 * @return
	 */
	public static String parseSetName(String fieldName){
		if(fieldName != null && !"".equals(fieldName)){
			return "set" + fieldName.substring(0 ,1).toUpperCase() + fieldName.substring(1) ;
		}
		return null ;
	}
	
	/**
	 * 获取属性的getter方法
	 * @param fieldName
	 * @return
	 */
	public static String parseGetName(String fieldName){
		if(fieldName != null && !"".equals(fieldName)){
			return "get" + fieldName.substring(0 ,1).toUpperCase() + fieldName.substring(1) ;
		}
		return null ;
	}
	
	/**
	 * 根据对象，域名称和域类型全路径获取对象某个域的值
	 * @param obj
	 * @param fieldName
	 * @param fieldTypeFullName
	 * @return
	 */
	public static Object getFieldValueByObjectAndFieldName(Object obj , String fieldName , String fieldTypeFullName) throws Exception{
		Class objClass = obj.getClass() ;
		Object o = null ;
		List<Field> fieldList = getAllFieldAndSuperclass(objClass) ;
		for(Field field : fieldList){
			if(field != null && field.getName().equals(fieldName) && fieldTypeFullName.equals(field.getType().getName())){
				Class fieldType = field.getType() ;
				Method method = objClass.getMethod(parseGetName(fieldName)) ;
				o = method.invoke(obj) ;
				break ;
			}
		}
		return o ;
	}
	
	/**
	 * 将objList中对应T中的属性给setter上值
	 * @param <T> 泛型类
	 * @param t 泛型对象
	 * @param objList
	 * @param objClazz
	 * @return
	 */
	public static <T> T executeChange(T t , List objList , Class objClazz) throws Exception{
		if(t == null) return null ;
		if(objList == null || objList.size() == 0) return t ;
		if(objClazz == null) return t ;
		for(Object obj : objList){
			if(obj == null) continue ;
			if(obj.getClass() != objClazz) continue ;
			List<Field> fieldList = getAllFieldAndSuperclass(obj.getClass()) ;
			DataChangeVo vo = new DataChangeVo() ;
			for(Field f : fieldList){
				String fieldName = f.getName() ;
				Class fieldType = f.getType() ;
				Method method = obj.getClass().getMethod(parseSetName(fieldName) , fieldType) ;
				Object value = getFieldValueByObjectAndFieldName(obj , fieldName , fieldType.getName()) ;
				if("changeItem".equals(f.getName())){
					vo.setFieldName(value.toString()) ;
				}
				if("changeType".equals(f.getName())){
					vo.setFieldType(value.toString()) ;
				}
				if("valueOfChange".equals(f.getName())){
					vo.setFieldValue(value) ;
				}
			}
			setNewValueToObject(t , vo) ;
		}
		return t ;
	}
	
	/**
	 * 根据变更辅助对象，更改原始对象里相对应的属性的值
	 * @param <T>
	 * @param t
	 * @param vo
	 * @return
	 */
	private static <T> T setNewValueToObject(T t , DataChangeVo vo) throws Exception{
		List<Field> fieldList = getAllFieldAndSuperclass(t.getClass()) ;
		for(Field field : fieldList){
			field.setAccessible(true) ;
			//修饰为final的成员变量不做处理
			if(Modifier.isFinal(field.getModifiers())) continue ;
			//修饰为static的成员变量不做处理
			if(Modifier.isStatic(field.getModifiers())) continue ;
			System.out.println(field.getName() + "----" + field.getType().getName() + "---" + vo.getFieldType()) ;
			if(field.getName().equals(vo.getFieldName())){
				if(vo.getFieldType().equals(vo.getFieldType())){
					Method method = t.getClass().getMethod(parseSetName(field.getName()) , field.getType()) ;
					switch(JavaBasicTypes.getJavaType(field.getType().getSimpleName())){
						case Date : {
							method.invoke(t , DateUtils.convertDate(vo.getFieldValue().toString() , "yyyy-MM-dd")) ;
							break ;
						}
						case Long : {
							method.invoke(t , StaticMethod.nullObject2Long(vo.getFieldValue())) ;
							break ;
						}
						case Integer : {
							method.invoke(t , StaticMethod.nullObject2Integer(vo.getFieldValue())) ;
							break ;
						}
						case Double : {
							method.invoke(t , StaticMethod.nullObject2Double(vo.getFieldValue())) ;
							break ;
						}
						case Float : {
							method.invoke(t , vo.getFieldValue() == null ? 0f : Float.valueOf(vo.getFieldValue().toString())) ;
							break ;
						}
						default : {
							method.invoke(t , vo.getFieldValue()) ;
							break ;
						}
					}
					
				}
			}
		}
		return t ;
	}
	
	/**
	 * 执行类中方法
	 * @param obj 类
	 * @param methodName 方法名
	 * @param args 参数
	 * @return
	 */
	public static Object invokeMethod(Object obj,String methodName,Object[] args){
		Class clazz = obj.getClass();
		Class[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}
		Method method;
		try {
			method = clazz.getMethod(methodName, argsClass);
			return method.invoke(obj, args);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * 定义反射所能支持的类型
	 * @author sky
	 * 
	 * @mail-to:284866420@qq.com
	 *
	 */
	public static enum JavaBasicTypes{
		
		Date , Long , String , Integer , Double , Float ;
		
		public static JavaBasicTypes getJavaType(String name){
			return JavaBasicTypes.valueOf(name) ;
		}
		
	}

	/**
	 * 根据属性名称获取类属性
	 * @param clazz 类
	 * @param fieldName 属性名称
	 * @return Field
     * @author Damian
	 * @since 2016-12-29
	 */
	public static Field findField(Class<?> clazz, String fieldName){
		Field field = null;
		if(clazz!=null&&!"object".equalsIgnoreCase(clazz.getName())&&fieldName!=null&&fieldName.trim().length()>0){
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return field;
	}

	/**
	 * 根据属性名称获取其属性的类型名称
	 * @param clazz 类
	 * @param fieldName 属性名称
	 * @param findSimple true:简单类型名称，false/null:类型全名
	 * @return String
	 * @author Damian
	 * @since 2016-12-29
	 */
	public static String findTypeName(Class<?> clazz, String fieldName, Boolean findSimple){
		String type = null;
		if(fieldName!=null&&fieldName.trim().length()>0){
			Field field = findField(clazz,fieldName);
			if(field!=null){
				if(findSimple) {
					type = field.getType().getSimpleName();
				}else{
					type = field.getType().getName();
				}
			}
		}
		return type;
	}

	/**
	 * 根据属性名称获取其属性的类型名称
	 * @param clazz 类
	 * @param fieldName 属性名称
	 * @return String
	 * @author Damian
	 * @since 2016-12-29
	 */
	public static String findTypeNameSimple(Class<?> clazz, String fieldName){
		return findTypeName(clazz,fieldName,true);
	}

	/**
	 * 根据属性名称获取其属性的类型名的全称
	 * @param clazz 类
	 * @param fieldName 属性名称
	 * @return String 类型名称全称
	 * @author Damian
	 * @since 2016-12-29
	 */
	public static String findTypeNameTotal(Class<?> clazz, String fieldName){
		return findTypeName(clazz,fieldName,false);
	}

	/**
	 * 获取属性的类型
	 * @param clazz 类
	 * @param fieldName 属性名称
	 * @return String 类型名称全称
	 * @author Damian
	 * @since 2016-12-29
	 */
	public static Type findType(Class<?> clazz, String fieldName){
		return findField(clazz,fieldName).getGenericType();
	}

}
