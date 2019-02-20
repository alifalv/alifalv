package com.legal.app.model;
import java.math.BigDecimal;
import java.sql.Timestamp;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
import com.legal.app.controller.AddressController;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="User")
@ApiModel(value = "用户实体")
public class User implements Serializable{
    @ApiModelProperty(notes = "id", required = false)
    private Integer userId;
    @ApiModelProperty(notes = "手机号", required = false)
    private String mobile;
    @ApiModelProperty(notes = "昵称", required = false)
    private String nickName;
    @ApiModelProperty(notes = "用户名", required = false)
    private String userName;
    @ApiModelProperty(notes = "省份", required = false)
    private Integer province;
	@ApiModelProperty(notes = "省份描述", required = false)
    private String provinceDescription;
    @ApiModelProperty(notes = "城市", required = false)
    private Integer city;
    @ApiModelProperty(notes = "城市描述", required = false)
    private String cityDescription;
    private String userPwd;
    @ApiModelProperty(notes = "会员类型 1：个人咨询者2：咨询师3：企业咨询者", required = false)
    private Integer userType;
    @ApiModelProperty(notes = "会员类型描述", required = false)
    private String userTypeDescription;
    private String userImg;
    @ApiModelProperty(notes = "vip等级", required = false)
    private Integer vipLevel;
    private String carType;
    @ApiModelProperty(notes = "车辆类型", required = false)
    private String carTypeDescription;
    @ApiModelProperty(notes = "注册时间", required = false)
    private Timestamp registerTime;
    private Timestamp inVipTime;
    private Timestamp expireTime;
    private Integer isWeChat;
    private Integer isWeiBo;
    private Integer isQQ;
    @ApiModelProperty(notes = "状态 1 正常 0 锁定", required = false)
    private Integer userState;
    private String userStateDescription;
    @ApiModelProperty(notes = "认证 0 未认证 1 待审核 2 已通过 3 未通过", required = false)
    private Integer isAuthentication;
    private String isAuthenticationDescription;
    @ApiModelProperty(notes = "是否签约 0 未签约 1 已签约", required = false)
    private Integer isContract;
    private String isContractDescription;
    private Integer isPush;

    private String pushCode;

    @ApiModelProperty(notes = "微信账号", required = false)
    private String WeChat;
    @ApiModelProperty(notes = "QQ账号", required = false)
    private String QQ;
    @ApiModelProperty(notes = "新浪微博", required = false)
    private String WeiBo;
    @ApiModelProperty(notes = "积分", required = false)
    private BigDecimal userScore;
    @ApiModelProperty(notes = "真实姓名", required = false)
    private String realName;

    private Integer equipmentType;//设备类型
    private Integer is_contract;//是否签约
    private Integer is_push;//是否推荐
    private String typeState;

    private Integer length;
    private Integer start;
    private Integer iDisplayStart;
    private Integer iDisplayLength;
    private String sEcho;

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
    public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getCarTypeDescription() {
        	if(carType==null || carType.equals("")) {
        		carTypeDescription = "";
        	}else if(carType.equals("0")) {
    			carTypeDescription = "其它";
    		}else if(carType.equals("1")) {
    			carTypeDescription = "货车";
    		}else if(carType.equals("2")) {
    			carTypeDescription = "客车";
    		}
		return carTypeDescription;
	}
	public void setCarTypeDescription(String carTypeDescription) {
		this.carTypeDescription = carTypeDescription;
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

    public String getWeChat() {
        return WeChat;
    }

    public void setWeChat(String weChat) {
        WeChat = weChat;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getWeiBo() {
        return WeiBo;
    }

    public void setWeiBo(String weiBo) {
        WeiBo = weiBo;
    }

    public String getPushCode() {
        return pushCode;
    }

    public void setPushCode(String pushCode) {
        this.pushCode = pushCode;
    }

    public Integer getIsContract() {
        return isContract;
    }

    public void setIsContract(Integer isContract) {
        this.isContract = isContract;
    }

    public BigDecimal getUserScore() {
        return userScore;
    }

    public void setUserScore(BigDecimal userScore) {
        this.userScore = userScore;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
	public Integer getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(Integer equipmentType) {
        this.equipmentType = equipmentType;
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

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(Integer iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public Integer getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(Integer iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public String getUserTypeDescription() {
        if (null != userType) {
            if (1 == userType.intValue()) {
                userTypeDescription = "个人咨询者";
            } else if (2 == userType.intValue()) {
                userTypeDescription = "咨询师";
            } else if (3 == userType.intValue()) {
                userTypeDescription = "企业咨询者";
            }
        }
        return userTypeDescription;
    }

    public void setUserTypeDescription(String userTypeDescription) {
        this.userTypeDescription = userTypeDescription;
    }

    public String getUserStateDescription() {
        if (null != userState) {
            if (0 == userState.intValue()) {
                userStateDescription = "锁定";
            } else if (1 == userState.intValue()) {
                userStateDescription = "正常";
            }
        }else {
            userStateDescription = "正常";
        }
        return userStateDescription;
    }

    public void setUserStateDescription(String userStateDescription) {
        this.userStateDescription = userStateDescription;
    }

    public String getIsAuthenticationDescription() {
        if (null != isAuthentication) {
        		if (0 == isAuthentication.intValue()) {
                isAuthenticationDescription = "未认证";
            }else if (1 == isAuthentication.intValue()) {
                isAuthenticationDescription = "待审核";
            }else if (2 == isAuthentication.intValue()) {
                isAuthenticationDescription = "已通过";
            }else if (3 == isAuthentication.intValue()) {
                isAuthenticationDescription = "未通过";
            }
        } else {
            isAuthenticationDescription = "未认证";
        }
        return isAuthenticationDescription;
    }

    public void setIsAuthenticationDescription(String isAuthenticationDescription) {
        this.isAuthenticationDescription = isAuthenticationDescription;
    }

    public String getIsContractDescription() {
        if (null != isContract) {
            if (1 == isContract.intValue()) {
                isContractDescription = "已签约";
            } else if (0 == isContract.intValue()) {
                isContractDescription = "未签约";
            }
        } else {
            isContractDescription = "未签约";
        }
        return isContractDescription;
    }

    public void setIsContractDescription(String isContractDescription) {
        this.isContractDescription = isContractDescription;
    }

    public Integer getProvince() {
		return province;
	}
	public void setProvince(Integer province) {
		this.province = province;
	}
	public String getProvinceDescription() {
		if(province!=null) {
			provinceDescription = AddressController.getProvince(province);
		}
		return provinceDescription;
	}
	public void setProvinceDescription(String provinceDescription) {
		this.provinceDescription = provinceDescription;
	}

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }
	public String getCityDescription() {
		if(city!=null) {
			cityDescription = AddressController.getCity(city);
		}
        return cityDescription;
    }

    public void setCityDescription(String cityDescription) {
        this.cityDescription = cityDescription;
    }
	public String getTypeState() {
		return typeState;
	}
	public void setTypeState(String typeState) {
		this.typeState = typeState;
	}
}