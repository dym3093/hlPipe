package org.hpin.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Date Utility Class This is used to convert Strings to Dates and Timestamps
 * 
 * <p>
 * 
 * </p>
 * 
 * @author panlong
 * 
 */
public class DateUtil {
	// ~ Static fields/initializers
	// =============================================

	// private static Log log = LogFactory.getLog(DateUtil.class);

	private static String defaultDatePattern = null;

	private static String timePattern = "HH:mm";

	private static String EOMS_SHEET_DATEPATTERN = "yyyy-dd-MM HH:mm";

	// ~ Methods
	// ================================================================

	/**
	 * Return default datePattern (MM/dd/yyyy)
	 * 
	 * @return a string representing the date pattern on the UI
	 */
	public static synchronized String getDatePattern() {
		Locale locale = LocaleContextHolder.getLocale();
		try {
			defaultDatePattern = ResourceBundle.getBundle(
					"config/ApplicationResources", locale).getString(
					"date.format");
		} catch (MissingResourceException mse) {
			defaultDatePattern = "MM/dd/yyyy";
		}

		return defaultDatePattern;
	}
	
	public static String  getTimestamp(){
		Timestamp appointTime=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
	    String timestamp = "";
		try {
			Date date = df.parse(String.valueOf(appointTime));
			timestamp = date.getTime()/1000+"";  
		} catch (ParseException e) {
			e.printStackTrace();
		}  
        return timestamp;
	}

	/**
	 * 返回工单日期pattern
	 * 
	 * @return
	 */
	public static String getEomsSheetDatepattern() {
		return EOMS_SHEET_DATEPATTERN;
	}

	public static String getDateTimePattern() {
		return DateUtil.getDatePattern() + " HH:mm:ss.S";
	}

