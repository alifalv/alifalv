package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.BusinessOrder;
import com.legal.app.model.BusinessOrderSelectParam;

import java.util.List;
import java.util.Map;

public interface IOrderService {
    List<BusinessOrder> list(BusinessOrderSelectParam businessOrderSelectParam, Paging paging) throws Exception ;

    BusinessOrder info(String businessOrder) throws Exception;

    int check(String businessOrder) throws Exception;

    int checkUn(String businessOrder) throws Exception;

    int totalCount(Map<String,Object> map) throws Exception;
}
