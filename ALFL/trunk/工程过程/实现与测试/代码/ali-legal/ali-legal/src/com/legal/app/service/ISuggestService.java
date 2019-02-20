package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.Suggest;

import java.util.List;

public interface ISuggestService {
    List<Suggest> list(Suggest suggest, Paging paging) throws Exception ;

    Suggest info(Integer suggestId) throws Exception;

    void add(Suggest suggest) throws Exception;

    int reply(Suggest suggest) throws Exception;

    int update(Suggest suggest) throws Exception;

    int remove(Integer suggestId) throws Exception;

	int numberOfSuggest() throws Exception;
}