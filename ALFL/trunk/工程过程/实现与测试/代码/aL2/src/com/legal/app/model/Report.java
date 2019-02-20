package com.legal.app.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.common.dbutil.MyBatisEntity;


@MyBatisEntity(namespace="Report")
public class Report implements Serializable{

	
	private Integer reportId;
	
	private String reportTitle;
	/** 1 法律培训    2 招聘 */
	private Integer reportType;
	
	private Integer userId;
	
	private String userName;
	private String mobile;
	
	/**举报时间*/
	private Timestamp reportTime;
	
	
	/** 没有处理时间  去掉这一列  经过确认的*/
	
	

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Timestamp getReportTime() {
		return reportTime;
	}

	public void setReportTime(Timestamp reportTime) {
		this.reportTime = reportTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
