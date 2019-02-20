package com.legal.app.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@SuppressWarnings("serial")
@ApiModel(value = "今日推荐查询")
public class MessageSelectParam implements Serializable{
	@ApiModelProperty(notes = "状态、类型", required = false)
	private Integer state;

	@ApiModelProperty(notes = "关键字", required = false)
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

	@ApiModelProperty(notes = "消息推送对象", required = false)
	private Message message;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public void setMessage(Message message) {
		this.message = message;
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

	public Message getMessage() {
		return message;
	}
}