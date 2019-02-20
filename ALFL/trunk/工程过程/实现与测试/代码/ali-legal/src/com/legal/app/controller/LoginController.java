package com.legal.app.controller;

import com.common.utils.MD5Util;
import com.common.web.WebUtils;
import com.legal.app.model.SysUser;
import com.legal.app.service.IMenuService;
import com.legal.app.service.ISysUserService;
import com.legal.app.utils.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Api(value = "登录", description = "登录")
@RestController
@CrossOrigin
@RequestMapping("login")
public class LoginController extends SuperController {
    @Autowired
    ISysUserService sysUserService;

    @Autowired
    IMenuService menuService;

    @Autowired
    SessionUtils sessionUtils;

    @SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/index")
    public ModelAndView show() {
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object execute(@RequestBody SysUser user,HttpServletRequest request) {
    		if(user.getSys_userName()==null || user.getSys_userPassword()==null 
    				|| user.getSys_userName().equals("") || user.getSys_userPassword().equals("")) {
    			return "{\"code\":-1,\"data\":\"用户名或密码不能为空\"}";
    		}
        try {
        		user.setSys_userPassword(MD5Util.MD5(user.getSys_userPassword()));
            SysUser systemUser = sysUserService.isExist(user);
            if (null != systemUser) {
	        		if(systemUser.getIsDelete().equals(1)) {
	        			return "{\"code\":-1,\"data\":\"该用户已删除\"}";
	        		}
            		if(systemUser.getSys_state().equals(2)) {
            			return "{\"code\":-1,\"data\":\"该用户已锁定\"}";
            		}
            		request.getSession().setAttribute("mmuserId", systemUser.getSys_userId().toString());
	    			String token ="";
	    			try {
	    				token = com.legal.app.utils.WebUtilCahe.creationToken(systemUser.getSys_userId().toString());
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	    			request.getSession().setAttribute("BIZ_CHECKTOKEN_"+systemUser.getSys_userId().toString(),token);
                return "{\"code\":1,\"sysuserId\":"+systemUser.getSys_userId()+",\"token\":\""+token+"\"}";
            } else {
                return "{\"code\":-1,\"data\":\"用户名或密码不正确\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "是否登录", notes = "是否登录")
    @RequestMapping(value = "is")
    public Boolean is() {
        boolean isLogin = false;
        SysUser sysUser = sessionUtils.getCurrentSysUser();
        System.out.println("主系统用户是否登录：" + (null != sysUser));
        if (null != sysUser) {
            isLogin = true;
        }
        return isLogin;
    }

    @ResponseBody
    @ApiOperation(value = "退出、注销", responseContainer = "Map")
    @RequestMapping(value = "out", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void logout(HttpServletRequest request, PrintWriter out) throws IOException {
    		//false代表：不创建session对象，只是从request中获取。
		HttpSession session = request.getSession(false);
		if(session==null){
			return;
		}
		session.removeAttribute("mmuserId");
        out.print(WebUtils.responseData(1));
    }
}
