package com.legal.app.model;
import java.sql.Timestamp;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")
@MyBatisEntity(namespace="ResumeMapping")
public class ResumeMapping implements Serializable{
    private Integer id;
    private Integer resumeId;
    private Integer advertiseId;
    private Timestamp sendTime;
    private String jobName;
    private String resumeName;

    public void setId(Integer id){
        this.id=id;
    }
    public Integer getId(){
        return id;
    }
    public void setResumeId(Integer resumeId){
        this.resumeId=resumeId;
    }
    public Integer getResumeId(){
        return resumeId;
    }
    public void setAdvertiseId(Integer advertiseId){
        this.advertiseId=advertiseId;
    }
    public Integer getAdvertiseId(){
        return advertiseId;
    }
    public void setSendTime(Timestamp sendTime){
        this.sendTime=sendTime;
    }
    public Timestamp getSendTime(){
        return sendTime;
    }
    public void setJobName(String jobName){
        this.jobName=jobName;
    }
    public String getJobName(){
        return jobName;
    }
    public void setResumeName(String resumeName){
        this.resumeName=resumeName;
    }
    public String getResumeName(){
        return resumeName;
    }
}