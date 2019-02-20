package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="Company")
public class Company implements Serializable{
    private Integer id;
    private Integer userId;
    private String companyCode;
    private String companyName;
    private String companySortName;
    private String registerAddress;
    private String workAddress;
    private String realName;
    private String workImg;
    private Integer adviceNum;
    private Integer followingNum;
    private Integer collectionNum;
    private Integer province;
    private Integer city;

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
    public void setCompanyCode(String companyCode){
        this.companyCode=companyCode;
    }
    public String getCompanyCode(){
        return companyCode;
    }
    public void setCompanyName(String companyName){
        this.companyName=companyName;
    }
    public String getCompanyName(){
        return companyName;
    }
    public void setCompanySortName(String companySortName){
        this.companySortName=companySortName;
    }
    public String getCompanySortName(){
        return companySortName;
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
    public void setRealName(String realName){
        this.realName=realName;
    }
    public String getRealName(){
        return realName;
    }
    public void setWorkImg(String workImg){
        this.workImg=workImg;
    }
    public String getWorkImg(){
        return workImg;
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
}