package com.legal.app.model;

import java.io.Serializable;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@SuppressWarnings("serial")
@MyBatisEntity(namespace="Report")
@ApiModel(value = "举报实体")
public class Report implements Serializable{
	@ApiModelProperty(notes = "编号", required = false)
	private Integer reportId;

	@ApiModelProperty(notes = "反馈信息", required = false)
	private String reportTitle;
	/** 1 法律培训    2 招聘 */
	private Integer reportType;
	@ApiModelProperty(notes = "反馈类型", required = false)
	private String reportTypeDescription;
	
	private Integer userId;

	@ApiModelProperty(notes = "姓名", required = false)
	private String userName;
	@ApiModelProperty(notes = "联系方式", required = false)
	private String mobile;
	
	/**举报时间*/
	@ApiModelProperty(notes = "时间", required = false)
	private String reportTime;

	@ApiModelProperty(notes = "处理时间", required = false)
	private String processingTime;
	
	
	/** 没有处理时间  去掉这一列  经过确认的*/
	private Integer isDelete;
	private Integer reportBusinessId;

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

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getReportTypeDescription() {
		if (null != reportType) {
			if (1 == reportType.intValue()) {
				reportTypeDescription = "法律培训";
			} else if (2 == reportType.intValue()) {
				reportTypeDescription = "招聘岗位";
			}
		}
		return reportTypeDescription;
	}

	public void setReportTypeDescription(String reportTypeDescription) {
		this.reportTypeDescription = reportTypeDescription;
	}

	public String getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(String processingTime) {
		this.processingTime = processingTime;
	}

	public Integer getReportBusinessId() {
		return reportBusinessId;
	}

	public void setReportBusinessId(Integer reportBusinessId) {
		this.reportBusinessId = reportBusinessId;
	}
}
