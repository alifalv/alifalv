package com.common.utils;

import java.util.concurrent.TimeUnit;

import com.common.log.BusinessException;

/**
 * TOKEN的加密、解密算法； </br>date:2017-05-26
 * 
 * @author hyq
 */
public class TokenHandle {
	/**
	 * 根据用户登陆名，生成一个带时间戳的aes加密的 TOKEN; 格式为：encryt(userName#timestamp) 如
	 * "hyqing#32474728472#20170909121212"
	 * 
	 * @param userName 用户ID
	 * @param deviceId 设备ID
	 * @return
	 */
	public static String encrytToken(String userName,String deviceId) throws Exception {
		long stime = System.currentTimeMillis();
		String text = userName + "#" + deviceId + "#" +stime;
		try {
			String entext = AESOperator.encrypt(text);
			System.out.println(entext);
			//鉴于有时候会生成带换行符的token
			entext = entext.replace("\n","@n@");//   
			entext = entext.replace("\r","@r@");//
			return entext;
		} catch (Exception e) {
			throw e;
		}
	}
	

	/**
	 * 解密访问token;如果合法，则返回用户名的明文，否则抛出“不合法访问标识”的异常；
	 * 
	 * @param token
	 *            加密的TOKEN
	 * @param interval
	 *            最大允许的超时秒数；
	 * @return
	 */
	public static String [] decrytToken(String token, long interval)
			throws Exception {
		long stime = System.currentTimeMillis();
		long ptime = 0;
		String rs = AESOperator.decrypt(token);
		String[] cis = rs.split("#");
		ptime = Long.parseLong(cis[2]);
		if (Math.abs(stime - ptime) < interval * TimeUnit.SECONDS.toMillis(1))
			return cis;
		else
			throw new BusinessException("访问终端不合法 ", -100);
	}


}
