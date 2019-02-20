package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")
@MyBatisEntity(namespace="Speciality")
public class Speciality implements Serializable{
    private Integer id;
    private Integer userId;
    private Integer typeId;

    public void setId(Integer id){
        this.id=id;
    }
    public Integer getId(){
        return id;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setTypeId(Integer typeId){
        this.typeId=typeId;
    }
    public Integer getTypeId(){
        return typeId;
    }
}