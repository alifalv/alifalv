package com.legal.app.model;
import java.math.BigDecimal;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="CaseOffer")
public class CaseOffer implements Serializable{
    private Integer offerId;
    private Integer caseId;
    private Integer userId;
    private String nickName;
    private String effect;
    private String thinking;
    private BigDecimal offerMoney;
    private String mobile;
    private Integer isSuccess;

    public void setOfferId(Integer offerId){
        this.offerId=offerId;
    }
    public Integer getOfferId(){
        return offerId;
    }
    public void setCaseId(Integer caseId){
        this.caseId=caseId;
    }
    public Integer getCaseId(){
        return caseId;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setNickName(String nickName){
        this.nickName=nickName;
    }
    public String getNickName(){
        return nickName;
    }
    public void setEffect(String effect){
        this.effect=effect;
    }
    public String getEffect(){
        return effect;
    }
    public void setThinking(String thinking){
        this.thinking=thinking;
    }
    public String getThinking(){
        return thinking;
    }
    public void setOfferMoney(BigDecimal offerMoney){
        this.offerMoney=offerMoney;
    }
    public BigDecimal getOfferMoney(){
        return offerMoney;
    }
    public void setMobile(String mobile){
        this.mobile=mobile;
    }
    public String getMobile(){
        return mobile;
    }
    public void setIsSuccess(Integer isSuccess){
        this.isSuccess=isSuccess;
    }
    public Integer getIsSuccess(){
        return isSuccess;
    }
}