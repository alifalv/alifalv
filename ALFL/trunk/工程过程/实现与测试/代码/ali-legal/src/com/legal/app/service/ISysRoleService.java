package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.SysRole;

import java.util.List;

public interface ISysRoleService {
    List<SysRole> list(SysRole sysRole, Paging paging) throws Exception ;

    SysRole info(SysRole sysRole) throws Exception;

    void add(SysRole sysRole) throws Exception;

    int update(SysRole sysRole) throws Exception;

    int remove(Integer sys_roleId) throws Exception;

    int disabled(Integer sys_roleId) throws Exception;

    int enable(Integer sys_roleId) throws Exception;
}