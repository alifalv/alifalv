package com.legal.app.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.legal.app.model.PayOrder;

public interface PayOrderService  extends Dao<PayOrder> {
	
	public int save(Map m) throws Exception;
	

	/**
	 *  新增一个支付订单
	 * @param plateform 微信|支付宝
	 * @param appId 应用id
	 * @param outTradeNo 商户订单号
	 * @param totalFee 支付金额	
	 * @param tradeNo 支付平台订单号
	 * @throws Exception
	 */
	public void insertPayOrder(String plateform,String appId,String outTradeNo,int totalFee,String tradeNo,String seller_id)throws Exception;
	
	
	/**
	 * 新增一个退款的支付订单，
	 * @param appId 应用id
	 * @param outTradeNo 商户订单号
	 * @param totalFee 支付金额	
	 * @param tradeNo 支付平台订单号
	 * @param payStatus 支付结果状态 
	 * @param orderTimeCompleted 支付时间
	 * @throws Exception
	 */
	public void insertRefundOrder(String appId,String outTradeNo,int totalFee,String tradeNo,String orderTimeCompleted,String payStatus)throws Exception;
	
	
	/**
	 * 根据订单号，查找到该订单信息
	 * @param outTradeNo 商户订单号
	 * @return 返回一个订单的全 部信息
	 */
	public Map getPayOrder(String outTradeNo)throws Exception;
	
	/**
	 * 将支付结果数据，更新入数据库
	 * @param outTradeNo 订单商务户号
	 * @param orderTimeCompleted 支付时间
	 * @param payStatus 支付结果
	 * @param tradeNo 支付平台的订单号
	 * @param buyer 支付方的帐号（支付帐号，或 微信的openid);
	 * @return
	 */
	public int upatePayOrder(String outTradeNo,String orderTimeCompleted,String payStatus,String tradeNo,String buyer)throws Exception;

	/**
	 * 查询出需要商户转款的用户信息列表数据；
	 * @return 
	 * 一个MAP的集合；map的结构:
	 * [{
	 *   "openId":"收款方所在商户的openid",
	 *   "amount":"转帐金额(单位为分，整型）",
	 *   "userName":"用户的真实姓名"
	 *   }
	 * ]
	 * 
	 */
	public List<Map<String,String>> queryRefundList() throws Exception;

	/**
	 * 修改退款(商户转帐）状态
	 * @param goodIds 退款商品ID
	 * @param payeeAccount 修改的帐号
	 * @param isSuccess 是否退款成功
	 * @param outBizNo 商务的转帐订单号
	 * @param tradeOrderNo 支付平台的转帐订单号
	 * @param plateform 支付平台 ： 微信|支付宝
	 * @return 成功修改返回1,否则返回0
	 * @throws Exception
	 */
	public int updateRefundStatus(String payeeAccount,boolean isSuccess,String outBizNo,String tradeOrderNo,String plateform) throws Exception;


}
