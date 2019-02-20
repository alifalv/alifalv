package com.common.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求相关工具
 * @author admin
 *
 */
public class RequestUtil {

	
	/**
	 * 根据请求参数名称生成Map对象
	 * @param request  请求对象
	 * @return
	 */
	public static Map<String, String> parametersToMap(HttpServletRequest request){
		
		Map<String, String> paramters = new HashMap<String, String>();
		
		Map<String, String []> requestParameters = request.getParameterMap();
		
		Set<String> keySet = requestParameters.keySet();
		
		for (String key : keySet) {
			paramters.put(key, requestParameters.get(key)[0]);
		}
		return paramters;
		
	}
	
	
	/**
	 * map对象转实体   key和属性匹配  区分大小写的
	 * @param parameters
	 * @param t
	 * @return
	 */
	public static <T> T mapToEntity(Map<String, String> parameters,Class<T> t) throws Exception{
		
		if(t==null){
			throw new ClassNotFoundException("实体类对象不能为NULL");
		}
		
		if(parameters==null){
			
			return t.newInstance();
			
		}
		
		
		Field [] fields = t.getDeclaredFields();
		
		T entity = t.newInstance();//创建实例
		
		for (Field field : fields) {
			field.setAccessible(true);//打开private访问权限
			
			Class<?> type = field.getType();
			
			if(type == String.class){
				field.set(entity, parameters.get(field.getName()));
			}else if(type == int.class){
				if(StringUtil.isBlank(parameters.get(field.getName()))){
					field.set(entity, 0);
				}else{
					field.set(entity, Integer.parseInt(parameters.get(field.getName())));
				}
			}else if(type == Integer.class){
				if(StringUtil.isBlank(parameters.get(field.getName()))){
					field.set(entity, 0);
				}else{
					field.set(entity, Integer.parseInt(parameters.get(field.getName())));
				}
			}else if(type == double.class){
				if(StringUtil.isBlank(parameters.get(field.getName()))){
					field.set(entity, 0d);
				}else{
					field.set(entity, Double.parseDouble(parameters.get(field.getName())));
				}
			}else if(type == Double.class){
				if(StringUtil.isBlank(parameters.get(field.getName()))){
					field.set(entity, 0d);
				}else{
					field.set(entity, Double.parseDouble(parameters.get(field.getName())));
				}
			}else if(type == float.class){
				if(StringUtil.isBlank(parameters.get(field.getName()))){
					field.set(entity, 0f);
				}else{
					field.set(entity, Float.parseFloat(parameters.get(field.getName())));
				}
			}else if(type == Float.class){
				if(StringUtil.isBlank(parameters.get(field.getName()))){
					field.set(entity, 0f);
				}else{
					field.set(entity, Float.parseFloat(parameters.get(field.getName())));
				}
			}
			
		}
		
		return entity;
	}
}
