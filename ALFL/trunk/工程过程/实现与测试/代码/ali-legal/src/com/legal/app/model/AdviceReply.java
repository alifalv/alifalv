package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")
@MyBatisEntity(namespace="AdviceReply")
public class AdviceReply implements Serializable{
    private Integer replyId;
    private Integer adviceId;
    private Integer userId;
    private Integer rankValue;
    private String nickName;

    public void setReplyId(Integer replyId){
        this.replyId=replyId;
    }
    public Integer getReplyId(){
        return replyId;
    }
    public void setAdviceId(Integer adviceId){
        this.adviceId=adviceId;
    }
    public Integer getAdviceId(){
        return adviceId;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setRankValue(Integer rankValue){
        this.rankValue=rankValue;
    }
    public Integer getRankValue(){
        return rankValue;
    }
    public void setNickName(String nickName){
        this.nickName=nickName;
    }
    public String getNickName(){
        return nickName;
    }
}