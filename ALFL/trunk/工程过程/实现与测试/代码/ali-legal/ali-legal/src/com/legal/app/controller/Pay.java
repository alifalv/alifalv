package com.legal.app.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 第三方支付的父类，定义一些公共的工具方法及抽象方法；
 * </br>时间： 2017-06-14
 * @author hyq
 *
 */
public interface Pay {
	
	final String ALIPAY = "支付宝";
    final String WEIXIN = "微信";
	//退款订单的统一备注
	final String REFUND_REMARK="积分商城兑换退款";
	/**
	 * 生成商户商用的订单
	 * @return
	 */
	public  default  String generateTradeNo(){
		synchronized(this){
			Date now=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmssSSS");
			return sdf.format(now); 
		}
	}
	
	
}
