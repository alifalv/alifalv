package com.legal.app.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.dbutil.Paging;
import com.common.log.BusinessException;
import com.common.utils.SMSUtilLiantong;
import com.common.utils.StringUtil;
import com.common.web.WebUtils;
import com.legal.app.service.CaseService;
import com.legal.app.service.OrderService;
import com.legal.app.service.UserService;
import com.legal.app.utils.BusinessType;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("case")
public class CaseController extends SuperController{
	
	@Autowired
	private UserService userService;
	@Autowired
	private CaseService caseService;
	@Autowired
	private OrderService orderService;
	/**
	 * 发布案件委托
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("sendCase")
	public void sendCase(HttpServletRequest request,
			PrintWriter out) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>(); 
		String userId = request.getParameter("userId"); 
		if(userId==null||userId.trim().equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		
		map.put("userId", userId);
		
		String title = request.getParameter("caseTitle");
		if(title==null||title.trim().equals("")){
			throw new BusinessException("标题不能为空.", -1);
		}
		map.put("caseTitle", title);
		
		String caseDesc = request.getParameter("caseDesc");
		if(caseDesc==null||caseDesc.trim().equals("")){
			throw new BusinessException("案情描述不能为空.", -1);
		}
		map.put("caseDesc", caseDesc);
		
		String caseHope = request.getParameter("caseHope");
		map.put("caseHope", caseHope);
		
		String caseAsk = request.getParameter("caseAsk");
		map.put("caseAsk", caseAsk);
		
		String province = request.getParameter("province");
		if(province==null||province.trim().equals("")){
			throw new BusinessException("省份不能为空.", -1);
		}
		map.put("province", province); 
		
		String city = request.getParameter("city");
		if(city==null||city.trim().equals("")){
			throw new BusinessException("城市不能为空.", -1);
		}
		map.put("city", city); 
		String [] caseImgs_tmp = request.getParameterValues("caseImgs");
		String caseImgs = "";
		if(null != caseImgs_tmp && caseImgs_tmp.length >0) {
			caseImgs = StringUtils.join(caseImgs_tmp, ",");
		}
		map.put("caseImgs", caseImgs);
		
		String caseType = request.getParameter("caseType");
		if(caseType==null||caseType.trim().equals("")){
			throw new BusinessException("案件类型不能为空.", -1);
		}
		map.put("caseType", caseType);
		

		
		//  1 价格 2 面议
		String offerType = request.getParameter("offerType");
		if(offerType==null||offerType.trim().equals("")){
			throw new BusinessException("报价类型不能为空.", -1);
		} 
		map.put("offerType", Integer.parseInt(offerType));
	  	
		//报价金额
		String offerMoney =  request.getParameter("offerMoney");
		//map.put("offerMoney", offerType.equals("1")?new BigDecimal(offerMoney):0);
		map.put("offerMoney", offerMoney);
		
		if(offerType.equals("2")) {
			//如果等于 2 就是面议，价格为0
			map.put("offerMoney", "0");
		}
		

		
		String mobile = request.getParameter("mobile");
		if(mobile==null||mobile.trim().equals("")){
			throw new BusinessException("手机号码不能为空.", -1);
		}
		
		map.put("mobile",mobile);
		//获取用户信息 
		Map<String,Object> user = userService.getUserInfo(Integer.parseInt(userId));
		map.put("userName", user.get("nickName"));
		map.put("userImg", user.get("userImg"));  
		
		try {
			caseService.saveCaseDepute(map); 
			//发布成功，需要为本市的律师发送短信；
			Map cityMap = new HashMap<>();
			cityMap.put("city", city);
			List list = userService.listCounselor(cityMap);
			this.sendCounselorMsg(list);
			//返回案件id
			out.print(WebUtils.responseData("发布成功.",1,map));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("发布案件委托失败.", -1);
		} 
	}
	
	/**
	 * 当咨询人员发布案件后，按案件的发布地点，为这个地区的律师发送短信
	 * @param list
	 */
	private void sendCounselorMsg(List<Map> list) {
		if(null != list && list.size()>0) {
			//启一个线程为这个城市的律师发送短信；
			Thread th = new Thread(new Runnable() { 
				@Override
				public void run() {
					 for(Map m : list ) {
						Object mobile =  m.get("mobile");
						if(null != mobile) {
							try {
								SMSUtilLiantong.sendNewAy(mobile.toString());
							} catch (Exception e) { 
								e.printStackTrace();
							}
						}
					 } 
				}
			});
			th.start(); 
		}
	}
	
	
	/**
	 * 案件列表
	 * @param city
	 * @param caseType
	 * @param pageNo
	 * @param pageSize
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("caseList/{pageNo}/{pageSize}")
	public void caseList(
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		
		   Map map = new HashMap();
		   String city = request.getParameter("city");
		   String caseType = request.getParameter("caseType");
		   String caseTitle = request.getParameter("caseTitle");
		   String province = request.getParameter("province");
		   
		   if(null!=caseTitle&&!caseTitle.equals("")){
			   map.put("caseTitle",caseTitle);
		   }
		   
		   if(null != province && !province.equals("")){
			   map.put("province",province);
		   }
		   if(null != city && !city.equals("")){
			   map.put("city",city);
		   }
		   if(null!=caseType&&!caseType.equals("")){ 
				   map.put("caseType", caseType); 
		   }
		  
		   Paging paging = new Paging(pageSize, pageNo,true);
		   
		   List<Map> list = caseService.findCaseList(map, paging);
		   out.print(WebUtils.webResponsePage(list));
		
	}
	
	
	/**
	 * 案源文章的详情， 并且返回上一面和下页的标题与id
	 * @param caseId
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("caseDetails/{caseId}")
	public void caseDetails(
			@PathVariable("caseId") int caseId,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		
		Map map = new HashMap();
		
		map.put("caseId", caseId);
		/**
		//获取查询条件 城市
		String city  = request.getParameter("city");
		//获取 案件类型
		String caseType = request.getParameter("caseType");
		
		if(city!=null&&!city.equals("")){
			map.put("city", Integer.parseInt(city));
		}
		
		if(caseType!=null&&!caseType.equals("")){
			map.put("caseType", Integer.parseInt(caseType));
		}
		**/
		//获取案件竞拍详情
		
