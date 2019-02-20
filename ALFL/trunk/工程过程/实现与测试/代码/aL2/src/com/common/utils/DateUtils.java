/*
 * Copyright 2015-2102 RonCoo(http://www.roncoo.com) Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.common.utils;

import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

    public static final String TIME_WITH_MINUTE_PATTERN = "HH:mm";

    public static final long DAY_MILLI = 24 * 60 * 60 * 1000; // 一天的MilliSecond

    public final static int LEFT_OPEN_RIGHT_OPEN = 1;
    public final static int LEFT_CLOSE_RIGHT_OPEN = 2;
    public final static int LEFT_OPEN_RIGHT_CLOSE = 3;
    public final static int LEFT_CLOSE_RIGHT_CLOSE = 4;
    /**
     * 比较日期的模式 --只比较日期，不比较时间
     */
    public final static int COMP_MODEL_DATE = 1;
    /**
     * 比较日期的模式 --只比较时间，不比较日期
     */
    public final static int COMP_MODEL_TIME = 2;
    /**
     * 比较日期的模式 --比较日期，也比较时间
     */
    public final static int COMP_MODEL_DATETIME = 3;

    private static Logger logger = Logger.getLogger(DateUtils.class);

    /**
     * 要用到的DATE Format的定义
     */
    public static String DATE_FORMAT_DATEONLY = "yyyy-MM-dd"; // 年/月/日
    public static String DATE_FORMAT_DATEONLYDOT = "yyyy.MM.dd"; // 年/月/日
    public static String DATE_FORMAT_TIMEONLY = "HH:mm:ss";

    public static String DATE_FORMAT_DATEONLY_STR = "yyyy年MM月dd"; // 年/月/日
    public static String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss"; // 年/月/日
    public static SimpleDateFormat sdfDateTime = new SimpleDateFormat(DateUtils.DATE_FORMAT_DATETIME);
    // Global SimpleDateFormat object
    public static SimpleDateFormat sdfDateOnly = new SimpleDateFormat(DateUtils.DATE_FORMAT_DATEONLY);
    public static final SimpleDateFormat SHORTDATEFORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat HMS_FORMAT = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat formatTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat timeStamp = new SimpleDateFormat("HH-mm-ss");


    /**
     * 得到长日期格式字串
     *
     * @return
     */
    public static String getLongDateStr() {
        return LONG_DATE_FORMAT.format(new Date());
    }


    /**
     * 根据指定的Format转化java.util.Date到String
     *
     * @param dt   java.util.Date instance
     * @param sFmt Date format , DATE_FORMAT_DATEONLY or DATE_FORMAT_DATETIME
     * @return
     * @history
     * @since 1.0
     */
    public static String toString(Date dt, String sFmt) {
        if (dt == null || sFmt == null || "".equals(sFmt)) {
            return "";
        }
        return toString(dt, new SimpleDateFormat(sFmt));
    }

    /**
     * 利用指定SimpleDateFormat instance转换java.util.Date到String
     *
     * @param dt        java.util.Date instance
     * @param formatter SimpleDateFormat Instance
     * @return
     * @history
     * @since 1.0
     */
    private static String toString(Date dt, SimpleDateFormat formatter) {
        String sRet = null;

        try {
            sRet = formatter.format(dt).toString();
        } catch (Exception e) {
            logger.error(e);
            sRet = null;
        }

        return sRet;
    }

    /**
     * 转换java.sql.Timestamp到String，格式为YYYY/MM/DD HH24:MI
     *
     * @param dt java.sql.Timestamp instance
     * @return
     * @history
     * @since 1.0
     */
    public static String toSqlTimestampString2(Timestamp dt) {
        if (dt == null) {
            return null;
        }
        String temp = toSqlTimestampString(dt, DateUtils.DATE_FORMAT_DATETIME);
        return temp.substring(0, 16);
    }

    public static String toString(Timestamp dt) {
        return dt == null ? "" : toSqlTimestampString2(dt);
    }

    /**
     * 根据指定的格式转换java.sql.Timestamp到String
     *
     * @param dt   java.sql.Timestamp instance
     * @param sFmt Date 格式，DATE_FORMAT_DATEONLY/DATE_FORMAT_DATETIME/
     *             DATE_FORMAT_SESSION
     * @return
     * @history
     * @since 1.0
     */
    public static String toSqlTimestampString(Timestamp dt, String sFmt) {
        String temp = null;
        String out = null;
        if (dt == null || sFmt == null) {
            return null;
        }
        temp = dt.toString();
        if (sFmt.equals(DateUtils.DATE_FORMAT_DATETIME) || // "YYYY/MM/DD
                // HH24:MI:SS"
                sFmt.equals(DateUtils.DATE_FORMAT_DATEONLY)) { // YYYY/MM/DD
            temp = temp.substring(0, sFmt.length());
            out = temp.replace('/', '-');
            // }else if( sFmt.equals (DateUtils.DATE_FORMAT_SESSION ) ){
            // //Session
            // out =
            // temp.substring(0,4)+temp.substring(5,7)+temp.substring(8,10);
            // out += temp.substring(12,14) + temp.substring(15,17);
        }
        return out;
    }


    /**
     * 取得指定日期格式的字符串
     *
     * @param date
     * @return String
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }


    /**
     * 字符转日期
     *
     * @param dateStr
     * @return
     */
    public static Date getDateByStr(String dateStr) {
        SimpleDateFormat formatter = null;
        if (dateStr == null) {
            return null;
        } else if (dateStr.length() == 10) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        } else if (dateStr.length() == 16) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        } else if (dateStr.length() == 19) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else if (dateStr.length() > 19) {
            dateStr = dateStr.substring(0, 19);
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            return null;
        }
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            logger.error(e);
            return null;
        }
    }

    public static String getAgeUnit(Date birthday, Date targetDate) {

        Calendar birthdayCalendar = Calendar.getInstance();
        birthdayCalendar.setTime(birthday);
        Calendar targetDateCalendar = Calendar.getInstance();
        targetDateCalendar.setTime(targetDate);
        LocalDate birthdate = LocalDate.of(birthdayCalendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH)+1, birthdayCalendar.get(Calendar.DAY_OF_MONTH));
        LocalDate now = LocalDate.of(targetDateCalendar.get(Calendar.YEAR), targetDateCalendar.get(Calendar.MONTH)+1, targetDateCalendar.get(Calendar.DAY_OF_MONTH));
        Period p = Period.between(birthdate, now);

        if (p.getYears() > 0) {
            return p.getYears() + "y";
        } else if (p.getMonths() > 0) {
            return p.getMonths() + "m";
        } else if (p.getDays() > 0) {
            return p.getDays() + "d";
        }
        return p.getYears() + "y";
    }

    public static String getAgeUnit(Date birthday) {
        return getAgeUnit(birthday, new Date());
    }

    public static String getAge(Date birthday, Date targetDate) {

        Calendar birthdayCalendar = Calendar.getInstance();
        birthdayCalendar.setTime(birthday);
        Calendar targetDateCalendar = Calendar.getInstance();
        targetDateCalendar.setTime(targetDate);
        LocalDate birthdate = LocalDate.of(birthdayCalendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH)+1, birthdayCalendar.get(Calendar.DAY_OF_MONTH));
        LocalDate now = LocalDate.of(targetDateCalendar.get(Calendar.YEAR), targetDateCalendar.get(Calendar.MONTH)+1, targetDateCalendar.get(Calendar.DAY_OF_MONTH));
        Period p = Period.between(birthdate, now);

        if (p.getYears() > 0) {
            return p.getYears() + "周岁";
        } else if (p.getMonths() > 0) {
            return p.getMonths() + "月";
        } else if (p.getDays() > 0) {
            return p.getDays() + "天";
        }
        return p.getYears() + "周岁";
    }

    public static String getAgeAndUnit(Date birthday, Date targetDate) {

        Calendar birthdayCalendar = Calendar.getInstance();
        birthdayCalendar.setTime(birthday);
        Calendar targetDateCalendar = Calendar.getInstance();
        targetDateCalendar.setTime(targetDate);
        LocalDate birthdate = LocalDate.of(birthdayCalendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH)+1, birthdayCalendar.get(Calendar.DAY_OF_MONTH));
        LocalDate now = LocalDate.of(targetDateCalendar.get(Calendar.YEAR), targetDateCalendar.get(Calendar.MONTH)+1, targetDateCalendar.get(Calendar.DAY_OF_MONTH));
        Period p = Period.between(birthdate, now);

        if (p.getYears() > 0) {
            return p.getYears() + "_周岁";
        } else if (p.getMonths() > 0) {
            return p.getMonths() + "_月";
        } else if (p.getDays() > 0) {
            return p.getDays() + "_天";
        }
        return p.getYears() + "_周岁";
    }
    
    
    public static String getAgeAndEnUnit(Date birthday, Date targetDate) {

        Calendar birthdayCalendar = Calendar.getInstance();
        birthdayCalendar.setTime(birthday);
        Calendar targetDateCalendar = Calendar.getInstance();
        targetDateCalendar.setTime(targetDate);
        LocalDate birthdate = LocalDate.of(birthdayCalendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH)+1, birthdayCalendar.get(Calendar.DAY_OF_MONTH));
        LocalDate now = LocalDate.of(targetDateCalendar.get(Calendar.YEAR), targetDateCalendar.get(Calendar.MONTH)+1, targetDateCalendar.get(Calendar.DAY_OF_MONTH));
        Period p = Period.between(birthdate, now);

        if (p.getYears() > 0) {
            return p.getYears() + "_Y";
        } else if (p.getMonths() > 0) {
            return p.getMonths() + "_M";
        } else if (p.getDays() > 0) {
            return p.getDays() + "_D";
        }
        return p.getYears() + "_Y";
    }
    
    public static String getAge(Date birthday) {
        return getAge(birthday, new Date());
    }

    /**
     * 获得前后日期
     * @param dateStart
     * @param day
     * @return
     */
    public static Date getBeforeAfterDate(Date dateStart, int day) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dateStart);

        int Year = cal.get(Calendar.YEAR);
        int Month = cal.get(Calendar.MONTH);
        int Day = cal.get(Calendar.DAY_OF_MONTH);

        int NewDay = Day + day;

        cal.set(Calendar.YEAR, Year);
        cal.set(Calendar.MONTH, Month);
        cal.set(Calendar.DAY_OF_MONTH, NewDay);

        return new Date(cal.getTimeInMillis());
    }

    public static Date getBeforeAfterMonthDate(Date dateStart, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateStart);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    public static Date getPreviousDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-1);
        return cal.getTime();
    }

    public static Date getNextDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
        return cal.getTime();
    }

    public static Date getPreviousYear(Date date){
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.YEAR, -1);
        return ca.getTime();
    }

    public static Date getNextYear(Date date){
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.YEAR, +1);
        return ca.getTime();
    }

    public static Date getFirstDateOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        return getFirstDateOfMonth(year, month);
    }

    public static Date getLastDateOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        return getLastDateOfMonth(year, month);
    }

    public static Date getFirstDateOfMonth(int year, int month){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getLastDateOfMonth(int year, int month){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    // 获得日期0点时间
    public static Date getStartTimesOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得日期24点时间
    public static Date getEndTimesOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得两个时间相差的天数
    public static int getDifferentDays(Date startTime,Date endTime){
        int days = (int) ((endTime.getTime() - startTime.getTime()) / (1000*3600*24));
        return days;
    }

    public static void main(String[] args) {
        Date date = DateUtils.getDateByStr("2017-08-31");
        System.out.println("1 = " + DateUtils.toString(date, "yyyy-MM-dd"));
        System.out.println("2 = " + DateUtils.toString(DateUtils.getBeforeAfterMonthDate(date, 0), "yyyy-MM-dd"));
    }
}
