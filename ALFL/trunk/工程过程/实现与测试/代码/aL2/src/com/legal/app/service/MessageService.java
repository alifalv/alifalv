package com.legal.app.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.legal.app.model.Message;

public interface MessageService extends Dao<Message>{

	
	public  List<Map> findMessageListByUserId(int userId) throws Exception;
	
	public  int saveMessage(Map map) throws Exception;
	
}
