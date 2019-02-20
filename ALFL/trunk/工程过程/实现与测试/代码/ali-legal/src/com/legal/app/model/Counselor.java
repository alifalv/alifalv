package com.legal.app.model;

import java.math.BigDecimal;
import java.util.List;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
import com.legal.app.controller.AddressController;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="Counselor")
@ApiModel(value = "咨询师信息")
public class Counselor implements Serializable{
    private Integer id;
    private Integer userId;
    private Integer province;
	@ApiModelProperty(notes = "省份描述", required = false)
    private String provinceDescription;
    private Integer city;
    @ApiModelProperty(notes = "城市描述", required = false)
    private String cityDescription;
    private Integer job;
    private String codeImg;
    private String realName;
    private String characterSign;
    private Integer carType;
    private String personDesc;
    private Integer adviceNum;
    private Integer followingNum;
    private Integer collectionNum;
    private Integer makeNum;
    private Integer complateAdviceNum;
    private Integer entrustNum;
    private BigDecimal levelScore;
    private BigDecimal attitudeScore;
    private BigDecimal sourceScore;
    private BigDecimal chargeScore;
    private Integer readNum;
    private String bankAccount;
    private String bankName;
    private String bankAccountName;
    private String companyName;
    private String registerAddress;
    private String workAddress;
    private String email;
    @ApiModelProperty(notes = "QQ账号", required = false)
    private String qq;
    @ApiModelProperty(notes = "微信账号", required = false)
    private String wechat;
    private String personImg;
    private String workImg;
    private String personUrl;
    @ApiModelProperty(notes = "积分", required = false)
    private Integer userScore;
    private BigDecimal userBalance;
    private Integer freeNum;
    private List<Speciality> speciality;
    private String licenseNum;

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
    public void setProvince(Integer province){
        this.province=province;
    }
    public Integer getProvince(){
        return province;
    }
    public void setCity(Integer city){
        this.city=city;
    }
    public Integer getCity(){
        return city;
    }
    public void setJob(Integer job){
        this.job=job;
    }
    public Integer getJob(){
        return job;
    }
    public void setCodeImg(String codeImg){
        this.codeImg=codeImg;
    }
    public String getCodeImg(){
        return codeImg;
    }
    public void setRealName(String realName){
        this.realName=realName;
    }
    public String getRealName(){
        return realName;
    }
    public void setCharacterSign(String characterSign){
        this.characterSign=characterSign;
    }
    public String getCharacterSign(){
        return characterSign;
    }
    public void setCarType(Integer carType){
        this.carType=carType;
    }
    public Integer getCarType(){
        return carType;
    }
    public void setPersonDesc(String personDesc){
        this.personDesc=personDesc;
    }
    public String getPersonDesc(){
        return personDesc;
    }
    public void setAdviceNum(Integer adviceNum){
        this.adviceNum=adviceNum;
    }
    public Integer getAdviceNum(){
        return adviceNum;
    }
    public void setFollowingNum(Integer followingNum){
        this.followingNum=followingNum;
    }
    public Integer getFollowingNum(){
        return followingNum;
    }
    public void setCollectionNum(Integer collectionNum){
        this.collectionNum=collectionNum;
    }
    public Integer getCollectionNum(){
        return collectionNum;
    }
    public void setMakeNum(Integer makeNum){
        this.makeNum=makeNum;
    }
    public Integer getMakeNum(){
        return makeNum;
    }
    public void setComplateAdviceNum(Integer complateAdviceNum){
        this.complateAdviceNum=complateAdviceNum;
    }
    public Integer getComplateAdviceNum(){
        return complateAdviceNum;
    }
    public void setEntrustNum(Integer entrustNum){
        this.entrustNum=entrustNum;
    }
    public Integer getEntrustNum(){
        return entrustNum;
    }
    public void setLevelScore(BigDecimal levelScore){
        this.levelScore=levelScore;
    }
    public BigDecimal getLevelScore(){
        return levelScore;
    }
    public void setAttitudeScore(BigDecimal attitudeScore){
        this.attitudeScore=attitudeScore;
    }
    public BigDecimal getAttitudeScore(){
        return attitudeScore;
    }
    public void setSourceScore(BigDecimal sourceScore){
        this.sourceScore=sourceScore;
    }
    public BigDecimal getSourceScore(){
        return sourceScore;
    }
    public void setChargeScore(BigDecimal chargeScore){
        this.chargeScore=chargeScore;
    }
    public BigDecimal getChargeScore(){
        return chargeScore;
    }
    public void setReadNum(Integer readNum){
        this.readNum=readNum;
    }
    public Integer getReadNum(){
        return readNum;
    }
    public void setBankAccount(String bankAccount){
        this.bankAccount=bankAccount;
    }
    public String getBankAccount(){
        return bankAccount;
    }
    public void setBankName(String bankName){
        this.bankName=bankName;
    }
    public String getBankName(){
        return bankName;
    }
    public void setBankAccountName(String bankAccountName){
        this.bankAccountName=bankAccountName;
    }
    public String getBankAccountName(){
        return bankAccountName;
    }
    public void setCompanyName(String companyName){
        this.companyName=companyName;
    }
    public String getCompanyName(){
        return companyName;
    }
    public void setRegisterAddress(String registerAddress){
        this.registerAddress=registerAddress;
    }
    public String getRegisterAddress(){
        return registerAddress;
    }
    public void setWorkAddress(String workAddress){
        this.workAddress=workAddress;
    }
    public String getWorkAddress(){
        return workAddress;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getEmail(){
        return email;
    }
    public void setQq(String qq){
        this.qq=qq;
    }
    public String getQq(){
        return qq;
    }
    public void setWechat(String wechat){
        this.wechat=wechat;
    }
    public String getWechat(){
        return wechat;
    }
    public void setPersonImg(String personImg){
        this.personImg=personImg;
    }
    public String getPersonImg(){
        return personImg;
    }
    public void setWorkImg(String workImg){
        this.workImg=workImg;
    }
    public String getWorkImg(){
        return workImg;
    }
    public void setPersonUrl(String personUrl){
        this.personUrl=personUrl;
    }
    public String getPersonUrl(){
        return personUrl;
    }
    public void setUserScore(Integer userScore){
        this.userScore=userScore;
    }
    public Integer getUserScore(){
        return userScore;
    }
    public void setUserBalance(BigDecimal userBalance){
        this.userBalance=userBalance;
    }
    public BigDecimal getUserBalance(){
        return userBalance;
    }
    public void setFreeNum(Integer freeNum){
        this.freeNum=freeNum;
    }
    public Integer getFreeNum(){
        return freeNum;
    }
	public String getProvinceDescription() {
		provinceDescription = AddressController.getProvince(province);
		return provinceDescription;
	}
	public void setProvinceDescription(String provinceDescription) {
		this.provinceDescription = provinceDescription;
	}
	public String getCityDescription() {
		cityDescription = AddressController.getCity(city);
		return cityDescription;
	}
	public void setCityDescription(String cityDescription) {
		this.cityDescription = cityDescription;
	}
	public List<Speciality> getSpeciality() {
		return speciality;
	}
	public void setSpeciality(List<Speciality> speciality) {
		this.speciality = speciality;
	}
	public String getLicenseNum() {
		return licenseNum;
	}
	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}
}