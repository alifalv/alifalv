package com.legal.app.model;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ConselorInfo {
	private String userId; 
	private String userName;
	private String userImg;
	private String isAuthentication;
	private String is_contract;
	private String mobile;
	private String province;
	private String city;
	private String levelScore;
	private String attitudeScore;
	private String sourceScore;
	private String complateAdviceNum;
	private String entrustNum;
	private String goodNum;
	private String job;
	private String realName;
	private String cityName;
	private String jobName;
	private List<Map> specialityName;
	public String getLevelScore() {
		return levelScore;
	}
	public void setLevelScore(String levelScore) {
		this.levelScore = levelScore;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public List<Map> getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(List<Map> specialityName) {
		this.specialityName = specialityName;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getIsAuthentication() {
		return isAuthentication;
	}
	public void setIsAuthentication(String isAuthentication) {
		this.isAuthentication = isAuthentication;
	}
	public String getIs_contract() {
		return is_contract;
	}
	public void setIs_contract(String is_contract) {
		this.is_contract = is_contract;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getAttitudeScore() {
		return attitudeScore;
	}
	public void setAttitudeScore(String attitudeScore) {
		this.attitudeScore = attitudeScore;
	}
	public String getSourceScore() {
		return sourceScore;
	}
	public void setSourceScore(String sourceScore) {
		this.sourceScore = sourceScore;
	}
	public String getComplateAdviceNum() {
		return complateAdviceNum;
	}
	public void setComplateAdviceNum(String complateAdviceNum) {
		this.complateAdviceNum = complateAdviceNum;
	}
	public String getEntrustNum() {
		return entrustNum;
	}
	public void setEntrustNum(String entrustNum) {
		this.entrustNum = entrustNum;
	}
	public String getGoodNum() {
		return goodNum;
	}
	public void setGoodNum(String goodNum) {
		this.goodNum = goodNum;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}

}
