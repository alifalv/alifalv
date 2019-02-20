package com.common.utils;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;
import com.common.web.HttpUtils;

/**
 * 微信H5 WAP支付,应用类只需要继承该类，并重写几个虚方法就OK;
 * @author hyq
 * @since 2017年8月4日
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked", "unused" })
public class WxPayUtils {
	private String noticeUrl;//微信支付回调通知的URL
	private String redirectUrl;//客户端支付完成后跳转的URL
	private String domainContext;//域名与上下文字符串,如http://www.baidu.com/app
	
	private String appId;//微信商户帐号中的的APPID; 
	private String mchId;//微信商务号
	private String mkey;//商户帐号中的商户密钥；
	
	private String SIGN_TYPE="MD5";
	
	private String wapUrl;//wap网站的首页地址；
	/**
	 * 统一下单
	 * @param outTradeNo 商户订单	
	 * @param body 商品名
	 * @param totalFee 金额，单位（分）
	 * @param remoteIp 客户端IP;
	 * @return 一个map:
	 *       注意：若return_code返回的是SUCCESS,则设置302响应码，设置location为mweb_url的值，让重定向到该URL;
	 *       自动拉起微信客户商用完成支付操作；
	 */
	public Map<String,String> unifiedOrder(String outTradeNo, 
			String body,int totalFee,String remoteIp) throws Exception{
		String URL = getUrl("unifiedorder");
		String noticeUrl=domainContext+"/api/pay/wx/paynotice";
		
		Map<String,String> data=new HashMap<String,String>(){{
			put("appid",appId);
			put("mch_id",mchId);
			put("body",body);
			put("sign_type",SIGN_TYPE);
			put("nonce_str",WechatUtils.generateRadomString());
			put("notify_url",noticeUrl);
			put("out_trade_no",outTradeNo);
			put("spbill_create_ip",remoteIp);
			put("total_fee",totalFee+"");
			put("scene_info",
					"\"h5_info\": {\"type\":\"Wap\",\"wap_url\":"+wapUrl+",\"wap_name\": \"积分商城\"}");
			put("trade_type","MWEB");
		}};
		
		//生成签名
		String signStr=WechatUtils.generateSignature(data,mkey,SIGN_TYPE);
		data.put("sign", signStr);
		//转成xml
		String dataStr=XmlUtils.map2xmlBody(data,"xml");
		
		ExceptionLogger.writeLog("---微信统一下单请求数据：---:"+dataStr);
		//发送请求
		HttpResponse sp=HttpUtils.post(URL, dataStr,"");		
		//响应
		String responseStr=EntityUtils.toString(sp.getEntity(),"UTF-8");
		ExceptionLogger.writeLog("---微信统一下单响应数据：---:"+responseStr);
		
		Map responseMap=XmlUtils.xmlBody2map(responseStr, "xml");
		ExceptionLogger.writeLog("---微信统一下单响应数据：---:"+responseMap);
		
		return responseMap;
	}
	
	
	
	
	/**
	 * 向微信服务端查询支付订单的支付情况,
	 * 将查到的结果用map返回；	 
	 * @param outTradeNo 商用号订单号
	 */
	private  Map<String,String> orderQuery(String outTradeNo) throws Exception{		
		String url=this.getUrl("orderquery");		
		Map<String,String> data=new HashMap<String,String>(){{
			put("appid",appId);
			put("mch_id",mchId);
			put("nonce_str",WechatUtils.generateRadomString());
			put("out_trade_no",outTradeNo);
			put("sign_type",SIGN_TYPE);
		}};
		//生成签名
		String signStr=WechatUtils.generateSignature(data,mkey,SIGN_TYPE);
		data.put("sign", signStr);
		
		//转成xml
		String dataStr=XmlUtils.map2xmlBody(data,"xml");
		ExceptionLogger.writeLog("------调用微信查询订单的请求数据：-------:"+dataStr);
		
		//发送请求
		HttpResponse sp=HttpUtils.post(url, dataStr,"");		
		//响应
		String responseStr=EntityUtils.toString(sp.getEntity(),"UTF-8");		
		ExceptionLogger.writeLog("------调用微信查询订单的响应数据：-------:"+responseStr);
		
		Map responseMap=XmlUtils.xmlBody2map(responseStr, "xml");
		
		if(responseMap.get("return_code").toString().equals("FAIL"))
			throw new BusinessException(responseMap.get("return_msg").toString(),-1);
		if(responseMap.get("result_code").toString().equals("FAIL"))
			throw new BusinessException(responseMap.get("err_code_des").toString(),-1);
		
		return responseMap;
	}
	
	
	/**
	 * 支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答;
	 * 对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，
	 * 尽可能提高通知的成功率，但微信不保证通知最终能成功。 （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）注意事项：
	 * <b>注意</b>同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知;

	 * @param request
	 * @param response
	 * @return map 结构详见：https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_7&index=7
	 * @throws Exception 如抛出了BussinessException则验证不正确；
	 */
	public Map<String,String> payNoticeCallback(String outTradeNo,int totalFee,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		 
		//解析请求的数据
		BufferedReader reader = null;		 
        reader = request.getReader();
        String line = "";
        String xmlString = null;
        StringBuffer inputString = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            inputString.append(line);
        }
        xmlString = inputString.toString();
        request.getReader().close();
        ExceptionLogger.writeLog("------微信的支付回调数据-------:"+xmlString);
        //转换XML至MAP
        Map data=XmlUtils.xmlBody2map(xmlString, "xml");
        String returnCode="",
        		returnMsg="";
        Map<String,String> resultMap=new HashMap<String,String>();
        
        //交易通讯失败处理
        if(data.get("return_code").toString().equals("FAIL")){ 
        	resultMap.put("return_code",data.get("return_code").toString());
            resultMap.put("return_msg",data.get("return_msg").toString());	
            ExceptionLogger.writeLog("------响应给微信支付回调的数据-------:"+resultMap);     		
     		return null;
        }       
		
		//签名验证
	    	String sign=data.get("sign").toString().trim();
	    	data.remove("sign");
	    	String sign_=WechatUtils.generateSignature(data,mkey,data.get("sign_type")+"");
	    	if(!sign_.equals(sign))
	    		throw new BusinessException("签名不正",-1);
	    	//订单号验证
        if(!outTradeNo.equals(data.get("out_trade_no").toString()))
		   	throw new BusinessException("订单号不正确",-1);      	
        //金额验证
        if(totalFee!=Integer.parseInt(data.get("total_fee").toString()))     
        	throw new BusinessException("订单金额不正确",-1);     
        return data;
	}
	
	/**
	 * 如果是测试环境，则访问测试仿真平台 ,目前访问仿真API，会报签名问题
	 * name API名称
	 * @return 
	 */
	private String getUrl(String name) {		
		String test="";
		/*if(model.equals("DEBUG"))
			test="/sandboxnew";*/
		String URL="https://api.mch.weixin.qq.com"+test+"/pay/"+name;
		return URL;
	}
	
	
	/**
	 * 微信回调时，响应成功的数据
	 * @return
	 */
	public String responseSuccess(){
		Map<String,String> resultMap=new HashMap<String,String>();
		resultMap.put("return_code","SUCCESS");
        resultMap.put("return_msg","OK");
        ExceptionLogger.writeLog("------响应给微信支付回调的数据-------:"+resultMap);
 		return XmlUtils.map2xmlBody(resultMap, "xml");
	}
	
	/**
	 * 微信回调时，响应失败的数据
	 * @return
	 */
	public String responseFail(){
		Map<String,String> resultMap=new HashMap<String,String>();
    		resultMap.put("return_code","FAIL");
        resultMap.put("return_msg","发生错误");
        ExceptionLogger.writeLog("------响应给微信支付回调的数据-------:"+resultMap);
 		return XmlUtils.map2xmlBody(resultMap, "xml");
	}
}
