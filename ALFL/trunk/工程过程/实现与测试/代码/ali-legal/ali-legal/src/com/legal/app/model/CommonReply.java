package com.legal.app.model;

import java.io.Serializable;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 统一回复表
 * @author huangzh
 *
 */

@SuppressWarnings("serial")
@MyBatisEntity(namespace="CommonReply")
@ApiModel(value = "回复实体")
public class CommonReply implements Serializable{
	/** 回复id */
	@ApiModelProperty(notes = "ID", required = false)
	private Integer replyId;
	/** 回复人id */
	private Integer userId;
	/** 回复人姓名 */
	@ApiModelProperty(notes = "昵称", required = false)
	private String userName;
	private String nickName;
	/** 回复人头像 */
	private String userImg;
	@ApiModelProperty(notes = "会员类型 1 个人咨询者 3 企业咨询者 2 咨询师", required = false)
	private Integer userType;
	/** 服务名称 */
	private String serviceName;
	/** 业务类型        1 咨询回复 2 案件委托回复 3 文书制作回复 4 百姓吐槽回复 5 阿里裁判回复     */
	private Integer businessType;
	@ApiModelProperty(notes = "回复类型 1 咨询回复 2 案件委托回复 3 文书制作回复 4 百姓吐槽回复 5 阿里裁判回复", required = false)
	private String businessTypeDescription;
	/** 回复内容 */
	@ApiModelProperty(notes = "回复内容", required = false)
	private String replyContent;
	/** 回复时间 */
	@ApiModelProperty(notes = "添加时间", required = false)
	private String replyTime;
	/** 关联id  取决于 业务类型     1 咨询回复  咨询id        2 案件委托回复   委托id  3 文书制作回复 文书制作 id  4 百姓吐槽回复  文章id  5 阿里裁决回复 文章id   */
	private Integer relativeId;
	private Integer upId;
	private Integer isDelete;

	private String effect;
	private String thinking;
	private Integer origin;
	
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
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
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public Integer getRelativeId() {
		return relativeId;
	}
	public void setRelativeId(Integer relativeId) {
		this.relativeId = relativeId;
	}
	public Integer getUpId() {
		return upId;
	}
	public void setUpId(Integer upId) {
		this.upId = upId;
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

	public String getBusinessTypeDescription() {
		if (null != businessType) {
			if (3 == businessType) {
				businessTypeDescription = "法律咨询";
			} else if (4 == businessType) {
				businessTypeDescription = "案件委托";
			} else if (5 == businessType) {
				businessTypeDescription = "文书制作";
			} else if (1 == businessType) {
				businessTypeDescription = "百姓吐槽";
			} else if (2 == businessType) {
				businessTypeDescription = "阿里裁判";
			}
		}
		return businessTypeDescription;
	}

	public void setBusinessTypeDescription(String businessTypeDescription) {
		this.businessTypeDescription = businessTypeDescription;
	}
	public String getEffect() {
		return effect;
	}
	public void setEffect(String effect) {
		this.effect = effect;
	}
	public String getThinking() {
		return thinking;
	}
	public void setThinking(String thinking) {
		this.thinking = thinking;
	}
	public Integer getOrigin() {
		return origin;
	}
	public void setOrigin(Integer origin) {
		this.origin = origin;
	}
}
