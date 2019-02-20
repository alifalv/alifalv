package com.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.common.log.ExceptionLogger;

import net.sf.json.JSONObject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SMSUtilHuawei {

	private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";

	private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

	// 请从应用管理页面获取APP接入地址，替换url中的ip地址和端口
	private  static String url = "https://117.78.29.66:10443/sms/batchSendSms/v1";
	// 请从应用管理页面获取APP_Key和APP_Secret进行替换
	private static String appKey = "7Uw1yc2RNwJ91axTOr7GA9NfQ7bc";
	private static String appSecret = "FaF6uPzbvb33Z48pq3b2uv7i25w5";

	// 填写短信签名中的通道号，请从签名管理页面获取
	private static String sender_code = "csms18071304";//验证码类签名；
	private static String sender_notify ="csms18073006";//通知类签名； 
	// 状态报告接收地址，为空或者不填表示不接收状态报告
	private static String statusCallBack = "";
	

	/**
	 * 验证码短信发送
	 * @param tel 手机号字符串，多个号码间用英文逗号隔开，最多1000个号码； 
	 * @param templateId 模板id
	 * @param templateParas  模板参数的值字符串； 如"[\"3\",\"人民公园正门\"]";
	 * @return 成功返回true,失败返回false;
	 * @throws Exception
	 */
	public static boolean sendCheckCode(String tel, String templateId, String templateParas) throws Exception {
		ExceptionLogger.writeLog(">>>" + templateParas);
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		// 模板变量请务必根据实际情况修改，查看更多模板变量规则
		// 如模板内容为“您有${NUM_2}件快递请到${TXT_32}领取”时，templateParas可填写为[\"3\",\"人民公园正门\"]
		// 双变量示例：String templateParas = "[\"3\",\"人民公园正门\"]";
		// String templateParas = "[\"369751\"]";

		String body = buildRequestBody(sender_code, tel, templateId, templateParas, statusCallBack);
		ExceptionLogger.writeLog("华为短信请求包： " + body);

		String wsseHeader = buildWsseHeader(appKey, appSecret);
		ExceptionLogger.writeLog("wsse header is " + wsseHeader);

		// 如果JDK版本低于1.8，可使用如下代码
		// CloseableHttpClient client = HttpClients.custom()
		// .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, new
		// TrustStrategy() {
		// @Override
		// public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws
		// CertificateException {
		// return true;
		// }
		// }).build()).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

		// 如果JDK版本是1.8，可使用如下代码
		CloseableHttpClient client = HttpClients.custom()
				.setSSLContext(
						new SSLContextBuilder().loadTrustMaterial(null, 
								(x509CertChain, authType) -> true).build())
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

		HttpResponse response = client.execute(RequestBuilder.create("POST").setUri(url)
				.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
				.addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE).addHeader("X-WSSE", wsseHeader)
				.setEntity(new StringEntity(body)).build());

		String result=EntityUtils.toString(response.getEntity());
		ExceptionLogger.writeLog("华为短信发送响应报文：" + response.toString());
		ExceptionLogger.writeLog("华为短信发送响应内容：" + result);
		return JSONObject.fromObject(result).getJSONArray("result").getJSONObject(0).getString("status").equals("000000");

	}
	
	/**
	 * 通知类短信发送
	 * @param tel 手机号字符串，多个号码间用英文逗号隔开，最多1000个号码； 
	 * @param templateId 模板id
	 * @param templateParas  模板参数的值字符串； 如"[\"3\",\"人民公园正门\"]";
	 * @return 成功返回true,失败返回false;
	 * @throws Exception
	 */
	public static boolean sendNotify(String tel, String templateId, String templateParas) throws Exception {
		ExceptionLogger.writeLog(">>>" + templateParas);
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		// 模板变量请务必根据实际情况修改，查看更多模板变量规则
		// 如模板内容为“您有${NUM_2}件快递请到${TXT_32}领取”时，templateParas可填写为[\"3\",\"人民公园正门\"]
		// 双变量示例：String templateParas = "[\"3\",\"人民公园正门\"]";
		// String templateParas = "[\"369751\"]";

		String body = buildRequestBody(sender_notify, tel, templateId, templateParas, statusCallBack);
		ExceptionLogger.writeLog("华为短信请求包： " + body);

		String wsseHeader = buildWsseHeader(appKey, appSecret);
		ExceptionLogger.writeLog("wsse header is " + wsseHeader);

		// 如果JDK版本低于1.8，可使用如下代码
		// CloseableHttpClient client = HttpClients.custom()
		// .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, new
		// TrustStrategy() {
		// @Override
		// public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws
		// CertificateException {
		// return true;
		// }
		// }).build()).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

		// 如果JDK版本是1.8，可使用如下代码
		CloseableHttpClient client = HttpClients.custom()
				.setSSLContext(
						new SSLContextBuilder().loadTrustMaterial(null, 
								(x509CertChain, authType) -> true).build())
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

		HttpResponse response = client.execute(RequestBuilder.create("POST").setUri(url)
				.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
				.addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE).addHeader("X-WSSE", wsseHeader)
				.setEntity(new StringEntity(body)).build());

		String result=EntityUtils.toString(response.getEntity());
		ExceptionLogger.writeLog("华为短信发送响应报文：" + response.toString());
		ExceptionLogger.writeLog("华为短信发送响应内容：" + result);
		return JSONObject.fromObject(result).getJSONArray("result").getJSONObject(0).getString("status").equals("000000");

	}

	private static String buildRequestBody(String sender, String receiver, String templateId, String templateParas,
			String statusCallbackUrl) {

		List<NameValuePair> keyValues = new ArrayList<>();

		keyValues.add(new BasicNameValuePair("from", sender));
		keyValues.add(new BasicNameValuePair("to", receiver));
		keyValues.add(new BasicNameValuePair("templateId", templateId));
		keyValues.add(new BasicNameValuePair("templateParas", templateParas));
		keyValues.add(new BasicNameValuePair("statusCallback", statusCallbackUrl));

		return URLEncodedUtils.format(keyValues, StandardCharsets.UTF_8);
	}

	private static String buildWsseHeader(String appKey, String appSecret) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'");
		String time = sdf.format(new Date());
		String nonce = UUID.randomUUID().toString().replace("-", "");

		byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
		String hexDigest = Hex.encodeHexString(passwordDigest);
		String passwordDigestBase64Str = Base64.encodeBase64String(hexDigest.getBytes(Charset.forName("utf-8")));
		return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
	}
	
	
	public static void main(String[] args) throws Exception{
		// 请从模板管理页面获取模板ID进行替换
		String templateId = "13142159066";

	//	boolean rs=SMSUtilHuawei.send("13142159066", templateId, "[\"1234\"]");

	//	boolean rs=SMSUtilHuawei.sendCheckCode("13378017839,17375804185", templateId, "[\"1234\"]");
		
		String mobileMsg = "***"+templateId.substring(templateId.length()-4, templateId.length());


		System.out.println("发送结果："+mobileMsg);
	}
}
