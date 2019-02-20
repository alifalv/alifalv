package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.Advice;
import com.legal.app.model.AdviceSelectParam;

import java.util.List;
import java.util.Map;

public interface IAdviceService {
    List<Advice> list(AdviceSelectParam adviceSelectParam, Paging paging) throws Exception ;

    Advice info(Integer adviceId) throws Exception;

    void add(Advice advice) throws Exception;

    int remove(Integer adviceId) throws Exception;

    int numbersOfAdvice() throws Exception;

	List<Map<String, String>> numbersOfAdviceType() throws Exception;
}