package com.legal.app.controller;

import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;
import com.common.utils.WechatUtils;
import com.common.utils.XmlUtils;
import com.common.web.HttpUtils;
import com.common.web.WebUtils;
import com.legal.app.service.PayOrderService;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信支付后台相关接口，包括微信回调，APP请求的各个api;
 * 此控制器的API接口，回调函数不要设置token限制；
 * </br>时间： 2017-06-14
 * @author hyq
 */
@Controller
@RequestMapping(path="/pay/wx")
public class WeixinPayController  extends SuperController {
	//@Value("#{configProperties['wx-appId']}")
	//private String appId="wx50120b6fecb7aa43";
	//@Value("#{configProperties['mchId']}")
	//private String mchId="1287364701";//商户ID
	//@Value("#{configProperties['mkey']}")
	//private String mkey="j7GsPSIwdt5IAQbos4AtPaclxCKIEld3";//商户的密钥；
	
	/*private String appId="wx3f95f6b73e9907b5";
	private String mchId="1501134831";
	private String mkey ="sdfsdfsdfsdfsrewrerwerwerwdfsdfs";*/
	
	
	//private String appId="wwbd84ed347e20b937";
	//private String appId="wx50120b6fecb7aa43";
	private String appId="wx8bcb38d335196bdb";
	//private String mchId="1287364701";
	private String mchId="1510373021";
	private String mkey ="YangShuWen1390731200107397060522";
	
	
	//@Value("#{configProperties['domainContext']}")
	//private String domainContext="http://simplyto.com/ali-legal";//域名及上下文
	private String domainContext="http://www.alifalv.cn/ali-legal";//域名及上下文
	//private String domainContext="http://vcw5ys.natappfree.cc/ali-legal";//域名及上下文
	@Value("#{configProperties['model']}")
	private String model;
	@Value("#{configProperties['wapUrl']}")
	private String wapUrl;//wap网站的首页地址；
	private String redirectUrl="/pay_complete.html";
	//private String h5Callback="/pay/wx/paynotice";//h5支付回调uri ,写在下面的方法中去了。
	private String certPath="/conf/apiclient_cert.p12";
	@Autowired
	private PayOrderService payOrderService; 
	private final static String WEIXIN="微信"; //支付方式
	private final static String REFUND_REMARK="兑换退款";//统一备注
	
	private final static  String SIGN_TYPE="MD5";
	
