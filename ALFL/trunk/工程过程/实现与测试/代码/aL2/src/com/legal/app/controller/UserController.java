package com.legal.app.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.cache.ICache;
import com.common.dbutil.Paging;
import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;
import com.common.utils.MD5Util;
import com.common.utils.SMSUtilLiantong;
import com.common.utils.StringUtil;
import com.common.utils.SystemUtil;
import com.common.utils.TokenHandle;
import com.common.web.WebUtils;
import com.gexin.fastjson.JSON;
import com.legal.app.model.ConselorInfo;
import com.legal.app.service.OrderService;
import com.legal.app.service.UserService;
import com.legal.app.utils.CreateOrderNumber;
import com.legal.app.utils.LunarUtil;
import com.legal.app.utils.OrderType;
import com.legal.app.utils.PayOrder;
import com.legal.app.utils.Tools;
import com.legal.app.utils.VipUtils;



/**
 * 用户所有相关的api
 * @author admin
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
@Controller
@RequestMapping("user")
public class UserController extends SuperController{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderser;
	
	private SMSUtilLiantong smsLt = new SMSUtilLiantong();
	/**
	 * 登陆
	 * @param userName
	 * @param password
	 * @param deviceId
	 * @param openId
	 * @param openType
	 * @param accountNo
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="login")
	public void  login(
			@RequestParam(value="userName") String userName,
			@RequestParam(value="password") String password,
			@RequestParam(value="deviceId") String deviceId,
			@RequestParam(value="openId",required=false) String openId,
			@RequestParam(value="openType",required=false) String openType, 
			@RequestParam(value="accountNo",required=false) String accountNo, HttpServletRequest request,
			PrintWriter out) throws Exception{
		String token =TokenHandle.encrytToken(userName);
		Map<String, Object> userInfo = null;
		
		userInfo = userService.userLogin(userName, password); 
		userInfo.remove("userPwd");//移除密码项
		userInfo.put("token",token);
		Map pushMap = new HashMap();
		pushMap.put("userId", userInfo.get("userId"));
		pushMap.put("pushCode", deviceId);
		if(userInfo.get("userState").toString().equals("0")){
			throw new BusinessException("当前用户被禁用 ,如果疑问请联系客服!", -1);
		}
	
		userService.updateUserPushCode(pushMap);//更新推送码
		//openId 不为空  标示  第三方登录 验证
		if(openId!=null&&!openId.equals("")){
			Map map =  new HashMap();
			map.put("userId", userInfo.get("userId"));
			map.put("openType", openType);
			map.put("openId", openId);
			map.put("accountNo", accountNo); 
			userService.bindThreeInfo(map);
			userInfo = userService.userLogin(userName, password);
			userInfo.remove("userPwd");//移除密码项
			userInfo.put("token",token);
			
		}
 
		ICache.tokenMap.remove("token");
		ICache.tokenMap.put("token", token);
		out.print(WebUtils.responseData(userInfo));
	}
	
	@ResponseBody
	@RequestMapping("/checkMobile")
	public String checkMobile(
			HttpServletRequest request
			) throws Exception{
		
		
		String mobile = request.getParameter("mobile");
		Map<String,String> returnMap = new HashMap<String, String>();
		if(StringUtil.isNotBlank(mobile)){
		Map mobileMap = new HashMap<String, String>();
		mobileMap.put("mobile", mobile);
		List<Map> mList  = userService.getUserInfoByMobileOruserName(mobileMap);
	    	if(mList.size()>0){
	    		returnMap.put("mobileNumber", "Y");
			}else {
				returnMap.put("mobileNumber", "N");
			}
    	}
		return JSON.toJSONString(returnMap) ;
	}
	
	
	
	@ResponseBody
	@RequestMapping("/ffindPassword")
	public String ffindPassword(
			HttpServletRequest request
			) throws Exception{
		
		String mobel1 = request.getParameter("mobel1");
		String code1 = request.getParameter("code1");
		Map<String,String> returnMap = new HashMap<String, String>();
		Object phoneCode=request.getSession().getAttribute("phoneCode");
		Object msgCode=request.getSession().getAttribute("msgCode");
		try {
			if(StringUtil.isNotBlank(mobel1)){
				phoneCode=phoneCode.toString();
				if(!phoneCode.equals(mobel1)){
					returnMap.put("mobileNumber", "phoneNo");
				}
			}if( StringUtil.isNotBlank(code1)) {
				msgCode=msgCode.toString();
				if(!msgCode.equals(code1)){
					returnMap.put("mobileNumber", "msgNo");
				}
			}if(null==phoneCode) {
				returnMap.put("mobileNumber", "phoneCodeNo");
			}if(null==msgCode){
				/*
				 * String phoneCode=request.getSession().getAttribute("phoneCode").toString();
				 * String msgCode=request.getSession().getAttribute("msgCode").toString();
				 */
					returnMap.put("mobileNumber", "MsgCodeNo");
			}if(phoneCode.equals(mobel1) && msgCode.equals(code1)) {
				returnMap.put("mobileNumber", "Y");
			}
		} catch (Exception e) {
			returnMap.put("mobileNumber", "N");
		}
		return JSON.toJSONString(returnMap) ;
	}
	
	
	/**
	 * 第三方登录回调
	 * @param code 授权码

	 */
	@RequestMapping("commonLoginCallback")
	public void commonLoginCallback(@RequestParam("code") String code,@RequestParam("state")String state) throws Exception{
		
	}
	
	

	/**
	 * 绑定第三方登录凭证
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("bindThreeLoginFlag")
	public void bindThreeLoginFlag(HttpServletRequest request,PrintWriter out) throws Exception{
		Map map = new HashMap();
		String openType = request.getParameter("openType");
		if(StringUtil.isBlank(openType)){
			throw new BusinessException("第三方类型缺失.", -1);
		}
		map.put("openType",openType.toUpperCase());
		String openId = request.getParameter("openId");
		
		if(StringUtil.isBlank(openId)){
			throw new BusinessException("第三方标示缺失.", -1);
		}
		map.put("openId",openId);
		String userId = request.getParameter("userId");
		if(StringUtil.isBlank(userId)){
			throw new BusinessException("用户ID缺失.", -1);
		}
		map.put("userId",userId);
		int count = userService.countThreeLoginFlag(map);
			if(count>0){
				out.print(WebUtils.responseMsg("该号已有绑定用户。", -2));
				return;
			}
 		try {
 		
			 userService.bindThreeLoginFlag(map);
			 out.print(WebUtils.responseMsg("绑定成功"));
		} catch (Exception e) {
			e.printStackTrace();

		}
		
	}
	
	/**
	 * 解除第三方登录凭证绑定
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("unbindThreeLoginFlag")
	public void unbindThreeLoginFlag(HttpServletRequest request,PrintWriter out) throws Exception{
		Map map = new HashMap();
		String openType = request.getParameter("openType");
		if(StringUtil.isBlank(openType)){
			throw new BusinessException("第三方类型缺失.", -1);
		}
		map.put("openType",openType.toUpperCase());
		
		String userId = request.getParameter("userId");
		if(StringUtil.isBlank(userId)){
			throw new BusinessException("用户ID缺失.", -1);
		}
		map.put("userId",userId);
 		try {
			 userService.unbindThreeLoginFlag(map);
			 out.print(WebUtils.responseMsg("解除绑定成功"));
		} catch (Exception e) {
			e.printStackTrace();
			out.print(WebUtils.responseMsg("解除绑定失败.", -2));

		}
		
	}
	
	
	
	
	
	/**
	 * 获取第三方登录凭证
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("getThreeLoginFlag")
	public void getThreeLoginFlag(HttpServletRequest request,PrintWriter out) throws Exception{
		Map map = new HashMap();
		String openType = request.getParameter("openType");
		if(StringUtil.isBlank(openType)){
			throw new BusinessException("第三方类型缺失.", -1);
		}
		map.put("openType",openType.toUpperCase());
		String openId = request.getParameter("openId");
		
		if(StringUtil.isBlank(openId)){
			throw new BusinessException("第三方标示缺失.", -1);
		}
		map.put("openId",openId);
		int userId = userService.getThreeLoginFlag(map);
		//未绑定 第三方登录账号
		if(userId == 0){
			//标示未绑定第三方
			out.print(WebUtils.responseMsg("未绑定.", -2));
		}else{
			Map m = userService.getUserInfo(userId);
			m.put("token", TokenHandle.encrytToken(m.get("userName").toString()));
			out.print(WebUtils.responseData(m));
		}
	}
	
	
	/**
	 * 个人咨询者注册
	 */
	@RequestMapping("consultantRegister")
	public void consultantRegister(
			@RequestParam("userName") String userName,
			@RequestParam("password") String password,
			@RequestParam("mobile") String mobile,
			@RequestParam("code") String code,
			@RequestParam("equipmentType") int  equipmentType,
			@RequestParam(value="openId",required=false) String openId,
			@RequestParam(value="openType",required=false) String openType, 
			PrintWriter out
			) throws Exception{
		
		//获取短信验证码
		Object smsCode = cache.get(mobile);
		
		if(StringUtil.isBlank(password)){
			throw new BusinessException("密码不能为空.", -1);
		}
		
		if(smsCode==null){
			throw new BusinessException("验证码失效.", -1);
		}
		
		if(!smsCode.toString().equals(code)){
			throw new BusinessException("验证码不正确.", -1);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("userPwd", MD5Util.MD5(password));
		map.put("mobile", mobile);
		map.put("equipmentType", equipmentType);
		map.put("userType", 1);//个人咨询注册
		map.put("userImg", SystemUtil.getDefaultUserImg());
		map.put("vipLevel", 0);
		map.put("isWeChat", 0);
		map.put("isWeiBo", 0);
		map.put("isQQ", 0);
		map.put("userState", 1);
		map.put("isAuthentication", 0);//
		map.put("is_push", 0);
		map.put("is_contract", 0);
		
		Map userNameMap = new HashMap<String, String>();
		userNameMap.put("userName", userName);
		List<Map> uList  = userService.getUserInfoByMobileOruserName(userNameMap);
 	    if(uList.size()>0){
			throw new BusinessException("该用户名已经注册", -1);
		} 
		Map mobileMap = new HashMap<String, String>();
		mobileMap.put("mobile", mobile);
		//mobileMap.put("userType", "1");
		List<Map> mList  = userService.getUserInfoByMobileOruserName(mobileMap);
		if(mList.size()>0){
			throw new BusinessException("该手机号码已经注册", -1);
		} 
		try {

		    userService.consultantRegister(map);
		    if(openId!=null&&!openId.equals("")&&openType!=null&&!openType.equals("")){
				Map map1 =  new HashMap();
				map1.put("userId", map.get("userId"));
				map1.put("openType", openType);
				map1.put("openId", openId);
				userService.bindThreeLoginFlag(map1);
			}
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("isRead","0"); //0:没读 1：已读
			m.put("sendTime",new Date()); //0:没读 1：已读
			m.put("isDelete","0"); //0:未删除 1：已删除
			m.put("messageState","0"); //
			m.put("is_send","1"); //0:未发送1：已发送
			m.put("create_time",new Date()); 	
			m.put("userId",map.get("userId")); 
			m.put("messageContent","尊敬的用户：恭喜您成功注册。淘法网为由湖南淘法法律服务有限公司携手湖南慧力律师事务所等多个律师事务所巨资打造。淘法法律平台由淘法网（www.taofa.vip）、手机APP、微信公众号、微网站、法律查询软件、微信小程序等组成。以法律咨询、法律文书写作和找律师为主，同时提供法律法规查询、诉讼费计算等各种信息服务。如果您在使用过程中有任何意见，欢迎随时给我们反馈。  感谢您选择“淘法网”。");
			m.put("messageType","注册内容");
			m.put("businessType","2");
			m.put("businessId",map.get("userId")+mobile);
			
		    int j = userService.messageSend(m);	
		    ExceptionLogger.writeLog("个人咨询者注册消息发送成功");
			out.print(WebUtils.responseData(map.get("userId")));
			//smsLt.sendReg(mobile, "1");
 		
		} catch (Exception e) {
			if(e instanceof BusinessException){
				throw (BusinessException)e;
			}
			ExceptionLogger.writeLog("个人咨询者注册异常："+e);
			throw new BusinessException("注册失败.", -1);
		}
	}
	
	
	/**
	 * 查询咨询师与咨询者的vip补差价；
	 * @param request
	 * @param out
	 */
	@RequestMapping("queryPersonAutoRenew")
	public void  queryPersonAutoRenew(
			HttpServletRequest request,
			PrintWriter out
			) throws Exception {
		Map map = getMap(request);
		Object vipLevel_tmp  = map.get("vipLevel");
		if(null == vipLevel_tmp || vipLevel_tmp.equals("")) {
			throw new BusinessException("需要补差价的等级不能为空.", -1);
		}
		int  vipLevel = Integer.valueOf(vipLevel_tmp.toString());
		if(vipLevel< 1 && vipLevel >3) {
			throw new BusinessException("需要补差价的等级不在范围内.", -1);
		}
		
		//id不能为空
		Object userId = map.get("userId");
		if(userId == null || userId.equals("")) {
			throw new BusinessException("用户id不能为空.", -1);
		}
		
		Map userMap = userService.getUserInfo(Integer.valueOf(userId.toString()));
		if(null == userMap) {
			throw new BusinessException("用户不存在.", -1);
		}
		int userType = (int)userMap.get("userType");
		if(userType == 3) {
			throw new BusinessException("您不是个人用户.", -1); 
		}	
		
		Object expireTime_tmp = userMap.get("expireTime");
		if(expireTime_tmp == null) {
			throw new BusinessException("会员开通失败,会员还没有开通会员，不能续充.", -1);
		}
		//判断补差价的等级 和现在的会员的等级是否一致；
		Integer userVipLevel = (Integer)userMap.get("vipLevel");
		if(vipLevel == (int)userVipLevel) {
			throw new BusinessException("相同等级不需要补差价.", -1);
		}else if((int)vipLevel < (int)userVipLevel ) {
			throw new BusinessException("补差价等级不能低于原有等级.", -1);
		}
		
		//得到数据库中的积分；
		Integer integral = null == userMap.get("integral") ? 0  :  (int)userMap.get("integral");
		//得以数据库中的车辆类型；
		Object carType_tmp = userMap.get("carType");
		if(null == carType_tmp) {
			throw new BusinessException("会员还没有提供车辆类型.", -1);
		}
		int carType = (int)carType_tmp;
		Date  expireTime = (Date)expireTime_tmp;
		Date inVipTime = (Date)userMap.get("inVipTime");
		String levelName = VipUtils.getLevelName((int)vipLevel);
		Map renMap = new HashMap<String,Object>();
		renMap.put("returnYear", 0);
		renMap.put("levelName", levelName);
		int returnYear = 0;
		int money = 0;
		for(int i = 1; i < 15; i ++) {
			int nowMoney = VipUtils.sumVipMoney(i, carType);
			int nowIntegral = VipUtils.getIntegral(nowMoney);
			int nowLevel = VipUtils.getVipLevelToConsultant(inVipTime, expireTime, i, carType, integral, nowIntegral);
			if(nowLevel >= (int)vipLevel ) {
				//如果等级大于等于 目标等级 
				returnYear = i;
				money = nowMoney;
				break;
			}
		}
		renMap.put("returnYear", returnYear);
		renMap.put("money", money);
		out.print(WebUtils.responseData("查询成功.", 1, renMap));
}
	@RequestMapping("checkLoginIsPay")
	public void checkLoginIsPay(
			HttpServletRequest request,
			PrintWriter out
			) throws Exception {
		Map map = getMap(request);
		Object  userId = map.get("userId");
		if(userId == null || userId.equals("")) {
			throw new BusinessException("用户编号不能为空.", -1);
		}
		
		//判断是否为初次开通会员，是否还有会员充值的订单还没有支付。
		//1.查询是否充值还没有完成的；
		Map orderMap =  new HashMap<String,Object>();
		orderMap.put("businessType",PayOrder.TYPE_HYCZ);
		orderMap.put("userId",userId);
		orderMap.put("special", 0); //查询 isPay = 2 或者 等于 3
		orderMap.put("orderState","1");//查询订单还是在进行中
		String msg="需要成为VIP会员.";
		List orderList = orderser.findOrderDetails(orderMap);
		if(null != orderList && orderList.size() > 0) {
			msg="还有订单没有支付.";
		} 
		out.print(WebUtils.responseData(msg, 1, orderList));
	}
	
	/**
	 *个人咨询者和咨询师续充vip 接口 
	 */
	@RequestMapping("continueVip")
	public void continueVip(
			HttpServletRequest request,
			PrintWriter out
			) throws Exception {
		Map map = getMap(request);
		
		//id不能为空
		Object userId = map.get("userId");
		if(userId == null || userId.equals("")) {
			throw new BusinessException("用户id不能为空.", -1);
		}
		
		//续充年限不能为空
		Object openYear_tmp  = map.get("openYear");
		if(null == openYear_tmp || openYear_tmp.equals("")) {
			throw new BusinessException("续充值年限不能为空.", -1);
		}
		int  openYear = 0;
		try {
			openYear= Integer.valueOf(openYear_tmp.toString());
			if(openYear < 1) {
				throw new BusinessException("续充值年限不能小于1.", -1);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new BusinessException("续充值年限需为数字.", -1);
		}
		
		//查询出这个用户是否存在；
		Map userMap = null;
		int carType =  0;
		int integral  = 0;
		Date inVipTime   =null;
		Date expireTime =null;
		Map orderM = null;
				
	 
			userMap = userService.getUserInfo(Integer.valueOf(userId.toString()));
			if(null == userMap) {
				throw new BusinessException("会员开通失败,用户不存在.", -1);
			}
			
			int userType = (int)userMap.get("userType");
			if(userType == 3) {
				throw new BusinessException("您不是个人用户.", -1); 
			}
			
			//得到数据库中的积分；
			integral = null == userMap.get("integral") ? 0  :  (int)userMap.get("integral");
			//得以数据库中的车辆类型；
			Object carType_tmp = userMap.get("carType");
			if(null == carType_tmp) {
				throw new BusinessException("会员开通失败,没有提供会员车辆类型.", -1);
			}
			carType = (int)carType_tmp;
			
			Object expireTime_tmp = userMap.get("expireTime");
			if(expireTime_tmp == null) {
				throw new BusinessException("会员开通失败,会员还没有开通会员，不能续充.", -1);
			}
			expireTime = (Date)expireTime_tmp;
			inVipTime = (Date)userMap.get("inVipTime");
			//如果续充时，会员原来的 【用户成为会员时间   inVipTime  】 为 null 时，就取当前的时间
			if(null == inVipTime) {
				inVipTime = new Date();
			}
			
		 
		
		//判断是否还有支付地订单没有完成；
		Map orderMap =  new HashMap<String,Object>();
		orderMap.put("businessType",PayOrder.TYPE_HYCZ);
		orderMap.put("userId",userId);
		orderMap.put("special", 0); //查询 isPay = 2 或者 等于 3
		orderMap.put("orderState","1");//查询订单还是在进行中
		
		List orderList = orderser.findOrderDetails(orderMap);
		if(null != orderList && orderList.size() > 0 ) {
			throw new BusinessException("会员开通失败,会员充值订单未完成.", -1);
		}
		 
	    //算出本次的金额 ；
		int carMoney = VipUtils.getMoney(carType);
		int nowMoney = VipUtils.sumVipMoney(openYear, carType);
		int nowIntegral = VipUtils.getIntegral(nowMoney);
		
		//此次续充后可以得到的等级；
		int nowLevel = VipUtils.getVipLevelToConsultant(inVipTime, expireTime, openYear, carType, integral, nowIntegral);
	    String levelName = VipUtils.getLevelName(nowLevel);
		
		//验证通过，写支付订单；
		orderM = new HashMap<String,Object>();
		orderM.put("orderName",levelName); 
		orderM.put("userId",  userId); 
		orderM.put("businessType", PayOrder.TYPE_HYCZ);
		orderM.put("remark", "");
		orderM.put("orderNum", openYear);
		orderM.put("orderType", OrderType.ORDERTYPE_1);
		orderM.put("orderPrice", VipUtils.getMoney(carType)); //根据车的类型得到费用
		orderM.put("sourceType", PayOrder.TYPE_HYCZ);//订单来源的类型Id
		//orderM.put("connectionId", 0);
		//orderM.put("checkTime", new Date()); 
		//orderM.put("endTime", new Date()); //赏金
		orderM.put("evaluate", 0); //是否评价   0：不需要评价   1：已评价  2：未评价， 
		orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
		orderM.put("isPay", 2);  //是否支付  ，0：不需要支付      1： 已支付   2：未支付  3: 正在支付中  	4：支付失败
		orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货；
		orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
		orderM.put("orderState", 1);//订单的状态； 1 ： 还在进行中， 0 ：已完成 
		orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败 
		orderM.put("userName",userMap.get("realName"));
		String sign = "{\"type\":\"续充\",\"userType\":\""+userMap.get("userType")+"\"}"; //标识为初次升级为会员，在支付成功的回调中，根据这个标识来进行业务判断
		orderM.put("sign", sign);
		
		orderser.saveBusinessOrder(orderM); 
		out.print(WebUtils.responseData("资料已提交成功，支付订单已生成.", 1, orderM));
 
		
	} 
	
	/**
	 * @param userId 用户id
	 * @param vipLevel 会员等级
	 * @param openYear 开通年数
	 * @param realName 真实姓名
	 * @param idCard   证件号码
	 * @param idCardFront 证件正面照片
	 * @param idCardBack  证件反面照片
	 * @param out
	 */
	@RequestMapping("openVipByConsultant")
	public void openVipByConsultant(
			@RequestParam("userId") int userId,
			@RequestParam("carType") int carType,
			@RequestParam("vipLevel") int vipLevel,
			@RequestParam("openYear") int openYear,
			@RequestParam("realName") String  realName,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		
		 
		
		
		
		String userType =  request.getParameter("userType");
		if(null == userType || userType.equals("") || (!userType.equals("1") && !userType.equals("2"))) {
			throw new BusinessException("用户职业类型不能为空，或者类型不对.", -1);
		}
		
		Object idCard = request.getParameter("idCard");
		Object idCardFront = request.getParameter("idCardFront");
		Object idCardBack = request.getParameter("idCardBack");
		
		
		if((null == idCard || idCard.equals(""))&& userType.toString().equals("1")) {
			throw new BusinessException("用户身份证号不能为空.", -1);
		}
		/**
		if(userType.toString().equals("1")) {
			//如果是咨询者，必须要验证身份证信息；
			if(null == idCard || idCard.equals("")) {
				throw new BusinessException("用户身份证号不能为空.", -1);
			}
			if(null == idCardFront || idCardFront.equals("")) {
				throw new BusinessException("用户身份证正面不能为空.", -1);
			}
			if(null == idCardBack || idCardBack.equals("")) {
				throw new BusinessException("用户身份证反面不能为空.", -1);
			} 
		}
		**/
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		//map.put("vipLevel", vipLevel);
		map.put("carType", carType+"");
		map.put("openYear", openYear);
		map.put("realName", realName);
		map.put("idCard", idCard);
		map.put("idCardFront",idCardFront);
		map.put("idCardBack", idCardBack);
		
		//判断是否为初次开通会员，是否还有会员充值的订单还没有支付。
		//1.查询是否充值还没有完成的；
		Map orderMap =  new HashMap<String,Object>();
		orderMap.put("businessType",PayOrder.TYPE_HYCZ);
		orderMap.put("userId",userId);
		orderMap.put("special", 0); //查询 isPay = 2 或者 等于 3
		orderMap.put("orderState","1");//查询订单还是在进行中
		 
		List orderList = orderser.findOrderDetails(orderMap);
		if(null != orderList && orderList.size() > 0 ) {
			throw new BusinessException("会员开通失败,会员充值订单未完成.", -1);
		} 
		
		//2.判断是否为初次充值；
		try {
			Map userMap = userService.getUserInfo(userId+"");
			if(null == userMap) {
				throw new BusinessException("会员开通失败,用户不存在.", -1);
			}
			
			Object expireTime = userMap.get("expireTime");
			if(expireTime != null) {
				throw new BusinessException("会员开通失败,会员非首次开通.", -1);
			}
		} catch (Exception e2) { 
			e2.printStackTrace();
			throw new BusinessException("会员开通失败", -1);
		}
		
		int money = 0;
		try {
			//获取vip的费用
			money = VipUtils.sumVipMoney(openYear, carType);
		} catch (Exception e1) {
			throw new BusinessException(e1.getMessage(), -1);
		}
	
		try {
			
			if(userType.equals("1")) {
				//修改咨询者的个人信息；
				userService.openVipByConsultant(map);
			}else {
				//修改咨询师的个人信息；
				userService.updateCounselorInfo(map);
			}
			//生成会员充值的订单；
			
			Map orderM = new HashMap<String,Object>();
			orderM.put("orderName", VipUtils.getLevelName(VipUtils.getVipLevelToInitial(openYear, VipUtils.getIntegral(VipUtils.sumVipMoney(openYear, carType))))); 
			orderM.put("userId",  userId); 
			orderM.put("businessType", PayOrder.TYPE_HYCZ);
			orderM.put("remark", VipUtils.companyName);
			orderM.put("orderNum", openYear);
			orderM.put("orderType", OrderType.ORDERTYPE_1);
			orderM.put("orderPrice", VipUtils.getMoney(carType)); //根据车的类型得到费用
			orderM.put("sourceType", PayOrder.TYPE_HYCZ);//订单来源的类型Id
			//orderM.put("connectionId", 0);
			//orderM.put("checkTime", new Date()); 
			//orderM.put("endTime", new Date()); //赏金
			orderM.put("evaluate", 0); //是否评价   0：不需要评价   1：已评价  2：未评价， 
			orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
			orderM.put("isPay", 2);  //是否支付  ，0：不需要支付      1： 已支付   2：未支付  3: 正在支付中  	4：支付失败
			orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货；
			orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
			orderM.put("orderState", 1);//订单的状态； 1 ： 还在进行中， 0 ：已完成 
			orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败 
			orderM.put("userName", realName);
			String sign = "{\"type\":\"初次\",\"userType\":\""+userType+"\"}"; //标识为初次升级为会员，在支付成功的回调中，根据这个标识来进行业务判断
			orderM.put("sign", sign);
			orderser.saveBusinessOrder(orderM); 
			out.print(WebUtils.responseData("资料已提交成功，支付订单已生成.", 1, orderM)); 
			
		} catch (Exception e) {
			if(e instanceof BusinessException){
				throw (BusinessException)e;
			}
			ExceptionLogger.writeLog("会员开通失败："+e);
			throw new BusinessException("会员开通失败.", -1);
		} 
	}	 
	
	/**
	 *  个人咨询者管理中心的认证接口
	 * @param userId 用户id
	 * @param realName 真实姓名
	 * @param idCard   证件号码
	 * @param idCardFront 证件正面照片
	 * @param idCardBack  证件反面照片
	 * @param out
	 */
	@RequestMapping("realNameAuthConsultant")
	public void realNameAuthConsultant(
			@RequestParam("userId") int userId,
		//	@RequestParam("carType") int carType,
			@RequestParam("realName") String  realName,
			@RequestParam("idCard") String  idCard,
			@RequestParam("idCardFront") String  idCardFront,
			@RequestParam("idCardBack") String  idCardBack,
			PrintWriter out
			){
		
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		/**
		if(StringUtil.isBlank(String.valueOf(carType))){
			throw new BusinessException("证件类型不能缺少.",-1);
		}
		**/  
		
		if(StringUtil.isBlank(String.valueOf(realName))){
			throw new BusinessException("真实姓名不能缺少.",-1);
		}
		if(StringUtil.isBlank(String.valueOf(idCard))){
			throw new BusinessException("证件号码不能缺少.",-1);
		}
		/**
		if(StringUtil.isBlank(String.valueOf(idCardFront))){
			throw new BusinessException("证件正面照片不能缺少.",-1);
		}
		if(StringUtil.isBlank(String.valueOf(idCardBack))){
			throw new BusinessException("证件正面照片不能缺少.",-1);
		}
		**/
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		//map.put("carType", carType);
		map.put("realName", realName);
		map.put("idCard", idCard);
		map.put("idCardFront",idCardFront);
		map.put("idCardBack", idCardBack);
		try {
		/*	Map userMap  = userService.getUserInfo(userId);
			String isAuthentication = userMap.get("isAuthentication").toString();*/
			/*map.put("isAuthentication", "2");
			userService.updateUserInfo(map);*/
			Map  m = userService.getConsultantInfo(map);
			if(null!=m){
				userService.realNameAuthConsultant(map);
			}else{
				userService.addConsultantInfo(map);
			}	
			out.print(WebUtils.responseMsg("认证成功"));
		
			/*
			if(!isAuthentication.equals("2")){
				Map  m = userService.getConsultantInfo(map);
				if(null!=m){
					userService.realNameAuthConsultant(map);
				}else{
					userService.addConsultantInfo(map);
				}	
				out.print(WebUtils.responseMsg("认证成功"));
			}else{
				out.print(WebUtils.responseMsg("认证失败",-6));
			}*/
			
			
		} catch (Exception e) {
			if(e instanceof BusinessException){
				throw (BusinessException) e;
			}
			ExceptionLogger.writeLog("会员认证失败："+e);
			throw new BusinessException("会员认证失败.", -1);
		}
		
	}	
	/**
	 * 检查是否注册
	 * @throws Exception 
	 */
	@RequestMapping("checkisRegister")
	public void checkisRegister(
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		
		String userName = request.getParameter("userName");
		String nickName = request.getParameter("nickName");
		String mobile = request.getParameter("mobile");
		String userType = request.getParameter("userType");
		
		if(StringUtil.isNotBlank(userName)){
			Map userNameMap = new HashMap<String, String>();
			userNameMap.put("userName", userName);
			List<Map> uList  = userService.getUserInfoByMobileOruserName(userNameMap);
	 		if(uList.size()>0){
				throw new BusinessException("该用户名已经注册", -1);
			} 
		}
		
		if(StringUtil.isNotBlank(mobile)){
		Map mobileMap = new HashMap<String, String>();
		mobileMap.put("mobile", mobile);
	//	mobileMap.put("userType", userType);
		List<Map> mList  = userService.getUserInfoByMobileOruserName(mobileMap);
    	if(mList.size()>0){
			throw new BusinessException("该手机号码已经注册", -1);
		} 	}
    	
    	if(StringUtil.isNotBlank(nickName)){
		Map nickNameMap = new HashMap<String, String>();
		nickNameMap.put("nickName", nickName);
		List<Map> nList  = userService.getUserInfoByMobileOruserName(nickNameMap);
 		if(nList.size()>0){
			throw new BusinessException("该昵称已经注册", -1);
		} 
    	}
		out.print(WebUtils.responseData(1));
	}
	
	
	/**
	 * 企业咨询者注册
	 * @throws Exception 
	 */
	@RequestMapping("companyRegister")
	public void companyRegister(
			@RequestParam(name="userName",required=false) String userName,
			@RequestParam("password") String password,
			@RequestParam("mobile") String mobile,
			@RequestParam("equipmentType") int  equipmentType,
			@RequestParam("companyCode") String companyCode,
			@RequestParam("companyName") String companyName,@RequestParam("code") String code,
			@RequestParam(value="openId",required=false) String openId,
			@RequestParam(value="openType",required=false) String openType, 
			PrintWriter out
			) throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("userPwd", MD5Util.MD5(password));
		map.put("mobile", mobile);
		map.put("equipmentType", equipmentType);
		map.put("userType", 3);//个人咨询注册
		map.put("userImg", SystemUtil.getDefaultUserImg());
		map.put("vipLevel", 0);
		map.put("isWeChat", 0);
		map.put("isWeiBo", 0);
		map.put("isQQ", 0);
		map.put("userState", 1);
		map.put("isAuthentication", 0);
		map.put("is_push", 0);
		map.put("is_contract", 0);
		map.put("companyCode", companyCode);//公司代码
		map.put("companyName", companyName);//公司全称
		map.put("nickName", companyName);

		if(StringUtil.isBlank(password)){
			throw new BusinessException("密码不能为空.", -1);
		}
		Map userNameMap = new HashMap<String, String>();
		userNameMap.put("userName", userName);
		List<Map> uList  = userService.getUserInfoByMobileOruserName(userNameMap);
 		if(uList.size()>0){
			throw new BusinessException("该用户名已经注册", -1);
		} 
		Map mobileMap = new HashMap<String, String>();
		mobileMap.put("mobile", mobile);
		//mobileMap.put("userType", "3");
		List<Map> mList  = userService.getUserInfoByMobileOruserName(mobileMap);
    	if(mList.size()>0){
			throw new BusinessException("该手机号码已经注册", -1);
		} 
    	
		Map nickNameMap = new HashMap<String, String>();
		nickNameMap.put("nickName", companyName);
		List<Map> nList  = userService.getUserInfoByMobileOruserName(nickNameMap);
 		if(nList.size()>0){
			throw new BusinessException("该公司名称已经注册", -1);
		} 

		//获取短信验证码
		Object smsCode = cache.get(mobile);
		
		if(smsCode==null){
			throw new BusinessException("验证码失效.", -1);
		}
		if(!smsCode.toString().equals(code)){
			throw new BusinessException("验证码不正确.", -1);
		}
		
		try {
			userService.companyRegister(map);
		    if(openId!=null&&!openId.equals("")&&openType!=null&&!openType.equals("")){
					Map map1 =  new HashMap();
					map1.put("userId", map.get("userId"));
					map1.put("openType", openType);
					map1.put("openId", openId);
					userService.bindThreeLoginFlag(map1);
		    }
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("isRead","0"); //0:没读 1：已读
			m.put("sendTime",new Date()); //0:没读 1：已读
			m.put("isDelete","0"); //0:未删除 1：已删除
			m.put("messageState","0"); // 
			m.put("is_send","1"); //0:未发送1：已发送
			m.put("create_time",new Date()); 	
			m.put("userId",map.get("userId")); 
			m.put("messageContent","尊敬的用户：恭喜您成功注册。淘法网为由湖南淘法法律服务有限公司携手湖南慧力律师事务所等多个律师事务所巨资打造。淘法法律平台由淘法网（www.taofa.vip）、手机APP、微信公众号、微网站、法律查询软件、微信小程序等组成。以法律咨询、法律文书写作和找律师为主，同时提供法律法规查询、诉讼费计算等各种信息服务。如果您在使用过程中有任何意见，欢迎随时给我们反馈。  感谢您选择“淘法网”。！");
			m.put("messageType","注册内容");
			m.put("businessType","2");
			m.put("businessId",map.get("userId")+mobile);
			
		    int j = userService.messageSend(m);	
		    ExceptionLogger.writeLog("企业咨询者注册消息发送成功");
		    //smsLt.sendReg(mobile, "3");
			out.print(WebUtils.responseData(map.get("userId")));
		} catch (Exception e) {
			if(e instanceof BusinessException){
				throw (BusinessException)e;
			}
			ExceptionLogger.writeLog("企业咨询者注册异常："+e);
			throw new BusinessException("注册失败.", -1);
		}
		
	}	
	
	/**
	 * 咨询师注册
	 * @throws Exception 
	 */
	@RequestMapping("counselorRegister")
	public void counselorRegister(
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		
		if(userName==null||userName.equals("")){
			throw new BusinessException("用户名为必填项.", -1);
		}
		map.put("userName", userName);
		if(StringUtil.isBlank(password)){
			throw new BusinessException("密码不能为空.", -1);
		}
		
		String mobile = request.getParameter("mobile");
		
		if(mobile==null||mobile.equals("")){
			throw new BusinessException("手机号码为必填项.", -1);
		}
		
		map.put("mobile", mobile);
		
		String code = request.getParameter("code");

		//获取短信验证码
		Object smsCode = cache.get(mobile);
		
		if(smsCode==null){
			throw new BusinessException("验证码失效.", -1);
		}
		
		if(!smsCode.toString().equals(code)){
			throw new BusinessException("验证码不正确.", -1);
		}
		
		
		if(password==null||password.equals("")){
			throw new BusinessException("密码为必填项.", -1);
		}
		map.put("userPwd", MD5Util.MD5(password));
		String equipmentType = request.getParameter("equipmentType");
		
		if(equipmentType==null||equipmentType.equals("")){
			throw new BusinessException("设备类型为必填项.", -1);
		}
		
		map.put("equipmentType", Integer.parseInt(equipmentType));
		//办公地址
		String nickName = request.getParameter("nickName");
		
		if(nickName==null||nickName.equals("")){
			throw new BusinessException("昵称为必填项.", -1);
		}
		map.put("nickName", nickName);
		
		
		String province = request.getParameter("province");
		if(province==null||province.equals("")){
			throw new BusinessException("省份为必填项.", -1);
		}
		map.put("province", Integer.parseInt(province));
		
		String city = request.getParameter("city");
		if(city==null||city.equals("")){
			throw new BusinessException("城市为必填项.", -1);
		}
		map.put("city", Integer.parseInt(city));
		
		String job = request.getParameter("job");
		if(job==null||job.equals("")){
			throw new BusinessException("职业为必填项.", -1);
		}
		map.put("job", Integer.parseInt(job));
		
		String [] cases = request.getParameterValues("cases");
		//修改数字格式化异常
		cases = Arrays.copyOf(cases, cases.length-1);
		int [] cs = new int[cases.length];
		
		for (int i = 0; i < cs.length; i++) {
			cs[i] = Integer.parseInt(cases[i]);
		}
		
		String openId = request.getParameter("openId");
		String openType = request.getParameter("openType");

		map.put("cases", cs);
		map.put("userType", 2);//咨询师
		map.put("userImg", SystemUtil.getDefaultUserImg());
		map.put("vipLevel", 0);
		map.put("isWeChat", 0);
		map.put("isWeiBo", 0);
		map.put("isQQ", 0);
		map.put("userState", 1);
		map.put("isAuthentication", 0);
		map.put("is_push", 0);
		map.put("is_contract", 0);
		Map userNameMap = new HashMap<String, String>();
		userNameMap.put("userName", userName);
		List<Map> uList  = userService.getUserInfoByMobileOruserName(userNameMap);
 		if(uList.size()>0){
			throw new BusinessException("该用户名已经注册", -1);
		} 
 		
		Map nickNameMap = new HashMap<String, String>();
		nickNameMap.put("nickName", nickName);
		List<Map> nickList  = userService.getUserInfoByMobileOruserName(nickNameMap);
 		if(nickList.size()>0){
			throw new BusinessException("该昵称已经注册", -1);
		} 
 		
		Map mobileMap = new HashMap<String, String>();
		mobileMap.put("mobile", mobile);
		//mobileMap.put("userType", "2");
		List<Map> mList  = userService.getUserInfoByMobileOruserName(mobileMap);
	 	if(mList.size()>0){
			throw new BusinessException("该手机号码已经注册", -1);
		} 
	
		try {
			userService.counselorRegister(map);
		    if(openId!=null&&!openId.equals("")&&openType!=null&&!openType.equals("")){
					Map map1 =  new HashMap();
					map1.put("userId", map.get("userId"));
					map1.put("openType", openType);
					map1.put("openId", openId);
					userService.bindThreeLoginFlag(map1);
		    }
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("isRead","0"); //0:没读 1：已读
			m.put("sendTime",new Date()); //0:没读 1：已读
			m.put("isDelete","0"); //0:未删除 1：已删除
			m.put("messageState","0"); // 
			m.put("is_send","1"); //0:未发送1：已发送
			m.put("create_time",new Date()); 	
			m.put("userId",map.get("userId")); 
			m.put("messageContent","尊敬的用户：恭喜您成功注册。淘法网为由湖南淘法法律服务有限公司携手湖南慧力律师事务所等多个律师事务所巨资打造。淘法法律平台由淘法网（www.taofa.vip）、手机APP、微信公众号、微网站、法律查询软件、微信小程序等组成。以法律咨询、法律文书写作和找律师为主，同时提供法律法规查询、诉讼费计算等各种信息服务。如果您在使用过程中有任何意见，欢迎随时给我们反馈。  感谢您选择“淘法网”。！");
			m.put("messageType","注册内容");
			m.put("businessType","2");
			m.put("businessId",map.get("userId")+mobile);
		    int j = userService.messageSend(m);	
		    ExceptionLogger.writeLog("咨询师注册消息发送成功");
		    //smsLt.sendReg(mobile, "2");
			out.print(WebUtils.responseData(map.get("userId")));
		} catch (Exception e) {
			
			if(e instanceof BusinessException){
				throw (BusinessException)e;
			}
			
			ExceptionLogger.writeLog("咨询师注册异常："+e);
			throw new BusinessException("注册失败.", -1);
		}
		
	}	
	/**
	 * 咨询师认证
	 * @param request
	 * @param out
	 */
	@RequestMapping("counselorAuthentication")
	public void counselorAuthentication(HttpServletRequest request,PrintWriter out){
		Map<String, Object> map = new HashMap<String, Object>();
		String userId = request.getParameter("userId");
		if(userId==null||userId.equals("")){
			throw new BusinessException("userId为必传项.", -1);
		}
		map.put("userId",Integer.parseInt(userId));
		
		String realName = request.getParameter("realName");
		map.put("realName",realName);
		String province = request.getParameter("province");
		if(province==null||province.equals("")){
			throw new BusinessException("省份为必填项.", -1);
		}
		map.put("province", Integer.parseInt(province));
		
		String city = request.getParameter("city");
		if(city==null||city.equals("")){
			throw new BusinessException("城市为必填项.", -1);
		}
		map.put("city", Integer.parseInt(city));
		
		String job = request.getParameter("job");
		if(job==null||job.equals("")){
			throw new BusinessException("职业为必填项.", -1);
		}
		map.put("job", Integer.parseInt(job));
		
		String [] cases = request.getParameterValues("cases");
		//修改数字格式化异常
		cases = Arrays.copyOf(cases, cases.length-1);
		int [] cs = new int[cases.length];
		
		for (int i = 0; i < cs.length; i++) {
			cs[i] = Integer.parseInt(cases[i]);
		}
		map.put("cases", cs);
		
		String bankAccount = request.getParameter("bankAccount");
		if(bankAccount==null||bankAccount.equals("")){
			throw new BusinessException("银行卡号为必填项.", -1);
		}
		map.put("bankAccount", bankAccount);
		
		String bankName = request.getParameter("bankName");
		if(bankName==null||bankName.equals("")){
			throw new BusinessException("开户行为必填项.", -1);
		}
		map.put("bankName", bankName);
		
		String bankAccountName = request.getParameter("bankAccountName");
		if(bankAccountName==null||bankAccountName.equals("")){
			throw new BusinessException("开户人姓名为必填项.", -1);
		}
		map.put("bankAccountName", bankAccountName);
		
		//companyName
		
		String companyName = request.getParameter("companyName");
		map.put("companyName", companyName);
		
		//registerAddress
		
		String registerAddress = request.getParameter("registerAddress");
		map.put("registerAddress", registerAddress);
		//workAddress
		
		String workAddress = request.getParameter("workAddress");
		map.put("workAddress", workAddress);
		
		//email
		
		String email = request.getParameter("email");
		map.put("email", email);
		
		//qq
		
		String qq = request.getParameter("qq");
		map.put("qq", qq);
		
		//weChat
		
		String weChat = request.getParameter("weChat");
		map.put("weChat", weChat);
		
		//personImg
		
		String personImg = request.getParameter("personImg");
		if(personImg==null||personImg.equals("")){
			throw new BusinessException("形象图片为必填项.", -1);
		}
		map.put("personImg", personImg);
		     
		//workImg
		
		String workImg = request.getParameter("workImg");
		if(workImg==null||workImg.equals("")){
			throw new BusinessException("从业执照图为必填项.", -1);
		}
		map.put("workImg", workImg);
		
		String licenseNum = request.getParameter("licenseNum");
		if(licenseNum==null||licenseNum.equals("")){
			throw new BusinessException("执照号码为必填项.", -1);
		}
		map.put("licenseNum", licenseNum);
		
		try {
			userService.counselorAuthentication(map);
			out.print(WebUtils.responseMsg("保存成功."));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("保存咨询师认证信息失败.",-1);
		}
	}
	/**
	 * 咨询者认证成为咨询师
	 * @param request
	 * @param out
	 */
	@RequestMapping("authenticateCounselor")
	public void authenticateCounselor(HttpServletRequest request,PrintWriter out){
		Map<String, Object> map = new HashMap<String, Object>();
		String userId = request.getParameter("userId");
		if(userId==null||userId.equals("")){
			throw new BusinessException("userId为必传项.", -1);
		}
		map.put("userId",Integer.parseInt(userId));
		
		String realName = request.getParameter("realName");
		map.put("realName",realName);
		String province = request.getParameter("province");
		if(province==null||province.equals("")){
			throw new BusinessException("省份为必填项.", -1);
		}
		map.put("province", Integer.parseInt(province));
		
		String city = request.getParameter("city");
		if(city==null||city.equals("")){
			throw new BusinessException("城市为必填项.", -1);
		}
		map.put("city", Integer.parseInt(city));
		
		String job = request.getParameter("job");
		if(job==null||job.equals("")){
			throw new BusinessException("职业为必填项.", -1);
		}
		map.put("job", Integer.parseInt(job));
		
		String [] cases = request.getParameterValues("cases");
		//修改数字格式化异常
		cases = Arrays.copyOf(cases, cases.length-1);
		int [] cs = new int[cases.length];
		
		for (int i = 0; i < cs.length; i++) {
			cs[i] = Integer.parseInt(cases[i]);
		}
		map.put("cases", cs);
		
		String bankAccount = request.getParameter("bankAccount");
		if(bankAccount==null||bankAccount.equals("")){
			throw new BusinessException("银行卡号为必填项.", -1);
		}
		map.put("bankAccount", bankAccount);
		
		String bankName = request.getParameter("bankName");
		if(bankName==null||bankName.equals("")){
			throw new BusinessException("开户行为必填项.", -1);
		}
		map.put("bankName", bankName);
		
		String bankAccountName = request.getParameter("bankAccountName");
		if(bankAccountName==null||bankAccountName.equals("")){
			throw new BusinessException("开户人姓名为必填项.", -1);
		}
		map.put("bankAccountName", bankAccountName);
		
		//companyName
		
		String companyName = request.getParameter("companyName");
		map.put("companyName", companyName);
		
		//registerAddress
		
		String registerAddress = request.getParameter("registerAddress");
		map.put("registerAddress", registerAddress);
		//workAddress
		
		String workAddress = request.getParameter("workAddress");
		map.put("workAddress", workAddress);
		
		//email
		
		String email = request.getParameter("email");
		map.put("email", email);
		
		//qq
		
		String qq = request.getParameter("qq");
		map.put("qq", qq);
		
		//weChat
		
		String weChat = request.getParameter("weChat");
		map.put("weChat", weChat);
		
		//personImg
		
		String personImg = request.getParameter("personImg");
		if(personImg==null||personImg.equals("")){
			throw new BusinessException("形象图片为必填项.", -1);
		}
		map.put("personImg", personImg);
		     
		//workImg
		
		String workImg = request.getParameter("workImg");
		if(workImg==null||workImg.equals("")){
			throw new BusinessException("从业执照图为必填项.", -1);
		}
		map.put("workImg", workImg);
		
		String licenseNum = request.getParameter("licenseNum");
		if(licenseNum==null||licenseNum.equals("")){
			throw new BusinessException("执照号码为必填项.", -1);
		}
		map.put("licenseNum", licenseNum);
		
		try {
			userService.authenticateCounselor(map);
			out.print(WebUtils.responseMsg("保存成功."));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("保存咨询师认证信息失败.",-1);
		}
	}
	
	/**
	 * 获取用户评价
	 * @param userId
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("getUserEvaluate/{userId}")
	public void getUserEvaluate(@PathVariable("userId") int userId,PrintWriter out) throws Exception{
		try {
			List<Map> user = userService.getUserEvaluate(String.valueOf(userId));
			out.print(WebUtils.responseData(user));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("获取用户信息异常", -1);
		}
	}
	
	
	
	@RequestMapping("userInfo/{userId}")
	public void getUserInfo(@PathVariable("userId") int userId,PrintWriter out) throws Exception{
		try {
			Map<String, Object> user = userService.getUserInfo(userId);
			out.print(WebUtils.responseData(user));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("获取用户信息异常", -1);
		}
	}
	
	/**
	 * 企业用户查询补差价；
	 * @param request
	 * @param out
	 */
	@RequestMapping("queryCompanyAutoRenew")
	public void queryCompanyAutoRenew(
			HttpServletRequest request,
			PrintWriter out
			) throws Exception {
	  Map map = getMap(request);
	  Object userId = map.get("userId");
	  if(null == userId || userId.equals("")) {
		  throw new BusinessException("用户编号不能为空.", -1);
	  }
	  
	  Object vipLevel = map.get("vipLevel");
	  if(null == vipLevel || vipLevel.equals("")) {
		  throw new BusinessException("补差价的等级不能为空.", -1);
	  }
	  int level =Integer.valueOf(vipLevel.toString());
	    //判断传入的等级是否在正常范围；
		if( level <1  ||  level >3 ) {
			throw new BusinessException("补差价的等级不在范围内.", -1);
		}
	  
	     //查询这个用户是否存在；
	    //判断用户是否存在；
		Map userMap = userService.getUserInfo(Integer.valueOf(userId.toString()));
		if(null == userMap) {
			throw new BusinessException("会员开通失败,用户不存在.", -1);
		} 
		
		int userType = (int)userMap.get("userType");
		if(userType != 3) {
			throw new BusinessException("您不是企业用户.", -1); 
		}	
		
		Object expireTime = userMap.get("expireTime");
		if(null == expireTime || expireTime.equals("")) {
			throw new BusinessException("您还没有开通会员，不能进行补差价.", -1);
		}
		int userLevel = (int)userMap.get("vipLevel"); 
		//如果补差价等级 小于 或者 等级 用户等级；
		if(level <= userLevel) {
			throw new BusinessException("补差价等级要高于当前会员等级.", -1);
		}
		int userMoney = VipUtils.getMoneyCompany(userLevel);
		int money = VipUtils.getMoneyCompany(level);
		int balance = money - userMoney;
		//算出原来的充值是几年的会员时间；
		Object  inVipTime = userMap.get("inVipTime");
		int year = VipUtils.returnYear((Date)inVipTime, (Date)expireTime);
		balance = balance * year;
		
		Map renMap = new HashMap<String,Object>();
		renMap.put("money", balance);
		renMap.put("levelName", VipUtils.getLevelName(level));
		renMap.put("expireTime", expireTime);
		out.print(WebUtils.responseData("查询成功.", 1, renMap));  
	}
	
	/**
	 * 企业开通会员
	 * @param userId
	 * @param vipLevel 会员等级
	 * @param openYear 开通年数
	 * @param out
	 */
	@RequestMapping("openVipByCompany")
	public void openVipByCompany(
			@RequestParam("userId") int userId,
			@RequestParam("vipLevel") int vipLevel,
			@RequestParam("openYear") int openYear,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		  
	      Map map = getMap(request);
			 //判断年数是否大于0 
			if(openYear <1) {
				throw new BusinessException("会员年限不能小于1年.", -1);
			} 
			//判断传入的等级是否在正常范围；
			if( vipLevel <1  ||  vipLevel >3 ) {
				throw new BusinessException("会员等级不匹配.", -1);
			}
			
			//判断用户是否存在；
			Map userMap = userService.getUserInfo(userId);
			if(null == userMap) {
				throw new BusinessException("会员开通失败,用户不存在.", -1);
			}
			
			int userType = (int)userMap.get("userType");
			if(userType != 3) {
				throw new BusinessException("您不是企业用户.", -1);
			}
			
			//原定是从页面传入，当前充值 状态  ,现取消了补差价这个方式， 
			//那 type 的类型，就需要从 这个会员 的过期时间字段判定；
			// 如果为空。就是初次
			// 如果有时间，就是续充
			Object expireTime = userMap.get("expireTime");
			Object  type_tmp =null;
			//Object  type_tmp = map.get("type");
			if(null ==expireTime || expireTime.equals("") ) {
				type_tmp = 0;
			}else {
				type_tmp = 1;
			}
			
			/**
			 * 3.3.2企业咨询者开通会员；11.9修改
接口调用地址	api/user/openVipByCompany
参数	请求参数
userId 用户id
vipLevel 会员等级 会员等级类型  0：普通会员   1:黄金会员   2:白金会员  3：钻石会员
openYear 开通年数
type : 开通时的类型 【0：初次；1：续充；2补差价】 

			 */
			
		
			
	
			
			if(null == type_tmp || type_tmp.equals("")) {
				throw new BusinessException("充值类型不能为空.", -1);
			}
			
			String type="";
			if(expireTime != null) {
				if(type_tmp.toString().equals("1")) {
					type="续充";
				}else if(type_tmp.toString().equals("2")) {
					type="补差价";
				}else {
					throw new BusinessException("会员不是续充或者补差价.", -1);
				}
				
			}else {
				if(type_tmp.toString().equals("0")) {
					type="初次";
				}else {
					throw new BusinessException("会员不是第一次充值.", -1);
				}
			}

			Map orderMap =  new HashMap<String,Object>();
			orderMap.put("businessType",PayOrder.TYPE_HYCZ);
			orderMap.put("userId",userId);
			orderMap.put("special", 0); //查询 isPay = 2 或者 等于 3
			orderMap.put("orderState","1");//查询订单还是在进行中
		 
			List orderList = orderser.findOrderDetails(orderMap);
			if(null != orderList && orderList.size() > 0 ) {
				throw new BusinessException("会员开通失败,会员充值订单未完成.", -1);
			}
		    
			 //下订单；
			Map orderM = new HashMap<String,Object>(); 
			orderM.put("userId",  userId); 
			orderM.put("businessType", PayOrder.TYPE_HYCZ);
			orderM.put("remark", VipUtils.companyName);
			
			orderM.put("orderType", OrderType.ORDERTYPE_1);
			if(type.equals("补差价")) {
				orderM.put("orderName", VipUtils.getLevelName(vipLevel)+type); 
				int userMoney = VipUtils.getMoneyCompany((int)userMap.get("vipLevel"));
				int money = VipUtils.getMoneyCompany(vipLevel);
				int balance = money - userMoney;
				//算出原来的充值是几年的会员时间；
				Object  inVipTime = userMap.get("inVipTime");
				int year = VipUtils.returnYear((Date)inVipTime, (Date)expireTime);
				balance = balance * year;
				orderM.put("orderNum", 1); //补差价，只需要保存为1 
				orderM.put("orderPrice", balance ); // 算出的补差价的费用
			}else {
			    orderM.put("orderName", VipUtils.getLevelName(vipLevel)); 
			    orderM.put("orderPrice", VipUtils.getMoneyCompany(vipLevel)); //根据车的类型得到费用
			    orderM.put("orderNum", openYear); 
			}
			
			orderM.put("sourceType", PayOrder.TYPE_HYCZ);//订单来源的类型Id
			//orderM.put("connectionId", 0);
			//orderM.put("checkTime", new Date()); 
			//orderM.put("endTime", new Date()); //赏金
			orderM.put("evaluate", 0); //是否评价   0：不需要评价   1：已评价  2：未评价， 
			orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
			orderM.put("isPay", 2);  //是否支付  ，0：不需要支付      1： 已支付   2：未支付  3: 正在支付中  	4：支付失败
			orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货；
			orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
			orderM.put("orderState", 1);//订单的状态； 1 ： 还在进行中， 0 ：已完成 
			orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败 
			orderM.put("userName", userMap.get("realName"));
			String sign = "{\"type\":\""+type+"\",\"userType\":\""+userMap.get("userType")+"\"}"; //标识为初次升级为会员，在支付成功的回调中，根据这个标识来进行业务判断
			orderM.put("sign", sign);
			orderser.saveBusinessOrder(orderM); 
			out.print(WebUtils.responseData("资料已提交成功，支付订单已生成.", 1, orderM));   
		 
	}	
	
	
	
	/**
	 * 企业咨询者认证
	 * @param userId
	 * @param vipLevel 会员等级
	 * @param openYear 开通年数
	 * @param out
	 */    
	@RequestMapping("companyAuthentication")
	public void companyAuthentication(
			HttpServletRequest request,
			PrintWriter out
			){
		Map<String, Object> map = new HashMap<String, Object>();
		//用户id
		int userId = Integer.parseInt(request.getParameter("userId"));
		map.put("userId", userId);
		//公司代码
		String companyCode = request.getParameter("companyCode");
		
		if(companyCode==null||companyCode.equals("")){
			throw new BusinessException("单位代码为必填项.", -1);
		}
		
		map.put("companyCode", companyCode);
		//公司名称
		String companyName = request.getParameter("companyName");
		map.put("companyName", companyName);
		
		if(companyName==null||companyName.equals("")){
			throw new BusinessException("单位全称为必填项.", -1);
		}
		
		//注册地址
		String registerAddress = request.getParameter("registerAddress");
		map.put("registerAddress", registerAddress);
		//办公地址
		String workAddress = request.getParameter("workAddress");
		map.put("workAddress", workAddress);
		//email
		String email = request.getParameter("email");
		map.put("email", email);
		String wechat = request.getParameter("weChat");
		map.put("weChat", wechat);
		String qq = request.getParameter("qq");
		map.put("qq", qq);
		String realName = request.getParameter("realName");
		map.put("realName", realName);
		if(realName==null||realName.equals("")){
			throw new BusinessException("联系人为必填项.", -1);
		}
		
		String workImg = request.getParameter("workImg");
		map.put("workImg", workImg);
		if(realName==null||realName.equals("")){
			throw new BusinessException("联系人为必填项.", -1);
		}
		
		try {
			userService.companyAuthentication(map);
			out.print(WebUtils.responseMsg("认证信息保存成功."));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("认证信息保存失败.", -1);
		}
		
		
	}	
	
	
	/**
	 * 查询咨询师列表
	 * @param queryType 查询类型   1：综合排名 2：人气排名3：法律水平4：收费标准
	 * @param pageNo 页码
	 * @param pageSize 每页的记录数
	 * @param province 省份id
	 * @param city     城市id
	 * @param speciality 专业领域id
	 * @param job      职业
	 * @param name 姓名
	 * @param outs
	 * @throws Exception
	 */
	@RequestMapping("counselorList/{pageNo}/{pageSize}")
	public void getCounselorList(
				@PathVariable("pageNo") int pageNo,
				@PathVariable("pageSize") int pageSize,HttpServletRequest request,
				PrintWriter out
			) throws Exception{
		    Paging p = new Paging(pageSize, pageNo,true);
		    Map map =getMap(request);
		    String sortType = request.getParameter("sortType"); 
		    // 1：综合排名  commonScore 2：人气排名 readNum 3：法律水平 levelScore 4：收费标准 chargeScore(sql排序不生效，临时解决)
		/*    if(sortType.equals("1")){
		    	sortType = "commonScore";
		    }else if (sortType.equals("2")){
		    	sortType = "readNum";
		    }else if (sortType.equals("3")){
		    	sortType = "levelScore";
		    }else if (sortType.equals("4")){
		    	sortType = "chargeScore";
		    }*/
		    if(null!=sortType){
			    map.put("sortType", sortType);
		    }else{
			    map.put("sortType", "1");
		    }
		    List<ConselorInfo> list = userService.counselorList(p,map);
		    out.print(WebUtils.webResponsePage(list));
	}
	
	
	/**
	 * 我浏览的咨询师
	 * @param userId
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("myBrowseConsultant/{userId}/{pageNo}/{pageSize}")
	public void myBrowseConsultant(
			@PathVariable("userId") int userId,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			PrintWriter out
			) throws Exception{
	    Paging p = new Paging(pageSize, pageNo,true);
	    Map map = new HashMap<String, String>();
	    map.put("userId", userId);
		List<Map> list = userService.findBrowseList(p,map);
		out.print(WebUtils.webResponsePage(list));
	}
	
	/**
	 * 添加访问记录
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("addBrowse")
	public void addBrowse(
			HttpServletRequest request,PrintWriter out
			)throws  Exception{
		 Map map = getMap(request);
		 Object userId = map.get("userId");
		 if(null == userId || userId.equals("")) {
			 throw	new BusinessException("登陆用户Id不能缺少.", -1);
		 }
		 Object counselorId = map.get("counselorId");
		 if(null == counselorId || counselorId.equals("")) {
			 throw new BusinessException("被访问者用户Id不能缺少.", -1);
		 }
		 
		 if(userId.equals(counselorId)) {
			 throw new BusinessException("两个用户Id不能相同.", -1);
		 }
		 this.userService.addBrowse(map);
		 out.print(WebUtils.responseData("操作成功", 1, map));
	}
	
	/**
	 * 我收藏的咨询师
	 * @param userId
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("myCollecionConsultant/{userId}")
	public void myCollecionConsultant(
			@PathVariable("userId") int userId,
			PrintWriter out
			) throws Exception{
		List<Map> list = userService.findFollowList(userId);
		out.print(WebUtils.responseData(list));
	}
	
	/**
	 * 我咨询的咨询师
	 * @param userId
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("myAdviceConsultant/{userId}/{pageNo}/{pageSize}")
	public void myAdviceConsultant(
	@PathVariable("userId") int userId,@PathVariable("pageNo") int pageNo,
	@PathVariable("pageSize") int pageSize,PrintWriter out) throws Exception{
		 Paging p = new Paging(pageSize, pageNo,true);
		 Map map = new HashMap<String, String>();
		 map.put("userId", userId);
		 List<Map> list = userService.findMyAdviceConsultant(p,map);
		 out.print(WebUtils.webResponsePage(list));
	}
	
	/**
	 * 企业咨询者 添加公司信息
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("addCompany")
	public void addCompany(HttpServletRequest request,PrintWriter out) throws Exception{
		Map map = new HashMap();
		
		String type = request.getParameter("type"); //1：咨询者企业 2：咨询师企业
		if(StringUtil.isBlank(type)){
			new BusinessException("用户类型不能缺少.", -1);
		}
		map.put("type", type);

		String userId = request.getParameter("userId");
		if(StringUtil.isBlank(userId)){
			new BusinessException("用户编号不能缺少.", -1);
		}
		
		map.put("userId", Integer.parseInt(userId));
		
		String companyName  = request.getParameter("companyName");
		if(StringUtil.isBlank(companyName)){
			new BusinessException("公司名称不能缺少.", -1);
		}
		
		map.put("companyName", companyName);
		
		String companySize  = request.getParameter("companySize");
		if(StringUtil.isBlank(companySize)){
			new BusinessException("公司规模不能缺少.", -1);
		}
		
		map.put("companySize", companySize);
		
		String province  = request.getParameter("province");
		if(StringUtil.isBlank(province)){
			new BusinessException("省份不能缺少.", -1);
		}
		map.put("province", province);
		
		String city  = request.getParameter("city");
		if(StringUtil.isBlank(city)){
			new BusinessException("城市不能缺少.", -1);
		}
		map.put("city", city);
		
		String companyAddress  = request.getParameter("companyAddress");
		if(StringUtil.isBlank(companyAddress)){
			new BusinessException("公司地址不能缺少.", -1);
		}
		map.put("companyAddress", companyAddress);
		
		String contactMobile  = request.getParameter("contactMobile");
		if(StringUtil.isBlank(contactMobile)){
			new BusinessException("联系电话不能缺少.", -1);
		}
		map.put("contactMobile", contactMobile);
		
		String contactName  = request.getParameter("contactName");
		if(StringUtil.isBlank(contactName)){
			new BusinessException("联系人不能缺少.", -1);
		}
		map.put("contactName", contactName);
		
		String email  = request.getParameter("email");
		if(StringUtil.isBlank(email)){
			new BusinessException("email不能缺少.", -1);
		}
		map.put("email", email);
		
		String companyUrl  = request.getParameter("companyUrl");
		if(StringUtil.isBlank(companyUrl)){
			new BusinessException("公司网址不能缺少.", -1);
		}
		map.put("companyUrl", companyUrl);
		
		String companyDesc  = request.getParameter("companyDesc");
		if(StringUtil.isBlank(companyDesc)){
			new BusinessException("公司简介不能缺失.", -1);
		}
		map.put("companyDesc", companyDesc);
		
		
		try {
			userService.addCompanyInfo(map);
			out.print(WebUtils.responseMsg("保存公司成功"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("保存公司信息异常.",-1);
		}
	}
	
	/**
	 * 发布/关闭简历信息
	 * @throws Exception
	 */
	@RequestMapping("publishResumeInfo")
	public void publishResumeInfo(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
		String state = request.getParameter("state");
		if(StringUtil.isBlank(userId)){
			new BusinessException("用户编号不能缺少.", -1);
		}
 		if(StringUtil.isBlank(state)){
			new BusinessException("简历状态不能缺少.", -1);
		}
		Map m = new HashMap<String,String>();
		m.put("userId", userId);
		m.put("state", state);
		int i = userService.publishResumeInfo(m);
		out.print(WebUtils.responseMsg("修改成功"));

	}
	
	
	
	/**
	 * 添加简历信息
	 * @throws Exception
	 */
	@RequestMapping("addResumeInfo")
	public void addResumeInfo(HttpServletRequest request,PrintWriter out) throws Exception{
		
		Map map = new HashMap();
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String school = request.getParameter("school");
		String sex = request.getParameter("sex");
		String birthDate = request.getParameter("birthDate");
		if(StringUtil.isBlank(userId)){
			out.print(WebUtils.responseMsg("用户编号不能缺少.", -1));		
			return;
		}
		map.put("userId", Integer.parseInt(userId));

		if(StringUtil.isBlank(userName)){
			out.print(WebUtils.responseMsg("用户姓名不能缺少.", -1));			
			return;
		}
		map.put("userName", userName);
		
		if(StringUtil.isBlank(school)){
			out.print(WebUtils.responseMsg("毕业学校不能缺少.", -1));			
			return;

		}
		map.put("school", school);
		
		if(StringUtil.isBlank(sex)){
			out.print(WebUtils.responseMsg("性别不能缺少.", -1));
			return;

		}
		map.put("sex", sex);
		
		if(StringUtil.isBlank(birthDate)){
			out.print(WebUtils.responseMsg("出生年月不能缺少", -1));
			return;

		}
		map.put("birthDate", birthDate);
 
		String type = request.getParameter("type");
		if(StringUtil.isBlank(type)){
			out.print(WebUtils.responseMsg("简历类型不能缺少.", -1));
			return;

		}
		map.put("type", type);
		
		String selfAssessment = request.getParameter("selfAssessment");
		if(StringUtil.isBlank(selfAssessment)){
			out.print(WebUtils.responseMsg("自我评价不能缺少.", -1));
			return;

		}
		map.put("selfAssessment", selfAssessment);

		String resumeName = request.getParameter("resumeName");
		if(StringUtil.isBlank(resumeName)){
			out.print(WebUtils.responseMsg("简历名称不能缺少.", -1));
			return;

		}
		map.put("resumeName", resumeName);
		
		String job = request.getParameter("job");
		if(StringUtil.isBlank(job)){
			out.print(WebUtils.responseMsg("求职岗位不能缺少.", -1));
			return;

		}
		map.put("job", job);
		
		String sal = request.getParameter("sal");
		if(StringUtil.isBlank(sal)){
			out.print(WebUtils.responseMsg("期望工资不能缺少.", -1));
			return;

		}
		map.put("sal", Integer.parseInt(sal));
		
		String city = request.getParameter("city");
		if(StringUtil.isBlank(city)){
			out.print(WebUtils.responseMsg("工作城市不能缺少.", -1));
			return;

		}
		map.put("city", Integer.parseInt(city));
		
		String province = request.getParameter("province");
		if(StringUtil.isBlank(province)){
			out.print(WebUtils.responseMsg("工作省份不能缺少.", -1));
			return;

		}
		map.put("province", Integer.parseInt(province));
		
		
		String workExperience = request.getParameter("workExperience");
		if(StringUtil.isBlank(workExperience)){
			out.print(WebUtils.responseMsg("工作经验不能缺少.", -1));			return;

		}
		map.put("workExperience", Integer.parseInt(workExperience));
		
		String qualification = request.getParameter("qualification");
		if(StringUtil.isBlank(qualification)){
			out.print(WebUtils.responseMsg("最高学历不能缺少.", -1));			return;

		}
		map.put("qualification", Integer.parseInt(qualification));
		
		String lastmajor = request.getParameter("major");
		if(StringUtil.isBlank(lastmajor)){
			out.print(WebUtils.responseMsg("最高学历所属的专业不能缺少.", -1));			return;

		}
		map.put("major", lastmajor);
		
		String mobile = request.getParameter("mobile");
		if(StringUtil.isBlank(mobile)){
			out.print(WebUtils.responseMsg("联系电话不能缺少.", -1));			return;

		}
		map.put("mobile", mobile);
		map.put("time", new Date());

		/////////////////////////工作经验////////////////////////////
		
		//工作经验数量
		String experienceNum = request.getParameter("experienceNum");
		if(!StringUtil.isBlank(experienceNum)&&Integer.parseInt(experienceNum)>0){
			List<Map> eList = new ArrayList<Map>();
			int eNum = Integer.parseInt(experienceNum); 
			for (int i = 1; i <= eNum; i++) {
				Map m = new HashMap(); 
				String companyName = request.getParameter("companyName"+i);
				if(StringUtil.isBlank(companyName)){
					out.print(WebUtils.responseMsg("公司名称不能缺少",-1));
					return;

				}
				String jobType = request.getParameter("jobType"+i);
				if(StringUtil.isBlank(jobType)){
					out.print(WebUtils.responseMsg("工作类型不能缺少.", -1));
					return;

				}
				String jobName = request.getParameter("jobName"+i);
				if(StringUtil.isBlank(jobName)){
					out.print(WebUtils.responseMsg("职位名称不能缺少.", -1));
					return;
				}
				String inDate = request.getParameter("experience_inDate"+i);
				if(StringUtil.isBlank(inDate)){
					out.print(WebUtils.responseMsg("入职日期不能缺少.", -1));
					return;

				}
				String outDate = request.getParameter("experience_outDate"+i);
				if(StringUtil.isBlank(outDate)){
					out.print(WebUtils.responseMsg("离职日期不能缺少.", -1));
					return;

				}
				String deptName = request.getParameter("deptName"+i);
				if(StringUtil.isBlank(deptName)){
					out.print(WebUtils.responseMsg("部门不能缺少.", -1));
					return;

				}
				String workContent = request.getParameter("workContent"+i);
				if(StringUtil.isBlank(workContent)){
					out.print(WebUtils.responseMsg("工作内容不能缺少.", -1));
					return;

				}
				
				m.put("userId", Integer.parseInt(userId));
				m.put("jobType", jobType);
				m.put("jobName", jobName);
				m.put("inDate", inDate);
				m.put("outDate", outDate);
				m.put("deptName", deptName);
				m.put("workContent", workContent);
				m.put("companyName", companyName);
				eList.add(m);
			}
			
			
			map.put("experienceList", eList);
			
		}else{
			out.print(WebUtils.responseMsg("工作经验不能缺少",-1));
			return;
			//new BusinessException("工作经验不能缺少.", -1);
		}
		
		/////////////////////////学习经历////////////////////////////
		//教育经历数量
		String eduNum = request.getParameter("eduNum");
		if(!StringUtil.isBlank(eduNum)&&Integer.parseInt(eduNum)>0){
			List<Map> eList = new ArrayList<Map>();
			int eNum = Integer.parseInt(eduNum);
			
			for (int i = 1; i <= eNum; i++) {
				Map m = new HashMap();
				String schoolName = request.getParameter("schoolName"+i);
				if(StringUtil.isBlank(schoolName)){
					out.print(WebUtils.responseMsg("学校名称不能缺少.", -1));
					return;

				}
				String major = request.getParameter("major"+i);
				if(StringUtil.isBlank(major)){
					out.print(WebUtils.responseMsg("专业不能缺少.", -1));
					return;

				}
				String qualificationValue = request.getParameter("qualification"+i);
				if(StringUtil.isBlank(qualificationValue)){
					out.print(WebUtils.responseMsg("学历层次不能缺少.", -1));
					return;

				}
				String inDate = request.getParameter("edu_inDate"+i);
				if(StringUtil.isBlank(inDate)){
					out.print(WebUtils.responseMsg("入校日期不能缺少.", -1));
					return;

				}
				String outDate = request.getParameter("edu_outDate"+i);
				if(StringUtil.isBlank(outDate)){
					out.print(WebUtils.responseMsg("离校日期不能缺少.", -1));
					return;

				}
				String schoolDesc = request.getParameter("schoolDesc"+i);
				if(StringUtil.isBlank(schoolDesc)){
					out.print(WebUtils.responseMsg("在校经历不能缺少.", -1));
					return;

				}
				
				m.put("userId", Integer.parseInt(userId));
				m.put("schoolName", schoolName);
				m.put("major", major);
				m.put("qualification", qualificationValue);
				m.put("outDate", outDate);
				m.put("inDate", inDate);
				m.put("schoolDesc", schoolDesc);
				eList.add(m);
			}

			map.put("eduList", eList);
		}else{
			out.print(WebUtils.responseMsg("教育经历不能缺少",-1));
			return;

		}
		//简历作品附件
		String [] files = request.getParameterValues("files"); 
		if(files!=null&&files.length>0){
			List<Map> fs = new ArrayList<Map>();
			for (int i = 0; i < files.length; i++) {
				Map m = new HashMap();
				m.put("userId", Integer.parseInt(userId));
				m.put("fileName", files[i]);
				fs.add(m);
			}
			map.put("files", fs);
		}
		try {
			userService.addResumeInfo(map);
		} catch (Exception e) {
			e.printStackTrace();
			out.print(WebUtils.responseMsg("简历保存异常.", -1));
		}
		ExceptionLogger.writeLog(WebUtils.responseData(map));
		out.print(WebUtils.responseMsg("保存成功."));
	}
	
	

	
	/**
	 * 修改简历信息
	 * @throws Exception
	 */
	@RequestMapping("updateResumeInfo")
	public void updateResumeInfo(HttpServletRequest request,PrintWriter out) throws Exception{
		Map map = new HashMap();
		String userName = request.getParameter("userName");
		String school = request.getParameter("school");
		String sex = request.getParameter("sex");
		String birthDate = request.getParameter("birthDate");

		if(StringUtil.isBlank(userName)){
			out.print(WebUtils.responseMsg("用户姓名不能缺少.", -1));
			return;
		}
		map.put("userName", userName);
		
		if(StringUtil.isBlank(sex)){
			out.print(WebUtils.responseMsg("性别不能缺少.", -1));return;

		}
		map.put("sex", sex);
		
		if(StringUtil.isBlank(school)){
			out.print(WebUtils.responseMsg("毕业学校不能缺少.", -1));	return;

		}
		map.put("school", school);
		
		if(StringUtil.isBlank(birthDate)){
			out.print(WebUtils.responseMsg("出生年月不能缺少", -1));return;
		}
		map.put("birthDate", birthDate);
		
		String type = request.getParameter("type");
		if(StringUtil.isBlank(type)){
			out.print(WebUtils.responseMsg("简历类型不能缺少.", -1));return;
		}
		map.put("type", type);
 		String resumeId = request.getParameter("resumeId");
		if(StringUtil.isBlank(resumeId)){
			out.print(WebUtils.responseMsg("简历ID不能缺少.", -1));return;
		}
		map.put("resumeId", Integer.parseInt(resumeId));
		
		String userId = request.getParameter("userId");
		if(StringUtil.isBlank(userId)){
			out.print(WebUtils.responseMsg("用户编号不能缺少.", -1));return;
		}
		map.put("userId", Integer.parseInt(userId));
		
		String resumeName = request.getParameter("resumeName");
		if(StringUtil.isBlank(resumeName)){
			out.print(WebUtils.responseMsg("简历名称不能缺少.", -1));return;
		}
		map.put("resumeName", resumeName);
		
		String selfAssessment = request.getParameter("selfAssessment");
		if(StringUtil.isBlank(selfAssessment)){
			out.print(WebUtils.responseMsg("自我评价不能缺少.", -1));return;
		}
		map.put("selfAssessment", selfAssessment);

		
		String job = request.getParameter("job");
		if(StringUtil.isBlank(job)){
			out.print(WebUtils.responseMsg("求职岗位不能缺少.", -1));return;
		}
		map.put("job", job);
		
		String sal = request.getParameter("sal");
		if(StringUtil.isBlank(sal)){
			out.print(WebUtils.responseMsg("期望工资不能缺少.", -1));return;
		}
		map.put("sal", Integer.parseInt(sal));
		
		String province = request.getParameter("province");
		if(StringUtil.isBlank(province)){
			out.print(WebUtils.responseMsg("工作省份不能缺少.", -1));return;
		}
		map.put("province", Integer.parseInt(province));
		
		String city = request.getParameter("city");
		if(StringUtil.isBlank(city)){
			out.print(WebUtils.responseMsg("工作城市不能缺少.", -1));return;
		}
		map.put("city", Integer.parseInt(city));
		
		String workExperience = request.getParameter("workExperience");
		if(StringUtil.isBlank(workExperience)){
			out.print(WebUtils.responseMsg("工作经验不能缺少.", -1));return;
		}
		map.put("workExperience", Integer.parseInt(workExperience));
		
		String qualification = request.getParameter("qualification");
		if(StringUtil.isBlank(qualification)){
			out.print(WebUtils.responseMsg("最高学历不能缺少.", -1));return;
		}
		map.put("qualification", Integer.parseInt(qualification));
		
		String lastmajor = request.getParameter("major");
		if(StringUtil.isBlank(lastmajor)){
			out.print(WebUtils.responseMsg("最高学历所属的专业不能缺少.", -1));return;
		}
		map.put("major", lastmajor);
		
		String mobile = request.getParameter("mobile");
		if(StringUtil.isBlank(mobile)){
			out.print(WebUtils.responseMsg("联系电话不能缺少.", -1));return;
		}
		map.put("mobile", mobile);
		
		/////////////////////////工作经验////////////////////////////
		
		//工作经验数量
		String experienceNum = request.getParameter("experienceNum").trim();
		if(!StringUtil.isBlank(experienceNum)&&Integer.parseInt(experienceNum)>0){
			List<Map> eList = new ArrayList<Map>();
			int eNum = Integer.parseInt(experienceNum);
			for (int i = 1; i <= eNum; i++) {
				Map m = new HashMap();
				String experienceId = request.getParameter("experienceId"+i);
				if(StringUtil.isBlank(experienceId)){
					out.print(WebUtils.responseMsg("工作经验ID不能缺少.", -1));
				}
				String companyName = request.getParameter("companyName"+i);
				String jobType = request.getParameter("jobType"+i);
				String jobName = request.getParameter("jobName"+i);
				String inDate = request.getParameter("experience_inDate"+i);
				String outDate = request.getParameter("experience_outDate"+i);
				String deptName = request.getParameter("deptName"+i);
				String workContent = request.getParameter("workContent"+i);
				
				if(StringUtil.isBlank(companyName)){
					out.print(WebUtils.responseMsg("公司名称不能缺少",-1));
					return;

				}
				if(StringUtil.isBlank(jobType)){
					out.print(WebUtils.responseMsg("工作类型不能缺少.", -1));
					return;

				}
				if(StringUtil.isBlank(jobName)){
					out.print(WebUtils.responseMsg("职位名称不能缺少.", -1));
					return;
				}
				if(StringUtil.isBlank(inDate)){
					out.print(WebUtils.responseMsg("入职日期不能缺少.", -1));
					return;

				}
				if(StringUtil.isBlank(outDate)){
					out.print(WebUtils.responseMsg("离职日期不能缺少.", -1));
					return;

				}
				if(StringUtil.isBlank(deptName)){
					out.print(WebUtils.responseMsg("部门不能缺少.", -1));
					return;

				}
				if(StringUtil.isBlank(workContent)){
					out.print(WebUtils.responseMsg("工作内容不能缺少.", -1));
					return;

				}
				
				
				
				
				
				
				m.put("userId", Integer.parseInt(userId));
				m.put("experienceId", experienceId);
				m.put("jobType", jobType);
				m.put("jobName", jobName);
				m.put("inDate", inDate);
				m.put("outDate", outDate);
				m.put("deptName", deptName);
				m.put("workContent", workContent);
				m.put("companyName", companyName);
				eList.add(m);
			}
			map.put("experienceList", eList);
		}else{
			out.print(WebUtils.responseMsg("工作经历不能缺少.", -1));return;
		}
						
		//教育经历数量
		String eduNum = request.getParameter("eduNum");
		if(!StringUtil.isBlank(eduNum)&&Integer.parseInt(eduNum)>0){
			List<Map> eList = new ArrayList<Map>();
			int eNum = Integer.parseInt(eduNum);
			for (int i = 1; i <= eNum; i++) {
				Map m = new HashMap();
				String educationId = request.getParameter("educationId"+i);
				if(StringUtil.isBlank(educationId)){
					out.print(WebUtils.responseMsg("教育经历ID不能缺少.", -1));return;
				}
				String schoolName = request.getParameter("schoolName"+i);
				String major = request.getParameter("major"+i);
				String qualificationValue = request.getParameter("qualification"+i);
				String inDate = request.getParameter("edu_inDate"+i);
				String outDate = request.getParameter("edu_outDate"+i);
				String schoolDesc = request.getParameter("schoolDesc"+i);

				if(StringUtil.isBlank(schoolName)){
					out.print(WebUtils.responseMsg("学校名称不能缺少.", -1));
					return;

				}
				if(StringUtil.isBlank(major)){
					out.print(WebUtils.responseMsg("专业不能缺少.", -1));
					return;

				}
				if(StringUtil.isBlank(qualificationValue)){
					out.print(WebUtils.responseMsg("学历层次不能缺少.", -1));
					return;

				}
				if(StringUtil.isBlank(inDate)){
					out.print(WebUtils.responseMsg("入校日期不能缺少.", -1));
					return;

				}
				if(StringUtil.isBlank(outDate)){
					out.print(WebUtils.responseMsg("离校日期不能缺少.", -1));
					return;

				}
				if(StringUtil.isBlank(schoolDesc)){
					out.print(WebUtils.responseMsg("在校经历不能缺少.", -1));
					return;

				}

				m.put("userId", Integer.parseInt(userId));
				m.put("educationId", educationId);
				m.put("schoolName", schoolName);
				m.put("major", major);
				m.put("qualification", qualificationValue);
				m.put("outDate", outDate);
				m.put("inDate", inDate);
				m.put("schoolDesc", schoolDesc);
				eList.add(m);
			}

			map.put("eduList", eList);
		}else{
			out.print(WebUtils.responseMsg("教育经历不能缺少.", -1));return;
		}
		//简历作品附件
		String [] files = request.getParameterValues("files");
		//简历作品附件
		if(files!=null&&files.length>0){
			List<Map> fs = new ArrayList<Map>();
			for (int i = 0; i < files.length; i++) {
				Map m = new HashMap();
				m.put("userId", Integer.parseInt(userId));
				m.put("fileName", files[i]);
				fs.add(m);
			}
			map.put("files", fs);
		}

		
		try {
			userService.updateResumeInfo(map);
		} catch (Exception e) {
			e.printStackTrace();
			 out.print(WebUtils.responseMsg("简历修改异常.", -1));return;
		}
		
		
		ExceptionLogger.writeLog(WebUtils.responseData(map));
		out.print(WebUtils.responseMsg("修改成功."));
	}
	
	

	/**
	 * 根据用户id获取简历信息
	 * @throws Exception
	 */
	@RequestMapping("getResumeInfo/{userId}")
	public void getResumeInfo(
				@PathVariable("userId") int userId,
				PrintWriter out
			) throws Exception{
		
		Map m = userService.getResumeInfoByUserId(userId);
		out.print(WebUtils.responseData(m));
	}
	
	/**
	 * 投递简历
	 * @throws Exception
	 */
	@RequestMapping("deliverResume")
	public void deliverResume(HttpServletRequest request,PrintWriter out) throws Exception{
		
		Map map = new HashMap();
		
		String consultantId = request.getParameter("userId");
		String companyId = request.getParameter("companyId");
		if(StringUtil.isBlank(consultantId)){
			throw new BusinessException("用户编号缺失.", -1);
		}
		
		map.put("consultantId", Integer.parseInt(consultantId));
		map.put("companyId", companyId);
		
		String advertiseId = request.getParameter("advertiseId");
		if(StringUtil.isBlank(advertiseId)){
			throw new BusinessException("岗位编号缺失.", -1);
		}
		map.put("advertiseId", Integer.parseInt(advertiseId));
		
		//获取用户简历信息
		Map resumeInfo = userService.findResumeInfoByUserId(Integer.parseInt(consultantId));
		if(resumeInfo==null){
			throw new BusinessException("用户简历信息不存在.", -1);
		}
		
		map.put("resumeId",resumeInfo.get("resumeId"));
		
		try {
			userService.deliverResume(map);
			out.print(WebUtils.responseMsg("简历投递成功."));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("简历投递异常.", -1);
		}
		
		
	}
	
	
	/**
	 * 我投递的简历
	 * @throws Exception
	 */
	@RequestMapping("myDeliverResume/{userId}")
	public void myDeliverResume() throws Exception{
		
	}
	
	/**
	 * 用户收到的简历
	 * @throws Exception
	 */
	@RequestMapping("collectResumeList/{userId}")
	public void collectResumeList() throws Exception{
		
	}
	
	
	/**
	 * 用户收藏的简历信息列表
	 * @throws Exception
	 */
	@RequestMapping("collectionResumeList/{userId}/{pageNo}/{pageSize}")
	public void collectionResumeList(PrintWriter out,
			@PathVariable("userId") int userId,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize
			) throws Exception{
	    Paging paging = new Paging(pageSize, pageNo,true);

	    List<Map> m = userService.collectionResumeList(paging,userId);
		out.print(WebUtils.webResponsePage(m));
	}
	
	/**
	 * 根据用户id获取公司信息
	 * @throws Exception
	 */
	@RequestMapping("getCompanyInfo/{userId}")
	public void getCompanyInfo(
			PrintWriter out,
			@PathVariable("userId") int userId
			) throws Exception{
		
		Map m = userService.getCompanyInfo(userId);
		out.print(WebUtils.responseData(m));
	}
	
	
	/**
	 * 发布/关闭岗位
	 * @throws Exception
	 */
	@RequestMapping("publishOrCloseJob")
	public void publishOrCloseJob(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
		String advertiseId = request.getParameter("advertiseId");
		String state = request.getParameter("state");
		if(StringUtil.isBlank(userId)){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		if(StringUtil.isBlank(advertiseId)){
			throw new BusinessException("岗位Id不能缺少.",-1);
		}
		if(StringUtil.isBlank(state)){
			throw new BusinessException("发布状态不能缺少.",-1);
		}
 		Map map = getMap(request);
 		int i =  userService.publishOrCloseJob(map);
 		String msg ="";
 		if(state.equals("1")){
 			msg = "发布成功";
 		}else{
 			msg = "撤销成功";
 		}
 		out.print(WebUtils.responseMsg(msg));
 	}
	
	
	/**
	 * 添加岗位
	 * @throws Exception
	 */
	@RequestMapping("sendJob")
	public void sendJob(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
		String jobName = request.getParameter("jobName");
		String type = request.getParameter("type");
		String sal = request.getParameter("sal");
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String qualification = request.getParameter("qualification");
		String workExperience = request.getParameter("workExperience");
		String jobDesc = request.getParameter("jobDesc");
		Map mapJob = getMap(request);//new HashMap<String, String>();
		/**
		mapJob.put("userId", userId);
		mapJob.put("jobName", jobName);
		mapJob.put("type", type);
		mapJob.put("sal", sal);
		mapJob.put("city", city);
		mapJob.put("qualification", qualification);
		mapJob.put("workExperience", workExperience);
		mapJob.put("jobDesc", jobDesc);**/
	
		if(StringUtil.isBlank(userId)){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		if(StringUtil.isBlank(type)){
			throw new BusinessException("岗位类型不能缺少.",-1);
		}
		if(StringUtil.isBlank(jobName)){
			throw new BusinessException("岗位名称不能缺少.",-1);
		}
		if(StringUtil.isBlank(sal)){
			throw new BusinessException("工资待遇不能缺少.",-1);
		}
		if(StringUtil.isBlank(province)){
			throw new BusinessException("工作省份不能缺少.",-1);
		}
		if(StringUtil.isBlank(city)){
			throw new BusinessException("工作城市不能缺少.",-1);
		}
		if(StringUtil.isBlank(qualification)){
			throw new BusinessException("学历要求不能缺少.",-1);
		}
		if(StringUtil.isBlank(workExperience)){
			throw new BusinessException("工作经验不能缺少.",-1);
		}
		if(StringUtil.isBlank(jobDesc)){
			throw new BusinessException("工作描述不能缺少.",-1);
		}
	
		String userType = request.getParameter("userType");
		if(null != userType && userType.equals("0")) {
			mapJob.put("linkman", request.getParameter("linkman"));
			//mapJob.put("phone", request.getParameter("phone"));
			mapJob.put("companyName", request.getParameter("companyName"));
			mapJob.put("userType", request.getParameter("userType"));
			mapJob.put("state", "0");
			mapJob.put("isComplain", "正常");

		}else {
			//去查询用户表；
			List<Map> list = this.userService.findUserCompanyInfo(mapJob);
			if(null == list || list.size() == 0 ) {
				throw new BusinessException("用户还没有认证公司信息，暂时不能发布信息.",-1);
			}
			Map listM = list.get(0);
			userType = listM.get("userType").toString();
			mapJob.put("userType", userType);
			Object companyName = listM.get("companyName");
			mapJob.put("companyName", companyName);
			Object contactName = listM.get("contactName"); 
			mapJob.put("linkman", contactName);
			mapJob.put("state", "1");
			mapJob.put("isComplain", "正常");
		}
		 userService.sendJob(mapJob); 
		out.print(WebUtils.responseData("发布成功",1,mapJob));
 	}
	
	/**
	 * 修改岗位
	 * @throws Exception
	 */
	@RequestMapping("updateJob")
	public void updateJob(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
		String advertiseId = request.getParameter("advertiseId");
		Map map = getMap(request);
		if(StringUtil.isBlank(userId)){
			throw new BusinessException("用户编号不能缺少.",-1);
		}		
		if(StringUtil.isBlank(advertiseId)){
			throw new BusinessException("岗位编号不能缺少.",-1);
		}
		int i = userService.updateJob(map);
		out.print(WebUtils.responseMsg("修改成功"));
 	}
	
	/**
	 * 删除岗位
	 * @throws Exception
	 */
	@RequestMapping("deleteJob")
	public void deleteJob(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
		String advertiseId = request.getParameter("advertiseId");
		Map map = getMap(request);
		if(StringUtil.isBlank(userId)){
			throw new BusinessException("用户编号不能缺少.",-1);
		}		
		if(StringUtil.isBlank(advertiseId)){
			throw new BusinessException("岗位编号不能缺少.",-1);
		}
		int i = userService.deleteJob(map);
		out.print(WebUtils.responseMsg("删除成功"));
 	}
	
	
	
	/**
	 * 查询岗位
	 * @throws Exception
	 */
	@RequestMapping("seacherJob/{userId}/{pageNo}/{pageSize}")
	public void seacherJob(@PathVariable("userId") int userId,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize, PrintWriter out) throws Exception{
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
	    Paging paging = new Paging(pageSize, pageNo,true);
	    Map map =new HashMap<String,String>();
	    map.put("userId", userId);
	    List<Map> list = userService.seacherJob(paging,map);
		out.print(WebUtils.webResponsePage(list));
 	}
	/**
	 * 查询岗位详情
	 * @throws Exception
	 */

	@RequestMapping("seacherJobDetail")
	public void seacherJobDetail(HttpServletRequest request,PrintWriter out) throws Exception{
/*		String userId = request.getParameter("userId");
*/		String advertiseId = request.getParameter("advertiseId");
/*		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}*/
		if(StringUtil.isBlank(String.valueOf(advertiseId))){
			throw new BusinessException("岗位编号不能缺少.",-1);
		}
		Map map = getMap(request);
		Map m = userService.seacherJobDetail(map);
		out.print(WebUtils.responseData(m));
 	}
	
	/**
	 * 举报岗位
	 * @throws Exception
	 */
	@RequestMapping("complainJob")
	public void complainJob(HttpServletRequest request,PrintWriter out) throws Exception{
		String advertiseId = request.getParameter("advertiseId");
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String mobile = request.getParameter("mobile");
		String reportTitle = request.getParameter("reportTitle");
	    if(StringUtil.isBlank(String.valueOf(advertiseId))){
			throw new BusinessException("岗位编号不能缺少.",-1);
		}
	    if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户不能缺少.",-1);
		}
	    if(StringUtil.isBlank(String.valueOf(userName))){
			throw new BusinessException("用户名称不能缺少.",-1);
		}
	    if(StringUtil.isBlank(String.valueOf(mobile))){
			throw new BusinessException("手机号码不能缺少.",-1);
		}
	    if(StringUtil.isBlank(String.valueOf(reportTitle))){
			throw new BusinessException("举报标题不能缺少.",-1);
		}


	    Map m = new HashMap<String, String>();
	    m.put("isDelete","0"); //0:未删除 1：已删除
	    m.put("reportBusinessId",advertiseId); 
	    m.put("userId",userId); 
	    m.put("userName",userName); 
	    m.put("mobile",mobile); 
	    m.put("reportType",2); 
	    m.put("reportTitle",reportTitle); 
	    m.put("reportTime",new Date()); 
		userService.insertReport(m);
		
		Map map = getMap(request);
		map.put("isComplain", "被举报");
		int i = userService.complainJob(map);
		Map<String, Object> m1 = new HashMap<String, Object>();
		m1.put("isRead","0"); //0:没读 1：已读
		m1.put("sendTime",new Date()); //0:没读 1：已读
		m1.put("isDelete","0"); //0:未删除 1：已删除
		m1.put("messageState","0"); // 
		m1.put("is_send","1"); //0:未发送1：已发送
		m1.put("create_time",new Date()); 	
		m1.put("userId",""); 
		m1.put("messageContent","管理员您好，该岗位已用户举报，请核实岗位详情是否进行删除。");
		m1.put("messageType","岗位举报");
		m1.put("businessType","2");
		m1.put("businessId",advertiseId);
		
	    int j = userService.messageSend(m1);
		out.print(WebUtils.responseMsg("举报成功"));
 	}
	
	
	/**
	 * 查询用户是否投递岗位
	 * @throws Exception
	 */
	@RequestMapping("seacherisResumeJob")
	public void seacherisResumeJob(HttpServletRequest request,PrintWriter out) throws Exception{
    	String userId = request.getParameter("userId");
 		String advertiseId = request.getParameter("advertiseId");
 		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		if(StringUtil.isBlank(String.valueOf(advertiseId))){
			throw new BusinessException("岗位编号不能缺少.",-1);
		}
		Map map = getMap(request);
		Map m1 = userService.seacherisResumeJob(map);
		Map m2 = new HashMap<String, String>();
		if(m1!=null){
			m2.put("id", m1.get("id"));
		}else{
			m2.put("id", "0");
		}
		out.print(WebUtils.responseData(m2));
 	}
	
	
	/**
	 * 查询用户是否收藏岗位
	 * @throws Exception
	 */
	@RequestMapping("seacherisCollectionJob")
	public void seacherisCollectionJob(HttpServletRequest request,PrintWriter out) throws Exception{
    	String userId = request.getParameter("userId");
 		String advertiseId = request.getParameter("advertiseId");
 		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		if(StringUtil.isBlank(String.valueOf(advertiseId))){
			throw new BusinessException("岗位编号不能缺少.",-1);
		}
		Map map = getMap(request);
		Map m1 = userService.seacherisCollectionJob(map);
		Map m2 = new HashMap<String, String>();
		
		if(m1!=null){
			m2.put("collectionId", m1.get("collectionId"));
		}else{
			m2.put("collectionId", "0");
		}
		out.print(WebUtils.responseData(m2));
 	}
	
	
	/**
	 * 查询企业是否收藏简历
	 * @throws Exception
	 */
	@RequestMapping("seacherisCollectionResume")
	public void seacherisCollectionResume(HttpServletRequest request,PrintWriter out) throws Exception{
    	String userId = request.getParameter("userId");
 		String resumeId = request.getParameter("resumeId");
 		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		if(StringUtil.isBlank(String.valueOf(resumeId))){
			throw new BusinessException("简历编号不能缺少.",-1);
		}
		Map map = getMap(request);
		Map m1 = userService.seacherisCollectionResume(map);
		Map m2 = new HashMap<String, String>();
		
		if(m1!=null){
			m2.put("collectionId", m1.get("collectionId"));
		}else{
			m2.put("collectionId", "0");
		}
		out.print(WebUtils.responseData(m2));
 	}
	
	
	
	
	/**
	 * 用户收藏岗位
	 * @throws Exception
	 */

	@RequestMapping("collectionJob")
	public void collectionJob(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
		String advertiseId = request.getParameter("advertiseId");
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		if(StringUtil.isBlank(String.valueOf(advertiseId))){
			throw new BusinessException("岗位编号不能缺少.",-1);
		}
		Map map = getMap(request);
		map.put("collectionTime", new Date());
		userService.collectionJob(map);
		out.print(WebUtils.responseData(map));
	}

	/**
	 * 用户收藏简历
	 * @throws Exception
	 */
	@RequestMapping("collectionResume")
	public void collectionResume(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
		String resumeId = request.getParameter("resumeId");
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		if(StringUtil.isBlank(String.valueOf(resumeId))){
			throw new BusinessException("简历编号不能缺少.",-1);
		}
		Map map = getMap(request);
		map.put("collectionTime", new Date());
	    userService.collectionResume(map);
		out.print(WebUtils.responseData("收藏成功",1,map));		
	}
	
	
	

	/**
	 * 用户推荐简历
	 * @throws Exception
	 */
	 @RequestMapping("recommendResume")
	public void recommendResume(HttpServletRequest request,PrintWriter out) throws Exception{
		List<Map> list  =  userService.recommendResume();
		out.print(WebUtils.webResponsePage(list));
	}

	/**
	 * 最新招聘列表(说明 传userId,根据公司ID查该公司招聘岗位列表，否则查最新招聘列表)
	 * @throws Exception
	 */
	@RequestMapping("newJobList/{pageNo}/{pageSize}")
	public void newJobList( 
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out) throws Exception{
		 Map m= getMap(request);
		 Object model =  request.getParameter("model");
		 if(null ==  model || model.equals("")) {
			 model = 1;
		 }
		 /**
		  // model 前台：1 后台  3
		  String userId = request.getParameter("userId");
		  Map map = new HashMap<String, String>();
		  map.put("userId", userId);**/
	/*	 Object userType = m.get("userType");
		 if(null == userType ||  userType.equals("")) {
			 m.put("other", 1);
		 }*/
		  
		  Paging paging = new Paging(pageSize, pageNo,true);
		  List<Map> list = userService.newJobList(paging,m);
		  if(null != model) {
			   out.print(WebUtils.webResponsePage(list,Integer.valueOf(model.toString())));
		  }else {
			   out.println(WebUtils.webResponsePage(list));
		  }
		
	}
	
	/**
	 * 免费交通案件申请
	 * @throws Exception
	 */
	@RequestMapping("freeCaseDeclare")
	public void freeCaseDeclare(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		
		int i = userService.freeCaseDeclare(userId);
			if(i>0){
				out.print(WebUtils.responseMsg("申请成功"));
			}else{
				out.print(WebUtils.responseMsg("申请失败"));
			}
	}
	
	
	/**
	 * 免费文书制作申请
	 * @throws Exception
	 */
	@RequestMapping("freeMakevoucher")
	public void freeMakevoucher(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
		String voucherId = request.getParameter("voucherId");
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		if(StringUtil.isBlank(String.valueOf(voucherId))){
			throw new BusinessException("文书编号不能缺少.",-1);
		}
		Map map  = getMap(request);
		int i = userService.freeMakevoucher(map);
		if(i>0){
			out.print(WebUtils.responseMsg("申请成功"));
		}else{
			out.print(WebUtils.responseMsg("申请失败",-4));
		}
	}
	
	
	/**
	 * 使用文书劵  
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("useVoucherBookDeclare")
	public void useVoucherBookDeclare(HttpServletRequest request,PrintWriter out) throws Exception{
		
	}

	
	/**
	 * 文书劵列表
	 * @throws Exception
	 */
	@RequestMapping("voucherList/{pageNo}/{pageSize}")
	public void voucherList(HttpServletRequest request,PrintWriter out,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize) throws Exception{
		Paging paging = new Paging(pageSize, pageNo,true);
		String userId  = request.getParameter("userId");
		Map map = new HashMap<String, String>();
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		map.put("userId", userId);
		map.put("state", request.getParameter("state"));
		List<Map> eduList = userService.voucherList(map,paging);
		out.print(WebUtils.webResponsePage(eduList));
	}
	/**
	 * 文书劵列表计数
	 * @throws Exception
	 */
	@RequestMapping("voucherCount")
	public void voucherCount(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId  = request.getParameter("userId");
		Map map = new HashMap<String, String>();
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		map.put("userId", userId);
		map.put("state", request.getParameter("state"));
		Map m = userService.voucherCount(map);
		out.print(WebUtils.responseData(m));
	}
	
	
	 
	
	
	@ResponseBody
	@RequestMapping("/UpdateUserNameAtPay")
	public String UpdateUserNameAtPay(
			HttpServletRequest request
			) throws Exception{
		Map<String,String> returnMap = new HashMap<String, String>();
		String id = request.getParameter("userId");
		String name = request.getParameter("userName");
		String type = request.getParameter("userType");
		Map Map = new HashMap<String, String>();
		Map.put("userId", id);
		Map.put("userName", name);
		try {
			if(type=="1") {
				userService.updateConsultantInfo(Map);
			}else if(type=="2"){
				userService.counselorAuthentication(Map);
			}
		} catch (Exception e) {
			returnMap.put("msg", "N");
			return JSON.toJSONString(returnMap) ;
		}
		returnMap.put("msg", "Y");
		return JSON.toJSONString(returnMap) ;
	}
	
	/**
	 * 个人咨询者保存个人信息
	 * @throws Exception
	 */
	@RequestMapping("saveUserInfo")
	public void saveUserInfo(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
		String nickName = request.getParameter("nickName");
		String userName = request.getParameter("userName");
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String realName = request.getParameter("realName");
		if(StringUtil.isBlank(userId)){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		if(StringUtil.isBlank(nickName)){
			throw new BusinessException("用户昵称不能缺少.",-1);
		}
		if(StringUtil.isBlank(userName)){
			throw new BusinessException("用户名不能缺少.",-1);
		}
		if(StringUtil.isBlank(province)){
			throw new BusinessException("所在省份不能缺少.",-1);
		}
		if(StringUtil.isBlank(city)){
			throw new BusinessException("所在城市不能缺少.",-1);
		}
		Map userMap = new HashMap<String, String>();
		userMap.put("userId", Integer.parseInt(userId));
		userMap.put("nickName", nickName);
		userMap.put("userName", userName);
		userMap.put("province", province);
		userMap.put("city", city);
		userMap.put("realName", realName);
		Map map  = userService.getUserInfo(userId);
		String  userType = map.get("userType").toString();
		Map nickNameMap = new HashMap<String, String>();
		nickNameMap.put("nickName", nickName);
		nickNameMap.put("userType", userType);
		nickNameMap.put("province", province);
		nickNameMap.put("city", city);
		//List<Map> nList  = userService.getUserInfoByMobileOruserName(nickNameMap);
		List<Map> nList  = userService.checkNickNameisReg(nickNameMap);
 		if(nList.size()>0){
			throw new BusinessException("该昵称已经注册", -1);
		} 
		String isAuthentication = map.get("isAuthentication").toString();
		int i=0; int j=0;
		if(isAuthentication.equals("2")){
				 i = userService.updateUserInfo(userMap);
				 userMap.remove("realName");
				 if(userType.equals("1")){
					 j = userService.updateConsultantInfo(userMap);
				 }else if(userType.equals("2")){
					 j = userService.updateCounselorInfo(userMap);
				 }
		 }else{
				 i = userService.updateUserInfo(userMap);
				 if(userType.equals("1")){
					 j = userService.updateConsultantInfo(userMap);
 				 }else if(userType.equals("2")){ 						 
 					 j = userService.updateCounselorInfo(userMap);
 				 }
		 }
		out.print(WebUtils.responseMsg("个人资料保存成功"));
	}
	
	
	@RequestMapping("editPassword")
	public void editPassword(HttpServletRequest request,PrintWriter out) throws Exception{
		//修改密码
		String userId = request.getParameter("userId");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		
		if(StringUtil.isBlank(userId)){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		
		if(StringUtil.isBlank(oldPassword)){
			throw new BusinessException("原来的密码不能缺少.",-1);
		}
		
		if(StringUtil.isBlank(newPassword)){
			throw new BusinessException("新的密码不能缺少.",-1);
		}
		
		Map m = new HashMap();
		m.put("userId", Integer.parseInt(userId));
		m.put("oldPassword", oldPassword);
		m.put("newPassword", newPassword);
		//修改密码
		userService.editPassword(m);

		Map<String, Object> m1 = new HashMap<String, Object>();
		m1.put("isRead","0"); //0:没读 1：已读
		m1.put("sendTime",new Date()); //0:没读 1：已读
		m1.put("isDelete","0"); //0:未删除 1：已删除
		m1.put("messageState","0"); // 
		m1.put("is_send","1"); //0:未发送1：已发送
		m1.put("create_time",new Date()); 	
		m1.put("userId",userId); 
		m1.put("messageContent","尊敬的用户您好，您的登录密码已经重置，您可用新密码进行登录。");
		m1.put("messageType","修改秘密");
		m1.put("businessType","2");
		m1.put("businessId",userId);
		
	    int j = userService.messageSend(m1);
	    userService.getUserInfo(userId).get("mobile");
		out.print(WebUtils.responseMsg("修改成功."));
		
	}
	
	/**
	 * 找回密码
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("findPassword")
	public void efindPassword(HttpServletRequest request,PrintWriter out) throws Exception{
		//修改密码
		String mobile = request.getParameter("mobile");
		String newPassword = request.getParameter("newPassword");
		String code = request.getParameter("code");
		if(StringUtil.isBlank(mobile)){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		
		if(StringUtil.isBlank(newPassword)){
			throw new BusinessException("新的密码不能缺少.",-1);
		}
		
		/*
		 * if(StringUtil.isBlank(code)){ throw new BusinessException("短信验证码不能缺少.",-1); }
		 */
		
		//从缓存获取验证码
		/*
		 * Object cacheCode = cache.get(mobile);
		 * 
		 * if(cacheCode==null){ throw new BusinessException("短信验证码失效.",-1); }
		 * 
		 * if(!code.equals(cacheCode.toString())){ throw new
		 * BusinessException("短信验证码错误.",-1); }
		 */
		
		
		
		Map m = new HashMap();
		m.put("mobile", mobile);
		m.put("newPassword", newPassword);
		//修改密码
		userService.editPasswordByMobile(m);
		Map umap =(Map)userService.getUserInfoByMobileOruserName(m);
		String userId="";
		if(null!=umap) {
			throw new BusinessException("用户不存在",-1);
		}else {
			userId = userService.getUserInfoByMobileOruserName(m).get(0).get("userId").toString();
		}
		Map<String, Object> m1 = new HashMap<String, Object>();
		m1.put("isRead","0"); //0:没读 1：已读
		m1.put("sendTime",new Date()); //0:没读 1：已读
		m1.put("isDelete","0"); //0:未删除 1：已删除
		m1.put("messageState","0"); // 
		m1.put("is_send","1"); //0:未发送1：已发送
		m1.put("create_time",new Date()); 	
		m1.put("userId",userId); 
		m1.put("messageContent","您成功找回密码！");
		//m1.put("messageType","1");
		m1.put("messageType","修改秘密");
		m1.put("businessId",userId+mobile);
		
	    int j = userService.messageSend(m1);
	   // smsLt.sendUppd(userService.getUserInfo(userId).get("mobile").toString());
		out.print(WebUtils.responseMsg("成功找回密码."));
	}
	
	/**
	 * 重新绑定手机号码
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("editMobile")
	public void editMobile(HttpServletRequest request,PrintWriter out) throws Exception{
		
		String userId = request.getParameter("userId"); //用户id
		String smsCode = request.getParameter("smsCode");//短信验证码
		String newMobile = request.getParameter("newMobile");//手机号码
		
		if(StringUtil.isBlank(userId)){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		
		if(StringUtil.isBlank(smsCode)){
			throw new BusinessException("短信验证码不能缺少.",-1);
		}
		
		if(StringUtil.isBlank(newMobile)){
			throw new BusinessException("新绑定的手机号码不能缺少.",-1);
		}
		
		//从缓存获取短信验证码
		Object code = cache.get(newMobile);
		
		if(code == null){
			throw new BusinessException("短信验证码失效.", -1);
		}
		
		if(!code.toString().equals(smsCode)){
			throw new BusinessException("短信验证码失效.", -1);
		}
		Map map = new HashMap();
		map.put("mobile", newMobile);

		List list = userService.getUserInfoByMobileOruserName(map);
		
		if(list.size()>0){
			throw new BusinessException("该手机号码已经存在.",-1);
		}

		Map m = new HashMap();
		m.put("userId", Integer.parseInt(userId));
		m.put("newMobile", newMobile);
		//绑定新号码
		userService.editMobile(m);
		out.print(WebUtils.responseMsg("绑定成功."));
		
	}
	
	
	

	/**
	 * 校验手机校验是否正确
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("checkCode")
	public void checkCode(HttpServletRequest request,PrintWriter out) throws Exception{
		String smsCode = request.getParameter("smsCode");//短信验证码
		String mobile = request.getParameter("mobile");//手机号
		if(StringUtil.isBlank(smsCode)){
			throw new BusinessException("短信验证码不能缺少.",-1);
		}
		
		if(StringUtil.isBlank(mobile)){
			throw new BusinessException("手机号码不能缺少.",-1);
		}
		//从缓存获取短信验证码
		Object code = cache.get(mobile);
		
		if(code == null){
			throw new BusinessException("校验失败", -1);
		}
		
		if(!code.toString().equals(smsCode)){
			throw new BusinessException("校验失败", -1);
		}
		out.print(WebUtils.responseMsg("校验通过"));
		
	}
	
	
	/**
	 * 根据个人主页地址 获取  用户id
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("getUserIdByPersonUrl")
	public void getUserIdByPersonUrl(HttpServletRequest request,PrintWriter out) throws Exception{
		
		String url = request.getParameter("url");
		if(StringUtil.isBlank(url)){
			throw new BusinessException("缺失咨询师个人主页地址.", -1);
		}
		int userId = userService.getUserIdByPersonUrl(url);
		if(userId == 0){
			throw new BusinessException("用户信息不存在.", -1);
		}
		out.print(WebUtils.responseData(userId));
	}
	
	/**
	 * 根据用户id更改头像
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("changeUserProfile")
	public void changeUserProfile(HttpServletRequest request,PrintWriter out) throws Exception{
		//用户id
		String userId = request.getParameter("userId");
		//用户新头像
		String newImg = request.getParameter("newImg");
		
		Map map = new HashMap();
		
		if(StringUtil.isBlank(userId)){
			throw new BusinessException("用户编号缺失.", -1);
		}
		if(StringUtil.isBlank(newImg)){
			throw new BusinessException("用户新头像缺失.", -1);
		}
		map.put("userId", Integer.parseInt(userId));
		map.put("userImg", newImg);
		try {
			userService.changeUserProfile(map);
			out.print(WebUtils.responseMsg("更改成功."));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("更改头像异常.",-1);
		}
	}

	/**
	 * 根据用户id获取我收藏的咨询师
	 * @throws Exception
	 */
	@RequestMapping("myCollectionConsultant/{userId}/{pageNo}/{pageSize}")
	public void myCollectionConsultant(@PathVariable("userId") int userId,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,HttpServletRequest request,PrintWriter out) throws Exception{
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
	    Paging p = new Paging(pageSize, pageNo,true);
	    Map m = new HashMap<String, String>();
	    m.put("userId", userId);
		List<Map> map = userService.myCollectionConsultant(p,m);
		out.print(WebUtils.webResponsePage(map));
	}
	
	
	

	/**
	 * 根据用户id获取用户岗位收藏
	 * @throws Exception
	 */
	@RequestMapping("collectionJobList/{userId}/{pageNo}/{pageSize}")
	public void collectionJobList(HttpServletRequest request,PrintWriter out,
			@PathVariable("userId") int userId,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize) throws Exception{
		Paging paging = new Paging(pageSize, pageNo,true);
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		List<Map> map = userService.collectionJobList(userId,paging);
		out.print(WebUtils.webResponsePage(map));
	}
	
 
	
	

	/**
	 * 根据用户id获取用户收到简历投递列表
	 * @throws Exception
	 */
 	@RequestMapping("collecResumeList/{userId}/{pageNo}/{pageSize}")
	public void collecResumeList(HttpServletRequest request,PrintWriter out,
			@PathVariable("userId") int userId,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize) throws Exception{
		Paging paging = new Paging(pageSize, pageNo,true);
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		List<Map> map = userService.collecResumeList(userId,paging);
		out.print(WebUtils.webResponsePage(map));
	}
	
	/**
	 * 根据用户id获取用户订单列表
	 * @throws Exception
	 */
	@RequestMapping("businessOrderList")
	public void businessOrderList(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId"); //用户id
		String orderState = request.getParameter("orderState");//订单状态
		String businessOrder = request.getParameter("businessOrder");//订单号码
		
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		Map m = new HashMap();
		m.put("userId", Integer.parseInt(userId));
		if(!StringUtil.isBlank(orderState)){
			m.put("orderState", orderState);
		}
		if(!StringUtil.isBlank(businessOrder)){
			m.put("businessOrder", businessOrder);
		}
		Map map = userService.businessOrderList(m);
		out.print(WebUtils.responseData(map));
	}
	
	
	/**
	 * 根据用户id删除用户简历信息
	 * @throws Exception
	 */
	@RequestMapping("deleteResumeInfo")
	public void deleteResumeInfo(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId"); //用户id
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
 
		int i = userService.deleteResumeInfo(Integer.parseInt(userId));
		out.print(WebUtils.responseMsg("简历删除成功."));
	}
	
	/**
	 * 用户关注 /取消关注咨询师
	 * @throws Exception
	 */
	@RequestMapping("insertAliFollow")
	public void insertAliFollow(HttpServletRequest request,PrintWriter out) throws Exception{
		String type = request.getParameter("type"); //类型 1：关注 0 取消关注
		if(type.equals("1")){
			String userId = request.getParameter("userId"); //用户id
			if(StringUtil.isBlank(String.valueOf(userId))){
				throw new BusinessException("用户编号不能缺少.",-1);
			}
			String counselorId = request.getParameter("counselorId"); //咨询师id
			if(StringUtil.isBlank(String.valueOf(counselorId))){
				throw new BusinessException("咨询师编号不能缺少.",-1);
			} 
			Map map  = super.getMap(request);
			map.put("followTime", new Date());
			Map m = userService.seacherAliFollow(map);
			if(m!=null){
				throw new BusinessException("该咨询师已关注.",-1);
			}
			int i = userService.insertAliFollow(map);
			out.print(WebUtils.responseData(map));
		}else{
			String followId = request.getParameter("followId"); //用户id
			if(StringUtil.isBlank(String.valueOf(followId))){
				throw new BusinessException("关注ID不能缺少.",-1);
			}
			int i = userService.deleteAliFollow(followId);
			out.print(WebUtils.responseMsg("取消成功"));

		}
		
		
	}
	
	
	
	/**
	 * 用户是否关注咨询师
	 * @throws Exception
	 */
	@RequestMapping("isAliFollow")
	public void isAliFollow(HttpServletRequest request,PrintWriter out) throws Exception{
			String userId = request.getParameter("userId"); //用户id
			if(StringUtil.isBlank(String.valueOf(userId))){
				throw new BusinessException("用户编号不能缺少.",-1);
			}
			String counselorId = request.getParameter("counselorId"); //咨询师id
			if(StringUtil.isBlank(String.valueOf(counselorId))){
				throw new BusinessException("咨询师编号不能缺少.",-1);
			} 
			Map map  = super.getMap(request);
			Map m1 = userService.seacherAliFollow(map);
			Map m2 = new HashMap<String, String>();
			if(null!=m1){
				m2.put("followId", m1.get("followId").toString());
			}else{
				m2.put("followId", "0");
			}
			out.print(WebUtils.responseData(m2));
	}
	
	
	/**
	 *	用户投递过的岗位列表
	 * @throws Exception
	 */
	@RequestMapping("userSendAdvertise/{userId}/{pageNo}/{pageSize}")
	public void userSendAdvertise(HttpServletRequest request,PrintWriter out,
			@PathVariable("userId") int userId,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize) throws Exception{
		Paging paging = new Paging(pageSize, pageNo,true);
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		List<Map> map = userService.userSendAdvertise(userId,paging);
		out.print(WebUtils.webResponsePage(map));
	}
	

	/**
	 *	用户删除投递过的岗位列表
	 * @throws Exception
	 */
	@RequestMapping("userDelSendAdvertise")
	public void userDelSendAdvertise(HttpServletRequest request,PrintWriter out) throws Exception{
		String Id = request.getParameter("Id");
 		if(StringUtil.isBlank(String.valueOf(Id))){
			throw new BusinessException("岗位Id不能缺少.",-1);
		}
 		int i = userService.userDelSendAdvertise(Id);
		out.print(WebUtils.responseMsg("删除成功"));
	}

	/**
	 *	删除用户收藏的岗位列表
	 * @throws Exception
	 */
	@RequestMapping("deleteCollectionJobList/{collectionId}")
	public void deleteCollectionJobList(HttpServletRequest request,PrintWriter out,@PathVariable("collectionId") int collectionId) throws Exception{
  		if(StringUtil.isBlank(String.valueOf(collectionId))){
			throw new BusinessException("收藏岗位Id不能缺少.",-1);
		}
 		int i = userService.deleteCollectionJobList(String.valueOf(collectionId));
		out.print(WebUtils.responseMsg("删除成功"));
	}
	
	/**
	 *	删除用户收藏的简历列表
	 * @throws Exception
	 */
	@RequestMapping("deleteCollectionResumeList")
	public void deleteCollectionResumeList(HttpServletRequest request,PrintWriter out) throws Exception{
		String collectionId = request.getParameter("collectionId");
 		if(StringUtil.isBlank(String.valueOf(collectionId))){
			throw new BusinessException("收藏简历Id不能缺少.",-1);
		}
 		int i = userService.deleteCollectionResumeList(collectionId);
		out.print(WebUtils.responseMsg("删除成功"));
	}


	/**
	 *	企业删除用户投递过的岗位列表
	 * @throws Exception
	 */
	@RequestMapping("companyDelSendAdvertise")
	public void companyDelSendAdvertise(HttpServletRequest request,PrintWriter out) throws Exception{
		String Id = request.getParameter("Id");
 		if(StringUtil.isBlank(String.valueOf(Id))){
			throw new BusinessException("岗位Id不能缺少.",-1);
		}
 		int i = userService.companyDelSendAdvertise(Id);
		out.print(WebUtils.responseMsg("删除成功"));
	}

	
	/**
	 *	消息设置
	 * @throws Exception
	 */
	@RequestMapping("msgSetting")
	public void msgSetting(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
		String type = request.getParameter("type");
		String state = request.getParameter("state");

 		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}

 		if(StringUtil.isBlank(String.valueOf(type))){
			throw new BusinessException("消息类型不能缺少.",-1);
		}

 		if(StringUtil.isBlank(String.valueOf(state))){
			throw new BusinessException("消息状态不能缺少.",-1);
		}
 		Map map = getMap(request);
 		int i = userService.msgSetting(map);
		out.print(WebUtils.responseMsg("设置成功"));
	}
	
	
	/**
	 *	消息设置结果查询
	 * @throws Exception
	 */
	@RequestMapping("msgSeacher")
	public void msgSeacher(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
 		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		Map m = userService.getUserInfo(userId);//is_accept_replylMsg, is_accept_sysMsg
		
		out.print(WebUtils.responseData(m));
	}
	
	
	/**
	 *咨询师推荐列表
	 *业务说明：首先按加v等级排序，同样等级按按总综合评分排序 按总综合评分= 文书制作数+解答咨询数+接受委托数+综合评分
	 * @throws Exception
	 */
	@RequestMapping("counselorRecommendList")
	public void counselorRecommendList(HttpServletRequest request,PrintWriter out) throws Exception{
		String city = request.getParameter("city");
		Map map = new HashMap<String, String>();
		if(null!=city&&!city.equals("")){
			map.put("city", city);
		}
 		List<Map> list  = userService.counselorRecommendList(map);
		out.print(WebUtils.responseData(list));
	}
	
	
	/**
	 *后台咨询师推荐列表
	 */
	@RequestMapping("adminRecommendCounselorList")
	public void adminRecommendCounselorList(HttpServletRequest request,PrintWriter out) throws Exception{
 		List<Map> list  = userService.adminRecommendCounselorList();
		out.print(WebUtils.responseData(list));
	}

	

	/**
	 *
	 *用户意见反馈
	 * @throws Exception
	 */
	@RequestMapping("userSuggest")
	public void userSuggest(HttpServletRequest request,PrintWriter out) throws Exception{
		String suggestTitle = request.getParameter("suggestTitle");
		String suggestContent = request.getParameter("suggestContent");
		String userId = request.getParameter("userId");
		String suggestName = request.getParameter("suggestName");
		String mobile = request.getParameter("mobile");
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		if(StringUtil.isBlank(String.valueOf(suggestContent))){
			throw new BusinessException("反馈内容不能缺少.",-1);
		}
		if(StringUtil.isBlank(String.valueOf(suggestName))){
			throw new BusinessException("反馈人姓名不能缺少.",-1);
		}
		if(StringUtil.isBlank(String.valueOf(mobile))){
			throw new BusinessException("手机号码不能缺少.",-1);
		}
		Map map = getMap(request);
		map.put("suggestTime", new Date());
		map.put("isComplate", "0");//0:未完成 1:已完成
		map.put("isDelete", "0");//1:已删除 0:未删除
		int i  = userService.userSuggest(map);
		out.print(WebUtils.responseMsg("反馈成功"));
	}

	/**
	 *
	 *招聘列表查询(招聘岗位高级搜索)
	 * @throws Exception
	 */
	@RequestMapping("advertiseSearch/{pageNo}/{pageSize}")
	public void advertiseSearch(HttpServletRequest request,PrintWriter out,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize)throws Exception{
/*		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String sal = request.getParameter("sal");//0:面议 1:2000-4000 2:4000-6000 3:6000-8000 4:8000-10000 5:10000-15000 6:15000 以上
		String qualification = request.getParameter("qualification");//0:无要求 1:高中  2 大专 3 本科 4 研究生及以上 
		String workExperience = request.getParameter("workExperience");//0 无要求 1：一年以上 2 三年以上 3 五年以上
		String companyName = request.getParameter("companyName");
		String jobName = request.getParameter("jobName");*/
		Paging paging = new Paging(pageSize, pageNo,true);
		Map map = getMap(request);
		List<Map> list  = userService.advertiseSearch(map,paging);
		out.print(WebUtils.webResponsePage(list));
	}
	
	
	/**
	 * 简历列表查询（人才简历高级搜索）
	 * @throws Exception
	 */
	@RequestMapping("userResumeSearch/{pageNo}/{pageSize}")
	public void userResumeSearch(HttpServletRequest request,PrintWriter out,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize)throws Exception{
		Paging paging = new Paging(pageSize, pageNo,true);
		Map map = getMap(request);
		List<Map> list  = userService.userResumeSearch(map,paging);
		out.print(WebUtils.webResponsePage(list));
	}
	
 
	/**
	 * 获取广告详情
	 * @throws Exception
	 */
	@RequestMapping("getAdvertisingById/{id}")
	public void getAdvertisingById(HttpServletRequest request,PrintWriter out,
			@PathVariable("id") String id)throws Exception{
 		if(StringUtil.isBlank(String.valueOf(id))){
			throw new BusinessException("广告ID不能缺少.",-1);
		}
		Map map  = userService.getAdvertisingById(String.valueOf(id));
		out.print(WebUtils.responseData(map));
	}
	
	 
		/**
		 * 根据用户id获取用户免费咨询资格 (0:无 1:有)
		 * @param request
		 * @param out
		 * @throws Exception
		 */
		@RequestMapping("getUserConsultantState/{userId}")
		public void getUserConsultantState(HttpServletRequest request,PrintWriter out,
				@PathVariable("userId") int userId)throws Exception{
	 		if(StringUtil.isBlank(String.valueOf(userId))){
				throw new BusinessException("用户ID不能缺少.",-1);
			}
			int state = userService.getUserConsultantState(userId);
			Map map =new HashMap<String, String>();
			map.put("state", state);
			out.print(WebUtils.responseData(map));
		}
		
	
	/**
	 * 站内搜索（法律法规，法律咨询）
	 * @throws Exception
	 */
	@RequestMapping("localSearch/{pageNo}/{pageSize}")
	public void localSearch(HttpServletRequest request,PrintWriter out,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize)throws Exception{
		Paging paging = new Paging(pageSize, pageNo,true);
		Map map = getMap(request);
		String type = request.getParameter("type");
		List<Map> list  = userService.localSearch(map,paging);
		out.print(WebUtils.webResponsePage(list));
	}
	
	/**
	 * 站内消息查询（消息列表）
	 * @throws Exception
	 */
	@RequestMapping("messageSearch/{pageNo}/{pageSize}")
	public void messageSearch(HttpServletRequest request,PrintWriter out,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize)throws Exception{

		String businessType = request.getParameter("businessType"); //0：回复消息 1：系统消息
		String userId = request.getParameter("userId");
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
 		Paging paging = new Paging(pageSize, pageNo,true);
		Map map = getMap(request);
		List<Map> list  = userService.messageSearch(map,paging);
		out.print(WebUtils.webResponsePage(list));
	}
	
	
	/**
	 * 站内消息类型计数（消息列表）//0：回复消息 1：系统消息  空：所有消息
	 * @throws Exception
	 */
	@RequestMapping("messageTypeCount")
	public void messageTypeCount(HttpServletRequest request,PrintWriter out)throws Exception{

		String userId = request.getParameter("userId");
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		//String businessType = request.getParameter("businessType"); //0：回复消息 1：系统消息
		Map map = getMap(request);
		Map m = userService.messageTypeCount(map);
		out.print(WebUtils.responseData(m));
	}
	

	/**
	 * 站内消息发送
	 * @throws Exception
	 */
	@RequestMapping("messageSend")
	public void messageSend(HttpServletRequest request,PrintWriter out)throws Exception{
		String businessType = request.getParameter("businessType"); //0：回复消息 1：系统消息
		String userId = request.getParameter("userId");
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		String messageContent = request.getParameter("messageContent");
		if(StringUtil.isBlank(String.valueOf(messageContent))){
			throw new BusinessException("消息内容不能缺少.",-1);
		}

		if(StringUtil.isBlank(String.valueOf(businessType))){  
			throw new BusinessException("业务类型不能缺少.",-1);
		}
		String businessId = request.getParameter("businessId");
		if(StringUtil.isBlank(String.valueOf(businessId))){
			throw new BusinessException("业务编号不能缺少.",-1);
		}
		String messageType = request.getParameter("messageType");
		if(StringUtil.isBlank(String.valueOf(messageType))){
			throw new BusinessException("消息类型.",-1);
		}
		Map map = getMap(request);
		map.put("isRead","0"); //0:没读 1：已读
		map.put("sendTime",new Date()); //0:没读 1：已读
		map.put("isDelete","0"); //0:未删除 1：已删除
		map.put("messageState","0"); // 
		map.put("is_send","1"); //0:未发送1：已发送
		map.put("create_time",new Date()); 
		
		int i= userService.messageSend(map);
		out.print(WebUtils.responseMsg("消息发送成功"));
	}
	
	
	/**
	 * 站内消息详情
	 * @throws Exception
	 */
	@RequestMapping("messageDetailsSearch")
	public void messageDetailsSearch(HttpServletRequest request,PrintWriter out)throws Exception{
		String messageId = request.getParameter("messageId");
		String userId = request.getParameter("userId");
		if(StringUtil.isBlank(String.valueOf(messageId))){
			throw new BusinessException("消息详情ID不能缺少.",-1);
		}
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		Map msg = new HashMap<String, String>();
		msg.put("userId", userId);
		msg.put("messageId", messageId);

  		Map m  = userService.messageDetailsSearch(msg);

  		Map up  = userService.upmessageDetailsSearch(msg);
  		
  		if(up!=null){
  			m.put("upMessageId", up.get("messageId").toString());
  		}else{
  			m.put("upMessageId", "");
  		}

  		Map next  = userService.nextmessageDetailsSearch(msg);
  		if(next!=null){
  			m.put("nextMessageId", next.get("messageId").toString());
  		}else{
  			m.put("nextMessageId", "");
  		}
		out.print(WebUtils.responseData(m));
	}
	
	

	/**
	 * 删除消息列表
	 * @throws Exception
	 */
	@RequestMapping("deleteMessage")
	public void deleteMessage(HttpServletRequest request,PrintWriter out)throws Exception{
		String [] messageIds = request.getParameterValues("messageIds"); 
	/*	String userId = request.getParameter("userId"); 
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}*/
		Map map = new HashMap<String, Object>();
	/*	map.put("userId", userId);*/
		map.put("messageIds", messageIds);
		int i =  userService.deleteMessage(map);
		out.print(WebUtils.responseMsg("删除成功"));
	}
	

	/**
	 * 标记消息列表已读 (0:未读 1:已读)
	 * @throws Exception
	 */
	@RequestMapping("updateMessage")
	public void updateMessage(HttpServletRequest request,PrintWriter out)throws Exception{
		String userId = request.getParameter("userId"); 
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		String messageId = request.getParameter("messageId"); 
		Map map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("messageId", messageId);
		int i =  userService.updateMessage(map);
		out.print(WebUtils.responseMsg("标记成功"));
	}
	
	
	

	/**
	 *	添加日程
	 * @throws Exception
	 */	
	@RequestMapping("insertSchedule")
	public void insertSchedule(HttpServletRequest request,PrintWriter out) throws Exception{
		String userId = request.getParameter("userId");
		String content = request.getParameter("content");
		String name = request.getParameter("name");
		String  gregorianCalendar[] = request.getParameter("gregorianCalendar".trim()).split(","); 

 		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}

 		if(StringUtil.isBlank(String.valueOf(name))){
			throw new BusinessException("日程不能缺少.",-1);
		}

 		if(gregorianCalendar.length==0){
			throw new BusinessException("日期不能缺少.",-1);
		}
 		
 		if(StringUtil.isBlank(String.valueOf(content))){
			throw new BusinessException("提醒内容不能缺少.",-1);
		}
 	
 		Map map = new HashMap();
 		map.put("name", name);
 		map.put("userId", Integer.parseInt(userId));
 		map.put("content", content);
 		int count = userService.insertSchedule(map);
 		String scheduleId = map.get("id").toString();
 		Map ScheduleReminderTimeMap = new HashMap();
		if(gregorianCalendar!=null&&gregorianCalendar.length>0){
			List<Map> scheduleList = new ArrayList<Map>();
			for (int i = 0; i < gregorianCalendar.length; i++) {
				ExceptionLogger.writeLog("日程："+gregorianCalendar[i]);
				String week = Tools.dateToWeek(gregorianCalendar[i]);
		 		String lunarCalendar =LunarUtil.getLunarDate(gregorianCalendar[i]).toString();
				Map m = new HashMap();
				m.put("userId", Integer.parseInt(userId));
				m.put("gregorianCalendar", gregorianCalendar[i]);
		 		m.put("lunarCalendar", lunarCalendar);
		 		m.put("week", week);
		 		m.put("scheduleId", scheduleId);

		 		scheduleList.add(m);
			}
			ScheduleReminderTimeMap.put("scheduleList", scheduleList);
		}
 		int i = userService.insertScheduleReminderTime(ScheduleReminderTimeMap);
		out.print(WebUtils.responseMsg("添加成功"));
	}
	
	/**
	 * 日程查询
	 * @throws Exception
	 */
	@RequestMapping("scheduleSearch/{pageNo}/{pageSize}")
	public void scheduleSearch(HttpServletRequest request,PrintWriter out,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize)throws Exception{
		String  gregorianCalendar = request.getParameter("gregorianCalendar");
		String content = request.getParameter("content");
		String startTime = Tools.thisMonthStart(gregorianCalendar);
		String endTime = Tools.thisMonthEnd(gregorianCalendar);
		String userId = request.getParameter("userId");

		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("userId", userId);
		map.put("content", content);
		Paging paging = new Paging(pageSize, pageNo,true);
		List<Map> list  = userService.scheduleSearch(map,paging);
		out.print(WebUtils.webResponsePage(list));
	}
	
	

	/**
	 * 日程查询所有
	 * @throws Exception
	 */
	@RequestMapping("scheduleSearchALL/{pageNo}/{pageSize}")
	public void scheduleSearchALL(HttpServletRequest request,PrintWriter out,@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize)throws Exception{
		String content = request.getParameter("content");
		String userId = request.getParameter("userId");

		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("content", content);
		Paging paging = new Paging(pageSize, pageNo,true);
		List<Map> list  = userService.scheduleSearchALL(map,paging);
		out.print(WebUtils.webResponsePage(list));
	}
	
	
	/**
	 * 日程删除
	 * @throws Exception
	 */
	@RequestMapping("deleteSchedule")
	public void deleteSchedule(HttpServletRequest request,PrintWriter out)throws Exception{
		String id = request.getParameter("id");
		if(StringUtil.isBlank(String.valueOf(id))){
			throw new BusinessException("Id不能缺少.",-1);
		}
		int i = userService.deleteSchedule(id);
		out.print(WebUtils.responseMsg("删除成功"));
	}
	

	
	/**
	 * 日程时间删除
	 * @throws Exception
	 */
	@RequestMapping("deleteScheduleReminderTime")
	public void deleteScheduleReminderTime(HttpServletRequest request,PrintWriter out)throws Exception{
		String id = request.getParameter("id");
		if(StringUtil.isBlank(String.valueOf(id))){
			throw new BusinessException("Id不能缺少.",-1);
		}
		int i = userService.deleteScheduleReminderTime(id);
		out.print(WebUtils.responseMsg("删除成功"));
	}
	
	
	/**
	 * 日程内容修改
	 * @throws Exception
	 */
	@RequestMapping("updateSchedule")
	public void updateSchedule(HttpServletRequest request,PrintWriter out)throws Exception{
		String id = request.getParameter("id");
		String content = request.getParameter("content");
		String name = request.getParameter("name");
		if(StringUtil.isBlank(String.valueOf(id))){
			throw new BusinessException("Id不能缺少.",-1);
		}
		Map map = getMap(request);
		int i = userService.updateSchedule(map);
		out.print(WebUtils.responseMsg("修改成功"));
	}
	
	
	/**
	 * 头条查询
	 * @throws Exception
	 */
	@RequestMapping("bannerSearch")
	public void bannerSearch(HttpServletRequest request,PrintWriter out)throws Exception{
		String  type = request.getParameter("type");//1：PC，2：APP
		String  is_available = request.getParameter("is_available");
		if(StringUtil.isBlank(String.valueOf(type))){
			throw new BusinessException("类型不能为空.",-1);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("is_available", is_available);
		List<Map> list  = userService.bannerSearch(map);
		out.print(WebUtils.responseData(list));
	}
	
	/**
	 * 个人主页开通
	 * @throws Exception
	 */
	@RequestMapping("openCounselorPersonUrl")
	public void openCounselorPersonUrl(HttpServletRequest request,PrintWriter out)throws Exception{
		String userId = request.getParameter("userId");
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		String personUrl = request.getParameter("personUrl");
		if(StringUtil.isBlank(String.valueOf(personUrl))){
			throw new BusinessException("个人主页地址不能缺少.",-1);
		}
		
		Map map = getMap(request);
		Map personUrlMap = userService.getUserConsultantInfo(map);
		if(null!=personUrlMap){
			throw new BusinessException("个人主页地址不能重复.",-1);
		}
		int i = userService.updateCounselorPersonInfo(map);
		out.print(WebUtils.responseMsg("个人主页开通成功"));
	}
		
	/**
	 *  咨询师个人主页设置
	 * @throws Exception
	 */
	@RequestMapping("settingCounselorInfo")
	public void settingCounselorInfo(HttpServletRequest request,PrintWriter out)throws Exception{
		String userId = request.getParameter("userId");
		if(StringUtil.isBlank(String.valueOf(userId))){
			throw new BusinessException("用户编号不能缺少.",-1);
		}
		Map map = getMap(request);
		int i = userService.updateCounselorPersonInfo(map);
		out.print(WebUtils.responseMsg("保存成功"));
	}
	
	
	
	
	
	
 



}
