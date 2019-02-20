package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.Advertising;
import com.legal.app.model.AdvertisingSelectParam;

import java.util.List;

public interface IAdvertisingService {
    List<Advertising> list(AdvertisingSelectParam advertisingSelectParam, Paging paging) throws Exception ;

    Advertising info(Advertising advertising);

    void add(Advertising advertising) throws Exception;

    int update(Advertising advertising) throws Exception;

    int remove(Integer id) throws Exception;
}