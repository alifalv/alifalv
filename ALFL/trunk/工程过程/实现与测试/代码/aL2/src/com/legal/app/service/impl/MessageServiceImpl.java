package com.legal.app.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.common.dbutil.DaoMybatisImpl;
import com.legal.app.model.Message;
import com.legal.app.service.MessageService;


@Service
public class MessageServiceImpl extends DaoMybatisImpl<Message> implements MessageService{

	@Override
	public List<Map> findMessageListByUserId(int userId) throws Exception {
		// TODO Auto-generated method stub
		return super.executeQuery("findMessageListByUserId", userId);
	}

	@Override
	public int saveMessage(Map map) throws Exception {
		// TODO Auto-generated method stub
		return super.executeUpdate("saveMessage", map);
	}
}
