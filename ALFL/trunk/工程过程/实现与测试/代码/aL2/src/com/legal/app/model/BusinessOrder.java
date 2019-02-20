package com.legal.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.common.dbutil.MyBatisEntity;

/**
 * 业务订单类
 * @author huangzh
 *
 */
@MyBatisEntity(namespace="BusinessOrder")
public class BusinessOrder implements Serializable{
	/** 订单号 */
	private String businessOrder;
	/** 订单描述 */
	private String orderName;
	/** 支付人ID */
	private Integer userId;
	/** 支付人姓名 */
    private String userName;
    /** 支付人头像 */
    private String userImg;
    /** 业务类型    1：咨询赏金  2：委托 3：文书制作  4 打赏  5 微信提现 6 银行提现*/
    private Integer businessType;
    /** 数量 */
    private Integer orderNum;
    /** 单价 */
    private BigDecimal orderPrice;
    /** 管理id    */
    private Integer connectionId;
    /** 支付时间 */
    private Timestamp orderTime;
    /** 状态  取决于 业务类型<br>
     * 1：咨询赏金  
     * 
     * 
     * */
    
    private Integer orderState;
    /** 手机号码 */
    private String mobile;
   
    
	public String getBusinessOrder() {
		return businessOrder;
	}
	public void setBusinessOrder(String businessOrder) {
		this.businessOrder = businessOrder;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public Integer getBusinessType() {
		return businessType;
	}
	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public BigDecimal getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}
	public Integer getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(Integer connectionId) {
		this.connectionId = connectionId;
	}
	public Timestamp getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}
	public Integer getOrderState() {
		return orderState;
	}
	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
