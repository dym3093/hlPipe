package org.hpin.common.util;

import java.lang.reflect.Method;

public class BeanUtils {

	/**
	 * 两个类中名字相同属性值互相复制
	 * @param dest 目标对象
	 * @param src 源对象
	 */
	public static void copyProperties(Object dest,Object src){
		Class srcClazz = src.getClass();
		Method[] srcMethods = srcClazz.getDeclaredMethods();
		for(Method m:srcMethods){
			String methodName = m.getName();
			if(methodName.startsWith("set")){
				String getMethodName = "get"+methodName.substring(methodName.indexOf("set")+3);
				Object value = invokeMethod(src,getMethodName,new Object[]{});
				if (value instanceof java.sql.Timestamp) {
					java.sql.Timestamp aa = (java.sql.Timestamp)value;
					java.util.Date bb = new java.util.Date(aa.getTime());
					value = bb;
				}
				if(value!=null){
					invokeMethod(dest,methodName,new Object[]{value});
				}
			}
		}
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

}
