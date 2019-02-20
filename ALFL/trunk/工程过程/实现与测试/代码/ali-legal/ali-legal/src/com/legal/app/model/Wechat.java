package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@MyBatisEntity(namespace="Wechat")
@ApiModel(value = "微信实体")
public class Wechat implements Serializable{
    private Integer id;
    private Integer userId;
    @ApiModelProperty(notes = "微信号", required = false)
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