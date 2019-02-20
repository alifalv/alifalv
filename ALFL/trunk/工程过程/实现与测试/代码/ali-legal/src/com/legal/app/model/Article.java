package com.legal.app.model;

import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@MyBatisEntity(namespace="Article")
@ApiModel(value = "文章实体类")
public class Article implements Serializable{
    private Integer articleId;
    private Integer userId;
    private String realName;
    private Integer isofficial;
    private Integer isOutJoin;
    @ApiModelProperty(notes = "标题", required = false)
    private String title;
    private String columnCode;
    private String columnName;
    private String txtPath;
    private String sendTime;
    private Integer lookNum;
    private String outJoinUrl;
    private String coverImg;
    private Integer articleState;
    private Integer isDelete;
    private Integer isPush;
    private Integer likeNum;
    private Integer collectionNum;

    public void setArticleId(Integer articleId){
        this.articleId=articleId;
    }
    public Integer getArticleId(){
        return articleId;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setRealName(String realName){
        this.realName=realName;
    }
    public String getRealName(){
        return realName;
    }
    public void setIsofficial(Integer isofficial){
        this.isofficial=isofficial;
    }
    public Integer getIsofficial(){
        return isofficial;
    }
    public void setIsOutJoin(Integer isOutJoin){
        this.isOutJoin=isOutJoin;
    }
    public Integer getIsOutJoin(){
        return isOutJoin;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getTitle(){
        return title;
    }
    public void setColumnCode(String columnCode){
        this.columnCode=columnCode;
    }
    public String getColumnCode(){
        return columnCode;
    }
    public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public void setTxtPath(String txtPath){
        this.txtPath=txtPath;
    }
    public String getTxtPath(){
        return txtPath;
    }
    public void setSendTime(String sendTime){
        this.sendTime=sendTime;
    }
    public String getSendTime(){
        return sendTime;
    }
    public void setLookNum(Integer lookNum){
        this.lookNum=lookNum;
    }
    public Integer getLookNum(){
        return lookNum;
    }
    public void setOutJoinUrl(String outJoinUrl){
        this.outJoinUrl=outJoinUrl;
    }
    public String getOutJoinUrl(){
        return outJoinUrl;
    }
    public void setCoverImg(String coverImg){
        this.coverImg=coverImg;
    }
    public String getCoverImg(){
        return coverImg;
    }
	public Integer getArticleState() {
		return articleState;
	}
	public void setArticleState(Integer articleState) {
		this.articleState = articleState;
	}
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	public Integer getIsPush() {
		return isPush;
	}
	public void setIsPush(Integer isPush) {
		this.isPush = isPush;
	}
	public Integer getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}
	public Integer getCollectionNum() {
		return collectionNum;
	}
	public void setCollectionNum(Integer collectionNum) {
		this.collectionNum = collectionNum;
	}
}