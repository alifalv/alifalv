package com.legal.app.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;
import com.common.utils.SMSUtilLiantong;
import com.common.utils.StringUtil;
import com.common.utils.SystemConfigUtil;
import com.legal.app.controller.model.Case;
import com.legal.app.model.Advice;
import com.legal.app.service.AdviceService;
import com.legal.app.service.OrderService;
import com.legal.app.service.UserService;
import com.legal.app.service.WalletRecordService;
import com.legal.app.utils.BusinessType;
import com.legal.app.utils.CreateOrderNumber;
import com.legal.app.utils.OrderType;
import com.legal.app.utils.PayOrder;
import com.legal.app.utils.Tools;
import com.legal.app.utils.VipUtils;

import net.sf.json.JSONObject;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class AdviceServiceImpl extends DaoMybatisImpl<Advice> implements AdviceService{
	@Autowired
	private OrderService orderser;
	@Autowired
	private UserService userSer;
	@Autowired
	private WalletRecordService walletSer;
	
	@Override
	public List<Map> adviceReplyList(int adviceId) throws Exception {
		return  super.executeQuery("adviceReplyList", adviceId);
	}

	@Override
	/**
	 * 删除这个文章时查看有没有回复 ；
	 * 返回 0  ： 说明 已经有回复了，不能删除 ；
	 * 返回 1   :  已经 把这篇咨询文章 删除 了；
	 */
	public int deleteAdvice(int adviceId) throws Exception {
		//删除这个法律咨询，先检查是否有回复
		List<Map>  listM = this.adviceReplyList(adviceId);
		if(null != listM && listM.size() > 0) {
			return 0;
		}else {
			 super.executeUpdate("deleteAdvice", adviceId);
			 return 1;
		}
	}

	/**
	 * 最近回复我的法律咨询和案件委托的人数
	 */
	@Override
	public Map myReplyRecently(int userId) throws Exception { 
			List listM = super.executeQuery("myReplyRecently", userId);
			Map<String,Object> m = new HashMap<String,Object>();
			int counts = 0;
			List list = new ArrayList<Object>();
			if(null != listM && listM.size() > 0 ) {
				counts = listM.size();
				list = listM;
			} 
			m.put("counts", counts);
			m.put("lists",list);
		return m;
	}

	@Override
	public List<Map<String, Object>> myConsultList(Map<String, Object> map, Paging paging) throws Exception {
		List<Map<String,Object>> listM = super.executeQuery("myConsultList", paging, map);
		if(null != listM && listM.size() > 0 ) {
			for(Object m : listM) {
				 if( m instanceof Map) {
					 Map ma = (Map)m;
					 String caseTypeName = SystemConfigUtil.getValue((int)ma.get("caseType"), SystemConfigUtil.TYPE_CASE);
					 ma.put("caseTypeName", caseTypeName);
				 }
			}
		} 
		return listM;
	}

	
	
	@Override
	public int updateAdvice(Map map) throws Exception {
		return super.executeUpdate("updateAdvice",map);
	}

	@Override
	public List<Map<String, Object>> findAdviceList(
			Map<String, Object> parameters, Paging paging) throws Exception {
		
		List list = super.executeQuery("findAdviceList", paging, parameters);
		for (Object m : list) {
			if(m instanceof Map){
				Map map = (Map)m;
				map.put("caseTypeName", SystemConfigUtil.getValue((int)map.get("caseType"), SystemConfigUtil.TYPE_CASE));
			}
		}
		//获取所有的adviceId
		/*		List<Integer> ids = new ArrayList<Integer>();
				
				for (Object m : list) {
					if(m instanceof Map){
						Map map = (Map)m;
						ids.add((int)map.get("adviceId"));
					}
				}
				
				//加载证据信息
				List<Map> imgs = super.executeQuery("findImgsByAdviceIds", ids.toArray());
				for (Object m : list) {
					if(m instanceof Map){
						Map map = (Map)m;
						
						List<String> imgList = new ArrayList<String>();
						
						for (Map mm : imgs) {
							
							if(mm.get("adviceId").equals(map.get("adviceId"))){
								imgList.add(mm.get("img").toString());
							}
							
						}
						
						map.put("", value)
						
					}
					
				}*/
		
		return list;
	}

	@Override
	public int sendAdvice(Map<String, Object> parameters) throws Exception {
		
		ExceptionLogger.writeLog("发布咨询");
		
		super.executeUpdate("addAdvice", parameters);
		
		//图片保存修改，在 advice 表中 增加了 imgs  这个字段；
		//2018-09-14 军山依旧
		
		
		/**
		if(parameters.get("imgs")!=null){
			super.executeUpdate("addAdviceImgs", parameters);
		}
		**/
		return 1;
	}
	
	

	@Override
	public List<Map> findRankValueAndIsReply(Map<String, Object> parameters) throws Exception {
		return super.executeQuery("findRankValueAndIsReply",parameters);
	}

	/**
	 * 保存法律咨询的解答；
	 */
	@Override
	public int saveAdviceReply(Map<String, Object> map) throws Exception {
		ExceptionLogger.writeLog("保存法律咨询的解答"); 
		int userId = (int)map.get("userId");
		//查询出咨询师的属性对象；
		Map counMap = userSer.findCounselorByUserId(userId);
		if(null == counMap || counMap.size() == 0) {
			throw new BusinessException("你不能进行解答.",-1); 
		}
		//根据当前解答人的id 和贴子的id查看是否已经解答过，并且查出这个帖子的当前解答者的排名；
		List<Map>  listD =  this.findRankValueAndIsReply(map);
		long rankValue = 0 ;
		boolean isReply = false;//是否已经回复过；
		if(null != listD && listD.size() > 0 ) {
			Map m = listD.get(0); 
			rankValue = (Long)m.get("rankValue");
			Long isReply_tmp = (Long)m.get("replyId");
			//如果大于0 说明这个登陆者已经对这个法律咨询解答过；
			if(isReply_tmp > 0) {
				isReply = true;
			} 
		}
		
		//如果登陆者没有回复过
		if(!isReply) {
			//如果当前排名小于3，就判断这个
			if(rankValue  < 3) {
				rankValue	++;
			}else {
				rankValue = 0 ;
			} 
		}else {
			rankValue = 0;
		}
		
		
		//查询当前解答人的用户信息；
		Map user = (Map)super.executeQueryOne("findUserInfo", userId); 
		
		map.putAll(user); 
		map.put("rankValue",rankValue);  
	   
		Map  mapRe = null;
		//查询出这个法律咨询的详情；
		Map advice = (Map)super.executeQueryOne("findAdviceById", map.get("adviceId"));
		
	    //如果回复总次 小于3 并且 自己没有回复过，才给回复的咨询师分回复赏金
	    if(rankValue > 0  && rankValue < 4 && !isReply) {  
			    Map m = new HashMap<String,Object>();
			    m.put("businessType", PayOrder.TYPE_FYZX);
			    m.put("connectionId", map.get("adviceId").toString());   
				m.put("orderState","1");//查询订单还是在进行中
			    //查询这个支付订单是否存在
			    List listR =   orderser.findOrderDetails(m);
			    if(null != listR &&  listR.size()> 0 ) {
			    	mapRe = (Map)listR.get(0);
			    	int isPay = (null == mapRe.get("isPay") ?  2 :   (int)mapRe.get("isPay"));
			    	//如果等于1 ，说明 这个订单已经充值到平台 ，现在就可以把回复咨询师的回复金发到回复人的帐户中；
			    	if(isPay == 1) { 
			    		int privateUserId= 0;
			    		//判断这个订单是否为指定某位咨询师回复；
			    		Object sign = mapRe.get("sign");
			    		if(null != sign && !sign.equals("")) {
			    			JSONObject sig =	JSONObject.fromObject(sign);
			    			if(null != sig) { 
				    		   Object privateUserId_tmp =  sig.get("privateUserId");
					    		if(null != privateUserId_tmp && !privateUserId_tmp.equals("")) {
					    			privateUserId = Integer.valueOf(privateUserId_tmp.toString());
					    			if(privateUserId > 0 && privateUserId != userId) {
					    					throw new BusinessException("你不能进行解答,这是指定咨询师解答.",-1); 
					    			}
					    		}
			    			}
			    		}
			    		
			    		
			    		//获取这个订单是多少赏金
			    		Double reward =Tools.rwipeOffDecimals( (null == mapRe.get("orderPrice") ? 0D :((BigDecimal)mapRe.get("orderPrice")).doubleValue()));
			    		//按赏金规则，获取能分到多少赏金
			    		if(privateUserId == 0) {
			    			//如果法律咨询不是指定某位咨询师解答，就按分成规则；这里是扣除了平台的费用了，
			    			reward =Tools.rwipeOffDecimals(VipUtils.getReward(rankValue, reward));
			    		}else {
			    			//否则赏金一次性给解答的咨询师，需要扣除平台费用
			    			reward = VipUtils.sysTemCost(null, reward);
			    			
			    		}
			    		//给回复师的订单中写一条记录；
			    		Map orderM = new HashMap<String,Object>();
						orderM.put("orderName", mapRe.get("orderName")); 
						//orderM.put("userId",  map.get("userId"));//不要写在前面这个userId 里面 
						orderM.put("favoreeUserId",map.get("userId"));  //写在收益人的id 里面
						orderM.put("businessType", OrderType.businessType_ZXHF);
						orderM.put("remark", user.get("nickName")+":"+userId);
						orderM.put("orderNum", 1);
						orderM.put("orderType", OrderType.ORDERTYPE_3);
						orderM.put("orderPrice", reward); //赏金
						orderM.put("connectionId", mapRe.get("connectionId"));
						orderM.put("sourceType", PayOrder.TYPE_FYZX);
						orderM.put("checkTime", new Date()); 
						orderM.put("endTime", new Date()); //赏金
						orderM.put("evaluate", 0); //是否评价   0：不需要评价   1：已评价  2：未评价， 
						orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
						orderM.put("isPay", 1);  //是否支付  ，0：不需要支付      1： 已支付   2：未支付  3: 正在支付中  	4：支付失败
						orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货；
						orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
						orderM.put("orderState", 0);//订单的状态； 1 ： 还在进行中， 0 ：已完成 
						orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败 
						orderser.saveBusinessOrder(orderM);
						
			    		
			    		//变更咨询师的钱包金额； 
						Double money = Tools.rwipeOffDecimals( (null == counMap.get("userBalance") ?  0D : ( (BigDecimal)counMap.get("userBalance")).doubleValue()) +reward);
						userSer.updateCounselorBalance(userId, money); 
						
						
			    		//插入一条咨询师的交易流水记录； 
			    		Map walletM = new HashMap<String,Object>();
			    		walletM.put("userId", userId);
			    		walletM.put("businessOrder", orderM.get("businessOrder"));
			    		walletM.put("balance", money);
			    		walletM.put("totalFee", reward);
			    		walletSer.saveWalletRecord(walletM);
			    		
			    		  //发站内消息 给咨询师已经得到了赏金
			    		Map<String, Object> msgMap1 = new HashMap<String, Object>();
		   		        msgMap1.put("isRead","0"); //0:没读 1：已读
		   		        msgMap1.put("sendTime",new Date()); //0:没读 1：已读
		   		        msgMap1.put("isDelete","0"); //0:未删除 1：已删除
		   		        msgMap1.put("messageState","0"); //
		   		        msgMap1.put("is_send","1"); //0:未发送1：已发送
		   		        msgMap1.put("create_time",new Date()); 	
		   		        msgMap1.put("userId",userId);  
		   		        msgMap1.put("messageContent","尊敬的用户，您参与的法律咨询回复，到帐"+reward+"元，快去看看吧！点击直接跳转订单内容");
		   		        msgMap1.put("messageType",mapRe.get("sourceType")+"赏金");
		   		        msgMap1.put("businessType","1");//0 : 回复消息类型  1：系统消息类型
		   		        //msgMap1.put("businessId",mapRe.get("connectionId").toString());   
		   		        msgMap1.put("businessId", orderM.get("businessOrder"));  //需要跳转订单详情
		   		        msgMap1.put("messageTitle",mapRe.get("orderName"));  
		   		    
		   		        userSer.messageSend(msgMap1);
			    		 

			    		//修改这个订单的备注，把回复的咨询姓名和id追加到 remark 字段中 
			    		Map orderMap = new HashMap<String,Object>();
			    		orderMap.put("businessOrder", mapRe.get("businessOrder"));//发布法律
			    		Object remark = mapRe.get("remark");
			    		String remark_tmp = "";
			    		if(null != remark && !remark.equals("")) {
			    			remark_tmp = "/";
			    		}
			    		remark_tmp +=user.get("nickName")+":"+userId;
			    		orderMap.put("remark",remark_tmp);
			    		
			    		//判断回复的咨询师加上自己是否有3 位，如果有，就写这个订单的状态为 完成功能；
			    		//或者回复的咨询是指定的，只要咨询师回复了，这个支付订单状态就是完成的
			    		if(rankValue == 3 || privateUserId > 0) { 
			    			orderMap.put("orderState",0);
			    			orderMap.put("endTime",new Date());
			    		} 
			    		//修改订单的状态为完成；
			    		orderser.update(orderMap);
			    		
			    	}else {
			    		//否则 就是订单还没有充值到平台 ，不能回复 ；
			    		throw new BusinessException("订单还未支付，暂时不能解答.",-1);
			    	}
			    	 
			    }else {
			    	//如果这个咨询没有产生订单，就看这个咨询是否为免费的 
			    	if(null != advice) {
			    		Object reward = advice.get("reward");
			    		if(!(new BigDecimal(reward.toString()).compareTo(new BigDecimal("0")) == 0)) {
			    			throw new BusinessException("订单还未支付，暂时不能解答.",-1);
			    		}
			    	}else {
			    		throw new BusinessException("订单还未支付，暂时不能解答.",-1);
			    	}
			    }
	    
	    } 
	    super.executeUpdate("saveAdviceReply",map);
	    super.executeUpdate("saveAdviceReplyDetails", map); 
	    if(!isReply){
		    super.executeUpdate("updateCounselorInfo",map);
	    }
	    //发站内消息给布人，告诉有咨询师进行了回复；
	     Map<String, Object> msgMap1 = new HashMap<String, Object>();
	    if(mapRe == null ) {
	    	//如果是免费的，就没有订单，需要查询这个免费咨询的原始数据；
		     Map objMap = (Map)super.executeQueryOne("listByAdviceId",map.get("adviceId"));
		     msgMap1.put("userId",objMap.get("userId"));  
		     msgMap1.put("messageType",PayOrder.TYPE_FYZX); 
		     msgMap1.put("businessId",map.get("adviceId"));
		     msgMap1.put("messageTitle",objMap.get("title"));  
	    }else { 
		     msgMap1.put("userId",mapRe.get("userId"));  
		     msgMap1.put("messageType",mapRe.get("sourceType")); 
		     msgMap1.put("businessId",mapRe.get("connectionId").toString());
		     msgMap1.put("messageTitle",mapRe.get("orderName"));  
	    }
	    
	  
	     msgMap1.put("isRead","0"); //0:没读 1：已读
	     msgMap1.put("sendTime",new Date()); //0:没读 1：已读
	     msgMap1.put("isDelete","0"); //0:未删除 1：已删除
	     msgMap1.put("messageState","0"); //
	     msgMap1.put("is_send","1"); //0:未发送1：已发送
	     msgMap1.put("create_time",new Date()); 	 
	     msgMap1.put("messageContent","尊敬的用户，您发布的法律咨询问题，已有咨询师为您解答，快去看看吧！点击直接跳转订单内容"); 
	     msgMap1.put("businessType","0");  
	     userSer.messageSend(msgMap1);
	     
	     //为咨询者发送信息；
	    //  这个必须是一对一的咨询才有信息；
	     // 判断一对一的咨询，是在咨询的订单中有指定的咨询者ID；
	     if(null != advice) {
	    	 Object privateUserId = advice.get("privateUserId");
	    	 if(null != privateUserId && !privateUserId.toString().equals("0")) {
	    		 //查询出发布人的手机号码；
	    		 Map userMap =  userSer.getUserInfo(advice.get("userId")+"");
	    		 //这是指定的律师进行咨询 ,发送短信
	    		 SMSUtilLiantong.sendConsultant(userMap.get("mobile").toString());
	    	 }
	     }
	    
		return 0;
	}
	

	
	
	 
	@Override
	public Map findAdviceById(int id) throws Exception { 
		return  (Map)super.executeQueryOne("findAdviceById", id);
	}

	@Override
	public int updateAdviceReply(Map<String, Object> map) throws Exception {
		 
		return  super.executeUpdate("updateAdviceReply", map);
	}


	
	/**
	public static void main(String[] args) {
		AdviceServiceImpl adv = new AdviceServiceImpl();
		for(Long i = 1L ; i < 10; i ++) {
		double d = 	adv.getReward(i, 20D);
		System.out.println(d);
		}
	}
	**/
	
	@Override
	public Map findAdviceDetails(Map<String, Object> parameters)
			throws Exception { 
		//获取咨询信息
		//Map map = (Map)super.executeQueryOne("findAdviceByAdviceId", parameters.get("adviceId"));
		
		//获取法律咨询的详情，并查询出他的上一页下一页的数据；
		Map map = (Map)super.executeQueryOne("findAdviceDetails", parameters);
		
		if(null != map) {
			String gt = (String)map.get("next");
			String lastAdviceId = "";
			String lastTitle ="";
			String nextAdviceId="";
			String nextTitle="";
			if(null != gt) {
				String [] next =gt.split("8m_m_8");
				nextAdviceId = null ==  next[0] ? "" : next[0];
				if(next.length >1){
					nextTitle = null ==next[1] ? "" : next[1];
				}else {
					nextTitle = "";
				}
				
				
			}
			String lt = (String)map.get("last");
			if(null != lt) {
				String[] last =lt.split("8m_m_8");
				lastAdviceId = null == last[0] ? "" : last[0];
				if(last.length >1) {
					lastTitle= null == last[1] ? "" : last[1];
				}else {
					lastTitle="";
				}
			}
			String formatName="";
			//if(StringUtil.isNotEmpty(map.get("userName").toString())) {
			if(null!=map.get("userName")) {
				formatName=map.get("userName").toString();
				formatName=formatName.substring(0, 1);
				formatName+="**";
			}
			map.put("userName", formatName);
			map.put("lastAdviceId", lastAdviceId);
			map.put("lastTitle", lastTitle);
			map.put("nextAdviceId", nextAdviceId);
			map.put("nextTitle", nextTitle);
			
			//放入法律咨询的类型；
			Case cases = SystemConfigUtil.caseMap.get("case"+map.get("caseType"));
			map.put("caseTypeName", cases.getCaseName());
			
		}
		
		
		/**
		
		if(map==null){
			throw new BusinessException("咨询详情内容获取失败.", -1);
		}
		
		int [] ids = {(int)parameters.get("adviceId")};
		
		//加载证据图片信息
		List<Map> imgs = super.executeQuery("findImgsByAdviceIds", ids);
		
		List<String> imgList = new ArrayList<String>();
		if(imgs!=null){
			for (Map m : imgs) {
				imgList.add(m.get("img").toString());
			}
		}
		
		map.put("adviceImgs", imgList);
		
		//加载上一章下一章信息
		List<Map> list = super.executeQuery("findPreAndNextAdviceInfo",parameters);
		
		for (Map m : list) {
			if(m.get("typeInfo").toString().equals("pre")){
				map.put("pre", m);
			}
			
			if(m.get("typeInfo").toString().equals("next")){
				map.put("next", m);
			}
		}
		**/
		
		return map;
	}
	public String formatName(String name) {
		
		if(StringUtil.isNotEmpty(name)) {
			name=name.substring(0, 1);
			name+="**";
		}
		return name;
	}
	@Override
	public List<Map> findAdviceReplyList(Map<String, Object> parameters,Paging paging)
			throws Exception {
		ExceptionLogger.writeLog("获取咨询问题留言列表");
		//加载咨询回复信息
		List<Map> listM = super.executeQuery("findAdviceReplyList", paging,parameters);
		//加载针评论的回复；
		if(null != listM && listM.size()>0) {
			parameters.put("businessType", BusinessType.REPLY_ADVICE);
			List<Map> listD = super.executeQuery("findAdviceReplyListNextReply", parameters);
			for(Object m_tmp: listM) {
				if( m_tmp instanceof Map) {
				Map m = (Map)m_tmp;
				
				
				int replyId = (int)m.get("replyId");
				//添加职业名称；
				String jobName="";
				Object job = m.get("job");
				if( null != job && !job.equals("")) {
					jobName = SystemConfigUtil.getValue(Integer.valueOf(job.toString()),SystemConfigUtil.TYPE_JOB);
				}
				m.put("jobName", jobName);
				List<Map<String,Object>> nextD= new ArrayList<Map<String,Object>>();
				if(null != listD && listD.size() > 0) {
					for(Map mb : listD) {
						int reD = (int)mb.get("relativeId");
						if(replyId == reD) {
							String nextJobName= "";
							Object nextJob = mb.get("job");
							if( null != nextJob && !nextJob.equals("")) {
								nextJobName = SystemConfigUtil.getValue(Integer.valueOf(nextJob.toString()),SystemConfigUtil.TYPE_JOB);
							}
							mb.put("jobName", nextJobName);
							nextD.add(mb);
						}
					}
				}
				m.put("nextReplyData", nextD);
				//添加总回复人数；
				m.put("replyNum", nextD.size());//总回复数；
				}
			}
		}
		return listM;
	}
	
	

	@Override
	public int deleteReplyLike(Map<String, Object> map) throws Exception {
		int  index = super.executeUpdate("deleteReplyLike", map);
		map.put("likeNum", -index);
		super.executeUpdate("updateLikeNum", map);
		int likeNum = (int)super.executeQueryOne("findLikeNumByReplyId", map.get("replyId"));
		return likeNum;
	}

	@Override
	public int replyLike(Map<String, Object> parameters) throws Exception {
		super.executeUpdate("addLike", parameters);
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("likeNum", 1);
		m.put("replyId", parameters.get("replyId"));
		super.executeUpdate("updateLikeNum", m);
		int likeNum = (int)super.executeQueryOne("findLikeNumByReplyId", parameters.get("replyId"));
		return likeNum;
	}
	
	

	@Override
	public List<Map> findLikeNumList(Map<String, Object> map) throws Exception {
	   return super.executeQuery("findLikeNumList",map);
	}

	@Override
	public List<Map> findAdviceByUserId(int userId) throws Exception {
		List list = super.executeQuery("findAdviceByUserId", userId);
		for (Object m : list) {
			if(m instanceof Map){
				Map map = (Map)m;
				map.put("caseTypeName", SystemConfigUtil.getValue((int)map.get("caseType"), SystemConfigUtil.TYPE_CASE));
			}
		}
		//获取所有的adviceId
		List<Integer> ids = new ArrayList<Integer>();
		
		for (Object m : list) {
			if(m instanceof Map){
				Map map = (Map)m;
				ids.add((int)map.get("adviceId"));
			}
		}
		
		//加载证据信息
		List<Map> imgs = super.executeQuery("findImgsByAdviceIds", ids.toArray());
		for (Object m : list) {
			if(m instanceof Map){
				Map map = (Map)m;
				
				List<String> imgList = new ArrayList<String>();
				
				for (Map mm : imgs) {
					
					if(mm.get("adviceId").equals(map.get("adviceId"))){
						imgList.add(mm.get("img").toString());
					}
					
				}
				map.put("adviceImgs", imgList);
			}
			
		}
		
		return list;
	}

	@Override
	public List<Map> myAnswerAdviceList(Map mymap,Paging paging) throws Exception {
		
		List<Map> list = super.executeQuery("myAnswerAdvice",paging,mymap);
		
		if(list!=null&&list.size()>0){
			
			int [] ids = new int[list.size()-1];
			int count = 0;
/*			for (Map map : list) {
				//注入咨询案件类型
				map.put("caseTypeName", SystemConfigUtil.caseMap.get("case"+map.get("caseType")).getCaseName());
				ids[count++] = (int)map.get("adviceId");
			}*/
			
			for (int i=0;i<list.size()-1;i++) {
				//注入咨询案件类型
				list.get(i).put("caseTypeName", SystemConfigUtil.caseMap.get("case"+list.get(i).get("caseType")).getCaseName());
				ids[count++] = (int)list.get(i).get("adviceId");
			}
			//加载图片
			//加载证据信息
			List<Map> imgs = super.executeQuery("findImgsByAdviceIds", ids);
			
			
			//for (Map m : list) {
				for (int j=0;j<list.size()-1;j++) {
					List<String> imgList = new ArrayList<String>();
					
					for (Map mm : imgs) {
						if(mm.get("adviceId").equals(list.get(j).get("adviceId"))){
							imgList.add(mm.get("img").toString());
						}
					}
					list.get(j).put("adviceImgs", imgList);
					
				
			}
			
			
			
		}
		
		
		return list;
	}

}
