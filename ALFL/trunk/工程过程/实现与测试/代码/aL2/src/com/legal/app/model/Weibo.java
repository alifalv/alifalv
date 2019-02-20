package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;

@MyBatisEntity(namespace="Weibo")
public class Weibo implements Serializable{
    private Integer id;
    private Integer userId;
    private String weibo;
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
    public void setWeibo(String weibo){
        this.weibo=weibo;
    }
    public String getWeibo(){
        return weibo;
    }
    public void setAuthorityId(String authorityId){
        this.authorityId=authorityId;
    }
    public String getAuthorityId(){
        return authorityId;
    }
}