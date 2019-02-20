package com.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 类型描述:封装一些与Date操作的相关功能 </br>创建时期: 2016年1月22日
 * 
 * @author zengyao
 */
public class DateUtil {

	/** yyyy-MM-dd HH:mm:ss */
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private final static String DATE_FORMAT1 = "yyyy-MM-dd";

	/**
	 * 获取将字段类型(yyyy-MM-dd HH:mm:ss)转换成Date类型
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateByString(String date) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

		if ((date == null) || (date.length() < DATE_FORMAT.length())) {
			return null;
		}
		try {
			return dateFormat.parse(date);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获取将字段类型(yyyy-MM-dd)转换成Date类型
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateByString1(String date) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT1);

		if ((date == null) || (date.length() < DATE_FORMAT1.length())) {
			return null;
		}
		try {
			return dateFormat.parse(date);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获取将Date类型转换成字符类型格式为： yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateStrByDate(Date date) {
		if (date == null || StringUtil.isBlank(DATE_FORMAT)) {
			return "";
		}
		return new SimpleDateFormat(DATE_FORMAT, Locale.SIMPLIFIED_CHINESE)
				.format(date);
	}

	/**
	 * 获取将Date类型转换成字符类型格式为： yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateStrByDate1(Date date) {
		if (date == null || StringUtil.isBlank(DATE_FORMAT1)) {
			return "";
		}
		return new SimpleDateFormat(DATE_FORMAT1, Locale.SIMPLIFIED_CHINESE)
				.format(date);
	}

	/**
	 * 获取当前时间的字符串数据类型
	 * 
	 * @return
	 */
	public static String getDateStrByNow() {
		return getDateStrByDate(new Date());
	}

}
