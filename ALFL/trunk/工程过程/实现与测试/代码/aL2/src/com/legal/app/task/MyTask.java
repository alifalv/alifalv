package com.legal.app.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;
import com.common.utils.GeTuiPushtoSingle;
import com.legal.app.service.UserService;


/**
 * 定义任务器：每个定时任务在此类中写一个方法即可 
 * </br>创建时期: 2018年10月10日
 * @author hyq
 */
@Component
public class MyTask  { 
	@Autowired
	private UserService userService;
	
	private static Map map = new HashMap<String, Object>();
	
	/**
	 * 每分钟查看缓存的内容，有需要推的，就推； 
	 * @throws Exception 
	 */
	/*@Scheduled(cron = "0 * * ? * *")
	private void pushTask() throws Exception {	
		//TODO
		GeTuiPushtoSingle pusher= GeTuiPushtoSingle.getInstance();
		List<Map>  list  = (List<Map>) map.get("scheduleRemindertimeKey");
		if(null!=list){
			for(int i=0;i<list.size();i++){
				String now = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
				String t =list.get(i).get("gregorianCalendar").toString();
				if(t.compareTo(now)<=0){
		    		String pushCode = list.get(i).get("pushCode").toString();
		    		String content = list.get(i).get("content").toString();
		    		pusher.pushToApple(pushCode, content);
		    		pusher.pushToAndroid(pushCode, content);
		    	    userService.updateScheduleRemindertime(list.get(i).get("id").toString());
		    		ExceptionLogger.writeLog("推送开始："+now+"向"+pushCode+"推送了"+content);
				 }
			}
		}
	}*/
	
	/**
	 * 每5分钟，同步一下数据库日程的最新内容到缓存	
	 * @throws Exception 
	 */
	/*@Scheduled(cron = "0 * * ? * *")
	public void queryData() throws Exception {		
		List<Map>  list = userService.getScheduleRemindertime();
		map.put("scheduleRemindertimeKey", list);
		ExceptionLogger.writeLog("提醒时间加入缓存"+map.get("scheduleRemindertimeKey").toString());
	}
	
	
	public static void main(String[] args){
			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
			System.out.println("2018-10-10 20:10".compareTo(now)+"当前时间"+now);
 			System.out.println("2018-10-10 20:07".compareTo(now));
 			System.out.println("2018-10-10 20:19".compareTo(now));
	}*/
	/**
	 * 每天晚上1点执行一次  检测一下文书是否过期
	 * @throws Exception
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void queryVoucher() throws Exception {		
		List<Map>  list = userService.queryVoucher();
		if(null!=list){
			for(int i=0;i<list.size();i++){
				String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
				String endTime =  list.get(i).get("endTime").toString();
				 if(endTime.compareTo(now)<0){
					 ExceptionLogger.writeLog("文书id="+list.get(i).get("voucherId").toString());
						Map m2 = new HashMap<String, String>();
						m2.put("state", 2);
						m2.put("voucherId", list.get(i).get("voucherId").toString());
						userService.updateVoucher(m2);
 				 }	
			}
		}
	
	}

}
