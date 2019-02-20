package com.legal.app.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.common.dbutil.Paging;
import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;
import com.common.utils.SystemConfigUtil;
import com.common.web.HttpUtils;
import com.common.web.WebUtils;
import com.gexin.fastjson.JSON;
import com.legal.app.service.OrderService;
import com.legal.app.service.UserService;
import com.legal.app.service.WalletRecordService;
import com.legal.app.utils.CreateOrderNumber;
import com.legal.app.utils.OrderType;
import com.legal.app.utils.PayOrder;
import com.legal.app.utils.SendArticleType;
import com.legal.app.utils.Tools;
import com.legal.app.utils.VipUtils;

import net.sf.json.JSONObject;
 


/**
 * 所有订单相关
 * @author admin
 *
 */
@Controller
@RequestMapping("order")
public class OrderController extends SuperController{
	@Autowired
	private OrderService orderser;

	@Autowired
	private UserService userSer;
	
	@Autowired
	private WalletRecordService walletSer;
	
	
	@ResponseBody
	@RequestMapping("/getRequestPayOrderStatus")
	public String getRequestPayOrderStatus(
			HttpServletRequest request
			) throws Exception{
		String orderNum=request.getParameter("orderNum");
		
		/*
		 * HttpSession hs=request.getSession();
		 * //System.out.println(hs.getAttribute("requestPayOrderStatus").toString());
		 * String requestPayOrderStatus=
		 * request.getSession().getAttribute("fuckj").toString() ;
		 */
		 	Map map =  new HashMap();
		 	map.put("businessOrder", orderNum);
		   // Object businessOrder = map.get("businessOrder");
		    if(null == orderNum) {
		    	throw new BusinessException("订单businessOrder不能为空.",-1);
		    }
		    Map orderMap = (Map)orderser.findOrderDetails(orderNum);
		    //Map order = (Map)orderser.findOrderDetails(map);
			String ordernum=orderMap.get("isPay").toString();
		Map<String, String> mapp = new HashMap<String, String>();
		mapp.put("requestPayOrderStatus",ordernum);
		
		return JSON.toJSONString(mapp);
	}
	/**
	 * 保存订单，不对外调用 ，只在测试时使用
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("saveBusinessOrder")
	public void saveOrder(
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Map<String, Object> m = super.getMap(request);
		try {
			m.put("businessOrder", CreateOrderNumber.create());
			m.put("businessType", PayOrder.TYPE_FYZX);
			m.put("evaluate", 0);
			m.put("sender", 0);
			m.put("reception", 0);
			m.put("favoreeUserId", 2);
			m.put("payment", "微信");
			
			orderser.saveBusinessOrder(m);
			out.print(WebUtils.responseData("保存成功.",1, m));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("保存失败.", -1);
		} 
	}
	
	
	/**
	 * 修改订单，不对外调用 ，只在测试时使用
	 * @param request
	 * @param out
	 */
	@RequestMapping("update")
	public void update(
			HttpServletRequest request,
			PrintWriter out
			) {
		Map map = getMap(request);
		String businessOrder  = (String)map.get("businessOrder");
		if(null == businessOrder || businessOrder.equals("")) {
			throw new BusinessException("订单号不能为空.",-1);
		}
		try {
			this.orderser.update(map);
			out.print(WebUtils.responseData("修改成功.",1, map));
		} catch (Exception e) { 
			e.printStackTrace();
			throw new BusinessException("修改失败.",-1);
		}
	}
	
	
	/**
	 * H5 订单支付详情中，发起订单支付。
	 * @param request
	 * @param out
	 */
	@RequestMapping("gotoPay_H5")
	public ModelAndView  gotoPay_H5(
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		
		
		Map map = getMap(request);
		String businessOrder  = (String)map.get("businessOrder");
		if(null == businessOrder || businessOrder.equals("")) {
			throw new BusinessException("订单号不能为空.",-1);
		}  
		//金额
		Object orderPrice = map.get("orderPrice");
		if(null == orderPrice || orderPrice.equals("")) {
			throw new BusinessException("订单金额不能为空.",-1);
		}
		//金额必须是数字；
		BigDecimal orderPrice_tmp = new BigDecimal("0");
		try {
			orderPrice_tmp = new BigDecimal(orderPrice.toString()); 
		} catch (Exception e1) { 
			e1.printStackTrace();
			throw new BusinessException("订单金额必须为数字.",-1);
		} 
		
		if(orderPrice_tmp.compareTo(new BigDecimal("0") )!= 1) {
			throw new BusinessException("订单金额不能为 0，或者小于 0 .",-1);
		}
		
		//姓名
		Object userName = map.get("userName");
		if(null == userName || userName.equals("")) {
			throw new BusinessException("真实姓名不能为空.",-1);
		}
		
		//支付类型；【微信支付 ：银行卡支付】
		Object payment = map.get("payment");
		if(null == payment || payment.equals("") ) {
			throw new BusinessException("支付类型来能为空.",-1);
		}
		if(!(payment.equals(PayOrder.PAYMENT_TYPE_WXZF) || payment.equals(PayOrder.PAYMENT_TYPE_YHKZF))) {
			throw new BusinessException("支付类型不匹配【"+PayOrder.PAYMENT_TYPE_WXZF+":"+PayOrder.PAYMENT_TYPE_YHKZF+"】.",-1);
		}
		
		
		//订单是否存在
		Map order = (Map)this.orderser.findOrderDetails(businessOrder);
		if(null == order) {
			throw new BusinessException("该订单不存在.",-1);
		}
		//订单状态可能在支付中，或者是其它的情况 ，不能继续支付；
		Integer isPay = (Integer)order.get("isPay");
		if(null != isPay && isPay.equals("2")) {
			throw new BusinessException("该订单不能支付.",-1);
		}
		
		//判断如果订单原来就有金额，现在页面上传过来的金额如果和数据库中的金额不相同，就不能提交；
		BigDecimal orderPrice_base = (BigDecimal)order.get("totalPrice");
		//如果数据库的金额大于 0 ，
		if(orderPrice_base.compareTo(new BigDecimal("0")) == 1) { 
			if(orderPrice_base.compareTo(orderPrice_tmp)  != 0  ) {
				throw new BusinessException("订单金额不正确.",-1);
			}
		}
		
		
		
		
		String  path = request.getContextPath();
		String url="http://" + request.getServerName() + ":"     + request.getServerPort();
		int price = orderPrice_base.multiply(new BigDecimal("100")).intValue(); 
		url=url +path+"/api/pay/wx/unifiedOrder/"+URLEncoder.encode(businessOrder)+"/"+URLEncoder.encode(order.get("businessType").toString())+"/"+price;
		ModelAndView view = new ModelAndView(new RedirectView(url));
		return view;
	}
	

	
	/**
	 * 订单支付详情中，发起订单支付。
	 * 综合了 h5 支付与 打码支付；
	 * @param request
	 * @param out
	 */
	@RequestMapping("gotoPay")
	public void  gotoPay(
			HttpServletRequest request,
			PrintWriter out
			) throws Exception {
		
		
		Map map = getMap(request);
		String businessOrder  = (String)map.get("businessOrder");
		if(null == businessOrder || businessOrder.equals("")) {
			throw new BusinessException("订单号不能为空.",-1);
		}  
		//金额
		Object orderPrice = map.get("orderPrice");
		if(null == orderPrice || orderPrice.equals("")) {
			throw new BusinessException("订单金额不能为空.",-1);
		}
		//金额必须是数字；
		BigDecimal orderPrice_tmp = new BigDecimal("0");
		try {
			orderPrice_tmp = new BigDecimal(orderPrice.toString()); 
		} catch (Exception e1) { 
			e1.printStackTrace();
			throw new BusinessException("订单金额必须为数字.",-1);
		} 
		
		if(orderPrice_tmp.compareTo(new BigDecimal("0") )!= 1) {
			throw new BusinessException("订单金额不能为 0，或者小于 0 .",-1);
		}
		
		//姓名
		Object userName = map.get("userName");
		if(null == userName || userName.equals("")) {
			throw new BusinessException("真实姓名不能为空.",-1);
		}
		
		//支付类型；【微信支付 ：银行卡支付】
		Object payment = map.get("payment");
		if(null == payment || payment.equals("") ) {
			throw new BusinessException("支付类型不能为空.",-1);
		}
		if(!(payment.equals(PayOrder.PAYMENT_TYPE_WXZF) || payment.equals(PayOrder.PAYMENT_TYPE_YHKZF))) {
			throw new BusinessException("支付类型不匹配【"+PayOrder.PAYMENT_TYPE_WXZF+":"+PayOrder.PAYMENT_TYPE_YHKZF+"】.",-1);
		}
		
		
		
		String wxPayWay = (String)map.get("wxPayWay");
		if(null == wxPayWay || wxPayWay.equals("")) {
			throw new BusinessException("支付方式不能为空.",-1);
		}	
		if(!(wxPayWay.equals("H5") || wxPayWay.equals("QCODE"))) {
			throw new BusinessException("支付方式不匹配，只能是扫码或者是H5.",-1);
		}
		
		//订单是否存在
		Map order = (Map)this.orderser.findOrderDetails(businessOrder);
		if(null == order) {
			throw new BusinessException("该订单不存在.",-1);
		}
		//订单状态可能在支付中，或者是其它的情况 ，不能继续支付；
		Integer isPay = (Integer)order.get("isPay");
		if(null != isPay && isPay.equals("2")) {
			throw new BusinessException("该订单不能支付.",-1);
		}
		
		BigDecimal orderPrice_base = null;
		//获取订单的类型；
		Object businessType =  order.get("businessType");
		//可以让某些订单的金额可以更改；
		if(
				//打赏
				!businessType.equals(PayOrder.TYPE_DS) &&
				//文书制作
				!businessType.equals(PayOrder.TYPE_FYWSZZ) &&
				//案件委托
				!businessType.equals(PayOrder.TYPE_AJWT) &&
				//协议律师费
				!businessType.equals(PayOrder.TYPE_XYYSF) 
		) {
			//判断如果订单原来就有金额，现在页面上传过来的金额如果和数据库中的金额不相同，就不能提交；
			orderPrice_base = (BigDecimal)order.get("totalPrice"); 
			//如果数据库的金额大于 0 ，
			if(orderPrice_base.compareTo(new BigDecimal("0")) == 1) { 
				if(orderPrice_base.compareTo(orderPrice_tmp)  != 0  ) {
					throw new BusinessException("订单金额不正确.",-1);
				}
			}
		}else {
			orderPrice_base = orderPrice_tmp;
		}
		if(wxPayWay.equals("H5")) { 
			//H5回返的格式；
			//业务状态为正常；
			Map saveMap = new HashMap<String,Object>();
			saveMap.put("businessOrder", businessOrder);
			saveMap.put("userName", userName);
			saveMap.put("payment", payment);
			saveMap.put("orderPrice", orderPrice_tmp);
			saveMap.put("totalPrice", orderPrice_tmp);
			this.orderser.update(saveMap); 
			String  path = request.getContextPath();
			//String url="http://www.alifalv.cn";
			String url="http://" + request.getServerName() + ":"     + request.getServerPort();   
			int price = orderPrice_base.multiply(new BigDecimal("100")).intValue(); 
			url=url +path+"/api/pay/wx/unifiedOrder/"+URLEncoder.encode(businessOrder)+"/"+URLEncoder.encode(order.get("businessType").toString())+"/"+price;
			out.print(WebUtils.responseData("下单成功",1,url));
			
		
		}else{  
			//支付请求地址 扫码支付的方式；
			String  path = request.getContextPath(); 
			String url="http://" + request.getServerName() + ":"     + request.getServerPort();   
			int price = orderPrice_base.multiply(new BigDecimal("100")).intValue();
			//生成订单
			url =url + path+"/api/pay/wx/unifiedOrder_qcode/"+URLEncoder.encode(businessOrder)+"/"+URLEncoder.encode(order.get("businessType").toString())+"/"+price;
			//发送请求
			HttpResponse sp=HttpUtils.post(url, new HashMap().put("dd",123),"");		
			//响应
			String responseStr=EntityUtils.toString(sp.getEntity(),"UTF-8");		
			ExceptionLogger.writeLog("------调用微信查询订单的响应数据：-------:"+responseStr);
			JSONObject json = JSONObject.fromObject(responseStr);
			Map rMap = (Map)json.get("data");
			String return_code  = (String)rMap.get("return_code");
			if(return_code.equals("SUCCESS")) {
				//返回正常的格式；
				//判断业务状态值是否为成功
				if(rMap.get("result_code").equals("SUCCESS")) {
					//业务状态为正常；
					Map saveMap = new HashMap<String,Object>();
					saveMap.put("businessOrder", businessOrder);
					saveMap.put("userName", userName);
					saveMap.put("payment", payment);
					saveMap.put("orderPrice", orderPrice_tmp);
					saveMap.put("totalPrice", orderPrice_tmp);
					this.orderser.update(saveMap);
					out.print(WebUtils.responseData("下单成功",1,rMap.get("code_url")));
				}else {
					//业务状态返回为不正常
					throw new BusinessException((String)rMap.get("err_code_des"),-1);
				}
				
			}else {
				//返回错误的结果；
				throw new BusinessException((String)rMap.get("return_msg"),-1);
			} 
		}
	}
	
 
	
