package com.legal.app.model;
import java.io.Serializable;
import java.util.List;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="SysPremisson")
@ApiModel(value = "菜单实体")
public class SysPremisson implements Serializable{
    @ApiModelProperty(notes = "菜单id", required = false)
    private Integer premissonId;
    @ApiModelProperty(notes = "菜单姓名", required = false)
    private String premissonName;
    @ApiModelProperty(notes = "菜单编码", required = false)
    private String premissonCode;
    @ApiModelProperty(notes = "菜单url", required = false)
    private String premissonUri;
    @ApiModelProperty(notes = "菜单父节点", required = false)
    private Integer parentId;
    @ApiModelProperty(notes = "菜单层级", required = false)
    private Integer level;
    @ApiModelProperty(notes = "菜单是否点击", required = false)
    private Integer isClick;

	private List<SysPremisson> childs;

    public void setPremissonId(Integer premissonId){
        this.premissonId=premissonId;
    }
    public Integer getPremissonId(){
        return premissonId;
    }
    public void setPremissonName(String premissonName){
        this.premissonName=premissonName;
    }
    public String getPremissonName(){
        return premissonName;
    }
    public void setPremissonCode(String premissonCode){
        this.premissonCode=premissonCode;
    }
    public String getPremissonCode(){
        return premissonCode;
    }
    public void setPremissonUri(String premissonUri){
        this.premissonUri=premissonUri;
    }
    public String getPremissonUri(){
        return premissonUri;
    }
    public void setParentId(Integer parentId){
        this.parentId=parentId;
    }
    public Integer getParentId(){
        return parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setIsClick(Integer isClick){
        this.isClick=isClick;
    }
    public Integer getIsClick(){
        return isClick;
    }

    public List<SysPremisson> getChilds() {
        return childs;
    }

    public void setChilds(List<SysPremisson> childs) {
        this.childs = childs;
    }

    @Override
	public String toString() {
		return "SysPremisson [premissonId=" + premissonId + ", premissonName=" + premissonName + ", premissonCode="
				+ premissonCode + ", premissonUri=" + premissonUri + ", parentId=" + parentId + ", level=" + level
				+ ", isClick=" + isClick + ", childs=" + childs + "]";
	}
}