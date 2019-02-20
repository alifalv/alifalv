package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.Sensitive;

import java.util.List;

public interface ISensitiveService {
    List<Sensitive> list(Sensitive sensitive, Paging paging) throws Exception ;

    Sensitive info(Integer sensitiveId) throws Exception;

    void add(Sensitive sensitive) throws Exception;

	int update(Sensitive sensitive) throws Exception;

    int remove(Integer sensitiveId) throws Exception;
}