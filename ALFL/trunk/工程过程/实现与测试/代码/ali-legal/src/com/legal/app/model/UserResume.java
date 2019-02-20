package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")
@MyBatisEntity(namespace="UserResume")
public class UserResume implements Serializable{
    private Integer resumeId;
    private String resumeName;
    private String job;
    private Integer sal;
    private Integer city;
    private Integer workExperience;
    private Integer qualification;
    private Integer state;

    public void setResumeId(Integer resumeId){
        this.resumeId=resumeId;
    }
    public Integer getResumeId(){
        return resumeId;
    }
    public void setResumeName(String resumeName){
        this.resumeName=resumeName;
    }
    public String getResumeName(){
        return resumeName;
    }
    public void setJob(String job){
        this.job=job;
    }
    public String getJob(){
        return job;
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
    public void setState(Integer state){
        this.state=state;
    }
    public Integer getState(){
        return state;
    }
}