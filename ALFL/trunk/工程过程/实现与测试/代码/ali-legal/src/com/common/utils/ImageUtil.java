package com.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.cloopen.rest.sdk.utils.encoder.BASE64Decoder;
import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;

/**
 * base64编码图片处理工具
 * @author hzh
 *
 */
public class ImageUtil {
	
	public static final int USER_IMG_TYPE = 1;//用户头像
	public static final String USER_IMG_PREFIX = "/user";
	public static final int NEWS_IMG_TYPE = 2;//帖子图片 
	public static final String NEWS_IMG_PREFIX = "/news";
	public static final int ROAD_CONDITION_IMG_TYPE = 3;//路况
	public static final String ROAD_CONDITION_IMG_PREFIX = "/condition";
	public static final int BRAND_IMG_TYPE = 4;//品牌
	public static final String BRAND_IMG_PREFIX = "/brand";
	public static final int DRIVE_IMG_TYPE = 5;//驾驶证
	public static final String DRIVE_IMG_PREFIX = "/drive";
	public static final int TRAVEL_IMG_TYPE = 6;//行驶证
	public static final String TRAVEL_IMG_PREFIX = "/travel";
	
	
	public static final int SUGGEST_IMG_TYPE = 7;//意见反馈
	public static final String SUGGEST_IMG_PREFIX = "/suggest";
	
	public static final int MESSAGE_IMG_TYPE = 8;//消息图片
	public static final String MESSAGE_IMG_PREFIX = "/message";
	
	
	public static final int PRODUCT_IMG_TYPE = 9;//商品图片
	public static final String PRODUCT_IMG_PREFIX = "/product";
	
	/**
	 * base64图片存储
	 * @param base64String base64图片字符
	 * @param imgType 图片类型  1:用户头像2：帖子图片  3 路况 4 品牌
	 * @return
	 */
	public static synchronized String base64ToImg(String base64String,int imgType) throws Exception{
		
		//获取图片类型
		if(base64String==null||base64String.length()==0){
			throw new BusinessException("图片不存在!", -100);
		}
		
		try {
			//后缀
			String suffix = null;
			//图片内容
			String imgContent = null;
			
			if(base64String.indexOf("base64")!=-1){
				suffix = "png";
				imgContent = base64String.split(",")[1];
			}else{
				suffix = "png";
				imgContent = base64String;
			}
			
			
			BASE64Decoder decoder = new BASE64Decoder();  
             //Base64解码  
            byte[] b = decoder.decodeBuffer(imgContent);  
            for(int i=0;i<b.length;++i)  
            {  
                if(b[i]<0)  
                {//调整异常数据  
                    b[i]+=256;  
                }  
            }  
             
            String savePath = null;
            
            String path = null;
            
            String uuid = System.nanoTime()+"";
             
            switch (imgType) {
			    case USER_IMG_TYPE:
			    	savePath = Const.user_imgs_path;
			    	path = USER_IMG_PREFIX;
			    	break;
			    case NEWS_IMG_TYPE:
			    	savePath = Const.news_imgs_path;
			    	path = NEWS_IMG_PREFIX;
			    	break;
			    case ROAD_CONDITION_IMG_TYPE:
			    	savePath = Const.road_condition_imgs_path;
			    	path = ROAD_CONDITION_IMG_PREFIX;
			    	break;
			    case BRAND_IMG_TYPE:
			    	savePath = Const.brand_imgs_path;
			    	path = BRAND_IMG_PREFIX;
			    	break;
			    case DRIVE_IMG_TYPE:
			    	savePath = Const.drive_imgs_path;
			    	path = DRIVE_IMG_PREFIX;
			    	break;
			    case TRAVEL_IMG_TYPE:
			    	savePath = Const.travel_imgs_path;
			    	path = TRAVEL_IMG_PREFIX;
			    	break;
			    case SUGGEST_IMG_TYPE:
			    	savePath = Const.suggest_imgs_path;
			    	path = SUGGEST_IMG_PREFIX;
			    	break;
			    case MESSAGE_IMG_TYPE:
			    	savePath = Const.message_imgs_path;
			    	path = MESSAGE_IMG_PREFIX;
			    	break;
			    case PRODUCT_IMG_TYPE:
			    	savePath = Const.product_imgs_path;
			    	path = PRODUCT_IMG_PREFIX;
			    	break;
			}
            
            File saveFile = new File(savePath);
            
            if(!saveFile.exists()){
            	saveFile.mkdir();
            }
            //生成图片  
            String imgFile = uuid+"."+suffix;//新生成的图片  
            OutputStream out = new FileOutputStream(new File(saveFile, imgFile));      
            out.write(b);  
            out.flush();  
            out.close();
            
            ExceptionLogger.writeLog("图片上传成功！");
            
            return path+"/"+imgFile;
		} catch (Exception e) {
			ExceptionLogger.writeLog(e+"");
			throw new BusinessException("图片上传失败！",-100);
		}
		
	}
	
	
	/**
	 * 根据图片http路径获取图片
	 * @param imgUrl
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String downloadImageByUrl(String imgUrl,String openid){
		
		HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(imgUrl); 
        
        OutputStream out = null;
        InputStream in = null;
        
        try {  
            HttpResponse resp = httpclient.execute(httpget);  
            if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {  
                HttpEntity entity = resp.getEntity();  
  
                in = entity.getContent();  
  
                File saveFile = new File(Const.user_imgs_path);
                
                if(!saveFile.exists()){
                	saveFile.mkdir();
                }
                
                
                //生成图片  
                String imgFile = openid+".png";//新生成的图片  

                out = new FileOutputStream(new File(saveFile, imgFile));   
                
                byte[] buf = new byte[1024];  
                int len = 0;  
                while ((len = in.read(buf)) != -1) {  
                    out.write(buf, 0, len);  
                }  
                out.flush();  
                out.close();  
                
                ExceptionLogger.writeLog("微信头像上传成功！");
                
                return USER_IMG_PREFIX+"/"+imgFile;
                
            }  
  
        } catch (Exception e) {  
            e.printStackTrace();
            ExceptionLogger.writeLog("微信头像上传失败...");
        } finally {  
            httpclient.getConnectionManager().shutdown();  
        } 
        
        return Const.default_img_path;
	}
	
	
	public static String getBrandImgUrl(int brand_id){
		return ImageUtil.BRAND_IMG_PREFIX+"/"+"b"+brand_id+".jpg";
	}
	
}
