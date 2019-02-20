package com.legal.app.utils;
/**
 * PS ： 可以使用PayOrder.java 类。
 * 
 * 针对 ali_business_order 中的字段对应的值  
 * 同payOrder 里有些相同 ，
 * @author 军山依旧在
 * 2018-09-27
 */
public class OrderType {
	/**
	 * businessType 的法律咨询
	 */
    public static final  String businessType_FYZX="法律咨询";
    
    /**
	 * businessType 的打赏
	 */
    public static final  String businessType_DS="打赏";
    
    /**
	 * businessType 咨询回复
	 */
    public static final  String businessType_ZXHF="咨询回复";
    
    /**
	 * businessType 法律文书制作
	 */
    public static final  String businessType_FYWSZZ="法律文书制作";
    
    /**
	 * businessType 提现
	 */
    public static final  String businessType_TX="提现";
    
    
    
    /**
     * ORDERTYPE 的用户充值到平台 
     */
    public static final int ORDERTYPE_1 = 1;
    
    /**
     * ORDERTYPE 的用户提现到自己微信  
     */
    public static final int ORDERTYPE_2 = 2;
    
    /**
     * ORDERTYPE 咨询师得到的打赏的赏金 或者是 回复咨询者的评论等 金额
     */
    public static final int ORDERTYPE_3 = 3;
    
    
    
    
    
}
