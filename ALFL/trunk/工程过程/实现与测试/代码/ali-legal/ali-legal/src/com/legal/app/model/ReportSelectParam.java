package com.legal.app.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@SuppressWarnings("serial")
@ApiModel(value = "举报管理查询")
public class ReportSelectParam implements Serializable{
	@ApiModelProperty(notes = "开始时间", required = false)
	private String reportTimeStart;

	@ApiModelProperty(notes = "结束时间", required = false)
	private String reportTimeEnd;

	@ApiModelProperty(notes = "类型", required = false)
	private Integer reportType;

	@ApiModelProperty(notes = "内容关键词", required = false)
	private String content;

	private Integer size;
	private Integer page;
	private Integer start;
	private Integer length;
	private String sEcho;

	@ApiModelProperty(notes = "举报实体", required = false)
	private Report report;

	public String getReportTimeStart() {
		return reportTimeStart;
	}

	public void setReportTimeStart(String reportTimeStart) {
		this.reportTimeStart = reportTimeStart;
	}

	public String getReportTimeEnd() {
		return reportTimeEnd;
	}

	public void setReportTimeEnd(String reportTimeEnd) {
		this.reportTimeEnd = reportTimeEnd;
	}

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
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

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}
}
