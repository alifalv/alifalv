package com.legal.app.model;
import java.sql.Timestamp;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="AdviceDetails")
public class AdviceDetails implements Serializable{
    private Integer detailsId;
    private Integer replyId;
    private String replyContent;
    private Timestamp replyTime;

    public void setDetailsId(Integer detailsId){
        this.detailsId=detailsId;
    }
    public Integer getDetailsId(){
        return detailsId;
    }
    public void setReplyId(Integer replyId){
        this.replyId=replyId;
    }
    public Integer getReplyId(){
        return replyId;
    }
    public void setReplyContent(String replyContent){
        this.replyContent=replyContent;
    }
    public String getReplyContent(){
        return replyContent;
    }
    public void setReplyTime(Timestamp replyTime){
        this.replyTime=replyTime;
    }
    public Timestamp getReplyTime(){
        return replyTime;
    }
}