package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.RecommendedToday;
import com.legal.app.model.RecommendedTodaySelectParam;

import java.util.List;

public interface IRecommendedTodayService {
    List<RecommendedToday> list(RecommendedTodaySelectParam recommendedTodaySelectParam, Paging paging) throws Exception ;

    RecommendedToday info(RecommendedToday recommendedToday);

    void add(RecommendedToday recommendedToday) throws Exception;

    int update(RecommendedToday recommendedToday) throws Exception;

    int remove(Integer id) throws Exception;
}