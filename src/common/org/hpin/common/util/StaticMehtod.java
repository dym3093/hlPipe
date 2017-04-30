package org.hpin.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * <p>
 * Title: friends
 * </p>
 * <p>
 * Description: 静态方法类，如字符转换等
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: panlong
 * </p>
 * 
 * @author panlong
 */
public class StaticMehtod {

	 static String CHARSET_PAGE = "UTF-8";
	 public static String CHARSET_DB ="UTF-8";
 static String CHARSET_BEAN = "UTF-8";
	/**
	 * classpath标识
	 */
	public final static String CLASSPATH_FLAG = "classpath:";

	/**
	 * classpath标识
	 */

	public final static ClassLoader loader = Thread.currentThread()
			.getContextClassLoader();

	/**
	 * 取filePath的绝对路径
	 * 
	 * @param filePath
	 *            文件路径
	 * @see StaticMehtod#getClasspath()
	 * @return
	 */
	public static String getFilePath(String filePath) {
		if (filePath != null) {
			if (filePath.length() > CLASSPATH_FLAG.length()) {
				if (CLASSPATH_FLAG.equals(filePath.substring(0, CLASSPATH_FLAG
						.length()))) {
					filePath = getClasspath()
							+ filePath.substring(CLASSPATH_FLAG.length());
				}
			}
		}
		return filePath;
	}

	/**
	 * 获取filePath的url
	 * 
	 * @param filePath
	 * @return
	 * @throws MalformedURLException
	 */
	public static URL getFileUrl(String filePath) {
		URL url = null;
		if (filePath != null) {
			if (filePath.length() > CLASSPATH_FLAG.length()) {
				if (CLASSPATH_FLAG.equals(filePath.substring(0, CLASSPATH_FLAG
						.length()))) {

					url = loader.getResource(filePath.substring(CLASSPATH_FLAG
							.length()));

				} else {
					// TODO 有问题，需修改
				}
			}
		}
		return url;
	}
	//数据库字符的转换函数
	  public static String dbNull2String(String s) {
	    String reStr = "";
	    reStr = s == null ? "" : s;
	    reStr = reStr.trim();
	    if (CHARSET_PAGE.equals(CHARSET_DB)) {
	      return reStr;
	    }
	    try {
	      reStr = new String(reStr.getBytes(CHARSET_DB), CHARSET_PAGE);
	    }
	    catch (Exception e) {
	      System.out.println("转化数据库字符错误!");
	    }
	    return reStr;
	  }
	  
	  public static synchronized String strFromBeanToPage(String s) {
		    String reStr = "";
		    reStr = s == null ? "" : s;
		    reStr = reStr.trim();
		    if (CHARSET_BEAN.equals(CHARSET_PAGE)) {
		      return reStr;
		    }
		    try {
		      reStr = new String(reStr.getBytes(CHARSET_BEAN), CHARSET_PAGE);
		    }
		    catch (Exception e) {
		    	System.out.println(e+"转化数据库字符错误!");
		    }
		    return reStr;
		  }

		  public static synchronized String strFromPageToBean(String s) {
		    String reStr = "";
		    reStr = s == null ? "" : s;
		    reStr = reStr.trim();
		    if (CHARSET_BEAN.equals(CHARSET_PAGE)) {
		      return reStr;
		    }
		    try {
		      reStr = new String(reStr.getBytes(CHARSET_PAGE), CHARSET_BEAN);
		    }
		    catch (Exception e) {
		    	System.out.println(e+"转化数据库字符错误!");
		    }
		    return reStr;
		  }

		  public static synchronized String strFromDBToPage(String s) {
		    String reStr = "";
		    reStr = s == null ? "" : s;
		    reStr = reStr.trim();
		    if (CHARSET_PAGE.equals(CHARSET_DB)) {
		      return reStr;
		    }
		    try {
		      reStr = new String(reStr.getBytes(CHARSET_DB), CHARSET_PAGE);
		    }
		    catch (Exception e) {
		    	System.out.println(e+"转化数据库字符错误!");
		    }
		    return reStr;
		  }

		  public static synchronized String strFromPageToDB(String s) {
		    String reStr = "";
		    reStr = s == null ? "" : s;
		    reStr = reStr.trim();
		    if (CHARSET_PAGE.equals(CHARSET_DB)) {
		      return reStr;
		    }
		    try {
		      reStr = new String(reStr.getBytes(CHARSET_PAGE), CHARSET_DB);
		    }
		    catch (Exception e) {
		    	System.out.println(e+ "转化数据库字符错误!");
		    }
		    return reStr;
		  }
	/**
	 * 读java包时返回的路径
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 * @throws MalformedURLException
	 */
	public static String getFilePathForUrl(String filePath) throws FileNotFoundException {
		URL url = getFileUrl(filePath);
		return url.getFile();
	}

	/**
	 * 取filePath的InputStream
	 * 
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getFileInputStream(String filePath)
			throws FileNotFoundException {
		InputStream inputStream = null;
		if (filePath != null) {
			if (filePath.length() > CLASSPATH_FLAG.length()) {
				if (CLASSPATH_FLAG.equals(filePath.substring(0, CLASSPATH_FLAG
						.length()))) {
					inputStream = loader.getResourceAsStream(filePath
							.substring(CLASSPATH_FLAG.length()));

				} else {

					inputStream = new FileInputStream(filePath);

				}
			}
		}
		return inputStream;
	}

	public StaticMehtod() {
	}

	/**
	 * @see 字符处理方法：将首字符转换为大写
	 * @param string
	 * @return
	 */
	public static String firstToUpperCase(String string) {
		String post = string.substring(1, string.length());
		String first = ("" + string.charAt(0)).toUpperCase();
		return first + post;
	}

