package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.Message;
import com.legal.app.model.MessageSelectParam;

import java.util.List;
import java.util.Map;

public interface IMessageService {
    List<Message> list(MessageSelectParam MessageSelectParam, Paging paging) throws Exception ;

    Message info(Message message);

    void add(Message message) throws Exception;

    int update(Message message) throws Exception;

    int remove(Integer messageId) throws Exception;

    int send(Integer messageId) throws Exception;

	@SuppressWarnings("rawtypes")
	int messageSend(Map map) throws Exception;
}