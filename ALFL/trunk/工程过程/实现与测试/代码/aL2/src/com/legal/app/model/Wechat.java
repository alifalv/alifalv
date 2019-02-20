package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;

@MyBatisEntity(namespace="Wechat")
public class Wechat implements Serializable{
    private Integer id;
    private Integer userId;
    private String wechat;
    private String authorityId;

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
    public void setWechat(String wechat){
        this.wechat=wechat;
    }
    public String getWechat(){
        return wechat;
    }
    public void setAuthorityId(String authorityId){
        this.authorityId=authorityId;
    }
    public String getAuthorityId(){
        return authorityId;
    }
}