package com.common.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.common.log.ExceptionLogger;
import com.common.web.HttpUtils;


/**
 * 高德地图
 * @author admin
 *
 */
public class GdMapUtil {

	/** 高德地图用户 key值 **/
	private static String key = "1825cbad4c9be76937e7a9c75a13f3de";
	/** 高德地图获取路况的api地址 **/
	private static String url = "http://restapi.amap.com/v3/traffic/status/circle";
	private static String address_url = "http://restapi.amap.com/v3/geocode/regeo";
	
	/**
	 * 获取高德地图路况信息
	 * @param location 中心点
	 * @param radius 半径
	 * @return
	 */
	public static JSONObject getRoadCondition(String location,String radius){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("key", key);
		parameters.put("location", location);
		parameters.put("radius", radius);
		JSONObject json;
		try {
			json = HttpUtils.getJSON(url, parameters);
			ExceptionLogger.writeLog("高德地图路况："+json.toString());
			return json;
		} catch (IOException e) {
			e.printStackTrace();
			ExceptionLogger.writeLog("获取官方路况异常："+e);
		}
		return null;
	}
	
	
	
	/**
	 * 高德地图根据经纬度获取地址
	 * @param location 中心点
	 * @param radius 半径
	 * @return
	 */
	public static JSONObject getAddres(String location){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("key", key);
		parameters.put("location", location);
		parameters.put("extensions", "base");
		parameters.put("batch", "false");
		JSONObject json;
		try {
			json = HttpUtils.getJSON(address_url, parameters);
			ExceptionLogger.writeLog("高德地图地址："+json.toString());
			return json;
		} catch (IOException e) {
			e.printStackTrace();
			ExceptionLogger.writeLog("获取地址异常："+e);
		}	
		return null;
	}
	
	
	
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(8800);
			System.out.println("开始...");
			Socket s = server.accept();
			
			OutputStream out = s.getOutputStream();
			
			byte [] bytes = new byte[10240];
			
			FileInputStream in = new FileInputStream("D:/BaiduYunDownload/solidworks2016.rar");
			
			while(in.read(bytes)!=-1){
				out.write(bytes);
			}
			
			in.close();
			
			out.close();
			
			s.close();
			
			server.close();
			
			System.out.println("文件传输完毕");
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
}
