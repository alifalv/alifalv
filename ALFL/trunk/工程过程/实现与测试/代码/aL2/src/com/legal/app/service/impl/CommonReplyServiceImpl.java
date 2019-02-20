package com.legal.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.common.log.ExceptionLogger;
import com.common.utils.SMSUtilLiantong;
import com.common.utils.SystemConfigUtil;
import com.legal.app.model.CommonReply;
import com.legal.app.service.AdviceService;
import com.legal.app.service.CommonReplyService;
import com.legal.app.service.UserService;
import com.legal.app.utils.BusinessType;

@Service
public class CommonReplyServiceImpl extends DaoMybatisImpl<CommonReply> implements CommonReplyService{
	@Autowired
	private UserService userSer;
	
	@Autowired
	private AdviceService adviceSer;
	/**
	 * 保存回复信息
	 * 返回 保证的id 
	 */
	@Override
	public int commonReply(Map map) throws Exception {
		ExceptionLogger.writeLog("回复");
		//获取业务类型
	//	int businessType = (int)map.get("businessType"); 
		/**
	    if(businessType == BusinessType.REPLY_ADVICE){
			serviceName = super.executeQueryOne("findServiceNameByAdvice", (int)map.get("relativeId")).toString();
		}else if(businessType == BusinessType.REPLY_OFFER){
			serviceName = super.executeQueryOne("findServiceNameByCase", (int)map.get("relativeId")).toString();
		}else if(businessType == BusinessType.REPLY_ALI_ADJUDUCATION||businessType == BusinessType.REPLY_MAKE_COMPLAINTS){
			serviceName = super.executeQueryOne("findServiceNameByAdvice", (int)map.get("relativeId")).toString();
		}
		**/
	//	map.put("serviceName", serviceName); 
	//	Map user = (Map)super.executeQueryOne("findUserInfo", (int)map.get("userId")); 
	//	map.putAll(user);
		 
		//查询出针对回复人的Id；
		
		//统一回复 发送站内信息；
		String typeName = BusinessType.getReplayTypeName((Integer)map.get("businessType"));
		String content = "尊敬的用户，【"+typeName+"】已有新的回复，快去看看吧！点击直接跳转订单内容";
		
		 Map<String, Object> msgMap1 = new HashMap<String, Object>();
         msgMap1.put("isRead","0"); //0:没读 1：已读
         msgMap1.put("sendTime",new Date()); //0:没读 1：已读
         msgMap1.put("isDelete","0"); //0:未删除 1：已删除
         msgMap1.put("messageState","0"); //
         msgMap1.put("is_send","1"); //0:未发送1：已发送
         msgMap1.put("create_time",new Date()); 	
         msgMap1.put("userId",map.get("sendUserId")); 
         msgMap1.put("messageContent",content);
         msgMap1.put("messageType",typeName);
         msgMap1.put("businessType","0");
         msgMap1.put("businessId",map.get("sourceArticleId"));
         msgMap1.put("messageTitle",map.get("serviceName"));  
         userSer.messageSend(msgMap1);
		
         //判断这个类型是否为法律咨询，
         if(typeName.equals(BusinessType.getReplayTypeName(3))) {
        	 //如果是法律咨询，查询这个法律咨询是否为一对一指定的； 
     		Map advice = adviceSer.findAdviceById(Integer.valueOf(map.get("sourceArticleId").toString())); 
     		 if(null != advice) {
    	    	 Object privateUserId = advice.get("privateUserId");
    	    	 if(null != privateUserId && !privateUserId.toString().equals("0")) {
    	    		 //查询出律师的手机号码；
    	    		 Map userMap =  userSer.getUserInfo(privateUserId+"");
    	    		 //这是指定的律师进行咨询 ,发送短信 
    	    		 SMSUtilLiantong.sendCounselor(userMap.get("mobile").toString());
    	    	 }
    	     }
         }
		
		return  	super.executeUpdate("addReply", map);
	}
	
	

	
	@Override
	public int updateReplay(Map map) throws Exception { 
		return  super.executeUpdate("updateReplay", map);
	}




	/**
	 * 
	 */
	@Override
	public List<Map> findReplyList(int relativeId, int businessType,int pageNo, int pageSize )
			throws Exception {  
		Map map = new HashMap();
		map.put("relativeId", relativeId);
		map.put("businessType", businessType);
        Paging paging = new Paging(pageSize, pageNo,true); 
		//查询出一级回复的帖子内容
		List<Map> list = super.executeQuery("findReplyList",paging, map);
		if(null != list && list.size() > 0 ) {
			List<Map> listMap =    super.executeQuery("findReplyListNextReply",map);
			for(Object ma_tmp : list) {
				if(ma_tmp instanceof Map) {
					Map ma = (Map)ma_tmp;
					int replyId = (int)ma.get("replyId");
					Object job = ma.get("job");
					String jobName = "";
					if(null != job && job.toString().equals("")) {
						jobName = SystemConfigUtil.getValue(Integer.valueOf(job.toString()),SystemConfigUtil.TYPE_JOB);
					}
					ma.put("jobName", jobName);
					List<Map> nextReply = new ArrayList<Map>();
					for(Map m : listMap) {
					   int upId = (int)m.get("upId");
					   if(upId == replyId) {
					   Object nextJob = m.get("job");
						String nextJobName = "";
						if(null != nextJob && nextJob.toString().equals("")) {
							nextJobName = SystemConfigUtil.getValue(Integer.valueOf(nextJob.toString()),SystemConfigUtil.TYPE_JOB);
						}
						m.put("jobName", nextJobName); 
						nextReply.add(m);
					   }
					}
				   ma.put("nextReplyData", nextReply);
				}
			}
		}
		return  list;
	}

}
