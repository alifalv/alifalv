package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.SysMapping;

import java.util.List;

public interface ISysMappingService {
    List<SysMapping> list(SysMapping sysMapping, Paging paging) throws Exception ;

    List<SysMapping> info(Integer roleId) throws Exception;

    void add(SysMapping sysMapping) throws Exception;

    void delete(Integer roleId) throws Exception;

    List<SysMapping> rolemapping() throws Exception;
}