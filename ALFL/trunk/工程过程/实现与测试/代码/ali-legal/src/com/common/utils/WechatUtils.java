package com.common.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.DocumentException;

/**
 * 微信接入相关功能实用类
 * </br>
 * 创建时间：2016年2月2日
 * 
 * @author zhouqian
 */
public class WechatUtils {
   
    /**
     * 生成签名
     * @param mkey 商户密钥
     * @param data 数据map
     * @param names 数据MAP的key的数组;
     * @param signType 签名类型 MD5,HMAC-SHA256
     * @return
     */
    public static String generateSignature(Map<String,String> data,String mkey,String signType){
    	String[] names=data.keySet().toArray(new String[]{});
    	 Arrays.sort(names);
         final StringBuffer sb = new StringBuffer();
         for (final String name : names) {
        	 String v=data.get(name);
             sb.append("&"+name+"="+v);
         }
         final String plainText = sb.toString().substring(1);
         if(signType.equals("HMAC-SHA256"))
        	 return DigestUtils.sha256Hex(plainText+"&key="+mkey);
         else
        	 return DigestUtils.md5Hex(plainText+"&key="+mkey).toUpperCase();
    }
    
    /**
     * 生成随机字符串
     * @return
     */
    public static String generateRadomString(){
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmssSSS");
    	
    	return sdf.format(new Date())+"dsfadsadsf";
    }
    
    @SuppressWarnings("rawtypes")
	public static void main(String[] args){ 
    	String responseStr="<xml>	<appid>gh_c9929f34e818</appid>	<body>1202</body>	<mch_id>1484405722</mch_id>	<nonce_str>20170727024251136dsfadsadsf</nonce_str>	<notify_url>http://localhost/pay/wx/paynotice</notify_url>	<out_trade_no>20170727024251097</out_trade_no>	<scene_info>'h5_info': {'type':'Wap','wap_url':http://www.catuncel.com/zuimao/dist/mall/index.html,'wap_name': '积分商城'}</scene_info><sign_type>MD5</sign_type>	<spbill_create_ip>0:0:0:0:0:0:0:1</spbill_create_ip>	<total_fee>100</total_fee>	<trade_type>MWEB</trade_type>	<sign>FA49C15A7C1AAAD275313C992CA4575A</sign></xml>";
    	try {
			Map map=XmlUtils.xmlBody2map(responseStr, "xml");
			System.out.println(map);
		} catch (DocumentException e) {
		}
    	    	    	
    }
    
}
