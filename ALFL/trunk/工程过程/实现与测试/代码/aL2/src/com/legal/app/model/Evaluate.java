package com.legal.app.model;
import java.io.Serializable;
import java.math.BigDecimal;

import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="Evaluate")
public class Evaluate implements Serializable{
    private Integer evaluateId;
    /** 评价类型 */
    private String evaluateType;
    /** 订单号 */
    private String businessOrder;
    /** 用户id */
    private Integer userId;
    /** 用户名*/
    private String userName;
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
    private BigDecimal allScore;
    /** 评价内容 */
    private String evaluateldContent;
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
	public BigDecimal getAllScore() {
		return allScore;
	}
	public void setAllScore(BigDecimal allScore) {
		this.allScore = allScore;
	}
	public String getEvaluateldContent() {
		return evaluateldContent;
	}
	public void setEvaluateldContent(String evaluateldContent) {
		this.evaluateldContent = evaluateldContent;
	}
    
}