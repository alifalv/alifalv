package com.common.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SystemUtil {

	private static Random random = new Random();
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
	
	/**
	 * 注册用户未上传图片的默认头像
	 * @return
	 */
	public static String getDefaultUserImg(){
		return "aliImgs/systemImg/defalut-head.png";
	}
	
	
	/**
	 * 获取注册咨询者免费咨询次数
	 * @return
	 */
	public static int getFreeNum(){
		return 3;
	}
	
	/**
	 * 获取各项基础评分
	 * @return
	 */
	public static BigDecimal getBasicScore(){
		return new BigDecimal(4.8);
	}
	
	
	public synchronized static String getOrderNo() {
		
		String orderNo = sdf.format(new Date());
		
		StringBuilder builder = new StringBuilder(orderNo);
		
		for (int i = 0; i < 6; i++) {
			
			builder.append(random.nextInt(10));
			
		}
		return builder.toString();
	}
	
}
