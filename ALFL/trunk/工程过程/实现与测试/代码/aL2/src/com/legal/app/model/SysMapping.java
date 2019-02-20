package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="SysMapping")
public class SysMapping implements Serializable{
    private Integer mappingId;
    private Integer roleId;
    private Integer premissonId;

    public void setMappingId(Integer mappingId){
        this.mappingId=mappingId;
    }
    public Integer getMappingId(){
        return mappingId;
    }
    public void setRoleId(Integer roleId){
        this.roleId=roleId;
    }
    public Integer getRoleId(){
        return roleId;
    }
    public void setPremissonId(Integer premissonId){
        this.premissonId=premissonId;
    }
    public Integer getPremissonId(){
        return premissonId;
    }
}