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
import com.common.utils.SystemConfigUtil;
import com.legal.app.controller.model.Address;
import com.legal.app.controller.model.City;
import com.legal.app.model.CaseDepute;
import com.legal.app.service.CaseService;
import com.legal.app.service.OrderService;
import com.legal.app.service.UserService;
import com.legal.app.service.WalletRecordService;
import com.legal.app.utils.CreateOrderNumber;
import com.legal.app.utils.OrderType;
import com.legal.app.utils.PayOrder;
import com.legal.app.utils.Tools;
import com.legal.app.utils.VipUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class CaseServiceImpl extends DaoMybatisImpl<CaseDepute> implements CaseService{ 
	@Autowired
	private OrderService orderser;
	
	@Autowired
	private UserService userSer;
	

	@Autowired
	private WalletRecordService walletSer;
	

	@Override
	public List findCaseDepute(Map map) throws Exception {
		return super.executeQuery("findCaseDepute",map);
	}

	@Override
	public List list(Map map) throws Exception {
		return  super.executeQuery("list",map);
	}

	@Override
	public int deleteCase(int caseId) throws Exception {
		List list = this.listCaseOffer(caseId);
		if(null !=list && list.size() >0) {
			return 0;
		}else { 
			executeUpdate("deleteCase",caseId);
			return 1;
		}
	 
	}

	@Override
	public List listCaseOffer(int caseId) throws Exception { 
		return super.executeQuery("listCaseOffer", caseId);
	}

	@Override
	public List myCaseList(Map m, Paging paging) throws Exception {
		List listM = super.executeQuery("myCaseList", paging,m);
		if(null != listM && listM.size() > 0 ) {
			for(Object map : listM) {
				if(map instanceof Map) {
					Map p = (Map)map;
					 String caseTypeName = SystemConfigUtil.getValue((int)p.get("caseType"), SystemConfigUtil.TYPE_CASE);
					 p.put("caseTypeName", caseTypeName);
				}
			}
		}
		
		return listM;
	}

	@Override
	public int saveCaseDepute(Map map) throws Exception {
		
		ExceptionLogger.writeLog("发布案件委托");
		
		super.executeUpdate("saveCaseDepute", map);
		
		
		//案源证据图片不用单独保存到一张表，都保存到主表的   caseImgs 字段
		
		/**
		Map m = new HashMap();
		m.put("userId", map.get("userId"));
		
		Object caseImgs = map.get("caseImgs");
		
		if(caseImgs!=null){
			String [] imgs = (String [])caseImgs;
			m.put("caseImgs", imgs);
			super.executeUpdate("saveCaseDeputeImgs", m);
		}
		**/
		return 1;
	}

	@Override
	public List<Map> findCaseList(Map map, Paging paging) throws Exception {
		List list = super.executeQuery("findCaseList", paging, map);
		//加载案件类型
		for (Object obj : list) {
			if(obj instanceof Map){
				Map m = (Map)obj;
				m.put("caseTypeName", SystemConfigUtil.caseMap.get("case"+m.get("caseType")).getCaseName());
				String provinceName="";
				Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+m.get("province").toString());
				provinceName = add.name;
				Map cityM = add.getCity();
				City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+m.get("city").toString());
				String cityName = city.getCityName();
				m.put("provinceName", provinceName);
				m.put("cityName", cityName);
			}
		}
		
		return list;
	}

	@Override
	public Map findCaseDetailsInfo(Map map) throws Exception {
		
		//加载案源详情信息
	//	Map details = (Map)super.executeQueryOne("findCaseDetailsById", map.get("caseId"));
		Map m = (Map)super.executeQueryOne("caseDetails", map);
		String caseTypeName="";
		String lastCaseId ="";
		String lastCaseTitle="";
		String nextCaseId="";
		String nextCaseTitle="";
		String cityName="";
		String provinceName="";
		if(null != m) {
			String gt = (String)m.get("gt");
			if(null != gt) {
				String [] gt_tmp = gt.split("8m_m_8");
				nextCaseId = gt_tmp[0];
				if(gt_tmp.length > 1) {
					nextCaseTitle = null == gt_tmp[1] ? ""  : gt_tmp[1];
				}
			}  
			
			String lt = (String)m.get("lt");
			
			if(null != lt) {
				String  [] lt_tmp = lt.split("8m_m_8");
				lastCaseId = lt_tmp[0];
				if(lt_tmp.length > 1) {
					lastCaseTitle =null ==  lt_tmp[1] ? "" : lt_tmp[1];
				}
			}
			
			caseTypeName = SystemConfigUtil.getValue((int)m.get("caseType"), SystemConfigUtil.TYPE_CASE);
			Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+m.get("province"));
			provinceName = add.name;
			Map cityM = add.getCity();
			City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+m.get("city"));
			cityName = city.getCityName();
		}
		
		
		m.put("caseTypeName", caseTypeName);
		m.put("lastCaseId", lastCaseId);
		m.put("lastCaseTitle", lastCaseTitle);
		m.put("nextCaseId", nextCaseId);
		m.put("nextCaseTitle", nextCaseTitle);
		m.put("cityName", cityName);
		m.put("provinceName", provinceName);
		
		
		/**
		
		if(details==null||details.isEmpty()){
			throw new BusinessException("案件详情不存在.", -1);
		}
		details.put("caseTypeName", SystemConfigUtil.caseMap.get("case"+details.get("caseType")).getCaseName());
		//加载图片
		int [] caseIds = {(int)details.get("caseId")};
		List<Map> imgList = super.executeQuery("findCaseImgs", caseIds);
		
		if(imgList!=null&&imgList.size()>0){
			String [] imgs = new String[imgList.size()]; 
			int i = 0;
			for (Map m : imgList) {
				imgs[i++] = m.get("img").toString();
			}
			
			details.put("caseImgs", imgs);
		}
		
		//获取上一章  下一章  信息
		List<Map> preNext = super.executeQuery("findCaseDetailsPreAndNext", map);
		
		if(preNext!=null&&preNext.size()>0){
			
			for (Map pn : preNext) {
				if(pn.get("infoType").toString().equals("pre")){
					details.put("pre", pn);
				}
				
				if(pn.get("infoType").toString().equals("next")){
					details.put("next", pn);
				}
			}
			
		}
		
		**/
		return m;
	}

	@Override
	public int offerCase(Map map) throws Exception {
		ExceptionLogger.writeLog("咨询师竞拍案件...");
		super.executeUpdate("offerCase", map); 
		//发送站内信
	    //发站内消息给用户
	     Map<String, Object> msgMap1 = new HashMap<String, Object>();  
	     msgMap1.put("userId",map.get("sendPerson"));  
	     msgMap1.put("messageType",PayOrder.TYPE_AJWT); 
	     msgMap1.put("businessId",map.get("caseId"));
	     msgMap1.put("messageTitle",PayOrder.TYPE_AJWT);   
	     msgMap1.put("isRead","0"); //0:没读 1：已读
	     msgMap1.put("sendTime",new Date()); //0:没读 1：已读
	     msgMap1.put("isDelete","0"); //0:未删除 1：已删除
	     msgMap1.put("messageState","0"); //
	     msgMap1.put("is_send","1"); //0:未发送1：已发送
	     msgMap1.put("create_time",new Date()); 	 
	     msgMap1.put("messageContent","尊敬的用户，您发布的案件委托问题，已有咨询师参与竞拍，快去看看吧！点击直接跳转订单内容"); 
	     msgMap1.put("businessType","0");  
	     userSer.messageSend(msgMap1);
		return 1;
	}
	
	

	@Override
	public int updateCase(Map map) throws Exception {
		 return super.executeUpdate("updateCase", map);
	}

	@Override
	public Map employ(int offerId) throws Exception {
		Map orderM = new HashMap<String,Object>(); 
		ExceptionLogger.writeLog("聘请案件竞价者");
		//查询出这个竞拍是否存在；
		//把参与竞拍的数据查询出来 ；
		Map offerM = new HashMap<String,Object>();
		offerM.put("offerId", offerId);
		List offerList = this.list(offerM);
		if(null == offerList || offerList.size() == 0) {
			//没有查询到竞拍的信息；
			throw new BusinessException("聘请失败，参与竞拍的信息不存在.", -1); 
		}else {
				 Map  offerBaseM = (Map)offerList.get(0);
				 int caseId = (int)offerBaseM.get("caseId");
				 Map map = new HashMap<String,Object>();
				map.put("caseId",caseId);
				 //查看是否结案了。 
				 List<Map>  caseList = this.findCaseDepute(map);
				 if(null != caseList && caseList.size() >0) {
					 Map caseListToMap = caseList.get(0);
					 Object caseState = caseListToMap.get("caseState");
					 if(caseState != null && caseState.toString().equals("2")) {
						 throw new BusinessException("聘请失败，该案件已结案.", -1); 
					 }
				 }
				 
				 
				//查看这个案件是否已经聘请了，如果有了，就不聘请； 
				map.put("isSuccess",1);
				//查询出这个案件是否已经有聘请，
				List  list =  this.list(map);
				if(null == list || list.size() == 0 ) { 
					//查询这个案件 是否支付，没有产生订单先支付，支付后成功后，才可以聘请人员；
					 Map m = new HashMap<String,Object>();
				    m.put("businessType", PayOrder.TYPE_AJWT);
				    m.put("connectionId", caseId); 
					m.put("orderState","1");//查询订单还是在进行中
				    //查询这个支付订单是否存在
				    List listR =   orderser.findOrderDetails(m);
				    if(null != listR &&  listR.size()> 0 ) {
				    	orderM = (Map)listR.get(0);
				    	int isPay = (null == orderM.get("isPay") ?  2 :   (int)orderM.get("isPay"));
				    	//如果等于1 ，说明 这个订单已经充值到平台 ，现在就可以把回复咨询师的回复金发到回复人的帐户中；
				    	if(isPay == 1) { 
				    			//需要把订单的备注标上聘请的咨询师名称与ID
				    		    String remark = "";
				    		    Object toUserId = offerBaseM.get("userId");//被竞拍成功的咨询Id
				    		    remark=  (  null == offerBaseM.get("nickName") ?  "--" : offerBaseM.get("nickName").toString() )+":"+toUserId;
				    			orderM.put("remark", remark);
				    			//把聘请的受益人，写到 favoreeUserId 字段中去；
				    			orderM.put("favoreeUserId", toUserId);
				    			//修改订单的信息；
				    			orderser.update(orderM); 
				    			//修改竞拍 ，改为竞拍成功；
					    		super.executeUpdate("employ", offerId);
					    		//修改竞拍的状态为正在进行中；
					    		Map caseMap = new HashMap<String,Object>();
					    		caseMap.put("caseId", caseId);
					    		caseMap.put("caseState", "1");
					    	    this.updateCase(caseMap); 
					    		orderM.put("code",1);
					    		orderM.put("msg","聘请成功。");
					    		
					    		//为聘请者添加
					    		
					    		//发站内消息
					    		Map<String, Object> msgMap1 = new HashMap<String, Object>();
				   		         msgMap1.put("isRead","0"); //0:没读 1：已读
				   		         msgMap1.put("sendTime",new Date()); //0:没读 1：已读
				   		         msgMap1.put("isDelete","0"); //0:未删除 1：已删除
				   		         msgMap1.put("messageState","0"); //
				   		         msgMap1.put("is_send","1"); //0:未发送1：已发送
				   		         msgMap1.put("create_time",new Date()); 	
				   		         msgMap1.put("userId",offerBaseM.get("userId")); 
				   		         msgMap1.put("messageContent","尊敬的用户，您参与的案件委托竞拍已成功竞拍，快去看看吧！点击直接跳转订单内容");
				   		         msgMap1.put("messageType",orderM.get("sourceType"));
				   		         msgMap1.put("businessType","1");//0 : 回复消息类型  1：系统消息类型
				   		         msgMap1.put("businessId",orderM.get("connectionId").toString());
				   		         msgMap1.put("messageTitle",orderM.get("orderName"));  
				   		         userSer.messageSend(msgMap1);
				    		
				    	}else{
				    		//如果已产生了订单没有支付，返回页面去支付；
				    		orderM.put("code",2);
							orderM.put("msg","聘请失败，需要先支付。"); 
				    	}
				    }else {
				    	//查询出这个案件的详情；
				    	Map caseM = new HashMap<String,Object>();
				    	caseM.put("caseId",caseId);
				    	List listCaseD = this.findCaseDepute(caseM);
				    	if(null == listCaseD || listCaseD.size() == 0) {
				    		throw new BusinessException("聘请失败，案件不存在了.", -1);
				    	}else { 
				    		   Map caseDMap = (Map)listCaseD.get(0);
						    	//如果没有支付订单记录，产生一个支付订单，返回页面去支付；
						    	orderM = new HashMap<String,Object>();
								orderM.put("orderName", caseDMap.get("caseTitle")); 
								orderM.put("userId", caseDMap.get("userId")); 
								orderM.put("businessType", PayOrder.TYPE_AJWT);
								orderM.put("remark", "");
								orderM.put("orderNum", 1);
								orderM.put("orderType", OrderType.ORDERTYPE_1);
								
								//判断金额 如果是数字，就在订单中保存数字，否则就保存0  
								try {
									orderM.put("orderPrice", new BigDecimal(caseDMap.get("offerMoney").toString())); //赏金
								} catch (Exception e) { 
									orderM.put("orderPrice", 0); //赏金
								}
								orderM.put("connectionId", caseDMap.get("caseId"));
								orderM.put("sourceType", PayOrder.TYPE_AJWT);
								//orderM.put("checkTime", new Date()); 
								//orderM.put("endTime",); //结束时间 
								orderM.put("isPay", 2);  //是否支付  ，0：不需要支付      1： 已支付   2：未支付  3: 正在支付中  	4：支付失败
								orderM.put("evaluate", 2); //是否评价   0：不需要评价   1：已评价  2：未评价， 
								orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
								orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货 ；
								orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
								orderM.put("orderState", 1);//订单的状态； 1 ： 还在进行中， 0 ：已完成 
								orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败 
								//标记着，充值成功后需要执行时，需要的值；
								String sign = "{\"offerId\":\""+offerId+"\"}"; //需要把这个竞拍申请的Id保存进来；
								orderM.put("sign", sign);
								orderser.saveBusinessOrder(orderM);
								orderM.put("code",2);
								orderM.put("msg","聘请失败，需要先支付。"); 
				    	} 
				    }  
				}else {
					throw new BusinessException("聘请失败，已经聘请.", -1);
				}
		}
		return orderM;
	}

	@Override
	public List<Map> findCaseListByUserId(int userId) throws Exception {
		ExceptionLogger.writeLog("我的案件委托列表");
		return super.executeQuery("findCaseListByUserId", userId);
	}
	
	
	
    /**
     * 当案源发布者在 支付订单中，点确认完成按钮
     */
	@Override
	public int caseDeputeAccomplish(Map map) throws Exception {
		Object businessOrder = map.get("businessOrder");
		if(null == businessOrder || businessOrder.equals("")) {
			throw new BusinessException("操作失败.订单号不能为空.", -1);
		}
		//把根据订单编号把订单查询出来 
	    List listR =   orderser.findOrderDetails(map);
	    if(null != listR &&  listR.size()> 0 ) {
	    	Map  mapRe = (Map)listR.get(0);
	    	int isPay = (null == mapRe.get("isPay") ?  2 :   (int)mapRe.get("isPay"));
	    	//如果等于1 ，说明 这个订单已经充值到平台 ，现在就可以把回复咨询师的回复金发到回复人的帐户中；
	    	if(isPay == 1) {
	    		     Object  orderState = mapRe.get("orderState");
	    		     if(orderState != null && ((int)orderState) == 0 ) {
	    		    	 throw new BusinessException("操作失败，已经完成.", -1);
	    		     }
			    	
			        //修改订单的结果为完成；
					Map oldOrderM = new HashMap<String,Object>();
					oldOrderM.put("orderState", 0);//修改订单的状态为已完成；
					oldOrderM.put("businessOrder", businessOrder);
					oldOrderM.put("endTime", new Date());
					orderser.update(oldOrderM);
					
					/**
					//同时给咨询师写订单
					Map orderM = new HashMap<String,Object>();
					orderM.put("orderName", mapRe.get("orderName")); 
					orderM.put("userId", mapRe.get("favoreeUserId")); 
					orderM.put("businessType", PayOrder.TYPE_AJWTJP);
					orderM.put("remark", "");
					orderM.put("orderNum", 1);
					orderM.put("orderType", OrderType.ORDERTYPE_3);
					orderM.put("orderPrice", mapRe.get("orderPrice")); //金额
					orderM.put("connectionId", mapRe.get("connectionId"));
					orderM.put("sourceType", PayOrder.TYPE_AJWT);
					
					orderM.put("checkTime", new Date()); 
					orderM.put("endTime",new Date()); //结束时间 
					orderM.put("isPay", 1);  //是否支付  ，0：不需要支付      1： 已支付   2：未支付  3: 正在支付中  	4：支付失败
					orderM.put("evaluate", 0); //是否评价   0：不需要评价   1：已评价  2：未评价， 
					orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
					orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货 ；
					orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
					orderM.put("orderState", 0);//订单的状态； 1 ： 还在进行中， 0 ：已完成 
					orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败 
					orderser.saveBusinessOrder(orderM);
					**/
					
					//查询为这个案件竞拍 成功的用户信息； 
					Map counMap = userSer.findCounselorByUserId((int)mapRe.get("favoreeUserId"));
					if(null != counMap ) {
						//获取这个订单是多少赏金
			    		Double reward =Tools.rwipeOffDecimals( (null == mapRe.get("orderPrice") ? 0D :((BigDecimal)mapRe.get("orderPrice")).doubleValue()));
			    		//扣除系统费用；
			    		reward = VipUtils.sysTemCost(null, reward);
			    		
			    		//变更咨询师的钱包金额； 
						Double money = Tools.rwipeOffDecimals( (null == counMap.get("userBalance") ?  0D : ( (BigDecimal)counMap.get("userBalance")).doubleValue()) +reward);
						userSer.updateCounselorBalance((int)mapRe.get("favoreeUserId"), money); 
						
						//插入一条咨询师的交易流水记录； 
			    		Map walletM = new HashMap<String,Object>();
			    		walletM.put("userId", (int)mapRe.get("favoreeUserId"));
			    		walletM.put("businessOrder", mapRe.get("businessOrder"));
			    		walletM.put("balance", money);
			    		walletM.put("totalFee", reward);
			    		walletSer.saveWalletRecord(walletM); 
			    		
			    		//修改案件委托的状态为已完成；
			    		Map caseMap = new HashMap<String,Object>();
			    		caseMap.put("caseId", mapRe.get("connectionId"));
			    		caseMap.put("caseState", "2");
			    	    this.updateCase(caseMap);
			    	    
			    	  //发站内消息
			    		Map<String, Object> msgMap1 = new HashMap<String, Object>();
		   		        msgMap1.put("isRead","0"); //0:没读 1：已读
		   		        msgMap1.put("sendTime",new Date()); //0:没读 1：已读
		   		        msgMap1.put("isDelete","0"); //0:未删除 1：已删除
		   		        msgMap1.put("messageState","0"); //
		   		        msgMap1.put("is_send","1"); //0:未发送1：已发送
		   		        msgMap1.put("create_time",new Date()); 	
		   		        msgMap1.put("userId",mapRe.get("favoreeUserId")); 
		   		        msgMap1.put("messageContent","尊敬的用户，您参与的【"+mapRe.get("sourceType")+"】竞拍已结案，到帐"+reward+"元，快去看看吧！点击直接跳转订单内容");
		   		        msgMap1.put("messageType",mapRe.get("sourceType")+"赏金");
		   		        msgMap1.put("businessType","1");//0 : 回复消息类型  1：系统消息类型
		   		        //msgMap1.put("businessId",mapRe.get("connectionId").toString());
		   		        msgMap1.put("businessId",mapRe.get("businessOrder"));//需要跳转到订单详情
		   		        msgMap1.put("messageTitle",mapRe.get("orderName"));  
		   		        userSer.messageSend(msgMap1);
			    		
					}else {
						throw new BusinessException("操作失败.咨询师信息不存在",-1);
					} 
	    	
	    	}else {
	    		//否则 就是订单还没有充值到平台 ，不能回复 ；
	    		throw new BusinessException("订单还未支付.",-1);
	    	} 
	    	
	    }else {
	    	throw new BusinessException("订单不存在.",-1);
	    }
		
	
		
		
		
		return 0;
	}

	/**
	 * 竞拍的列表和竞拍的回复
	 */
	@Override
	public List<Map> findOfferList(Map map,Paging paging) throws Exception {
		ExceptionLogger.writeLog("案件竞拍列表");
		List<Map> mapL = super.executeQuery("findOfferList",paging, map);
		if(null != mapL && mapL.size() > 0) {
			List<Map> nextReplyList = super.executeQuery("findOfferListNextReply", map);
			if(null != nextReplyList) {
				for(Object ms : mapL) {
					if(ms instanceof Map) {
						Map m = (Map)ms;
						int offerId = (int)m.get("offerId");
						String jobName = "";
						Object job_tmp = m.get("job");
						if(null != job_tmp && job_tmp.toString().equals("")) {
							jobName = SystemConfigUtil.getValue(Integer.valueOf(job_tmp.toString()), SystemConfigUtil.TYPE_JOB);
						}
						m.put("jobName", jobName);
						List<Map> nextReplyListData = new ArrayList<Map>();
						for(Map ma : nextReplyList) {
							int relativeId = (int )ma.get("relativeId") ;
							if(offerId ==  relativeId) {
								Object nextJob= ma.get("job");
								String nextJobName = "";
								if(null !=nextJob && nextJob.toString().equals("")) {
									nextJobName = SystemConfigUtil.getValue(Integer.valueOf(nextJob.toString()),SystemConfigUtil.TYPE_JOB);
								}
								ma.put("jobName", nextJobName);
								nextReplyListData.add(ma);
							}
						}
						m.put("nextReplyListData", nextReplyListData);
					}
				}
			}			
		}
		return mapL;
	}

	@Override
	public List<Map> myOfferCaseList(Map mymap,Paging paging) throws Exception {
		
		List<Map> list = super.executeQuery("myOfferCase", paging,mymap);
		
		if(list!=null&&list.size()>0){
			
			int [] ids = new int[list.size()];
			int count = 0;
		/*	for (Map map : list) {
				map.put("caseTypeName", SystemConfigUtil.caseMap.get("case"+map.get("caseType")).getCaseName());
				ids[count++] = (int)map.get("caseId");
			}*/
			for (int i=0;i<list.size()-1;i++) {
				list.get(i).put("caseTypeName", SystemConfigUtil.caseMap.get("case"+list.get(i).get("caseType")).getCaseName());
				ids[count++] = (int)list.get(i).get("caseId");
			}
			
			
			List<Map> imgList = super.executeQuery("findCaseImgs", ids);
			
			if(imgList!=null&&imgList.size()>0){
				for (Map m : list) {
					
					List<String> caseImgs = new ArrayList<String>();
					
					for (Map mm : imgList) {
						if(m.get("caseId").equals(mm.get("caseId"))){
							
							caseImgs.add(mm.get("img").toString());
						}
					}
					
					m.put("caseImgs", caseImgs);
				}
			}
			
			
		}
		
		return list;
	}

	@Override
	public void casePrivateDeal(Map map) throws Exception {
		/**
		 * 案源委托时，私下成交，关闭这一订单
		 * 1，要检查这个案件委托有没有产生支付订单，如果有，就把支付订单取消
		 * 2.   把发布的案件委托的状态改为已成交； 
		  
		 */
		
		 //  1，要检查这个案件委托有没有产生支付订单，如果有，就把支付订单取消
		Map sourceMap = new HashMap<String ,Object>();
		sourceMap.put("sourceType", PayOrder.TYPE_AJWT);
		sourceMap.put("connectionId", map.get("caseId"));
		orderser.updateOrderByconnectionId(sourceMap);
		//2.   把发布的案件委托的状态改为已成交；  
		this.updateCase(map);
	}

	
	
}
