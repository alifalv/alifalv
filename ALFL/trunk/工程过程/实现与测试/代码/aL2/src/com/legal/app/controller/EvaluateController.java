package com.legal.app.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.log.BusinessException;
import com.common.web.WebUtils;
import com.legal.app.service.EvaluateService;
import com.legal.app.service.UserService;
import com.legal.app.utils.CheckEvaluate;
import com.legal.app.utils.PayOrder;
/**
 * 军山依旧在
 * @author Administrator
 *用户评论功能；
 */
@Controller
@RequestMapping("Evaluate")
public class EvaluateController extends SuperController{
	
	@Autowired
	private EvaluateService evaSer;
	
	@Autowired
	private UserService userSer;
	
	@RequestMapping("listEvaluate")
	public void listEvaluate(
			PrintWriter out,
			HttpServletRequest request
			) {
		   Map map = getMap(request);
		   Object businessOrder = map.get("businessOrder");
		   if(businessOrder  == null || businessOrder.equals("")) {
			   throw new BusinessException("订单编号不能为空.",-1);
		   } 
		   Object userId = map.get("userId");
		   if(userId  == null || userId.equals("")) {
			   throw new BusinessException("咨询师Id不能为空.",-1);
		   }
		   try {
			List  list = evaSer.listEvaluate(map);
			out.print(WebUtils.responseData("查询成功。",1, list));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("查询失败.",-1);
		}
	}
	
	@RequestMapping("saveEvaluate")
	public void saveEvaluate(
			PrintWriter out,
			HttpServletRequest request
			) {
		   Map map = getMap(request);

		   
		   Object businessOrder = map.get("businessOrder");
		   if(businessOrder  == null || businessOrder.equals("")) {
			   throw new BusinessException("订单编号不能为空.",-1);
		   } 
		   Object userId_ = map.get("userId");
		   if(userId_  == null || userId_.equals("")) {
			   throw new BusinessException("咨询师Id不能为空.",-1);
		   } 
		   
		   Object evaluateUserId = map.get("evaluateUserId");
		   if(evaluateUserId  == null || evaluateUserId.equals("")) {
			   throw new BusinessException("评价人Id不能为空.",-1);
		   } 

		   Integer userId = Integer.valueOf((String)userId_);
		   try { 
			   //查询  根据 这个订单号和userId 查询是否评价，如果已经评价，就不能再次评价；
			    List listData = this.evaSer.listEvaluate(map);
			   if(null != listData && listData.size() > 0  ) {
				   throw new BusinessException("评价失败，已经评价过.",-1);
			   }
		   
			//根据提交的数据，返回每项得分的
			CheckEvaluate.check(map);
			//查询出咨询师的属性对象;
			Map c_user = userSer.findCounselorByUserId(userId);
			//查询出历史评分记录；
			List<Map> countMap  = this.evaSer.countsCounselorScore(map);
			Map backMap = countMap.get(0);
			//根据 本次的评分 与 咨询师的历史评分记录 汇总，算出
			CheckEvaluate.scoreCount(map,backMap , c_user); 
			map.put("is_delete", 0);
			//保存当前的评价信息；
			this.evaSer.saveEvaluate(map);
			
			//修改咨询师的评分数据 ；为防止错误提交；只需要把需要的提交；
			Map  couMap = new HashMap<String,Object>();
			couMap.put("id", c_user.get("id"));
			couMap.put("userId", c_user.get("userId"));
			couMap.put("levelScore", c_user.get("levelScore"));
			couMap.put("attitudeScore", c_user.get("attitudeScore"));
			couMap.put("sourceScore", c_user.get("sourceScore"));
			couMap.put("chargeScore", c_user.get("chargeScore"));
			couMap.put("commonScore", c_user.get("commonScore"));
			couMap.put("goodNum", c_user.get("goodNum"));
			couMap.put("middleNum", c_user.get("middleNum"));
			couMap.put("badNum", c_user.get("badNum")); 
			this.userSer.updateCounselor(couMap);
			out.print(WebUtils.responseData("评价成功。",1, map));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException(e.getMessage(),-1);
		}
	}

}
