package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.CastReply;
import com.legal.app.model.CastReplySelectParam;

import java.util.List;

public interface ICastReplyService {
    List<CastReply> list(CastReplySelectParam castReplySelectParam, Paging paging) throws Exception ;

    CastReply info(Integer replyId) throws Exception;

    void add(CastReply castReply) throws Exception;

    int update(CastReply castReply) throws Exception;

    int check(Integer replyId) throws Exception;

    int numbersOfLegalDocumentProduction() throws Exception;

	int uncheck(Integer replyId) throws Exception;

	int numbersOfTrafficCase() throws Exception;
}