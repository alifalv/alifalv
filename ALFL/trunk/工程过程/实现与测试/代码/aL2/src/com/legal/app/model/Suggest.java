package com.legal.app.model;
import java.sql.Timestamp;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="Suggest")
public class Suggest implements Serializable{
    private Integer suggestId;
    private String suggestTitle;
    private String suggestContent;
    private Integer userId;
    private String img1;
    private String img2;
    private String img3;
    private Timestamp suggestTime;

    public void setSuggestId(Integer suggestId){
        this.suggestId=suggestId;
    }
    public Integer getSuggestId(){
        return suggestId;
    }
    public void setSuggestTitle(String suggestTitle){
        this.suggestTitle=suggestTitle;
    }
    public String getSuggestTitle(){
        return suggestTitle;
    }
    public void setSuggestContent(String suggestContent){
        this.suggestContent=suggestContent;
    }
    public String getSuggestContent(){
        return suggestContent;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setImg1(String img1){
        this.img1=img1;
    }
    public String getImg1(){
        return img1;
    }
    public void setImg2(String img2){
        this.img2=img2;
    }
    public String getImg2(){
        return img2;
    }
    public void setImg3(String img3){
        this.img3=img3;
    }
    public String getImg3(){
        return img3;
    }
    public void setSuggestTime(Timestamp suggestTime){
        this.suggestTime=suggestTime;
    }
    public Timestamp getSuggestTime(){
        return suggestTime;
    }
}