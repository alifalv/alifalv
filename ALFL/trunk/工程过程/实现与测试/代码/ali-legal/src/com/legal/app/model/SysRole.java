package com.legal.app.model;
import java.io.Serializable;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="SysRole")
@ApiModel(value = "角色实体")
public class SysRole implements Serializable{
    @ApiModelProperty(notes = "编号", required = false)
    private Integer sys_roleId;
    @ApiModelProperty(notes = "角色", required = false)
    private String sys_roleName;
    @ApiModelProperty(notes = "状态", required = false)
    private Integer sys_state;
    private String sysStateDescription;
    private String sys_desc;
    private Integer is_delete;

    private String sysMappings;

    private Integer size;
    private Integer page;
    private Integer start;
    private Integer length;
    private String sEcho;

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

    public String getSysMappings() {
        return sysMappings;
    }

    public void setSysMappings(String sysMappings) {
        this.sysMappings = sysMappings;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public String getSysStateDescription() {
        if (null != sys_state) {
            if (0 == sys_state.intValue()) {
                sysStateDescription = "正常";
            }else {
                sysStateDescription = "禁用";
            }
        } else {
            sysStateDescription = "禁用";
        }
        return sysStateDescription;
    }

    public void setSysStateDescription(String sysStateDescription) {
        this.sysStateDescription = sysStateDescription;
    }
}