	/**
	 * 订单列表；
	 * @param request
	 * @param out
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @throws Exception
	 */
	@RequestMapping("listBusinessOrder/{model}/{pageNo}/{pageSize}")
	public void listBusinessOrder(
			HttpServletRequest request,
			PrintWriter out, 
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize
			) throws Exception{
		Paging paging = new Paging(pageSize, pageNo, true);
		Map map = getMap(request);
		Object startTime_tmp = map.get("startTime");
		String startTime= null;
		if(null != startTime_tmp && !startTime_tmp.equals("") ) {
			startTime =(String)startTime_tmp+" 00:00:00";
		}
		
		Object endTime_tmp = map.get("endTime");
		String endTime= null;
		if(null != endTime_tmp && !endTime_tmp.equals("") ) {
			endTime =(String)endTime_tmp+" 00:00:00";
		}
	 
		
		try {
			List   list = orderser.listBusinessOrder(map, paging);
			out.print(WebUtils.webResponsePage(list,model));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("查询失败.",-1);
		}
	}
	
	
	@RequestMapping("findOrderDetails")
	public void findOrderDetails(
			HttpServletRequest request,
			PrintWriter out) {
		
		request.getSession().setAttribute("fuckj", "Y");
		/*
		 * HttpSession hs = request.getSession();
		 * hs.removeAttribute("requestPayOrderStatus");
		 */

		    Map map =  getMap(request);
		    Object businessOrder = map.get("businessOrder");
		    if(null == businessOrder || businessOrder.equals("")) {
		    	throw new BusinessException("订单businessOrder不能为空.",-1);
		    }
		   try {
			List list =  orderser.findOrderDetails(map);
			Object obj = null;
			if(null != list && list.size() > 0 ) {
				obj = list.get(0);
			}
			out.print(WebUtils.responseData("查询成功.", 1,obj));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ExceptionLogger.writeLog(e,e.getClass());
			throw new BusinessException("查询失败.",-1);
		}
	}
	


	
	
