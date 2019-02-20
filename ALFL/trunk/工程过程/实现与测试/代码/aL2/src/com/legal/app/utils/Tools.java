package com.legal.app.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 
 * @author 军山依旧在
 *
 */
public class Tools {
	

	/**
	 * 去除Double 的小数 ，保留二位小数 ；
	 * 如 23.9898 变成  23.98
	 * @param du
	 * @return
	 */
	public static Double rwipeOffDecimals (Double du) {
		  DecimalFormat df = new DecimalFormat("#.00");
		  return  Double.valueOf((df.format(du)));
		}
	 /**
     * 
     * @doc 日期转换星期几
     * @param datetime
     *            日期 例:2017-10-17
     * @return String 例:星期二
     * @author 
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    /**
     * 功能：判断输入年份是否为闰年<br>
     *
     * @param year
     * @return 是：true  否：false
     * @author pure
     */
    public static boolean leapYear(int year) {
        boolean leap;
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) leap = true;
                else leap = false;
            } else leap = true;
        } else leap = false;
        return leap;
    }
    public static String thisMonthStart(String rq) {
        String x;
        String y;
        x = rq.substring(0, 4);
        y = rq.substring(5, 7);
        return x + "-" + y + "-01";
    }


    public static String thisMonthEnd(String rq) {
        String strY = null;
        String strZ = null;
        int x;
        int y;
        boolean leap = false;
        x = Integer.parseInt(rq.substring(0, 4));
        y = Integer.parseInt(rq.substring(5, 7));
        if (y == 1 || y == 3 || y == 5 || y == 7 || y == 8 || y == 10 || y == 12) {
            strZ = "31";
        }
        if (y == 4 || y == 6 || y == 9 || y == 11) {
            strZ = "30";
        }
        if (y == 2) {
            leap = leapYear(x);
            if (leap) {
                strZ = "29";
            } else {
                strZ = "28";
            }
        }
        strY = y >= 10 ? String.valueOf(y) : ("0" + y);
        return x + "-" + strY + "-" + strZ;
    }

    
    public static void main(String[] args){
    	Object da = null;
    	Date date = (Date)da;
    	System.out.println("123");
    	//System.out.println(dateToWeek("2018-10-6 10:11"));
    	//System.out.println(thisMonthEnd("2018-10-6 10:11"));
    	
    }
}
