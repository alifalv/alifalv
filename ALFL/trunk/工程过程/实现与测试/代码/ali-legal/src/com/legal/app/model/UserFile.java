package com.legal.app.model;
import java.io.Serializable;
import com.common.dbutil.MyBatisEntity;
@SuppressWarnings("serial")
@MyBatisEntity(namespace="UserFile")
public class UserFile implements Serializable{
    private Integer fileId;
    private String fileName;
    private Integer fileSize;
    private String fileType;
    private String filePath;
    private Integer userId;

    public void setFileId(Integer fileId){
        this.fileId=fileId;
    }
    public Integer getFileId(){
        return fileId;
    }
    public void setFileName(String fileName){
        this.fileName=fileName;
    }
    public String getFileName(){
        return fileName;
    }
    public void setFileSize(Integer fileSize){
        this.fileSize=fileSize;
    }
    public Integer getFileSize(){
        return fileSize;
    }
    public void setFileType(String fileType){
        this.fileType=fileType;
    }
    public String getFileType(){
        return fileType;
    }
    public void setFilePath(String filePath){
        this.filePath=filePath;
    }
    public String getFilePath(){
        return filePath;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
    }
}