	/**
	 * 提现；
	 */
	@RequestMapping("withdrawDeposit")
	public void withdrawDeposit(
			HttpServletRequest request,
			PrintWriter out
			) {
		
		Map map = getMap(request);
		
		Object userId = map.get("userId");
		if(null == userId || userId.equals("")) {
			throw new BusinessException("用户编号不能为空.",-1);
		}
		
		Object money = map.get("money");
		if(null == money || money.equals("")) {
			throw new BusinessException("提现金额不能为空.",-1);
		}
		//判断金额是否为数字；
		BigDecimal moneyB = null;
		try {
			moneyB = new BigDecimal((String)money);
		} catch (Exception e1) { 
			e1.printStackTrace();
			throw new BusinessException("提现金额为非数字.",-1);
		}
		
		Object payment = map.get("payment");
		if(null == payment || payment.equals("")) {
			throw new BusinessException("提现类型不能为空.",-1);
		}else if(!payment.equals(PayOrder.PAYMENT_TYPE_WXZF) && !payment.equals(PayOrder.PAYMENT_TYPE_YHKZF)) {
			//如果不是微信支付和银行卡，就提示
			throw new BusinessException("提现类型为【"+PayOrder.PAYMENT_TYPE_WXZF+"/"+PayOrder.PAYMENT_TYPE_YHKZF+"】.",-1);
		} 
				

		try {
			Map counMap = userSer.findCounselorByUserId(Integer.valueOf((String)userId));
			if(null == counMap) {
				throw new BusinessException("提现用户不存在.",-1);
			}
			//判断金额是否大于等于余额；
		  Object userBalance_tmp = 	counMap.get("userBalance");
		  BigDecimal userBalance= new BigDecimal("0");
		  if(null != userBalance_tmp) {
			  userBalance = new BigDecimal(userBalance_tmp+"");
		  }
		  if(userBalance.compareTo(moneyB) == -1) {
				throw new BusinessException("提现金额不能大于余额.",-1);
		  }
		  
		  String cardNumber = "";
		  String cardName = "";
		  if(payment.equals(PayOrder.PAYMENT_TYPE_WXZF)) {
			  //如果微信支付，判断是否绑定微信号；
			Object openid =   counMap.get("openid");
			if(openid == null || openid.equals("")) {
				throw new BusinessException("还没有绑定微信帐号.",-1);
			}
			cardNumber = (String)openid;
			cardName = "微信";
		  }else if(payment.equals(PayOrder.PAYMENT_TYPE_YHKZF)) {
			  Object bankAccount = counMap.get("bankAccount");
			  Object bankName = counMap.get("bankName");
			  
			  if(bankAccount== null || bankAccount.equals("")) {
				  throw new BusinessException("还没有绑定提现银行卡号.",-1);
			  }
			  cardNumber = (String)bankAccount;
			  cardName = (String)bankName;
		  }
		 
			Map orderM = new HashMap<String,Object>();
			orderM.put("orderName", OrderType.businessType_TX); 
			orderM.put("userId",  userId); 
			orderM.put("businessType", OrderType.businessType_TX);
			orderM.put("remark", "");
			orderM.put("orderNum", 1);
			orderM.put("orderType", OrderType.ORDERTYPE_2);
			orderM.put("orderPrice", moneyB); //提现金额; 
			//orderM.put("connectionId", 0);
			//orderM.put("checkTime", new Date()); 
			//orderM.put("endTime", new Date());  
			orderM.put("evaluate", 0); //是否评价   0：不需要评价   1：已评价  2：未评价， 
			orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
			orderM.put("isPay", 6);  //是否支付  ，0：不需要支付      1： 已支付   2：未支付  3: 正在支付中  	4：支付失败  5：已退款  6 :待审核
			orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货；
			orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
			orderM.put("orderState", 1);//订单的状态； 1 ： 还在进行中， 0 ：已完成 
			orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败 
			orderM.put("payment",  payment); //支付类型： 微信支付还是银联支付；
			orderM.put("userName", counMap.get("realName"));//真实姓名；
			orderM.put("cardNumber", cardNumber);//提现的卡号或者是微信openid
			orderM.put("cardName", cardName);//银行名称
			orderM.put("mobile", counMap.get("mobile"));//手机号
			orderser.saveBusinessOrder(orderM);
			
			//减去用户的余额；
			Double balance = userBalance.subtract(moneyB).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
			userSer.updateCounselorBalance(Integer.valueOf((String)userId), balance); 
			//同时写一条金额的变动流水；
			//插入一条咨询师的交易流水记录； 
    		Map walletM = new HashMap<String,Object>();
    		walletM.put("userId", userId);
    		walletM.put("businessOrder", orderM.get("businessOrder"));
    		walletM.put("balance", new BigDecimal(0).subtract(moneyB));
    		walletM.put("totalFee", balance);
    		walletSer.saveWalletRecord(walletM); 
			
			out.print(WebUtils.responseData("申请已提交，等待审核.",1,orderM)); 
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException(e.getMessage(),-1);
		} 
	}	

