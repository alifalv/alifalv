package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.Banner;
import com.legal.app.model.BannerSelectParam;

import java.util.List;

public interface IBannerService {
    List<Banner> list(BannerSelectParam BannerSelectParam, Paging paging) throws Exception ;

    Banner info(Banner banner);

    void add(Banner banner) throws Exception;

    int update(Banner banner) throws Exception;

    int remove(Integer id) throws Exception;
}