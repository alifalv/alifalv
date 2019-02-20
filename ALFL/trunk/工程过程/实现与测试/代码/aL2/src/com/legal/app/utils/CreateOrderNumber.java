package com.legal.app.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 
 * @author 军山依旧在
 * 2018-09-25
 *
 */
public class CreateOrderNumber {
  /**
   * 生成订单号：格式 年月日时分秒+“加上6位数随机数”
   * @return
   */
	public static  String create() { 
		Date getDate = Calendar.getInstance().getTime(); 
		String dateStr1 = new SimpleDateFormat("yyyyMMddHHmmss").format(getDate);
		return   dateStr1+CreateOrderNumber.getRandom();
   }
	
 public static String getRandom() {
	 Random  ran = new Random();
	 String res = "";
	 for(int i = 0 ; i < 6; i ++) {
		 res += ran.nextInt(10); 
	 }
	 return res;
 }	
	
	 public static void main(String[] args) {
		 for(int i = 0 ; i < 20;i++) {
		String ran =  CreateOrderNumber.create();
		System.out.println(ran);
		 }
   }
}
