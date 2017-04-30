package org.hpin.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hpin.common.core.SystemConstant;

/**
 * HTTP request工具类 thinkpad
 * 
 */
public class HttpTool {
	/**
	 * 获取参数
	 * 
	 * @param name
	 * @return
	 */
	public static String getParameter(String name) {
		HttpServletRequest request = ServletActionContext.getRequest();
		try{
			request.setCharacterEncoding("UTF-8") ;
		}catch(Exception e){
			System.out.println(e.getMessage()) ;
		}
		if (StringUtils.isBlank(name)) {
			throw new NullPointerException("参数名不能为空！");
		}

		return request.getParameter(name);
	}

	/**
	 * 获取参数
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static String getParameter(String name, String defaultValue) {
		String value = HttpTool.getParameter(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static Integer getIntegerParameter(String name) {
		String value = HttpTool.getParameter(name);
		if (StringUtils.isNotBlank(value)) {
			return Integer.parseInt(value);
		}
		return null;
	}

	public static Integer getIntegerParameter(String name, Integer defaultValue) {
		Integer value = HttpTool.getIntegerParameter(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static Long getLongParameter(String name) {
		String value = HttpTool.getParameter(name);
		if (StrUtils.isNotNullOrBlank(value)) {
			return Long.parseLong(value);
		}
		return null;
	}

	public static Long getLongParameter(String name, Long defaultValue) {
		Long value = HttpTool.getLongParameter(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static Short getShortParameter(String name) {
		String value = HttpTool.getParameter(name);
		if (StringUtils.isNotBlank(value)) {
			return Short.parseShort(value);
		}
		return null;
	}

	public static Short getShortParameter(String name, Short defaultValue) {
		Short value = HttpTool.getShortParameter(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static Float getFloatParameter(String name) {
		String value = HttpTool.getParameter(name);
		if (StringUtils.isNotBlank(value)) {
			return Float.parseFloat(value);
		}
		return null;
	}

	public static Float getFloatParameter(String name, Float defaultValue) {
		Float value = HttpTool.getFloatParameter(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static Double getDoubleParameter(String name) {
		String value = HttpTool.getParameter(name);
		if (StringUtils.isNotBlank(value)) {
			return Double.parseDouble(value);
		}
		return null;
	}

	public static Double getDoubleParameter(String name, Double defaultValue) {
		Double value = HttpTool.getDoubleParameter(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static Boolean getBooleanParameter(String name) {
		String value = HttpTool.getParameter(name);
		if (value != null) {
			Boolean reval = Boolean.valueOf(value);
			return reval;
		}
		return null;
	}

	public static Boolean getBooleanParameter(String name, Boolean defaultValue) {
		Boolean value = HttpTool.getBooleanParameter(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static String[] getParameterValues(String name) {
		HttpServletRequest request = ServletActionContext.getRequest();
		if (StringUtils.isBlank(name)) {
			throw new NullPointerException("参数名不能为空！");
		}

		return request.getParameterValues(name);
	}

	public static String[] getParameterValues(String name,
			String[] defaultValues) {
		String[] values = HttpTool.getParameterValues(name);
		if (values == null) {
			return defaultValues;
		}
		return values;
	}

	public static Integer[] getIntegerParameterValues(String name) {
		String[] values = HttpTool.getParameterValues(name);
		if (values == null) {
			return null;
		}
		int len = values.length;
		Integer[] intValues = new Integer[len];
		for (int i = 0; i < len; i++) {
			intValues[i] = Integer.parseInt(values[i]);
		}
		return intValues;
	}

	public static Integer[] getIntegerParameterValues(String name,
			Integer[] defaultValues) {
		Integer[] values = HttpTool.getIntegerParameterValues(name);
		if (values == null) {
			return defaultValues;
		}
		return values;
	}

	public static Long[] getLongParameterValues(String name) {
		String[] values = HttpTool.getParameterValues(name);
		if (values == null) {
			return null;
		}
		int len = values.length;
		Long[] intValues = new Long[len];
		for (int i = 0; i < len; i++) {
			intValues[i] = Long.parseLong(values[i]);
		}
		return intValues;
	}

	public static Long[] getLongParameterValues(String name,
			Long[] defaultValues) {
		Long[] values = HttpTool.getLongParameterValues(name);
		if (values == null) {
			return defaultValues;
		}
		return values;
	}

	public static Short[] getShortParameterValues(String name) {
		String[] values = HttpTool.getParameterValues(name);
		if (values == null) {
			return null;
		}
		int len = values.length;
		Short[] intValues = new Short[len];
		for (int i = 0; i < len; i++) {
			intValues[i] = Short.parseShort(values[i]);
		}
		return intValues;
	}

	public static Short[] getShortParameterValues(String name,
			Short[] defaultValues) {
		Short[] values = HttpTool.getShortParameterValues(name);
		if (values == null) {
			return defaultValues;
		}
		return values;
	}

	public static Float[] getFloatParameterValues(String name) {
		String[] values = HttpTool.getParameterValues(name);
		if (values == null) {
			return null;
		}
		int len = values.length;
		Float[] intValues = new Float[len];
		for (int i = 0; i < len; i++) {
			intValues[i] = Float.parseFloat(values[i]);
		}
		return intValues;
	}

	public static Float[] getFloatParameterValues(String name,
			Float[] defaultValues) {
		Float[] values = getFloatParameterValues(name);
		if (values == null) {
			return defaultValues;
		}
		return values;
	}

	public static Double[] getDoubleParameterValues(String name) {
		String[] values = getParameterValues(name);
		if (values == null) {
			return null;
		}
		int len = values.length;
		Double[] intValues = new Double[len];
		for (int i = 0; i < len; i++) {
			intValues[i] = Double.parseDouble(values[i]);
		}
		return intValues;
	}

	public static Double[] getDoubleParameterValues(String name,
			Double[] defaultValues) {
		Double[] values = HttpTool.getDoubleParameterValues(name);
		if (values == null) {
			return defaultValues;
		}
		return values;
	}

	public static Date getDateParameter(String name) throws ParseException {
		String value = HttpTool.getParameter(name);
		if (StringUtils.isNotBlank(value)) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return df.parse(value);
		}
		return null;
	}

	public static Date getDateParameter(String name, Date defaultValue)
			throws ParseException {
		Date value = HttpTool.getDateParameter(name);
		if (value == null)
			return defaultValue;
		return value;
	}

	/**
	 * 获取分页页号
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Integer getPageNum() throws ParseException {
		Integer pageNum = HttpTool.getIntegerParameter("pageNum");
		if (pageNum == null) {
			pageNum = HttpTool.getIntegerParameter("_pageNum");
		}
		if (pageNum == null) {
			pageNum = 1;
		}
		return pageNum;
	}

	/**
	 * 封装request设置参数
	 * 
	 * @param name
	 * @param o
	 */
	public static void setAttribute(String name, Object o) {
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute(name,  o);
	}
	
	public static Integer getPageSize() throws ParseException{
	    Integer pageSize = StaticMethod.nullObject2Integer(getParameter("pageSize"));
	    if (pageSize.intValue() == 0) {
	      return StaticMethod.nullObject2Integer(SystemConstant.getSystemConstant("default_page_size"));
	    }
	    return pageSize;
	}
	
	/**
	 * 获取HttpSession
	 * @return
	 */
	public static HttpSession getSession(){
		HttpSession session = ServletActionContext.getRequest().getSession() ;
		return session ;
	}
	/**
	 * 获取当前IP
	 * @return
	 * @throws ParseException
	 */
	public static String getCurrentIp()throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		return request.getRemoteAddr();
	}
	
	public static void removeAttribute(String name){
		HttpServletRequest request = ServletActionContext.getRequest() ;
		request.removeAttribute(name) ;
	}
}