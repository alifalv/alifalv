package com.common.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import com.common.cache.ICache;

/**
 * 缓存的实现类，目前是通过map实现。
 * 
 * @author Lqin
 *
 */
public class ICacheImpl implements ICache {
	private  final int TIME_LONG_CLEAN =4000;//定时清理超时的对象的间隔时长；单位毫秒
	private static final Map sessionIDMap = new HashMap();
	private static final Map sessionTimeMap = new HashMap();
	
	private ICacheImpl() {
		//定时清理超时的缓存对象
		new Thread(new Runnable() {			
			@Override
			public void run() {				
				while(true){
					try {
						Thread.currentThread().sleep(TIME_LONG_CLEAN);
					} catch (InterruptedException e) {}
										
					synchronized (sessionIDMap) {
						Iterator itr=sessionTimeMap.entrySet().iterator();
						while(itr.hasNext()){
							Map.Entry en=(Map.Entry)itr.next();
							long curt=System.currentTimeMillis();
							long putt=((Long)en.getValue()).longValue();
							if(curt>=putt){
								itr.remove();
								sessionIDMap.remove(en.getKey());
							}							
						}	
					} 				
				}				
			}
		}).start();
	}

	private static final ICacheImpl single = new ICacheImpl();

	//静态工厂方法
	public static ICacheImpl getInstance() {
		return single;
	}

	@Override
	public Object get(Serializable key) throws Exception {
		if (sessionTimeMap.get(key) != null) {
			if (Long.parseLong(sessionTimeMap.get(key).toString()) >= System
					.currentTimeMillis()) {
				return sessionIDMap.get(key);
			} else {
				remove(key);
				return null;
			}
		} else {
			if (sessionIDMap.get(key) != null)
				return sessionIDMap.get(key);
			else
				return null;
		}

	}

	@Override
	public void remove(Serializable key) throws Exception {
		sessionIDMap.remove(key);
		sessionTimeMap.remove(key);
	}

	@Override
	public void put(Serializable key, Object value) throws Exception {
		sessionIDMap.put(key, value);
	}

	@Override
	public void put(Serializable key, Object value, long timeLength)
			throws Exception {
		sessionTimeMap.put(key, System.currentTimeMillis() + timeLength);
		sessionIDMap.put(key, value);
	}

	@Override
	public Object get(Serializable key, String fieldName) throws Exception {
		throw new Exception("此方法未提供实现");
	}

	@Override
	public void put(Serializable key, String fieldName, Object value,
			long timeLength) throws Exception {
		throw new Exception("此方法未提供实现");

	}
	
	
	public static void main(String[] args) throws Exception {
		ICacheImpl c=ICacheImpl.getInstance();
		c.put(1, "a",10000);
		c.put(2, "b",5000);
		c.put(3, "c",50000);
		c.put(4, "d");
		int i=0;
		while(true){
			i++;
			Thread.sleep(1000);
			System.out.println(c.sessionIDMap);
			System.out.println(c.sessionTimeMap);
			System.out.println("---------------"+i+"---------------");
		}
	}
	
	
}
