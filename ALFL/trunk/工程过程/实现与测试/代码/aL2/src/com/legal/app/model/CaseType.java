package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="CaseType")
public class CaseType implements Serializable{
    private Integer tagId;
    private String tagName;
    private Integer orderIndex;

    public void setTagId(Integer tagId){
        this.tagId=tagId;
    }
    public Integer getTagId(){
        return tagId;
    }
    public void setTagName(String tagName){
        this.tagName=tagName;
    }
    public String getTagName(){
        return tagName;
    }
    public void setOrderIndex(Integer orderIndex){
        this.orderIndex=orderIndex;
    }
    public Integer getOrderIndex(){
        return orderIndex;
    }
}