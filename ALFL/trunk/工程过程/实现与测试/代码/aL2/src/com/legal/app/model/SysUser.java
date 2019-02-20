package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="SysUser")
public class SysUser implements Serializable{
    private Integer sys_userId;
    private String sys_userName;
    private String sys_userPassword;
    private Integer sys_roleId;
    private String sys_nickName;
    private Integer sys_state;

    public void setSys_userId(Integer sys_userId){
        this.sys_userId=sys_userId;
    }
    public Integer getSys_userId(){
        return sys_userId;
    }
    public void setSys_userName(String sys_userName){
        this.sys_userName=sys_userName;
    }
    public String getSys_userName(){
        return sys_userName;
    }
    public void setSys_userPassword(String sys_userPassword){
        this.sys_userPassword=sys_userPassword;
    }
    public String getSys_userPassword(){
        return sys_userPassword;
    }
    public void setSys_roleId(Integer sys_roleId){
        this.sys_roleId=sys_roleId;
    }
    public Integer getSys_roleId(){
        return sys_roleId;
    }
    public void setSys_nickName(String sys_nickName){
        this.sys_nickName=sys_nickName;
    }
    public String getSys_nickName(){
        return sys_nickName;
    }
    public void setSys_state(Integer sys_state){
        this.sys_state=sys_state;
    }
    public Integer getSys_state(){
        return sys_state;
    }
}