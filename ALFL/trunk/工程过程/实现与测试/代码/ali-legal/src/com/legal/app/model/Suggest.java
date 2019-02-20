package com.legal.app.model;

import java.io.Serializable;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="Suggest")
@ApiModel(value = "意见反馈实体")
public class Suggest implements Serializable{
    @ApiModelProperty(notes = "编号", required = false)
    private Integer suggestId;
    private String suggestTitle;
    @ApiModelProperty(notes = "反馈信息 内容关键词", required = false)
    private String suggestContent;
    private Integer userId;
    @ApiModelProperty(notes = "姓名", required = false)
    private String userName;
    @ApiModelProperty(notes = "联系方式", required = false)
    private String mobile;
    private String img1;
    private String img2;
    private String img3;
    @ApiModelProperty(notes = "时间", required = false)
    private String suggestTime;

    private Integer isComplate;
    @ApiModelProperty(notes = "状态", required = false)
    private String stateDescription;

    @ApiModelProperty(notes = "处理时间", required = false)
    private String complateTime;

    @ApiModelProperty(notes = "开始时间", required = false)
    private String suggestTimeStart;
    @ApiModelProperty(notes = "结束时间", required = false)
    private String suggestTimeEnd;
    
    @ApiModelProperty(notes = "回复内容", required = false)
    private String replyContent;

    private Integer isDelete;

    private Integer size;
    private Integer page;
    private Integer start;
    private Integer length;
    private String sEcho;

    public void setSuggestId(Integer suggestId){
        this.suggestId=suggestId;
    }
    public Integer getSuggestId(){
        return suggestId;
    }
    public void setSuggestTitle(String suggestTitle){
        this.suggestTitle=suggestTitle;
    }
    public String getSuggestTitle(){
        return suggestTitle;
    }
    public void setSuggestContent(String suggestContent){
        this.suggestContent=suggestContent;
    }
    public String getSuggestContent(){
        return suggestContent;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setImg1(String img1){
        this.img1=img1;
    }
    public String getImg1(){
        return img1;
    }
    public void setImg2(String img2){
        this.img2=img2;
    }
    public String getImg2(){
        return img2;
    }
    public void setImg3(String img3){
        this.img3=img3;
    }
    public String getImg3(){
        return img3;
    }
    public void setSuggestTime(String suggestTime){
        this.suggestTime=suggestTime;
    }
    public String getSuggestTime(){
        return suggestTime;
    }

    public String getSuggestTimeStart() {
        return suggestTimeStart;
    }

    public void setSuggestTimeStart(String suggestTimeStart) {
        this.suggestTimeStart = suggestTimeStart;
    }

    public String getSuggestTimeEnd() {
        return suggestTimeEnd;
    }

    public void setSuggestTimeEnd(String suggestTimeEnd) {
        this.suggestTimeEnd = suggestTimeEnd;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getIsComplate() {
        return isComplate;
    }

    public void setIsComplate(Integer isComplate) {
        this.isComplate = isComplate;
    }

    public String getComplateTime() {
        return complateTime;
    }

    public void setComplateTime(String complateTime) {
        this.complateTime = complateTime;
    }
    public void setReplyContent(String replyContent){
        this.replyContent=replyContent;
    }
    public String getReplyContent(){
        return replyContent;
    }

    public String getStateDescription() {
        if (null != isComplate) {
            if (1 == isComplate.intValue()) {
                stateDescription = "已处理";
            }else if (0 == isComplate.intValue()) {
                stateDescription = "未处理";
            }
        } else {
            stateDescription = "";
        }
        return stateDescription;
    }

    public void setStateDescription(String stateDescription) {
        this.stateDescription = stateDescription;
    }
}