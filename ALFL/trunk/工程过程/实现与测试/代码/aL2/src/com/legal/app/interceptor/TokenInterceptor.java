package com.legal.app.interceptor;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.common.cache.ICache;
import com.common.web.WebUtils;

public class TokenInterceptor extends HandlerInterceptorAdapter{

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = request.getHeader("token"); 
		if(null == token || token.equals("") || token.equals("null")||token.equals("undefined")){
			return true; 
		}else{ 
			if(!token.equals((String)ICache.tokenMap.get("token"))){
				PrintWriter  writer= response.getWriter();  
				response.setContentType("text/html;charset=utf-8"); 
				String print_str = WebUtils.responseError("请求错误",-9);
				writer.write(print_str);
				return false; 
			}
			return true; 
		}
	}


}
