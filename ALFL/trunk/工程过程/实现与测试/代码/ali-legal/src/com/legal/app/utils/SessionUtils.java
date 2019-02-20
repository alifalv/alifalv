package com.legal.app.utils;

import com.legal.app.model.SysPremisson;
import com.legal.app.model.SysUser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * session util
 */
@Component
public class SessionUtils {
    public SysUser getCurrentSysUser() {
        SysUser sysUser = null;
        Object obj = RequestUtils.getRequest().getSession().getAttribute("current-sysUser-session-key");
        if (null != obj) {
            sysUser = (SysUser) obj;
        }

        // 测试使用
        sysUser = new SysUser();
        sysUser.setSys_userId(1);
        System.out.println("sysUser = " + sysUser.getSys_userId());

        return sysUser;
    }

    public void setCurrentSysUser(SysUser sysUser) {
        HttpSession session = RequestUtils.getRequest().getSession();
        // 设置 session 永不过期
        session.setMaxInactiveInterval(0);
                session.setAttribute("current-user-session-key", sysUser);
    }

    @SuppressWarnings("unchecked")
	public List<SysPremisson> getUserSysPremissonList() {
        List<SysPremisson> sysPremissonList = null;
        Object obj = RequestUtils.getRequest().getSession().getAttribute("current-sysUser-premisson-session-key");
        if (null != obj) {
            sysPremissonList = (List<SysPremisson>) obj;
        }
        return sysPremissonList;
    }

    public void setUserSysPremissonList(List<SysPremisson> sysPremissonList) {
        HttpSession session = RequestUtils.getRequest().getSession();
        // 设置 session 永不过期
        session.setMaxInactiveInterval(0);
                session.setAttribute("current-sysUser-premisson-session-key", sysPremissonList);
    }
}
