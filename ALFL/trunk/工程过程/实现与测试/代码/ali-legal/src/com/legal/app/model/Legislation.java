package com.legal.app.model;
import java.sql.Timestamp;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")
@MyBatisEntity(namespace="Legislation")
public class Legislation implements Serializable{
    private Integer legislationId;
    private Integer articleId;
    private String titleKey;
    private String contentKey;
    private Integer effectLevel;
    private Integer sendUnit;
    private String referenceNo;
    private Timestamp issueTime;
    private Timestamp implementTime;
    private Timestamp uploadTime;

    public void setLegislationId(Integer legislationId){
        this.legislationId=legislationId;
    }
    public Integer getLegislationId(){
        return legislationId;
    }
    public void setArticleId(Integer articleId){
        this.articleId=articleId;
    }
    public Integer getArticleId(){
        return articleId;
    }
    public void setTitleKey(String titleKey){
        this.titleKey=titleKey;
    }
    public String getTitleKey(){
        return titleKey;
    }
    public void setContentKey(String contentKey){
        this.contentKey=contentKey;
    }
    public String getContentKey(){
        return contentKey;
    }
    public void setEffectLevel(Integer effectLevel){
        this.effectLevel=effectLevel;
    }
    public Integer getEffectLevel(){
        return effectLevel;
    }
    public void setSendUnit(Integer sendUnit){
        this.sendUnit=sendUnit;
    }
    public Integer getSendUnit(){
        return sendUnit;
    }
    public void setReferenceNo(String referenceNo){
        this.referenceNo=referenceNo;
    }
    public String getReferenceNo(){
        return referenceNo;
    }
    public void setIssueTime(Timestamp issueTime){
        this.issueTime=issueTime;
    }
    public Timestamp getIssueTime(){
        return issueTime;
    }
    public void setImplementTime(Timestamp implementTime){
        this.implementTime=implementTime;
    }
    public Timestamp getImplementTime(){
        return implementTime;
    }
    public void setUploadTime(Timestamp uploadTime){
        this.uploadTime=uploadTime;
    }
    public Timestamp getUploadTime(){
        return uploadTime;
    }
}