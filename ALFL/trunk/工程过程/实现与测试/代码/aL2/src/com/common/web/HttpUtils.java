package com.common.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;

import net.sf.json.JSONObject;

public class HttpUtils {

	public static final String DEFAULT_CHARSET = "UTF-8";

	private static final String URL_FORMAT = "%s?%s";
	private static final String URL_MATRIX_FORMAT = "%s?%s#%s";

	/**
	 * 文件上传
	 * 
	 * @param action
	 * @param name
	 * @param fileFullName
	 * @return
	 */
	public static String uploadFile(String action, String name,
			String fileFullName) {
		try {
			HttpClient tc = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(action);
			MultipartEntityBuilder me = MultipartEntityBuilder.create();
			me.addBinaryBody(name, new File(fileFullName));
			post.setEntity(me.build());
			HttpResponse response = tc.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			post.abort();
			return result;
		} catch (ClientProtocolException e) {
			ExceptionLogger.writeLog(e, HttpUtils.class);
		} catch (IOException e) {
			ExceptionLogger.writeLog(e, HttpUtils.class);
		}
		return null;
	}

	/**
	 * 从指定 URL 获取数据
	 * 
	 * @param url
	 *            资源链接
	 * @return JSON格式数据
	 * @throws IOException
	 */
	public static JSONObject getJSON(final String url, final Map parameters)
			throws IOException {
		HttpResponse resp = get(url, parameters);
		String resBody = EntityUtils
				.toString(resp.getEntity(), DEFAULT_CHARSET);
		JSONObject resObj = JSONObject.fromObject(resBody);
		return resObj;
	}

	public static HttpResponse get(String action, Map parameters) {
		return get(action, parameters, null);
	}

	public static HttpResponse get(String action) {
		return get(action, null, null);
	}

	/**
	 * get提交
	 * 
	 * @param action
	 *            请求的URL
	 * @param parameters
	 *            是个Map<String,String>键值对集合
	 * @param headers
	 *            是个Map<String,String>键值对集合
	 * @return
	 */
	public static HttpResponse get(String action, Map parameters, Map headers) {
		HttpClient tc = HttpClientBuilder.create().build();
		if (parameters != null && parameters.size() > 0) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			action += "?";
			for (Object entry : parameters.entrySet()) {
				Map.Entry<String, String> tmp = (Map.Entry<String, String>) entry;
				action += tmp.getKey() + "=" + tmp.getValue() + "&";
			}
		}
		HttpGet get = new HttpGet(action);
		if (headers != null && headers.size() > 0) {
			for (Object entry : headers.entrySet()) {
				Map.Entry<String, String> tmp = (Map.Entry<String, String>) entry;
				get.setHeader(tmp.getKey(), tmp.getValue());
			}
		}

