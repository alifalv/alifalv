package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.SysUser;
import java.util.List;

public interface ISysUserService {
    List<SysUser> list(SysUser sysUser, Paging paging) throws Exception ;

    SysUser info(Integer sysUserId) throws Exception;

    SysUser isExist(SysUser sysUser) throws Exception;

    void add(SysUser sysUser) throws Exception;

    int update(SysUser sysUser) throws Exception;

    int remove(Integer sysUserId) throws Exception;

    int lock(Integer sysUserId) throws Exception;

    int unLock(Integer sysUserId) throws Exception;
}