		try {
			Map details = caseService.findCaseDetailsInfo(map);
			out.print(WebUtils.responseData(details));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("获取案源详情失败.", -1);
		}
	}
	
	
	
	
	
	/**
	 * 案源竞拍
	 * @param caseId 案源ID
	 * @param userId  登陆用户Id
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("caseReply/{caseId}/{userId}")
	public void caseReply(
			@PathVariable("caseId") int caseId,
			@PathVariable("userId") int userId,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		
		Map map = new HashMap();
		
		map.put("caseId", caseId);
		map.put("userId",userId);
		
		//获取咨询师竞拍信息
		String effect = request.getParameter("effect");
		if(effect==null||effect.trim().equals("")){
			throw new BusinessException("可能的效果不能为空.", -1);
		}
		map.put("effect", effect);
		
		String thinking = request.getParameter("thinking");
		if(thinking==null||thinking.trim().equals("")){
			throw new BusinessException("解决思路不能为空.", -1);
		}
		map.put("thinking", thinking);
		
		String offerMoney = request.getParameter("offerMoney");
		if(thinking==null||thinking.trim().equals("")){
			throw new BusinessException("报价不能为空.", -1);
		}  
		map.put("offerMoney", offerMoney);
		  
		
		String mobile = request.getParameter("mobile");
		if(mobile==null||mobile.trim().equals("")){
			throw new BusinessException("号码不能为空.", -1);
		}
		map.put("mobile", mobile);
		
		String sendPerson = request.getParameter("sendPerson");
		if(sendPerson == null || sendPerson.equals("")) {
			throw new BusinessException("发贴人Id不能为空.", -1);
		} 
		map.put("sendPerson", sendPerson);
		
		//获取用户信息
		Map user = userService.findUserBasicInfoByUserId(userId);
		map.putAll(user);
		
		try {
			 caseService.offerCase(map); 
			out.print(WebUtils.responseData(map));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("竞拍案件失败.", -1);
		}
	}
	
	
	/**
	 * 案源聘请
	 * @param offerId  案源竞拍id 
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("employ/{offerId}")
	public void employ(
			@PathVariable("offerId") int offerId, 
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{ 
		try {
			Map m = caseService.employ(offerId);
			out.print(WebUtils.responseData("操作成功.", 1, m));
		} catch (Exception e) {
			e.printStackTrace();
			String msg = e.getMessage();
			throw new BusinessException(msg, -1);
		}
	}
	
	/**
	 * 案件委托确认完成按钮
	 * @param request
	 * @param out
	 */
	@RequestMapping("/caseDeputeAccomplish")
	public void caseDeputeAccomplish(
			HttpServletRequest request,
			PrintWriter out) {
		Map m = getMap(request);
		Object businessOrder = m.get("businessOrder");
		if(null == businessOrder || businessOrder.equals("")) {
			throw new BusinessException("订单编号不能为空.", -1);
		} 
		try {
			this.caseService.caseDeputeAccomplish(m);
			out.print(WebUtils.responseData("操作成功.", 1, m));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("操作失败.", -1);
		}
		
	}
	

	/**
	 * 案源竞拍列表
	 * @param caseId
	 * @param pageNo
	 * @param pageSize
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("offerList/{caseId}/{sendCaseUserId}/{loginUserId}/{pageNo}/{pageSize}")
	public void findOfferList(
			@PathVariable("caseId") int caseId,
			@PathVariable("sendCaseUserId") int sendCaseUserId,
			@PathVariable("loginUserId") int loginUserId,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			PrintWriter out
			) throws Exception{
		Map map = new HashMap();
		map.put("caseId",caseId);
		map.put("businessType",BusinessType.REPLY_OFFER);
		Paging paging = new Paging(pageSize, pageNo, true);
 
		//功能需求，当访问页面的人与发帖的人是同一个时，就把参与竞拍的人都展示出来；查询竞拍列表时就不需要带上竞拍的人的ID
		//否则查询竞拍列表时，只能查询登陆人的id
		if(sendCaseUserId != loginUserId) {
			map.put("userId",loginUserId);
		}
		List<Map> list = caseService.findOfferList(map,paging);
		int model = 1;
		Object model_tmp = map.get("model");
		if(null != model_tmp && !model_tmp.toString().equals("")) {
			model = Integer.valueOf(model_tmp.toString());
		}
		out.print(WebUtils.webResponsePage(list, model));
		//out.print(WebUtils.responseData(list));
	}
	
	@RequestMapping("myCaseList/{userId}/{pageNo}/{pageSize}")
	public void myCaseList(
			@PathVariable("userId") int userId, 
			@PathVariable("pageNo") int pageNo, 
			@PathVariable("pageSize") int pageSize,  
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		
		Map  m= getMap(request);
		Paging paging = new Paging(pageSize, pageNo);
		try {
			List listM = this.caseService.myCaseList(m, paging);
			out.print(WebUtils.webResponsePage(listM));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.println(WebUtils.responseError("查询失败.",-1));
		}
		/**
		List<Map> list = caseService.findCaseListByUserId(userId);
		out.print(WebUtils.responseData(list));**/
	}
	/**
	 * 咨询师账户中心-我竞拍的案件列表
	 * @param userId
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("myOfferCase/{userId}/{pageNo}/{pageSize}")
	public void myOfferCase(
			@PathVariable("userId") int userId, 
			@PathVariable("pageNo") int pageNo, 
			@PathVariable("pageSize") int pageSize,  
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Paging paging = new Paging(pageSize, pageNo,true);
		Map map = new HashMap<String, String>();
		map.put("userId", userId);
		List<Map> list = caseService.myOfferCaseList(map,paging);
		out.print(WebUtils.webResponsePage(list));
	}
	
	
	@RequestMapping("deleteCase/{caseId}")
	public void deleteCase(
			@PathVariable("caseId") int caseId,
			HttpServletRequest request,
			PrintWriter out
			) {
		try {
			Map map = getMap(request);
			int num = caseService.deleteCase(caseId);
			if(num == 0 ) {
				 map.put("ok",false);
				out.print(WebUtils.responseData("删除失败，已经有回复。", 1, map));
			}else {
				map.put("ok",true);
				out.print(WebUtils.responseData("删除成功。", 1,  map));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("删除失败.", -1);
		}
		
		
	}
	
	@RequestMapping("casePrivateDeal/{caseId}")
	public void casePrivateDeal( 
			@PathVariable("caseId") int caseId,
			PrintWriter out
			) throws Exception{
		Map map = new HashMap<String,Object>();
		map.put("caseId", caseId); 
		map.put("caseState", "2"); 
		this.caseService.casePrivateDeal(map);
		out.print(WebUtils.responseData("操作成功。", 1,  map));
	}
	
}
