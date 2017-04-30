package org.hpin.common.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * @author : 胡五音
 * 
 *         特殊转换，规避NullException
 */

public class StaticMethod {

	  
	public final static String CLASSPATH_FLAG = "classpath:";
	
	public final static URL URI = StaticMethod.class.getResource("/");

	public final static int DEFAULT_DECIMAL_LEN = 10 ; 
	
	 static String CHARSET_PAGE ="UTF-8";
	 public static String CHARSET_DB = "UTF-8";
	 static String CHARSET_BEAN = "UTF-8";

	
	public static URL getFileUrl(String filePath) throws Exception {
		URL url = null;
		if (filePath != null) {
			if (filePath.length() > CLASSPATH_FLAG.length()) {
				if (CLASSPATH_FLAG.equals(filePath.substring(0, CLASSPATH_FLAG
						.length()))) {
					try {
					
						url = StaticMethod.class.getClassLoader().getResource(
								getPathButClasspath(filePath));
						URL url1 = StaticMethod.class
								.getResource(getPathButClasspath(filePath));
						URL url2 = Thread.currentThread()
								.getContextClassLoader().getResource(
										getPathButClasspath(filePath));

					} catch (Exception e) {
						throw new Exception(filePath
								+ "is not found.");
					}

				} else {
				}
			}
		}
		return url;
	}
	private static String getPathButClasspath(String path) {
		return path.substring(CLASSPATH_FLAG.length());
	}

	public static String getFilePath(String filePath) {
		if (filePath != null) {
			if (filePath.length() > CLASSPATH_FLAG.length()) {
				if (CLASSPATH_FLAG.equals(filePath.substring(0,
						CLASSPATH_FLAG.length()))) {
					filePath = getClasspath()
							+ filePath.substring(CLASSPATH_FLAG.length());
				}
			}
		}
		return filePath;
	}

	public final static String getClasspath() {

		return URI.getFile();

	}

