package com.common.utils;

import java.util.Map;

public class MapUtil {
	
	
	/**
	 * map取值
	 * @param map map对象
	 * @param key 键
	 * @param defualtValue 默认的值
	 * @return
	 */
	public static String getValue(Map map,String key,String defualtValue){
		
		if(map == null){
			return defualtValue;
		}
		if(map.get(key)==null){
			return defualtValue;
		}else{
			return map.get(key).toString();
		}
		
	}

}
