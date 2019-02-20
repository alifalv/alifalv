package com.legal.app.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.log.ExceptionLogger;
import com.common.utils.CodeUtil;
import com.common.utils.SMSUtilLiantong;
import com.common.web.WebUtils;

@RequestMapping("sms")
@Controller
public class SmsController extends SuperController{
	/**
	 * "您的验证码为"+code;
	 * @param mobile
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("send/{mobile}")
	public void send(
			@PathVariable("mobile") String mobile,HttpServletRequest hsr,
			PrintWriter out
			) throws Exception{
		
		ExceptionLogger.writeLog("短信验证，号码="+mobile);
		 String code = CodeUtil.createSmsCode();
		 String message7 = "您的验证码为"+code;
		String rs=SMSUtilLiantong.sendMsg(mobile, message7);
		ExceptionLogger.writeLog("验证码："+code);
		System.out.println("发送结果："+rs);
		hsr.getSession().setAttribute("phoneCode", mobile);
		hsr.getSession().setAttribute("msgCode", code);
		if(rs.equals("0")){
			//加入到缓存   保存10分钟
			cache.put(mobile, code,600000);
			out.print(WebUtils.responseMsg("发送成功."));
			hsr.getSession().setAttribute("phoneCode", mobile);
			hsr.getSession().setAttribute("msgCode", code);
		}else{
			out.print(WebUtils.responseMsg(rs,-1));
		}
	}
	

	/**
	 * "您正在重置密码,手机验证码是"+code+"请在5分钟内按页面提示提交验证码,为了您的账户安全,请勿透露给他人.";
	 * @param mobile
	 * @param templateId
	 * @param content
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("sendUppd/{mobile}")
	public  void sendUppd(@PathVariable("mobile") String mobile,PrintWriter out) throws Exception{
		String code = CodeUtil.createSmsCode();
		ExceptionLogger.writeLog("验证码"+code);
	    String message4 = "您正在重置密码,手机验证码是"+code+"请在5分钟内按页面提示提交验证码,为了您的账户安全,请勿透露给他人.";
		String rs=SMSUtilLiantong.sendMsg(mobile, message4);
		ExceptionLogger.writeLog("发送结果："+rs);
		if(rs.equals("0")){
			cache.put(mobile, code,600000);
			ExceptionLogger.writeLog("发送成功.");
			out.print(WebUtils.responseMsg("发送成功."));
		}else{			
			ExceptionLogger.writeLog("发送失败");
			out.print(WebUtils.responseMsg(rs,-1));
		}
	}
	
	

	/**
	 *  "尊敬的用户您好,您正在注册为"+userType+"会员,请输入手机验证码"+code+"有效期,5分钟,请勿透露给他人,如非您本人操作,请忽略本短信.";
	 * @param mobile
	 * @param templateId
	 * @param content
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("sendReg/{mobile}/{userType}")
	public  void sendReg(@PathVariable("mobile") String mobile,@PathVariable("userType") String userType,PrintWriter out) throws Exception{
		String userName = "";
		if(userType.equals("1")){
			userName = "咨询者";
		}else if(userType.equals("2")){
			userName = "咨询师";
		}else if(userType.equals("3")){
			userName = "企业";
		}
		String code = CodeUtil.createSmsCode();
	    String message5 = "尊敬的用户您好,您正在注册为"+userName+"会员,请输入手机验证码"+code+"有效期,5分钟,请勿透露给他人,如非您本人操作,请忽略本短信.";
		ExceptionLogger.writeLog("注册身份="+userName+"验证码"+code);
		String rs=SMSUtilLiantong.sendMsg(mobile, message5);
		ExceptionLogger.writeLog("发送结果："+rs);
		if(rs.equals("0")){
			cache.put(mobile, code,600000);
			out.print(WebUtils.responseMsg("发送成功."));
			ExceptionLogger.writeLog("发送成功.");
		}else{
			ExceptionLogger.writeLog("发送失败");
			out.print(WebUtils.responseMsg(rs,-1));

		}
	}	
	
	
	
	
}
