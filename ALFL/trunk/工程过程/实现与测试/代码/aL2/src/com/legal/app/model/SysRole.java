package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="SysRole")
public class SysRole implements Serializable{
    private Integer sys_roleId;
    private String sys_roleName;
    private Integer sys_state;
    private String sys_desc;
    private Integer is_delete;

    public void setSys_roleId(Integer sys_roleId){
        this.sys_roleId=sys_roleId;
    }
    public Integer getSys_roleId(){
        return sys_roleId;
    }
    public void setSys_roleName(String sys_roleName){
        this.sys_roleName=sys_roleName;
    }
    public String getSys_roleName(){
        return sys_roleName;
    }
    public void setSys_state(Integer sys_state){
        this.sys_state=sys_state;
    }
    public Integer getSys_state(){
        return sys_state;
    }
    public void setSys_desc(String sys_desc){
        this.sys_desc=sys_desc;
    }
    public String getSys_desc(){
        return sys_desc;
    }
	public Integer getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(Integer is_delete) {
		this.is_delete = is_delete;
	}
    
}