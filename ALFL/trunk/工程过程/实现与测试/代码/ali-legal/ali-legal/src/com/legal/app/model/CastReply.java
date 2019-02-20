package com.legal.app.model;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="CastReply")
@ApiModel(value = "vip免费申报实体类")
public class CastReply implements Serializable{
    @ApiModelProperty(notes = "ID", required = false)
    private Integer id;
    private Integer userId;
    @ApiModelProperty(notes = "会员类型 1：个人咨询者2：咨询师3：企业咨询者", required = false)
    private Integer userType;
    @ApiModelProperty(notes = "真实姓名", required = false)
    private String userName;
    private String consultant_realName;
    private String counselor_realName;
    private String company_realName;
    @ApiModelProperty(notes = "联系电话", required = false)
    private String mobile;
    private Integer replyState;
    @ApiModelProperty(notes = "状态：0待审核，1已通过，2未通过", required = false)
    private String replyStateDescription;
    @ApiModelProperty(notes = "申请时间", required = false)
    private String replyTime;
    @ApiModelProperty(notes = "审核时间", required = false)
    private String completeTime;
    @ApiModelProperty(notes = "类型", required = false)
    private String type;
    private Integer voucherId;

    public void setId(Integer id){
        this.id=id;
    }
    public Integer getId(){
        return id;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setReplyState(Integer replyState){
        this.replyState=replyState;
    }
    public Integer getReplyState(){
        return replyState;
    }
    public void setReplyTime(String replyTime){
        this.replyTime=replyTime;
    }
    public String getReplyTime(){
        return replyTime;
    }

    public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getUserName() {
        if (null != userType) {
            if (1 == userType.intValue()) {
    				userName = consultant_realName;
            } else if (2 == userType.intValue()) {
    				userName = counselor_realName;
            } else if (3 == userType.intValue()) {
    				userName = company_realName;
            }
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
	public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReplyStateDescription() {
		if (null != replyState) {
			if (0 == replyState) {
				replyStateDescription = "待审核";
			} else if (1 == replyState) {
				replyStateDescription = "已通过";
			} else if (2 == replyState) {
				replyStateDescription = "未通过";
			}
		} else {
			replyStateDescription = "待审核";
		}
        return replyStateDescription;
    }

    public void setReplyStateDescription(String replyStateDescription) {
        this.replyStateDescription = replyStateDescription;
    }
    public String getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(Integer voucherId) {
		this.voucherId = voucherId;
	}
}