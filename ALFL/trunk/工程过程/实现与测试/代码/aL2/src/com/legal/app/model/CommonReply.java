package com.legal.app.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.common.dbutil.MyBatisEntity;

/**
 * 统一回复表
 * @author huangzh
 *
 */

@MyBatisEntity(namespace="CommonReply")
public class CommonReply implements Serializable{

	/** 回复id */
	private Integer replyId;
	/** 回复人id */
	private Integer userId;
	/** 回复人姓名 */
	private String userName;
	/** 回复人头像 */
	private String userImg;
	/** 服务名称 */
	private String serviceName;
	/** 业务类型        1 咨询回复 2 案件委托回复 3 文书制作回复 4 百姓吐槽回复 5 阿里裁决回复     */
	private Integer businessType;
	/** 回复内容 */
	private String replyContent;
	/** 回复时间 */
	private Timestamp replyTime;
	/** 关联id  取决于 业务类型     1 咨询回复  咨询id        2 案件委托回复   委托id  3 文书制作回复 文书制作 id  4 百姓吐槽回复  文章id  5 阿里裁决回复 文章id   */
	private Integer relativeId;
	public Integer getReplyId() {
		return replyId;
	}
	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
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
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Integer getBusinessType() {
		return businessType;
	}
	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public Timestamp getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(Timestamp replyTime) {
		this.replyTime = replyTime;
	}
	public Integer getRelativeId() {
		return relativeId;
	}
	public void setRelativeId(Integer relativeId) {
		this.relativeId = relativeId;
	}
	
	
	
	
}
