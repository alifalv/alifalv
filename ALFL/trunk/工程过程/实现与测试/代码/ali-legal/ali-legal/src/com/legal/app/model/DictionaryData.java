package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="DictionaryData")
public class DictionaryData implements Serializable{
    private Integer dataId;
    private String dictionaryCode;
    private String dataDesc;
    private Integer dataValue;

    public void setDataId(Integer dataId){
        this.dataId=dataId;
    }
    public Integer getDataId(){
        return dataId;
    }
    public void setDictionaryCode(String dictionaryCode){
        this.dictionaryCode=dictionaryCode;
    }
    public String getDictionaryCode(){
        return dictionaryCode;
    }
    public void setDataDesc(String dataDesc){
        this.dataDesc=dataDesc;
    }
    public String getDataDesc(){
        return dataDesc;
    }
    public void setDataValue(Integer dataValue){
        this.dataValue=dataValue;
    }
    public Integer getDataValue(){
        return dataValue;
    }
}