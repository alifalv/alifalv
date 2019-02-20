package com.legal.app.model;
import java.math.BigDecimal;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")@MyBatisEntity(namespace="Document")
public class Document implements Serializable{
    private Integer makeId;
    private Integer makeType;
    private Integer userId;
    private String nickName;
    private Integer consultantId;
    private String ConsultantName;
    private BigDecimal price;
    private Integer payType;
    private String voucherId;

    public void setMakeId(Integer makeId){
        this.makeId=makeId;
    }
    public Integer getMakeId(){
        return makeId;
    }
    public void setMakeType(Integer makeType){
        this.makeType=makeType;
    }
    public Integer getMakeType(){
        return makeType;
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
    public void setConsultantId(Integer consultantId){
        this.consultantId=consultantId;
    }
    public Integer getConsultantId(){
        return consultantId;
    }
    public void setConsultantName(String ConsultantName){
        this.ConsultantName=ConsultantName;
    }
    public String getConsultantName(){
        return ConsultantName;
    }
    public void setPrice(BigDecimal price){
        this.price=price;
    }
    public BigDecimal getPrice(){
        return price;
    }
    public void setPayType(Integer payType){
        this.payType=payType;
    }
    public Integer getPayType(){
        return payType;
    }
    public void setVoucherId(String voucherId){
        this.voucherId=voucherId;
    }
    public String getVoucherId(){
        return voucherId;
    }
}