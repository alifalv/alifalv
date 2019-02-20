package com.legal.app.utils;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.common.web.HttpUtils;

/**
 * 第三方登录工具类
 * @author huangzh
 *
 */
public class ThreeLoginUtil {

	public static final String OPEN_TYPE_WEIXIN = "WECHAT";
	public static final String OPEN_TYPE_WEIBO = "WEIBO";
	public static final String OPEN_TYPE_QQ = "QQ";
	
	//微信相关信息
	private static String appId="wx9ecfe9dff1e2c47c";
	private static String secret="b6c49586515572a46960bdace4c5fa3b";
	
	
	
	private static String appId_qq="101494455";
	private static String secret_qq="ef6ff02bcde2d4bdf23584a40f197c6b";
	
	public static String  getThreeUserOpenId(String code,String openType) throws Exception{
		
		if(openType.equals(OPEN_TYPE_WEIXIN)){
			
			String url="https://api.weixin.qq.com/sns/oauth2/access_token?";
			String pathParams="appid="+appId+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
			HttpResponse response=HttpUtils.get(url+pathParams);
			String reponseStr=EntityUtils.toString(response.getEntity());
			JSONObject responseJson=JSONObject.fromObject(reponseStr);
			
			if(responseJson.getInt("code") == 1){
				return responseJson.getJSONObject("data").getString("openid");
			}
			
		}else if(openType.equals(OPEN_TYPE_QQ)){
			String url="https://graph.qq.com/oauth2.0/token?";
			String pathParams="client_id="+appId_qq+"&client_secret="+secret_qq+"&code="+code+"&grant_type=authorization_code";
			HttpResponse response=HttpUtils.get(url+pathParams);
			String reponseStr=EntityUtils.toString(response.getEntity());
			JSONObject responseJson=JSONObject.fromObject(reponseStr);
			
			if(responseJson.getInt("code") == 1){
				return responseJson.getJSONObject("data").getString("openid");
			}
		}
		
		return null;
		
	}
	
}
