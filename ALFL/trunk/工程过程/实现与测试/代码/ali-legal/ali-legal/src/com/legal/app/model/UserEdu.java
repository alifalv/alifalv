package com.legal.app.model;
import java.sql.Date;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="UserEdu")
public class UserEdu implements Serializable{
    private Integer educationId;
    private String schoolName;
    private String major;
    private Integer qualification;
    private Date inDate;
    private Date outDate;
    private String schoolDesc;
    private Integer userId;

    public void setEducationId(Integer educationId){
        this.educationId=educationId;
    }
    public Integer getEducationId(){
        return educationId;
    }
    public void setSchoolName(String schoolName){
        this.schoolName=schoolName;
    }
    public String getSchoolName(){
        return schoolName;
    }
    public void setMajor(String major){
        this.major=major;
    }
    public String getMajor(){
        return major;
    }
    public void setQualification(Integer qualification){
        this.qualification=qualification;
    }
    public Integer getQualification(){
        return qualification;
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
    public void setSchoolDesc(String schoolDesc){
        this.schoolDesc=schoolDesc;
    }
    public String getSchoolDesc(){
        return schoolDesc;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
}