package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.CommonReply;
import com.legal.app.model.CommonReplySelectParam;

import java.util.List;

public interface IReplyService {
    List<CommonReply> list(CommonReplySelectParam commonReplySelectParam, Paging paging) throws Exception ;

    CommonReply info(Integer replyId) throws Exception;

    void add(CommonReply commonReply) throws Exception;

    int reply(CommonReply commonReply) throws Exception;

    int update(CommonReply commonReply) throws Exception;

    int remove(Integer replyId,Integer origin) throws Exception;
}