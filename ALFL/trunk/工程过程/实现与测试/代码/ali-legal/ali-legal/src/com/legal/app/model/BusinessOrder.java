package com.legal.app.model;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 业务订单类
 * @author huangzh
 *
 */
@SuppressWarnings("serial")
@MyBatisEntity(namespace="BusinessOrder")
@ApiModel(value = "订单列表")
public class BusinessOrder implements Serializable{
	/** 订单号 */
	@ApiModelProperty(notes = "订单号 ID号", required = false)
	private String businessOrder;
	/** 订单描述 */
	@ApiModelProperty(notes = "名称", required = false)
	private String orderName;
	/** 支付人ID */
	private Integer userId;
	/** 支付人姓名 */
	@ApiModelProperty(notes = "咨询师 真实姓名", required = false)
	private String userName;
    /** 支付人头像 */
    private String userImg;
    @ApiModelProperty(notes = "会员类型 1：个人咨询者2：咨询师3：企业咨询者", required = false)
    private Integer userType;
    @ApiModelProperty(notes = "真实姓名", required = false)
    private String realName;
    private String consultant_realName;
    private String counselor_realName;
    private String company_realName;
    /** 业务类型 1 咨询赏金 2 案件委托 3文书制作 4 文章打赏 5 开通充值 6 评论赏金 7竞拍赏金 8 微信提现 9 银行转账 */
	private String businessType;
    /** 数量 */
    private Integer orderNum;
    /** 单价 */
    private BigDecimal orderPrice;
	@ApiModelProperty(notes = "总价 提现金额", required = false)
	private BigDecimal totalPrice;
    /** 管理id   */
    private Integer connectionId;
    /** 支付时间 */
	@ApiModelProperty(notes = "创建时间 申请", required = false)
	private String orderTime;
    /** 状态   0 未支付 1 已支付 2 处理中 3 完成  4 已退款 **/
	private Integer orderState;
	@ApiModelProperty(notes = "状态", required = false)
	private String orderStateDescription;
    /** 手机号码 */
	@ApiModelProperty(notes = "联系电话 联系人手机号码", required = false)
	private String mobile;
	@ApiModelProperty(notes = "编号", required = false)
	private Integer id;

	@ApiModelProperty(notes = "审核时间", required = false)
	private String checkTime;

	@ApiModelProperty(notes = "银行卡号", required = false)
	private String bankCardNumber;

	@ApiModelProperty(notes = "开户行", required = false)
	private String bank;

	@ApiModelProperty(notes = "支付方式", required = false)
	private String payment;
    
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
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getRealName() {
        if (null != userType) {
            if (1 == userType.intValue()) {
    				realName = consultant_realName;
            } else if (2 == userType.intValue()) {
    				realName = counselor_realName;
            } else if (3 == userType.intValue()) {
    				realName = company_realName;
            }
        }
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getConsultant_realName() {
		return consultant_realName;
	}
	public void setConsultant_realName(String consultant_realName) {
		this.consultant_realName = consultant_realName;
	}
	public String getCounselor_realName() {
		return counselor_realName;
	}
	public void setCounselor_realName(String counselor_realName) {
		this.counselor_realName = counselor_realName;
	}
	public String getCompany_realName() {
		return company_realName;
	}
	public void setCompany_realName(String company_realName) {
		this.company_realName = company_realName;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
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
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
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

	public BigDecimal getTotalPrice() {
		if (null != orderPrice && null != orderNum) {
			BigDecimal b2 = new BigDecimal(Double.valueOf(orderNum));
			totalPrice = orderPrice.multiply(b2);
		}
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderStateDescription() {
		if (null != orderState) {
			if (null != businessType && (businessType.equals("微信支付") || businessType.equals("银行卡支付"))) {
				if (1 == orderState) {
					orderStateDescription = "待审核";
				} else if (2 == orderState) {
					orderStateDescription = "提现成功";
				} else if (3 == orderState) {
					orderStateDescription = "未通过";
				}
			} else {
				if (1 == orderState) {
					orderStateDescription = "已支付";
				} else if (2 == orderState) {
					orderStateDescription = "处理中";
				} else if (3 == orderState) {
					orderStateDescription = "完成";
				} else if (4 == orderState) {
					orderStateDescription = "已退款";
				}
			}
		} else {
			orderStateDescription = "未支付";
		}
		return orderStateDescription;
	}

	public void setOrderStateDescription(String orderStateDescription) {
		this.orderStateDescription = orderStateDescription;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankCardNumber() {

		return bankCardNumber;
	}

	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	
}
