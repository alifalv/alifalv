package com.legal.app.model;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@MyBatisEntity(namespace="Message")
@ApiModel(value = "消息实体")
public class Message {
    @ApiModelProperty(notes = "ID", required = false)
    private Integer messageId;

    private Integer userId;

    @ApiModelProperty(notes = "发送人", required = false)
    private String sys_userName;

    private String messageContent;

    private String businessId;

    private String businessType;

    @ApiModelProperty(notes = "发送时间", required = false)
    private String sendTime;

    private String messageType;

    private Integer isRead;

    private String messageTitle;

    private String addresser;

    private Integer messageState;

    private Integer isDelete;

    @ApiModelProperty(notes = "是否返送", required = false)
    private Integer is_send;
    private String sysStateDescription;

    @ApiModelProperty(notes = "标题", required = false)
    private String title;

    private String create_time;

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent == null ? null : messageContent.trim();
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle == null ? null : messageTitle.trim();
    }

    public String getAddresser() {
        return addresser;
    }

    public void setAddresser(String addresser) {
        this.addresser = addresser == null ? null : addresser.trim();
    }

    public Integer getMessageState() {
        return messageState;
    }

    public void setMessageState(Integer messageState) {
        this.messageState = messageState;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getIs_send() {
        return is_send;
    }

    public void setIs_send(Integer is_send) {
        this.is_send = is_send;
    }

    public String getSysStateDescription() {
        if (null != is_send) {
            if (1 == is_send.intValue()) {
                sysStateDescription = "已发送";
            }else {
                sysStateDescription = "未发送";
            }
        } else {
            sysStateDescription = "未发送";
        }
        return sysStateDescription;
    }

    public void setSysStateDescription(String sysStateDescription) {
        this.sysStateDescription = sysStateDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSys_userName() {
        return sys_userName;
    }

    public void setSys_userName(String sys_userName) {
        this.sys_userName = sys_userName;
    }
}