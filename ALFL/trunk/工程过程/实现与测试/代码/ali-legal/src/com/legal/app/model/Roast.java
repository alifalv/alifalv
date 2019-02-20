package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")
@MyBatisEntity(namespace="Roast")
public class Roast implements Serializable{
    private Integer roastId;
    private Integer state;
    private Integer articleId;

    public void setRoastId(Integer roastId){
        this.roastId=roastId;
    }
    public Integer getRoastId(){
        return roastId;
    }
    public void setState(Integer state){
        this.state=state;
    }
    public Integer getState(){
        return state;
    }
    public void setArticleId(Integer articleId){
        this.articleId=articleId;
    }
    public Integer getArticleId(){
        return articleId;
    }
}