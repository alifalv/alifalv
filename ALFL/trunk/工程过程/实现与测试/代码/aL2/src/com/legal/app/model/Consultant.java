package com.legal.app.model;

import com.common.dbutil.MyBatisEntity;

import java.io.Serializable;

@MyBatisEntity(namespace="Consultant")
public class Consultant implements Serializable{
    private Integer id;
    private Integer userId;
    private Integer carType;
    private String idCardFront;
    private String idCardBack;
    private String realName;
    private String idCard;
    private Integer province;
    private Integer city;
    private Integer adviceNum;
    private Integer followingNum;
    private Integer collectionNum;
    private Integer freeNum;

    public void setId(Integer id){
        this.id=id;
    }
    public Integer getId(){
        return id;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setCarType(Integer carType){
        this.carType=carType;
    }
    public Integer getCarType(){
        return carType;
    }
    public void setIdCardFront(String idCardFront){
        this.idCardFront=idCardFront;
    }
    public String getIdCardFront(){
        return idCardFront;
    }
    public void setIdCardBack(String idCardBack){
        this.idCardBack=idCardBack;
    }
    public String getIdCardBack(){
        return idCardBack;
    }
    public void setRealName(String realName){
        this.realName=realName;
    }
    public String getRealName(){
        return realName;
    }
    public void setIdCard(String idCard){
        this.idCard=idCard;
    }
    public String getIdCard(){
        return idCard;
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
    public void setAdviceNum(Integer adviceNum){
        this.adviceNum=adviceNum;
    }
    public Integer getAdviceNum(){
        return adviceNum;
    }
    public void setFollowingNum(Integer followingNum){
        this.followingNum=followingNum;
    }
    public Integer getFollowingNum(){
        return followingNum;
    }
    public void setCollectionNum(Integer collectionNum){
        this.collectionNum=collectionNum;
    }
    public Integer getCollectionNum(){
        return collectionNum;
    }
    public void setFreeNum(Integer freeNum){
        this.freeNum=freeNum;
    }
    public Integer getFreeNum(){
        return freeNum;
    }
}