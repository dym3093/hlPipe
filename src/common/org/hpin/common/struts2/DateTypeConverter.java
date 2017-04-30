package org.hpin.common.struts2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

/**
 * 替换Struts2时间转换器
 * 
 * @author thinkpad
 * @May 9, 2010
 */
@SuppressWarnings({ "unchecked" })
public class DateTypeConverter extends StrutsTypeConverter {

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	// 暂时只考虑这几种日期格式
	public static final DateFormat[] ACCEPT_DATE_FORMATS = {
			new SimpleDateFormat("yyyy年MM月dd日HH时"),
			new SimpleDateFormat("yyyy年MM月dd日"),
			new SimpleDateFormat("yyyy-MM-dd HH时"),
			new SimpleDateFormat("yyyy-MM-dd HH:mm"),
			new SimpleDateFormat("yyyy-MM-dd HH"),
			new SimpleDateFormat(DEFAULT_DATE_FORMAT),
			new SimpleDateFormat("yyyy-MM") ,
			new SimpleDateFormat("HH:mm") ,
		};

	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (values[0] == null || values[0].trim().equals(""))
			return null;
		for (int i = 0; i < ACCEPT_DATE_FORMATS.length; i++) {
			try {
				Object o = ACCEPT_DATE_FORMATS[i].parse(values[0]);
				return o;
			} catch (ParseException e) {
				continue;
			} catch (RuntimeException e) {
				continue;
			}
		}
		return null;
	}

	public String convertToString(Map context, Object o) {
		if (o instanceof Date) {
			SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			try {
				return format.format((Date) o);
			} catch (RuntimeException e) {
				return "";
			}
		}
		return "";
	}

}