	public static Date getLocalTime() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		return date;

	}
	public static String getClassespaths() {

		URL url = StaticMethod.class.getClassLoader().getResource("abc.xml");
		String classPathName = url.getPath();
		int endingIndex = classPathName.length() - "abc.xml".length();
		classPathName = classPathName.substring(0, endingIndex);
		return classPathName;
	}

	public static long null2Long(String s, long defaultValue) {

		long i = defaultValue;
		try {
			i = Long.parseLong(s);
		} catch (Exception e) {
			i = defaultValue;
		}
		return i;
	}

	public static int null2int(String s, int in) {
		int i = in;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			i = in;
		}
		return i;
	}

	public static int null2int(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	public static String null2String(String s, String s1) {
		return s == null ? s1 : s;
	}

	public static String null2String(String s) {
		return s == null ? "" : s;
	}

	public static String nullObject2String(Object s) {
		String str = "";
		try {
			str = s.toString();
		} catch (Exception e) {
			str = "";
		}
		return str;
	}

	public static String nullObject2String(Object s, String chr) {
		String str = chr;
		try {
			str = s.toString();
		} catch (Exception e) {
			str = chr;
		}
		return str;
	}

	public static String method2class(Method method) {
		String methodName = method.getName();
		String clz = method.getDeclaringClass().getName();
		Class paramTypes[] = method.getParameterTypes();
		String types = "";
		if (paramTypes != null && paramTypes.length > 0) {
			for (int i = 0; i < paramTypes.length; i++) {
				types += paramTypes[i].getName() + ",";
			}
			types = types.substring(0, types.length() - 1);
		}
		return clz + "." + methodName + "(" + types + ")";
	}

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

	public static Double nullObject2Double(Object s) {
		String str = "";
		Double d = 0d;
		try {
			str = s.toString();
			d = Double.parseDouble(str);
		} catch (Exception e) {
			d = 0d;
		}
		return d;
	}

	public static Vector list2vector(List list) {
		Vector vector = new Vector();
		if (null != list && !list.isEmpty()) {

			for (Iterator it = list.iterator(); it.hasNext();) {
				vector.add(it.next());
			}
		}
		return vector;
	}

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
	
	public static Integer long2Integer(Long num){
		 if(num != null ) return new Integer( num.intValue());
		 else return null ;
	}

	public static Integer nullObject2Integer(Object s) {
		return new Integer(StaticMethod.nullObject2int(s));
	}

	public static boolean fHasId(int id, ArrayList array) throws Exception {

		boolean ret = false;
		int i = 0;
		try {
			while (i < array.size() && ret == false) {
				if (id == StaticMethod.nullObject2int(array.get(i))) {
					ret = true;
				}
				i++;
			}
		} catch (Exception e) {
		}

		return ret;
	}

	public static boolean fHasId(String id, ArrayList array) throws Exception {

		boolean ret = false;
		int i = 0;
		try {
			while (i < array.size() && ret == false) {
				if (id == StaticMethod.nullObject2String(array.get(i))) {
					ret = true;
				}
				i++;
			}
		} catch (Exception e) {
		}

		return ret;
	}

	public static String StringReplace(String sou, String s1, String s2) {
		int idx = sou.indexOf(s1);
		if (idx < 0) {
			return sou;
		}
		return StringReplace(
				sou.substring(0, idx) + s2 + sou.substring(idx + s1.length()),
				s1, s2);
	}

	public static int getIntValue(String v) {
		return getIntValue(v, -1);
	}

	public static int getIntValue(String v, int def) {
		try {
			return Integer.parseInt(v);
		} catch (Exception ex) {
			return def;
		}
	}

	public static int getMonth(Date s, Date e) {
		if (s.after(e)) {
			Date t = s;
			s = e;
			e = t;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(s);
		Calendar end = Calendar.getInstance();
		end.setTime(e);
		Calendar temp = Calendar.getInstance();
		temp.setTime(e);
		temp.add(Calendar.DATE, 1);

		int y = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
		int m = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);

		if ((start.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {// 前后都不破月
			return y * 12 + m + 1;
		} else if ((start.get(Calendar.DATE) != 1)
				&& (temp.get(Calendar.DATE) == 1)) {// 前破月后不破月
			return y * 12 + m;
		} else if ((start.get(Calendar.DATE) == 1)
				&& (temp.get(Calendar.DATE) != 1)) {// 前不破月后破月
			return y * 12 + m;
		} else {// 前破月后破月
			return (y * 12 + m - 1) < 0 ? 0 : (y * 12 + m - 1);
		}
	}

	public static int getMonthDiff(Date s, Date e) {
		if (s.after(e)) {
			Date t = s;
			s = e;
			e = t;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(s);
		Calendar end = Calendar.getInstance();
		end.setTime(e);
		Calendar temp = Calendar.getInstance();
		temp.setTime(e);
		// temp.add(Calendar.DATE, 1);

		int y = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
		int endM = end.get(Calendar.MONTH), startM = start.get(Calendar.MONTH);
		int m = endM - startM + y * 12;

		if (start.get(Calendar.DAY_OF_MONTH) > end.get(Calendar.DAY_OF_MONTH) + 1) {
			m -= 1;
		}

		return m;
	}

	public static int getDayDiffFact(Date s, Date e) {

		long diff = e.getTime() - s.getTime();
		int d = (int) (diff / 1000 / 3600 / 24);

		return d;
	}

	public static int getDayDiff(Date s, Date e) {

		if (s.after(e)) {
			Date t = s;
			s = e;
			e = t;
		}
		long diff = e.getTime() - s.getTime();
		int d = (int) (diff / 1000 / 3600 / 24);

		return d;
	}

	public static int getMonthDayDiff(Date s, Date e) {
		if (s.after(e)) {
			Date t = s;
			s = e;
			e = t;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(s);
		Calendar end = Calendar.getInstance();
		end.setTime(e);

		int endD = end.get(Calendar.DAY_OF_MONTH), startD = start
				.get(Calendar.DAY_OF_MONTH);
		int d = endD - startD + 1;
		if (d < 0) {
			Calendar temp = Calendar.getInstance();
			temp.setTime(e);
			temp.add(Calendar.MONTH, -1);
			temp.set(Calendar.DAY_OF_MONTH, startD);
			long ms = end.getTime().getTime() - temp.getTime().getTime();
			d = (int) (ms / 1000 / 3600 / 24);// 1000*60*60*24
		}

		return d;
	}

	// 总共相差的天数
	public static long getDays(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}

	// 去掉月后相差的天数
	public static int getDay(Date s, Date e) {
		if (s.after(e)) {
			Date t = s;
			s = e;
			e = t;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(s);
		Calendar end = Calendar.getInstance();
		end.setTime(e);
		Calendar temp = Calendar.getInstance();
		temp.setTime(e);
		temp.add(Calendar.DATE, 1);

		if ((start.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {// 前后都不破月
			return 0;
		} else if ((start.get(Calendar.DATE) != 1)
				&& (temp.get(Calendar.DATE) == 1)) {// 前破月后不破月
			return getDayP(start);
		} else if ((start.get(Calendar.DATE) == 1)
				&& (temp.get(Calendar.DATE) != 1)) {// 前不破月后破月
			return end.get(Calendar.DATE);
		} else {// 前破月后破月
			if (start.get(Calendar.MONTH) == end.get(Calendar.MONTH)
					&& start.get(Calendar.YEAR) == end.get(Calendar.YEAR)) {
				return end.get(Calendar.DATE) - start.get(Calendar.DATE) + 1;
			} else {
				return getDayP(start) + end.get(Calendar.DATE);
			}
		}
	}

	public static int getDayP(Calendar s) {
		int d;
		if (s.get(Calendar.MONTH) == 1 && s.get(Calendar.YEAR) % 4 == 0
				&& s.get(Calendar.YEAR) % 100 != 0) {// 闰年2月
			d = 29;
		} else {
			Map<Integer, Integer> m = new HashMap<Integer, Integer>();
			m.clear();
			m.put(1, 31);
			m.put(3, 31);
			m.put(5, 31);
			m.put(7, 31);
			m.put(8, 31);
			m.put(10, 31);
			m.put(12, 31);
			m.put(4, 30);
			m.put(6, 30);
			m.put(9, 30);
			m.put(11, 30);
			m.put(2, 28);
			d = m.get(s.get(Calendar.MONTH) + 1);
		}
		return d - s.get(Calendar.DATE) + 1;
	}

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

	public static Timestamp nullObject2Timestamp(Object s) {
		Timestamp str = null;
		try {
			str = Timestamp.valueOf(s.toString());
		} catch (Exception e) {
			str = null;
		}
		return str;
	}

	public static String getTimeStr() {
		Date date = new Date();
		Random r = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = sdf.format(date) + String.valueOf(r.nextInt(100));
		return dateStr;
	}

	public static String getCurrentDateTime() {
		return getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	}

	public static Date string2Date(String strdate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(strdate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;

	}

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
	 * @see 字符处理方法：将首字符转换为大写
	 * @param string
	 * @return
	 */
	public static String firstToUpperCase(String string) {
		String post = string.substring(1, string.length());
		String first = ("" + string.charAt(0)).toUpperCase();
		return first + post;
	}

	public static String getMonthAndDaysBetween(Date stardate, Date enddate) {

		return StaticMethod.getMonthDiff(stardate, enddate) + "月零"
				+ StaticMethod.getDay(stardate, enddate) + "天";
	}

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

	public static String date2String(Date date, String strformat) {
		String ret = "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(strformat);
			ret = dateFormat.format(date);
		} catch (Exception e) {
		}
		return ret;
	}

	public static Date dateForMat(Date dates, String formatStr) {
		Date ret = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
			String datesa = dateFormat.format(dates);
			ret = dateFormat.parse(datesa);
		} catch (Exception e) {
		}
		return ret;
	}

	public static void mainaa(String[] str) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		try {
			int montsh = StaticMethod.getMonth(format.parse("2012-03-02"),
					format.parse("2012-06-01"));
			int days = StaticMethod.getMonthDayDiff(format.parse("2012-03-02"),
					format.parse("2012-06-01"));

			int dayss = StaticMethod.getDayDiff(format.parse("2012-03-02"),
					format.parse("2012-06-01")) + 1;

			System.out.println(montsh
					+ ":"
					+ days
					+ "---"
					+ dayss
					+ ":"
					+ StaticMethod.date2String(DateUtil.addDate(
							format.parse("2012-07-01"), 91)));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将源数据根据src的值做四舍五入.
	 * @param src  源数据
	 * @param decimal 保留小数位数
	 * @remark : 此方法对于一些精确度要求高的转换不能满足要求，标注为Deprecated
	 * @ Modified By 胡五音 于 2012-04-13
	 * @return
	 */
	@Deprecated()
	public static double convertFloatValue2Precision(Double src , Integer decimal ){
		if(src == null || decimal == null){
			return 0d ;
		}else{
			String format = "0." ;
			if(decimal <= 0){
				format = "0.00" ;
			}else{
  				while(decimal > 0){
					format += "0" ;
					decimal = decimal - 1 ;
				}
			}
			DecimalFormat df = new DecimalFormat(format) ;
			return nullObject2Double(df.format(src)) ;
		}
	}
	
	@Deprecated()
	public static String convertFloatValue2PrecisionEndAddZero(Double src , Integer decimal ){
		if(src == null || decimal == null){
			return "0" ;
		}else{
			NumberFormat nf = NumberFormat.getInstance() ;
			nf.setMinimumFractionDigits(decimal.intValue()) ;
			String value = nf.format(src) ;
			value = value.replaceAll("," , "") ;
			System.out.println(value) ;
			return value ;
		}
	}
	
	/**
	 * 将Data类型的日期转换为XMLGregorianCalendar类型
	 * @param d
	 * @return
	 */
	public static XMLGregorianCalendar getXmlDate(Date d) {
		GregorianCalendar cal = new GregorianCalendar();
		if (d == null)
			d = new Date();
		cal.setTime(d);
		XMLGregorianCalendar xcal = null;

		try {
			xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return xcal;
	}
	
	/**
	 * 对Double类型的数据进行精确度更高的四舍五入运算
	 * @param d 需要转换的数字
	 * @param scale 保留小数的位数
	 * @return
	 */
	public static Double formatDoubleValueByScale(Double d , int scale){
		Double data = Double.valueOf(d) ;
		BigDecimal bd = new BigDecimal(Double.toString(data)) ;
		BigDecimal m = new BigDecimal("1") ;
		BigDecimal _value = bd.divide(m , scale, BigDecimal.ROUND_HALF_UP) ;
		Double value = Double.valueOf(_value.doubleValue()) ;
		return value ; 
	}
	
	/**
	 * 获取一个浮点数，从最后一位开始四舍五入，知道被要求保留的小数位数时终止
	 * @param data 要进行四舍五入的浮点数
	 * @param scale 被要求精确的位数
	 * @return
	 */
	public static Double getSpecialDecimalValueByScale(Double data , int scale){
		int _decimaLen = getDecimalLength(data) ;
		if(_decimaLen <= scale) return data ;
		Double opeData = getDecimalDataByLength(data , _decimaLen) ;
		int _len = getDecimalLength(opeData) ;
		do{
			opeData = formatDoubleValueByScale(opeData , _len) ;
			_len -- ;
		}while(_len >= scale);
		return opeData ;
	}
	
	/**
	 * 将浮点数根据length截取，如果小数位数(length)过长，则默认取10位
	 * @param data
	 * @param length
	 * @return
	 */
	private static Double getDecimalDataByLength(Double data , int length){
		if(length > DEFAULT_DECIMAL_LEN) length = DEFAULT_DECIMAL_LEN ;
		return formatDoubleValueByScale(data, length) ;
	}
	
	/**
	 * 获取浮点小数的小数位数
	 * @param data
	 * @return
	 */
	private static int getDecimalLength(Double data){
		if(data == null) return 0 ;
		String dataStr = data.toString() ;
		String decimalStr = dataStr.substring(dataStr.indexOf(".") + 1) ;
		return decimalStr.length() ;
	}
	
	/**
	 * 将两个数值做除法，得到百分比
	 * @param data1 被除数
	 * @param data2 除数
	 * @return
	 */
	public static String getPercentNumber(Object data1 , Object data2){
		if(data1 == null || data2 == null) return "0.00%" ;
		Double d1 = nullObject2Double(data1) ;
		Double d2 = nullObject2Double(data2) ;
		if(d1.doubleValue() == 0 || d2.doubleValue() == 0) return "0.00%" ;
		String d = formatDoubleValueByScale((d1/d2) , 4) + "" ;
		return getPercentNumber(d)  ;
	}
	
	/**
	 * 将Object类型的数据转换为百分数
	 * @param data
	 * @return
	 */
	public static String getPercentNumber(Object obj){
		Double d = nullObject2Double(obj) ;
		if(d.doubleValue() == 0) return "0.00%" ;
		String result = formatDoubleValueByScale((d * 100),2) + "%" ;
		return result ;
	}
	
	/**
	 * 将参数map中的旧的key转换为新key，
	 * @param paramsMap
	 * @param oldKey
	 * @param newKey
	 * @param newParamMap
	 */
	public static void changParamsMapKey(Map<String, String> paramsMap, String oldKey ,String newKey, Map<String, String> newParamMap) {
		
		String paramValue = paramsMap.get(oldKey);
		if(paramValue !=null ){			 
			if(StringUtils.isNotBlank(paramValue)){	
				newParamMap.put(newKey, paramValue);
			}
		}
	}
	
	public static void main(String args[]) throws Exception{
		//Double d1 = Double.valueOf(6) ;
		//Double d2 = Double.valueOf(11) ;
		//Integer d1 = Integer.valueOf(2) ;
		//Integer d2 = Integer.valueOf(14) ;
		StaticMethod sm = new StaticMethod();
//		String date = "Tue Sep 10 19:58:11 CST 2013";
//		System.out.println(sm.) ;
//		Date d1 = DateUtils.convertDate("2012-08-12" , "yyyy-MM-dd") ;
//		Date d2 = DateUtils.convertDate("2012-07-12" , "yyyy-MM-dd") ;
//		System.out.println(getMonthDayDiff(d1 , d2)) ;
//		System.out.println(sm.gjString2Date("Tue Sep 10 19:58:11 CST 2013")+"/////");
//		System.out.println(StaticMethod.date2String(sm.gjString2Date("Tue Sep 10 19:58:11 CST 2013"), "yyyy-MM-dd"));
		
	}
	
	
	//国际化时间字符串转化为时间类型
	public static Date gjString2Date(String str) throws Exception{
		
		str = str.replace("Jan", "01");
		str = str.replace("Feb", "02");
		str = str.replace("Mar", "03");
		str = str.replace("Apr", "04");
		str = str.replace("May", "05");
		str = str.replace("Jun", "06");
		str = str.replace("Jul", "07");
		str = str.replace("Aug", "08");
		str = str.replace("Sep", "09");
		str = str.replace("Oct", "10");
		str = str.replace("Nov", "11");
		str = str.replace("Dec", "12");
		             
		SimpleDateFormat sdf = new SimpleDateFormat("EE MM dd HH:mm:ss 'CST' yyyy", Locale.US);
		Date date = sdf.parse(str);
		return date;
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
	    }
	    return reStr;
	  }
}
