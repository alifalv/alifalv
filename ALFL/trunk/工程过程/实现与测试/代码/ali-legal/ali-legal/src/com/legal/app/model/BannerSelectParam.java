package com.legal.app.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@SuppressWarnings("serial")
@ApiModel(value = "Banner查询")
public class BannerSelectParam implements Serializable{
	@ApiModelProperty(notes = "所属栏目", required = false)
	private Integer columnType;
	private Integer type;

	private Integer size;
	private Integer page;
	private Integer start;
	private Integer length;
	private String sEcho;

	@ApiModelProperty(notes = "列表对象", required = false)
	private Banner banner;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getColumnType() {
		return columnType;
	}

	public void setColumnType(Integer columnType) {
		this.columnType = columnType;
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

	public Banner getBanner() {
		return banner;
	}

	public void setBanner(Banner banner) {
		this.banner = banner;
	}
}