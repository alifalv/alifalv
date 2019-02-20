package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")
@MyBatisEntity(namespace="CaseImg")
public class CaseImg implements Serializable{
    private Integer caseImgId;
    private Integer caseId;
    private String img;

    public void setCaseImgId(Integer caseImgId){
        this.caseImgId=caseImgId;
    }
    public Integer getCaseImgId(){
        return caseImgId;
    }
    public void setCaseId(Integer caseId){
        this.caseId=caseId;
    }
    public Integer getCaseId(){
        return caseId;
    }
    public void setImg(String img){
        this.img=img;
    }
    public String getImg(){
        return img;
    }
}