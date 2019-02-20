package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@MyBatisEntity(namespace="Weibo")
@ApiModel(value = "微博实体")
public class Weibo implements Serializable{
    private Integer id;
    private Integer userId;
    @ApiModelProperty(notes = "微博号", required = false)
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