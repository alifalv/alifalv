package com.legal.app.utils;

import com.legal.app.model.SysUser;
import com.legal.app.model.User;
import com.legal.app.service.IMenuService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * authentication util
 */
@Component
public class AuthenticationUtils {
    @Resource
    IMenuService menuService;

    SessionUtils sessionUtils;

    public boolean is(String resourceCode) {
        SysUser sysUser = sessionUtils.getCurrentSysUser();

        return true;
    }
}
