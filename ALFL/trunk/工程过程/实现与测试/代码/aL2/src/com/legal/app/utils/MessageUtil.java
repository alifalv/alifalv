package com.legal.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;


public class MessageUtil {
	//  黄曾海  13487884141  湘南中学  327462374234773
	public static String getMessage(String templeteId,Map map) {
		
		InputStream in = MessageUtil.class.getClassLoader().getResourceAsStream("com/legal/app/controller/smsModel/"+templeteId+".txt");
		System.out.println("com/legal/app/controller/smsModel/"+templeteId+".txt");
		byte [] bytes = new byte[1024];
		String str = "";
		try {
			//读取到内容
			int rn = in.read(bytes);
			str = new String(bytes,0,rn,"UTF-8");
			if(map!=null&&!map.isEmpty()){
				Set<String> sets = map.keySet();
				for (String key : sets) {
					
					String k = "${"+key+"}";
					System.out.println(str+":"+k);
					str =str.replace(k, map.get(key).toString());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return str;
		
	}

}
