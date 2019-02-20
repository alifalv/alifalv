package com.legal.app.model;
import java.sql.Timestamp;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")
@MyBatisEntity(namespace="Follow")
public class Follow implements Serializable{
    private Integer followId;
    private Integer userId;
    private String nickName;
    private Integer counselorId;
    private String counselorName;
    private Timestamp adviceTime;

    public void setFollowId(Integer followId){
        this.followId=followId;
    }
    public Integer getFollowId(){
        return followId;
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
    public void setCounselorId(Integer counselorId){
        this.counselorId=counselorId;
    }
    public Integer getCounselorId(){
        return counselorId;
    }
    public void setCounselorName(String counselorName){
        this.counselorName=counselorName;
    }
    public String getCounselorName(){
        return counselorName;
    }
    public void setAdviceTime(Timestamp adviceTime){
        this.adviceTime=adviceTime;
    }
    public Timestamp getAdviceTime(){
        return adviceTime;
    }
}