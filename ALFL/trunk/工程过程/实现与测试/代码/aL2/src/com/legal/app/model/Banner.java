package com.legal.app.model;

import com.common.dbutil.MyBatisEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@MyBatisEntity(namespace="Banner")
@ApiModel(value = "banner")
public class Banner {
    @ApiModelProperty(notes = "编号", required = false)
    private Integer id;

    @ApiModelProperty(notes = "名称", required = false)
    private String name;

    @ApiModelProperty(notes = "链接", required = false)
    private String url;

    private Integer columnType;
    @ApiModelProperty(notes = "所属列别", required = false)
    private String columnTypeDescription;

    @ApiModelProperty(notes = "摆放位置", required = false)
    private String place;

    private Integer type;

    @ApiModelProperty(notes = "是否可用", required = false)
    private Integer isAvailable;

    @ApiModelProperty(notes = "内容", required = false)
    private String content;

    //private List<Resources> images;

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

    public String getColumnTypeDescription() {
        return columnTypeDescription;
    }

    public void setColumnTypeDescription(String columnTypeDescription) {
        this.columnTypeDescription = columnTypeDescription;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place == null ? null : place.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    /*public List<Resources> getImages() {
        return images;
    }

    public void setImages(List<Resources> images) {
        this.images = images;
    }*/
}