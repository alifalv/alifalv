package com.legal.app.model;

import java.math.BigDecimal;
import java.io.Serializable;

import com.common.dbutil.MyBatisEntity;
import com.legal.app.utils.SystemConfigUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="Advice")
@ApiModel(value = "咨询实体")
public class Advice implements Serializable{
    @ApiModelProperty(notes = "主键id", required = false)
    private Integer adviceId;
    @ApiModelProperty(notes = "咨询者id", required = false)
    private Integer userId;
    @ApiModelProperty(notes = "发布人", required = false)
    private String userName;
    private String userImg;
    private Integer caseType;
    @ApiModelProperty(notes = "类型", required = false)
    private String caseTypeDescription;
    @ApiModelProperty(notes = "赏金", required = false)
    private BigDecimal reward;
    @ApiModelProperty(notes = "添加时间", required = false)
    private String sendTime;
    @ApiModelProperty(notes = "状态", required = false)
    private Integer state;
    private String stateDescription;
    /** 标题 */
    @ApiModelProperty(notes = "标题", required = false)
    private String title;

    @ApiModelProperty(notes = "是否删除 1 已删除", required = false)
    private Integer isDelete;
    
    private String adviceContent;

    @ApiModelProperty(notes = "阅读人数", required = false)
    private Integer readNum;

    @ApiModelProperty(notes = "回复人数", required = false)
    private Integer replyNumber;

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public void setAdviceId(Integer adviceId){
        this.adviceId=adviceId;
    }
    public Integer getAdviceId(){
        return adviceId;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    
    public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setCaseType(Integer caseType){
        this.caseType=caseType;
    }
    public Integer getCaseType(){
        return caseType;
    }
    public void setReward(BigDecimal reward){
        this.reward=reward;
    }
    public BigDecimal getReward(){
        return reward;
    }
    public void setSendTime(String sendTime){
        this.sendTime=sendTime;
    }
    public String getSendTime(){
        return sendTime;
    }
    public void setState(Integer state){
        this.state=state;
    }
    public Integer getState(){
        return state;
    }
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

    public String getCaseTypeDescription() {
        if (null != caseType) {
        		caseTypeDescription = SystemConfigUtil.getValue((int)caseType, SystemConfigUtil.TYPE_CASE);
        }
        return caseTypeDescription;
    }

    public void setCaseTypeDescription(String caseTypeDescription) {
        this.caseTypeDescription = caseTypeDescription;
    }

    public String getStateDescription() {
        if (null != state) {
            if (1 == state.intValue()) {
                stateDescription = "待支付";
            } else if (2 == state.intValue()) {
                stateDescription = "待解答";
            } else if (3 == state.intValue()) {
                stateDescription = "已完成";
            }
        }
        return stateDescription;
    }

    public void setStateDescription(String stateDescription) {
        this.stateDescription = stateDescription;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getReplyNumber() {
        return replyNumber;
    }

    public void setReplyNumber(Integer replyNumber) {
        this.replyNumber = replyNumber;
    }

	public String getAdviceContent() {
		return adviceContent;
	}

	public void setAdviceContent(String adviceContent) {
		this.adviceContent = adviceContent;
	}
}