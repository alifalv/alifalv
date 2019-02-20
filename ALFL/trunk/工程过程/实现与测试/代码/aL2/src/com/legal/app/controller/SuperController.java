package com.legal.app.controller;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.View;

import com.common.cache.ICache;
import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;
import com.common.web.WebUtils;

/**
 * 类型描述:所有MVC之控制层Controller,都应继承的父类,让子类自动获得事务、异常等处理机制;
 * 同时拥有全局缓存（cache）对象、当前开发模式（model
 * )的引用；并通过Model.model.asMap().get(DEVICE_CODE)获得当前请求端的设备类型码；
 * <ul style="color:red">
 * 针对Controller方法,以下几个方面需要注意: *
 * <li>所有public方法都必须声明抛出Exception;
 * <li>所有public方法一般情况下(原则上)标注为@ResponseBody;
 * <li>如有自行使用printWriter(response.getWrite())的情况,则须自行close()，以释放资源；
 * </ul>
 * </br>
 * 创建时期: 2015年12月14日
 * 
 * @author hyq
 */
public abstract class SuperController {

	 /**
	  * 把所有的参数都封装到Map中去
	  * @param request
	  * @return
	  */
	public static Map getMap(HttpServletRequest request) {
		Map<String, Object> map_new = new HashMap<String, Object>();

		NativeWebRequest webRequest = new ServletWebRequest(request);
		Map<String, Object> maps = (Map<String, Object>) webRequest.getAttribute(View.PATH_VARIABLES,
				RequestAttributes.SCOPE_REQUEST);

		Map<String, String[]> map_ = request.getParameterMap();
		if(null != map_ && map_.size() > 0) {
			for (Map.Entry<String, String[]> entry : map_.entrySet()) {
				if (entry.getValue().length > 1) {
					map_new.put(entry.getKey(), entry.getValue());  
				} else {  
					map_new.put(entry.getKey(), entry.getValue()[0]);
				}
			}
		}
		if(null != maps && maps.size() > 0 ) {
			for (Map.Entry<String, Object> entry : maps.entrySet()) {
				map_new.put(entry.getKey(), entry.getValue());
			}
		} 
		return map_new;
	}

	/**
	 * 成功响应数据
	 */
	@SuppressWarnings("serial")
	public static final Map<String, Object> SUCCESS_DATA = new HashMap<String, Object>() {
		{
			put("success", 1);
			put("message", "操作成功！");
		}
	};

	/**
	 * 标识当前环境是生产或开发环境
	 */
	@Value("#{configProperties['model']}")
	protected String model;

	/*
	 * 统全局缓存 需要缓存时，打开此注释 ，spring.xml也要打开对应的注释
	 */
	@Autowired
	protected  ICache cache;
	
	

	/**
	 * 访问设备类型码 ,苹果电脑，平板，android平板的类型 后面测试补上 hyq 2016-2-5
	 */
	public final static byte DEVICE_ANDROID = 1;
	public final static byte DEVICE_IPHONE = 2;
	public final static byte DEVICE_OTHER = 10;// 未知
	public final static byte DEVICE_PC = 0;

	public final static String IS_WEB = "IS_WEB";
	public final static String DEVICE_CODE = "DEVICE_CODE";
	public final static String TOKEN = "token";

	/**
	 * <ul>
	 * 请求中的head中，需有"token"头，其值有两种情况：
	 * <Li>web版，其值的格式为"WEB+时间戳",时间戳为当前时间距1970-1-1 00:00:00之秒数；
	 * 如“WEB1455592054”,测试环境时，时间戳可以是<100000的任意正整数
	 * <li>app版,其值的格式为AES(时间戳,KEY,); 测试环境时，可以是不以"WEB"开头的任意值
	 * </ul>
	 * Model中有两个变量，isWeb,deviceCode分别存储了”是否WEB请求“及当前访问设备码值；
	 * 
	 * @param request
	 * @param response
	 */
	@ModelAttribute
	public void _init(HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");

		/*
		 * 访问设备码:0:pc,1:android,2:IPHONE,10:其他</br>
		 * Controller层实现可能取决于访问设备的类型,如是PC,则返回内容可能及数据可能与移动设备的有差异;
		 * 故注意在controller层实现时,注意关注访问设备类型
		 */
		byte deviceCode = parseDeviceCode(request);

		/* 是否为WEB版； */
		String tokenValue = request.getHeader("token");
		boolean isWeb = tokenValue != null && tokenValue.startsWith("WEB");

		// 存储当前访问端的相关属性
		model.addAttribute(this.IS_WEB, isWeb);
		model.addAttribute(this.DEVICE_CODE, deviceCode);
		model.addAttribute(this.TOKEN, "tokenValue");
	}

	/**
	 * 根据请求的http-agent类型解析出设备类型, ```````````该方法还未通过测试
	 */
	private byte parseDeviceCode(HttpServletRequest request) {
		// TODO 细测各种类型的访问终端；
		Enumeration<String> names = request.getHeaderNames();
		/* 测试代码 */
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getHeader(name).toUpperCase();
			System.out.println("---------" + name + ":" + request.getHeader(name));
		}

		String value = request.getHeader("user-agent");
		if (value == null)
			return this.DEVICE_OTHER;
		if (value.indexOf("ANDROID") >= 0)
			return this.DEVICE_ANDROID;
		else if (value.indexOf("IPHONE") >= 0) {
			return this.DEVICE_IPHONE;
		} else if (value.indexOf("WINDOWS NT") >= 0 || value.indexOf("MAC OS") >= 0) {
			return this.DEVICE_PC;
		} else
			return this.DEVICE_OTHER;
	}

	/**
	 * 系统级未知异常的统一处理机制
	 * 
	 * @param excption
	 *            异常对象,则spring注入,不用管
	 * @param request
	 *            异常对象,则spring注入,不用管
	 * @param response
	 *            异常对象,则spring注入,不用管
	 */
	@ExceptionHandler
	protected void _exceptionHandle(Exception exception, PrintWriter out) {
		// 写错误日志,errorId是显示到前端,以便用来定位日志文档中对应位置
		long errorLogId = ExceptionLogger.writeLog(exception, this);
		if (exception instanceof BusinessException) {
			out.print(WebUtils.responseMsg(exception.getMessage(), -1));
		} else {
			if (model.equals("DEBUG"))
				out.print(WebUtils.responseMsg("系统发生异常:" + exception, -1, errorLogId));
			else
				out.print(WebUtils.responseMsg("系统发生异常", -1, errorLogId));
		}
	}

	/**
	 * 用户设置缓存（登录标示）
	 * 
	 * @param user_id
	 *            用户ID
	 * @param device_id
	 *            app设备ID
	 */
	public void setUserCache(String user_id, String device_id) {
		try {
			cache.put("user_" + user_id, device_id);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionLogger.writeLog("登记用户" + user_id + "异常：" + e);
		}
	}

}
