package com.legal.app.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@SuppressWarnings("serial")
@ApiModel(value = "咨询列表查询")
public class AdviceSelectParam implements Serializable{
    @ApiModelProperty(notes = "类型", required = false)
    private Integer caseType;

    @ApiModelProperty(notes = "时间开始", required = false)
    private String sendTimeStart;

    @ApiModelProperty(notes = "时间结束", required = false)
    private String sendTimeEnd;

    @ApiModelProperty(notes = "标题 发布人", required = false)
    private String content;

    private Integer size;
    private Integer page;
    private Integer start;
    private Integer length;
    private String sEcho;

    @ApiModelProperty(notes = "咨询实体", required = false)
    private Advice advice;

    public Integer getCaseType() {
        return caseType;
    }

    public void setCaseType(Integer caseType) {
        this.caseType = caseType;
    }

    public String getSendTimeStart() {
        return sendTimeStart;
    }

    public void setSendTimeStart(String sendTimeStart) {
        this.sendTimeStart = sendTimeStart;
    }

    public String getSendTimeEnd() {
        return sendTimeEnd;
    }

    public void setSendTimeEnd(String sendTimeEnd) {
        this.sendTimeEnd = sendTimeEnd;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }
}