	/**
	 * @see 将中文格式转换成标准格式
	 * @see 例如：StaticMehtod.getString("中文");
	 * @param para
	 *            String 中文字符串
	 * @return String para的标准格式的串
	 */
	public static String getString(String para) {
		String reStr = "";
		try {
			reStr = new String(para.getBytes("GB2312"), "ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reStr;
	}

	/**
	 * @see 修改iso到GB2312
	 * 
	 * @param para
	 *            String
	 * @return String
	 */

	public static String getPageString(String para) {
		String reStr = "";
		try {
			reStr = new String(para.getBytes("ISO-8859-1"), "GB2312");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reStr;
	}

	/**
	 * @see 得到一指定分隔符号分隔的vector 如：Vector nn =
	 *      StaticMehtod.getVector("2003-4-5","-");
	 */
	public static Vector getVector(String string, String tchar) {
		StringTokenizer token = new StringTokenizer(string, tchar);
		Vector vector = new Vector();
		if (!string.trim().equals("")) {
			try {
				while (token.hasMoreElements()) {
					vector.add(token.nextElement().toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return vector;
	}

	/**
	 * @see 将一个字符串按照一定长度截为一个List
	 * 
	 * @param str
	 * @param size
	 * @return
	 */
	public static ArrayList getArrayList(String str, int size) {
		ArrayList vec = new ArrayList();
		String temp = "";
		str = str.trim();
		try {
			while (str.length() > size) {
				temp = str;
				vec.add(temp);
				str = str.substring(0, str.length() - size);
			}
			if (!str.equals("")) {
				vec.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vec;
	}

	/**
	 * @see 得到一指定分隔符号分隔的ArrayList
	 * 
	 * @param string
	 * @param tchar
	 * @return
	 */
	public static ArrayList getArrayList(String string, String tchar) {
		StringTokenizer token = new StringTokenizer(string, tchar);
		ArrayList array = new ArrayList();
		if (!string.trim().equals("")) {
			try {
				while (token.hasMoreElements()) {
					array.add(token.nextElement().toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	/**
	 * @see 判断id值是否包含在数组中
	 * @param id
	 *            int id
	 * @param array
	 *            集合 数组
	 * @return
	 * @throws Exception
	 */
	public static boolean fHasId(int id, ArrayList array) throws Exception {

		boolean ret = false;
		int i = 0;
		try {
			while (i < array.size() && ret == false) {
				if (id == StaticMehtod.nullObject2int(array.get(i))) {
					ret = true;
				}
				i++;
			}
		} catch (Exception e) {
			// BocoLog.error("StaticMehtod.java", 0, "错误", e);
		}

		return ret;
	}

	/**
	 * @see 得到两个集合的交集，返回一个新的集合
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static ArrayList getArrayList(ArrayList array1, ArrayList array2) {

		ArrayList array = new ArrayList();
		try {
			for (int i = 0; i < array1.size(); i++) {
				int temp = StaticMehtod.nullObject2int(array1.get(i));
				if (StaticMehtod.fHasId(temp, array2) == true) {
					array.add(new Integer(temp));
				}
			}
		} catch (Exception e) {
			// BocoLog.error("StaticMehtod.java", 0, "错误", e);
		}

		return array;
	}

	/**
	 * @see 得到两个集合的叉集，返回一个新的集合，即：在array1中，不在array2中
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static ArrayList getArrayList2(ArrayList array1, ArrayList array2) {

		ArrayList array = new ArrayList();
		int size1 = array1.size();
		int size2 = array2.size();
		if ((size2 == 0) || (size1 == 0)) {
			array = array1;
		} else {
			try {
				for (int i = 0; i < array1.size(); i++) {
					int temp = StaticMehtod.nullObject2int(array1.get(i));
					if (StaticMehtod.fHasId(temp, array2) == false) {
						array.add(new Integer(temp));
					}
				}
			} catch (Exception e) {
				// BocoLog.error("StaticMehtod.java", 0, "错误", e);
			}
		}

		return array;
	}

	/**
	 * 字符转换函数
	 * 
	 * @param s
	 * @return output:如果字符串为null,返回为空,否则返回该字符串
	 */
	public static String nullObject2String(Object s) {
		String str = "";
		try {
			str = s.toString();
		} catch (Exception e) {
			str = "";
		}
		return str;
	}

	/**
	 * 将一个对象转换为String,如果
	 * 
	 * @param s
	 * @param chr
	 * @return
	 */
	public static String nullObject2String(Object s, String chr) {
		String str = chr;
		try {
			str = s.toString();
		} catch (Exception e) {
			str = chr;
		}
		return str;
	}

	/**
	 * 将一个对象转换为整形
	 * 
	 * @param s
	 * @return
	 */
	public static int nullObject2int(Object s) {
		String str = "";
		int i = 0;
		try {
			str = s.toString();
			i = Integer.parseInt(str);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}
	/**
	 * 将一个对象转换为整形
	 * 
	 * @param s
	 * @return
	 */
	public static Double nullObject2Double(Object s) {
		String str = "";
		Double i = 0.0;
		try {
			str = s.toString();
			i = Double.valueOf(str);
		} catch (Exception e) {
			i = 0.0;
		}
		return i;
	}
	/**
	 * 将对象转换为整形
	 * 
	 * @param s
	 * @param in
	 * @return
	 */
	public static int nullObject2int(Object s, int in) {
		String str = "";
		int i = in;
		try {
			str = s.toString();
			i = Integer.parseInt(str);
		} catch (Exception e) {
			i = in;
		}
		return i;
	}

	public static Timestamp nullObject2Timestamp(Object s) {
		Timestamp str = null;
		try {
			str = Timestamp.valueOf(s.toString());
		} catch (Exception e) {
			str = null;
		}
		return str;
	}

	/**
	 * 字符转换函数如果字符串为null,返回为空,否则返回该字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String null2String(String s) {
		return s == null ? "" : s;
	}

	public static ArrayList getSubList(ArrayList list1, ArrayList list2) {
		ArrayList list = new ArrayList();
		int j = list1.size();
		int k = list2.size();
		if ((j > 0) && (k > 0)) {
			Collections.sort(list1);
			Collections.sort(list2);
			for (int i = 0; i < j; i++) {
				String temp = StaticMehtod.nullObject2String(list1.get(i));
				for (int l = 0; l < k; l++) {
					String temp2 = StaticMehtod.nullObject2String(list2.get(l));
					if (temp.equals(temp2)) {
						list.add(temp);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static Vector getSubVec(Vector list1, Vector list2) {
		Vector list = new Vector();
		int j = list1.size();
		int k = list2.size();
		if ((j > 0) && (k > 0)) {
			Collections.sort(list1);
			Collections.sort(list2);
			for (int i = 0; i < j; i++) {
				String temp = StaticMehtod.nullObject2String(list1.get(i));
				for (int l = 0; l < k; l++) {
					String temp2 = StaticMehtod.nullObject2String(list2.get(l));
					if (temp.equals(temp2)) {
						list.add(temp);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 页面间传参字符的反转换函数
	 * 
	 * @param s
	 * @return
	 */
	public static String PageNStrRev(String s, String charPage, String charDB) {

		String charForm = "";
		String reStr = "";

		try {
			
		}

		catch (Exception e) {
			// BocoLog.error(e, 290, "数据库字符的反转换错误!");
		}

		return reStr;
	}

	/**
	 * 字符转换函数,如果字符串1为null,返回为字符串2,否则返回该字符串
	 * 
	 * @param s
	 * @param s1
	 * @return
	 */
	public static String null2String(String s, String s1) {
		return s == null ? s1 : s;
	}

	/**
	 * 字符转换函数:如果字符串1为null或者不能转换成整型,返回为0
	 * 
	 * @param s
	 * @return
	 */
	public static int null2int(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	/**
	 * 字符转换函数:如果字符串1为null或者不能转换成整型,返回为0
	 * 
	 * @param s
	 * @return
	 */
	public static long null2Long(String s) {
		long i = 0;
		try {
			i = Long.parseLong(s);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	/**
	 * 对象转换为long型
	 * 
	 * @param s
	 * @return
	 */
	public static long nullObject2Long(Object s) {
		long i = 0;

		String str = "";
		try {
			str = s.toString();
			i = Long.parseLong(str);
		} catch (Exception e) {
			i = 0;
		}

		return i;
	}

	/**
	 * 将对象转换为long,如果无法转换则返回temp
	 * 
	 * @param s
	 * @param temp
	 * @return
	 */
	public static long nullObject2Long(Object s, long temp) {
		long i = temp;

		String str = "";
		try {
			str = s.toString();
			i = Long.parseLong(str);
		} catch (Exception e) {
			i = temp;
		}

		return i;
	}

	/**
	 * 字符转换函数:如果字符串1为null或者不能转换成整型,返回为整型2
	 * 
	 * @param s
	 * @param in
	 * @return
	 */
	public static int null2int(String s, int in) {
		int i = in;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			i = in;
		}
		return i;
	}

	/**
	 * 字符串处理方法：
	 * 
	 * @param str
	 * @param dim
	 * @return
	 */
	public static ArrayList TokenizerString(String str, String dim) {
		return TokenizerString(str, dim, false);
	}

	/***************************************************************************
	 * 将输入的字符串str按照分割符dim分割成字符串数组并返回ArrayList字符串数组**** If the returndim flag is
	 * true, then the dim characters are also returned as tokens. Each delimiter
	 * is returned as a string of length one. If the flag is false, the
	 * delimiter characters are skipped and only serve as separators between
	 * tokens.
	 **************************************************************************/
	public static ArrayList TokenizerString(String str, String dim,
			boolean returndim) {
		str = null2String(str);
		dim = null2String(dim);
		ArrayList strlist = new ArrayList();
		StringTokenizer strtoken = new StringTokenizer(str, dim, returndim);
		while (strtoken.hasMoreTokens()) {
			strlist.add(strtoken.nextToken());
		}
		return strlist;
	}

	/**
	 * 字符串处理方法： 类似上面的方法,将输入的字符串str按照分割符dim分割成字符串数组,**** 并返回定长字符串数组
	 * 
	 * @param str
	 * @param dim
	 * @return
	 * @see TokenizerString
	 */

	public static String[] TokenizerString2(String str, String dim) {
		return TokenizerString2(str, dim, false);
	}

	/**
	 * 字符串处理方法：
	 * 
	 * @see TokenizerString
	 * @param str
	 * @param dim
	 * @param returndim
	 * @return
	 */
	public static String[] TokenizerString2(String str, String dim,
			boolean returndim) {
		ArrayList strlist = TokenizerString(str, dim, returndim);
		int strcount = strlist.size();
		String[] strarray = new String[strcount];
		for (int i = 0; i < strcount; i++) {
			strarray[i] = (String) strlist.get(i);
		}
		return strarray;
	}

	/**
	 * 
	 * @param v
	 * @param l
	 * @return
	 */
	public static String add0(int v, int l) {
		long lv = (long) Math.pow(10, l);
		return String.valueOf(lv + v).substring(1);
	}

	/**
	 * Cookie系列方法：获得请求中Cookie的某个key的值
	 * 
	 * @param req
	 * @param key
	 * @return
	 */
	public static String getCookie(HttpServletRequest req, String key) {
		Cookie[] cookies = req.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(key)) {
				return cookies[i].getValue();
			}
		}
		return null;
	}

	/**
	 * Cookie系列方法
	 * 
	 * @param res
	 * @param key
	 * @param value
	 * @param age
	 * @param domain
	 */
	public static void setCookie(HttpServletResponse res, String key,
			String value, int age, String domain) {

		Cookie newCookie = new Cookie(key, value);
		newCookie.setMaxAge(age);
		newCookie.setDomain(domain);
		newCookie.setPath("/");

		res.addCookie(newCookie);

	}

	/**
	 * Cookie系列方法
	 * 
	 * @param res
	 * @param key
	 * @param value
	 * @param age
	 */
	public static void setCookie(HttpServletResponse res, String key,
			String value, int age) {

		Cookie newCookie = new Cookie(key, value);
		newCookie.setMaxAge(age);
		newCookie.setPath("/");
		res.addCookie(newCookie);

	}

	/**
	 * Cookie系列方法
	 * 
	 * @param res
	 * @param key
	 * @param value
	 */
	public static void setCookie(HttpServletResponse res, String key,
			String value) {
		setCookie(res, key, value, -1);
	}

	/**
	 * 编码处理方法：
	 * 
	 * @param s
	 * @return
	 */
	public static String fromScreen(String s) {
		if (s == null) {
			s = null2String(s);
		}
		s = fromBaseEncoding(s);
		s = toHtml(s);
		return s;
	}

	/**
	 * 编码处理方法：
	 * 
	 * @param s
	 * @return
	 */
	public static String toScreen(String s) {
		if (s == null) {
			s = null2String(s);
		}
		s = toBaseEncoding(s);
		s = fromHtml(s);
		return s;
	}

	/**
	 * 字符串处理方法：
	 * 
	 * @param s
	 * @param languageid
	 * @return
	 */
	public static String toScreenToEdit(String s, int languageid) {
		if (s == null) {
			s = null2String(s).trim();
		}
		s = toBaseEncoding(s);
		s = fromHtmlToEdit(s);
		return s;
	}

	/**
	 * 编码处理方法：转换GB2312到ISO-8859-1
	 * 
	 * @param s
	 * @return
	 */
	public static String toBaseEncoding(String s) {
		try {
			byte[] target_byte = s.getBytes("GB2312");
			return new String(target_byte, "ISO-8859-1");
		} catch (Exception ex) {
			return s;
		}
	}

	/**
	 * 编码处理方法：转换ISO-8859-1到GB2312
	 * 
	 * @param s
	 * @return
	 */
	public static String fromBaseEncoding(String s) {
		try {
			byte[] target_byte = s.getBytes("ISO-8859-1");
			return new String(target_byte, "GB2312");
		} catch (Exception ex) {
			return s;
		}
	}

	/**
	 * 字符串处理方法：转换html标签为可显示
	 * 
	 * @param s
	 * @return
	 */
	public static String toHtml(String s) {
		char c[] = s.toCharArray();
		char ch;
		int i = 0;
		StringBuffer buf = new StringBuffer();

		while (i < c.length) {
			ch = c[i++];

			if (ch == '"') {
				buf.append("&quot;");
			} else if (ch == '\'') {
				buf.append("\\'");
			} else if (ch == '&') {
				buf.append("&amp;");
			} else if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '>') {
				buf.append("&gt;");
			} else if (ch == '\n') {
				buf.append("<br>");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	/**
	 * 字符串处理方法：转换html标签为可显示
	 * 
	 * @param s
	 * @return
	 */
	public static String fromHtml(String s) {
		return StringReplace(s, "\\'", "\'");

	}

	/**
	 * 字符串处理方法：转换html标签为可显示
	 * 
	 * @param s
	 * @return
	 */
	public static String fromHtmlToEdit(String s) {
		return StringReplace(StringReplace(s, "<br>", ""), "\\'", "\'");
	}

	/**
	 * 数组处理方法：察看某数组对象是否有s对象
	 * 
	 * @param a
	 * @param s
	 * @return
	 */
	public static boolean contains(Object a[], Object s) {
		if (a == null || s == null) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i] != null && a[i].equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 字符串处理方法：根据起始结束标记截断String c
	 * 
	 * @param c
	 * @param begin_tag
	 * @param end_tag
	 * @return
	 */
	public static String extract(String c, String begin_tag, String end_tag) {
		int begin = begin_tag == null ? 0 : c.indexOf(begin_tag);
		int len = begin_tag == null ? 0 : begin_tag.length();
		int end = end_tag == null ? c.length() : c
				.indexOf(end_tag, begin + len);

		if (begin == -1) {
			begin = 0;
			len = 0;
		}
		if (end == -1) {
			end = c.length();
		}
		return c.substring(begin + len, end);
	}

	/**
	 * 字符串处理：截取s1中的对象
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static String remove(String s1, String s2) {
		int i = s1.indexOf(s2);
		int l = s2.length();
		if (i != -1) {
			return s1.substring(0, i) + s1.substring(i + l);
		}
		return s1;
	}

	/**
	 * 字符串处理：
	 * 
	 * @param s
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static String replaceChar(String s, char c1, char c2) {
		if (s == null) {
			return s;
		}

		char buf[] = s.toCharArray();
		for (int i = 0; i < buf.length; i++) {
			if (buf[i] == c1) {
				buf[i] = c2;
			}
		}
		return String.valueOf(buf);
	}

	/**
	 * 校验方法：是否含有@
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmail(String s) {
		int pos = 0;
		pos = s.indexOf("@");
		if (pos == -1) {
			return false;
		}
		return true;
	}

	/**
	 * 数组处理：将对象数组中的第i个对象与第j个对象对调
	 * 
	 * @param a
	 * @param i
	 * @param j
	 */
	public static void swap(Object a[], int i, int j) {
		Object t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	/**
	 * 字符串处理方法：
	 * 
	 * @param sou
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static String StringReplace(String sou, String s1, String s2) {
		int idx = sou.indexOf(s1);
		if (idx < 0) {
			return sou;
		}
		return StringReplace(sou.substring(0, idx) + s2
				+ sou.substring(idx + s1.length()), s1, s2);
	}

	/**
	 * 
	 * @param sentence
	 * @param oStart
	 * @param oEnd
	 * @param rWord
	 * @param matchCase
	 * @return
	 */
	public static String replaceRange(String sentence, String oStart,
			String oEnd, String rWord, boolean matchCase) {
		int sIndex = -1;
		int eIndex = -1;
		if (matchCase) {
			sIndex = sentence.indexOf(oStart);
		} else {
			sIndex = sentence.toLowerCase().indexOf(oStart.toLowerCase());
		}
		if (sIndex == -1 || sentence == null || oStart == null || oEnd == null
				|| rWord == null) {
			return sentence;
		} else {
			if (matchCase) {
				eIndex = sentence.indexOf(oEnd, sIndex);
			} else {
				eIndex = sentence.toLowerCase().indexOf(oEnd.toLowerCase(),
						sIndex);
			}
			String newStr = null;
			if (eIndex > -1) {
				newStr = sentence.substring(0, sIndex) + rWord
						+ sentence.substring(eIndex + oEnd.length());
			} else {
				newStr = sentence.substring(0, sIndex) + rWord
						+ sentence.substring(sIndex + oStart.length());
			}
			return replaceRange(newStr, oStart, oEnd, rWord, matchCase);
		}
	}

	/**
	 * 类型转换：字符串转换为整形
	 * 
	 * @param v
	 * @return
	 */

	public static int getIntValue(String v) {
		return getIntValue(v, -1);
	}

	/**
	 * 类型转换：字符串转换为整形如果例外则返回预给值def
	 * 
	 * @param v
	 * @return
	 */

	public static int getIntValue(String v, int def) {
		try {
			return Integer.parseInt(v);
		} catch (Exception ex) {
			return def;
		}
	}

	/**
	 * 类型转换：将给出的字符串v转换成浮点值返回，如果例外则返回预给值-1
	 * 
	 * @param v
	 * @return
	 */
	public static float getFloatValue(String v) {
		return getFloatValue(v, -1);
	}

	/**
	 * 类型转换：将给出的字符串v转换成浮点值返回，如果例外则返回预给值def
	 * 
	 * @param v
	 * @return
	 */
	public static float getFloatValue(String v, float def) {
		try {
			return Float.parseFloat(v);
		} catch (Exception ex) {
			return def;
		}
	}

	/**
	 * 
	 * @param in
	 * @return
	 */

	public static String makeNavbar(int current, int total, int per_page,
			String action) {
		int begin = 1;
		int border = 100;
		String prevLink = "";
		String nextLink = "";
		String rslt = "";

		String strNext = "Next";
		String strPrev = "Previous";
		String strStart = "";
		if (action.indexOf("?") < 0) {
			strStart = "?start=";
		} else {
			strStart = "&start=";

		}
		Hashtable ht = new Hashtable();
		ht.put("action", action);
		begin = current + 1;
		int j = 0;
		while (j * border < begin) {
			j++;

		}
		begin = (((j - 1) * border) + 1);

		if (current + 1 > border) {
			prevLink = "<a href=" + action + strStart
					+ Math.max(1, begin - border) + ">[ " + strPrev + " "
					+ border + " ]</a>&nbsp;";

		}
		while (begin < (j * border) && begin < total + 1) {
			ht.put("from", String.valueOf(begin));
			ht.put("to", String.valueOf(Math.min(total, begin + per_page - 1)));

			if (current + 1 >= begin && current + 1 <= begin + per_page - 1) {
				rslt += fillValuesToString("[$from - $to]&nbsp;", ht);
			} else {
				rslt += fillValuesToString("<a href='$action" + strStart
						+ "$from'>[$from - $to]</a>&nbsp;", ht);
			}
			begin += per_page;
		}

		if (total >= begin) {
			nextLink = "&nbsp;<a href=" + action + strStart + begin + ">[ "
					+ strNext + " " + Math.min(border, total - begin + 1)
					+ " ]</a>";
		}
		return prevLink + rslt + nextLink;
	}

	/**
	 * 
	 * @param current
	 * @param total
	 * @param per_page
	 * @param action
	 * @return
	 */
	public static String makeNavbarReverse(int current, int total,
			int per_page, String action) {
		int border = 100;
		String prevLink = "";
		String nextLink = "";
		String rslt = "";
		int begin = current + 1;
		Hashtable ht = new Hashtable();
		ht.put("action", action);
		String strNext = "Next";
		String strPrev = "Previous";
		String strStart = "";
		if (action.indexOf("?") < 0) {
			strStart = "?start=";
		} else {
			strStart = "&start=";

		}
		int j = 0;
		while (j * border < begin) {
			j++;
		}
		begin = ((j - 1) * border) + 1;
		if (begin > border) {
			prevLink = "<a href=" + action + strStart
					+ Math.max(1, begin - border) + ">[ " + strNext + " "
					+ border + " ]</a>&nbsp;";
		}
		current++;
		for (int i = 0; i < per_page && begin <= total; i++) {
			ht.put("from", String.valueOf(total - begin + 1));
			ht.put("to", String.valueOf(Math.max(1, total - begin + 1 + 1
					- per_page)));
			if (current >= begin && current <= begin + per_page - 1) {
				rslt += fillValuesToString("[$from - $to]&nbsp;", ht);
			} else {
				rslt += fillValuesToString(
						"<a href='$action" + strStart + String.valueOf(begin)
								+ "'>[ $from - $to ]</a>&nbsp;", ht);
			}
			begin += per_page;
		}
		if (total > begin) {
			nextLink = "&nbsp;<a href=" + action + strStart
					+ String.valueOf(begin) + ">[ " + strPrev + " "
					+ Math.min(total - begin + 1, 100) + " ]</a>";
		}
		return prevLink + rslt + nextLink;
	}

	/**
	 * 字符串处理方法：
	 * 
	 * @param str
	 * @param ht
	 * @return
	 */
	public static String fillValuesToString(String str, Hashtable ht) {
		char VARIABLE_PREFIX = '$';
		char TERMINATOR = '\\';

		if (str == null || str.length() == 0 || ht == null) {
			return str;
		}

		char s[] = str.toCharArray();
		char ch, i = 0;
		String vname;
		StringBuffer buf = new StringBuffer();

		ch = s[i];
		while (true) {
			if (ch == VARIABLE_PREFIX) {
				vname = "";
				if (++i < s.length) {
					ch = s[i];
				} else {
					break;
				}
				while (true) {
					if (ch != '_' && ch != '-'
							&& !Character.isLetterOrDigit(ch)) {
						break;
					}
					vname += ch;
					if (++i < s.length) {
						ch = s[i];
					} else {
						break;
					}
				}

				if (vname.length() != 0) {
					String vval = (String) ht.get(vname);
					if (vval != null) {
						buf.append(vval);
					}
				}
				if (vname.length() != 0 && ch == VARIABLE_PREFIX) {
					continue;
				}
				if (ch == TERMINATOR) {
					if (++i < s.length) {
						ch = s[i];
					} else {
						break;
					}
					continue;
				}
				if (i >= s.length) {
					break;
				}
			}

			buf.append(ch);
			if (++i < s.length) {
				ch = s[i];
			} else {
				break;
			}
		}
		return buf.toString();
	}

	public static char getSeparator() {
		return 2;
	}

	/**
	 * 时间处理方法：
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static String addTime(String time1, String time2) {
		if (time1.equals("") || time2.equals("")) {
			return "00:00";
		} else {
			ArrayList timearray1 = TokenizerString(time1, ":");
			ArrayList timearray2 = TokenizerString(time2, ":");
			int hour1;
			int hour2;
			int min1;
			int min2;
			int hour;
			int min;
			hour1 = getIntValue((String) timearray1.get(0));
			min1 = getIntValue((String) timearray1.get(1));
			hour2 = getIntValue((String) timearray2.get(0));
			min2 = getIntValue((String) timearray2.get(1));
			if ((min1 + min2) >= 60) {
				hour = hour1 + hour2 + 1;
				min = min1 + min2 - 60;
			} else {
				hour = hour1 + hour2;
				min = min1 + min2;
			}
			if (hour < 10) {
				if (min < 10) {
					return "0" + hour + ":" + "0" + min;
				} else {
					return "0" + hour + ":" + "" + min;
				}
			} else {
				if (min < 10) {
					return "" + hour + ":" + "0" + min;
				} else {
					return "" + hour + ":" + "" + min;
				}
			}
		}
	}

	/**
	 * 时间处理方法：
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static String subTime(String time1, String time2) {
		if (time1.equals("") || time2.equals("")) {
			return "00:00";
		} else {
			ArrayList timearray1 = TokenizerString(time1, ":");
			ArrayList timearray2 = TokenizerString(time2, ":");
			int hour1;
			int hour2;
			int min1;
			int min2;
			int hour;
			int min;
			hour1 = getIntValue((String) timearray1.get(0));
			min1 = getIntValue((String) timearray1.get(1));
			hour2 = getIntValue((String) timearray2.get(0));
			min2 = getIntValue((String) timearray2.get(1));
			if ((min1 - min2) < 0) {
				hour = hour1 - hour2 - 1;
				min = min1 - min2 + 60;
			} else {
				hour = hour1 - hour2;
				min = min1 - min2;
			}
			if (hour < 10) {
				if (min < 10) {
					return "0" + hour + ":" + "0" + min;
				} else {
					return "0" + hour + ":" + "" + min;
				}
			} else {
				if (min < 10) {
					return "" + hour + ":" + "0" + min;
				} else {
					return "" + hour + ":" + "" + min;
				}
			}
		}
	}

	
	/**
	 * 获得每dimlen位的逗号
	 * 
	 * @param str
	 * @param dimlen
	 * @return
	 */
	public static String getFloatStr(String str, int dimlen) {
		int dicimalindex = str.indexOf(".");
		String decimalstr = "";
		if (dicimalindex != -1) {
			decimalstr = extract(str, ".", null);
		}
		String intstr = extract(str, null, ".");
		if (intstr.length() < (dimlen + 1)) {
			return str;
		}
		String beginstr = "";
		int thebeginlen = intstr.length() % dimlen;
		beginstr = intstr.substring(0, thebeginlen);
		intstr = intstr.substring(thebeginlen);
		int intstrcount = intstr.length() / dimlen;

		for (int i = 0; i < intstrcount; i++) {
			if (beginstr.equals("") || beginstr.equals("-")) {
				beginstr += intstr.substring(0, dimlen);
				intstr = intstr.substring(dimlen);
			} else {
				beginstr += "," + intstr.substring(0, dimlen);
				intstr = intstr.substring(dimlen);
			}
		}
		if (dicimalindex != -1) {
			return beginstr + "." + decimalstr;
		} else {
			return beginstr;
		}
	}

	/**
	 * 将字符串日期转成日历型
	 * 
	 * @param strDate
	 * @return
	 */
	public static GregorianCalendar String2Cal(String strDate) {
		GregorianCalendar calDate = new GregorianCalendar();
		strDate = StaticMehtod.null2String(strDate).replaceAll("\\.0", "");
		Vector vecDate = StaticMehtod.getVector(strDate, " ");
		if (vecDate.size() > 0 && vecDate.size() < 3) {
			if (vecDate.size() == 1) {
				Vector vecData = StaticMehtod.getVector(String.valueOf(vecDate
						.elementAt(0)), "-");
				if (vecData.size() == 3) {
					int intYear = Integer.parseInt(String.valueOf(vecData
							.elementAt(0)));
					int intMonth = Integer.parseInt(String.valueOf(vecData
							.elementAt(1)));
					int intDay = Integer.parseInt(String.valueOf(vecData
							.elementAt(2)));
					calDate.set(Calendar.YEAR, intYear);
					calDate.set(Calendar.MONTH, intMonth - 1);
					calDate.set(Calendar.DATE, intDay);
				}
				calDate.set(Calendar.HOUR_OF_DAY, 0);
				calDate.set(Calendar.MINUTE, 0);
				calDate.set(Calendar.SECOND, 0);
			}
			if (vecDate.size() == 2) {
				Vector vecData = StaticMehtod.getVector(String.valueOf(vecDate
						.elementAt(0)), "-");
				Vector vecTime = StaticMehtod.getVector(String.valueOf(vecDate
						.elementAt(1)), ":");
				if (vecData.size() == 3) {
					int intYear = Integer.parseInt(String.valueOf(vecData
							.elementAt(0)));
					int intMonth = Integer.parseInt(String.valueOf(vecData
							.elementAt(1)));
					int intDay = Integer.parseInt(String.valueOf(vecData
							.elementAt(2)));
					calDate.set(Calendar.YEAR, intYear);
					calDate.set(Calendar.MONTH, intMonth - 1);
					calDate.set(Calendar.DATE, intDay);
				}
				if (vecTime.size() == 3) {
					int intHour = Integer.parseInt(String.valueOf(vecTime
							.elementAt(0)));
					int intMinute = Integer.parseInt(String.valueOf(vecTime
							.elementAt(1)));
					int intSecond = Integer.parseInt(String.valueOf(vecTime
							.elementAt(2)));
					calDate.set(Calendar.HOUR_OF_DAY, intHour);
					calDate.set(Calendar.MINUTE, intMinute);
					calDate.set(Calendar.SECOND, intSecond);
				}
			}
		}
		return calDate;
	}

	/**
	 * 将日历型转成日期字符串
	 * 
	 * @param calDate
	 * @return
	 */
	public static String Cal2String(GregorianCalendar calDate) {
		String strDate = "";
		strDate = String.valueOf(calDate.get(Calendar.YEAR));
		strDate = strDate + "-"
				+ String.valueOf(calDate.get(Calendar.MONTH) + 1);
		strDate = strDate + "-" + String.valueOf(calDate.get(Calendar.DATE));
		strDate = strDate + " "
				+ String.valueOf(calDate.get(Calendar.HOUR_OF_DAY));
		strDate = strDate + ":" + String.valueOf(calDate.get(Calendar.MINUTE));
		strDate = strDate + ":" + String.valueOf(calDate.get(Calendar.SECOND));
		return strDate;
	}

	/**
	 * @see 得到时间字符串
	 * @see 例如：StaticMehtod.getDateString(-1),可以返回昨天的时间字符串
	 * @param disday
	 *            int 和当前距离的天数
	 * @return String para的标准时间格式的串 例如：返回'2003-8-10'
	 */
	public static String getDateString(int disday) {
		String ls_display = "";
		Calendar c;
		c = Calendar.getInstance();
		long realtime = c.getTimeInMillis();
		realtime += 86400000 * disday;
		c.setTimeInMillis(realtime);
		String _yystr = "", _mmstr = "", _ddstr = "";
		int _yy = c.get(Calendar.YEAR);
		_yystr = _yy + "";
		int _mm = c.get(Calendar.MONTH) + 1;
		_mmstr = _mm + "";
		if (_mm < 10) {
			_mmstr = "0" + _mm;
		}
		int _dd = c.get(Calendar.DATE);
		_ddstr = _dd + "";
		if (_dd < 10) {
			_ddstr = "0" + _dd;
		}
		ls_display = "'" + _yystr + "-" + _mmstr + "-" + _ddstr + "'";
		return ls_display;
	}

	/**
	 * @see 得到时间字符串
	 * @see 例如：StaticMehtod.getDateString(-1),可以返回昨天的时间字符串
	 * @param disday
	 *            int 和当前距离的天数
	 * @return String para的标准时间格式的串 例如：返回'2003-8-10'
	 */
	public static String getDateString(String strDateLimit, int disday) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date thisdate = dateFormat.parse(strDateLimit);
			c.setTime(thisdate);
		} catch (Exception e) {
		}
		c.set(Calendar.DATE, c.get(Calendar.DATE) + disday);
		String ls_display = dateFormat.format(c.getTime()).toString();
		return ls_display;
	}

	/**
	 * @see 得到时间字符串
	 * @see 例如：StaticMehtod.getDateString(-1),可以返回昨天的时间字符串
	 * @param disday
	 *            int 和当前距离的天数
	 * @return String para的标准时间格式的串,例如：返回'2003-8-10 00:00:00'
	 */

	public static String getTimeString(int disday) {
		String ls_display = "";
		Calendar c;
		c = Calendar.getInstance();
		long realtime = c.getTimeInMillis();
		realtime += 86400000 * disday;
		c.setTimeInMillis(realtime);
		int _yy = c.get(Calendar.YEAR);

		int _mm = c.get(Calendar.MONTH) + 1;

		int _dd = c.get(Calendar.DATE);

		ls_display = "'" + _yy + "-" + _mm + "-" + _dd + " 00:00:00'";
		return ls_display;
	}
	
	/**
	 * @see 得到时间字符串
	 * @see 例如：StaticMehtod.getDateString(-1),可以返回昨天的时间字符串
	 * @param disday
	 *            int 和当前距离的天数
	 * @return String para的标准时间格式的串,例如：返回2003-8-10 00:00:00
	 */

	public static String getCurrentTimeString(int disday) {
		String ls_display = "";
		Calendar c;
		c = Calendar.getInstance();
		long realtime = c.getTimeInMillis();
		realtime += 86400000 * disday;
		c.setTimeInMillis(realtime);
		int _yy = c.get(Calendar.YEAR);

		int _mm = c.get(Calendar.MONTH) + 1;

		int _dd = c.get(Calendar.DATE);

		ls_display = _yy + "-" + _mm + "-" + _dd + " 00:00:00";
		return ls_display;
	}

	/**
	 * @see 得到某一天开始的时间字符串
	 * @see 例如：StaticMehtod.getDateString(-1),可以返回昨天的时间字符串
	 * @param disday
	 *            int 和当前距离的天数
	 * @return String para的标准时间格式的串,例如：返回'2003-8-10 00:00:00'
	 */

	public static String getTimeBeginString(int disday) {
		String ls_display = "";
		Calendar c;
		c = Calendar.getInstance();
		long realtime = c.getTimeInMillis();
		realtime += 86400000 * disday;
		c.setTimeInMillis(realtime);
		int _yy = c.get(Calendar.YEAR);
		int _mm = c.get(Calendar.MONTH) + 1;
		int _dd = c.get(Calendar.DATE);
		ls_display = _yy + "-" + _mm + "-" + _dd + " 00:00:00";
		return ls_display;
	}

	/**
	 * @see 得到某一天结束的时间字符串
	 * @see 例如：StaticMehtod.getDateString(-1),可以返回昨天的时间字符串
	 * @param disday
	 *            int 和当前距离的天数
	 * @return String para的标准时间格式的串,例如：返回'2003-8-10 00:00:00'
	 */

	public static String getTimeEndString(int disday) {
		String ls_display = "";
		Calendar c;
		c = Calendar.getInstance();
		long realtime = c.getTimeInMillis();
		realtime += 86400000 * disday;
		c.setTimeInMillis(realtime);
		int _yy = c.get(Calendar.YEAR);
		int _mm = c.get(Calendar.MONTH) + 1;
		int _dd = c.get(Calendar.DATE);
		ls_display = _yy + "-" + _mm + "-" + _dd + " 23:59:59";
		return ls_display;
	}

	/**
	 * @see 得到时间字符串
	 * @see 例如：StaticMehtod.getTimeString(-1,16),可以返回昨天的时间字符串
	 * @param disday
	 *            int 和当前距离的天数
	 * @return String para的标准时间格式的串,例如：返回'2003-8-10 16:00:00'
	 */

	public static String getTimeString(int disday, int hour) {
		String ls_display = "";
		Calendar c;
		c = Calendar.getInstance();
		long realtime = c.getTimeInMillis();
		realtime += 86400000 * disday;
		c.setTimeInMillis(realtime);
		String _hourstr = "";
		int _yy = c.get(Calendar.YEAR);

		int _mm = c.get(Calendar.MONTH) + 1;

		int _dd = c.get(Calendar.DATE);

		if (hour < 10) {
			_hourstr = "0" + hour;
		} else {
			_hourstr = "" + hour;
		}
		ls_display = "'" + _yy + "-" + _mm + "-" + _dd + " " + _hourstr
				+ ":00:00'";
		return ls_display;
	}

	/**
	 * 时间处理方法
	 * 
	 * @param disday
	 * @return
	 */
	public static Timestamp getTimestamp(int disday) {
		Calendar c;
		c = Calendar.getInstance();
		long realtime = c.getTimeInMillis();
		realtime += 86400000 * disday;
		return new Timestamp(realtime);
	}

	/**
	 * 时间串分割函数，将时间串分割成年、月、日、小时、分、秒。
	 * 
	 * @param time
	 * @return
	 */
	public static String[] subTimeString(String time) {

		String[] timeString = time.split(" ");
		String[] time1 = timeString[0].split("-");
		String[] time2 = timeString[1].split(":");
		int length = time1.length + time2.length;
		String[] Time = new String[length];

		for (int i = 0; i < time1.length; i++) {
			Time[i] = time1[i];
		}
		for (int j = 0; j < time2.length; j++) {
			Time[time1.length + j] = time2[j];
		}
		return Time;
	}

	/**
	 * 时间处理方法
	 * 
	 * @param str
	 * @return
	 */
	public static Timestamp getTimestamp(String str) {
		Timestamp ret = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			Date date = dateFormat.parse(str);
			long datelong = date.getTime();
			ret = new Timestamp(datelong);

		} catch (Exception e) {
		}
		return ret;
	}
	
	
	/**
	 * 时间处理方法
	 * 
	 * @param str
	 * @return
	 */
	public static String date2String(Date date) {
		String ret = "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			ret = dateFormat.format(date);
		} catch (Exception e) {
		}
		return ret;
	}

	public static Timestamp getTimestamp() {
		return getTimestamp(0);
	}

	public static String getTimestampString(Timestamp sta) {
		String ret = "";
		try {
			String str = sta.toString();
			ret = str.substring(0, str.lastIndexOf('.'));
		} catch (Exception e) {
			ret = "";
		}
		return ret;
	}

	/**
	 * @see 由运维系统发送工单确认信息，到客服系统
	 * @param operateId
	 *            KF工单流水号
	 * @param dealId
	 *            KF工单派单号
	 * @param userId
	 *            EOMS处理人
	 * @param dealDept
	 *            EOMS处理部门 根据编码表获取KF 的部门ID
	 * @param contact
	 *            EOMS联系方式
	 * @return XML格式字符串 <?xml version="1.0" encoding="UTF-8"?>
	 *         <SOAP-ENV:Envelope
	 *         xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
	 *         xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/"
	 *         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	 *         xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	 *         xmlns:ns="urn:KFWebService"> <SOAP-ENV:Body
	 *         encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
	 *         <ns:CONFIRM-CPLT> <CONFIRM-CPLT> <FLOW-NO></FLOW-NO> <DEAL-ID></DEAL-ID>
	 *         <PROCESS-UNIT></PROCESS-UNIT> <PROCESS-NAME></PROCESS-NAME>
	 *         <CONTACTOR-TEL></CONTACTOR-TEL> </CONFIRM-CPLT>
	 *         </ns:CONFIRM-CPLT> </SOAP-ENV:Body> </SOAP-ENV:Envelope>
	 */

	public static String confirmMSGEOMS21860(String operateId, String dealId,
			String userId, int dealDept, String contact) {

		String xmlComfirmString = "";

		xmlComfirmString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		xmlComfirmString = xmlComfirmString
				+ "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"";
		xmlComfirmString = xmlComfirmString
				+ " xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"";
		xmlComfirmString = xmlComfirmString
				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
		xmlComfirmString = xmlComfirmString
				+ " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"  xmlns:ns=\"urn:KFWebService\">";
		xmlComfirmString = xmlComfirmString
				+ "<SOAP-ENV:Body encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding\">";
		xmlComfirmString = xmlComfirmString + "<ns:CONFIRM-CPLT>";
		xmlComfirmString = xmlComfirmString + "<CONFIRM-CPLT>";
		xmlComfirmString = xmlComfirmString + "<FLOW-NO>" + operateId.trim()
				+ "</FLOW-NO>";
		xmlComfirmString = xmlComfirmString + "<DEAL-ID>" + dealId.trim()
				+ "</DEAL-ID>";
		xmlComfirmString = xmlComfirmString + "<PROCESS-UNIT>" + dealDept
				+ "</PROCESS-UNIT>";
		xmlComfirmString = xmlComfirmString + "<PROCESS-NAME>" + userId
				+ "</PROCESS-NAME>";
		xmlComfirmString = xmlComfirmString + "<CONTACTOR-TEL>"
				+ contact.trim() + "</CONTACTOR-TEL>";
		xmlComfirmString = xmlComfirmString + "</CONFIRM-CPLT>";
		xmlComfirmString = xmlComfirmString + "</ns:CONFIRM-CPLT>";
		xmlComfirmString = xmlComfirmString + "</SOAP-ENV:Body>";
		xmlComfirmString = xmlComfirmString + "</SOAP-ENV:Envelope>";

		return xmlComfirmString;
	}

	/**
	 * @see 由运维系统发送工单回复信息，到客服系统
	 * @param operateId
	 *            KF工单流水号
	 * @param dealID
	 *            KF 派单号
	 * @param userId
	 *            处理人
	 * @param dealDept
	 *            处理部门更具编码表获取KF 的部门ID
	 * @param proTime
	 *            处理时间
	 * @param finallyResult
	 *            最终处理结果。如果是退回，可以为空。
	 * @param rejectCause
	 *            故障原因可以为空
	 * @param Status
	 *            对应运维EOMS工单主要有两个回复操作，处理结束（status = 0）和处理未结束(status=1)
	 * @param areaCode
	 *            地区代码
	 * @return XML格式化字符串
	 */
	public static String returnMSGEOMS21860(String operateId, String dealId,
			String userId, int dealDept, String proTime, String finallyResult,
			String rejectCause, int status, int areaCode) {

		String xmlReturnString = "";

		xmlReturnString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		xmlReturnString = xmlReturnString
				+ "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"";
		xmlReturnString = xmlReturnString
				+ " xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"";
		xmlReturnString = xmlReturnString
				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
		xmlReturnString = xmlReturnString
				+ " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"  xmlns:ns=\"urn:KFWebService\">";
		xmlReturnString = xmlReturnString
				+ "<SOAP-ENV:Body encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding\">";
		xmlReturnString = xmlReturnString + "<ns:FILL-PRCS-RSLT>";
		xmlReturnString = xmlReturnString + "<PRCS-RSLT>";
		xmlReturnString = xmlReturnString + "<FLOW-NO>" + operateId.trim()
				+ "</FLOW-NO>";
		xmlReturnString = xmlReturnString + "<DEAL-ID>" + dealId.trim()
				+ "</DEAL-ID>";
		xmlReturnString = xmlReturnString + "<PROCESS-UNIT>" + dealDept
				+ "</PROCESS-UNIT>";
		xmlReturnString = xmlReturnString + "<PROCESS-NAME>" + userId.trim()
				+ "</PROCESS-NAME>";
		xmlReturnString = xmlReturnString + "<PROCESS-TIME>" + proTime.trim()
				+ "</PROCESS-TIME>";
		xmlReturnString = xmlReturnString + "<PROCESS-RESULT>" + finallyResult
				+ "</PROCESS-RESULT>";
		/*
		 * if(status ==0) { xmlReturnString = xmlReturnString + "<PROCESS-RESULT>"+finallyResult+"</PROCESS-RESULT>";
		 * }else if(status == 1) { xmlReturnString = xmlReturnString + "<PROCESS-RESULT>"+rejectCause+"</PROCESS-RESULT>";
		 * }else { xmlReturnString = xmlReturnString + "<PROCESS-RESULT></PROCESS-RESULT>"; }
		 */
		xmlReturnString = xmlReturnString + "<AREA-CODE>" + areaCode
				+ "</AREA-CODE>";
		xmlReturnString = xmlReturnString + "<END-HANDLE-FLAG>" + status
				+ "</END-HANDLE-FLAG>";
		xmlReturnString = xmlReturnString + "</PRCS-RSLT>";
		xmlReturnString = xmlReturnString + "</ns:FILL-PRCS-RSLT>";
		xmlReturnString = xmlReturnString + "</SOAP-ENV:Body>";
		xmlReturnString = xmlReturnString + "</SOAP-ENV:Envelope>";

		return xmlReturnString;
	}

	/**
	 * @see 得到当前时刻的时间字符串
	 * @return String para的标准时间格式的串,例如：返回'2003-08-09 16:00:00'
	 */
	public static String getLocalString() {
		java.util.Date currentDate = new java.util.Date();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(currentDate);

		return date;
	}

	/**
	 * 时间处理方法：
	 * 
	 * @param day
	 * @return
	 */
	public static String getLocalString(int day) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, day);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(c.getTime());

		return date;
	}

	/**
	 * 时间处理方法
	 * 
	 * @param hour
	 * @return
	 */
	public static String getLocalStringByHours(int hour) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, hour);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(c.getTime());

		return date;
	}

	/**
	 * @see 根据参数str,得到标准的时间字符串
	 * @param str
	 * @returnpara的标准时间格式的串,例如：返回'2003-08-09 16:00:00'
	 */
	public static String getLocalString(String str) {
		String time = "";

		Vector temp = StaticMehtod.getVector(str, " ");

		if (!temp.isEmpty()) {
			Vector first = StaticMehtod.getVector(temp.get(0).toString(), "-");
			Vector last = StaticMehtod.getVector(temp.get(1).toString(), ":");

			String year = first.get(0).toString();
			;
			String month = first.get(1).toString();
			;
			String day = first.get(2).toString();
			String hour = last.get(0).toString();
			String minute = last.get(1).toString();
			String second = last.get(2).toString();

			if (month.length() == 1) {
				month = "0" + month;
			}
			if (day.length() == 1) {
				day = "0" + day;
			}
			if (hour.length() == 1) {
				hour = "0" + hour;
			}
			if (minute.length() == 1) {
				minute = "0" + minute;
			}
			if (second.length() == 1) {
				second = "0" + second;

			}
			time = year + "-" + month + "-" + day + " " + hour + ":" + minute
					+ ":" + second;
		}

		return time;
	}

	/**
	 * 时间转换方法
	 * 
	 * @param disday
	 * @param hour
	 * @return
	 */
	public static String getLocalString(int disday, int hour) {
		String ls_display = "";
		Calendar c;
		c = Calendar.getInstance();
		long realtime = c.getTimeInMillis();
		realtime += 86400000 * disday;
		c.setTimeInMillis(realtime);
		String _mmstr = "", _ddstr = "", _hourstr = "";
		int _yy = c.get(Calendar.YEAR);
		int _mm = c.get(Calendar.MONTH) + 1;
		_mmstr = _mm + "";
		if (_mm < 10) {
			_mmstr = "0" + _mm;
		}
		int _dd = c.get(Calendar.DATE);
		_ddstr = _dd + "";
		if (_dd < 10) {
			_ddstr = "0" + _dd;
		}
		if (hour < 10) {
			_hourstr = "0" + hour;
		} else {
			_hourstr = "" + hour;

		}
		ls_display = _yy + "-" + _mmstr + "-" + _ddstr + " " + _hourstr
				+ ":00:00";
		return ls_display;
	}

	/**
	 * @see 转换时间方法：转换格式，如时间“2002-1-12”转换成字符串“020112”
	 * @param DateStr
	 *            “2002-1-12”
	 * @return “020112”
	 */
	public static String getYYMMDD() {
		return getYYMMDD(getCurrentDateTime());
	}

	public static String getYYMMDD(String DateStr) {
		String YY, MM, DD;
		String ReturnDateStr;

		int s = DateStr.indexOf(" ");
		ReturnDateStr = DateStr.substring(0, s);

		Vector ss = getVector(ReturnDateStr, "-");
		YY = ss.elementAt(0).toString();
		YY = YY.substring(2, 4);
		MM = ss.elementAt(1).toString();
		if (Integer.valueOf(MM).intValue() < 10) {
			MM = "0" + Integer.valueOf(MM).intValue();
		}

		DD = ss.elementAt(2).toString();
		if (Integer.valueOf(DD).intValue() < 10) {
			DD = "0" + Integer.valueOf(DD).intValue();
		}
		ReturnDateStr = YY + MM + DD;

		return ReturnDateStr;
	}

	/**
	 * 时间处理方法：返回日期格式：20051201
	 * 
	 * @param DateStr
	 * @return
	 */
	public static String getYYYYMMDD(String DateStr) {

		String YY, MM, DD;
		String ReturnDateStr;

		int s = DateStr.indexOf(" ");
		ReturnDateStr = DateStr.substring(0, s);

		Vector ss = getVector(ReturnDateStr, "-");
		YY = ss.elementAt(0).toString();
		// YY = YY.substring(2, 4);
		MM = ss.elementAt(1).toString();
		DD = ss.elementAt(2).toString();

		ReturnDateStr = YY + MM + DD;

		return ReturnDateStr;
	}

	/**
	 * @see 时间转换方法：根据传入的分钟数,得到转化后的天，小时和分钟值
	 * @param times
	 *            int
	 * @return ArrayList 保存这些值的集合，0位置保存天，1位置保存小时，2位置保存分钟
	 */
	public static ArrayList getDHMString(long times) {
		long day;
		long hour;
		long minute;
		ArrayList list = new ArrayList();

		day = times / (24 * 60);
		if (day == 0) {

			hour = times / 60;
			minute = times - hour * 60;
		} else {
			hour = (times % (24 * 60)) / 60;
			minute = times - day * 24 * 60;
			if (hour != 0) {
				minute -= hour * 60;
			}
		}

		list.add(String.valueOf(day));
		list.add(String.valueOf(hour));
		list.add(String.valueOf(minute));
		return list;
	}

	/**
	 * 
	 * 时间转换方法：根据输入的格式(String _dtFormat)得到当前时间格式得到当前的系统时间 Add By ChengJiWu
	 * 
	 * @param _dtFormat
	 * @return
	 */
	public static String getCurrentDateTime(String _dtFormat) {
		String currentdatetime = "";
		try {
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dtFormat = new SimpleDateFormat(_dtFormat);
			currentdatetime = dtFormat.format(date);
		} catch (Exception e) {
			
		}
		return currentdatetime;
	}

	/**
	 * 时间转换方法：得到默认的时间格式为("yyyy-MM-dd HH:mm:ss")的当前时间
	 * 
	 * @return
	 */
	public static String getCurrentDateTime() {
		return getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * @param strDate
	 *            时间型的字符串
	 * @param _dtFormat
	 *            形如"yyyy-MM-dd HH:mm:ss"的字符串 把 strDate 时间字符串 转换为 _dtFormat 格式
	 * @return
	 */
	public static String getCurrentDateTime(String strDate, String _dtFormat) {
		String strDateTime;
		Date tDate = null;
		if (null == strDate) {
			return getCurrentDateTime();
		}
		SimpleDateFormat smpDateFormat = new SimpleDateFormat(_dtFormat);
		ParsePosition pos = new ParsePosition(0);
		tDate = smpDateFormat.parse(strDate, pos); // 标准格式的date类型时间
		strDateTime = smpDateFormat.format(tDate); // 标准格式的String 类型时间
		return strDateTime;
	}

	/**
	 * 
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	public static Vector unionAllVec(Vector vec1, Vector vec2) {
		boolean flag = false;

		int itemp = 0;
		for (int i = 0; i < vec2.size(); i++) {
			itemp = 0;
			flag = false;
			while ((itemp < vec1.size()) && (!flag)) {
				if (vec1.get(itemp).toString().equals(vec2.get(i).toString())) {
					flag = true;
				} else {
					itemp = itemp + 1;
				}
			}
			if (!flag) {
				vec1.add(vec2.get(i));
			}
		}

		return vec1;
	}

	/**
	 * @see 格式化字符串
	 * @param OriStr
	 *            分割符号为“，”情况下，“,Str1,Str2,Str3” 或 “Str1,Str2,Str3，”或
	 *            “，Str1,Str2,Str3，”等
	 * @param dim
	 *            分割符号，如“，”
	 * @return Str1,Str2,Str3
	 */
	public static String right(String str, int i) {
		String sc = "";
		if (!str.equals("")) {
			int strlength = str.length();
			int priostrlength = (strlength - i);
			sc = str.substring(priostrlength, strlength);
		}
		return sc;
	}

	/**
	 * @see 字符串处理方法
	 * 
	 * @param str
	 * @param i
	 * @return
	 */
	public static String left(String str, int i) {
		String sc = "";
		if (!str.equals("")) {
			int priostrlength = (i);
			sc = str.substring(0, priostrlength);
		}
		return sc;
	}

	/**
	 * @see 字符串处理方法
	 * 
	 * @param OriStr
	 * @param dim
	 * @return
	 */
	public static String getFormatStr(String OriStr, String dim) {
		// OriStr 不能为空；
		OriStr = null2String(OriStr, "");
		if (OriStr == "") {
			OriStr = "";
		} else {
			int dimLength = dim.length();
			if (left(OriStr, dimLength).equals(dim)) {
				OriStr = right(OriStr, (OriStr.length() - dimLength));
			}
			if (right(OriStr, dimLength).equals(dim)) {
				OriStr = left(OriStr, (OriStr.length() - dimLength));
			}
		}
		return OriStr;
	}

	/**
	 * @see 时间处理方法
	 * @return
	 */
	public static synchronized int getSingleId() {
		int ret = 0;
		Date date = new Date();
		ret = new Long(date.getTime()).intValue();
		if (ret < 0)
			ret = -ret;
		return ret;
	}

	/**
	 * @see 时间处理方法：for applysheet 2005-09-27 add by chenyuanshu
	 * 
	 * @see 将millonSecond转换成时:分:秒
	 * @param millonSecond
	 *            long
	 * @return String
	 */
	public static String convert(long millonSecond) {
		int totalsecond = (int) millonSecond / 1000;
		int hour, min, sec;
		String minStr = "", hourStr = "";
		/*
		 * day=totalsecond /(60*60*24); dayStr=String.valueOf(day);
		 */
		hour = (totalsecond) / (60 * 60);
		hourStr = String.valueOf(hour);
		min = (totalsecond - hour * 60 * 60) / 60;
		minStr = String.valueOf(min);
		sec = totalsecond - min * 60 - hour * 60 * 60;
		if (min == 0)
			minStr = "00";
		if (hour == 0)
			hourStr = "00";
		return hourStr + ":" + minStr + ":" + sec;
	}

	/**
	 * @see 计算步骤之间历时
	 * @throws Exception
	 *             输入参数：如下所示： 参数名称 参数类型 参数说明 备注 historyTime String 历史步骤时间
	 *             nowTime String 当前步骤时间 输出：步骤之间历时（单位小时）
	 */
	public static int getTimeIntervals(String historyTime, String nowTime) {
		String[] timeHistory = subTimeString(historyTime);
		String[] timeNow = subTimeString(nowTime);
		int year1 = StaticMehtod.getIntValue(timeHistory[0]);
		int year2 = StaticMehtod.getIntValue(timeNow[0]);
		int month1 = StaticMehtod.getIntValue(timeHistory[1]);
		int month2 = StaticMehtod.getIntValue(timeNow[1]);
		int day1 = StaticMehtod.getIntValue(timeHistory[2]);
		int day2 = StaticMehtod.getIntValue(timeNow[2]);
		int hour1 = StaticMehtod.getIntValue(timeHistory[3]);
		int hour2 = StaticMehtod.getIntValue(timeNow[3]);
		int min1 = StaticMehtod.getIntValue(timeHistory[4]);
		int min2 = StaticMehtod.getIntValue(timeNow[4]);
		int sec1 = StaticMehtod.getIntValue(timeHistory[5]);
		int sec2 = StaticMehtod.getIntValue(timeNow[5]);
		int flag, minutes;
		GregorianCalendar localCalendar = new GregorianCalendar(year1, month1,
				day1, hour1, min1, sec1);
		GregorianCalendar limitCalendar = new GregorianCalendar(year2, month2,
				day2, hour2, min2, sec2);
		Date localDate = localCalendar.getTime();
		Date limitDate = limitCalendar.getTime();
		flag = historyTime.compareTo(nowTime);
		if (flag <= 0) {
			long oddTime = limitDate.getTime() - localDate.getTime();
			minutes = (int) (oddTime / (60 * 60 * 1000));

		} else {
			long oddTime = localDate.getTime() - limitDate.getTime();
			minutes = (int) (oddTime / (60 * 60 * 1000));
			minutes = 0 - minutes;
		}
		return minutes;
	}

	/**
	 * @see 计算步骤之间历时
	 * @throws Exception
	 *             输入参数：如下所示： 参数名称 参数类型 参数说明 备注 historyTime String 历史步骤时间
	 *             nowTime String 当前步骤时间 输出：步骤之间历时（单位分钟）
	 */
	public static int getTimeDistance(String historyTime, String nowTime) {
		String[] timeHistory = subTimeString(historyTime);
		String[] timeNow = subTimeString(nowTime);
		int year1 = StaticMehtod.getIntValue(timeHistory[0]);
		int year2 = StaticMehtod.getIntValue(timeNow[0]);
		int month1 = StaticMehtod.getIntValue(timeHistory[1]);
		int month2 = StaticMehtod.getIntValue(timeNow[1]);
		int day1 = StaticMehtod.getIntValue(timeHistory[2]);
		int day2 = StaticMehtod.getIntValue(timeNow[2]);
		int hour1 = StaticMehtod.getIntValue(timeHistory[3]);
		int hour2 = StaticMehtod.getIntValue(timeNow[3]);
		int min1 = StaticMehtod.getIntValue(timeHistory[4]);
		int min2 = StaticMehtod.getIntValue(timeNow[4]);
		int sec1 = StaticMehtod.getIntValue(timeHistory[5]);
		int sec2 = StaticMehtod.getIntValue(timeNow[5]);
		int flag, minutes;
		GregorianCalendar localCalendar = new GregorianCalendar(year1, month1,
				day1, hour1, min1, sec1);
		GregorianCalendar limitCalendar = new GregorianCalendar(year2, month2,
				day2, hour2, min2, sec2);
		Date localDate = localCalendar.getTime();
		Date limitDate = limitCalendar.getTime();
		flag = historyTime.compareTo(nowTime);
		if (flag <= 0) {
			long oddTime = limitDate.getTime() - localDate.getTime();
			minutes = (int) (oddTime / (60 * 1000));

		} else {
			long oddTime = localDate.getTime() - limitDate.getTime();
			minutes = (int) (oddTime / (60 * 1000));
			minutes = 0 - minutes;
		}
		return minutes;
	}

	public static String calcHMS(long timeInSeconds) {
		long hours = 0;
		long minutes = 0;
		long seconds = 0;
		try {
			hours = timeInSeconds / 3600;
			timeInSeconds = timeInSeconds - (hours * 3600);
			minutes = timeInSeconds / 60;
			timeInSeconds = timeInSeconds - (minutes * 60);
			seconds = timeInSeconds;
		} catch (Exception e) {

			e.printStackTrace();
		}

		return hours + "小时 " + minutes + "分钟 " + seconds + "秒";
	}

	/**
	 * @see 转换时间的方法：
	 * @param timeInSeconds
	 * @param time2
	 * @return
	 */
	public static String calcHMS(long timeInSeconds, long time2) {
		long hours = 0;
		long minutes = 0;
		long seconds = 0;
		String ret = "";
		try {
			timeInSeconds = timeInSeconds / time2;
			hours = timeInSeconds / 3600;
			timeInSeconds = timeInSeconds - (hours * 3600);
			minutes = timeInSeconds / 60;
			timeInSeconds = timeInSeconds - (minutes * 60);
			seconds = timeInSeconds;

			ret = hours + "小时 " + minutes + "分钟 " + seconds + "秒";
		} catch (Exception e) {
			ret = "0";
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * @see 转换时间方法
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonthLastDay(int year, int month) {

		if (month == 2) {
			if (year % 4 == 0)
				return 29;
			else
				return 28;
		} else if (month == 4 || month == 6 || month == 9 || month == 11)
			return 30;
		else
			return 31;

	}

	/**
	 * @see 获取当前时间（Date类型）
	 * @author qinmin
	 * @return
	 */
	public static Date getLocalTime() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		return date;

	}

	/**
	 * 字符类型转换函数（String->long）
	 * 
	 * @author qinmin
	 * @param str
	 * @return
	 */
	public static long getLongValue(String str) {
		long i = 0;
		try {
			i = Long.parseLong(str);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	/**
	 * 取classpath路径
	 * 
	 * @return classpath路径
	 * @see StaticMehtod#getFilePath(String)
	 * @see StaticMehtod#getFileUrl(String)
	 */
	public final static String getClasspath() {
		String classpath = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath()
				+ "";

		return classpath.substring(1);
	}
	/**
	 * 判断是否为叶子节点
	 * @param leaf
	 * @return
	 */
	public static boolean isLeaf(Integer leaf){
		if(leaf.equals(new Integer(1)))
			return true;
		return false;
	}
	/**
	 * 判断是否为空
	 * @param values
	 * @return
	 */
    public static boolean hasValue( String values ){
		boolean flag = true;
		if( values == null || values.equals("") || values.equals("null")){
			flag = false;
		}
		return flag;
	}
    
    public static Date getDateForString(String dates){
    	Date date =null;
    	try {
    		if( dates != null && !dates.equals("")){
			 date = DateFormat.getInstance().parse(dates);
    		}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
    }
    
    public static String removeLastStr(String str, String removedStr) {
		String result = null;
		if (str.endsWith(removedStr)) {
			result = str.substring(0, str.length() - removedStr.length());
		}
		return result;
	}
    
    /**
     * 取业态NAME
     * @param type
     * @return
     */
    public static String getYetName(String type){
    	String name = "其它";
    	if( type.equalsIgnoreCase("B")){
    		name = "便利店";
    	}else if( type.equalsIgnoreCase("C")){
    		name = "超市";
    	}else if( type.equalsIgnoreCase("F")){
    		name = "服务与餐饮业";
    	}else if( type.equalsIgnoreCase("Y")){
    		name = "烟草专卖店";
    	}else if( type.equalsIgnoreCase("S")){
    		name = "商场";
    	}else if( type.equalsIgnoreCase("Z")){
    		name = "杂货店";
    	}else if( type.equalsIgnoreCase("q")){
    		name = "其它";
    	}
    	
    	return name;
    }
}
