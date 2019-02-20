package com.legal.app.controller;

import com.common.dbutil.Paging;
import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;
import com.common.utils.StringUtil;
import com.common.web.WebUtils;
import com.legal.app.service.AdviceService;
import com.legal.app.service.OrderService;
import com.legal.app.service.UserService;
import com.legal.app.utils.CreateOrderNumber;
import com.legal.app.utils.OrderType;
import com.legal.app.utils.PayOrder;
import com.legal.app.utils.VipUtils;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("advice")
public class AdviceController extends SuperController {
	
	@Autowired
	private AdviceService adviceService;
	
	@Autowired
	private UserService userService;
	

	@Autowired
	private OrderService orderService;
	
	/*@RequestMapping({ "/adviceList/{queryType}/{pageNo}/{pageSize}" })
	public void queryAdviceList(@PathVariable("queryType") int queryType,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize, PrintWriter out)
			throws Exception {
		List<Object> list = new ArrayList();
		for (int i = 0; i < 10; i++) {
			Map map = new HashMap();
			map.put("adviceId", Integer.valueOf(i + 1));
			map.put("userId", "10000001");
			map.put("nickName", "巅峰男人");
			map.put("userImg", "/imgs/ldf.png");
			map.put("caseType", Integer.valueOf(1));
			map.put("caseTypeName", "交通事故");

			map.put("reward", Integer.valueOf(30));
			map.put("sendTime", "2018-06-18 12:00");
			map.put("replyNum", Integer.valueOf(i % 4));
			list.add(map);
		}
		Paging p = new Paging(pageSize, pageNo);
		p.setTotalCount(28);
		list.add(p);
		out.print(WebUtils.webResponsePage(list));
	}*/

