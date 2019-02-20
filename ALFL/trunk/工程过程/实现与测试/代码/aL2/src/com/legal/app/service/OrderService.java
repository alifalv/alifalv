package com.legal.app.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.common.dbutil.Paging;
import com.legal.app.model.BusinessOrder;

public interface OrderService extends Dao<BusinessOrder>{

	/**
	 * 保存支付订单
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public int saveBusinessOrder(Map m) throws Exception;
	
	/**
	 * 查询订单情况
	 * @param map
	 * @param paging
	 * @return
	 * @throws Exception
	 */
	public List  listBusinessOrder(Map map, Paging paging) throws Exception;
	
	/**
	 * 查询支付订单详情 ,有联表查询
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List findOrderDetails(Map map) throws Exception;
	
	
	
	/**
	 * 查询出支付订单的对象（没有联表查询，只查自己本身的数据）
	 * @param businessOrder 订单Id
	 * @return
	 * @throws Exception
	 */
	public Object   findOrderDetails(String businessOrder) throws Exception;
	
	/**
	 * 修改  BusinessOrder  的信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int update(Map map) throws Exception;
	
	
	/**
	 * 订单评价时，需要展示的数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List listOrderEvaluate(Map map) throws Exception;
	
	/**
	 * 订单回调成功后，
	 * 根据不同业务的订单，处理不同的回调事情；
	 * 
	 * PS： 此处不验签，订单的回调，只根据 type 的状态 来处理该订单的业务
	 * 
	 * @param businessOrder  系统订单编号 
 	 * @param outTradeNo  交易公司订单编号
	 * @param type      支付类型【0：成功，1：失败】
	 * @param openid 微信用户id
	 * @throws Exception
	 */
	public void  orderPaySuccess(String businessOrder,String outTradeNo, int type,String openid ) throws Exception;
	
	
	/**
	 * 根据订单的来源类型和订单的来源Id 把订单关闭； 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public  int updateOrderByconnectionId(Map map) throws Exception;
}
