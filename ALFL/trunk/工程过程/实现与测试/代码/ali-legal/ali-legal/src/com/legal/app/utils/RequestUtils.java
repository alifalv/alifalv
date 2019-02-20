/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.legal.app.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Request util
 */
public class RequestUtils {

    /**
     * 取request
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 判断一个请求是不是ajax请求
     *
     * @return Boolean
     */
    public Boolean isAjaxRequest() {
        String xr = RequestUtils.getRequest().getHeader("x-requested-with");
        Boolean result = false;
        if (xr != null && !"".equals(xr) && xr.equals("XMLHttpRequest")) {
            result = true;
        }
        return result;
    }

    /**
     * 获取用户浏览器信息
     *
     * @return String
     */
    public static String getUserAgent() {
        String result = "";
        result = getRequest().getHeader("User-Agent");
        return result;
    }

    /**
     * 获取客户端ip
     *
     * @return String ip
     */
    public static String getClientIP() {
        String ip = "";
        HttpServletRequest request = RequestUtils.getRequest();
        ip = request.getHeader("X-Real-IP");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 判断一个字符串,是不是ip字符串
     *
     * @param ipStr String
     * @return boolean
     */
    public static boolean isIpString(String ipStr) {
        String regex = "(((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(ipStr);
        return m.matches();
    }
}
