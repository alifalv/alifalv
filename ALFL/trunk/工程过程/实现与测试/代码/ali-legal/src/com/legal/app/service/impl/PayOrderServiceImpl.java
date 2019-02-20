package com.legal.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.legal.app.service.PayOrderService;

@Service
public class PayOrderServiceImpl implements PayOrderService {

	@Override
	public void insertPayOrder(String plateform, String appId, String outTradeNo, int totalFee, String tradeNo,
			String seller_id) throws Exception {
		
	}

	@Override
	public void insertRefundOrder(String appId, String outTradeNo, int totalFee, String tradeNo,
			String orderTimeCompleted, String payStatus) throws Exception {

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getPayOrder(String outTradeNo) throws Exception {
		return null;
	}

	@Override
	public int upatePayOrder(String outTradeNo, String orderTimeCompleted, String payStatus, String tradeNo,
			String buyer) throws Exception {
		return 0;
	}

	@Override
	public List<Map<String, String>> queryRefundList() throws Exception {
		//以下是测试用的；
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		Map<String,String> map=new HashMap<String,String>();
		map.put("amount", "1");
		map.put("openId", "o55LOw4bKRR4N87jKO6g5iEg45xY");
		map.put("userName", "黄宇清");
		list.add(map);
		return list;
	}

	@Override
	public int updateRefundStatus(String payeeAccount, boolean isSuccess, String outBizNo, String tradeOrderNo,
			String plateform) throws Exception {
		return 0;
	}

}
