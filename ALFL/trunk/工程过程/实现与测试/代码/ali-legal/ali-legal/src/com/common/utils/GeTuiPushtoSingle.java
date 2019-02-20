package com.common.utils;

import java.util.ArrayList;
import java.util.List;

import com.common.log.ExceptionLogger;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;

/**
 * 个推消息推送；
 * </br>创建时间：2018-06-18
 * @author hyq
 * 
 */
public class GeTuiPushtoSingle {
    //采用"Java SDK 快速入门"， "第二步 获取访问凭证 "中获得的应用配置，用户可以自行替换
    
    private static String appId = "bggVYy1Jyq53nAc3pLZo24";
    private static String appKey = "gtimlKjrvN7bljVZGg3NP1";
    private static String masterSecret = "MO2PB4TieY5IZRD4fHoid6";

    //static  String CID = "30929033bd62810b104f268b6c794524";//android 焦
    //static String CID1 = "425355c711166013bfeb30a7d19c6ca8";//ios
    //static String CID2 = "0db7620826303ae3d2fa8f969f9c1c4d";//android 华
    //别名推送方式
    // static String Alias = "";
    static String host = "http://sdk.open.api.igexin.com/apiex.htm";
    private static GeTuiPushtoSingle instance;
    private IGtPush push;
    public  static GeTuiPushtoSingle getInstance() {
    		if(instance==null) {
    			instance=new GeTuiPushtoSingle();
    		}
    		return instance;
    }
    
    private GeTuiPushtoSingle() {
    		push = new IGtPush(host, appKey, masterSecret);
    }
    
    
    /**
     * 针对苹果机推送；
     * @param clientId 客户端id，多个id间用英文逗号隔开； 
     * @param content 推送内容
     * @throws Exception
     */
    public void pushToApple(String clientId,String content) throws Exception {
        //IGtPush push = new IGtPush(host, appKey, masterSecret);
        TransmissionTemplate template= getTransmissionTemplate(content);
//        SingleMessage message = new SingleMessage();
//        message.setOffline(true);
//        // 离线有效时间，单位为毫秒，可选
//        message.setOfflineExpireTime(24 * 3600 * 1000);
//        message.setData(template);
        
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0); 
        
//      Target target = new Target();
//      target.setAppId(appId);
//      target.setClientId(clientId);
        
        String[] clientIds=clientId.split(",");
        List<Target> clientList=new ArrayList();
        for(String id  : clientIds) {
        		Target target = new Target();
	        target.setAppId(appId);
	        target.setClientId(clientId);
	        clientList.add(target);
        }
        //target.setAlias(Alias);
        IPushResult ret = null;
        try {
//   		ret = push.pushMessageToSingle(message, target);
        		// taskId用于在推送时去查找对应的message
            String taskId = push.getContentId(message);
        		ret=push.pushMessageToList(taskId, clientList);
        } catch (RequestException e) {
        		ExceptionLogger.writeLog(e,this.getClass());
//            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            ExceptionLogger.writeLog(ret.getResponse().toString());
        } else {
        		ExceptionLogger.writeLog("服务器响应异常");
        }
    }
  
    
    /**
     * 针对android机推送；
     * @param clientId 客户端id字符串，多个id用英文逗号隔开；
     * @param content 推送内容
     * @throws Exception
     */
    public void pushToAndroid(String clientId,String content) throws Exception {
       
        //LinkTemplate template = linkTemplateDemo(content,content);
        NotificationTemplate template = getNotificationTemplate(content);
//        SingleMessage message = new SingleMessage();
//        message.setOffline(true);
//        // 离线有效时间，单位为毫秒，可选
//        message.setOfflineExpireTime(24 * 3600 * 1000);
//        message.setData(template);

        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        
        
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0); 
        
//      Target target = new Target();
//      target.setAppId(appId);
//      target.setClientId(clientId);
        
        String[] clientIds=clientId.split(",");
        List<Target> clientList=new ArrayList();
        for(String id  : clientIds) {
        		Target target = new Target();
	        target.setAppId(appId);
	        target.setClientId(clientId);
	        clientList.add(target);
        }
        

        //target.setAlias(Alias);
        IPushResult ret = null;
        try {
//          ret = push.pushMessageToSingle(message, target);
        	
        		// taskId用于在推送时去查找对应的message
            String taskId = push.getContentId(message);
        		ret=push.pushMessageToList(taskId, clientList);
        } catch (RequestException e) {
        		ExceptionLogger.writeLog(e,this.getClass());
//          ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            ExceptionLogger.writeLog(ret.getResponse().toString());
        } else {
        		ExceptionLogger.writeLog("服务器响应异常");
        }
    }
    
    
    /**
     * 安桌透传消息模板
     * @param title
     * @param content
     * @return
     */
    private static NotificationTemplate getNotificationTemplate(String content) {
    	 	NotificationTemplate template = new NotificationTemplate();
         // 设置APPID与APPKEY
         template.setAppId(appId);
         template.setAppkey(appKey);

         Style0 style = new Style0();
         // 设置通知栏标题与内容
         style.setTitle("");
         style.setText(content);
         // 配置通知栏图标
         style.setLogo("icon.png");
         // 配置通知栏网络图标
         style.setLogoUrl("");
         // 设置通知是否响铃，震动，或者可清除
         style.setRing(true);
         style.setVibrate(true);
         style.setClearable(true);
         template.setStyle(style);

         // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
         template.setTransmissionType(2);
         template.setTransmissionContent(content);
         return template;
    }
    /**
     * ios透传消息模板
     * @param content 消息内容
     * @return
     */
    private static TransmissionTemplate getTransmissionTemplate(String content) {
   	 	TransmissionTemplate template = new TransmissionTemplate();
	    template.setAppId(appId);
	    template.setAppkey(appKey);
	    template.setTransmissionContent(content);
	    template.setTransmissionType(2);
	    APNPayload payload = new APNPayload();
	    payload.setBadge(1);
	    payload.setContentAvailable(1);
	    payload.setSound("default");
	    payload.setCategory("$由客户端定义");
	    //简单模式APNPayload.SimpleMsg
	    
	    payload.setAlertMsg(new APNPayload.SimpleAlertMsg(content));
	    //字典模式使用下者
	    //payload.setAlertMsg(getDictionaryAlertMsg());
	    template.setAPNInfo(payload);
	    return template;
   }
    
    public static void main(String[] args) throws Exception{
    		GeTuiPushtoSingle pusher= GeTuiPushtoSingle.getInstance();
    		pusher.pushToApple("425355c711166013bfeb30a7d19c6ca8", "look一下看看什么情况：）");
    		pusher.pushToAndroid("0db7620826303ae3d2fa8f969f9c1c4d", "look一下看看什么情况：）");
    }
}