	/**
	 * 法律咨询列表
	 * @param queryType
	 * @param caseId
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping({"/adviceList/{pageNo}/{pageSize}" })
	public void queryAdviceList(
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize, 
			HttpServletRequest request,
			PrintWriter out)
			throws Exception {
		
		Paging p = new Paging(pageSize, pageNo, true);//分页对象
		
		Map<String, Object> map = new HashMap<String, Object>();

		//@PathVariable("queryType") int queryType,
		//@PathVariable("caseId") int caseId,
		String queryType =request.getParameter("queryType");
		if(null != queryType &&  !queryType.equals("")) { 
			map.put("queryType", Integer.valueOf(queryType));//查询类型
		}
		
		
		String caseId =request.getParameter("caseId");
		if(null != caseId &&  !caseId.equals("")) { 
			map.put("caseId", Integer.valueOf(caseId));//案件类型id
		} 
		
		//关键字
		String keyWord = request.getParameter("keyWord");
		map.put("keyWord", keyWord);
		//开始日期
		String startDate = request.getParameter("startDate");
		//结束日期
		String endDate = request.getParameter("endDate");
		String state = request.getParameter("state");
		if(null == state || state.equals("")) {
			state = "0";
		}
		map.put("state", state);
		
		 
		Object isofficial = map.get("isofficial");
		if(null != isofficial && !isofficial.equals("") && isofficial.toString().equals("0") ) {
			//如果是官方查询，就需要把咨询者指定咨询师的法律咨询也查询出来；
		}else {
			//否则就只能查询出，不指定某位咨询师的法律咨询
			map.put("privateUserId", "0");
		}
		
		
		String is_delete = request.getParameter("is_delete");
		if(null == is_delete || is_delete.equals("")) {
			is_delete = "0";
		}
		map.put("is_delete", is_delete);
		
		int f = 0;
		if(StringUtil.isBlank(startDate)){
			f++;
		}
		if(StringUtil.isBlank(endDate)){
			f++;
		}
		if(f==1){
			throw new BusinessException("起始日期或结束日期不能为空.", -1);
		}
		if(f==0){
			map.put("hasDate", 1);
		}else{
			map.put("hasDate", 0);
		}
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		
		
		try {
			List<Map<String, Object>> list = adviceService.findAdviceList(map, p);
			out.print(WebUtils.webResponsePage(list));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("咨询问题获取失败.", -1);
		}
		
		
	}
	
	/**
	 * 会员咨询次数查询
	 * @param userId
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping({"/adviceCount/{userId}" })
	public void adviceCount(
			@PathVariable("userId") String userId,
			HttpServletRequest request,
			PrintWriter out)
			throws Exception {
		if(userId==null || userId=="") {
			throw new BusinessException("会员ID不能为空", -1);
		}
		//根据userId获取用户信息
		Map<String, Object> userInfo = userService.getUserInfo(Integer.parseInt(userId));
		if(userInfo==null){
			throw new BusinessException("用户信息不存在.", -1);
		}
		Map<String,String> data = new HashMap<String,String>();
		String responseString = "";
		int vipLevel = (int)userInfo.get("vipLevel");
		int freeNum = null == userInfo.get("freeNum") ? 0 : (int)userInfo.get("freeNum") ;
		//检测是否为会员就否过期 ，如果没有过期会员就可以发免费的
		Object expireTime = userInfo.get("expireTime");
		if(vipLevel==0) {
			responseString = "您是阿里法律网的普通会员，仅剩"+freeNum+"次免费咨询机会。成为黄金会员，全年可享无限次咨询。";
		}else if(vipLevel==0 && freeNum==3) {
			responseString = "您是阿里法律网的普通会员，有三次免费咨询的机会（不含打赏咨询）";
		}else if(vipLevel==0 && freeNum==0) {
			responseString = "您是阿里法律网的普通会员，三次免费咨询机会已用完。成为黄金会员，全年可享无限次咨询。";
		}else if(vipLevel>0) {
			if(VipUtils.checkVipExpires(new Date(), (Date)expireTime) ) {
				responseString = "您是阿里法律网的付费会员，您的会员已到期，请尽快续费！";
				freeNum = 0;
			}else {
				if(vipLevel==1) {
					responseString = "您是阿里法律网的黄金会员，享有无限次免费咨询服务。";
				}else if(vipLevel==2) {
					responseString = "您是阿里法律网的白金会员，享有无限次免费咨询服务。";
				}else if(vipLevel==3) {
					responseString = "您是阿里法律网的钻石会员，享有无限次免费咨询服务。";
				}
				freeNum = 3;
			}
		}
		data.put("freeNum", String.valueOf(freeNum));
		data.put("responseString", responseString);
		out.print(WebUtils.responseData(data));
	}
	
	
	
	/**
	 * 发布咨询
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("sendAdvice")
	public void sendAdvice(
			HttpServletRequest request,
			PrintWriter out) throws Exception{
		Map p = getMap(request);
		Map<String, Object> map  = new HashMap<String, Object>();
		
		String adviceUserId = request.getParameter("adviceUserId"); 
		// 来源 1 正常发布咨询流程 2 在某个咨询师主页点击了免费咨询按钮
		if(adviceUserId == null || adviceUserId.equals("")){ 
			map.put("privateUserId", 0);
		}else { 
			map.put("privateUserId", adviceUserId);
		}
		
		
		
		String userId = request.getParameter("userId"); 
		if(userId == null || userId.equals("")){
			throw new BusinessException("缺失用户编号.", -1);
		}
		
		//根据id获取用户信息
		//Map<String, Object> user = userService.findUserBasicInfoByUserId(Integer.parseInt(userId));
		Map<String, Object> user = userService.getUserInfo(Integer.parseInt(userId));
		
		if(user==null){
			throw new BusinessException("用户信息不存在.", -1);
		}
		
		
		//获取发布咨询相关信息
		String title = request.getParameter("title");
		if(StringUtil.isBlank(title)){
			throw new BusinessException("标题为必填项.", -1);
		}
		
		//咨询 内容
		String adviceContent = request.getParameter("adviceContent");
		
		if(StringUtil.isBlank(adviceContent)){
			throw new BusinessException("内容为必填项.", -1);
		}
		
		String caseType = request.getParameter("caseType");
		
		if(StringUtil.isBlank(caseType)){
			throw new BusinessException("案件类型为必填项.", -1);
		}

		String [] imgs_arr = request.getParameterValues("imgs");
		String imgs = "";
		//把图片地址转换成一个字符窜都保存到imgs 字段中；
       if(null != imgs_arr && imgs_arr.length >0) {
    	   imgs = StringUtils.join(imgs_arr,",");
       }
		map.put("imgs", imgs); 
		
		//赏金    免费为0
		String money = request.getParameter("reward");
		if(StringUtil.isBlank(money)){
			throw new BusinessException("赏金为必填项.", -1);
		}
		
		BigDecimal reward = new BigDecimal(money);		
		
		map.put("title", title);
		map.put("adviceContent", adviceContent);
		map.put("caseType", Integer.parseInt(caseType));
		map.put("reward", reward);
		map.put("userId", Integer.parseInt(userId));
		map.put("userName", user.get("nickName"));
		map.put("userImg", user.get("userImg"));
		map.put("is_delete", 0);
		int state= 0 ;
	 
		//如果是免费咨询，检查此用户免费咨询数量是否足够
		if(reward.compareTo(new BigDecimal(0))<=0){
			//检测是否为会员就否过期 ，如果没有过期会员就可以发免费的
			Object expireTime = user.get("expireTime");
			if(null == expireTime ||  VipUtils.checkVipExpires(new Date(), (Date)expireTime) ) {
			
				int userType = (int)user.get("userType");
				int freeNum = null == user.get("freeNum") ? 0 : (int)user.get("freeNum") ;
						//userService.findFreeNumByUserId(Integer.parseInt(userId));
				if(freeNum==0){
						throw new BusinessException("免费咨询次数为0.", -1);
						//out.print(WebUtils.responseMsg("免费咨询次数为0")); 
				}
				//更新用户免费次数
				userService.updateFree(Integer.parseInt(userId),userType,-1);
			}
			//免费的直接发布，直接发布出动
			state= 0 ;
			
		}else {
			//如果不是免费的，需要赏金支付成功后，才能发布出去，所以现在状态为 1
			state =1; 
		}
		map.put("state", state);
	
		adviceService.sendAdvice(map);  
		//如果金额大于0 就 需要写订单记录
		//保存支付订单信息；
		Map orderM  = null;
		if(reward.compareTo(new BigDecimal(0)) >0) {
			orderM = new HashMap<String,Object>();
			orderM.put("orderName", title); 
			orderM.put("userId", userId); 
			orderM.put("businessType", PayOrder.TYPE_FYZX);//OrderType.businessType_FYZX);
			orderM.put("orderNum", 1);
			orderM.put("remark", "");
			orderM.put("orderType", OrderType.ORDERTYPE_1);
			orderM.put("sourceType", PayOrder.TYPE_FYZX);//订单来源的类型Id 
			orderM.put("orderPrice", reward); //赏金
			orderM.put("connectionId", map.get("adviceId"));
			orderM.put("evaluate", 2); //是否评价   0：不需要评价   1：已评价  2：未评价， 
			orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
			orderM.put("isPay", 2);  //0：不需要支付 1： 已支付   2：未支付  3: 正在支付中  	4：支付失败
			orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货；
			orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
			orderM.put("orderState", 1);//订单的状态； 1 ： 还在进行中， 0 ：已完成
			orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败
			String sign = "{\"privateUserId\":\""+map.get("privateUserId")+"\"}"; //把指定的某位咨询师的Id 保存进来
			orderM.put("sign", sign);
			
			 orderService.saveBusinessOrder(orderM);
		}else {
			//如果没有写订单，返回一个空的支付订单号给前端；
			map.put("businessOrder","");
		} 
		
		if(null != orderM) {
			map.putAll(orderM);
		} 
		
		//如果又是指定的某位咨询师，并且又是免费咨询的，就需要发送站内信息到指定的咨询师
		Object privateUserId = map.get("privateUserId");
		if(!privateUserId.toString().equals("0") && reward.compareTo(new BigDecimal(0))<=0) {
			//判断发站内消息；
		         Map<String, Object> msgMap1 = new HashMap<String, Object>();
		         msgMap1.put("isRead","0"); //0:没读 1：已读
		         msgMap1.put("sendTime",new Date()); //0:没读 1：已读
		         msgMap1.put("isDelete","0"); //0:未删除 1：已删除
		         msgMap1.put("messageState","0"); //
		         msgMap1.put("is_send","1"); //0:未发送1：已发送
		         msgMap1.put("create_time",new Date()); 	
		         msgMap1.put("userId",privateUserId); 
		         msgMap1.put("messageContent","尊敬的用户，您收到了新的法律咨询订单，快去看看吧！点击直接跳转订单内容");
		         msgMap1.put("messageType",PayOrder.TYPE_FYZX);
		         msgMap1.put("businessType","1"); //0 : 回复消息类型     1：系统消息类型
		         msgMap1.put("businessId",map.get("adviceId"));
		         msgMap1.put("messageTitle",map.get("title"));  
		         userService.messageSend(msgMap1);
		}
		out.print(WebUtils.responseData("咨询发布成功.", 1, map)); 
	}
	
	
	
	/**
	 * 获取咨询详情（加载上一咨询问题和下一咨询问题信息）
	 * @param adviceId 咨询id
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping({ "/adviceDetails/{adviceId}" })
	public void adviceDetails(@PathVariable("adviceId") int adviceId,
			HttpServletRequest request,
			PrintWriter out) throws Exception {
		
		//获取查询条件 组装查询条件
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("adviceId", adviceId);
		
		/**
		String caseId = request.getParameter("caseId");
		
		map.put("caseId", caseId==null?caseId:Integer.parseInt(caseId));//案件类型id
		
		String queryType = request.getParameter("queryType");
		map.put("queryType", queryType==null?queryType:Integer.parseInt(queryType));//查询类型
		//关键字
		String keyWord = request.getParameter("keyWord");
		map.put("keyWord", keyWord);
		//开始日期
		String startDate = request.getParameter("startDate");
		//结束日期
		String endDate = request.getParameter("endDate");
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		
		int f = 0;
		if(StringUtil.isBlank(startDate)){
			f++;
		}
		if(StringUtil.isBlank(endDate)){
			f++;
		}
		if(f==1){
			throw new BusinessException("起始日期或结束日期不能为空.", -1);
		}
		if(f==0){  
			map.put("hasDate", 1);
		}else{
			map.put("hasDate", 0);
		}
		**/
		try {
			Map data = adviceService.findAdviceDetails(map);
			out.print(WebUtils.responseData(data));
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof BusinessException){
				throw e;
			}else{
				throw new BusinessException("获取咨询详情信息失败.", -1);
			}
		}
		
	}

	
	/**
	 * 查询法律咨询回复
	 * @param adviceId
	 * @param loginUserId
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping({ "/adviceReplyList/{adviceId}/{loginUserId}/{pageNo}/{pageSize}" })
	public void queryAdviceReplyList(@PathVariable("adviceId") int adviceId,
			@PathVariable("loginUserId") int loginUserId, 
			@PathVariable("pageNo") int pageNo, 
			@PathVariable("pageSize") int pageSize, 
			HttpServletRequest request,
			PrintWriter out)
			throws Exception {
		Paging paging = new  Paging(pageSize, pageNo,true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("adviceId",adviceId);
		map.put("userId", loginUserId);//登录人id
		map.put("pageSize", pageSize);
		map.put("pageNo", pageNo);
		String model = request.getParameter("model");
		
		try { 
			List<Map> list = adviceService.findAdviceReplyList(map,paging);
			if(model == null  ||  model.equals("")) {
				model = "1";
			}
			out.print(WebUtils.webResponsePage(list, Integer.valueOf(model))); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("获取咨询回复列表失败.", -1);
		}
	}

	/**
	 * 法律咨询详情的解答
	 * @param adviceId
	 * @param loginUserId
	 * @param replyContent
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping({ "/sendReply/{adviceId}" })
	public void sendReply(@PathVariable("adviceId") int adviceId,
			@RequestParam("loginUserId") int loginUserId,
			@RequestParam("replyContent") String replyContent, PrintWriter out)
			throws Exception {  
		
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("userId", loginUserId);
		m.put("adviceId", adviceId);
		m.put("replyContent", replyContent);
		m.put("likeNum",0); 
		m.put("rewardNum",0); //打赏数量
		adviceService.saveAdviceReply(m);  
		try {
			out.print(WebUtils.responseData(m));
		} catch (Exception e) {
			// TODO Auto-generated catch block 
			throw new BusinessException("解答失败.", -1);
		}
	}

	
	/**
	 * 法律咨询的 点赞
	 * @param replyId
	 * @param loginUserId
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping({ "/replyLike/{replyId}" })
	public void replyLike(@PathVariable("replyId") int replyId,
			@RequestParam("loginUserId") int loginUserId, PrintWriter out)
			throws Exception {
		ExceptionLogger.writeLog("点赞，replyId=" + replyId);
		Map<String, Object> map = new HashMap();
		
		map.put("replyId",replyId);
		map.put("userId", loginUserId);		
		try {
			int likeNum = 0 ;
			//查询这个点赞人 对这个法律咨询有没有点赞过，
			List<Map> listM = adviceService.findLikeNumList(map);
			//如果查询出的数据长度大于 0 ，说明已经点赞过；
			String msg = "";
			boolean ok = true;
			if(null != listM && listM.size() > 0 ){
				//删除这个人的点赞数据，关改正
				likeNum = adviceService.deleteReplyLike(map);
				msg = "您已取消点赞";
				ok = false;
			}else { 
				//返回点赞后的  点赞数量
				likeNum = adviceService.replyLike(map);
				msg = "点赞成功";
				ok = true;
			}
			Map<String, Object> m = new HashMap();
			m.put("replyId", replyId);
			m.put("likeNum", likeNum);
			m.put("ok", ok);
			out.print(WebUtils.responseData(msg, 1, m));
			//out.print(WebUtils.responseData(m));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("点赞失败", -1);
		}
	}
	
	
	/**
	 * 删除法律咨询，
	 * 不做物理删除 
	 * 
	 */
	@RequestMapping("deleteAdvice/{adviceId}")
	public  void deleteAdvice(
			@PathVariable("adviceId") int adviceId,
			HttpServletRequest request,
			PrintWriter out) {
		try {
			int num = this.adviceService.deleteAdvice(adviceId);
			Map m = getMap(request);
			
			if(num == 0 ) {
				 m.put("ok",false);
				out.print(WebUtils.responseData("删除失败，已经有回复。", 1, m));
			}else {
				m.put("ok",true);
				out.print(WebUtils.responseData("删除成功。", 1,  m));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("删除失败", -1);
		}
		
	}
	
	/**
	 * 最近回复过我的人；
	 * @param userId
	 * @param out
	 */
	@RequestMapping("myReplyRecently/{userId}")
	public void myReplyRecently(
			@PathVariable("userId") int userId,
			PrintWriter out) {
		 try {
		 Map m = 	this.adviceService.myReplyRecently(userId);
		 out.print(WebUtils.responseData("查询成功.",1,m));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 out.print(WebUtils.responseError("查询失败",-1));
		}
	}
	
	/**
	 * 我的咨询问题列表
	 * @throws Exception
	 */
	
	@RequestMapping("/myAdviceList/{userId}/{pageNo}/{pageSize}")
	public void myAdviceList(
			@PathVariable("userId") int userId,
			@PathVariable("pageNo") int pageNo, 
			@PathVariable("pageSize") int pageSize,  
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Map  m= getMap(request);
		Paging paging = new Paging(pageSize, pageNo,true);
		try {
			List listM = this.adviceService.myConsultList(m, paging);
			out.print(WebUtils.webResponsePage(listM));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.println(WebUtils.responseError("查询失败.",-1));
		}
		
		
		/**
		List<Map> list = adviceService.findAdviceByUserId(userId);
		out.print(WebUtils.responseData(list));
		**/
	}
	
	
	/**
	 * 我解答的咨询
	 * @param userId
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("/myAnswerAdvice/{userId}/{pageNo}/{pageSize}")
	public void myAnswerAdvice(
			@PathVariable("userId") int userId,
			@PathVariable("pageNo") int pageNo, 
			@PathVariable("pageSize") int pageSize,  
			HttpServletRequest request,
			PrintWriter out) throws Exception{
		Paging paging = new Paging(pageSize, pageNo,true);
		Map map =  new HashMap<String, String>();
		map.put("userId", userId);
		List<Map> list = adviceService.myAnswerAdviceList(map,paging);
		out.print(WebUtils.webResponsePage(list));
	}
	
	
}