		try {
			return tc.execute(get);
		} catch (ClientProtocolException e1) {
			ExceptionLogger.writeLog(e1, HttpUtils.class);
		} catch (IOException e2) {
			ExceptionLogger.writeLog(e2, HttpUtils.class);
		}
		return null;
	}

	/**
	 * 
	 * @param action
	 *            请求url
	 * @param body
	 *            请求体内容:可以是一个json字符串,也可以是个Map<String,String>键值对集合
	 * @param headers
	 *            可以是个Map<String,String>键值对集合
	 * @param format 数据格式 applicaton/json application/XML
	 * @return 返回一个HttpResponse的响应对象
	 */
	public static HttpResponse post(String action, Object body, Map headers,String format) {
		try {
			HttpClient tc = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(action);
			if (headers != null && headers.size() > 0) {
				for (Object entry : headers.entrySet()) {
					Map.Entry<String, String> tmp = (Map.Entry<String, String>) entry;
					post.setHeader(tmp.getKey(), tmp.getValue());
				}
			}

			if (body != null) {
				if (body instanceof Map) {
					Map parameters = (Map) body;
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					for (Object entry : parameters.entrySet()) {
						Map.Entry<String, String> tmp = (Map.Entry<String, String>) entry;
						params.add(new BasicNameValuePair(tmp.getKey(), tmp
								.getValue()));
					}
					post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				} else if (body instanceof String) {
					post.setEntity(new StringEntity(body.toString(),
							format, "UTF-8"));
				} else {

				}
			}
			return tc.execute(post);
		} catch (UnsupportedEncodingException e) {

		} catch (ClientProtocolException e1) {
			ExceptionLogger.writeLog(e1, HttpUtils.class);
		} catch (IOException e2) {
			ExceptionLogger.writeLog(e2, HttpUtils.class);
		}
		return null;
	}

	/**
	 * POST提交一键值对数据;
	 * 
	 * @param action
	 *            请求url
	 * @param body
	 *            请求体内容:可以是一个json字符串,也可以是个Map<String,String>键值对集合
	 * @return
	 */
	public static HttpResponse post(String action, Object body,String format) {
		return post(action, body, null,format);
	}

	/**
     * 通过Https往API post xml数据
     *
     * @param url API地址
     * @param xmlObj 要提交的XML数据对象
    * @param mchId 商户ID
    * @param certPath 证书位置
     * @return
     */
    public static HttpResponse post(String action, String data, String certPassword, String certPath,String format) throws Exception {
    		  int socketTimeout = 10000;// 连接超时时间，默认10秒
	      int connectTimeout = 30000;// 传输超时时间，默认30秒
	      RequestConfig requestConfig;// 请求器的配置	 
	      HttpClient httpClient=null;// HTTP请求器    
    		 // 加载证书
        	httpClient=initCert(certPassword, certPath,httpClient);
     
        String result = null;
        HttpPost httpPost = new HttpPost(action);
        // 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", format);
        httpPost.setEntity(postEntity);
        // 根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        // 设置请求器的配置
        httpPost.setConfig(requestConfig);
        try {
            	HttpResponse response = null;
            return    response = httpClient.execute(httpPost);        
            
        } finally {
            httpPost.abort();
        }
    }
	
	/**
	 * 根据资源地址和查询串构建URL
	 * 
	 * @param base
	 *            资源地址
	 * @param query
	 *            查询字符串(矩阵)
	 * @return 资源URL
	 */
	public static String constructURL(final String base, final String... query) {
		if (query.length == 0) {
			return base;
		} else if (query.length == 1) {
			return String.format(URL_FORMAT, base, query[0]);
		} else {
			return String.format(URL_MATRIX_FORMAT, base, query[0], query[1]);
		}
	}

	/**
	 * 构建URL编码的查询字符串
	 * 
	 * @param parameters
	 *            参数集合
	 * @return 查询字符串
	 */
	public static String constructQuery(final Map<String, String> parameters) {
		final Map<String, String> map = new LinkedHashMap<>();
		parameters.forEach((String k, String v) -> {
			try {
				map.put(k, URLEncoder.encode(v, DEFAULT_CHARSET));
			} catch (UnsupportedEncodingException e) {
				throw new BusinessException("不支持的查询参数编码", -1);
			}
		});
		return constructPlainQuery(map);
	}

	/**
	 * 构建普通查询字符串，无URL编码
	 * 
	 * @param parameters
	 *            参数集合
	 * @return 查询字符串
	 */
	public static String constructPlainQuery(
			final Map<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		parameters.forEach((String k, String v) -> sb.append(k).append("=")
				.append(v).append("&"));
		return sb.substring(0, sb.length() - 1);
	}
	
	/**
     * 加载证书
     *
     * @param mchId 商户ID
     * @param certPath 证书位置
     * @throws Exception
     */
    private static HttpClient initCert(String password, String certPath,HttpClient httpClient) throws Exception {
        // 证书密码，默认为商户ID
        String key = password;
        // 证书的路径
        String path = certPath;
        // 指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // 读取本机存放的PKCS12证书文件
        FileInputStream instream = new FileInputStream(new File(path));
        try {
            // 指定PKCS12的密码(商户ID)
            keyStore.load(instream, key.toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, key.toCharArray()).build();
        SSLConnectionSocketFactory sslsf =
                new SSLConnectionSocketFactory(sslcontext, new String[] {"TLSv1"}, null,
                        SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        return  HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }
}
