package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.Message;
import com.legal.app.model.MessageSelectParam;
import com.legal.app.model.User;
import com.legal.app.service.Constant;
import com.legal.app.service.IMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageService extends DaoMybatisImpl<Message> implements IMessageService {
    @Autowired
    private UserService userService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Message> list(MessageSelectParam MessageSelectParam, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("state", MessageSelectParam.getState());
		map.put("content", MessageSelectParam.getContent());
		map.put("addTimeStart", MessageSelectParam.getAddTimeStart());
		map.put("addTimeEnd", MessageSelectParam.getAddTimeEnd());
		List list = super.executeQuery("selectPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}

	@Override
	public Message info(Message message) {
		try {
			message = (Message) super.executeQueryOne("queryInfo", message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	@Override
	public void add(Message message) {
		super.executeUpdate("insert", message);
	}

	@Override
	public int update(Message message) {
		return super.executeUpdate("update", message);
	}

	@Override
	public int remove(Integer messageId) throws Exception {
		Message Message = new Message();
		Message.setMessageId(messageId); 
		Message.setIsDelete(Constant.DELETED);
		return super.executeUpdate("update", Message);
	}

	@Override
	public int send(Integer messageId) throws Exception {
		Message message = new Message();
		message.setMessageId(messageId);
		Message msg = info(message);
		msg.setIs_send(Constant.SENDED);
		msg.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		User user = new User();
		List<User> userlist = userService.queryUser(user);
		for(User list : userlist) {
			msg.setUserId(list.getUserId());
			super.executeUpdate("messageSend", msg);
		}
		message.setIs_send(Constant.SENDED);
		message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		return super.executeUpdate("update", message);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int messageSend(Map map) throws Exception {
		return super.executeUpdate("messageSend", map);
	}
}
