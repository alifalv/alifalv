package com.legal.app.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@ApiModel(value = "查询参数")
public class ResearchParameter implements Serializable {
	@ApiModelProperty(notes = "类型", required = false)
	private Integer type;
	@ApiModelProperty(notes = "状态", required = false)
	private Integer state;

	@ApiModelProperty(notes = "内容", required = false)
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
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
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
}
