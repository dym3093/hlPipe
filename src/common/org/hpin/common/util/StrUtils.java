package org.hpin.common.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * 字符串工具类
 * 
 * @author thinkpad
 * 
 */
public class StrUtils {
	/**
	 * 判断字符串是否不为空或者不为空字符
	 * 
	 * @param str
	 *            允许为NULL
	 * @return
	 */
	public static boolean isNotNullOrBlank(Object o) {
		if (null == o) {
			return false;
		} else {
			String str = o.toString();
			if ("".equals(str.trim())) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * 两个数组做对比
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean compareArgs(String[] a, String b[]) {
		String[] aa = {};
		if (a != null && a.length > 0) {
			aa = new String[a.length-1];
			for (int i = 0; i < a.length; i++) {
				if(i != a.length-1){
					aa[i] = a[i];
				}
			}
		}
		String[] bb = {};
		if (b != null && b.length > 0) {
			bb = new String[b.length-1];
			for (int i = 0; i < b.length; i++) {
				if(i != b.length-1) {
					bb[i] = b[i];
				}
			}
		}
		
		if (aa.length != bb.length) {
			return false;
		}
		else {
			Arrays.sort(aa);
			Arrays.sort(bb);
			return Arrays.equals(aa, bb);
		}
	}
	
	public static boolean compareArgsLength(String[] a, String b[]) {
		boolean flag = false;
		if(a.length==b.length) flag = true;
		return flag;
	}
	
	/**
	 * 将null转化为空白
	 * @param str
	 * @return
	 */
	public static String toStr(Object str) {
		return (str == null) ? "" : String.valueOf(str).trim();
	}

	/**
	 * 判断字符串是否为空或者不为空字符
	 * 
	 * @param str
	 *            允许为NULL
	 * @return
	 */
	public static boolean isNullOrBlank(Object o) {
		return !isNotNullOrBlank(o);
	}

	/**
	 * 将sourceObject转换为字符串后与compareStr对象比较，如果相等就返回afterIsEqualToObject对象，
	 * 不相等就返回sourceObject
	 * 
	 * @param sourceObject
	 *            被比较的对象
	 * @param compareStr
	 *            比较的对象
	 * @param object
	 *            如果相等，则返回afterIsEqualToObject对象
	 */
	public static Object toStringForCompare(Object sourceObject,
			String compareStr, Object afterIsEqualToObject) {
		if (null == sourceObject) {
			return sourceObject;
		} else if (sourceObject.toString().equals(compareStr)) {
			return compareStr;
		} else {
			return null;
		}

	}

	/**
	 * 如果对象为空，则返回空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String isNullToBank(Object str) {
		if (null == str) {
			return "";
		} else {
			return str.toString();
		}
	}

	/**
	 * 将对象转换为String类型
	 * 
	 * @param str
	 * @return
	 */
	public static String ObjectToStr(Object o, String defaultValue) {
		if (null == o) {
			return defaultValue;
		} else {
			return o.toString().trim();
		}
	}

	public static String ObjectToStr(Object o) {
		if (null == o) {
			return null;
		} else {
			return o.toString().trim();
		}
	}
	
	public static String doubleChange(double money) {
		DecimalFormat df = new DecimalFormat("######0.00");   
		return df.format(money);
	}
	
	public static void main(String arg[]) {
		double a = 12.658;
		double b = 12;
		String x = doubleChange(b);
	}

	/**
	 * 将对象转换为String类型
	 * 
	 * @param str
	 * @return
	 */
	public static String ObjectToString(Object o) {
		if (null == o) {
			return null;
		} else {
			return o.toString();
		}
	}

	/**
	 * 将对象转换为Long类型
	 * 
	 * @param str
	 * @return
	 */
	public static Long ObjectToLong(Object o, Long defaultValue) {
		if (null == o) {
			return defaultValue;
		} else {
			return (Long) o;
		}
	}

	/**
	 * 将对象转换为Long类型
	 * 
	 * @param str
	 * @return
	 */
	public static Long ObjectToLong(Object o) {
		if (null == o) {
			return null;
		} else {
			return new Long(o.toString());
		}
	}
	/**
	 * 将对象转换为Double类型
	 * 
	 * @param str
	 * @return
	 */
	public static Double ObjectToDouble(Object o) {
		if (null == o || "" == o) {
			return null;
		} else {
			return new Double(o.toString());
		}
	}
	/**
	 * 将对象转换为Integer类型
	 * 
	 * @param str
	 * @return
	 */
	public static Integer ObjectToInteger(Object o) {
		if (null == o|| ""==o) {
			return null;
		} else {
			return Integer.parseInt(o.toString());
		}
	}

	/**
	 * 集合转换成字符串
	 * 
	 * @param collection
	 *            集合
	 * @param separator
	 *            分隔符
	 * @return
	 */
	public static String CollectionToStr(Collection collection,
			String separator, boolean all) {
		Iterator it = collection.iterator();
		Object object = null;
		String str = "";
		while (it.hasNext()) {
			object = it.next();
			str = str + object.toString() + separator;
		}
		if (collection.size() > 0) {
			if (all) {
				str = "," + str;
			} else {
				str = str.substring(0, str.length() - (separator.length()));
			}
		}
		return str;
	}
}
