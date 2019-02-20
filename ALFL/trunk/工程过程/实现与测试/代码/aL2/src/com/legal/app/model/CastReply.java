package com.legal.app.model;
import java.sql.Timestamp;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="CastReply")
public class CastReply implements Serializable{
    private Integer id;
    private Integer userId;
    private Integer replyState;
    private Timestamp replyTime;
    private Timestamp completeTime;
    private String mobile;

    public void setId(Integer id){
        this.id=id;
    }
    public Integer getId(){
        return id;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setReplyState(Integer replyState){
        this.replyState=replyState;
    }
    public Integer getReplyState(){
        return replyState;
    }
    public void setReplyTime(Timestamp replyTime){
        this.replyTime=replyTime;
    }
    public Timestamp getReplyTime(){
        return replyTime;
    }
	public Timestamp getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Timestamp completeTime) {
		this.completeTime = completeTime;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
    
}