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
@MyBatisEntity(namespace="CastReplySelectParam")
@ApiModel(value = "vip免费申报查询类")
public class CastReplySelectParam implements Serializable{
    @ApiModelProperty(notes = "类型", required = false)
    private String type;
    @ApiModelProperty(notes = "会员类型", required = false)
    private Integer userType;
    @ApiModelProperty(notes = "状态", required = false)
    private Integer replyState;
    @ApiModelProperty(notes = "创建时间开始", required = false)
    private String createTimeStart;
    @ApiModelProperty(notes = "创建时间结束", required = false)
    private String createTimeEnd;
    @ApiModelProperty(notes = "ID号、真实姓名、联系电话", required = false)
    private String content;

    private Integer size;
    private Integer page;
    private Integer start;
    private Integer length;
    private String sEcho;

    @ApiModelProperty(notes = "vip免费申报实体类", required = false)
    private CastReply castReply;

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getReplyState() {
        return replyState;
    }

    public void setReplyState(Integer replyState) {
        this.replyState = replyState;
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

    public CastReply getCastReply() {
        return castReply;
    }

    public void setCastReply(CastReply castReply) {
        this.castReply = castReply;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
