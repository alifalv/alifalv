package com.legal.app.model;

import java.io.Serializable;

import com.common.dbutil.MyBatisEntity;
import com.legal.app.controller.AddressController;
import com.legal.app.utils.SystemConfigUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="CaseDepute")
@ApiModel(value = "案源实体")
public class CaseDepute implements Serializable{
    @ApiModelProperty(notes = "编号", required = false)
    private Integer caseId;
    @ApiModelProperty(notes = "标题", required = false)
    private String caseTitle;
    @ApiModelProperty(notes = "委托费报价（元）", required = false)
    private String offerMoney;
    @ApiModelProperty(notes = "支付类型", required = false)
    private Integer offerType;
    private String offerTypeString;
    @ApiModelProperty(notes = "地区", required = false)
    private String city;
    private String cityDescription;
    @ApiModelProperty(notes = "发布人", required = false)
    private String userName;
    @ApiModelProperty(notes = "添加时间", required = false)
    private String deputeTime;
    private String deputeTimeStart;
    private String deputeTimeEnd;
    @ApiModelProperty(notes = "案源状态", required = false)
    private Integer caseState;
    private String caseDesc;
    private String caseHope;
    private String caseAsk;
    private String province;
    private String provinceDescription;
    @ApiModelProperty(notes = "案源类型", required = false)
    private Integer caseType;
    private String caseTypeDescription;
    private String mobile;
    private Integer isDelete;

    private Integer userId;
    private String userImg;
    private Integer isPay;
    private String isPayDescription;

    @ApiModelProperty(notes = "标题/发布人", required = false)
    private String content;

    private Integer size;
    private Integer page;
    private Integer start;
    private Integer length;
    private String sEcho;

    public void setCaseId(Integer caseId){
        this.caseId=caseId;
    }
    public Integer getCaseId(){
        return caseId;
    }
    public void setCaseTitle(String caseTitle){
        this.caseTitle=caseTitle;
    }
    public String getCaseTitle(){
        return caseTitle;
    }
    public void setCaseDesc(String caseDesc){
        this.caseDesc=caseDesc;
    }
    public String getCaseDesc(){
        return caseDesc;
    }
    public void setCaseHope(String caseHope){
        this.caseHope=caseHope;
    }
    public String getCaseHope(){
        return caseHope;
    }
    public void setCaseAsk(String caseAsk){
        this.caseAsk=caseAsk;
    }
    public String getCaseAsk(){
        return caseAsk;
    }
    public void setProvince(String province){
        this.province=province;
    }
    public String getProvince(){
        return province;
    }
    public void setCity(String city){
        this.city=city;
    }
    public String getCity(){
        return city;
    }
    public void setCaseType(Integer caseType){
        this.caseType=caseType;
    }
    public Integer getCaseType(){
        return caseType;
    }
    public void setMobile(String mobile){
        this.mobile=mobile;
    }
    public String getMobile(){
        return mobile;
    }
    public void setOfferType(Integer offerType){
        this.offerType=offerType;
    }
    public Integer getOfferType(){
        return offerType;
    }
    public void setOfferMoney(String offerMoney){
        this.offerMoney=offerMoney;
    }
    public String getOfferMoney(){
        return offerMoney;
    }
    public void setCaseState(Integer caseState){
        this.caseState=caseState;
    }
    public Integer getCaseState(){
        return caseState;
    }
    public void setDeputeTime(String deputeTime){
        this.deputeTime=deputeTime;
    }
    public String getDeputeTime(){
        return deputeTime;
    }

    public String getDeputeTimeStart() {
        return deputeTimeStart;
    }

    public void setDeputeTimeStart(String deputeTimeStart) {
        this.deputeTimeStart = deputeTimeStart;
    }

    public String getDeputeTimeEnd() {
        return deputeTimeEnd;
    }

    public void setDeputeTimeEnd(String deputeTimeEnd) {
        this.deputeTimeEnd = deputeTimeEnd;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getOfferTypeString() {
        return offerTypeString;
    }

    public void setOfferTypeString(String offerTypeString) {
        this.offerTypeString = offerTypeString;
    }

    public String getProvinceDescription() {
		provinceDescription = AddressController.getProvince(Integer.parseInt(province));
		return provinceDescription;
	}
	public void setProvinceDescription(String provinceDescription) {
		this.provinceDescription = provinceDescription;
	}
	public String getCityDescription() {
		cityDescription = AddressController.getCity(Integer.parseInt(city));
        return cityDescription;
    }

    public void setCityDescription(String cityDescription) {
        this.cityDescription = cityDescription;
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
	public String getCaseTypeDescription() {
        if (null != caseType) {
    			caseTypeDescription = SystemConfigUtil.getValue((int)caseType, SystemConfigUtil.TYPE_CASE);
        }
		return caseTypeDescription;
	}
	public void setCaseTypeDescription(String caseTypeDescription) {
		this.caseTypeDescription = caseTypeDescription;
	}
	public Integer getIsPay() {
		return isPay;
	}
	public void setIsPay(Integer isPay) {
		this.isPay = isPay;
	}
	public String getIsPayDescription() {
        if (null != isPay) {
	        	if (0 == isPay.intValue()) {
	        		isPayDescription = "不需要支付";
	        } else if (1 == isPay.intValue()) {
            		isPayDescription = "已支付";
            } else if (2 == isPay.intValue()) {
            		isPayDescription = "未支付";
            } else if (3 == isPay.intValue()) {
	        		isPayDescription = "正在支付中";
	        } else if (4 == isPay.intValue()) {
		    		isPayDescription = "支付失败";
		    } else if (5 == isPay.intValue()) {
				isPayDescription = "已退款";
		    } else if (6 == isPay.intValue()) {
				isPayDescription = "待审核";
		    }
        }
		return isPayDescription;
	}
	public void setIsPayDescription(String isPayDescription) {
		this.isPayDescription = isPayDescription;
	}
}