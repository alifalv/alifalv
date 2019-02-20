package com.common.utils;

import java.util.Random;

import org.apache.commons.lang.StringUtils;

/**
 * 验证码生成
 * @author hzh
 * <br/>  20170614
 *
 */
public class CodeUtil {

	
	
	/**
	 * 生成6位验证码
	 * @return
	 */
	public static String createSmsCode(){
		
		Random random = new Random();
		
		String code = StringUtils.EMPTY;
		
		for (int i = 0; i < 4; i++) {
			code += random.nextInt(10);
		}
		
		return code;
	}
	
}
