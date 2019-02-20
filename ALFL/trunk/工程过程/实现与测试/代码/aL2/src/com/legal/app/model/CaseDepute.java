package com.legal.app.model;
import java.sql.Timestamp;

import java.math.BigDecimal;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="CaseDepute")
public class CaseDepute implements Serializable{
    private Integer caseId;
    private String caseTitle;
    private String caseDesc;
    private String caseHope;
    private String caseAsk;
    private Integer province;
    private Integer city;
    private Integer caseType;
    private String mobile;
    private Integer offerType;
    private BigDecimal offerMoney;
    private Integer caseState;
    private Timestamp deputeTime;    private Integer userId;    private String userName;    private String userImg;    private Integer is_delete;

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
    public void setProvince(Integer province){
        this.province=province;
    }
    public Integer getProvince(){
        return province;
    }
    public void setCity(Integer city){
        this.city=city;
    }
    public Integer getCity(){
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
    public void setOfferMoney(BigDecimal offerMoney){
        this.offerMoney=offerMoney;
    }
    public BigDecimal getOfferMoney(){
        return offerMoney;
    }
    public void setCaseState(Integer caseState){
        this.caseState=caseState;
    }
    public Integer getCaseState(){
        return caseState;
    }
    public void setDeputeTime(Timestamp deputeTime){
        this.deputeTime=deputeTime;
    }
    public Timestamp getDeputeTime(){
        return deputeTime;
    }	public Integer getUserId() {		return userId;	}	public void setUserId(Integer userId) {		this.userId = userId;	}	public String getUserName() {		return userName;	}	public void setUserName(String userName) {		this.userName = userName;	}	public String getUserImg() {		return userImg;	}	public void setUserImg(String userImg) {		this.userImg = userImg;	}	public Integer getIs_delete() {		return is_delete;	}	public void setIs_delete(Integer is_delete) {		this.is_delete = is_delete;	}        
}