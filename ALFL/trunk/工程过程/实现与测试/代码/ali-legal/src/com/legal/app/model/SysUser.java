package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="SysUser")
@ApiModel(value = "系统用户")
public class SysUser implements Serializable{
    @ApiModelProperty(notes = "id", required = false)
    private Integer sys_userId;
    @ApiModelProperty(notes = "姓名", required = false)
    private String sys_userName;
    @ApiModelProperty(notes = "密码", required = false)
    private String sys_userPassword;
    private Integer sys_roleId;
    @ApiModelProperty(notes = "角色", required = false)
    private String sysRoleName;
    @ApiModelProperty(notes = "用户名", required = false)
    private String sys_nickName;
    private Integer sys_state;
    @ApiModelProperty(notes = "状态", required = false)
    private String sysStateDescription;
    @ApiModelProperty(notes = "是否删除", required = false)
    private Integer isDelete;

    @ApiModelProperty(notes = "手机", required = false)
    private String mobile;

    private Integer size;
    private Integer page;
    private Integer start;
    private Integer length;
    private String sEcho;

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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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

    public String getSysRoleName() {
        return sysRoleName;
    }

    public void setSysRoleName(String sysRoleName) {
        this.sysRoleName = sysRoleName;
    }

    public String getSysStateDescription() {
        if (null != sys_state) {
            if (1 == sys_state.intValue()) {
                sysStateDescription = "正常";
            } else if (2 == sys_state.intValue()) {
                sysStateDescription = "锁定";
            }
        } else {
            sysStateDescription = "锁定";
        }
        return sysStateDescription;
    }

    public void setSysStateDescription(String sysStateDescription) {
        this.sysStateDescription = sysStateDescription;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}