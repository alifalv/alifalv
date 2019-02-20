package com.legal.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;
import com.common.web.WebUtils;

@Controller
@RequestMapping("file")
public class FileController extends SuperController{

	@Value("#{configProperties['filePath']}") 
	private String filePath;
	

	private String imgPath = "aliImgs"; //图片地址的前缀 
	
	private String fileUrl= "aliFiles";//文件地址的前缀
	
	/**
	 * 上传图片  只支持base64格式文件上传
	 */
	@RequestMapping("uploadImg")
	public void uploadImg(
			@RequestParam("imgBase64Str") String imgBase64Str,
			PrintWriter out){
		if(imgBase64Str==null||imgBase64Str.length()==0){
			throw new BusinessException("图片不存在!", -100);
		}
		String imgFile = null;
		try {
			//后缀
			String suffix = null;
			//图片内容
			String imgContent = null;
			
			if(imgBase64Str.indexOf("base64")!=-1){
				suffix = "png";
				imgContent = imgBase64Str.split(",")[1];
			}else{
				suffix = "png";
				imgContent = imgBase64Str;
			}
			
             //Base64解码  
            byte[] b = Base64.getDecoder().decode(imgContent);  
            for(int i=0;i<b.length;++i)  
            {  
                if(b[i]<0)  
                {//调整异常数据  
                    b[i]+=256;  
                }  
            }  
            String uuid = System.nanoTime()+"";
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); 
            String dateStr =  df.format(new Date()); 
            String add =   filePath +"//"+dateStr;
            File saveFile = new File(add); 
            if(!saveFile.exists()){
            	Boolean bl = saveFile.mkdirs(); 
                ExceptionLogger.writeLog("创建图片文件夹："+add);
            }
            //生成图片  
            imgFile = uuid+"."+suffix;//新生成的图片  
            OutputStream output = new FileOutputStream(new File(saveFile, imgFile));   
            
            ExceptionLogger.writeLog("图片上传路径："+add+"/"+imgFile);
            output.write(b);
            output.flush();
            output.close();
            ExceptionLogger.writeLog("图片上传成功！");
            out.print(WebUtils.responseData(imgPath+"/"+dateStr+"/"+imgFile));
		} catch (Exception e) {
			ExceptionLogger.writeLog(e+"");
			throw new BusinessException("图片上传失败！",-100);
		}
		
	}
	
	@RequestMapping("uploadFile")
	public void uploadFile( 
			@RequestParam("fileBase64Str") String fileBase64Str,
			HttpServletRequest request,
		PrintWriter out) throws Exception{
	  Map<String, Object> m = 	getMap(request);
	  if(null == fileBase64Str || fileBase64Str.length() == 0 ) {
			throw new BusinessException("上传文件失败!", -1);
	  }
	  String  suffix=null;
	  String fileContent=null;
	  //截取文件名；
	  if(fileBase64Str.indexOf("base64")!=-1){
			String [] str =  fileBase64Str.split(",");
			 suffix=str[0].replaceAll("data:application/", "").replaceAll(";base64", "").replaceAll("data:image/", "").replaceAll("/plain", "").replaceAll("data:", "");
			 if(suffix.equals("msword")){
				 suffix = "doc"; 
			 }else if(suffix.equals("vnd.openxmlformats-officedocument.wordprocessingml.document")){
				 suffix = "docx";
			 }
			fileContent =str[1];
		    ExceptionLogger.writeLog("文件格式："+suffix);
		}else{
			suffix = "text";
			fileContent = fileBase64Str;
		}
	  
	  try {
		//Base64解码  
		  byte[] b = Base64.getDecoder().decode(fileContent);  
		  for(int i=0;i<b.length;++i)  
		  {  
		      if(b[i]<0)  
		      {//调整异常数据  
		          b[i]+=256;  
		      }  
		  }  
		  String fileName = "";
		  
		  String uuid = System.nanoTime()+"";
		  SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); 
		  String dateStr =  df.format(new Date()); 
		  String add =   filePath +"\\"+dateStr;
		  File saveFile = new File(add); 
		  if(!saveFile.exists()){
		  	Boolean bl = saveFile.mkdirs(); 
            ExceptionLogger.writeLog("创建wenjian文件夹："+add);

		  }
		  //生成图片  
		  fileName = uuid+"."+suffix;//新生成的图片  
		  OutputStream output = new FileOutputStream(new File(saveFile, fileName));    
		  ExceptionLogger.writeLog("文件上传保存路径："+add+fileName);
		  output.write(b);
		  output.flush();
		  output.close();
		  ExceptionLogger.writeLog("文件上传成功！"+"返回页面地址："+fileUrl+"/"+dateStr+"/"+fileName);
		  out.print(WebUtils.responseData(fileUrl+"/"+dateStr+"/"+fileName));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new BusinessException("文件上传失败.", -1);
	} 
	}
	
	
	/**
	 * 上传文件
	 */
	
	@RequestMapping("uploadFiles")
	public void uploadFiles(
			@RequestParam("file") MultipartFile file,
			PrintWriter out) throws Exception{
		String fileName = null;
		try {
			String uuid = System.nanoTime()+"";
		    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); 
            String dateStr =  df.format(new Date()); 
            String add = filePath +"\\"+dateStr;
	        File saveFile = new File(add);
	        if(!saveFile.exists()){
	        	saveFile.mkdirs();
	        }
	        
	        //获取文件后缀
	        fileName = file.getOriginalFilename();
	        //后缀
	        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
	        
	        fileName = uuid+"."+suffix;
	        
	        file.transferTo(new File(saveFile, fileName));
	        
	        out.print(WebUtils.responseData(fileUrl +"/"+dateStr+"/"+fileName));
	        
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("文件上传失败.", -1);
		}
		
		
        
	}
	
	/**
	 * 展示图片
	 */
	@RequestMapping("images/{imgName}")
	public void showImage(
			@PathVariable("imgName") String imgName,
			HttpServletResponse response
			) throws Exception{
		
		FileInputStream fin = null;
		try {
			File file = new File(filePath,imgName+".png");
			response.setHeader("Content-Type","image/png");//设置响应的媒体类型，这样浏览器会识别出响应的是图片
			
			fin = new FileInputStream(file);
			
			byte [] bs = new byte[1024];
			int readNum = -1;
			while((readNum=fin.read(bs))!=-1){
				response.getOutputStream().write(bs,0,readNum);
			}
			response.getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendError(404);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}finally{
			if(fin!=null){
				fin.close();
			}
			response.getOutputStream().close();
		}
	}
	
	/**
	 * 下载文件
	 */
	@RequestMapping("downloadFile")
	public void downloadFile(
			@RequestParam("fileName") String fileName,
			HttpServletResponse response
			) throws Exception{
		
		FileInputStream fin = null;
		try {
			File file = new File(filePath,fileName);
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment;filename="+fileName);
			fin = new FileInputStream(file);
			
			byte [] bs = new byte[1024];
			int readNum = -1;
			while((readNum=fin.read(bs))!=-1){
				response.getOutputStream().write(bs,0,readNum);
			}
			response.getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendError(404);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}finally{
			if(fin!=null){
				fin.close();
			}
			response.getOutputStream().close();
		}
		
	}
	
	
	
}
