package com.legal.app.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.common.utils.SystemConfigUtil;
import com.legal.app.controller.model.Address;
import com.legal.app.controller.model.City;
import com.legal.app.model.BusinessOrder;
import com.legal.app.service.AdviceService;
import com.legal.app.service.ArticleService;
import com.legal.app.service.OrderService;
import com.legal.app.service.UserService;
import com.legal.app.service.WalletRecordService;
import com.legal.app.utils.PayOrder;
import com.legal.app.utils.VipUtils;

import net.sf.json.JSONObject;


@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class OrderServiceImpl extends DaoMybatisImpl<BusinessOrder> implements OrderService{
	@Autowired
	private UserService userSer;
	@Autowired
	private WalletRecordService walletSer;
	@Autowired
	private AdviceService adviceSer;
	
	@Autowired
	private ArticleService articleSer;
   
	@Override
	public int saveBusinessOrder(Map m) throws Exception {
		ExceptionLogger.writeLog("添加订单");
		//把合计算一下; 
		Object orderNum = m.get("orderNum");
		Object orderPrice = m.get("orderPrice");
		BigDecimal totalPrice = new BigDecimal(orderNum.toString()).multiply(new BigDecimal(orderPrice.toString())).setScale(2,BigDecimal.ROUND_HALF_UP);
		m.put("totalPrice", totalPrice);
		return super.executeUpdate("saveBusinessOrder", m);
	}
	
	public List  listBusinessOrder(Map map, Paging paging) throws Exception {
		 List list =  super.executeQuery("listBusinessOrder", paging, map);
		 if(null != list && list.size() > 0 ){
			 for(Object obj : list) {
				 if(obj instanceof  Map) {
					 Object userId_tmp = map.get("userId");
					 Integer userId = null;
					 if(null != userId_tmp) {
						 userId = Integer.valueOf((String)userId_tmp);
					 }
					 setOrderAttr(userId, (Map)obj); 
				 }
			 }
		 }
		 return list;
	}

	/**
	 * 根据查询的数据为每一个订单详情设置页面需要展示的数据；
	 * @param userId 查询参数的userId ,如果 没有，可以传入 null ,或者后台 管理员查询订单时，就可以传入  null;
	 * @param m  订单的详情
	 */
	private void setOrderAttr(Integer userId, Map  m) {
		String statusName = "";
		 String butA = "";
		 String butB = "";
		  
		 //因为订单返回的类型有区别；要会针对不同状态返回不同的名称；
		 String businessType = (String)m.get("businessType"); 

		 int  isPay = (int)m.get("isPay");
		 //如果已经支付
		 if(isPay == 1) {
			 //如果是法律咨询
			 int orderState = (int)m.get("orderState");
			 if(orderState == 2) {
				 //如果订单为 2   ，说明这个订单已经取消了；
				 statusName=PayOrder.STATUS_YQX; 
			 }else  if(businessType.equals(PayOrder.TYPE_FYZX)) {
					 //判断remark  为空 或者为"" 时。 butA 不显示评价按钮； butB 不需要显示
					 if(orderState == 0) {
						 statusName=PayOrder.STATUS_YWC;
					 }else {
						 statusName=PayOrder.STATUS_DJD;
					 }
					 String remark  = (String)m.get("remark");
					 if(null != remark &&  remark.length() > 0) {
						 String[] names = remark.split("/");
						 if(names.length<3) {
							 butA = "";
							 statusName=PayOrder.STATUS_YHF;
						 }else {
	 						 butA = PayOrder.STATUS_PJ;
						 }
					 } 
			 } else if(businessType.equals(PayOrder.TYPE_AJWT)){
						 //案件委托 
				 //	Object userId = map.get("userId"); //这是查询参数中的userId； 
				 	//有二种情况 ， 1： 当 查询参数 userId 为null 时，说明是后台管理员查询订单列表；
				 	if(null == userId) {
				 		//第一种情况 ：（后台管理人员查询）
				 		//判断，isComplain 是否等于 0 ；
				 		int isComplain = (int)m.get("isComplain");
				 		if(isComplain == 0 ) {
				 			//如果等于 0 ，就是没有申诉； 
							 if(orderState == 0) {
								 statusName=PayOrder.STATUS_YWC;
							 }else {
								 statusName=PayOrder.STATUS_WTCLZ;
							 }
				 		}else if(isComplain == 1){
				 			 //申诉中
				 			 statusName=PayOrder.STATUS_SSZ;
				 		}else if(isComplain == 2){
				 			 //申诉成功
				 			 statusName=PayOrder.STATUS_SSCG;
				 		}else if(isComplain == 1){
				 			 //申诉失败
				 			 statusName=PayOrder.STATUS_SSSB;
				 		}  
				 	}else {
				 		//第二种情况 ：有带自己userId 查询的；
				 		
				 		//带userId ,如果 订单中的userId = 查询参数中的userId，说明是发布人查询这个订单， 显示
				 		if(userId.toString().equals(m.get("userId").toString())) {
					 		//如果等于 0 ，就是没有申诉； 
							if(orderState == 0) {
								 statusName=PayOrder.STATUS_YWC;
								 butA =  PayOrder.STATUS_PJ;
							 }else {
								 statusName=PayOrder.STATUS_WTCLZ;
								 butA =  PayOrder.STATUS_QRWC;
								 butB = PayOrder.STATUS_DDYYY;
							 } 
				 		}else {
				 			 //否则就是案源竞拍者查询这个订单，不展示按钮；
				 			//如果等于 0 ，就是没有申诉； 
							if(orderState == 0) {
								 statusName=PayOrder.STATUS_YWC; 
							 }else {
								 statusName=PayOrder.STATUS_WTCLZ; 
							 } 
				 		}
				 		
				 } 	 
			} else if (businessType.equals(PayOrder.TYPE_FYWSZZ) ||businessType.equals(PayOrder.TYPE_XYYSF) ){
						//法律文书制作  
					 	//Object userId = map.get("userId"); //这是查询参数中的userId；

					 	int userIdData = Integer.parseInt(m.get("userId").toString());//数据库中查询的订单生成人的ID；
					 	int favoreeUserId = Integer.parseInt(m.get("favoreeUserId").toString());//这是数据库中 受益人的ID；

					 	//有三种情况 ， 1： 当 查询参数 userId 为null 时，说明是后台管理员查询订单列表；
					 	if(null == userId) {
					 		//第一种情况 ：（后台管理人员查询）
					 		//判断，isComplain 是否等于 0 ；
					 		int isComplain = (int)m.get("isComplain");
					 		if(isComplain == 0 ) {
					 			//如果等于 0 ，就是没有申诉； 
								 if(orderState == 0) {
									 statusName=PayOrder.STATUS_YWC;
								 }else { 
									 statusName=PayOrder.STATUS_WTCLZ;
								 }
					 		}else if(isComplain == 1){
					 			 //申诉中
					 			 statusName=PayOrder.STATUS_SSZ;
					 		}else if(isComplain == 2){
					 			 //申诉成功
					 			 statusName=PayOrder.STATUS_SSCG;
					 		}else if(isComplain == 3){
					 			 //申诉失败
					 			 statusName=PayOrder.STATUS_SSSB;
					 		}  
					 	}else if((int)userId == userIdData) {
					 		//第二种情况 ： 查询的userId  等于 订单生成人的 Id；
					 		//如果等于 0 ，就是没有申诉； 
							 if(orderState == 0) {
								 statusName=PayOrder.STATUS_YWC;
								 butA =  PayOrder.STATUS_PJ;
							 }else {
								 //是否发货
								 int sender = (int)m.get("sender");
								 if(sender == 1) {
									 //还没有发货；
									 statusName=PayOrder.STATUS_WS_DDFH; 
								 }else if(sender ==2) {
									 //已经发货 （制作人已经发货）
									 //判断自己有没有收货；
									 int  reception = (int)m.get("reception");
									 if(reception == 1) {
										 //别人已经发货，自己还没有收货；
										 statusName=PayOrder.STATUS_WS_DSH;
										 butA =  PayOrder.STATUS_WS_QRSH;
										 butB = PayOrder.STATUS_DDYYY;
									 }else if(reception ==2) {
										 statusName=PayOrder.STATUS_YWC;
									 }
								 } 
							 } 
					 }else if((int)userId == favoreeUserId ) {
						 //第三种情况  ， 查询人userId 等于 受益人； 
						 if(orderState == 0) {
							 statusName=PayOrder.STATUS_YWC;
						 }else {
							 //是否发货
							 int sender = (int)m.get("sender");
							 if(sender == 1) {
								 //还没有发货；
								 statusName=PayOrder.STATUS_WS_DDFH;
								 butA =  PayOrder.STATUS_WS_QRFH;
							 }else if(sender == 2) {
								 //如果已经发货，看对方有没有收货；
								 int  reception = (int)m.get("reception");
								 if(reception == 1) {
									 statusName=PayOrder.STATUS_WS_DSH;
								 }else if(reception == 2) {
									 statusName=PayOrder.STATUS_WS_YSH;
								 }
								 statusName=PayOrder.STATUS_YWC;
							 }
						 } 
					 } 	 
				
			}else if(businessType.equals(PayOrder.TYPE_DS)) {
				//打赏  
				 if(orderState == 0) {
					 statusName=PayOrder.STATUS_YWC;
				 }else {
					 statusName=PayOrder.STATUS_JXZ;
				 }
			}else if(businessType.equals(PayOrder.TYPE_HYCZ)) {
				//充值 
				 if(orderState == 0) {
					 statusName=PayOrder.STATUS_ZF_YZF;
				 }else {
					 statusName=PayOrder.STATUS_JXZ;
				 }
			}else if(businessType.equals(PayOrder.TYPE_TX)) {
				//如果类型等于提现，
				 if(orderState == 0) {
					 statusName=PayOrder.STATUS_TX_TXCG;
				 }else {
					 statusName=PayOrder.STATUS_JXZ;
				 }
			}
		 }else if(isPay == 2) {
			 //如果没有支付，就需要显示 支付功能；
			 statusName = PayOrder.STATUS_ZF_WZF; //状态显示未支付；
			 //如果受益人等于 查询的人的userId ,就不需要展示支付按钮和取消订单按钮 
			 Object favoreeUserId = m.get("favoreeUserId");//这是数据库中 受益人的ID；
			 if( null != userId &&  null != favoreeUserId && userId.toString().equals(favoreeUserId.toString())) {
				 statusName = "委托人未支付";
				 butA =  "";//按钮A 不显示 去支付
				 butB =  "";//按钮B 不显示取消订单 
			 }else {
				 butA =  PayOrder.STATUS_ZF_QZF;//按钮A 显示 去支付
				 butB =  PayOrder.STATUS_ZF_QXDD;//按钮B 显示取消订单 
			 }
		 }else if(isPay == 3) {
			 statusName = PayOrder.STATUS_ZF_ZFZ; //状态显示支付中； 
		 }else if(isPay == 4) {
			  if(businessType.equals(PayOrder.TYPE_TX)) {	 
					 statusName = PayOrder.STATUS_TX_TXSB;  //提现失败
			  }else {
				  statusName = PayOrder.STATUS_ZF_ZFSB;  // 支付失败
			  }
		 }else if(isPay == 5) {
			 statusName = PayOrder.STATUS_ZF_YTK;  //已退款
		 }else if(isPay == 6) {
		    if(businessType.equals(PayOrder.TYPE_TX)) {	 
			 statusName = PayOrder.STATUS_ZF_DSH;  //待审核
		    }
		 }else {
			 //否则显示已完成；
			 statusName=PayOrder.STATUS_YWC;
		 } 
		 
		 
		 //判断状态
		 //如果这个订单是有异议的话，就不能显示完成按钮；并且，订单有异议显示为申诉中
		Object  isComplain =  m.get("isComplain") ;
		if( null != isComplain) {
			//如果是在申诉中
			if(isComplain.toString().equals("1")) {
				//状态改为待审核
				statusName = PayOrder.STATUS_ZF_DSH;
				//按钮一不显示；
				butA="";
				//按钮二显示申诉中
				butB=PayOrder.STATUS_SSZ;
			}else if(isComplain.toString().equals("2")) {
				//状态改为申诉成功
				statusName = PayOrder.STATUS_SSCG;
				//按钮一不显示；
				butA="";
				//按钮二不显示
				butB="";
			}else if(isComplain.toString().equals("3")) {
				//状态改为申诉失败
				statusName = PayOrder.STATUS_SSSB;
				//按钮一不显示；
				butA="";
				//按钮二不显示
				butB="";
			}
		}
		 
		 //支付状态；
		 m.put("statusName", statusName);
		 m.put("butA", butA);
		 m.put("butB", butB);
	}

	@Override
	public List findOrderDetails(Map map) throws Exception { 
		List list;
		Map nmap = new HashMap();
		nmap.put("businessOrder", map.get("businessOrder"));
		if(map.get("businessOrder")==null || map.get("businessOrder").equals("")) {
			list = super.executeQuery("findOrderDetails", map);
		}else {
			list = super.executeQuery("findOrderDetails", nmap);
		}
		 if(null != list && list.size() > 0 ){
			
			 for(Object obj : list) {
				 if(obj instanceof  Map) {
					 Object userId_tmp = map.get("userId");
					 Integer userId = null;
					 Map map_tmp = (Map)obj;
					 List userList = new ArrayList<Map>();
					 map_tmp.put("userList", userList);
					 if(null != userId_tmp) {
						 userId = Integer.valueOf(userId_tmp.toString());
					 }  
					setOrderAttr(userId, map_tmp); 
					
					//根据订单的 类型来 查询；订单的详情；
					//如果是等于 会员充值；
					
					String businessType = (String)map_tmp.get("businessType");
					//如果
				if(businessType.equals("法律咨询")) {
						//如果类型为法律咨询的；需要看remk回复的有几个人；再根据Id，查询出这几个人的信息；
						String  remark = (String)map_tmp.get("remark");
						if(null != remark && !remark.equals("")) {
							String [] userIds = remark.split("/");
							for(String val : userIds) {
								String [] user = val.split(":");
								Map userMap =  userSer.getUserInfo(Integer.valueOf(user[1]));
								Map mapR =this.setUserInfo(userMap);
								userList.add(mapR);
							}
						}
					}else if(
							businessType.equals("法律文书制作") || 
							businessType.equals("案件委托") ||
							businessType.equals("协议律师费")||
							businessType.equals("打赏")
							
						) {
						//取受益人的id返回出去；
						 Object favoreeUserId = 	map_tmp.get("favoreeUserId");
						 if(null != favoreeUserId) {
								Map userMap =  userSer.getUserInfo(Integer.valueOf(favoreeUserId.toString()));
								//如果用户类型不是咨询师；
								if(userMap.get("userType").toString().equals("2")) {
									Map mapR =this.setUserInfo(userMap);
									userList.add(mapR);
								}
						 }
					}else if(businessType.equals("会员充值")){
						//如果是充值，就不需要展示咨询师信息内容；
						
					}else {
						 Object userIdOrder = 	map_tmp.get("userId"); 
						Map userMap =  userSer.getUserInfo(Integer.valueOf(userIdOrder.toString()));
						Map mapR =this.setUserInfo(userMap);
						userList.add(mapR); 
					}
					
			       if(userList.size() == 0 ) {
			    	    Map defaultUser = new HashMap<String,Object>(); 
			    	    defaultUser.put("userName", "--");
			    	    defaultUser.put("realName", "--");
			    	    defaultUser.put("mobile", "--");
			    	    defaultUser.put("province", "--");
			    	    defaultUser.put("userId", "--"); 
						defaultUser.put("provinceName",  "--");  
						defaultUser.put("city", "--"); 
						defaultUser.put("cityName", "--");
						userList.add(defaultUser);
			       }
				 }
			 }
		 }
		return list;
	}
	
	/**
	 * 把查询出的user信息，过滤些数据，不要返回出去
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private Map setUserInfo(Map map) throws Exception{
		  Map<String,Object> m = new HashMap<String,Object>();  
			//返回省份的名称
			m.put("userName", map.get("userName"));
			m.put("realName", map.get("realName"));
			m.put("mobile", map.get("mobile"));
			m.put("province", map.get("province"));
			m.put("userId", map.get("userId"));
			
			//如果省份为null 就不需要返回省份和城市 
			 if(null != map.get("province")) {
				String provinceName="";
				Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+map.get("province").toString());
				provinceName = add.name; 
				m.put("provinceName",  provinceName); 
				//返回城市的名称 
				m.put("city", map.get("city"));
				Map cityM = add.getCity();
				City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+map.get("city"));
				String cityName = city.getCityName();   
				m.put("cityName", cityName);
		    } 
			 
			 if(m.get("cityName")  == null || m.get("cityName").toString().equals("")) {
				 m.put("cityName", "--");
			 }
			 
			 if(m.get("provinceName")  == null || m.get("provinceName").toString().equals("")) {
				 m.put("provinceName", "--");
			 }
			 if(m.get("mobile")  == null || m.get("mobile").toString().equals("")) {
				 m.put("mobile", "--");
			 }
		return m;
	}
	
	

	@Override
	public  Object  findOrderDetails(String businessOrder) throws Exception { 
		Map m = new HashMap<String,Object>();
		m.put("businessOrder", businessOrder);
		return super.executeQueryOne("findOrderDetails", m); 
	}

	@Override
	public int update(Map map) throws Exception { 
		return  super.executeUpdate("updateBusinessOrder",map);
	}

	@Override
	public List listOrderEvaluate(Map map) throws Exception {
		return super.executeQuery("listOrderEvaluate", map);
	}

	@SuppressWarnings("unused")
	@Override
	public void orderPaySuccess(String businessOrder, String outTradeNo, int type,String openid) throws Exception {
		ExceptionLogger.writeLog("支付回调： 平台订单号："+businessOrder+" 和 支付订单号："+outTradeNo +"  type:"+type +" openid:"+openid);
		 //查询这个订单是否存在；
		Map m = new HashMap<String,Object>();
		m.put("businessOrder", businessOrder); 
		Object order= super.executeQueryOne("findOrderDetails", m); 
		if(null == order ) {
			ExceptionLogger.writeLog("会员回调处理失败： 查询订单号："+businessOrder+" 和 支付订单号："+outTradeNo +" 不存在。");
			throw new BusinessException("订单不存在.", -1); 
		}
		//查询这个订单是还是在处理中，如果不是，说明已经处理了，就不能再次处理；
		Map orderM =(Map)order;
		if(orderM.get("isPay").toString().equals("3") || orderM.get("isPay").toString().equals("2")) {
			//等于3，说明，这个订单是向支付公司提交过，正在处理中； 或者等于 2 ，还没有支付过的；
		}else {
			ExceptionLogger.writeLog("会员回调处理失败： 订单号："+businessOrder+" 和 支付订单号："+outTradeNo +"  状态为:"+orderM.get("isPay"));
			throw new BusinessException("订单状态不是在支付中，是."+orderM.get("isPay"), -1); 
		}
		
		//根据订单的业务，来进行；
		String businessType = (String)orderM.get("businessType"); 
		Integer orderState = null;
		Integer isPay = null; 
		Date  endTime = null;
		Date  checkTime = null;
		Object favoreeUserId = null;
		Object remark = null;
		Map saveOrderMap = new HashMap<String,Object>();
		if(businessType.equals(PayOrder.TYPE_HYCZ)) { 
			if(type == 0) {
				//1.成功
			          //如果是会员充值；
		         	orderVipRecharge(orderM); 
		         	isPay = 1;
			}else {
				//2.失败 
				isPay = 4;
			}
			 orderState = 0 ;
			 checkTime = new Date();
			 endTime = new Date();
		
		}else if(businessType.equals(PayOrder.TYPE_FYZX)) {
			//如果是法律咨询；
			if(type == 0) {
				//1.成功  
		         isPay = 1;
		         //成功后，还要把法律咨询这个文章发布出去， 
		         Map advMap = new HashMap<String,Object>();
		         advMap.put("state", "0");
		         advMap.put("adviceId",orderM.get("connectionId").toString());
		         this.adviceSer.updateAdvice(advMap);
		         Map userInfoMap=(Map)this.userSer.executeQueryOne("findUserByUserId",Integer.parseInt(orderM.get("userId").toString()));
		         
		         if((int)Math.round(Double.parseDouble(orderM.get("totalPrice").toString()))>=1) {					
					 int nowIgl;
					 if(null==userInfoMap.get("integral")) {
						 nowIgl=0;
					 }else {
						 nowIgl=Integer.parseInt(userInfoMap.get("integral").toString());
					 }
					 Map userIntMap = new HashMap<String,Object>();
					 userIntMap.put("integral", nowIgl+(int)Math.round(Double.parseDouble(orderM.get("totalPrice").toString())));						
					 userIntMap.put("userId", userInfoMap.get("userId").toString());
					 userSer.updateUserIntegral(userIntMap);
				 }
		         //判断发站内消息；
		         Map<String, Object> msgMap = new HashMap<String, Object>();
		         msgMap.put("isRead","0"); //0:没读 1：已读
		         msgMap.put("sendTime",new Date()); //0:没读 1：已读
		         msgMap.put("isDelete","0"); //0:未删除 1：已删除
		         msgMap.put("messageState","0"); //
		         msgMap.put("is_send","1"); //0:未发送1：已发送
		         msgMap.put("create_time",new Date()); 	
		         msgMap.put("userId",orderM.get("userId")); 
		         msgMap.put("messageContent","尊敬的用户，您的法律咨询赏金（"+orderM.get("totalPrice")+"），充值成功！");
		         msgMap.put("messageType",orderM.get("sourceType")+"赏金");
		         msgMap.put("businessType","1");
		        // msgMap.put("businessId",orderM.get("connectionId").toString());
		         msgMap.put("businessId",orderM.get("businessOrder"));//充值需要跳转到订单详情中去
		         msgMap.put("messageTitle",orderM.get("orderName"));  
		         userSer.messageSend(msgMap);	
		         
		         Object sign = orderM.get("sign");
		         if(null != sign && !sign.equals("")) {
		        	 JSONObject sig = JSONObject.fromObject(sign);
		        	Object privateUserId =  sig.get("privateUserId");
		        	if(null != privateUserId && !privateUserId.equals("")) {
		        		int userId = Integer.valueOf(privateUserId.toString());
		        		if(userId >0) {
		        		 //判断发站内消息；
		   		         Map<String, Object> msgMap1 = new HashMap<String, Object>();
		   		         msgMap1.put("isRead","0"); //0:没读 1：已读
		   		         msgMap1.put("sendTime",new Date()); //0:没读 1：已读
		   		         msgMap1.put("isDelete","0"); //0:未删除 1：已删除
		   		         msgMap1.put("messageState","0"); //
		   		         msgMap1.put("is_send","1"); //0:未发送1：已发送
		   		         msgMap1.put("create_time",new Date()); 	
		   		         msgMap1.put("userId",userId); 
		   		         msgMap1.put("messageContent","尊敬的用户，您收到了新的法律咨询订单，快去看看吧！点击直接跳转订单内容");
		   		         msgMap1.put("messageType",orderM.get("sourceType"));
		   		         msgMap1.put("businessType","1");
		   		         msgMap1.put("businessId",orderM.get("connectionId").toString());
		   		         msgMap1.put("messageTitle",orderM.get("orderName"));  
		   		         userSer.messageSend(msgMap1);
		   		         
		   		          Map userMap =  this.userSer.getUserInfo(Integer.valueOf(privateUserId.toString()));
		   		         //发短信 给这信咨询师；
		   		        Object mobile = userMap.get("mobile");
			   		        if(null != mobile) {
			   		        	SMSUtilLiantong.sendCounselor(mobile.toString());
			   		        }else {
			   		        		ExceptionLogger.writeLog("ID为："+favoreeUserId+" 的会员，没有电话号码，没有发信息");
			   		        }
		        		}
		        	}
		         }
		         
		         
			}else {
				//2.失败 
				isPay = 4;
			}
			checkTime = new Date();
		}else if(businessType.equals(PayOrder.TYPE_DS)) {
			//如果是打赏
			if(type == 0) {
				//1.成功  
		         isPay = 1;
		         //打赏成功需要更改 数据的打赏数量；
		         this.orderRewardSuccess(orderM);
		       //如果订单来源为法律咨询的打赏；需要取订单数据中的 sign
			    String sign = orderM.get("sign").toString();
			    JSONObject signM = JSONObject.fromObject(sign);
		         // 设置favoreeUserId id 
		         favoreeUserId = signM.get("favoreeUserId");
		         Map userMap =  this.userSer.getUserInfo(Integer.valueOf(favoreeUserId.toString()));
		         Map userInfoMap=(Map)this.userSer.executeQueryOne("findUserByUserId",Integer.parseInt(orderM.get("userId").toString()));
		         BigDecimal totalPrice =new BigDecimal(orderM.get("totalPrice").toString()); 
		        //扣除系统费用后
		         Double systemCost =  VipUtils.sysTemCost(PayOrder.TYPE_DS, totalPrice.doubleValue());
		    	 BigDecimal totalFee = new BigDecimal(systemCost);
		    	 
		         //如果是被打赏用户是咨询师，就需要写钱包流水
		    	Object userType =  userMap.get("userType");
		         if(userType.toString().equals("2")) {
			         remark = userMap.get("realName")+":"+favoreeUserId;   
			         //修改钱包金额 ； 
			    	 BigDecimal userBalance = new BigDecimal(userMap.get("userBalance").toString()); //用户的余额
					 Double balance =userBalance.add(totalFee).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue(); 
					 //TODO修改打赏后的用户积分
					 if((int)Math.round(Double.parseDouble(orderM.get("totalPrice").toString()))>=1) {
						/* if(Integer.parseInt(orderM.get("totalPrice").toString())>=1) { */
						 //User u = new User();
						 int nowIgl;
						 if(null==userInfoMap.get("integral")) {
							 nowIgl=0;
						 }else {
							 nowIgl=Integer.parseInt(userInfoMap.get("integral").toString());
						 }
						 Map userIntMap = new HashMap<String,Object>();
						 userIntMap.put("integral", nowIgl+(int)Math.round(Double.parseDouble(orderM.get("totalPrice").toString())));
						 //userIntMap.put("integral", nowIgl+1);
						 //userIntMap.put("integral", 11);
						 userIntMap.put("userId", userInfoMap.get("userId").toString());
						 //u.setIntegral(nowIgl+(int)Math.round(Double.parseDouble(orderM.get("totalPrice").toString())));
						 userSer.updateUserIntegral(userIntMap);
					 }
					 //修改咨询师余额
					 Map userbanace = new HashMap<String,Object>();
					 userbanace.put("userId", userInfoMap.get("userId").toString());
					 userbanace.put("userBalance", balance);
					 userSer.executeUpdate("updateCounselorBalance", userbanace);
					 Map walletM = new HashMap<String,Object>();
		    		 walletM.put("userId", favoreeUserId);
		    		 walletM.put("businessOrder", orderM.get("businessOrder"));
		    		 walletM.put("balance",balance);
		    		 walletM.put("totalFee", totalPrice);
			         //写钱包流水；
		    		 walletSer.saveWalletRecord(walletM);   
		         }else {
		        	 remark = VipUtils.companyName;   
		         }
	    		 
		         //发送站内信（二个人）；
		         
	    		//发送站内通知； (打赏人员)
    			Map<String, Object> msgMap1 = new HashMap<String, Object>();
    	       msgMap1.put("isRead","0"); //0:没读 1：已读
    	       msgMap1.put("sendTime",new Date()); //0:没读 1：已读
    	       msgMap1.put("isDelete","0"); //0:未删除 1：已删除
    	       msgMap1.put("messageState","0"); //
    	       msgMap1.put("is_send","1"); //0:未发送1：已发送
    	       msgMap1.put("create_time",new Date()); 	
    	       msgMap1.put("userId",orderM.get("userId")); 
    	       msgMap1.put("messageContent","尊敬的用户，您的"+orderM.get("sourceType")+"【"+orderM.get("businessType")+"】金额（"+totalPrice+"）已充值成功并且打赏成功。");
    	       msgMap1.put("messageType",(orderM.get("sourceType").toString()+orderM.get("businessType")));
    	       msgMap1.put("businessType","1");
    	      // msgMap1.put("businessId",orderM.get("connectionId"));打赏需要跳转到订单详情中去
    	       msgMap1.put("businessId",orderM.get("businessOrder"));
    	       msgMap1.put("messageTitle",orderM.get("orderName"));  
    	       userSer.messageSend(msgMap1);
    	      
    	       //被打赏人员
    	       Map<String, Object> msgMap2 = new HashMap<String, Object>();
    	       msgMap2.put("isRead","0"); //0:没读 1：已读
    	       msgMap2.put("sendTime",new Date()); 
    	       msgMap2.put("isDelete","0"); //0:未删除 1：已删除
    	       msgMap2.put("messageState","0"); //
    	       msgMap2.put("is_send","1"); //0:未发送1：已发送
    	       msgMap2.put("create_time",new Date()); 	
    	       msgMap2.put("userId",favoreeUserId); 
    	       msgMap2.put("messageContent","尊敬的用户，有会员在【"+orderM.get("sourceType")+"】中为您进行打赏,金额（"+systemCost+"）已成功到帐，快去看看吧！点击直接跳转订单内容。");
    	       msgMap2.put("messageType",(orderM.get("sourceType").toString()+orderM.get("businessType")));
    	       msgMap2.put("businessType","1");
    	       //msgMap2.put("businessId",orderM.get("connectionId"));
    	       msgMap2.put("businessId",orderM.get("businessOrder")); //打赏需要跳转到订单的详情，
    	       msgMap2.put("messageTitle",orderM.get("orderName"));  
    	       userSer.messageSend(msgMap2);
	    		 
		         
			}else {
				//2.失败 
				isPay = 4;
			}
			 orderState = 0 ;
			 checkTime = new Date();
			 endTime = new Date();
		}else if(businessType.equals(PayOrder.TYPE_FYWSZZ)) {
			//如果是法律文书制作
			if(type == 0) {
				//1.成功  
		         isPay = 1;
		         saveOrderMap.put("sender", 1);
		         favoreeUserId = orderM.get("favoreeUserId");
		         Map userMap =  this.userSer.getUserInfo(Integer.valueOf(favoreeUserId.toString()));
		         remark = userMap.get("realName")+":"+favoreeUserId;  
		         
		         Map userInfoMap=(Map)this.userSer.executeQueryOne("findUserByUserId",Integer.parseInt(orderM.get("userId").toString()));
		         
		         if((int)Math.round(Double.parseDouble(orderM.get("totalPrice").toString()))>=1) {					
					 int nowIgl;
					 if(null==userInfoMap.get("integral")) {
						 nowIgl=0;
					 }else {
						 nowIgl=Integer.parseInt(userInfoMap.get("integral").toString());
					 }
					 Map userIntMap = new HashMap<String,Object>();
					 userIntMap.put("integral", nowIgl+(int)Math.round(Double.parseDouble(orderM.get("totalPrice").toString())));						
					 userIntMap.put("userId", userInfoMap.get("userId").toString());
					 userSer.updateUserIntegral(userIntMap);
				 }
		         //站内信；二个人；
		       //发送站内通知； (打赏人员)
	    			Map<String, Object> msgMap1 = new HashMap<String, Object>();
	    	       msgMap1.put("isRead","0"); //0:没读 1：已读
	    	       msgMap1.put("sendTime",new Date()); //0:没读 1：已读
	    	       msgMap1.put("isDelete","0"); //0:未删除 1：已删除
	    	       msgMap1.put("messageState","0"); //
	    	       msgMap1.put("is_send","1"); //0:未发送1：已发送
	    	       msgMap1.put("create_time",new Date()); 	
	    	       msgMap1.put("userId",orderM.get("userId")); 
	    	       msgMap1.put("messageContent","尊敬的用户，您的【"+orderM.get("businessType")+"】费用（"+orderM.get("userId")+"）支付成功。");
	    	       msgMap1.put("messageType",orderM.get("sourceType"));
	    	       msgMap1.put("businessType","1");
	    	       msgMap1.put("businessId",orderM.get("businessOrder"));//法律文书制作费用需要转到订单详情
	    	       msgMap1.put("messageTitle",orderM.get("orderName"));  
	    	       userSer.messageSend(msgMap1);
	    	      
	    	       //被打赏人员
	    	       Map<String, Object> msgMap2 = new HashMap<String, Object>();
	    	       msgMap2.put("isRead","0"); //0:没读 1：已读
	    	       msgMap2.put("sendTime",new Date()); 
	    	       msgMap2.put("isDelete","0"); //0:未删除 1：已删除
	    	       msgMap2.put("messageState","0"); //
	    	       msgMap2.put("is_send","1"); //0:未发送1：已发送
	    	       msgMap2.put("create_time",new Date()); 	
	    	       msgMap2.put("userId",favoreeUserId); 
	    	       msgMap2.put("messageContent","尊敬的用户，收到【"+orderM.get("sourceType")+"】的申请，快去看看吧！点击直接跳转订单内容。");
	    	       msgMap2.put("messageType",orderM.get("sourceType"));
	    	       msgMap2.put("businessType","1");
	    	       msgMap2.put("businessId",orderM.get("businessOrder"));//法律文书制作费用需要转到订单详情
	    	       msgMap2.put("messageTitle",orderM.get("orderName"));  
	    	       userSer.messageSend(msgMap2);
		         
		         
			}else {
				//2.失败 
				isPay = 4;
			}
			//需要把支付的时间写一下；
			 checkTime = new Date();
		}else if(businessType.equals(PayOrder.TYPE_AJWT)) {
			//案件委托
			if(type == 0) {
				//1.成功  
		         isPay = 1; 
		         
		         Map userInfoMap=(Map)this.userSer.executeQueryOne("findUserByUserId",Integer.parseInt(orderM.get("userId").toString()));

		         if((int)Math.round(Double.parseDouble(orderM.get("totalPrice").toString()))>=1) {					
		        						 int nowIgl;
		        						 if(null==userInfoMap.get("integral")) {
		        							 nowIgl=0;
		        						 }else {
		        							 nowIgl=Integer.parseInt(userInfoMap.get("integral").toString());
		        						 }
		        						 Map userIntMap = new HashMap<String,Object>();
		        						 userIntMap.put("integral", nowIgl+(int)Math.round(Double.parseDouble(orderM.get("totalPrice").toString())));						
		        						 userIntMap.put("userId", userInfoMap.get("userId").toString());
		        						 userSer.updateUserIntegral(userIntMap);
		        					 }
		         //发送站内信给充值的人员； 
	    		   Map<String, Object> msgMap1 = new HashMap<String, Object>();
	    	       msgMap1.put("isRead","0"); //0:没读 1：已读
	    	       msgMap1.put("sendTime",new Date()); //0:没读 1：已读
	    	       msgMap1.put("isDelete","0"); //0:未删除 1：已删除
	    	       msgMap1.put("messageState","0"); //
	    	       msgMap1.put("is_send","1"); //0:未发送1：已发送
	    	       msgMap1.put("create_time",new Date()); 	
	    	       msgMap1.put("userId",orderM.get("userId")); 
	    	       msgMap1.put("messageContent","尊敬的用户，您的【"+orderM.get("businessType")+"】（"+orderM.get("totalPrice")+"）已支付成功。");
	    	       msgMap1.put("messageType",orderM.get("sourceType")+"赏金");
	    	       msgMap1.put("businessType","1");
	    	       msgMap1.put("businessId",orderM.get("businessOrder")); //协议律师费用需要转到订单详情
	    	       msgMap1.put("messageTitle",orderM.get("orderName"));  
	    	       userSer.messageSend(msgMap1);
		         
			}else {
				//2.失败 
				isPay = 4;
			}
			 checkTime = new Date();
		}else if(businessType.equals(PayOrder.TYPE_XYYSF)) { 
			if(type == 0) {
				//1.成功  
		         isPay = 1; 
	         	String sign = orderM.get("sign").toString();
			    JSONObject signM = JSONObject.fromObject(sign);
		         // 设置favoreeUserId id 
		         favoreeUserId = signM.get("favoreeUserId");
		         Map userMap =  this.userSer.getUserInfo(Integer.valueOf(favoreeUserId.toString()));
		         remark = userMap.get("realName")+":"+favoreeUserId;  
		         saveOrderMap.put("sender", 1);
		         saveOrderMap.put("evaluate", 2);
		         saveOrderMap.put("reception", 1);
		         saveOrderMap.put("favoreeUserId", favoreeUserId);
		         
		         
		         Map userInfoMap=(Map)this.userSer.executeQueryOne("findUserByUserId",Integer.parseInt(orderM.get("userId").toString()));

		         if((int)Math.round(Double.parseDouble(orderM.get("totalPrice").toString()))>=1) {					
		        						 int nowIgl;
		        						 if(null==userInfoMap.get("integral")) {
		        							 nowIgl=0;
		        						 }else {
		        							 nowIgl=Integer.parseInt(userInfoMap.get("integral").toString());
		        						 }
		        						 Map userIntMap = new HashMap<String,Object>();
		        						 userIntMap.put("integral", nowIgl+(int)Math.round(Double.parseDouble(orderM.get("totalPrice").toString())));						
		        						 userIntMap.put("userId", userInfoMap.get("userId").toString());
		        						 userSer.updateUserIntegral(userIntMap);
		        					 }
		         //修改钱包金额 ；
		         BigDecimal totalPrice =new BigDecimal(orderM.get("totalPrice").toString());
		         BigDecimal userBalance = new BigDecimal(userMap.get("userBalance").toString()); //用户的余额
		        //扣除系统费用后
		         Double systemCost =  VipUtils.sysTemCost(PayOrder.TYPE_XYYSF, totalPrice.doubleValue());
		    	 BigDecimal totalFee = new BigDecimal(systemCost);
				 Double balance =userBalance.add(totalFee).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue(); 
			
				 Map walletM = new HashMap<String,Object>();
	    		 walletM.put("userId", favoreeUserId);
	    		 walletM.put("businessOrder", orderM.get("businessOrder"));
	    		 walletM.put("balance",balance);
	    		 walletM.put("totalFee", totalPrice);
		         //写钱包流水；
	    		 walletSer.saveWalletRecord(walletM);   
		         
		         
		         //站内信；二个人；
			    //发送站内通知； (打赏人员)
    			Map<String, Object> msgMap1 = new HashMap<String, Object>();
    	       msgMap1.put("isRead","0"); //0:没读 1：已读
    	       msgMap1.put("sendTime",new Date()); //0:没读 1：已读
    	       msgMap1.put("isDelete","0"); //0:未删除 1：已删除
    	       msgMap1.put("messageState","0"); //
    	       msgMap1.put("is_send","1"); //0:未发送1：已发送
    	       msgMap1.put("create_time",new Date()); 	
    	       msgMap1.put("userId",orderM.get("userId")); 
    	       msgMap1.put("messageContent","尊敬的用户，您的【"+orderM.get("businessType")+"】金额（"+totalPrice+"）已支付成功。");
    	       msgMap1.put("messageType",orderM.get("sourceType"));
    	       msgMap1.put("businessType","1");
    	       msgMap1.put("businessId",orderM.get("businessOrder")); //协议律师费用需要转到订单详情
    	       msgMap1.put("messageTitle",orderM.get("orderName"));  
    	       userSer.messageSend(msgMap1);
    	      
    	       //被打赏人员
    	       Map<String, Object> msgMap2 = new HashMap<String, Object>();
    	       msgMap2.put("isRead","0"); //0:没读 1：已读
    	       msgMap2.put("sendTime",new Date()); 
    	       msgMap2.put("isDelete","0"); //0:未删除 1：已删除
    	       msgMap2.put("messageState","0"); //
    	       msgMap2.put("is_send","1"); //0:未发送1：已发送
    	       msgMap2.put("create_time",new Date()); 	
    	       msgMap2.put("userId",favoreeUserId); 
    	       msgMap2.put("messageContent","尊敬的用户，收到【"+orderM.get("sourceType")+"】的金额 "+systemCost+" 元，快去看看吧！点击直接跳转订单内容。");
    	       msgMap2.put("messageType",orderM.get("sourceType"));
    	       msgMap2.put("businessType","1");
    	       msgMap2.put("businessId",orderM.get("businessOrder"));//协议律师费用需要转到订单详情
    	       msgMap2.put("messageTitle",orderM.get("orderName"));  
    	       userSer.messageSend(msgMap2);
		         
			}else {
				//2.失败 
				isPay = 4;
			}
			checkTime = new Date();
			//endTime = new Date();
			orderState = 1 ;
		}
		
		saveOrderMap.put("businessOrder", businessOrder);
		saveOrderMap.put("userId", orderM.get("userId"));
		saveOrderMap.put("isPay", isPay);
		saveOrderMap.put("orderState", orderState);
		saveOrderMap.put("cardNumber", openid);
		saveOrderMap.put("outTradeNo",outTradeNo);
		 saveOrderMap.put("checkTime", checkTime);
		 //saveOrderMap.put("endTime", endTime);
		 saveOrderMap.put("favoreeUserId", favoreeUserId);
		 saveOrderMap.put("remark", remark);
		this.update(saveOrderMap);
	} 
   
 
	
	/**
	 * 打赏支付订单成功后，需要给业务订单的打赏数量 加 1 ；
	 * @param map 支付订单；
	 */
	private void orderRewardSuccess(Map map) throws Exception { 
		//订单的来源业务类型；
	    Object sourceType = map.get("sourceType");
	    //订单的来源业务编号；
	    Object connectionId = map.get("connectionId");
	    if(null != sourceType && sourceType.toString().equals(PayOrder.TYPE_FYZX)) {
	    	//如果订单来源为法律咨询的打赏；需要取订单数据中的 sign
	    	String sign = map.get("sign").toString();
	    	JSONObject signM = JSONObject.fromObject(sign);
	    	//取这里面的 currentId 
	    	Object currentId = signM.get("currentId");
	    	//修改
	    	Map saveMap = new HashMap<String,Object>();
	    	saveMap.put("replyId", currentId);
	    	saveMap.put("rewardNum", 1);
	    	adviceSer.updateAdviceReply(saveMap);
	    	
	    }else if(null != sourceType &&( sourceType.toString().equals(PayOrder.TYPE_BXTC) || sourceType.toString().equals(PayOrder.TYPE_ALCJ))) {
	    	//如果订单来源为百姓吐槽 或者是 阿里裁决的 打赏 
	    	//修改
	    	Map saveMap = new HashMap<String,Object>();
	    	saveMap.put("articleId", connectionId);
	    	saveMap.put("rewardNum", 1);
	    	articleSer.updateArticle(saveMap);
	    }else {
	    	ExceptionLogger.writeLog("打赏支付成功回调时，打赏来源类型为" +sourceType+"， 没有改变任何打赏的统计数据！");
	    } 
	    
	}
	
	/**
	 * 处理会员充值成功业务
	 * 
	 * userTyoe 【 1：个人咨询者2：咨询师3：企业咨询者】
	 * 
	 * @param orderMap
	 */
	private void orderVipRecharge(Map orderMap) throws Exception {
		//根据订单的业务，来进行；
		String businessType = (String)orderMap.get("businessType");
		//如果是会员充值；
		if(!businessType.equals(PayOrder.TYPE_HYCZ)) {
			ExceptionLogger.writeLog("会员回调处理失败： 功能是处理会员充值，但是该订单是" + businessType +"所以不处理"); 
		} 
		
		//获取 user 用户信息；
		Integer userId = (Integer)orderMap.get("userId");
		Map userMap = userSer.getUserInfo(userId);
		
		int oldVipLevel = null == userMap.get("vipLevel") ?  0 :(Integer)userMap.get("vipLevel")  ; //改变之前的等级；
		
		String sign = (String)orderMap.get("sign");
		 
		JSONObject obj = JSONObject.fromObject(sign); 
		//获取充值的类型是初次还是续充； 
		String type = (String)obj.get("type");
		
		//获取充值用户是什么职业；
		Integer userType = (Integer)userMap.get("userType"); //【 1：个人咨询者2：咨询师3：企业咨询者】
		
		//获取的年限数量；
		Integer orderNum = (Integer)orderMap.get("orderNum");
		//获取会员等级的名称；
		String orderName = (String)orderMap.get("orderName"); 
		Map voucherMap = null;//产生的文书券
		
		//充值后。应该成为哪种会员等级；
		int vipLevel = 0; 
		vipLevel = VipUtils.getLevelCode(orderName.replaceAll("补差价", ""));
		//生成文书券
		voucherMap = this.createVoucher(userId, orderName+"赠送", orderNum,(Date)userMap.get("expireTime"));
		int num = 0; 
		//该订单发生的金额；
		BigDecimal totalPrice =new BigDecimal(orderMap.get("totalPrice").toString());
		
		/**
		 * 企业用户
		 */
		if(userType.equals(3)) { 
			//企业用户的充值 （不管是初次还是续费 )，赠送的文书券 都是等级 * 年限；
			num = VipUtils.getCompanyVoucher(vipLevel); //根据会员等级，得到每年能赠送多少个文书券
			
			//如果企业用户type 是补差价；需要把这个时间段所赠送的文书券数量 查询出来；
			if(type.equals("补差价")) {
				int year = VipUtils.returnYear((Date)userMap.get("inVipTime"), (Date)userMap.get("expireTime"));
				num = num * year;
				
				Map  map1 = new HashMap<String,Object>();
				map1.put("userId", userId);
				map1.put("startTime", orderMap.get("startTime"));
				map1.put("endTime", orderMap.get("endTime"));
				List   voucherList = userSer.listVoucher(map1);
				if(null != voucherList) {
					//需要把此次赠送的文书券数量 减去 已经赠送的数量；
					num = num - voucherList.size();
				}
			}else {
				num = num * orderNum;
			}
			
			for(int i = 0 ; i <num; i ++) {
				this.userSer.saveVoucher(voucherMap);
			} 
			
		/**
		 * 咨询师用户
		 */
		}else  if(userType.equals(2)){
			//咨询师处理；
            num = VipUtils.getPersonVoucher(vipLevel); 
			//判断如果是初次充值；
			if(type.equals("初次")) {
				//算出赠送多少文书券；
				for(int i = 0 ; i < num; i ++) {
					this.userSer.saveVoucher(voucherMap);
				}
			}else if(type.equals("续充")) {
				//如果是续充，要判断开始等级和现在的等级是否一样，如果不一样。那只能送一个基本的文书券；
				if(oldVipLevel != vipLevel ) {
					for(int i = 0 ; i < num; i ++) {
						this.userSer.saveVoucher(voucherMap);
					}
				}else {
					//如果等级相等，就在次数 还要  *  会员年数；
					num = num * orderNum;
					for(int i = 0 ; i < num; i ++) {
						this.userSer.saveVoucher(voucherMap);
					}
				}
			}
			
			//订单流水记录；充值的流水
			BigDecimal userBalance = new BigDecimal(userMap.get("userBalance").toString()); //用户的余额
			BigDecimal totalFee = new BigDecimal(totalPrice+"");
			Double balance =userBalance.add(totalFee).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue(); 
			Map walletM = new HashMap<String,Object>();
    		walletM.put("userId", userId);
    		walletM.put("businessOrder", orderMap.get("businessOrder"));
    		walletM.put("balance",balance);
    		walletM.put("totalFee", totalPrice);
    	
    		
    		
    		balance =new BigDecimal(balance).subtract(totalFee).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue(); 
    		//订单流水记录；支付的流水
			Map walletMa = new HashMap<String,Object>();
			walletMa.put("userId", userId);
			walletMa.put("businessOrder", orderMap.get("businessOrder"));
			walletMa.put("balance",balance);
			walletMa.put("totalFee",new BigDecimal(0).subtract(totalPrice));
		 
			walletSer.saveWalletRecord(walletMa);
			
			walletSer.saveWalletRecord(walletM); 
				 
    	
		/**
		 * 咨询者用户	
		 */
		}else if(userType.equals(1)) {
			//咨询者处理
			//根据等级获取文书券的数量；
			 num = VipUtils.getPersonVoucher(vipLevel); 
			//判断如果是初次充值；
				if(type.equals("初次")) {
					//算出赠送多少文书券；
					for(int i = 0 ; i < num; i ++) {
						this.userSer.saveVoucher(voucherMap);
					}
				}else if(type.equals("续充")) {
					//如果是续充，要判断开始等级和现在的等级是否一样，如果不一样。那只能送一个基本的文书券；
					if(oldVipLevel != vipLevel ) {
						for(int i = 0 ; i < num; i ++) {
							this.userSer.saveVoucher(voucherMap);
						}
					}else {
						//如果等级相等，就在次数 还要  *  会员年数；
						num = num * orderNum;
						for(int i = 0 ; i < num; i ++) {
							this.userSer.saveVoucher(voucherMap);
						}
					}
				}			
		}else {
			ExceptionLogger.writeLog("会员回调处理失败： 功能是处理会员充值，但是该订单的用户类型没有找到。" + userType +"所以不处理"); 
		} 
		
		//修改企业用户的会员到期时间；
		String vipBeginDateTime = (String)voucherMap.get("startTime");
		String vipEndDateTime = (String)voucherMap.get("endTime");
		Map saveUserMap = new HashMap<String,Object>();
		saveUserMap.put("expireTime", vipEndDateTime);
		if(type.equals("初次")) {
			saveUserMap.put("inVipTime", vipBeginDateTime);
		}
		//算出总积分；
		int nowIntegral = VipUtils.getIntegral(totalPrice.intValue());
		int integral =  null ==  userMap.get("integral")  ? 0 :(Integer)userMap.get("integral") ;
		integral = integral+nowIntegral;
		saveUserMap.put("integral", integral);
		saveUserMap.put("userId", userId); 
		saveUserMap.put("vipLevel", vipLevel); 
		this.userSer.updateUserInfo(saveUserMap);
		
		//发送站内通知；
		Map<String, Object> msgMap1 = new HashMap<String, Object>();
       msgMap1.put("isRead","0"); //0:没读 1：已读
       msgMap1.put("sendTime",new Date()); //0:没读 1：已读
       msgMap1.put("isDelete","0"); //0:未删除 1：已删除
       msgMap1.put("messageState","0"); //
       msgMap1.put("is_send","1"); //0:未发送1：已发送
       msgMap1.put("create_time",new Date()); 	
       msgMap1.put("userId",userId); 
       msgMap1.put("messageContent","尊敬的用户，您"+orderMap.get("sourceType")+"【"+orderMap.get("orderName")+"】充值成功。");
       msgMap1.put("messageType",orderMap.get("sourceType"));
       msgMap1.put("businessType","1");
       msgMap1.put("businessId",orderMap.get("businessOrder"));//保存订单号的Id，因为在站内信详情中需要跳到订单详情
       msgMap1.put("messageTitle",orderMap.get("orderName"));  
       userSer.messageSend(msgMap1);
		
	}
	
	/**
	 * 生成文书使用券
	 * @param userId 用户Id
	 * @param voucherDesc  券的由来
	 * @param date 会员到期时间
	 * @param year 券使用的的期限，年为单位 ，1 就是 1年使用期；
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Map createVoucher(int userId,String voucherDesc,int year,Date date) throws Exception {
		//如果会员到时间比现在的时间要还没有到，就需要在会员到期时间上面累加年数；
		//否则以当前的时间再加上年数；
		 Date dt = new Date();  
		if(null == date || VipUtils.checkVipExpires(dt, date)) {
			
		} else {
			 dt = date;
		}
		
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
	    String  temp_str=sdf.format(dt);
	    dt.setYear(dt.getYear()+year);
	    String  temp_str_end=sdf.format(dt); 
		Map m = new HashMap<String,Object>();
		m.put("voucherDesc", voucherDesc);
		m.put("userId", userId);
		m.put("startTime", temp_str+" 00:00:00");
		m.put("endTime", temp_str_end+" 23:59:59");
		m.put("state", 0);
		return m;
	}

	@Override
	public int updateOrderByconnectionId(Map map) throws Exception {
		return super.executeUpdate("updateOrderByconnectionId", map);
	}
	
 
	
}
