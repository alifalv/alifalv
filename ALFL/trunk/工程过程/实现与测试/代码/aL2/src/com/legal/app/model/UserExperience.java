package com.legal.app.model;
import java.sql.Date;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="UserExperience")
public class UserExperience implements Serializable{
    private Integer experienceId;
    private Integer jobType;
    private String jobName;
    private Date inDate;
    private Date outDate;
    private String deptName;
    private String workContent;
    private Integer userId;

    public void setExperienceId(Integer experienceId){
        this.experienceId=experienceId;
    }
    public Integer getExperienceId(){
        return experienceId;
    }
    public void setJobType(Integer jobType){
        this.jobType=jobType;
    }
    public Integer getJobType(){
        return jobType;
    }
    public void setJobName(String jobName){
        this.jobName=jobName;
    }
    public String getJobName(){
        return jobName;
    }
    public void setInDate(Date inDate){
        this.inDate=inDate;
    }
    public Date getInDate(){
        return inDate;
    }
    public void setOutDate(Date outDate){
        this.outDate=outDate;
    }
    public Date getOutDate(){
        return outDate;
    }
    public void setDeptName(String deptName){
        this.deptName=deptName;
    }
    public String getDeptName(){
        return deptName;
    }
    public void setWorkContent(String workContent){
        this.workContent=workContent;
    }
    public String getWorkContent(){
        return workContent;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
}