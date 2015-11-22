package me.thrasher.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Date Utility Class used to convert Strings to Dates and Timestamps
 */
public class DateUtil {
	private static final String TIME_PATTERN = "HH:mm";

	public static final int THIS_YEAR = Calendar.getInstance().get(
			Calendar.YEAR);

	public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	public static final String DATE_TIME_PATTERN = DATE_PATTERN + " HH:mm:ss.S";

	public static final SimpleDateFormat ISO8601_GMT;
	static {
		ISO8601_GMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		ISO8601_GMT.setTimeZone(GMT);
	}

	public static final SimpleDateFormat RFC2822;
	static {
		RFC2822 = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z",
				Locale.US);
	}

	public static final SimpleDateFormat UNIXDATE = new SimpleDateFormat(
			"EEE MMM dd HH:mm:ss z yyyy");

	/**
	 * Converts a Calendar object to an ISO-8601 date/time string. Always
	 * converts to GMT. Adapted from example at
	 * http://www.dynamicobjects.com/d2r/archives/003057.html
	 */
	public static String calendarToISO8601(Calendar cal) {
		Date date = cal.getTime();
		return calendarToISO8601(date);
	}

	public static String calendarToISO8601(Date date) {
		synchronized (ISO8601_GMT) {
			return ISO8601_GMT.format(date); // guaranteed GMT
		}
	}

	public static Date parseDateISO8601(String date) throws ParseException {
		synchronized (ISO8601_GMT) {
			return ISO8601_GMT.parse(date);
		}
	}

	/**
	 * Parse an RFC 2822 date. This is commonly used in iTunes pubDate fields.
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDateRFC2822(String date) throws ParseException {
		return RFC2822.parse(date);
	}

	public static Date parseUnixDate(String date) throws ParseException {
		return UNIXDATE.parse(date);
	}

	/**
	 * Returns a timestamp string for the current time in ISO-8601 format.
	 * Always in GMT.
	 */
	public static String timestamp() {
		return calendarToISO8601(Calendar.getInstance(GMT));
	}

	public static String sqlDateString(Calendar cal) {
		if (cal == null) {
			return null;
		}
		return new java.sql.Date(cal.getTimeInMillis()).toString();
	}

	public static String sqlDateString(Date d) {
		if (d == null) {
			return null;
		}
		return new java.sql.Date(d.getTime()).toString();
	}

	/**
	 * Checkstyle rule: utility classes should not have public constructor
	 */
	private DateUtil() {
	}

	/**
	 * This method attempts to convert an Oracle-formatted date in the form
	 * dd-MMM-yyyy to mm/dd/yyyy.
	 * 
	 * @param aDate
	 *            date from database as a string
	 * @return formatted string for the ui
	 */
	public static String getDate(Date aDate) {
		SimpleDateFormat df;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(DATE_PATTERN);
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
	 *             when String doesn't match the expected format
	 */
	public static Date convertStringToDate(String aMask, String strDate)
			throws ParseException {
		SimpleDateFormat df;
		Date date;
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
		return getDateTime(TIME_PATTERN, theTime);
	}

	/**
	 * This method returns the current date in the format: MM/dd/yyyy
	 * 
	 * @return the current date
	 * @throws ParseException
	 *             when String doesn't match the expected format
	 */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);

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
	public static String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		df = new SimpleDateFormat(aMask);
		returnValue = df.format(aDate);

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
	public static String convertDateToString(Date aDate) {
		return getDateTime(DATE_PATTERN, aDate);
	}

	/**
	 * This method converts a String to a date using the datePattern
	 * 
	 * @param strDate
	 *            the date to convert (in format MM/dd/yyyy)
	 * @return a date object
	 * @throws ParseException
	 *             when String doesn't match the expected format
	 */
	public static Date convertStringToDate(String strDate)
			throws ParseException {
		Date aDate = null;
		aDate = convertStringToDate(DATE_PATTERN, strDate);

		return aDate;
	}
}
