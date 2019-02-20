package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")
@MyBatisEntity(namespace="Advertise")
public class Advertise implements Serializable{
    private Integer advertiseId;
    private String jobName;
    private Integer sal;
    private Integer city;
    private Integer workExperience;
    private Integer qualification;
    private String jobDesc;
    private Integer state;

    public void setAdvertiseId(Integer advertiseId){
        this.advertiseId=advertiseId;
    }
    public Integer getAdvertiseId(){
        return advertiseId;
    }
    public void setJobName(String jobName){
        this.jobName=jobName;
    }
    public String getJobName(){
        return jobName;
    }
    public void setSal(Integer sal){
        this.sal=sal;
    }
    public Integer getSal(){
        return sal;
    }
    public void setCity(Integer city){
        this.city=city;
    }
    public Integer getCity(){
        return city;
    }
    public void setWorkExperience(Integer workExperience){
        this.workExperience=workExperience;
    }
    public Integer getWorkExperience(){
        return workExperience;
    }
    public void setQualification(Integer qualification){
        this.qualification=qualification;
    }
    public Integer getQualification(){
        return qualification;
    }
    public void setJobDesc(String jobDesc){
        this.jobDesc=jobDesc;
    }
    public String getJobDesc(){
        return jobDesc;
    }
    public void setState(Integer state){
        this.state=state;
    }
    public Integer getState(){
        return state;
    }
}