package com.legal.app.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "回顾管理案源")
public class CommonReplySelectParam {
    @ApiModelProperty(notes = "ID", required = false)
    private Integer replyId;

    @ApiModelProperty(notes = "会员类型 1 个人咨询者 2 企业咨询者 3 咨询师", required = false)
    private Integer userType;

    @ApiModelProperty(notes = "回复类型 1 法律咨询 2 案件委托 4 百姓吐槽 5 阿里裁判", required = false)
    private Integer businessType;

    @ApiModelProperty(notes = "名称、回复内容", required = false)
    private String content;

    @ApiModelProperty(notes = "时间开始", required = false)
    private String addTimeStart;
    @ApiModelProperty(notes = "时间结束", required = false)
    private String addTimeEnd;

    private Integer size;
    private Integer page;
    private Integer start;
    private Integer length;
    private String sEcho;

    @ApiModelProperty(notes = "回复实体", required = false)
    private CommonReply commonReply;

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTimeStart() {
        return addTimeStart;
    }

    public void setAddTimeStart(String addTimeStart) {
        this.addTimeStart = addTimeStart;
    }

    public String getAddTimeEnd() {
        return addTimeEnd;
    }

    public void setAddTimeEnd(String addTimeEnd) {
        this.addTimeEnd = addTimeEnd;
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

    public CommonReply getCommonReply() {
        return commonReply;
    }

    public void setCommonReply(CommonReply commonReply) {
        this.commonReply = commonReply;
    }
}