	/**
	 * 不对外
	 * @param businessOrder
	 * @param outTradeNo
	 * @param pageSize
	 * @throws Exception
	 */
    @RequestMapping("orderPaySuccess")
	public void orderPaySuccess(
			HttpServletRequest request,
			PrintWriter out 
			) throws Exception{
    	Map map = getMap(request);
    	String outTradeNo= (String)map.get("outTradeNo");
    	String businessOrder = (String)map.get("businessOrder");
    	this.orderser.orderPaySuccess(businessOrder, outTradeNo, 0,null);
	}
	
	
	/**
	 * 提现审核
	 */
	@RequestMapping("auditWithdrawDeposit")
	public void  auditWithdrawDeposit(
			HttpServletRequest request,
			PrintWriter out
			) {
		Map m = getMap(request); 
		//判断订单编号是否存在；
		Object businessOrder = m.get("businessOrder");
		if(null == businessOrder || businessOrder.equals("")) {
			throw new BusinessException("订单号不能为空.",-1);
		}
		
		
		//判断审核状态是否为 1 和 4  ，如果不是这二个值 ，就不能进行审核；
		Object state_tmp = m.get("state");
		int state = 0;
		if(null ==  state_tmp || state_tmp.equals("")) {
			throw new BusinessException("订单号审核状态不正确.",-1);
		}else {
			try {
				state = Integer.valueOf(state_tmp.toString());
				if(!(state == 1 || state == 4)) {
					//1:审核成功  4：审核失败；
					throw new BusinessException("订单号审核状态不正确.",-1);
				}  
			} catch (NumberFormatException e) { 
				e.printStackTrace();
				throw new BusinessException("订单号审核状态不正确.",-1);
			}
		}
		
		//查询这个订单是否存在；
		 try {
			Map orderMap = (Map)orderser.findOrderDetails(businessOrder.toString());
			if(null == orderMap ) {
				throw new BusinessException("订单不存在.",-1);
			}
			//判断这个订单是否已经审核；
			int isPay = (int)orderMap.get("isPay");
			if(isPay != 6) {
				throw new BusinessException("订单不需要审核.",-1);
			}
			
			int orderState = 1;
			//判断审核状态：
			if(state == 1) {
				//审核成功；
				//1.改订单状态  字段 isPay : 1 【支付成功】,  orderState = 0 【状态为完成】;
				isPay = 1;
				orderState = 0;
			}else if(state ==4) {
				//审核失败；
				//1.修改订单状态；字段 isPay : 4 【支付成功】,  orderState = 0 【状态为完成】;
				orderState = 0;
				isPay = 4; 
				
				Object userId= orderMap.get("userId"); 
				Object moneyB = orderMap.get("orderPrice");
				Object orderNum = orderMap.get("orderNum");
				
				
			  Map counMap = userSer.findCounselorByUserId(Integer.valueOf(userId.toString())); 
			   Object userBalance_tmp = 	counMap.get("userBalance");
			   BigDecimal userBalance= new BigDecimal("0");
			   if(null != userBalance_tmp) {
				   userBalance = new BigDecimal(userBalance_tmp+"").add(new BigDecimal(moneyB.toString()));
			   }
				
				//2.插入一个订单，写上提现失败返款
				Map orderM = new HashMap<String,Object>(); 
				orderM.put("orderName", "提现失败退款"); 
				orderM.put("userId",  userId); 
				orderM.put("businessType", OrderType.businessType_TX);
				orderM.put("remark", "");
				orderM.put("orderNum", orderNum);
				orderM.put("orderType", OrderType.ORDERTYPE_2);
				orderM.put("orderPrice", moneyB); //提现金额; 
				//orderM.put("connectionId", 0);
				//orderM.put("checkTime", new Date()); 
				//orderM.put("endTime", new Date());  
				orderM.put("evaluate", 0); //是否评价   0：不需要评价   1：已评价  2：未评价， 
				orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
				orderM.put("isPay", 1);  //是否支付  ，0：不需要支付      1： 已支付   2：未支付  3: 正在支付中  	4：支付失败  5：已退款  6 :待审核
				orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货；
				orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
				orderM.put("orderState", 0);//订单的状态； 1 ： 还在进行中， 0 ：已完成 
				orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败 
				//orderM.put("payment",  payment); //支付类型： 微信支付还是银联支付；
				//orderM.put("userName", counMap.get("realName"));//真实姓名；
				//orderM.put("cardNumber", cardNumber);//提现的卡号或者是微信openid
				orderser.saveBusinessOrder(orderM); 
				
				
				//3.插入一个流水记录，把原来扣款的金额再补上流水；
				Map walletM = new HashMap<String,Object>();
	    		walletM.put("userId", userId);
	    		walletM.put("businessOrder", orderM.get("businessOrder"));
	    		walletM.put("balance",moneyB);
	    		walletM.put("totalFee", userBalance);
	    		walletSer.saveWalletRecord(walletM); 
			 
				//4.修改用户的余额；
	    		userSer.updateCounselorBalance(Integer.valueOf(userId.toString()), userBalance.doubleValue()); 
				
			}else {
				throw new BusinessException("提现审核失败，状态不正确.",-1);
			} 
			Map updataMap = new HashMap<String,Object>();
			updataMap.put("businessOrder", businessOrder);
			updataMap.put("isPay", isPay);
			updataMap.put("orderState", orderState);
			updataMap.put("checkTime", new Date());
			updataMap.put("endTime", new Date());
			orderser.update(updataMap);
			out.print(WebUtils.responseData("审核成功.",1,updataMap ));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("提现审核失败.",-1);
		}
		
	} 
	
	
	/**
	 * 发起打赏 
	 * 过程流程；
	 * 1，此方法的过程，只是用户发起打赏，
	 *      提交一个充值的订单，去支付；走支付流程；
	 * 2. 打赏的成功与失败，等支付成功后，在支付的回调接口中，根据订单的类型完成最后的打赏过程；
	 * @param request
	 * @param out
	 */
	@RequestMapping("gotoReward")
	public void gotoReward(
			HttpServletRequest request,
			PrintWriter out
			) {
		 Map  m = getMap(request);
		 Object userId = m.get("userId");
		 if(null == userId || userId.equals("")) {
			 throw new BusinessException("用户编号不能为空.",-1);
		 }
		 
		 Object favoreeUserId = m.get("favoreeUserId");
		 if(null == favoreeUserId || favoreeUserId.equals("")) {
			 throw new BusinessException("被打赏用户编号不能为空.",-1);
		 }
		 
		 Object connectionId = m.get("connectionId");
		 if(null == connectionId || connectionId.equals("")) {
			 throw new BusinessException("业务编号不能为空.",-1);
		 } 
		 
		 Object businessType = m.get("businessType");
		 if(businessType == null || businessType.equals("")) {
			 throw new BusinessException("业务类型不能为空.",-1);
		 }
		 
		 Object title = m.get("title");
		 if(title == null || title.equals("")) {
			 throw new BusinessException("文章标题不能为空.",-1);
		 }
		 
		 Object currentId  = m.get("currentId");
		 if(currentId == null || currentId.equals("")) {
			 throw new BusinessException("对象编号不能为空.",-1);
		 }
		 
		    //产生一个支付订单 
	    	//如果没有支付订单记录，产生一个支付订单，返回页面去支付；
	    	Map orderM = new HashMap<String,Object>();
			//orderM.put("orderName",PayOrder.ORDER_NAME_DSCZ ); 
			orderM.put("orderName",title); 
			orderM.put("userId", userId); 
			orderM.put("businessType", PayOrder.TYPE_DS);  //标注当前订单是什么类型
			orderM.put("sourceType", businessType);//订单来源的类型Id 
			orderM.put("remark", "");
			orderM.put("orderNum", 1);
			orderM.put("orderType", OrderType.ORDERTYPE_1);
			orderM.put("orderPrice", 0); //赏金
			orderM.put("connectionId",connectionId);//业务的ID 
			//orderM.put("favoreeUserId", null); //制作人的Id 
			//orderM.put("checkTime", new Date()); 
			//orderM.put("endTime",); //结束时间 
			orderM.put("isPay", 2);  //是否支付  ，0：不需要支付      1： 已支付   2：未支付  3: 正在支付中  	4：支付失败  5：已退款
			orderM.put("evaluate", 0); //是否评价   0：不需要评价    1：已评价  2：未评价， 
			orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
			orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货 ；
			orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
			orderM.put("orderState", 1);//订单的状态； 1 ： 还在进行中， 0 ：已完成 
			orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败 
			//标记着，充值成功后需要执行时，需要的值；
			String sign = "{\"favoreeUserId\":\""+favoreeUserId+"\"," 
					+ "\"currentId\":\""+currentId+"\"}";
			orderM.put("sign", sign);
			try {
				orderser.saveBusinessOrder(orderM);
				out.print(WebUtils.responseData("生成订单，进行支付。", 1, orderM));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BusinessException("操作失败.",-1);
			}
	}
   
