package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="Column")
public class Column implements Serializable{
    private Integer columnId;
    private String columnName;
    private Integer columnType;
    private String columnCode;

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
}