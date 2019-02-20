package com.legal.app.model;
import java.sql.Timestamp;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")
@MyBatisEntity(namespace="ArticleCollection")
public class ArticleCollection implements Serializable{
    private Integer collectionId;
    private Integer userId;
    private Integer articleId;
    private Timestamp collectionTime;
    private String userName;

    public void setCollectionId(Integer collectionId){
        this.collectionId=collectionId;
    }
    public Integer getCollectionId(){
        return collectionId;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setArticleId(Integer articleId){
        this.articleId=articleId;
    }
    public Integer getArticleId(){
        return articleId;
    }
    public void setCollectionTime(Timestamp collectionTime){
        this.collectionTime=collectionTime;
    }
    public Timestamp getCollectionTime(){
        return collectionTime;
    }
    public void setUserName(String userName){
        this.userName=userName;
    }
    public String getUserName(){
        return userName;
    }
}