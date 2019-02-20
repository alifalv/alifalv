package com.legal.app.model;

import java.io.Serializable;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="Evaluate")
@ApiModel(value = "订单评价实体")
public class Evaluate implements Serializable{
	@ApiModelProperty(notes = "ID", required = false)
	private Integer evaluateId;
    /** 订单类型 */
    private String evaluateType;
    /** 订单号 */
    private String businessOrder;
    /** 关联id */
    private String connectionId;
	@ApiModelProperty(notes = "订单名称", required = false)
	private String orderName;
    /** 用户id */
    private Integer userId;
	@ApiModelProperty(notes = "会员类型 1：个人咨询者2：咨询师3：企业咨询者", required = false)
	private Integer userType;
    private String userTypeDescription;
    /** 用户名*/
    private String userName;
	@ApiModelProperty(notes = "昵称", required = false)
	private String nickName;
    /** 用户头像*/
    private String userImg;
    /** 法律水平打分*/
    private Integer levelScore;
    /** 服务态度打分*/
    private Integer mannerScore;
    /** 社会资源打分*/
    private Integer sourceScore;
    /** 收费打分*/
    private Integer scaleScore;
    /**综合打分 */
	@ApiModelProperty(notes = "评价分数", required = false)
	private Integer allScore;
    /** 评价内容 */
	@ApiModelProperty(notes = "评价内容", required = false)
	private String evaluateldContent;

	@ApiModelProperty(notes = "添加时间", required = false)
	private String evaluateTime;

	private Integer isDelete;

	public Integer getEvaluateId() {
		return evaluateId;
	}
	public void setEvaluateId(Integer evaluateId) {
		this.evaluateId = evaluateId;
	}
	public String getEvaluateType() {
		return evaluateType;
	}
	public void setEvaluateType(String evaluateType) {
		this.evaluateType = evaluateType;
	}
	public String getBusinessOrder() {
		return businessOrder;
	}
	public void setBusinessOrder(String businessOrder) {
		this.businessOrder = businessOrder;
	}
	public String getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
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
	public Integer getLevelScore() {
		return levelScore;
	}
	public void setLevelScore(Integer levelScore) {
		this.levelScore = levelScore;
	}
	public Integer getMannerScore() {
		return mannerScore;
	}
	public void setMannerScore(Integer mannerScore) {
		this.mannerScore = mannerScore;
	}
	public Integer getSourceScore() {
		return sourceScore;
	}
	public void setSourceScore(Integer sourceScore) {
		this.sourceScore = sourceScore;
	}
	public Integer getScaleScore() {
		return scaleScore;
	}
	public void setScaleScore(Integer scaleScore) {
		this.scaleScore = scaleScore;
	}
	public Integer getAllScore() {
		return levelScore+mannerScore+sourceScore+scaleScore;
	}
	public void setAllScore(Integer allScore) {
		this.allScore = allScore;
	}
	public String getEvaluateldContent() {
		return evaluateldContent;
	}
	public void setEvaluateldContent(String evaluateldContent) {
		this.evaluateldContent = evaluateldContent;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getUserTypeDescription() {
		if (null != userType) {
			if (1 == userType.intValue()) {
				userTypeDescription = "个人咨询者";
			} else if (2 == userType.intValue()) {
				userTypeDescription = "咨询师";
			} else if (3 == userType.intValue()) {
				userTypeDescription = "企业咨询者";
			}
		}
		return userTypeDescription;
	}

	public void setUserTypeDescription(String userTypeDescription) {
		this.userTypeDescription = userTypeDescription;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getEvaluateTime() {
		return evaluateTime;
	}

	public void setEvaluateTime(String evaluateTime) {
		this.evaluateTime = evaluateTime;
	}
}