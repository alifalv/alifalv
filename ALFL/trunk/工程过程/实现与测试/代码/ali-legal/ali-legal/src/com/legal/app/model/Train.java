package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="Train")
public class Train implements Serializable{
    private Integer trainIs;
    private String trainName;
    private String trainAddress;
    private String cost;
    private Integer size;
    private String trainContent;
    private String mobile;
    private String email;
    private String trinDesc;
    private Integer articleIs;

    public void setTrainIs(Integer trainIs){
        this.trainIs=trainIs;
    }
    public Integer getTrainIs(){
        return trainIs;
    }
    public void setTrainName(String trainName){
        this.trainName=trainName;
    }
    public String getTrainName(){
        return trainName;
    }
    public void setTrainAddress(String trainAddress){
        this.trainAddress=trainAddress;
    }
    public String getTrainAddress(){
        return trainAddress;
    }
    public void setCost(String cost){
        this.cost=cost;
    }
    public String getCost(){
        return cost;
    }
    public void setSize(Integer size){
        this.size=size;
    }
    public Integer getSize(){
        return size;
    }
    public void setTrainContent(String trainContent){
        this.trainContent=trainContent;
    }
    public String getTrainContent(){
        return trainContent;
    }
    public void setMobile(String mobile){
        this.mobile=mobile;
    }
    public String getMobile(){
        return mobile;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getEmail(){
        return email;
    }
    public void setTrinDesc(String trinDesc){
        this.trinDesc=trinDesc;
    }
    public String getTrinDesc(){
        return trinDesc;
    }
    public void setArticleIs(Integer articleIs){
        this.articleIs=articleIs;
    }
    public Integer getArticleIs(){
        return articleIs;
    }
}