package com.legal.app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.common.cache.ICache;
import com.common.log.ExceptionLogger;
import com.common.utils.AESOperator;

/**
 * 全局变量缓存工具
 * @author 李军
 *
 */
public class WebUtilCahe {
	public final static String SYS_CONFIG_IMAGEUPLOADPATH="SYS_CONFIG_IMAGEUPLOADPATH";
	public final static String SYS_CONFIG_IMAGEPREVIEWPATH="SYS_CONFIG_IMAGEPREVIEWPATH";
	
	private static Map<String,Object> cache= new ConcurrentHashMap<String, Object>();
	//设置token失效时长,默认为 1小时;
	private static long tokenTimeout = 60*60*60*1000;
	//设置统计请示ipr 的次数;
	public static int queryIpcount = 40; 
	//设置IP黑名单的禁用时间;(毫秒)
	public static int ipDisableTime = 1000 * 60;
	/**
	 * 记录ip请求数据;
	 */
	public final static String BIZ_FILTER_QUERYIP = "BIZ_FILTER_QUERYIP";
	/**
	 * IP黑名单;
	 */
	public final static String BIZ_FILTER_IPBLACKLIST = "BIZ_FILTER_IPBLACKLIST";
	
	public static ICache redisCache; 
	
	public static void put(String key,Object value){
		cache.put(key, value);
	}
	
	

	public static Object get(String key){
		return cache.get(key);
	}
	
	/**
	 *  根据合约id 获取开盘的时间
	 * @param instrumentId
	 * @return map  //{"instrumentID":XXX,"openTradeTime":XXX,closeTradeTime:XXX}; 
	 * @throws Exception
	 */
	public static Map getTradeTime(String instrumentId) throws Exception{		
		Object ob = cache.get("TradeTime"+instrumentId);
		return (Map)ob;
	}
	
	/**
	 * 把合约开市与闭市的对象放在全局缓存；
	 * @param obj
	 * @throws Exception
	 */
	public static Boolean setTradeTime(Map obj) throws Exception{
		Boolean rel = false;
		if(null != obj) {
			cache.put("TradeTime"+obj.get("instrumentID"), obj);
			rel = true;
		}
		return rel;
	}
	
	/**
	 * 清除缓存
	 * @param key
	 */
	public static void removeCache(String key) {
		cache.remove(key);
	}
	
	/**
	 * 批量把每个合约的最新行情放入缓存中
	 * @param list
	 */
	public static void setLatestQuotationsList(List<Map<String,Object>> list) {
		if(null != list) {
			for(int i = 0 ; i < list.size();i++) {
				Map m = list.get(i);
				cache.put("LatestQuotations_"+m.get("InstrumentID"), m);
			}
		}
		//Map map =  WebUtilCahe.getLatestQuotations("bu1712"); 
	}
	
	/**
	 * 把单个的最新行情数据放入到缓存
	 * @param m
	 */
	public   static void setLatestQuotationsMap(Map m) {
		if(null != m) {
			cache.put("LatestQuotations_"+m.get("InstrumentID"), m);
		}
	}
	
	/**
	 * 根据合约号取出这个合约的最新行情
	 * @param instrumentID 合级id
	 * @return  
	 */
	public static Map getLatestQuotations(String instrumentID) {
		return (Map)cache.get("LatestQuotations_"+instrumentID);
	}
	
	/**
	 * 获取CTP 结算状态是否 登陆 
	 * @return 如果为false 是需要继续 登陆 
	 */
	public static Boolean getSettlementStatus() {
		return null == (Boolean)cache.get("settlementStatus") ? false : (Boolean)cache.get("settlementStatus");
	} 
	
	/**
	 * 存放CTP登陆结算状态
	 * @param val
	 */
	public static void setSettlementStatus(Boolean val) {
		cache.put("settlementStatus",val);
	}
	
	/**
	 * 存放着上线的产品合约号；（原始数据是从 config.properties 中取的）
	 * @param ids
	 */
	public static void setProdutNumbers(String[] ids) {
		cache.put("_ProdutNumbers_", ids);
	}
	
