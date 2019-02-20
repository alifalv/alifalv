package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="Column")
@ApiModel(value = "所属栏目")
public class Column implements Serializable{
    @ApiModelProperty(notes = "编号", required = false)
    private Integer columnId;
    @ApiModelProperty(notes = "栏目名称", required = false)
    private String columnName;
    private Integer columnType;
    private String columnCode;
    private Integer isDelete;

    public void setColumnId(Integer columnId){
        this.columnId=columnId;
    }
    public Integer getColumnId(){
        return columnId;
    }
    public void setColumnName(String columnName){
        this.columnName=columnName;
    }
    public String getColumnName(){
        return columnName;
    }
    public void setColumnType(Integer columnType){
        this.columnType=columnType;
    }
    public Integer getColumnType(){
        return columnType;
    }
    public void setColumnCode(String columnCode){
        this.columnCode=columnCode;
    }
    public String getColumnCode(){
        return columnCode;
    }
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}