package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="Qq")
@ApiModel(value = "QQ实体")
public class Qq implements Serializable{
    private Integer id;
    private Integer userId;
    @ApiModelProperty(notes = "qq", required = false)
    private String qq;
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
    public void setQq(String qq){
        this.qq=qq;
    }
    public String getQq(){
        return qq;
    }
    public void setAuthorityId(String authorityId){
        this.authorityId=authorityId;
    }
    public String getAuthorityId(){
        return authorityId;
    }
}