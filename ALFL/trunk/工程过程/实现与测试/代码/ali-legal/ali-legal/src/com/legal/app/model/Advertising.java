package com.legal.app.model;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@MyBatisEntity(namespace="Advertising")
@ApiModel(value = "广告设置")
public class Advertising {
    @ApiModelProperty(notes = "编号", required = false)
    private Integer id;

    @ApiModelProperty(notes = "广告名称", required = false)
    private String name;

    @ApiModelProperty(notes = "广告链接", required = false)
    private String url;

    @ApiModelProperty(notes = "广告类型 0： 内容 1：外链", required = false)
    private Integer columnType;

    @ApiModelProperty(notes = "摆放位置", required = false)
    private String place;

    @ApiModelProperty(notes = "广告图url", required = false)
    private String image;

    @ApiModelProperty(notes = "是否生效 1 ：生效  0：下架", required = false)
    private Integer isAvailable;

    @ApiModelProperty(notes = "内容", required = false)
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getColumnType() {
        return columnType;
    }

    public void setColumnType(Integer columnType) {
        this.columnType = columnType;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place == null ? null : place.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

}