	/**
	 * 协议律师费订金
	 * @param request
	 * @param out
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@RequestMapping("bargainMoney")
	public void bargainMoney(
			HttpServletRequest request,
			PrintWriter out
			) throws NumberFormatException, Exception {
		 Map  m = getMap(request);
		 Object userId = m.get("userId");
		 if(null == userId || userId.equals("")) {
			 throw new BusinessException("用户编号不能为空.",-1);
		 }
		 
		 Object favoreeUserId = m.get("favoreeUserId");
		 if(null == favoreeUserId || favoreeUserId.equals("")) {
			 throw new BusinessException("律师订金编号不能为空.",-1);
		 }
		 
		/**  
		 
		 Object businessType = m.get("businessType");
		 if(businessType == null || businessType.equals("")) {
			 throw new BusinessException("业务类型不能为空.",-1);
		 }
 **/
		 
 

		Map userInfo = userSer.getUserInfo(Integer.valueOf((String)userId));
		Map userdetailInfo = null;
		if(userInfo.get("userType").equals(1)) {
			userdetailInfo = userSer.getConsultantInfo(m);
		}else if(userInfo.get("userType").equals(2)) {
			userdetailInfo = userSer.getCounselorInfo(m);
		}else if(userInfo.get("userType").equals(3)) {
			userdetailInfo = userSer.getCompanyInfo(Integer.valueOf((String)userId));
		}
		    //产生一个支付订单 
	    	//如果没有支付订单记录，产生一个支付订单，返回页面去支付；
	    	Map orderM = new HashMap<String,Object>();
			orderM.put("orderName",PayOrder.TYPE_XYYSF ); 
			orderM.put("userId", userId); 
			orderM.put("businessType", PayOrder.TYPE_XYYSF);  //标注当前订单是什么类型
			orderM.put("sourceType", PayOrder.TYPE_XYYSF);//订单来源的类型Id 
			orderM.put("remark", "");
			orderM.put("userName", userdetailInfo.get("realName"));
			orderM.put("mobile", userInfo.get("mobile"));
			orderM.put("orderNum", 1);
			orderM.put("orderType", OrderType.ORDERTYPE_1);
			orderM.put("orderPrice", 0); //赏金
			orderM.put("connectionId",null);//业务的ID 
			orderM.put("favoreeUserId", favoreeUserId); //制作人的Id 
			//orderM.put("favoreeUserId", null); //制作人的Id 
			orderM.put("checkTime", new Date()); 
			//orderM.put("endTime",); //结束时间 
			orderM.put("isPay", 2);  //是否支付  ，0：不需要支付      1： 已支付   2：未支付  3: 正在支付中  	4：支付失败  5：已退款
			orderM.put("evaluate", 0); //是否评价   0：不需要评价    1：已评价  2：未评价， 
			orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
			orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货 ；
			orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
			orderM.put("orderState", 1);//订单的状态； 1 ： 还在进行中， 0 ：已完成 
			orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败 
			//标记着，充值成功后需要执行时，需要的值；
			String sign = "{\"favoreeUserId\":\""+favoreeUserId+"\"}";
			orderM.put("sign", sign);
			try {
				orderser.saveBusinessOrder(orderM);
				out.print(WebUtils.responseData("生成订单，进行支付。", 1, orderM));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BusinessException("操作失败.",-1);
			}
	}

	
	/**
	 * 订单的申诉的审核
	 * @param request
	 * @param out
	 */
	@RequestMapping("complainAudit")
	public void  complainAudit(
			HttpServletRequest request,
			PrintWriter out
			) {
		Map<String,Object> map = new HashMap<String,Object>();
		String businessOrder  =request.getParameter("businessOrder");
		if(null == businessOrder || businessOrder.equals("")) {
			throw new BusinessException("订单号不能为空.",-1);
		}
		map.put("businessOrder", businessOrder);
		
		
		String  isComplain  =  request.getParameter("isComplain");
		if(null == isComplain || !( isComplain.equals("2") || isComplain.equals("3"))){
			throw new BusinessException("审核参数不匹配.",-1);
		}
		map.put("isComplain", isComplain);
		
		if(isComplain.equals("2")) {
			//如果申诉失败，就需要返还金额；（暂时还没有）
			//订单支付状态改为已退款；
			map.put("isPay", 5);
		}
		
		try {
			//判断这个订单是否在申诉中；
			Object orderMap = this.orderser.findOrderDetails(businessOrder);
			if(null == orderMap) {
				throw new BusinessException("操作失败，该订单不存在.",-1);
			}
		   //判断这个订单是否在申诉中；
			Map order = (Map)orderMap;
			Integer isComplain_base = (Integer)order.get("isComplain");
			if(!isComplain_base.equals(1)) {
				throw new BusinessException("订单不能审核或者已经审核过.",-1);
			}
			
			map.put("orderState", 0);
			String noPassReason  =request.getParameter("noPassReason");
			map.put("noPassReason", noPassReason);
			this.orderser.update(map);
			out.print(WebUtils.responseData("操作成功.",1, map));
		} catch (Exception e) { 
			e.printStackTrace();
			throw new BusinessException("操作失败.",-1);
		}
	} 
	
	
	/**
	 * 订单的申诉
	 * @param request
	 * @param out
	 */
	@RequestMapping("complain")
	public void complain(
			HttpServletRequest request,
			PrintWriter out
			) {
		Map<String,Object> map = new HashMap<String,Object>();
		String businessOrder  =  request.getParameter("businessOrder");
		if(null == businessOrder || businessOrder.equals("")) {
			throw new BusinessException("订单号不能为空.",-1);
		}
		map.put("businessOrder", businessOrder);
		String complainType  = request.getParameter("complainType");
		if(null == complainType || complainType.equals("")) {
			throw new BusinessException("申诉类型不能为空.",-1);
		}
		map.put("complainType", complainType);
	   map.put("isComplain", 1);
	   map.put("complainContent", request.getParameter("complainContent")); 
	   
		try {
			this.orderser.update(map);
			out.print(WebUtils.responseData("订单已经申诉.",1, map));
		} catch (Exception e) { 
			e.printStackTrace();
			throw new BusinessException("提交申诉失败.",-1);
		}
		
	}
	
	
	/**
	 * 文书制作 
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * 
	 */
	@RequestMapping("makeWrit")
	public void makeWrit(
			HttpServletRequest request,
			PrintWriter out
			) throws NumberFormatException, Exception { 
		Map m = getMap(request);
		Object realName = m.get("realName");
		if(null == realName || realName.equals("")) {
			throw new BusinessException("发起人的真实姓名不能为空.",-1);
		}
		Object userId = m.get("userId");
		if(null == userId || userId.equals("")) {
			throw new BusinessException("发起人的ID不能为空.",-1);
		}
		
		Object   favoreeUserId = m.get("favoreeUserId");
		if(null == favoreeUserId || favoreeUserId.equals("")) {
			throw new BusinessException("文书制作人的ID不能为空.",-1);
		}
		Map userInfo = userSer.getUserInfo(Integer.valueOf((String)userId));
		    //产生一个支付订单 
	    	//如果没有支付订单记录，产生一个支付订单，返回页面去支付；
	    	Map orderM = new HashMap<String,Object>();
			orderM.put("orderName",realName+"/"+userId ); 
			orderM.put("userId", userId); 
			orderM.put("businessType", PayOrder.TYPE_FYWSZZ);
			orderM.put("remark", "");
			orderM.put("userName", realName);
			orderM.put("mobile", userInfo.get("mobile"));
			orderM.put("orderNum", 1);
			orderM.put("orderType", OrderType.ORDERTYPE_1);
			orderM.put("sourceType", PayOrder.TYPE_FYWSZZ);
			orderM.put("orderPrice", 0); //赏金
			//orderM.put("connectionId","");//业务Id为"" 因为文书制作是没有业务文章的
			orderM.put("favoreeUserId", favoreeUserId); //制作人的Id 
			//orderM.put("checkTime", new Date()); 
			//orderM.put("endTime",); //结束时间 
			orderM.put("isPay", 2);  //是否支付  ，0：不需要支付      1： 已支付   2：未支付  3: 正在支付中  	4：支付失败  5：已退款
			orderM.put("evaluate", 2); //是否评价   0：不需要评价    1：已评价  2：未评价， 
			orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
			orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货 ；
			orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
			orderM.put("orderState", 1);//订单的状态； 1 ： 还在进行中， 0 ：已完成 
			orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败 
			try {
				orderser.saveBusinessOrder(orderM);
				out.print(WebUtils.responseData("申请成功，进行支付。", 1, orderM));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BusinessException("申请文制作失败.",-1);
			}
	}
	
	
	/**
	 * 订单列表中文书制作【确认收货】
	 */
	@RequestMapping("reception")
	public void reception(
			HttpServletRequest request,
			PrintWriter out) {
		Map map = getMap(request);
		Object businessOrder = map.get("businessOrder");
		if(null == businessOrder || businessOrder.equals("")) {
			throw new BusinessException("订单号不能为空.",-1);
		}
		Object userId = map.get("userId");
		if(null == userId || userId.equals("")) {
			throw new BusinessException("userId不能为空.",-1);
		}
		
		//把这一个订单查询出来， 看是否存在 ；
		try {
			List list = this.orderser.findOrderDetails(map);
			if(null == list || list.size() == 0 ) {
				throw new BusinessException("操作失败,操作订单不存在.",-1);
			}else {
				Map dataM = (Map)list.get(0);
				int  sender = null == dataM.get("reception") ?   0  : (int)dataM.get("reception");
				if(sender == 2) {
					throw new BusinessException("订单已收货.",-1);
				}else { 
					map.put("reception", 2);//文需求者状态改为未收货
					map.put("orderState", 0);//订单已完成
					map.put("endTime", new Date());//订单已完成
					this.orderser.update(map);
					
					//查询出咨询师的属性对象；

					//Map counMap = userSer.findCounselorByUserId((int)dataM.get("favoreeUserId"));
					Map counMap = userSer.findCounselorByUserId(Integer.parseInt(dataM.get("favoreeUserId").toString()));


					//Map counMap = userSer.findCounselorByUserId((int)dataM.get("favoreeUserId"));

					if(null == counMap || counMap.size() == 0) {
						throw new BusinessException("文书制作人不匹配",-1); 
					}
					
					Double reward =  Tools.rwipeOffDecimals( (null == dataM.get("orderPrice") ? 0D :((BigDecimal)dataM.get("orderPrice")).doubleValue()));
					//操作平台的费用；
					reward = VipUtils.sysTemCost(OrderType.businessType_FYWSZZ, reward);
					
					/**
					Map orderM = new HashMap<String,Object>();
					orderM.put("orderName", dataM.get("orderName")); 
					orderM.put("userId",  dataM.get("favoreeUserId")); 
					orderM.put("businessType", OrderType.businessType_FYWSZZ);
					orderM.put("remark", "");
					orderM.put("orderNum", 1);
					orderM.put("orderType", OrderType.ORDERTYPE_3);
					orderM.put("sourceType", OrderType.businessType_FYWSZZ);
					orderM.put("orderPrice", reward); //赏金
					//orderM.put("connectionId", 0);
					orderM.put("checkTime", new Date()); 
					orderM.put("endTime", new Date()); //赏金
					orderM.put("evaluate", 0); //是否评价   0：不需要评价   1：已评价  2：未评价， 
					orderM.put("sender", 0); //是否发货 ， 0 : 不需要发货    1：未发货， 2：已发货
					orderM.put("isPay", 0);  //是否支付  ，0：不需要支付      1： 已支付   2：未支付  3: 正在支付中  	4：支付失败
					orderM.put("reception", 0);   //是否收货 ， 0：不需要收货类型  1：未收货 ，2： 已收货；
					orderM.put("businessOrder",CreateOrderNumber.create()); //订单号  
					orderM.put("orderState", 0);//订单的状态； 1 ： 还在进行中， 0 ：已完成 
					orderM.put("isComplain", 0);//是否申诉， 0 ：不需要申诉  ，1：申诉中 ；2：申诉成功 3：申诉失败 
					orderser.saveBusinessOrder(orderM);
					**/
		    		
		    		//变更咨询师的钱包金额； 
					Double money = Tools.rwipeOffDecimals( (null == counMap.get("userBalance") ?  0D : ( (BigDecimal)counMap.get("userBalance")).doubleValue()) +reward);

					/* userSer.updateCounselorBalance((int)dataM.get("favoreeUserId"), money); */
					userSer.updateCounselorBalance(Integer.parseInt(dataM.get("favoreeUserId").toString()) , money); 

		    		//插入一条咨询师的交易流水记录； 
		    		Map walletM = new HashMap<String,Object>();
		    		walletM.put("userId", Integer.parseInt(dataM.get("favoreeUserId").toString()));

		    		walletM.put("businessOrder", businessOrder);
		    		walletM.put("balance", money);
		    		walletM.put("totalFee", reward);
		    		walletSer.saveWalletRecord(walletM); 
		    		
		    		
					//发送站内信；
					Map<String, Object> msgMap1 = new HashMap<String, Object>();
	    	       msgMap1.put("isRead","0"); //0:没读 1：已读
	    	       msgMap1.put("sendTime",new Date()); //0:没读 1：已读
	    	       msgMap1.put("isDelete","0"); //0:未删除 1：已删除
	    	       msgMap1.put("messageState","0"); //
	    	       msgMap1.put("is_send","1"); //0:未发送1：已发送
	    	       msgMap1.put("create_time",new Date()); 	
	    	       msgMap1.put("userId",dataM.get("favoreeUserId")); 
	    	       msgMap1.put("messageContent","尊敬的用户，您的【"+dataM.get("businessType")+"】已收货，并成功收到货款 "+reward+" 元。");
	    	       msgMap1.put("messageType",dataM.get("sourceType")+"赏金");
	    	       msgMap1.put("businessType","1");
	    	       msgMap1.put("businessId",businessOrder);
	    	       msgMap1.put("messageTitle",dataM.get("orderName"));  
	    	       userSer.messageSend(msgMap1);
		    		
					
					out.print(WebUtils.responseData("发货成功.", 1, map));
				} 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException(e.getMessage(),-1);
		}
		
	}
	
	/**
	 * 订单列表中文书制作【确认发货】
	 */
	@RequestMapping("sender")
	public void sender(
			HttpServletRequest request,
			PrintWriter out
		 ) {
		Map map = getMap(request);
		Object businessOrder = map.get("businessOrder");
		if(null == businessOrder || businessOrder.equals("")) {
			throw new BusinessException("订单号不能为空.",-1);
		}
		Object favoreeUserId = map.get("favoreeUserId");
		if(null == favoreeUserId || favoreeUserId.equals("")) {
			throw new BusinessException("文制作人Id不能为空.",-1);
		}
		//把这一个订单查询出来， 看是否存在 ；
		try { 
			List list = this.orderser.findOrderDetails(map);
			if(null == list || list.size() == 0 ) {
				throw new BusinessException("操作失败,操作订单不存在.",-1);
			}else {
				Map dataM = (Map)list.get(0);
				//判断是否支付；
				int   isPay = null == dataM.get("isPay") ?   1  : (int)dataM.get("isPay");
				if(isPay == 1) {   
					int  sender = null == dataM.get("sender") ?   0  : (int)dataM.get("sender");
					if(sender == 2) {
						throw new BusinessException("订单已发货.",-1);
					}else {
						map.put("sender", 2); //文书制作人状态改为已发货
						map.put("reception", 1);//文需求者状态改为未收货
						this.orderser.update(map);
						
						//发送站内信；
						Map<String, Object> msgMap1 = new HashMap<String, Object>();
		    	       msgMap1.put("isRead","0"); //0:没读 1：已读
		    	       msgMap1.put("sendTime",new Date()); //0:没读 1：已读
		    	       msgMap1.put("isDelete","0"); //0:未删除 1：已删除
		    	       msgMap1.put("messageState","0"); //
		    	       msgMap1.put("is_send","1"); //0:未发送1：已发送
		    	       msgMap1.put("create_time",new Date()); 	
		    	       msgMap1.put("userId",dataM.get("userId")); 
		    	       msgMap1.put("messageContent","尊敬的用户，您的【"+dataM.get("businessType")+"】已发货，请注意查收。");
		    	       msgMap1.put("messageType",dataM.get("sourceType"));
		    	       msgMap1.put("businessType","1");
		    	       msgMap1.put("businessId",dataM.get("businessOrder"));
		    	       msgMap1.put("messageTitle",dataM.get("orderName"));  
		    	       userSer.messageSend(msgMap1);
						
		    	     
						out.print(WebUtils.responseData("发货成功.", 1, map));
					} 
				}else {
					throw new BusinessException("订单还没有支付，不能发货.",-1);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException(e.getMessage(),-1);
		}
	}
	
	/**
	 * 订单列表中   【取消订单】
	 */
	@RequestMapping("cancelOrder")
	public void cancelOrder(
			HttpServletRequest request,
			PrintWriter out
		 ) {
		Map map = getMap(request);
		Object businessOrder = map.get("businessOrder");
		if(null == businessOrder || businessOrder.equals("")) {
			throw new BusinessException("订单号不能为空.",-1);
		}
		Object userId = map.get("userId");
		if(null == userId || userId.equals("")) {
			throw new BusinessException("userId不能为空.",-1);
		}
		
		try {
			List list = this.orderser.findOrderDetails(map);
			if(null == list || list.size() == 0 ) {
				throw new BusinessException("操作失败,操作订单不存在.",-1);
			}else {
				//判断这个订单是否完成；
			 Map 	dataM = (Map)list.get(0);
			int  orderState = (int)dataM.get("orderState");
			//如果还在进行中
			 if(orderState == 1) {
				int isPay = (int)dataM.get("isPay");
				//如果支付订单是未支付，或者是支付失败；
				if(isPay == 2 || isPay ==3) {
					map.put("orderState", 2);
					this.orderser.update(map);
					out.print(WebUtils.responseData("订单已取消。",1, map));
				}else {
					 throw new BusinessException("操作失败,操作订单不能取消.",-1);
				} 
			 }else {
				 throw new BusinessException("操作失败,操作订单不能取消.",-1);
			 } 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException(e.getMessage(),-1);
		}
		
	}
	
	
	/**
	 * 加载评价页面
	 * @param request
	 * @param out
	 */
	@RequestMapping("listOrderEvaluate")
	public void listOrderEvaluate(
			HttpServletRequest request,
			PrintWriter out
			) {
		Map map = new HashMap<String,Object>();
		String businessOrder = request.getParameter("businessOrder"); 
		if(null == businessOrder || businessOrder.equals("")) {
			throw new BusinessException("订单号不能为空.",-1);
		}
		map.put("businessOrder", businessOrder);
		String remark = request.getParameter("remark"); 
		if(null == remark || remark.equals("")) {
			throw new BusinessException("咨询师不能为空.",-1);
		}
		//需要根据 remark 的值 来判断需要查询几位咨询师的评分
		String []  userIds = remark.split("/");
		String joinSql = "";
		int i = 0 ; 
		for(String ids : userIds) {
			 String[] id =  ids.split(":");
			 String asA = "";
			 String saA_b = "";
			 String saA_c = "";
			 if(i == 0) {
				 asA = "userA";
				 saA_b= "ua";
				 saA_c="userAname";
			 }else if(i == 1) {
				 asA = "userB";
				 saA_b= "ub";
				 saA_c="userBname";
			 }else if(i == 2) {
				 asA = "userC";
				 saA_b= "uc";
				 saA_c="userCname";
			 }
			 joinSql+= " '"+ id[0]+"' as "+saA_c+", "+id[1] +" as "+asA+" ,   (SELECT count(*) from ali_evaluate where userId="+id[1] +" and businessOrder = '"+businessOrder+"') as  "+saA_b+",";
			 i++; 
		}
		map.put("joinSql", joinSql);
		
		try {
			List list = this.orderser.listOrderEvaluate(map);
			out.print(WebUtils.responseData("查询成功。",1, list));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("查询失败.",-1);
		}
	}
	 
	
}
