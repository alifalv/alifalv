package com.legal.app.model;
import java.sql.Timestamp;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="Article")
public class Article implements Serializable{
    private String articleId;
    private Integer userId;
    private String realName;
    private Integer isofficial;
    private Integer isOutJoin;
    private String title;
    private String columnCode;
    private String txtPath;
    private Timestamp sendTime;
    private Integer lookNum;
    private String outJoinUrl;
    private String coverImg;

    public void setArticleId(String articleId){
        this.articleId=articleId;
    }
    public String getArticleId(){
        return articleId;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setRealName(String realName){
        this.realName=realName;
    }
    public String getRealName(){
        return realName;
    }
    public void setIsofficial(Integer isofficial){
        this.isofficial=isofficial;
    }
    public Integer getIsofficial(){
        return isofficial;
    }
    public void setIsOutJoin(Integer isOutJoin){
        this.isOutJoin=isOutJoin;
    }
    public Integer getIsOutJoin(){
        return isOutJoin;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getTitle(){
        return title;
    }
    public void setColumnCode(String columnCode){
        this.columnCode=columnCode;
    }
    public String getColumnCode(){
        return columnCode;
    }
    public void setTxtPath(String txtPath){
        this.txtPath=txtPath;
    }
    public String getTxtPath(){
        return txtPath;
    }
    public void setSendTime(Timestamp sendTime){
        this.sendTime=sendTime;
    }
    public Timestamp getSendTime(){
        return sendTime;
    }
    public void setLookNum(Integer lookNum){
        this.lookNum=lookNum;
    }
    public Integer getLookNum(){
        return lookNum;
    }
    public void setOutJoinUrl(String outJoinUrl){
        this.outJoinUrl=outJoinUrl;
    }
    public String getOutJoinUrl(){
        return outJoinUrl;
    }
    public void setCoverImg(String coverImg){
        this.coverImg=coverImg;
    }
    public String getCoverImg(){
        return coverImg;
    }
}