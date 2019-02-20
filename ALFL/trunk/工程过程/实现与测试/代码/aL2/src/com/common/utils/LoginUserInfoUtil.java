package com.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 登陆用户的信息；
 * @author Administrator
 *
 */
public class LoginUserInfoUtil {
	 private static  Map<String,Object> cacheLoginUserInfo= new HashMap<String,Object>();
	 private static final String USERINFO_KEY = "userinfo"; 
	 
	 public static String getLoginUserName(int userid) {
		 return  "张三";
	 }
	 
	  
}
