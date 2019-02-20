package com.legal.app.model;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@MyBatisEntity(namespace="RecommendedToday")
@ApiModel(value = "今日推荐")
public class RecommendedToday {
    @ApiModelProperty(notes = "编号", required = false)
    private Integer id;

    @ApiModelProperty(notes = "标题", required = false)
    private String title;

    private Integer columnType;
    @ApiModelProperty(notes = "所属栏目类型", required = false)
    private String columnTypeDescription;

    @ApiModelProperty(notes = "文章ID", required = false)
    private Integer articleId;

    @ApiModelProperty(notes = "顺序", required = false)
    private Integer order;

    private Integer createUser;
    @ApiModelProperty(notes = "创建者", required = false)
    private String createUserName;

    @ApiModelProperty(notes = "创建时间", required = false)
    private String createTime;

    @ApiModelProperty(notes = "是否可用", required = false)
    private Integer isAvailable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getColumnType() {
        return columnType;
    }

    public void setColumnType(Integer columnType) {
        this.columnType = columnType;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getColumnTypeDescription() {
        return columnTypeDescription;
    }

    public void setColumnTypeDescription(String columnTypeDescription) {
        this.columnTypeDescription = columnTypeDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
}