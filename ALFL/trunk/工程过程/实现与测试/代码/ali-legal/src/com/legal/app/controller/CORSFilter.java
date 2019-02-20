package com.legal.app.controller;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.legal.app.model.SysMapping;
import com.legal.app.model.SysUser;
import com.legal.app.service.impl.SysMappingService;
import com.legal.app.service.impl.SysUserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CORSFilter implements Filter{
	public static List<SysMapping> rolemappingList = new ArrayList<SysMapping>();
    @Autowired
//    private MenuService menuService;
    private SysMappingService sysMappingService;
    private SysUserService sysUserService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext sc = filterConfig.getServletContext(); 
        XmlWebApplicationContext cxt = (XmlWebApplicationContext)WebApplicationContextUtils.getWebApplicationContext(sc);
//        menuService = (MenuService) cxt.getBean("menuService");  
        sysMappingService = (SysMappingService) cxt.getBean("sysMappingService");  
        sysUserService = (SysUserService) cxt.getBean("sysUserService");  
    }

	@Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("work");
		servletRequest.setCharacterEncoding("utf-8");
		// 获得在下面代码中要用的request,response,session对象
		HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
		String requestMethod = request.getMethod();
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Max-Age", "1728000");
		response.setHeader("Access-Control-Allow-Headers", "SYSUSERID,TOKEN,Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed","1");
		// 获得用户请求的URI
		String path = request.getRequestURI();
		// 登陆页面无需过滤
        if(path.indexOf("/login") > -1 ||
        	   path.equals("/") ||
        	   path.equals("/favicon.ico") ||
           path.indexOf("/menu/userlist") > -1 ||
           path.indexOf("/build/") > -1 ||
           path.indexOf("/css/") > -1 ||
           path.indexOf("/dialogs/") > -1 ||
           path.indexOf("/dist/") > -1 ||
           path.indexOf("/image/") > -1 ||
           path.indexOf("/jedate-6.0.0/") > -1 ||
           path.indexOf("/js/") > -1 ||
           path.indexOf("/jsp/") > -1 ||
           path.indexOf("/lang/") > -1 ||
           path.indexOf("/pages/") > -1 ||
           path.indexOf("/plugins/") > -1 ||
           path.indexOf("/themes/") > -1 ||
           path.indexOf("/third-party/") > -1 ||
           path.indexOf("/umeditor/") > -1 ||
           path.indexOf("/upload/") > -1 ||
           path.indexOf("/index.html") > -1 ||
           path.indexOf("/api/overview/indexCount") > -1 
            ) {
            filterChain.doFilter(request, response);
        }else {
			if(requestMethod.equals("OPTIONS")) {
	            filterChain.doFilter(request, response);
			}else {
		        String mmuserId = (String) request.getSession().getAttribute("mmuserId");
				String sys_userId = request.getHeader("SYSUSERID");
				String token = request.getHeader("TOKEN"); 
				System.out.println("SYSUSERID:"+sys_userId+",TOKEN:"+token);
//		        // 判断如果没有取到用户账号,就跳转到登陆页面
//		        if (sys_userId == null || "".equals(sys_userId) || "null".equals(sys_userId)) {
//		            // 跳转到登陆页面
//		        		response.sendRedirect("/ali-legal/views/login.html");
//		        }else{
//					String token = request.getHeader("TOKEN"); 
//					// 从session里面获得用户的帐号token
//			        String sessionToken = (String) request.getSession().getAttribute("BIZ_CHECKTOKEN_"+sys_userId);
//			        if(!token.equals(sessionToken)) {
//			        		System.out.println("用户id："+sys_userId+"，TOKEN："+token+"，SESSIONTOKEN："+sessionToken);
//			        }
//		        	try {// 如果能取到用户账号,就判断该用户有哪些权限
				if(mmuserId==null) {
					// 登陆url
					String loginUrl = request.getContextPath() + "/login.html";
					if(path.indexOf(".html") > -1) {
						String str = "<script language='javascript'>alert('会话过期,请重新登录');"
								+ "window.top.location.href='"+ loginUrl+ "';</script>";
						response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
						try {
							PrintWriter writer = response.getWriter();
							writer.write(str);
							writer.flush();
							writer.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else {
						response.setHeader("Access-Control-Expose-Headers", "sessionstatus");
						response.addHeader("sessionstatus", "timeOut");
						filterChain.doFilter(request, response);// 不可少，否则请求会出错
					}
				}else {
		        		SysUser sysUser = new SysUser();
		        		sysUser.setSys_userId(Integer.parseInt(mmuserId));
		        		try {
							sysUser = sysUserService.info(Integer.parseInt(mmuserId));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					if(rolemappingList.isEmpty()) {
						try {
							rolemappingList = sysMappingService.rolemapping();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
	        			boolean rR = false; 
					for(SysMapping map : rolemappingList) {
						if(map.getPremissonUri()==null || map.getPremissonUri().equals("")) {continue;}
						if(map.getRoleId().equals(sysUser.getSys_roleId()) && path.indexOf(map.getPremissonUri())>-1) {
							rR = true;
							break;
						}
					}
						if(rR) {
						    // 有权限,继续此次请求
				            filterChain.doFilter(request, response);
						}else {
							System.out.println("没有权限路径："+path);
							// 没权限,跳转到登陆页面
//							StringBuilder sb = new StringBuilder();
//							String port = "",contextPath="";
//							if(request.getServerPort()!=80){	
//								port = ":" + request.getServerPort();
//							}
//							if(StringUtils.isNotBlank(request.getContextPath().replace("/", ""))){
//								contextPath = request.getContextPath();
//							}
//							sb.append(request.getScheme()).append("://").append(request.getServerName()).append(port).append("/ali-legal").append("/views/login.html");
//							String loginPath =  sb.toString();
							//判断如果是ajax，则需要设置response参数，告诉ajax这是重定向
							//这里设置完之后，需要到common.js中设置jquery-ajax默认设置，详细查看$.ajaxSetup方法
//							String type = request.getHeader("X-Requested-With")==null?"":request.getHeader("X-Requested-With");
//							if ("XMLHttpRequest".equals(type)) {
								response.setHeader("Access-Control-Expose-Headers", "REDIRECT");
								response.setHeader("REDIRECT", "REDIRECT");//告诉ajax这是重定向
								response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//								return;
//							}else{//如果不是ajax请求，则直接重定向
//								response.sendRedirect(loginPath);
//								return;
//							}
//			        			response.sendRedirect("http://192.168.1.104:8080/ali-legal/views/login.html");
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					filterChain.doFilter(request, response);
					}
		        }
			}
        }
    }

    @Override
    public void destroy() {

    }
}