	/**
	 * This method attempts to convert an Oracle-formatted date in the form
	 * dd-MMM-yyyy to mm/dd/yyyy.
	 * 
	 * @param aDate
	 *            date from database as a string
	 * @return formatted string for the ui
	 */
	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date/time in the
	 * format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param strDate
	 *            a string representation of a date
	 * @return a converted Date object
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String aMask, String strDate)
			throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			// log.error("ParseException: " + pe);

			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	/**
	 * This method returns the current date time in the format: MM/dd/yyyy HH:MM
	 * a
	 * 
	 * @param theTime
	 *            the current time
	 * @return the current date/time
	 */
	public static String getTimeNow(Date theTime) {
		return getDateTime(timePattern, theTime);
	}

	/**
	 * This method returns the current date in the format: MM/dd/yyyy
	 * 
	 * @return the current date
	 * @throws ParseException
	 */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

		// This seems like quite a hack (date -> string -> date),
		// but it works ;-)
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));

		return cal;
	}

	/**
	 * This method generates a string representation of a date's date/time in
	 * the format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param aDate
	 *            a date object
	 * @return a formatted string representation of the date
	 * 
	 * @see java.text.SimpleDateFormat
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {

		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date based on the
	 * System Property 'dateFormat' in the format you specify on input
	 * 
	 * @param aDate
	 *            A date to convert
	 * @return a string representation of the date
	 */
	public static final String convertDateToString(Date aDate) {
		return getDateTime(getDatePattern(), aDate);
	}

	/**
	 * This method converts a String to a date using the datePattern
	 * 
	 * @param strDate
	 *            the date to convert (in format MM/dd/yyyy)
	 * @return a date object
	 * 
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate)
			throws ParseException {
		Date aDate = null;

		try {

			aDate = convertStringToDate("yyyy-MM-dd", strDate);
		} catch (ParseException pe) {

			pe.printStackTrace();
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());

		}

		return aDate;
	}

	public static Date truncateToDate(Timestamp t) {
		return new Date(DateUtils.truncate(t, Calendar.DATE).getTime());
	}

	public static Date truncateToDate(java.util.Date date) {
		return new Date(DateUtils.truncate(date, Calendar.DATE).getTime());
	}

	public static Timestamp truncateToMonth(Timestamp t) {
		return new Timestamp(DateUtils.truncate(t, Calendar.MONTH).getTime());
	}

	// public static java.util.Date truncateToMonth( java.util.Date t ) {
	// return DateUtils.truncate( t, Calendar.MONTH );
	// }

	public static Date truncateToMonth(Date t) {
		return new Date(DateUtils.truncate(t, Calendar.MONTH).getTime());
	}

	/**
	 * month begins at 1
	 */
	public static Date getMonth(int year, int month) {
		Calendar c = GregorianCalendar.getInstance();
		c.clear();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DATE, 1);
		return new Date(c.getTimeInMillis());
	}

	/**
	 * month begins at 1
	 */
	public static Date getMonth(String yearString, String monthString) {
		int year = Util.getUnsignedInteger(yearString, 0);
		if (year == 0 || year < 2000) {
			return null;
		}
		int month = Util.getUnsignedInteger(monthString, 0);
		if (month == 0 || month > 12) {
			return null;
		}
		return getMonth(year, month);
	}

	public static int getCurrentYear() {
		return GregorianCalendar.getInstance().get(Calendar.YEAR);
	}

	public static int getCurrentMonth() {
		return GregorianCalendar.getInstance().get(Calendar.MONTH) + 1;
	}

	public static List<Date> getAllMonthDates(Date fromDate, Date toDate) {
		List<Date> tmpDates = new ArrayList<Date>();
		long toDateMiliSeconds = toDate.getTime();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(fromDate);
		Date tmpDate;
		while (calendar.getTimeInMillis() <= toDateMiliSeconds) {
			tmpDate = new Date(calendar.getTimeInMillis()); // cannot be
															// modified.
			tmpDates.add(tmpDate);
			calendar.add(Calendar.MONTH, 1); // important: add by month
												// default not for day.
		}
		return tmpDates;
	}

	public static java.util.Date getAddMonth(java.util.Date date, int count) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String datess = format.format(date);
		Calendar gc = GregorianCalendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.MONTH, count);
		
		return gc.getTime();
	}
	public static java.util.Date getStMonth(java.util.Date date, int count) {
		Calendar gc = GregorianCalendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.MONTH, -count);

		return DateUtil.truncateToMonth(new Date(gc.getTimeInMillis()));
	}
	public static Date getDateToStartDate(Date date) {

		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");
			String sdate = format.format(date);
			sdate += " 00:00:00";
			date = format.parse(sdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}
	
	public static Date getDateToEndDate(Date date) {

		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");
			String sdate = format.format(date);
			sdate += " 23:59:59";
			date = format.parse(sdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}
	public static Date getDateToStartDateTime(Date date) {

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");
			String sdate = formats.format(date);
			sdate += " 00:00:00";
			date = format.parse(sdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}
	
	public static Date getDateToEndDateTime(Date date) {

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");
			String sdate = formats.format(date);
			sdate += " 23:59:59";
			date = format.parse(sdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}
	
	public static String getDateToStartDateTimeString(Date date) {
		SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");
		return formats.format(date)+ " 00:00:00";

	}
	
	public static String getDateToEndDateTimeString(Date date) {
		SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");
		return formats.format(date)+ " 23:59:59";

	}
	
	/**
	 * 根据输入的毫秒数，获得年份
	 * 
	 * @param millis
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 根据输入的毫秒数，获得月份
	 * 
	 * @param millis
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}

	/**
	 * 根据输入的毫秒数，获得日期
	 * 
	 * @param millis
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DATE);
	}


	
	public static java.util.Date getCurrentDate(){
		java.util.Date date = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sz_date = format.format(date);
		try {
			 date = format.parse(sz_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取月的最后一天
	 * 
	 * @param fiducialDate
	 *            Date
	 * @param offset
	 *            int 偏移量
	 * @return Date
	 */
	public static Date getLastDayOfMonth(Date fiducialDate, int offset) {
		Calendar cal = Calendar.getInstance();
		if (fiducialDate == null)
			fiducialDate = new Date();
		cal.setTime(fiducialDate);
		cal.add(Calendar.MONTH, offset + 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}
	
	public static java.util.Date addDate(java.util.Date date, int day) { 
		java.util.Calendar c = java.util.Calendar.getInstance(); 
		c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000); 
		return c.getTime(); 
	}
	
	 /** * 返回毫秒 * @param date 日期 * @return 返回毫秒 */ 
	public static long getMillis(java.util.Date date) { 
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date); return c.getTimeInMillis(); 
	}
	
	public static void main(String[] args) {
		Date date  = getLastDayOfMonth(new Date(),0);
		Date lastdate = DateUtil.getLastDayOfMonth(date, 0);
		Date dates = DateUtil.getAddMonth(date, 1);
		if(StaticMethod.date2String(lastdate).equals(StaticMethod.date2String(date)) ){
			dates = DateUtil.getLastDayOfMonth(dates, 0);
		}
		System.out.println(StaticMethod.date2String(dates));
	}
	
	
}