	public static String getIPAddress(HttpServletRequest request) {
	    String ip = null;

	    //X-Forwarded-For：Squid 服务代理
	    String ipAddresses = request.getHeader("X-Forwarded-For");
	if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
	        //Proxy-Client-IP：apache 服务代理
	        ipAddresses = request.getHeader("Proxy-Client-IP");
	    }
	if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
	        //WL-Proxy-Client-IP：weblogic 服务代理
	        ipAddresses = request.getHeader("WL-Proxy-Client-IP");
	    }
	if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
	        //HTTP_CLIENT_IP：有些代理服务器
	        ipAddresses = request.getHeader("HTTP_CLIENT_IP");
	    }
	if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
	        //X-Real-IP：nginx服务代理
	        ipAddresses = request.getHeader("X-Real-IP");
	    }

	    //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
	    if (ipAddresses != null && ipAddresses.length() != 0) {
	        ip = ipAddresses.split(",")[0];
	    }

	    //还是不能获取到，最后再通过request.getRemoteAddr();获取
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
	        ip = request.getRemoteAddr();
	    }
	    return ip;
	}
	 
	/**
	 * 统一下单api (h5支付）
	 * {
	 * 	  body:'商品描述'，
	 *    totalFee:4501 支付金额:单位为分，整型数
	 *    outTradeNo:商户订单号
	 * }
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="/unifiedOrder/{outTradeNo}/{body}/{totalFee}")
	public void unifiedOrder(@PathVariable("outTradeNo")String outTradeNo, 
			@PathVariable("body")String body,
			@PathVariable("totalFee")int totalFee,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
			
 
		String ip3 = this.getIPAddress(request);
		System.out.println(ip3);
		String ip=request.getRemoteAddr(); 
		System.out.println(ip);
		String URL = getUrl("unifiedorder");
		System.out.println(URL);
		//String noticeUrl=domainContext+h5Callback;
		String noticeUrl=domainContext+"/api/pay/wx/paynotice";
		
		Map<String,String> data=new HashMap<String,String>(){{
			put("appid",appId);
			put("mch_id",mchId);
			put("body",body);
			put("sign_type",SIGN_TYPE);
			put("nonce_str",WechatUtils.generateRadomString());
			put("notify_url",noticeUrl);
			put("out_trade_no",outTradeNo);
			put("spbill_create_ip",ip3);
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
		
		ExceptionLogger.writeLog("------微信统一下单请求数据：-------:"+dataStr);
		//发送请求
		HttpResponse sp=HttpUtils.post(URL, dataStr,"");		
		//响应
		String responseStr=EntityUtils.toString(sp.getEntity(),"UTF-8");
		ExceptionLogger.writeLog("------微信统一下单响应数据：-------:"+responseStr);
		
		Map responseMap=XmlUtils.xmlBody2map(responseStr, "xml");
		ExceptionLogger.writeLog("------微信统一下单响应数据：-------:"+responseMap);
		PrintWriter out=response.getWriter();
		if (!responseMap.get("return_code").equals("SUCCESS")){			
			out.write(WebUtils.responseError(responseMap.get("return_msg").toString(), -1));
			out.flush();
			out.close();			
		}else{
			//TODO:新增支付订单
			//payOrderService.insertPayOrder(WEIXIN,appId,outTradeNo, totalFee,"业务值","业务值");
			ExceptionLogger.writeLog("微信的统一下单跳转："+responseMap.get("mweb_url").toString());
			String redUrl="&redirect_url="+URLEncoder.encode(this.domainContext+redirectUrl)+"?oid="+outTradeNo;
			//response.setStatus(302);
			//response.addHeader("location", responseMap.get("mweb_url").toString()+redUrl);
			//out.print(WebUtils.responseData(responseMap.get("mweb_url").toString()));
			out.print(WebUtils.responseData("下单成功",1,responseMap.get("mweb_url").toString()));
			out.flush();
			out.close();			
		}
	}
	
	/**
	 * 统一下单api（二维码支付）
	 * {
	 * 	  body:'商品描述'，
	 *    totalFee:4501 支付金额:单位为分，整型数
	 *    outTradeNo:商户订单号
	 * }
	 * @return {
	 * 		code:为正数,读data,负数，读error错误
	 * 		data:'微信支付的url，据此js生成二维码'
	 * 		error:"错误文本说明",
	 * }
	 * @throws Exception
	 */
	@RequestMapping(path="/unifiedOrder_qcode/{outTradeNo}/{body}/{totalFee}")	
	@ResponseBody
	public String unifiedOrder_qcode(@PathVariable("outTradeNo")String outTradeNo,
			@PathVariable("body")String body,
			@PathVariable("totalFee")int totalFee,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
				
		String ip=request.getRemoteAddr();
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
			put("spbill_create_ip",ip);
			put("total_fee",totalFee+"");
			put("scene_info",
					"\"h5_info\": {\"type\":\"Wap\",\"wap_url\":"+wapUrl+",\"wap_name\": \"积分商城\"}");
			//put("trade_type","MWEB");
			put("trade_type","NATIVE");
		}};
		
		//生成签名
		String signStr=WechatUtils.generateSignature(data,mkey,SIGN_TYPE);
		ExceptionLogger.writeLog("------签名字符串：-------:"+signStr);

		data.put("sign", signStr);
		//转成xml
		String dataStr=XmlUtils.map2xmlBody(data,"xml");
		
		ExceptionLogger.writeLog("------微信统一下单请求数据：-------:"+dataStr);
		//发送请求
		HttpResponse sp=HttpUtils.post(URL, dataStr,"");		
		//响应
		String responseStr=EntityUtils.toString(sp.getEntity(),"UTF-8");
		ExceptionLogger.writeLog("------微信统一下单响应数据：-------:"+responseStr);
				Map responseMap=XmlUtils.xmlBody2map(responseStr, "xml");
		ExceptionLogger.writeLog("------微信统一下单响应数据：-------:"+responseMap);
		return WebUtils.responseData(responseMap);
		/**
		if (!responseMap.get("return_code").equals("SUCCESS")){			
			return WebUtils.responseError(responseMap.get("return_msg").toString(),-100);
		}else{
			//TODO:新增支付订单
			//payOrderService.insertPayOrder(WEIXIN,appId,outTradeNo, totalFee,"业务值","业务值");
			
			return 	WebUtils.responseData(responseMap.get("code_url").toString());
		}**/
	}
	
	/**
	 * 如果是测试环境，则访问测试仿真平台 
	 * name API名称
	 * @return
	 */
	
	private String getUrl(String name) {		
		String test="";
		//if(model.equals("DEBUG"))
		//	test="/sandboxnew";
		String URL="https://api.mch.weixin.qq.com"+test+"/pay/"+name;
		return URL;
	}
	
	private String getTransferUrl(String name) {		
		String test="";
		//if(model.equals("DEBUG"))
		//	test="/sandboxnew";
		String URL="https://api.mch.weixin.qq.com"+test+"/mmpaymkttransfers/promotion/"+name;
		return URL;
	}
	
	/**
	 * 向微信服务端查询支付订单的支付情况,
	 * 将查到的结果用map返回；	 
	 * @param outTradeNo 商用号订单号
	 */
	private  Map<String,String>  _queryOrder(String outTradeNo) throws Exception{		
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
		
		String payResult=responseMap.get("trade_state").toString();
		String orderTimeComplete=responseMap.get("time_end").toString();
		String payTransationId=responseMap.get("transaction_id").toString();
		String openId=responseMap.get("openid").toString();
		
		Map result=new HashMap<String,String>(){{
			put("trade_state",payResult);
			put("time_end",orderTimeComplete);
			put("transaction_id",payTransationId);
			put("openid",openId);
		}};
		
		return 	result;
	}
	
	/**
	 * 查询支付结果
	 * @param outTradeNo 商户支付订单号
	 * @return{
	 *  data:"支付结果"
	 *  }
	 *  结果可能值为：
		  	SUCCESS—支付成功
			REFUND—转入退款
			NOTPAY—未支付
			CLOSED—已关闭
			REVOKED—已撤销（刷卡支付）
			USERPAYING--用户支付中
		    PAYERROR-支付失败
	 * 
	 * @throws Exception
	 */
	@RequestMapping(path="/queryOrder/{outTradeNo}")
	@ResponseBody
	public String queryPayOrder(@PathVariable("outTradeNo") String outTradeNo) throws Exception{
		
		if(outTradeNo==null||outTradeNo.trim().equals(""))
			return WebUtils.responseError("商户支付订单号不正确!", -1);
		Map<String,Object> order=payOrderService.getPayOrder(outTradeNo);	
		if(order==null) {
			return WebUtils.responseError("找不到该笔支付订单!", -1);		
		}
		//如果该订单已经保存了交易成功或失败标志，则直接返回成功或失败标志，否则调用微信的查询订单API，并保存订单结果到数据库;
		Object isPay = (Object)order.get("isPay");
		if(isPay !=null){
			if(isPay.equals(3) || isPay.equals(2)) {
				return  WebUtils.responseError("还没有获取到银行的支付状态，请稍等", -1);
			}else if(isPay.equals(1)) {
				return  WebUtils.responseMsg("支付成功.", 1);
			}else if(isPay.equals(4)) {
				return  WebUtils.responseMsg("支付失败.", -1);
			}else if(isPay.equals(5)) {
				return  WebUtils.responseMsg("已退款.", -1);
			}else {
				return  WebUtils.responseMsg("订单还没有支付.", -1);
			}
			 
		}else {
			return  WebUtils.responseError("查询失败", -1);
		}
		/**
		else{ 
			Map<String,String> result=this._queryOrder(outTradeNo);			
			//将支付订单的状态数据更新到数据库
			payOrderService.upatePayOrder(outTradeNo,
					result.get("orderTimeComplete"),
					result.get("payResult"),
					result.get("payTransationId"),
					result.get("openId"));	
			
			return WebUtils.responseData(this._queryOrder(outTradeNo)); 
		}
		**/
	}
	

	/**
	 * 微信支付结果通知回调方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="/paynotice")
	@ResponseBody
	public String payNoticeCallback(HttpServletRequest request, HttpServletResponse response) throws Exception{
		 
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
        ExceptionLogger.writeLog("------微信的支付回调数据 转为Map-------:"+data);
        String returnCode="",
        		returnMsg="";
        Map<String,String> resultMap=new HashMap<String,String>();
        
        //交易通讯失败处理
        if(data.get("return_code").toString().equals("FAIL")){
        		returnCode="FAIL";
        		returnMsg="ERROR";    
        		resultMap.put("return_code",returnCode);
            resultMap.put("return_msg",returnMsg);	
     		return XmlUtils.map2xmlBody(resultMap, "xml");
        }       
		
		//签名验证
	    	String sign=data.get("sign").toString().trim();
	    	data.remove("sign");
	    	String sign_=WechatUtils.generateSignature(data,mkey,data.get("sign_type")+"");
	    	if(!sign_.equals(sign)){
	    		returnCode="FAIL";
	        	returnMsg="签名不正确";
	        	resultMap.put("return_code",returnCode);
	            resultMap.put("return_msg",returnMsg);	
	            ExceptionLogger.writeLog("------响应给微信支付回调的数据-------:"+resultMap);
	     		return XmlUtils.map2xmlBody(resultMap, "xml");
	    	}
        String outTradeNo=data.get("out_trade_no").toString();
		
        //根据订单号，取得订单对象
        Map<String,Object> order=payOrderService.getPayOrder(outTradeNo);
        //订单号验证
        if(order==null){
        	returnCode="FAIL";
        	returnMsg="订单号不正确";
        	resultMap.put("return_code",returnCode);
            resultMap.put("return_msg",returnMsg);	
            ExceptionLogger.writeLog("------响应给微信支付回调的数据-------:"+resultMap);
     		return XmlUtils.map2xmlBody(resultMap, "xml");
        }
        //如果订单已经处理过了，则直接返回
        if(order.get("isPay") !=null && !(order.get("isPay").toString().equals("3") || order.get("isPay").toString().equals("2") )){
        		returnCode="SUCCESS";
        		returnMsg="已经处理过了！";
        		resultMap.put("return_code",returnCode);
            resultMap.put("return_msg",returnMsg);
            ExceptionLogger.writeLog("------响应给微信支付回调的数据-------:"+resultMap);
     		return XmlUtils.map2xmlBody(resultMap, "xml");
        }        	
        	
        //金额验证
        Double values =  Double.valueOf(order.get("totalPrice").toString()) * 100;
        int total=Integer.parseInt(new java.text.DecimalFormat("0").format(values));
        int totalFee=Integer.parseInt(data.get("total_fee").toString()); //因为微信是以分为单位，要 除100 ，在生成订单的时间，已经乘了100 
        if(total!=totalFee){
        	returnCode="FAIL";
        	returnMsg="订单金额不正确";
        	resultMap.put("return_code",returnCode);
            resultMap.put("return_msg",returnMsg);	
            ExceptionLogger.writeLog("------响应给微信支付回调的数据-------:"+resultMap);
     		return XmlUtils.map2xmlBody(resultMap, "xml");
        }
        /*	异步通知的数据结构
         *  {    
         * 		transaction_id=4004372001201707303530176398,         
        		nonce_str=20170730083214843dsfadsadsf,
        		bank_type=CFT, 
        		openid=oRA3P1R4slsJ7N6b3fL2yYG6bV18, 
        		sign=1571460800033EADF26F38C0F6827546, 
        		fee_type=CNY,
        		mch_id=1484405722,
        		cash_fee=1, 
        		out_trade_no=20170730083214843, 
        		appid=wx4e04927d7b6b632d, 
        		total_fee=1, 
        		trade_type=MWEB, 
        		result_code=SUCCESS,
        		time_end=20170730203224, 
        		is_subscribe=Y, 
        		return_code=SUCCESS
        	}
         */
        //读取支付结果
  		/*
  		 * 	trade_state 支付状态的几种值
  			SUCCESS—支付成功
  			REFUND—转入退款
  			NOTPAY—未支付
  			CLOSED—已关闭
  			REVOKED—已撤销（刷卡支付）
  			USERPAYING--用户支付中
  			PAYERROR--支付失败(其他原因，如银行返回失败)
  		 */
		String payResult=data.get("result_code").toString();//支付结果
		String orderTimeComplete=data.get("time_end").toString();//支付完成时间
		String payTransationId=data.get("transaction_id").toString();//支付微信的订单号
		String openId=data.get("openid").toString();//用户在此商户号的下的唯一ID
		//TODO:将支付订单的状态数据更新到数据库
		int rs= 2;
		HttpSession  hs = request.getSession();
		if(payResult.equals("SUCCESS")) {
				hs.setAttribute("requestPayOrderStatus", "Y");
			System.out.println(hs.getAttribute("requestPayOrderStatus").toString());
			    //成功；
				payOrderService.upatePayOrder(outTradeNo,orderTimeComplete,"0",payTransationId,openId);
				rs = 1;
		}else if(!payResult.equals("USERPAYING")) {
		      	//支付失败；
				hs.setAttribute("requestPayOrderStatus", "N");
			    payOrderService.upatePayOrder(outTradeNo,orderTimeComplete,"1",payTransationId,openId);
		}
		if(rs!=1){
			returnCode="FAIL";
        		returnMsg="数据处理不成功！";
        		resultMap.put("return_code",returnCode);		
            resultMap.put("return_msg",returnMsg);
            ExceptionLogger.writeLog("------响应给微信支付回调的数据-------:"+resultMap);
     		return XmlUtils.map2xmlBody(resultMap, "xml");
        }  
		
	    	returnCode="SUCCESS";
	    	returnMsg="OK";
	    	resultMap.put("return_code",returnCode);
        resultMap.put("return_msg",returnMsg);
        ExceptionLogger.writeLog("------响应给微信支付回调的数据-------:"+resultMap);
 		return XmlUtils.map2xmlBody(resultMap, "xml");
	}
	
	public static void main(String[] args) {
		String x = "2.56";
		int d = Integer.parseInt(new java.text.DecimalFormat("0").format(Double.valueOf(x)));
		System.out.println(d +4);
	}
	
	/**
	 * 给指定的微信用户，从商户帐号中付款；
	 * @param waitPayList
	 * @return
	 */
	@RequestMapping(path="/refund")
	@ResponseBody
	public String refund(HttpServletRequest request) throws Exception{
		List<Map<String,String>> list=payOrderService.queryRefundList();
		int errorCount=0;
		
		for(Map<String,String> map : list){
			String openId=map.get("openId");
			String amount=map.get("amount");	
			String userName=map.get("userName");
			String ip=request.getRemoteAddr();
			//执行微信退款指令
			String rs=this._refund(request,openId,userName,Integer.parseInt(amount),ip);
			String[] params=rs.split(",");
			if(!params[0].equals("1")) {
				errorCount++;
			}
			//修改数据状态
			payOrderService.updateRefundStatus(openId,params[0].equals("1"),
					params[1],params[2],"微信");			
		}
	
		if(errorCount==0)
			return WebUtils.responseCode(list.size());
		else
			return WebUtils.responseError("有"+errorCount+"笔款未成功退回",-1);
	}
	
	/**
	 * 调用微信的商户转帐到微信(单笔转账到支付宝账户接口)
	 * @param payeeAccount 收款帐号
	 * @param amount 退款金额（单位为分）
	 * @param remark 备注
	 * @return 返回成功与否(1|0)，商户订单号，支付宝订单号拼成的字符串,如"1,34343434324354677,2346534243543543";
	 * @throws Exception
	 */
	private String _refund(HttpServletRequest request,String openId,String userRealName,int amount,String ip) throws Exception{
		String url=this.getTransferUrl("transfers");		
		String partnerTradeNo=this.generateTradeNo();
		Map<String,String> data=new HashMap<String,String>(){{
			put("mch_appid",appId);
			put("mchid",mchId);
			put("nonce_str",WechatUtils.generateRadomString());
			put("partner_trade_no",partnerTradeNo);
			put("openid",openId);			
			put("check_name","FORCE_CHECK");
			put("re_user_name",userRealName);
			put("amount",amount+"");
			put("desc",REFUND_REMARK);
			put("spbill_create_ip",ip);		
			
		}};
		//生成签名
		String signStr=WechatUtils.generateSignature(data,mkey,this.SIGN_TYPE);
		data.put("sign", signStr);
		
		//转成xml
		String dataStr=XmlUtils.map2xmlBody(data,"xml");
		ExceptionLogger.writeLog("---微信商户付款请求---"+dataStr);
		
		String filePath=this.getClass().getClassLoader().getResource(this.certPath).getFile();
		//发送请求
		HttpResponse sp=HttpUtils.post(url, dataStr,mchId,filePath,"application/XML");		
		//响应
		String responseStr=EntityUtils.toString(sp.getEntity());	
		ExceptionLogger.writeLog("---微信商户付款响应---"+responseStr);
		Map responseMap=XmlUtils.xmlBody2map(responseStr, "xml");
		
		if (!responseMap.get("return_code").equals("SUCCESS")){
			throw new BusinessException(responseMap.get("return_msg").toString(), -1);
		}
		String resultCode=responseMap.get("result_code").toString();
		if (!resultCode.equals("SUCCESS"))
			throw new BusinessException(responseMap.get("err_code_des").toString(),-1);
		
		String paymentNo=responseMap.get("payment_no").toString();
		String paymentTime=responseMap.get("payment_time").toString();
		
		//新增支付订单
		payOrderService.insertRefundOrder(appId, partnerTradeNo, amount, paymentNo, paymentTime, resultCode);
		return (resultCode.equals("SUCCESS")?1:0)+","+partnerTradeNo+","+paymentNo;
	}

	/**
	 * 生成商户商用的订单
	 * @return
	 */
	private  String generateTradeNo(){
		synchronized(this){
			Date now=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmssSSS");
			return sdf.format(now); 
		}
	}
}
