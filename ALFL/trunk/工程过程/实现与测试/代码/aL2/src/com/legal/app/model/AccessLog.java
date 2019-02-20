package com.legal.app.model;
import java.sql.Timestamp;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="AccessLog")
public class AccessLog implements Serializable{
    private Integer logId;
    private Integer userId;
    private String userName;
    private Integer counselorId;
    private String counselorName;
    private Timestamp adviceTime;

    public void setLogId(Integer logId){
        this.logId=logId;
    }
    public Integer getLogId(){
        return logId;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setUserName(String userName){
        this.userName=userName;
    }
    public String getUserName(){
        return userName;
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