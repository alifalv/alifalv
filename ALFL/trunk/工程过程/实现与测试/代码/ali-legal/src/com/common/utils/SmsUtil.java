package com.common.utils;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.common.log.ExceptionLogger;
import com.common.web.JavaPropertiesUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Administrator on 2016/3/4.
 */

public class SmsUtil {

	/** 短信服务开发账号SID */
	private static String smsAccountSid = null;
	/** 短信服务开发账号TOKEN */
	private static String smsToken = null;
	/** 短信服务开发账号应用ID */
	private static String appId = null;

	static {
		try {
			smsAccountSid = JavaPropertiesUtils.getValue(
					"/conf/config.properties", "smsAccount");
			smsToken = JavaPropertiesUtils.getValue("/conf/config.properties",
					"smsToken");
			appId = JavaPropertiesUtils.getValue("/conf/config.properties",
					"smsAppId");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public static boolean sendSms(String phone, String templateId,
			String[] contents) throws Exception {

		boolean flag = false;
		HashMap<String, Object> result = null;
		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount(smsAccountSid, smsToken);// 初始化主帐号和主帐号TOKEN
		restAPI.setAppId(appId);// 初始化应用ID
		result = restAPI.sendTemplateSMS(phone, templateId, contents);

		System.out.println("SDKTestSendTemplateSMS result=" + result);
		ExceptionLogger.writeLog(ExceptionLogger.WARN, "短信发送接口返回结果：" + result);

		if ("000000".equals(result.get("statusCode"))) {
			// 正常返回输出data包体信息（map）
			HashMap<String, Object> data = (HashMap<String, Object>) result
					.get("data");
			Set<String> keySet = data.keySet();
			ExceptionLogger.writeLog(ExceptionLogger.WARN,
					"短信发送接口返回正常返回输出data包体信息：");
			for (String key : keySet) {
				Object object = data.get(key);
				ExceptionLogger.writeLog(ExceptionLogger.WARN, key + " = "
						+ object);
			}

			flag = true;

		} else {
			// 异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") + " 错误信息= "
					+ result.get("statusMsg"));
		}

		return flag;
	}

}
