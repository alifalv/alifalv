package com.legal.app.model;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="Advice")
public class Advice implements Serializable{
    private Integer adviceId;
    private Integer userId;
    private String userName;    private String userImg;
    private Integer caseType;
    private BigDecimal reward;
    private Timestamp sendTime;
    private Integer state;    /** 标题 */    private String title;

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
    
    public String getUserName() {		return userName;	}	public void setUserName(String userName) {		this.userName = userName;	}	public void setCaseType(Integer caseType){
        this.caseType=caseType;
    }
    public Integer getCaseType(){
        return caseType;
    }
    public void setReward(BigDecimal reward){
        this.reward=reward;
    }
    public BigDecimal getReward(){
        return reward;
    }
    public void setSendTime(Timestamp sendTime){
        this.sendTime=sendTime;
    }
    public Timestamp getSendTime(){
        return sendTime;
    }
    public void setState(Integer state){
        this.state=state;
    }
    public Integer getState(){
        return state;
    }	public String getUserImg() {		return userImg;	}	public void setUserImg(String userImg) {		this.userImg = userImg;	}	public String getTitle() {		return title;	}	public void setTitle(String title) {		this.title = title;	}    
}