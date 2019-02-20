package com.common.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.beans.factory.annotation.Autowired;

import com.common.cache.ICache;
import com.common.log.ExceptionLogger;

public class SMSUtilLiantong {

	private  static String url = "http://sms.api.ums86.com:8899/sms/Api/Send.do";
	private static String SpCode = "233243";
	private static String LoginName = "hn_alfy";
	private static String Password = "alifalv2015";
	@Autowired
	protected  ICache cache;
    /**
     * 
     * @param tel 手机号码
     * @param MessageContent 短信内容
     * @return 成功返回true,失败返回false;
     * @throws Exception
     */
	public static boolean sendCheckCode(String tel, String messageContent) throws Exception {
		ExceptionLogger.writeLog(">>>" + messageContent);
		
		HttpClient httpclient = new HttpClient();
		PostMethod post = new PostMethod(url);//
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"gbk");
		post.addParameter("SpCode", SpCode);
		post.addParameter("LoginName",LoginName);
		post.addParameter("Password", Password);
		post.addParameter("MessageContent", messageContent);
		post.addParameter("UserNumber", tel);
		post.addParameter("SerialNumber", String.valueOf(System.currentTimeMillis()));
		post.addParameter("ScheduleTime", "");
		post.addParameter("ExtendAccessNum", "");
		post.addParameter("f", "1");
		httpclient.executeMethod(post);
 		ExceptionLogger.writeLog("联通短信请求包： " + post.toString());
		String info = new String(post.getResponseBody(),"gbk");
 		ExceptionLogger.writeLog("联通短信发送响应报文：" + info);
 		String state = info.split("&")[0].split("=")[1];
 		String description = info.split("&")[1].split("=")[1];		
 		if(state.equals("0")){
 	 		ExceptionLogger.writeLog("发送成功");
 	 		return true;
 		}
 		ExceptionLogger.writeLog("发送失败："+description);
 		return false;
	}
	
	
	
	 /**
     * 
     * @param tel 手机号码
     * @param MessageContent 短信内容
     * @return 成功返回true,失败返回false;
     * @throws Exception
     */
	public static String sendMsg(String tel, String messageContent) throws Exception {
		ExceptionLogger.writeLog(">>>" + messageContent);
		
		HttpClient httpclient = new HttpClient();
		PostMethod post = new PostMethod(url);//
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"gbk");
		post.addParameter("SpCode", SpCode);
		post.addParameter("LoginName",LoginName);
		post.addParameter("Password", Password);
		post.addParameter("MessageContent", messageContent);
		post.addParameter("UserNumber", tel);
		post.addParameter("SerialNumber", String.valueOf(System.currentTimeMillis()));
		post.addParameter("ScheduleTime", "");
		post.addParameter("ExtendAccessNum", "");
		post.addParameter("f", "1");
		httpclient.executeMethod(post);
 		ExceptionLogger.writeLog("联通短信请求包： " + post.toString());
		String info = new String(post.getResponseBody(),"gbk");
 		ExceptionLogger.writeLog("联通短信发送响应报文：" + info);
 		String state = info.split("&")[0].split("=")[1];
 		String description = info.split("&")[1].split("=")[1];		
 		if(state.equals("0")){
 	 		ExceptionLogger.writeLog("发送成功");
 	 		return state;
 		}
 		ExceptionLogger.writeLog("发送失败："+description);
 		return description;
	}
	
	
	
	
	
	
	/**
	 *  mobileMsg+"您市又有新的案源了,登录账户中心查看吧,回复TD拒收,";
	 * @param mobile
	 * @param templateId
	 * @param content
	 * @param out
	 * @throws Exception
	 */
	public static  void sendNewAy(String mobile) throws Exception{
		String mobileMsg = "***"+mobile.substring(mobile.length()-4, mobile.length());
		String message3 = mobileMsg+"您市又有新的案源了,登录账户中心查看吧,回复TD拒收.";
 		boolean rs=SMSUtilLiantong.sendCheckCode(mobile, message3);
		ExceptionLogger.writeLog("发送结果："+rs);
		if(rs){
			ExceptionLogger.writeLog("发送成功.");
		}else{
			ExceptionLogger.writeLog("发送失败");
		}
	}	
	

	

	/**
	 * 一对一咨询(咨询师收)mobileMsg+"您收到新的法律咨询,请及时登录查看,回复!";
	 * @param mobile
	 * @param templateId
	 * @param content
	 * @param out
	 * @throws Exception
	 */
	public static void sendCounselor(String mobile) throws Exception{
		String mobileMsg = "***"+mobile.substring(mobile.length()-4, mobile.length());
		String message2 = mobileMsg+"您收到新的法律咨询,请及时登录查看,回复!";
 		boolean rs=SMSUtilLiantong.sendCheckCode(mobile, message2);
		ExceptionLogger.writeLog("发送结果："+rs);
		if(rs){
			ExceptionLogger.writeLog("发送成功.");
		}else{
			ExceptionLogger.writeLog("发送失败");
		}
	}	

	/**
	 * 一对一咨询(咨询用户收)mobileMsg+"你好!咨询师回复了你咨询的法律问题,请及时登录查看.";
	 * @param mobile
	 * @param templateId
	 * @param content
	 * @param out
	 * @throws Exception
	 */
	public static void sendConsultant(String mobile) throws Exception{
		String mobileMsg = "***"+mobile.substring(mobile.length()-4, mobile.length());
		String message1 = mobileMsg+"你好!咨询师回复了你咨询的法律问题,请及时登录查看.";
 		boolean rs=SMSUtilLiantong.sendCheckCode(mobile, message1);
		ExceptionLogger.writeLog("发送结果："+rs);
		if(rs){
			ExceptionLogger.writeLog("发送成功.");
		}else{
			ExceptionLogger.writeLog("发送失败");
		}
	}	
	
	

	
	public static void main(String[] args) throws Exception{
	 	String tel = "15675137771";
	 	String code= "118811";
	 	String userType= "咨询师";
		String mobileMsg = "***"+tel.substring(tel.length()-4, tel.length());
		String message1 = mobileMsg+"你好!咨询师回复了你咨询的法律问题,请及时登录查看.";
		String message2 = mobileMsg+"您收到新的法律咨询,请及时登录查看,回复!";
		String message3 = mobileMsg+"您市又有新的案源了,登录账户中心查看吧,回复TD拒收,";
		String message4 = "您正在重置密码,手机验证码是"+code+"请在5分钟内按页面提示提交验证码,为了您的账户安全,请勿透露给他人.";
		String message5 = "尊敬的用户您好,您正在注册为"+userType+"会员,请输入手机验证码"+code+"有效期,5分钟,请勿透露给他人,如非您本人操作,请忽略本短信.";
		String message6 = "您正在重置密码,手机验证码是"+code+"请在5分钟内按页面提示提交验证码,为了您的账户安全,请勿透露给他人.";
		String message7 = "您的验证码为"+code;
     	boolean rs=SMSUtilLiantong.sendCheckCode(tel,message7);
    	ExceptionLogger.writeLog("发送结果："+rs);
	}
}
