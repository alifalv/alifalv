package com.legal.app.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.log.BusinessException;
import com.common.utils.StringUtil;
import com.common.web.WebUtils;
import com.legal.app.service.MessageService;

@Controller
@RequestMapping("message")
public class MessageController extends SuperController{

	@Autowired
	private MessageService messageService;
	
	
	/**
	 * 获取消息
	 * @param request
	 * @param out
	 * @param userId
	 * @throws Exception
	 */
	@RequestMapping("findMessageByUserId/{userId}")
	public void getMessage(HttpServletRequest request,PrintWriter out,
			@PathVariable("userId") int userId) throws Exception{
		
		List<Map> list = messageService.findMessageListByUserId(userId);
		
		out.print(WebUtils.responseData(list));
	}
	
	/**
	 * 发送消息
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("sendMessage")
	public void sendMessage(HttpServletRequest request,PrintWriter out) throws Exception{
		
		Map m = new HashMap();
		
		String userId = request.getParameter("userId");
		if(StringUtil.isBlank(userId)){
			throw new BusinessException("缺失userId", -1);
		}
		
		m.put("userId",Integer.parseInt(userId));
		
		//userId  == 0  后台发送的
		if(Integer.parseInt(userId) == 0){
			
		}
		
		String messageContent = request.getParameter("messageContent");
		
	}
	
}
