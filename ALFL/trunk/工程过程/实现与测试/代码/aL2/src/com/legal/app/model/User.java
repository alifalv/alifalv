package com.legal.app.model;
import java.sql.Timestamp;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;

@MyBatisEntity(namespace="User")
public class User implements Serializable{
	 
    private Integer userId;
    private String userName;
    private String mobile;
    private String userPwd;
    private String nickName;
    private Integer userType;
    private String userImg;
    private Integer vipLevel;
    private Timestamp registerTime;
    private Timestamp inVipTime;
    private Timestamp expireTime;
    private Integer isWeChat;
    private Integer isWeiBo;
    private Integer isQQ;
    private Integer userState;
    private Integer isAuthentication;
    private Integer isContract;
    private Integer isPush;

    private String pushCode;

    private Wechat WeChat;
    private Weibo WeiBo;
    private Qq QQ;
    private Counselor counselor;
    private Consultant consultant;



    private Integer equipmentType;//设备类型
    private Integer is_contract;//是否签约
    private Integer is_push;//是否推荐
    
    private Integer integral;

    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setUserName(String userName){
        this.userName=userName;
    }
    public String getUserName(){
        return userName;
    }
    public void setMobile(String mobile){
        this.mobile=mobile;
    }
    public String getMobile(){
        return mobile;
    }
    public void setUserPwd(String userPwd){
        this.userPwd=userPwd;
    }
    public String getUserPwd(){
        return userPwd;
    }
    public void setNickName(String nickName){
        this.nickName=nickName;
    }
    public String getNickName(){
        return nickName;
    }
    public void setUserType(Integer userType){
        this.userType=userType;
    }
    public Integer getUserType(){
        return userType;
    }
    public void setUserImg(String userImg){
        this.userImg=userImg;
    }
    public String getUserImg(){
        return userImg;
    }
    public void setVipLevel(Integer vipLevel){
        this.vipLevel=vipLevel;
    }
    public Integer getVipLevel(){
        return vipLevel;
    }
    public void setRegisterTime(Timestamp registerTime){
        this.registerTime=registerTime;
    }
    public Timestamp getRegisterTime(){
        return registerTime;
    }
    public void setInVipTime(Timestamp inVipTime){
        this.inVipTime=inVipTime;
    }
    public Timestamp getInVipTime(){
        return inVipTime;
    }
    public void setExpireTime(Timestamp expireTime){
        this.expireTime=expireTime;
    }
    public Timestamp getExpireTime(){
        return expireTime;
    }
    public void setIsWeChat(Integer isWeChat){
        this.isWeChat=isWeChat;
    }
    public Integer getIsWeChat(){
        return isWeChat;
    }
    public void setIsWeiBo(Integer isWeiBo){
        this.isWeiBo=isWeiBo;
    }
    public Integer getIsWeiBo(){
        return isWeiBo;
    }
    public void setIsQQ(Integer isQQ){
        this.isQQ=isQQ;
    }
    public Integer getIsQQ(){
        return isQQ;
    }
    public void setUserState(Integer userState){
        this.userState=userState;
    }
    public Integer getUserState(){
        return userState;
    }
    public void setIsAuthentication(Integer isAuthentication){
        this.isAuthentication=isAuthentication;
    }
    public Integer getIsAuthentication(){
        return isAuthentication;
    }

    public Wechat getWeChat() {
        return WeChat;
    }

    public void setWeChat(Wechat weChat) {
        this.WeChat = weChat;
    }

    public Weibo getWeiBo() {
        return WeiBo;
    }

    public void setWeiBo(Weibo weiBo) {
        this.WeiBo = weiBo;
    }

    public Qq getQQ() {
        return QQ;
    }

    public void setQQ(Qq QQ) {
        this.QQ = QQ;
    }

    public String getPushCode() {
        return pushCode;
    }

    public void setPushCode(String pushCode) {
        this.pushCode = pushCode;
    }

    public Counselor getCounselor() {
        return counselor;
    }

    public void setCounselor(Counselor counselor) {
        this.counselor = counselor;
    }

    public Integer getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(Integer equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Consultant getConsultant() {
        return consultant;
    }

    public Integer getIsContract() {
        return isContract;
    }

    public void setIsContract(Integer isContract) {
        this.isContract = isContract;
    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
    }

    public Integer getIsPush() {
        return isPush;
    }

    public void setIsPush(Integer isPush) {
        this.isPush = isPush;
    }
	public Integer getIs_contract() {
		return is_contract;
	}
	public void setIs_contract(Integer is_contract) {
		this.is_contract = is_contract;
	}
	public Integer getIs_push() {
		return is_push;
	}
	public void setIs_push(Integer is_push) {
		this.is_push = is_push;
	}
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	 
}