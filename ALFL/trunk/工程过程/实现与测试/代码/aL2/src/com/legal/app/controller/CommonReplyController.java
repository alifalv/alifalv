package com.legal.app.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.log.BusinessException;
import com.common.web.WebUtils;
import com.legal.app.service.CommonReplyService;
import com.legal.app.utils.BusinessType;

@Controller
@RequestMapping("reply")
public class CommonReplyController extends SuperController{

	@Autowired
	private CommonReplyService commonReplyService;
	
	/**
	 * 统一回复功能
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("/commonReply")
	public void commonReply(
				HttpServletRequest request,
				PrintWriter out
			) throws Exception{
		
		Map map = new HashMap();
		
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("登陆用户Id不能为空.",-1);
		} 
		map.put("userId", Integer.parseInt(userId)); 
		
		String sendUserId = request.getParameter("sendUserId");
		if(sendUserId == null || sendUserId.equals("")){
			throw new BusinessException("针对回复人的Id不能为空.",-1);
		} 
		map.put("sendUserId", Integer.parseInt(sendUserId));
		
		String caseId = request.getParameter("caseId");  
		if(caseId == null || caseId.equals("")){
			throw new BusinessException("回复数据的Id不能为空.",-1);
		} 
		map.put("relativeId", Integer.parseInt(caseId)); 
		
		
		Object sourceArticleId = request.getParameter("sourceArticleId");
		if(sourceArticleId == null || sourceArticleId.equals("")){
			throw new BusinessException("源文章的Id不能为空.",-1);
		}
		map.put("sourceArticleId", sourceArticleId);
		
		
		String replyContent = request.getParameter("replyContent"); 
		if(replyContent == null || replyContent.equals("")){
			throw new BusinessException("回复内容不能为空.",-1);
		}
		map.put("replyContent", replyContent);
		
	   String businessType = request.getParameter("businessType");
	   if(businessType == null || businessType.equals("")) {
		   throw new BusinessException("业务类型不能为空.",-1);
	   }
		map.put("businessType", Integer.parseInt(businessType));
		
		String upId = request.getParameter("upId");
		if(upId == null || upId.equals("")) {
			throw new BusinessException("针对回复的id不能为空.",-1);
		}
		map.put("upId", upId);
		
		String serviceName = request.getParameter("serviceName");
		if(serviceName == null || serviceName.equals("")){
			throw new BusinessException("文章标题不能为空.",-1);
		}
		map.put("serviceName", serviceName); 
		map.put("isDelete",0); 
		
		
		try {
			 commonReplyService.commonReply(map); 
			out.print(WebUtils.responseData(map));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("回复失败.", -1);
		}
		
	}
	
	/**
	 * 【百姓吐槽详情、阿里裁判详情】的回帖列表
	 * @param businessType   业务类型【1：百姓吐槽 2：阿里裁决 3：法律咨询 4：案件竞拍 5：文书制作】
	 * @param relativeId 贴子id
	 * @param pageNo 开始页数
	 * @param pageSize  每页多少行
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("commonReplyList/{relativeId}/{businessType}/{pageNo}/{pageSize}")
	public void findCommonReplyList(
				@PathVariable("businessType") int businessType,
				@PathVariable("relativeId") int relativeId,
				@PathVariable("pageNo") int pageNo,
				@PathVariable("pageSize") int pageSize,
				HttpServletRequest request,
				PrintWriter out
			) throws Exception{
		List<Map> list = commonReplyService.findReplyList(relativeId, businessType,pageNo,pageSize);
		Map map = getMap(request);
		int model = 1;
		Object model_tmp = map.get("model");
		if(null != model_tmp && !model_tmp.toString().equals("")) {
			model = Integer.valueOf(model_tmp.toString());
		}
		out.print(WebUtils.webResponsePage(list, model));
	}
}
