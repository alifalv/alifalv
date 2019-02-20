package com.legal.app.model;

import java.io.Serializable;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 敏感词
 * @author admin
 *
 */
@SuppressWarnings("serial")
@MyBatisEntity(namespace="Sensitive")
@ApiModel(value = "敏感词库")
public class Sensitive implements Serializable{
	@ApiModelProperty(notes = "主键id", required = false)
	private Integer sensitiveId;
	@ApiModelProperty(notes = "关键词", required = false)
	private String  sensitiveValue;
	private Integer is_delete;

	private Integer size;
	private Integer page;
	private Integer start;
	private Integer length;
	private String sEcho;

	public Integer getSensitiveId() {
		return sensitiveId;
	}
	public void setSensitiveId(Integer sensitiveId) {
		this.sensitiveId = sensitiveId;
	}
	public String getSensitiveValue() {
		return sensitiveValue;
	}
	public void setSensitiveValue(String sensitiveValue) {
		this.sensitiveValue = sensitiveValue;
	}

	public Integer getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(Integer is_delete) {
		this.is_delete = is_delete;
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
