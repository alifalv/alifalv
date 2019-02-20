package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="SysDictionary")
public class SysDictionary implements Serializable{
    private Integer dictionaryId;
    private String dictionaryCode;
    private String dictionaryDesc;

    public void setDictionaryId(Integer dictionaryId){
        this.dictionaryId=dictionaryId;
    }
    public Integer getDictionaryId(){
        return dictionaryId;
    }
    public void setDictionaryCode(String dictionaryCode){
        this.dictionaryCode=dictionaryCode;
    }
    public String getDictionaryCode(){
        return dictionaryCode;
    }
    public void setDictionaryDesc(String dictionaryDesc){
        this.dictionaryDesc=dictionaryDesc;
    }
    public String getDictionaryDesc(){
        return dictionaryDesc;
    }
}