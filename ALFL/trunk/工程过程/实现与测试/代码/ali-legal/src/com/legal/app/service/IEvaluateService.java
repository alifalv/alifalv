package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.Evaluate;
import com.legal.app.model.EvaluateSelectParam;

import java.util.List;

public interface IEvaluateService {
    List<Evaluate> list(EvaluateSelectParam evaluateSelectParam, Paging paging) throws Exception ;

    Evaluate info(Integer replyId) throws Exception;

    void add(Evaluate Evaluate) throws Exception;

    int remove(Integer replyId) throws Exception;
}