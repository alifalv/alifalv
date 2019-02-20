package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;

@MyBatisEntity(namespace="SysPremisson")
public class SysPremisson implements Serializable{
    private Integer premissonId;
    private String premissonName;
    private String premissonCode;
    private String premissonUri;
    private Integer parentId;
    private Integer level;
    private Integer isClick;

    public void setPremissonId(Integer premissonId){
        this.premissonId=premissonId;
    }
    public Integer getPremissonId(){
        return premissonId;
    }
    public void setPremissonName(String premissonName){
        this.premissonName=premissonName;
    }
    public String getPremissonName(){
        return premissonName;
    }
    public void setPremissonCode(String premissonCode){
        this.premissonCode=premissonCode;
    }
    public String getPremissonCode(){
        return premissonCode;
    }
    public void setPremissonUri(String premissonUri){
        this.premissonUri=premissonUri;
    }
    public String getPremissonUri(){
        return premissonUri;
    }
    public void setParentId(Integer parentId){
        this.parentId=parentId;
    }
    public Integer getParentId(){
        return parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setIsClick(Integer isClick){
        this.isClick=isClick;
    }
    public Integer getIsClick(){
        return isClick;
    }
}