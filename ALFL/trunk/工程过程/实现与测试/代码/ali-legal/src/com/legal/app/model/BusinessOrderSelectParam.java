package com.legal.app.model;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 业务订单类
 * @author huangzh
 *
 */
@SuppressWarnings("serial")
@MyBatisEntity(namespace="BusinessOrderSelectParam")
@ApiModel(value = "业务订单查询类")
public class BusinessOrderSelectParam implements Serializable{
	@ApiModelProperty(notes = "订单名称、订单编号、联系电话", required = false)
	private String content;
	@ApiModelProperty(notes = "会员类型", required = false)
	private Integer userType;
	@ApiModelProperty(notes = "状态", required = false)
	private Integer orderState;
	@ApiModelProperty(notes = "类型", required = false)
	private String businessType;
	@ApiModelProperty(notes = "支付方式", required = false)
	private String payment;
	@ApiModelProperty(notes = "创建时间开始", required = false)
	private String createTimeStart;
	@ApiModelProperty(notes = "创建时间结束", required = false)
	private String createTimeEnd;

	private Integer size;
	private Integer page;
	private Integer start;
	private Integer length;
	private String sEcho;

	@ApiModelProperty(notes = "订单对象", required = false)
	private BusinessOrder businessOrder;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getsEcho() {
		return sEcho;
	}

	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public BusinessOrder getBusinessOrder() {
		return businessOrder;
	}

	public void setBusinessOrder(BusinessOrder businessOrder) {
		this.businessOrder = businessOrder;
	}
	
	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

}
