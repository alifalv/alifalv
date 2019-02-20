package com.legal.app.controller;

import com.common.web.HttpUtils;
import com.common.web.WebUtils;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信登陆相关接口，包括微信回调，APP请求的各个api;
 * 此控制器的API接口，回调函数不要设置token限制；
 * </br>时间： 2017-06-14
 * @author hyq
 */
@Controller
@RequestMapping(path="/wx")
public class WeixinController  extends SuperController /*implements Pay*/ {
	private String appId="wx9ecfe9dff1e2c47c";
	private String secret="b6c49586515572a46960bdace4c5fa3b";
	private String context="alifalv.cn/alilaw/";
	
	/**
	 * 通过code访问微信api,以获得当前登陆微信用户的openID;
	 * {
	 * 	  code:"微信api所需要的code字段的值",
		  state:"业务系统要带给微信并希望传回来的数据"
	 * }
	 * @return
	 * {
	 * 		code:1
	 * 		data:{ 
				"access_token":"ACCESS_TOKEN", //微信api访问token;
				"openid":"OPENID", //微信的openid,
			}
		}
	 * @throws Exception
	 */
	@RequestMapping(path="/logincallback")	
	public String loginCallback(String code,String state) throws Exception{
		String url="https://api.weixin.qq.com/sns/oauth2/access_token?";
		String pathParams="appid="+appId+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
		HttpResponse response=HttpUtils.get(url+pathParams);
		String reponseStr=EntityUtils.toString(response.getEntity());
		JSONObject responseJson=JSONObject.fromObject(reponseStr);
		return "redirect:http://"+context+"/weChatLogin.html?key="+responseJson.getString("openid")+"&type=WECHAT";
		//return "redirect:http://"+context+"/views/weChatLogin.html?key="+responseJson.getString("openid")+"&type=WECHAT";
	}
	
	

}
