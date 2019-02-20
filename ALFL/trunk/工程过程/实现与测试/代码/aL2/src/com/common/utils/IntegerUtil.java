package com.common.utils;


/**
 * 数据库统计sql结果都为long类型
 * @author hzh
 *
 */
public class IntegerUtil {
	
	public static int toInt(Object value){
		
		if(value==null){
			return 0;
		}
		
		int v = 0;
		
		try {
			
			v = Integer.valueOf(value.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return v;
	}
	
}
