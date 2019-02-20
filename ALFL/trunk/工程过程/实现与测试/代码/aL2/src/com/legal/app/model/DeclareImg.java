package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="DeclareImg")
public class DeclareImg implements Serializable{
    private Integer id;
    private Integer declareId;
    private String img;
    private Integer imgType;

    public void setId(Integer id){
        this.id=id;
    }
    public Integer getId(){
        return id;
    }
    public void setDeclareId(Integer declareId){
        this.declareId=declareId;
    }
    public Integer getDeclareId(){
        return declareId;
    }
    public void setImg(String img){
        this.img=img;
    }
    public String getImg(){
        return img;
    }
    public void setImgType(Integer imgType){
        this.imgType=imgType;
    }
    public Integer getImgType(){
        return imgType;
    }
}