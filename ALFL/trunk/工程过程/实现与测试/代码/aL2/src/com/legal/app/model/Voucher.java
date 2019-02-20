package com.legal.app.model;
import java.sql.Timestamp;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="Voucher")
public class Voucher implements Serializable{
    private Integer voucherId;
    private String voucherDesc;
    private Integer userId;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer state;

    public void setVoucherId(Integer voucherId){
        this.voucherId=voucherId;
    }
    public Integer getVoucherId(){
        return voucherId;
    }
    public void setVoucherDesc(String voucherDesc){
        this.voucherDesc=voucherDesc;
    }
    public String getVoucherDesc(){
        return voucherDesc;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setStartTime(Timestamp startTime){
        this.startTime=startTime;
    }
    public Timestamp getStartTime(){
        return startTime;
    }
    public void setEndTime(Timestamp endTime){
        this.endTime=endTime;
    }
    public Timestamp getEndTime(){
        return endTime;
    }
    public void setState(Integer state){
        this.state=state;
    }
    public Integer getState(){
        return state;
    }
}