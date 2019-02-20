package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")
@MyBatisEntity(namespace="Declare")
public class Declare implements Serializable{
    private Integer declareId;
    private Integer articleId;
    private Integer declareType;
    private String reasonInfo;
    private String caseDesc;
    private String caseUrl;
    private String bookUrl;
    private String bookType;
    private String img;

    public void setDeclareId(Integer declareId){
        this.declareId=declareId;
    }
    public Integer getDeclareId(){
        return declareId;
    }
    public void setArticleId(Integer articleId){
        this.articleId=articleId;
    }
    public Integer getArticleId(){
        return articleId;
    }
    public void setDeclareType(Integer declareType){
        this.declareType=declareType;
    }
    public Integer getDeclareType(){
        return declareType;
    }
    public void setReasonInfo(String reasonInfo){
        this.reasonInfo=reasonInfo;
    }
    public String getReasonInfo(){
        return reasonInfo;
    }
    public void setCaseDesc(String caseDesc){
        this.caseDesc=caseDesc;
    }
    public String getCaseDesc(){
        return caseDesc;
    }
    public void setCaseUrl(String caseUrl){
        this.caseUrl=caseUrl;
    }
    public String getCaseUrl(){
        return caseUrl;
    }
    public void setBookUrl(String bookUrl){
        this.bookUrl=bookUrl;
    }
    public String getBookUrl(){
        return bookUrl;
    }
    public void setBookType(String bookType){
        this.bookType=bookType;
    }
    public String getBookType(){
        return bookType;
    }
    public void setImg(String img){
        this.img=img;
    }
    public String getImg(){
        return img;
    }
}