	/**
	 * 取出上线的产品合约号 （原始数据是从 config.properties 中取的）
	 * @return 合约号数组
	 */
	public static String[] getProdutNumbers() {
		 return (String[])cache.get("_ProdutNumbers_");
	}
	/**
	 * 把合约乘数放入到缓存中去；
	 * @param instrumentID 合约号
	 * @param m 乘数内容
	 * @格式如下："{contractMultiplier:XXX,heyueInExchange:XXX}" 从监听器 ，OnRspQryInstrument 的 CThostFtdcInstrumentField对象中取;
	 */
	public static void setContractMultiplier(String instrumentID,Map m) {
		cache.put("ContractMultiplier_"+instrumentID, m);
	}
	
	/**
	 * 从缓存中取合约乘数；
	 * @param instrumentID
	 * @return
	 */
	public static Map<String,Object> getContractMultiplier(String instrumentID){
		Map m = (Map)cache.get("ContractMultiplier_"+instrumentID);
		if(null == m) {
			ExceptionLogger.writeLog(1, " 从缓存中获取"+instrumentID +"的合级乘数失败；");
		}
		return m;
	}
	
	/**
	 * 把每一手需缴纳的保证金和手续费放入到缓存中去；
	 * @param instrumentID 合约号
	 * @param m 乘数内容
	 * @格式如下："{contractMultiplier:XXX,heyueInExchange:XXX}" 从监听器 ，OnRspQryInstrument 的 CThostFtdcInstrumentField对象中取;
	 */
	public static void setContractEnterPriceAndCost(String instrumentID,Map m) {
		cache.put("ContractEnterPriceAndCost"+instrumentID, m);
	}
	
	/**
	 * 从缓存中每一手需缴纳的保证金和手续费；
	 * @param instrumentID
	 * @return
	 */
	public static Map<String,Object> getContractEnterPriceAndCost(String instrumentID){
		Map m = (Map)cache.get("ContractEnterPriceAndCost"+instrumentID);
		if(null == m) {
			ExceptionLogger.writeLog(1, " 从缓存中获取"+instrumentID +"每一手需缴纳的保证金和手续费失败；");
		}
		return m;
	}
	
	/**
	 * 缓存后台用户ID，角色，权限
	 * @param list
	 */
	public static void setRoleRightsList(List<Map<String,Object>> list) {
		cache.put("RoleRightsList",list);
	}
	/**
	 * 从缓存中读取后台用户ID，角色，权限
	 * @return
	 */
	public static List<Map<String,Object>> getRoleRightsList(){
		List<Map<String,Object>> list = (List<Map<String,Object>>)cache.get("RoleRightsList");
		return list;
	}
	
	public static Long getSysCurrentTimeMillis() {
		return System.currentTimeMillis();
	}
	
	/**
	 * 检测token 是否失效
	 * @param old  旧的毫秒数
	 * @return false 失效;
	 */
	public static Boolean checkTokenLose(long old) {
		if(WebUtilCahe.tokenTimeout > ( WebUtilCahe.getSysCurrentTimeMillis() - old )) {
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * 根据usercode 产生一个相对应的token
	 * @param usercode
	 * @return
	 * @throws Exception
	 */
	public static String creationToken(String usercode)throws Exception { 
	    String str="";
	    String encryption="";
	    try {
			ArrayList list = new ArrayList();
			for (char c = 'a'; c <= 'z'; c++) {
			    list.add(c); 
			}
			for (char c = 'A'; c <= 'Z'; c++) {
			    list.add(c); 
			}
			str = "";
			for (int i = 0; i < 20; i++) {
			    int num = (int) (Math.random() * 52);
			    str = str + list.get(num);
			}
			str += getSysCurrentTimeMillis();
			encryption = AESOperator.encrypt(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Map<String, Object> userMap = new HashMap<String,Object>();
		userMap.put("token",encryption); 
		userMap.put("mis", getSysCurrentTimeMillis()); 
		//redisCache.put("BIZ_"+"CHECKTOKEN_"+usercode,userMap);
		return encryption;
	}
}
