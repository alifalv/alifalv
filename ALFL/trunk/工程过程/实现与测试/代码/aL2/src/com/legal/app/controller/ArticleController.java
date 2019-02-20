package com.legal.app.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.dbutil.Paging;
import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;
import com.common.utils.StringUtil;
import com.common.utils.SystemConfigUtil;
import com.common.web.WebUtils;
import com.legal.app.service.ArticleService;
import com.legal.app.service.UserService;
import com.legal.app.utils.SendArticleType;

@SuppressWarnings({"rawtypes","unchecked"})
@Controller
@RequestMapping("article")
public class ArticleController
  extends SuperController
{
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private UserService userService;
	
	@Value("#{configProperties['txtPath']}")
	private String txtPath;
	
	/**
	 * 获取所有文章的基础表；
	 * @param request
	 * @param out
	 */
	@RequestMapping("listArticle/{model}/{pageNo}/{pageSize}")
	public void listArticle(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Map map = getMap(request);
		Object startTime = map.get("startTime");
		if(null != startTime && startTime.equals("")) {
			map.put("startTime",startTime.toString()+" 00:00:00");
		}
		Object endTime =  map.get("endTime");
		if(null != endTime && endTime.equals("")) {
			map.put("endTime",endTime.toString()+" 23:59:59");
		}
		Object isDelete = map.get("isDelete");
		if(null == isDelete || isDelete.equals("")) {
			map.put("isDelete","0");
		} 
		
		//如果是PC，不是Admin用户查询，需要查询的是没有删除；并且是发布出去了的。
		if(model ==1) {
			map.put("isDelete","0");
			map.put("isPush", "1");
		} 
 
		
		//查询都是已发布的
		Paging paging = new Paging(pageSize, pageNo, true);
	    List list = 	this.articleService.listArticle(map,paging);
		out.print(WebUtils.webResponsePage(list, model));
	  //  out.print(WebUtils.responseData("查询成功", 1, list));
		
	}
	
	
	/**
	 * 首页获取所有今日推荐文章的；
	 * @param request
	 * @param out
	 */
	@RequestMapping("todayPushlistArticle")
	public void todayPushlistArticle(
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
	    List list = 	this.articleService.todayPushlistArticle();
		out.print(WebUtils.responseData(list));
	}
	
	
	/**
	 * 我的文采
	 */
	@RequestMapping("/myMien/{model}/{userId}/{pageNo}/{pageSize}")
	public void myMien(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			@PathVariable("userId") int userId,
			PrintWriter out
			){ 
		Paging paging = new Paging(pageSize, pageNo, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_10);
		//map.put("columnCode", SystemConfigUtil.getColumnCode(1));
		try {
			List<Map> list = articleService.findArticleByColumnId(map, paging);
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("我的风采获取失败.", -1);
		}
	}
	
	/**
	 * 工作动态
	 */
	@RequestMapping("/myWork/{model}/{userId}/{pageNo}/{pageSize}")
	public void myWork(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			@PathVariable("userId") int userId,
			PrintWriter out
			){
		
		Paging paging = new Paging(pageSize, pageNo, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_11);
		//map.put("columnCode", SystemConfigUtil.getColumnCode(2));
		try {
			List<Map> list = articleService.findArticleByColumnId(map, paging);
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("工作动态获取失败.", -1);
		}
	}
	

	
	/**
	 * 成功案例
	 */
	@RequestMapping("/mySuccess/{model}/{userId}/{pageNo}/{pageSize}")
	public void mySuccess(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			@PathVariable("userId") int userId,
			PrintWriter out
			){
		
		Paging paging = new Paging(pageSize, pageNo, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);

		map.put("columnCode", SendArticleType.SENDARTICLETYPE_12);
		//map.put("columnCode", SystemConfigUtil.getColumnCode(3));
		try {
			List<Map> list = articleService.findArticleByColumnId(map, paging);
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("成功案例获取失败.", -1);
		}
	}
	
	
	@RequestMapping("/myEssay/{model}/{userId}/{pageNo}/{pageSize}")
	public void myEssay(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			@PathVariable("userId") int userId,
			PrintWriter out
			){
		
		Paging paging = new Paging(pageSize, pageNo, true);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_13);
		//map.put("columnCode", SystemConfigUtil.getColumnCode(4));
		try {
			List<Map> list = articleService.findArticleByColumnId(map, paging);
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("我的随笔获取失败.", -1);
		}
	}
	

	@RequestMapping("/commonPeopleList/{model}/{pageNo}/{pageSize}")
	public void commonPeopleList(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Paging paging = new Paging(pageSize, pageNo, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", 0);
		map.put("model", model);
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_0);
		//map.put("columnCode", SystemConfigUtil.getColumnCode(5));
		//获取查询时间
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(!StringUtil.isBlank(startTime)){
			map.put("startTime", startTime);
		}
		
		if(!StringUtil.isBlank(endTime)){
			map.put("endTime", endTime);
		}
		//查询条件
		String conditionValue = request.getParameter("conditionValue");
		
		if(conditionValue!=null&&!conditionValue.equals("")){
			map.put("conditionValue", conditionValue);
		}
		try {
			List<Map> list = articleService.findArticleByColumnId(map, paging);
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {	
			e.printStackTrace();
			throw new BusinessException("百姓吐槽列表获取失败.", -1);
		}
	}
	
	
	/**
	 * 阿里裁决列表
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("/aliAdjudicationList/{model}/{pageNo}/{pageSize}")
	public void aliAdjudicationList(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Paging paging = new Paging(pageSize, pageNo, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_9);
		//map.put("columnCode", SystemConfigUtil.getColumnCode(6));
		map.put("model", model);
		//获取查询条件
		//获取查询时间
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(!StringUtil.isBlank(startTime)){
			map.put("startTime", startTime);
		}
		
		if(!StringUtil.isBlank(endTime)){
			map.put("endTime", endTime);
		}
		//查询条件
		String conditionValue = request.getParameter("conditionValue");
		
		if(conditionValue!=null&&!conditionValue.equals("")){
			map.put("conditionValue", conditionValue);
		}
		
		String declareType = request.getParameter("declareType");
		
		if(declareType !=null &&  !declareType.equals("")){
			map.put("declareType", declareType);
		}
		
		String isDelete = request.getParameter("isDelete");
		if(isDelete == null || isDelete.equals("")) {
			map.put("isDelete",0+"");
		}
		//如果model ==1 说明前端请求，那只能查询出已经审核通过的数据；
		if(model == 1) {
			map.put("isPush", "1");
		}
		try {
			
			List<Map> list = articleService.findAliAdjudicationList(map, paging);
			for (Object m : list) {
				if(m instanceof Map){
					Map obj = (Map)m;
					obj.put("declareName",SystemConfigUtil.declareMap.get("declare"+obj.get("declareType")).getDeclareName());
				}
			}
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("阿里裁决列表获取失败.", -1);
		}
	}
	
	@RequestMapping("/bookMakeKnowledgeList/{model}/{pageNo}/{pageSize}")
	public void bookMakeKnowledgeList(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Paging paging = new Paging(pageSize, pageNo, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", 0);
		map.put("model", model);
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_4);
		//map.put("columnCode", SystemConfigUtil.getColumnCode(9));
		
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(!StringUtil.isBlank(startTime)){
			map.put("startTime", startTime);
		}
		
		if(!StringUtil.isBlank(endTime)){
			map.put("endTime", endTime);
		}
		
		//查询条件
		String conditionValue = request.getParameter("conditionValue");
		
		if(conditionValue!=null&&!conditionValue.equals("")){
			map.put("conditionValue", conditionValue);
		}
		
		try {
			List<Map> list = articleService.findArticleByColumnId(map, paging);
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("文书制作知识列表获取失败.", -1);
		}
	}
	
	@RequestMapping("/bookMakeModelList/{model}/{pageNo}/{pageSize}")
	public void bookMakeModelList(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Paging paging = new Paging(pageSize, pageNo, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", 0);
		map.put("model", model);
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_3);
		//map.put("columnCode", SystemConfigUtil.getColumnCode(10));
		
		//获取查询条件
		//获取查询时间
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(!StringUtil.isBlank(startTime)){
			map.put("startTime", startTime);
		}
		
		if(!StringUtil.isBlank(endTime)){
			map.put("endTime", endTime);
		}
		//查询条件
		String conditionValue = request.getParameter("conditionValue");
		
		if(conditionValue!=null&&!conditionValue.equals("")){
			map.put("conditionValue", conditionValue);
		}
		
		try {
			List<Map> list = articleService.findArticleByColumnId(map, paging);
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("文书制作模板列表获取失败.", -1);
		}
	}
	
	@RequestMapping("/adviceQuestionList/{model}/{pageNo}/{pageSize}")
	public void adviceQuestionList(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Paging paging = new Paging(pageSize, pageNo, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", 0);
		map.put("model", model);
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_6);
		//map.put("columnCode", SystemConfigUtil.getColumnCode(11));
		//获取查询条件
		//获取查询时间
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(!StringUtil.isBlank(startTime)){
			map.put("startTime", startTime);
		}
		
		if(!StringUtil.isBlank(endTime)){
			map.put("endTime", endTime);
		}
		//查询条件
		String conditionValue = request.getParameter("conditionValue");
		
		if(conditionValue!=null&&!conditionValue.equals("")){
			map.put("conditionValue", conditionValue);
		}
		try {
			List<Map> list = articleService.findArticleByColumnId(map, paging);
			if(list.size()>0 && list!=null) {
				for(Object m : list) {
					if(m instanceof  Map) {
						String articleTypeDesc = "";
						if(((Map)m).get("articleType")!=null && !((Map)m).get("articleType").equals("")) {
							articleTypeDesc = tpyeDescription(((Map)m).get("articleType").toString());
						}
						((Map)m).put("articleTypeDesc", articleTypeDesc);
					}
				}
			}
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("常见咨询问题列表获取失败.", -1);
		}
	}
	
	/*
	 * 常见咨询问题/法律疑难问题与观点 类型描述
	 */
	public String tpyeDescription(String type) {
		String tpyeDescription = "";
		if(type.equals("1")) {tpyeDescription = "立案问题";}
		else if(type.equals("2")) {tpyeDescription = "刑事侦查";}
		else if(type.equals("3")) {tpyeDescription = "刑事犯罪";}
		else if(type.equals("4")) {tpyeDescription = "监所问题";}
		else if(type.equals("5")) {tpyeDescription = "行政处罚";}
		else if(type.equals("6")) {tpyeDescription = "行政审批";}
		else if(type.equals("7")) {tpyeDescription = "行政复议与诉讼";}
		else if(type.equals("8")) {tpyeDescription = "婚姻家庭纠纷";}
		else if(type.equals("9")) {tpyeDescription = "交通事故纠纷";}
		else if(type.equals("10")) {tpyeDescription = "民间借贷纠纷";}
		else if(type.equals("11")) {tpyeDescription = "劳动雇佣纠纷";}
		else if(type.equals("12")) {tpyeDescription = "公司问题";}
		else if(type.equals("13")) {tpyeDescription = "房地产纠纷";}
		else if(type.equals("14")) {tpyeDescription = "担保纠纷";}
		else if(type.equals("15")) {tpyeDescription = "合同纠纷";}
		else if(type.equals("16")) {tpyeDescription = "侵权责任纠纷";}
		else if(type.equals("17")) {tpyeDescription = "知识产权纠纷";}
		else if(type.equals("18")) {tpyeDescription = "税务问题";}
		else if(type.equals("19")) {tpyeDescription = "拆迁争议";}
		else if(type.equals("20")) {tpyeDescription = "司法鉴定";}
		else if(type.equals("21")) {tpyeDescription = "民事执行";}
		else if(type.equals("22")) {tpyeDescription = "医疗纠纷";}
		else if(type.equals("23")) {tpyeDescription = "仲裁与诉讼";}
		else if(type.equals("24")) {tpyeDescription = "其他民商事纠纷";}
		return tpyeDescription;
	}
	
	
	/**
	 * 公务员招聘列表
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("/civilWorkerRecruit/{model}/{pageNo}/{pageSize}")
	public void civilWorkerRecruitList(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Paging paging = new Paging(pageSize, pageNo, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", 0);
		map.put("model", model);
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_2);
		//map.put("columnCode", SystemConfigUtil.getColumnCode(14));
		
		//获取查询条件
		//获取查询时间
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(!StringUtil.isBlank(startTime)){
			map.put("startTime", startTime);
		}
		
		if(!StringUtil.isBlank(endTime)){
			map.put("endTime", endTime);
		}
		//查询条件
		String conditionValue = request.getParameter("conditionValue");
		
		if(conditionValue!=null&&!conditionValue.equals("")){
			map.put("conditionValue", conditionValue);
		}
		
		try {
			List<Map> list = articleService.findArticleByColumnId(map, paging);
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("公务员招聘列表获取失败.", -1);
		}
	}
	
	@RequestMapping("/questionAndViewpointList/{model}/{pageNo}/{pageSize}")
	public void questionAndViewpointList(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Paging paging = new Paging(pageSize, pageNo, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", 0);
		map.put("model", model);
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_1);
		//map.put("columnCode", SystemConfigUtil.getColumnCode(12));
		//获取查询条件
		//获取查询时间
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(!StringUtil.isBlank(startTime)){
			map.put("startTime", startTime);
		}
		
		if(!StringUtil.isBlank(endTime)){
			map.put("endTime", endTime);
		}
		//查询条件
		String conditionValue = request.getParameter("conditionValue");
		
		if(conditionValue!=null&&!conditionValue.equals("")){
			map.put("conditionValue", conditionValue);
		}
		try {
			List<Map> list = articleService.findArticleByColumnId(map, paging);
			if(list.size()>0 && list!=null) {
				for(Object m : list) {
					if(m instanceof  Map) {
						String articleTypeDesc = "";
						if(((Map)m).get("articleType")!=null && !((Map)m).get("articleType").equals("")) {
							articleTypeDesc = tpyeDescription(((Map)m).get("articleType").toString());
						}
						((Map)m).put("articleTypeDesc", articleTypeDesc);
					}
				}
			}
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("法律疑难问题与观点列表获取失败.", -1);
		}
	}
	
	@RequestMapping("/legalPersonInfoList/{model}/{pageNo}/{pageSize}")
	public void legalPersonInfoList(
			@PathVariable("model") int model,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Paging paging = new Paging(pageSize, pageNo, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", 0);
		map.put("model", model);
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_5);
		//map.put("columnCode", SystemConfigUtil.getColumnCode(13));
		//获取查询时间
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(!StringUtil.isBlank(startTime)){
			map.put("startTime", startTime);
		}
		
		if(!StringUtil.isBlank(endTime)){
			map.put("endTime", endTime);
		}
		//查询条件
		String conditionValue = request.getParameter("conditionValue");
		
		if(conditionValue!=null&&!conditionValue.equals("")){
			map.put("conditionValue", conditionValue);
		}
		try {
			List<Map> list = articleService.findArticleByColumnId(map, paging);
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("法律人的信息列表获取失败.", -1);
		}
	}
	
	/**
	 * 法律培训   app pc 使用
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("/legalTrainList/{pageNo}/{pageSize}")
	public void legalTrainList( 
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		Paging paging = new Paging(pageSize, pageNo, true);
		Object model_tmp = request.getParameter("model");
		int model = 1;
		if(null != model_tmp && !model_tmp.equals("")) {
			model = Integer.valueOf((String)model_tmp);
		}
		Map<String, Object> map = getMap(request);
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_7);
		String year = (String)map.get("year");
		if(null != year && year !="") {
			String startTime = year+"-";
			String endTime = year +"-";
			String month = (String)map.get("month");
			if(null != month && month !="") {
				startTime+=month +"-00 00:00:00";
				endTime +=month +"-31 23:59:59";
			}else {
				startTime+="01-00 00:00:00";
				endTime +="12-31 23:59:59";
			} 
			map.put("startTime", startTime);
			map.put("endTime", endTime);
		}
		
		if(model == 1) {
			map.put("isPush", "1");
		}
 
	 
		List<Map> list = articleService.findLegalTrainListByApp(map, paging);
		out.print(WebUtils.webResponsePage(list,model));
		 
	}

	/**
	 * 法律法规查询  
	 * 
	 * 后台管理查询按PC的条件 
	 * @throws Exception
	 */  
	@RequestMapping("/newestLegalList")
	public void newestLegalList(
				HttpServletRequest request,
				PrintWriter out
			)throws Exception{
		
		try {

 			List list = this.articleService.newestLegalList(); 
			out.print(WebUtils.responseData(list));
		} catch (Exception e) { 
			e.printStackTrace();
			ExceptionLogger.writeLog("法律法规查询异常信息："+e+e.getClass());
			out.print(WebUtils.responseError("查询失败",-1));
		}
		
	}
	
	
	/**
	 * 法律法规查询  200条
	 * 
	 * 后台管理查询按PC的条件 
	 * @throws Exception
	 */  
	@RequestMapping("/newest200LegalList/{pageNo}/{pageSize}")
	public void newest200LegalList(@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out) throws Exception{
		try {
			Paging paging = new Paging(pageSize, pageNo, true);
			Map map = new HashMap();
 			List list = this.articleService.newest200LegalList(map, paging); 
			out.print(WebUtils.responseData(list));
		} catch (Exception e) { 
			e.printStackTrace();
			ExceptionLogger.writeLog("法律法规查询异常信息："+e+e.getClass());
			out.print(WebUtils.responseError("查询失败",-1));
		}
		
	}
	
	
	
	
	/**
	 * 法律法规查询  
	 * 
	 * 后台管理查询按PC的条件 
	 * @throws Exception
	 */  
	@RequestMapping("/legalList/{pageNo}/{pageSize}")
	public void findLegalList( 
				@PathVariable("pageNo") int pageNo,
				@PathVariable("pageSize") int pageSize,
				HttpServletRequest request,
				PrintWriter out
			)throws Exception{
		
		Map<String,Object> map = getMap(request); 
		
		String title = (String) map.get("title");
		String articleContent = (String) map.get("articleContent");
		
		String titleSql="";
		 if (title.trim().lastIndexOf("") != -1) { //含有空格
			String[] x = title.split(" ");
		    for(int i = 0;i<x.length;i++){
		     if(!x[i].equals("")){
		    	 System.out.println(x[i].trim());
		    	 titleSql+="and  a.title like   CONCAT('%','"+x[i].trim()+"','%')"; 
		     }   
		    }
			map.put("title", titleSql);
		}else{
			if(null!=title&&!title.equals("")){
				titleSql ="and  a.title like   CONCAT('%','"+title+"','%')"; 
				map.put("title", titleSql);
			}
		}
		 
		 
		 String articleContentSql ="";
			if (articleContent.trim().lastIndexOf("") != -1) { //含有空格
				String[] x = articleContent.split(" ");
			    for(int i = 0;i<x.length;i++){
			     if(!x[i].equals("")){
			    	 System.out.println(x[i].trim());
			    	 articleContentSql+="and  le.contentKey like   CONCAT('%','"+x[i].trim()+"','%')";
	 		     }   
			    }
				map.put("articleContent", articleContentSql);
			}else{
				if(null!=articleContent&&!articleContent.equals("")){
					articleContentSql ="and  le.contentKey like   CONCAT('%','"+articleContent+"','%')"; 
					map.put("articleContent", articleContentSql);
				}
			} 
		 
	/*	 String articleContentSql ="";
		if (articleContent.trim().lastIndexOf("") != -1) { //含有空格
			String[] x = articleContent.split(" ");
		    for(int i = 0;i<x.length;i++){
		     if(!x[i].equals("")){
		    	 System.out.println(x[i].trim());
		    	 articleContentSql+="and  a.articleContent like   CONCAT('%','"+x[i].trim()+"','%')";
 		     }   
		    }
			map.put("articleContent", articleContentSql);
		}else{
			if(null!=articleContent&&!articleContent.equals("")){
				articleContentSql ="and  a.articleContent like   CONCAT('%','"+articleContent+"','%')"; 
				map.put("articleContent", articleContentSql);
			}
		} */
		String startTime = (String)map.get("startTime");
		if(null != startTime && !startTime.equals("")) {
			startTime = startTime +" 00:00:00";
			map.put("startTime", startTime);
		}
		
		String endTime = (String)map.get("endTime");
		if(null != endTime && !endTime.equals("")) {
			endTime = endTime +" 21:59:59";
			map.put("endTime", endTime);
		}
		
		Object model_tmp = map.get("model");
		int model = 1;
		if(null != model_tmp && !model_tmp.equals("")) {
			model = Integer.valueOf((String)model_tmp);
		}
		map.put("columnCode", SendArticleType.SENDARTICLETYPE_8);
		Paging paging = new Paging(pageSize, pageNo, true); 
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			ExceptionLogger.writeLog("开始时间："+df.format(new Date()));// new Date()为获取当前系统时间
			List list = this.articleService.legalList(map, paging); 
			ExceptionLogger.writeLog("结束时间："+df.format(new Date()));// new Date()为获取当前系统时间
			out.print(WebUtils.webResponsePage(list,model));
		} catch (Exception e) { 
			e.printStackTrace();
			ExceptionLogger.writeLog("法律法规查询异常信息："+e+e.getClass());
			out.print(WebUtils.responseError("查询失败",-1));
		}
		
	}
 
	
	/**
	 * 用户对文章的点赞与取消；
	 * PS ：当用户没有点赞这篇文章时，就可以点赞文章， 
	 *          当用户已经点赞这篇文章时，就取消原来的点赞。
	 */
	@RequestMapping("saveArticleLike/{userId}/{articleId}")
	public void saveArticleLike(
			@PathVariable("userId") int userId,
			@PathVariable("articleId") int articleId, 
			HttpServletRequest request,
			PrintWriter out) {
		 Map parM = super.getMap(request);
		 //查询这个用户是否已经点赞过；
		try {
			List<Map> listM = this.articleService.listArticleLike(parM);
			boolean ok = true;
			if(null == listM ||  listM.size() == 0 ) {
				 //这个用户没有对这篇文章进行点赞，就保存点赞数据；
				 this.articleService.saveArticleLike(parM);
				 parM.put("likeNum", 1);
				 this.articleService.updateLikeNum(parM);
				 ok= true;
				 parM.put("ok",ok);
				 out.print(WebUtils.responseData("点赞成功.", 1,parM));
			}else {
				this.articleService.deleteArticleLike((int)listM.get(0).get("articleLikeId"));
				 parM.put("likeNum", -1);
				 this.articleService.updateLikeNum(parM);
				 ok= false;
				 parM.put("ok",ok);
				 out.print(WebUtils.responseData("取消点赞.", 1,parM));
			}
			//修改这个文章的 点赞数量；
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("点赞失败.", -1);
		}
		 
	}
	
	
	
	/**
	 * 保存用户对文章的收藏
	 * PS ：  当用户没有收藏这篇文章时，就可以收藏文章， 
	 *          当用户已经收藏这篇文章时，就取消原来的收藏。
	 */
	@RequestMapping("saveArticleCollection/{userId}/{articleId}")
	public void saveArticleCollection(
			@PathVariable("userId") int userId,
			@PathVariable("articleId") int articleId, 
			HttpServletRequest request,
			PrintWriter out) {  
		
		   String nickName = request.getParameter("nickName");
		 //  if(null == nickName || nickName.equals("")) {
		//	   throw new BusinessException("用户昵称不能为空.", -1);
		  // }
		
		try {
			//查询这篇文章，这个用户有没有收藏；
			Map<String,Object> m = new HashMap<String ,Object>();
			m.put("userId", userId);
			m.put("articleId", articleId);
			m.put("userName", nickName);
			List<Map> listMap =   this.articleService.listArticleCollection(m);
			boolean ok=true;
			if(null == listMap || listMap.size() == 0 ) {
				//如果这一登陆用户没有收藏这篇文章，就保存
				this.articleService.saveArticleCollection(m);
				m.put("collectionNum", 1);
				this.articleService.updateCollectionNum(m);
				ok = true;
				m.put("ok",ok);
				out.print(WebUtils.responseData("收藏成功.", 1,m));
				
			}else {
				//如果这一登陆用户已经收藏这篇文章，就删除 原来的收藏记录；
				this.articleService.deleteArticleCollection((int)listMap.get(0).get("collectionId"));
				m.put("collectionNum", -1);
				this.articleService.updateCollectionNum(m);
				ok = false;
				m.put("ok",ok);
				out.print(WebUtils.responseData("取消收藏.", 1,m));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("收藏失败.", -1);
		}
		
	}

	
	/**
	 * 文章详情
	 * @param userId 用户id
	 * @param articleId 文章id
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("articleDetails/{userId}/{articleId}")
	public void findArticleDetails(
			@PathVariable("userId") int userId,
			@PathVariable("articleId") int articleId,
			HttpServletRequest request,
			PrintWriter out
			) throws Exception{
		 
		   Map map = super.getMap(request);
		   
		   String columnCode =  (String)map.get("columnCode");
		   if(null == columnCode ||  columnCode.equals("")) { 
			   throw new BusinessException("所属栏目不能为空.", -1);
		   } 
		   out.print(WebUtils.responseData("加载成功", 1, this.articleService.articleDetails(map)));
		
		 /**
		//获取文章信息
		Map m = articleService.findArticleById(articleId);
		
		if(m==null){
			throw new BusinessException("文章缺失.", -1);
		}
		
		//是否当前用户已经点赞
		int isCollection = 0;
		
		if(userId != 0){
			isCollection = articleService.isCollection(userId, articleId);
		}
		//  0  未收藏   1 收藏
		m.put("isCollection", isCollection);
		
		//判断当前用户是否已点赞
		int isLike = 0;
		
		if(userId!=0){
			isLike = articleService.isLike(userId, articleId);
		}
		//  0  未点赞   1 已点赞
		m.put("isLike", isLike);
		//添加浏览次数
		//加载文章内容
		// String articleContent = getArticleContent(m.get("txtPath").toString());
		//m.put("articleContent", articleContent); 
		
		Map pre = new HashMap();
		pre.put("articleId", 1);
		pre.put("title", "上一篇文章");
		m.put("pre", pre);
		
		Map next = new HashMap();
		next.put("articleId", 3);
		next.put("title", "下一篇文章");
		
		m.put("next", next);
		 //加载上一篇 下一篇
		//Map map = new HashMap();
	//	map.put("columnCode",m.get("columnCode")); 
		//添加浏览次数
		
		
		out.print(WebUtils.responseData(m));
		**/
		
	}
	
	
	
	/**
	 * 咨询师发布个人相关文章
	 * @throws Exception
	 */
	@RequestMapping("/user/sendArticle")
	public void sendAricle(
				HttpServletRequest request,
				PrintWriter out
			) throws Exception{
		
		Map map = new HashMap();
		
		String userId = request.getParameter("userId");
		
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户id不能为空.", -1);
		}
		
		map.put("userId", Integer.parseInt(userId));
		
		String realName = request.getParameter("realName");//作者名称
		
		if(realName==null||realName.equals("")){
			throw new BusinessException("作者昵称不能为空.",-1);
		}
		
		map.put("realName",realName);
		//获取文章栏目值
		String columnCode = request.getParameter("columnCode");
		
		if(columnCode == null || columnCode.equals("")){
			throw new BusinessException("文章不能为空.", -1);
		} 
		map.put("columnCode", columnCode);
		
		String title = request.getParameter("title");
		if(title == null || title.equals("")){
			throw new BusinessException("标题不能为空.", -1);
		} 
		map.put("title", title); 
		
		//工作动态 类型的文章，封面图不需要
		if(!columnCode.equals(SendArticleType.SENDARTICLETYPE_11)) {
			String coverImg = request.getParameter("coverImg");
			if(coverImg == null || coverImg.equals("")){
				throw new BusinessException("封面图不能为空.", -1);
			} 
			map.put("coverImg", coverImg);
		}
		
		//
		if(!columnCode.equals(SendArticleType.SENDARTICLETYPE_10)) {
			String articleContent = request.getParameter("articleContent");
			if(articleContent == null || articleContent.equals("")){
				throw new BusinessException("内容不能为空.", -1);
			} 
			map.put("articleContent", articleContent);
		}
		String isofficial = request.getParameter("isofficial");
		if(null == isofficial || isofficial.equals("")) {
			isofficial ="0";
		}
		
		map.put("isofficial", isofficial);//是否是官方
		map.put("isOutJoin", 0);// 是否是外链
		map.put("lookNum", 0);//   浏览次数
		map.put("articleState", 0);//文章状态
		map.put("isPush", SendArticleType.getIspush(columnCode, isofficial));//是否发布
		map.put("isDelete", 0);//是否删除
		map.put("likeNum", 0);//点赞次数
		map.put("outJoinUrl", "");//外链地址
		//保存
		try { 
			articleService.saveArticle(map);
			out.print(WebUtils.responseData("发布成功.", 1, map));
		} catch (Exception e) {
			e.printStackTrace();
			out.print(WebUtils.responseError("发布失败.", -1));
		}
		
	}
	

	
	
	/**
	 * 获取用户收藏的文章
	 * @throws Exception
	 */
	@RequestMapping("/collectionArticle/{userId}")
	public void findCollectionArticle(
			@PathVariable("userId") int userId,
			PrintWriter out
			) throws Exception{
		
		List<Map> list = articleService.findCollectionArticle(userId);
		out.print(WebUtils.responseData(list));
	}
	
	
	//发布法律法规
	@RequestMapping("/sendLegal")
	public void sendLegal(
				HttpServletRequest request,
				PrintWriter out
			) throws Exception{
		
		Map m = new HashMap();
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		m.put("userId", Integer.parseInt(userId));
		
		String isofficial = request.getParameter("isofficial");
		if(null == isofficial || isofficial.equals("")) {
			throw new BusinessException("发布的类型不能为空。.", -1);
		}
		
		m.put("isofficial", isofficial);
		m.put("isPush",SendArticleType.getIspush(SendArticleType.SENDARTICLETYPE_8, isofficial));
		 
		
		
		String realName = request.getParameter("realName");
	/*	if(realName == null || realName.equals("")){
			throw new BusinessException("用户名称不能为空.", -1);
		}*/
		m.put("realName", realName);
		
		String title = request.getParameter("title");
		if(title == null || title.equals("")){
			throw new BusinessException("用户标题不能为空.", -1);
		}
		m.put("title", title);
		
		String effectLevel = request.getParameter("effectLevel");
		if(effectLevel == null || effectLevel.equals("")){
			throw new BusinessException("效力等级不能为空.", -1);
		}
		m.put("effectLevel", Integer.parseInt(effectLevel));
		String referenceNo = request.getParameter("referenceNo");
/*		if(referenceNo == null || referenceNo.equals("")){
			throw new BusinessException("文号不能为空.", -1);
		}*/
		m.put("referenceNo", referenceNo);
		
		String sendUnit = request.getParameter("sendUnit");
		if(sendUnit == null || sendUnit.equals("")){
			throw new BusinessException("发文单位不能为空.", -1);
		}
		m.put("sendUnit", Integer.parseInt(sendUnit));
		
		String issueTime = request.getParameter("issueTime");
		if(issueTime == null || issueTime.equals("")){
			throw new BusinessException("颁发日期不能为空.", -1);
		}
		m.put("issueTime", issueTime);
		
		String implementTime = request.getParameter("implementTime");
		if(implementTime == null || implementTime.equals("")){
			throw new BusinessException("实施日期不能为空.", -1);
		}
		m.put("implementTime", implementTime);
		
		String isOutJoin = request.getParameter("isOutJoin");
		if(isOutJoin == null || isOutJoin.equals("")){
			throw new BusinessException("是否为外链标示不能为空.", -1);
		}
		m.put("isOutJoin", Integer.parseInt(isOutJoin));
		
		if(Integer.parseInt(isOutJoin) == 1){
			String outJoinUrl = request.getParameter("outJoinUrl");
			if(outJoinUrl==null||outJoinUrl.equals("")){
				throw new BusinessException("外链不能为空.", -1);
			}
			
			m.put("outJoinUrl", outJoinUrl);
			m.put("articleContent", "<a href=\""+outJoinUrl+"\" target=\"_blank\">"+title+"</a>");
		}else{
			String articleContent = request.getParameter("articleContent");
			if(articleContent==null||articleContent.equals("")){
				throw new BusinessException("内容不能为空.", -1);
			}
			
			m.put("articleContent", articleContent);
		}
		
		
		m.put("likeNum", 0);
		m.put("collectionNum", 0);
		m.put("articleState", 0);
		m.put("isDelete", 0);
		m.put("columnCode", SendArticleType.SENDARTICLETYPE_8);
		int articleId = articleService.saveArticle(m);
		articleService.sendLegal(m);
		
		Map map = new HashMap<String, String>();
		map.put("articleId", articleId);
		out.print(WebUtils.responseData(map));
		
	}
	
	/**
	 * 发布法律培训
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("sendLegalTrain")
	public void sendLegalTrain(HttpServletRequest request,PrintWriter out) throws Exception{
		
		Map m = new HashMap();
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		m.put("userId", Integer.parseInt(userId));
		
		String isofficial = request.getParameter("isofficial");
		if(null == isofficial || isofficial.equals("")) {
			throw new BusinessException("发布的类型不能为空。.", -1);
		}
		
		m.put("isofficial", isofficial);
		m.put("isPush",SendArticleType.getIspush(SendArticleType.SENDARTICLETYPE_7, isofficial));
		 
		
		
		String realName = request.getParameter("realName");
		if(realName == null || realName.equals("")){
			throw new BusinessException("用户名称不能为空.", -1);
		}
		m.put("realName", realName);
		
		String title = request.getParameter("title");
		if(title == null || title.equals("")){
			throw new BusinessException("用户标题不能为空.", -1);
		}
		m.put("title", title);
		
		String trainName = request.getParameter("trainName");
		if(trainName == null || trainName.equals("")){
			throw new BusinessException("机构名称不能为空.", -1);
		}
		m.put("trainName", trainName);
		
		String province = request.getParameter("province");
		if(province == null || province.equals("")){
			throw new BusinessException("省份不能为空.", -1);
		}
		m.put("province", Integer.parseInt(province));
		
		String city = request.getParameter("city");
		if(city == null || city.equals("")){
			throw new BusinessException("城市不能为空.", -1);
		}
		m.put("city", Integer.parseInt(city));
		
		String trainAddress = request.getParameter("trainAddress");
		if(trainAddress == null || trainAddress.equals("")){
			throw new BusinessException("培训详细地址不能为空.", -1);
		}
		m.put("trainAddress", trainAddress);
		
		String cost = request.getParameter("cost");
		if(cost == null || cost.equals("")){
			throw new BusinessException("费用不能为空.", -1);
		}
		m.put("cost", cost);
		
		String size = request.getParameter("size");
		/**
		if(size == null || size.equals("")){
			throw new BusinessException("规模不能为空.", -1);
		}**/
		m.put("size", size);
		
		String mobile = request.getParameter("mobile");
		if(mobile == null || mobile.equals("")){
			throw new BusinessException("联系号码不能为空.", -1);
		}
		m.put("mobile", mobile);
		
		String email = request.getParameter("email");
		m.put("email", email);
		
		String trainTime = request.getParameter("trainTime");
		if(trainTime != null && !trainTime.equals("")){
			m.put("trainTime", trainTime);
		}
		
		String trainDesc = request.getParameter("trainDesc");
		if(trainDesc == null || trainDesc.equals("")){
			throw new BusinessException("培训说明不能为空.", -1);
		}
		m.put("trainDesc", trainDesc);
		m.put("isOutJoin", 0); 
		m.put("likeNum", 0);
		m.put("collectionNum", 0);
		m.put("articleState", 0);
		m.put("isDelete", 0);
		m.put("columnCode", SendArticleType.SENDARTICLETYPE_7);
	    articleService.saveArticle(m);
		m.put("isComplain", "正常");
		articleService.sendLegalTrain(m);
		Map map = new HashMap<String, String>(); 
		out.print(WebUtils.responseData("操作成功.",1,map));
	}
	
	
	/**
	 * 发布法律常见咨询问题
	 * @throws Exception
	 */
	@RequestMapping("sendAdviceQuestion")
	public void sendAdviceQuestion(HttpServletRequest request,PrintWriter out) throws Exception{
		Map m = new HashMap();
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		m.put("userId", Integer.parseInt(userId));
		
		String isofficial = request.getParameter("isofficial");
		if(null == isofficial || isofficial.equals("")) {
			throw new BusinessException("发布的类型不能为空。.", -1);
		}
		
		m.put("isofficial", isofficial);
		m.put("isPush",0);
		
		String realName = request.getParameter("realName");
		if(realName == null || realName.equals("")){
			throw new BusinessException("用户名称不能为空.", -1);
		}
		m.put("realName", realName);
		
		String title = request.getParameter("title");
		if(title == null || title.equals("")){
			throw new BusinessException("用户标题不能为空.", -1);
		}
		m.put("title", title);
		
		String isOutJoin = request.getParameter("isOutJoin");
		if(isOutJoin == null || isOutJoin.equals("")){
			throw new BusinessException("是否为外链标示不能为空.", -1);
		}
		m.put("isOutJoin", Integer.parseInt(isOutJoin));
		
		if(Integer.parseInt(isOutJoin) == 1){
			String outJoinUrl = request.getParameter("outJoinUrl");
			if(outJoinUrl==null||outJoinUrl.equals("")){
				throw new BusinessException("外链不能为空.", -1);
			}
			
			m.put("outJoinUrl", outJoinUrl);
			m.put("articleContent", "<a href=\""+outJoinUrl+"\" target=\"_blank\">"+title+"</a>");
		}else{
			String articleContent = request.getParameter("articleContent");
			if(articleContent==null||articleContent.equals("")){
				throw new BusinessException("内容不能为空.", -1);
			}
			
			m.put("articleContent", articleContent);
		}
		
		m.put("likeNum", 0);
		m.put("collectionNum", 0);
		m.put("articleState", 0);
		m.put("isDelete", 0);
		m.put("columnCode", SendArticleType.SENDARTICLETYPE_6);
		
		articleService.saveArticle(m);
		Map map = new HashMap<String, String>(); 
		out.print(WebUtils.responseData(map));
		
	}
	
	
	/**
	 * 发布法律疑难问题与观点
	 * @throws Exception
	 */
	@RequestMapping("sendQuestionAndViewpoint")
	public void sendQuestionAndViewpoint(HttpServletRequest request,PrintWriter out) throws Exception{
		Map m = new HashMap();
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		m.put("userId", Integer.parseInt(userId));
		
		
		String isofficial = request.getParameter("isofficial");
		if(null == isofficial || isofficial.equals("")) {
			throw new BusinessException("发布的类型不能为空。.", -1);
		}
		
		m.put("isofficial", isofficial);
		m.put("isPush",0);
		
		
		String realName = request.getParameter("realName");
		if(realName == null || realName.equals("")){
			throw new BusinessException("用户名称不能为空.", -1);
		}
		m.put("realName", realName);
		
		String title = request.getParameter("title");
		if(title == null || title.equals("")){
			throw new BusinessException("用户标题不能为空.", -1);
		}
		m.put("title", title);
		
		String isOutJoin = request.getParameter("isOutJoin");
		if(isOutJoin == null || isOutJoin.equals("")){
			throw new BusinessException("是否为外链标示不能为空.", -1);
		}
		m.put("isOutJoin", Integer.parseInt(isOutJoin));
		
		if(Integer.parseInt(isOutJoin) == 1){
			String outJoinUrl = request.getParameter("outJoinUrl");
			if(outJoinUrl==null||outJoinUrl.equals("")){
				throw new BusinessException("外链不能为空.", -1);
			}
			
			m.put("outJoinUrl", outJoinUrl);
			m.put("articleContent", "<a href=\""+outJoinUrl+"\" target=\"_blank\">"+title+"</a>");
		}else{
			String articleContent = request.getParameter("articleContent");
			if(articleContent==null||articleContent.equals("")){
				throw new BusinessException("内容不能为空.", -1);
			}
			
			m.put("articleContent", articleContent);
		}
		
		m.put("likeNum", 0);
		m.put("collectionNum", 0);
		m.put("articleState", 0);
		m.put("isDelete", 0);
		m.put("columnCode", SendArticleType.SENDARTICLETYPE_1);
		
		int articleId = articleService.saveArticle(m);
		Map map = new HashMap<String, String>();
		map.put("articleId", articleId);
		out.print(WebUtils.responseData(map));
		
	}
	
	/**
	 * 发布法律人信息
	 * @throws Exception
	 */
	@RequestMapping("sendLegalPersonInfo")
	public void sendLegalPersonInfo(HttpServletRequest request,PrintWriter out) throws Exception{
		Map m = new HashMap();
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		m.put("userId", Integer.parseInt(userId));
		
		String isofficial = request.getParameter("isofficial");
		if(null == isofficial || isofficial.equals("")) {
			throw new BusinessException("发布的类型不能为空。.", -1);
		} 
		m.put("isofficial", isofficial);
		m.put("isPush",0);
		
		String realName = request.getParameter("realName");
		if(realName == null || realName.equals("")){
			throw new BusinessException("用户名称不能为空.", -1);
		}
		m.put("realName", realName);
		
		String title = request.getParameter("title");
		if(title == null || title.equals("")){
			throw new BusinessException("用户标题不能为空.", -1);
		}
		m.put("title", title);
		
		String isOutJoin = request.getParameter("isOutJoin");
		if(isOutJoin == null || isOutJoin.equals("")){
			throw new BusinessException("是否为外链标示不能为空.", -1);
		}
		m.put("isOutJoin", Integer.parseInt(isOutJoin));
		
		if(Integer.parseInt(isOutJoin) == 1){
			String outJoinUrl = request.getParameter("outJoinUrl");
			if(outJoinUrl==null||outJoinUrl.equals("")){
				throw new BusinessException("外链不能为空.", -1);
			}
			
			m.put("outJoinUrl", outJoinUrl);
			m.put("articleContent", "<a href=\""+outJoinUrl+"\" target=\"_blank\">"+title+"</a>");
		}else{
			String articleContent = request.getParameter("articleContent");
			if(articleContent==null||articleContent.equals("")){
				throw new BusinessException("内容不能为空.", -1);
			}
			
			m.put("articleContent", articleContent);
		}
		
		m.put("likeNum", 0);
		m.put("collectionNum", 0);
		m.put("articleState", 0);
		m.put("isDelete", 0);
		m.put("columnCode", SendArticleType.SENDARTICLETYPE_5);
		 
		articleService.saveArticle(m);
		out.print(WebUtils.responseData("发布成功.", 1, m)); 
	
	}
	
	
	/**
	 * 发布文书制作知识
	 * @throws Exception
	 */
	@RequestMapping("sendBookMakeKnowledge")
	public void sendBookMakeKnowledge(HttpServletRequest request,PrintWriter out) throws Exception{
		Map m = new HashMap();
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		m.put("userId", Integer.parseInt(userId));
		
		
		String isofficial = request.getParameter("isofficial");
		if(null == isofficial || isofficial.equals("")) {
			throw new BusinessException("发布的类型不能为空。.", -1);
		}
		
		m.put("isofficial", isofficial);
		m.put("isPush",0);
		
		
		String realName = request.getParameter("realName");
		if(realName == null || realName.equals("")){
			throw new BusinessException("用户名称不能为空.", -1);
		}
		m.put("realName", realName);
		
		String title = request.getParameter("title");
		if(title == null || title.equals("")){
			throw new BusinessException("用户标题不能为空.", -1);
		}
		m.put("title", title);
		
		String isOutJoin = request.getParameter("isOutJoin");
		if(isOutJoin == null || isOutJoin.equals("")){
			throw new BusinessException("是否为外链标示不能为空.", -1);
		}
		m.put("isOutJoin", Integer.parseInt(isOutJoin));
		
		if(Integer.parseInt(isOutJoin) == 1){
			String outJoinUrl = request.getParameter("outJoinUrl");
			if(outJoinUrl==null||outJoinUrl.equals("")){
				throw new BusinessException("外链不能为空.", -1);
			}
			
			m.put("outJoinUrl", outJoinUrl);
			m.put("articleContent", "<a href=\""+outJoinUrl+"\" target=\"_blank\">"+title+"</a>");
		}else{
			String articleContent = request.getParameter("articleContent");
			if(articleContent==null||articleContent.equals("")){
				throw new BusinessException("内容不能为空.", -1);
			}
			
			m.put("articleContent", articleContent);
		}
		
		m.put("likeNum", 0);
		m.put("collectionNum", 0);
		m.put("articleState", 0);
		m.put("isDelete", 0);
		m.put("columnCode", SendArticleType.SENDARTICLETYPE_4);
		 try {
				articleService.saveArticle(m);
				out.print(WebUtils.responseData("发布成功.", 1, m));
			} catch (Exception e) {
				e.printStackTrace();
				out.print(WebUtils.responseMsg("发布成功.",-1));
			}

	}
	
	
	/**
	 * 发布常用文书模板
	 * @throws Exception
	 */
	@RequestMapping("sendBookMakeModel")
	public void sendBookMakeModel(HttpServletRequest request,PrintWriter out) throws Exception{
		Map m = new HashMap();
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		m.put("userId", Integer.parseInt(userId));
		
		
		String isofficial = request.getParameter("isofficial");
		if(null == isofficial || isofficial.equals("")) {
			throw new BusinessException("发布的类型不能为空。.", -1);
		}
		
		m.put("isofficial", isofficial);
		m.put("isPush",0);
		
		
		String realName = request.getParameter("realName");
		if(realName == null || realName.equals("")){
			throw new BusinessException("用户名称不能为空.", -1);
		}
		m.put("realName", realName);
		
		String title = request.getParameter("title");
		if(title == null || title.equals("")){
			throw new BusinessException("用户标题不能为空.", -1);
		}
		m.put("title", title);
		
		String isOutJoin = request.getParameter("isOutJoin");
		if(isOutJoin == null || isOutJoin.equals("")){
			throw new BusinessException("是否为外链标示不能为空.", -1);
		}
		m.put("isOutJoin", Integer.parseInt(isOutJoin));
		
		if(Integer.parseInt(isOutJoin) == 1){
			String outJoinUrl = request.getParameter("outJoinUrl");
			if(outJoinUrl==null||outJoinUrl.equals("")){
				throw new BusinessException("外链不能为空.", -1);
			}
			
			m.put("outJoinUrl", outJoinUrl);
			m.put("articleContent", "<a href=\""+outJoinUrl+"\" target=\"_blank\">"+title+"</a>");
		}else{
			String articleContent = request.getParameter("articleContent");
			if(articleContent==null||articleContent.equals("")){
				throw new BusinessException("内容不能为空.", -1);
			}
			
			m.put("articleContent", articleContent);
		}
		
		m.put("likeNum", 0);
		m.put("collectionNum", 0);
		m.put("articleState", 0);
		m.put("isDelete", 0);
		m.put("columnCode", SendArticleType.SENDARTICLETYPE_3);
		 
		articleService.saveArticle(m);
		out.print(WebUtils.responseData("发布成功.", 1, m));
		 
	}
	
	
	/**
	 * 发布公务员招聘
	 * @throws Exception
	 */
	@RequestMapping("sendCivilWorkerRecruit")
	public void sendCivilWorkerRecruit(HttpServletRequest request,PrintWriter out) throws Exception{
		Map m = new HashMap();
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		m.put("userId", Integer.parseInt(userId));
		
		
		String isofficial = request.getParameter("isofficial");
		if(null == isofficial || isofficial.equals("")) {
			throw new BusinessException("发布的类型不能为空。.", -1);
		}
		
		m.put("isofficial", isofficial);
		m.put("isPush",0);
		
		
		String realName = request.getParameter("realName");
		if(realName == null || realName.equals("")){
			throw new BusinessException("用户名称不能为空.", -1);
		}
		m.put("realName", realName);
		
		String title = request.getParameter("title");
		if(title == null || title.equals("")){
			throw new BusinessException("用户标题不能为空.", -1);
		}
		m.put("title", title);
		
		String isOutJoin = request.getParameter("isOutJoin");
		if(isOutJoin == null || isOutJoin.equals("")){
			throw new BusinessException("是否为外链标示不能为空.", -1);
		}
		m.put("isOutJoin", Integer.parseInt(isOutJoin));
		
		if(Integer.parseInt(isOutJoin) == 1){
			String outJoinUrl = request.getParameter("outJoinUrl");
			if(outJoinUrl==null||outJoinUrl.equals("")){
				throw new BusinessException("外链不能为空.", -1);
			}
			m.put("outJoinUrl", outJoinUrl);
			m.put("articleContent", "<a href=\""+outJoinUrl+"\" target=\"_blank\">"+title+"</a>");
		}else{
			String articleContent = request.getParameter("articleContent");
			if(articleContent==null||articleContent.equals("")){
				throw new BusinessException("内容不能为空.", -1);
			}
			
			m.put("articleContent", articleContent);
		}
		
		m.put("likeNum", 0);
		m.put("collectionNum", 0);
		m.put("articleState", 0);
		m.put("isDelete", 0);
		m.put("columnCode", SendArticleType.SENDARTICLETYPE_2);
		 
		articleService.saveArticle(m);
		out.print(WebUtils.responseData("发布成功.", 1, m)); 
	}
	
	/**
	 * 设置 和取消今天推荐
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("updateRecommend")
	public void updateRecommend(
			HttpServletRequest request,PrintWriter out
			)  throws Exception{
		Map map = getMap(request);
		Object articleState =  map.get("articleState");
		if(null == articleState || articleState.equals("")) {
			throw new BusinessException("推荐状态不能为空.", -1);
		}
		Object  articleId = map.get("articleId");
		if(null == articleId || articleId.equals("")) {
			throw new BusinessException("id不能为空.", -1);
		}
		this.articleService.updateRecommend(map);
		out.print(WebUtils.responseData("操作成功",1, map));
	}
	
	/**
	 * 修改文章接口(阿里裁决除外)
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("updateArticle")
	public void updateArticle(
			HttpServletRequest request,PrintWriter out) throws Exception{
		    Map map = getMap(request);
		    
		    Object articleId = map.get("articleId");
		    if(articleId == null || articleId.equals("")) {
		    	throw new BusinessException("ID不能为空.", -1);
		    }
		    
		    Object columnCode = map.get("columnCode");
		    if(null == columnCode || columnCode.equals("")) {
		    	throw new BusinessException("类型不能为空.", -1);
		    }
		    
			String [] imgsArray = request.getParameterValues("imgs"); 
			String imgs = "";
			if(null != imgsArray && imgsArray.length > 0 ) {
				for(int i = 0 ; i <imgsArray.length ; i ++) {
					if(i > 0 ) {
						imgs+=",";
					}
					imgs+=imgsArray[i];
				}
			}
			map.put("articleId",Integer.parseInt(articleId.toString()));
			Object province = map.get("province");
			if(null != province && province.equals("")) {
				 map.put("province",Integer.valueOf(province.toString()));
			}
			Object city = map.get("city");
			if(null != city && city.equals("")) {
				 map.put("city",Integer.valueOf(city.toString()));
			}
			
			
			map.put("imgs", imgs); 
			if(columnCode.toString().equals(SendArticleType.SENDARTICLETYPE_8.toString())) {
				Object legislationId = map.get("legislationId");
				if(null == legislationId || legislationId.equals("")) {
					throw new BusinessException("属性类型不能为空.", -1);
				}
				
				//法律法规保存扩展表；
				this.articleService.updateLegal(map);
			}
			
			if(columnCode.toString().equals(SendArticleType.SENDARTICLETYPE_7.toString())) {
				Object trainId  = map.get("trainId");
				if(null == trainId || trainId.equals("")) {
					throw new BusinessException("属性类型不能为空.", -1);
				} 
				map.put("trainId",Integer.valueOf(trainId.toString()));
				
				System.out.println(map);
				this.articleService.updateTrain(map);
			}
			this.articleService.updateArticle(map);
			out.print(WebUtils.responseData("操作成功！",1,map));
	}
	
	
	
	
	/**
	 * 举报培训
	 * @throws Exception
	 */
	@RequestMapping("complainTrain")
	public void complainTrain(HttpServletRequest request,PrintWriter out) throws Exception{
	      String trainId = request.getParameter("trainId");
	      String reportTitle = request.getParameter("reportTitle");
      	if(StringUtil.isBlank(String.valueOf(trainId))){
			throw new BusinessException("培训Id不能缺少.",-1);
		}
        String userId = request.getParameter("userId");
        String userName = request.getParameter("userName");
        String mobile = request.getParameter("mobile");
	
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
	    m.put("reportBusinessId",trainId); 
	    m.put("userId",userId); 
	    m.put("userName",userName); 
	    m.put("mobile",mobile); 
	    m.put("reportType",1); 
	    m.put("reportTitle",reportTitle); 
	    m.put("reportTime",new Date()); 
		userService.insertReport(m);
		Map map = getMap(request);
		map.put("isComplain", "被举报");
		this.articleService.updateTrain(map);
		Map<String, Object> m1 = new HashMap<String, Object>();
		m1.put("isRead","0"); //0:没读 1：已读
		m1.put("sendTime",new Date()); //0:没读 1：已读
		m1.put("isDelete","0"); //0:未删除 1：已删除
		m1.put("messageState","0"); // 
		m1.put("is_send","1"); //0:未发送1：已发送
		m1.put("create_time",new Date()); 	
		m1.put("userId",""); 
		m1.put("messageContent","管理员您好，该培训已用户举报，请核实该培训内容是否进行删除。");
		m1.put("messageType","培训举报");
		m1.put("businessType","2");
		m1.put("businessId",trainId);
	    userService.messageSend(m1);
		out.print(WebUtils.responseMsg("举报成功"));
 	}
	
	
	/**
	 * 发布百姓吐槽
	 * 军山依旧
	 * @throws Exception
	 */
	@RequestMapping("sendCommonPeople")
	public void sendCommonPeople(HttpServletRequest request,PrintWriter out) throws Exception{
		Map m = new HashMap();
//		Map maps = getMap(request);
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		m.put("userId", Integer.parseInt(userId));
		
		String isofficial_tmp = request.getParameter("isofficial");
		if(isofficial_tmp == null || isofficial_tmp.equals("")) {
			throw new BusinessException("发文类型不能为空.", -1);
		}
		if(!(isofficial_tmp.equals("0") || isofficial_tmp.equals("1"))) {
			throw new BusinessException("发文类型不能匹配.", -1);
		}
		 
		int isofficial = Integer.parseInt(isofficial_tmp);
		m.put("isofficial", isofficial); //0: 官方 1：用户
		m.put("isPush",0); //  0 : 【普通用户 待审核 || 官方 待发布】   1：已发布  2：未通过

		
		String realName = request.getParameter("realName");
		if(realName == null || realName.equals("")){
			throw new BusinessException("用户名称不能为空.", -1);
		}
		m.put("realName", realName);
		
		String title = request.getParameter("title");
		if(title == null || title.equals("")){
			throw new BusinessException("用户标题不能为空.", -1);
		}
		m.put("title", title);
		
		Map userM = null;
		//根据UserId 查询是 用户还是管理员；
		if(isofficial == 0 ) {
			//查询官 Map
			userM = this.userService.getSysUserInfo(userId);
		}else {
			//查询普通用户是否存在；
			userM = this.userService.getUserInfo(userId); 
		}
		if(null == userM) {
			throw new BusinessException("用户Id与用户类型不匹配.", -1);
		}
		
		/**
		String isOutJoin = request.getParameter("isOutJoin");
		if(isOutJoin == null || isOutJoin.equals("")){
			throw new BusinessException("是否为外链标示不能为空.", -1);
		}
		m.put("isOutJoin", Integer.parseInt(isOutJoin));
		
		if(Integer.parseInt(isOutJoin) == 1){
			String outJoinUrl = request.getParameter("outJoinUrl");
			if(outJoinUrl==null||outJoinUrl.equals("")){
				throw new BusinessException("外链不能为空.", -1);
			}
			m.put("outJoinUrl", outJoinUrl);
			m.put("articleContent", "<a href=\""+outJoinUrl+"\" target=\"_blank\">"+title+"</a>");
		}else{
		
		}
		**/
		
		
		m.put("isOutJoin", 0);
		m.put("outJoinUrl", "");  
		String articleContent = request.getParameter("articleContent");
		if(articleContent==null||articleContent.equals("")){
			throw new BusinessException("内容不能为空.", -1);
		}
		
		m.put("articleContent", articleContent);
		
		String [] imgsArray = request.getParameterValues("imgs"); 
		String imgs = "";
		if(null != imgsArray && imgsArray.length > 0 ) {
			for(int i = 0 ; i <imgsArray.length ; i ++) {
				if(i > 0 ) {
					imgs+=",";
				}
				imgs+=imgsArray[i];
			}
		}
		m.put("imgs", imgs);
		
		m.put("likeNum", 0);
		m.put("collectionNum", 0);
		m.put("articleState", 0);//0 :  基本状态 默认 1： 是今日推荐
		m.put("isDelete", 0); // 0 ：未删除 1： 已 删除
		m.put("columnCode", SendArticleType.SENDARTICLETYPE_0);
		articleService.saveArticle(m);
		try {
			out.print(WebUtils.responseData("发布成功！",1,m));
		} catch (Exception e) {
			out.print(WebUtils.responseMsg("发布失败！",-1));
		}
	}

	
	/**
	 * 阿里裁决置顶功能；
	 * stickType  0，是把这一行数据 置顶， 其它是把这一行置顶数据取消；
	 * @param request
	 * @param out
	 */
	@RequestMapping("stickAliDeclare")
	public  void  stickAliDeclare(HttpServletRequest request,PrintWriter out) throws Exception {
		Map m = getMap(request);
		Object  stickType = m.get("stickType");
		if(null == stickType || stickType.equals("")) {
			throw new BusinessException("置顶类型不能为空.", -1);
		}
		Object  articleId = m.get("articleId");
		if(null == articleId || articleId.equals("") ) {
			throw new BusinessException("Id不能为空.", -1);
		}
		String msg = "置顶";
		int indexs = 0;
		if(stickType.equals("0")) {
			//把这行数据置顶；
			  indexs = this.articleService.getMaxIndexs();
			  indexs ++; 
		}else {
			msg = "取消"+msg;
		} 
		Map saveMap = new HashMap<String,Object>();
		saveMap.put("articleId", articleId.toString());
		saveMap.put("indexs", indexs+"");
		this.articleService.updateArticle(saveMap);
		out.print(WebUtils.responseData(msg+"成功.", 1, saveMap));
	}
	
	/**
	 * 修改阿里裁决
	 * @param request
	 * @param out
	 */
	@RequestMapping("updateAliDeclare")
	public void updateAliDeclare(HttpServletRequest request,PrintWriter out) throws Exception {
		Map map = getMap(request);
		//判断 文章Id不能为空；
		Object articleId_tmp = map.get("articleId");
		if(null == articleId_tmp || articleId_tmp.equals("")) {
			throw new BusinessException("裁决Id不能为空.", -1);
		}
		//申诉依据不能为空；
		Object declareId_tmp = map.get("declareId");
		if(null == declareId_tmp || declareId_tmp.equals("")) {
			throw new BusinessException("依据Id不能为空.", -1);
		}
		this.articleService.updateArticleAndDeclare(map);
		out.print(WebUtils.responseData("修改成功",1,map));
	}
	
	/**
	 * 发布阿里裁决文章
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("aliDeclare")
	public void sendAliDeclare(HttpServletRequest request,PrintWriter out) throws Exception{
		Map m = new HashMap();
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		m.put("userId", Integer.parseInt(userId));
		
		String isofficial_tmp = request.getParameter("isofficial");
		if(isofficial_tmp == null || isofficial_tmp.equals("")) {
			throw new BusinessException("发文类型不能为空.", -1);
		}
		if(!(isofficial_tmp.equals("0") || isofficial_tmp.equals("1"))) {
			throw new BusinessException("发文类型不能匹配.", -1);
		}
		 
		int isofficial = Integer.parseInt(isofficial_tmp);
		m.put("isofficial", isofficial); //0: 官方 1：用户
		m.put("isPush",0); //  0 : 【普通用户 待审核 || 官方 待发布】   1：已发布  2：未通过
 

		String realName = request.getParameter("realName");
		if(isofficial==1 && (realName == null || realName.equals(""))){
			throw new BusinessException("用户名称不能为空.", -1);
		}
		m.put("realName", realName);
		
		String title = request.getParameter("title");
		if(title == null || title.equals("")){
			throw new BusinessException("用户标题不能为空.", -1);
		}
		m.put("title", title);
		
		String declareType = request.getParameter("declareType");
		if(declareType == null || declareType.equals("")){
			throw new BusinessException("申报类型不能为空.", -1);
		}
		m.put("declareType", Integer.parseInt(declareType));
		
		String reasonInfo = request.getParameter("reasonInfo");
		if(isofficial==1 && (reasonInfo == null || reasonInfo.equals(""))){
			throw new BusinessException("申报理由不能为空.", -1);
		}
		m.put("reasonInfo", reasonInfo);
		
		String caseDesc = request.getParameter("caseDesc");
		if(caseDesc == null || caseDesc.equals("")){
			throw new BusinessException("案件描述不能为空.", -1);
		}
		m.put("caseDesc", caseDesc);
		
		String caseUrl = request.getParameter("caseUrl");
		m.put("caseUrl", caseUrl);
		
		String bookName = request.getParameter("bookName");
		m.put("bookName", bookName);
		
		String primaryImg = request.getParameter("primaryImg");
		if(isofficial==1 && (primaryImg == null || primaryImg.equals(""))){
			throw new BusinessException("主题图片不能为空.", -1);
		}
		m.put("primaryImg", primaryImg);
		
		String [] declareImgs = request.getParameterValues("declareImgs");//裁决书图片
		String declareImgs_ = "";
		if(null != declareImgs && declareImgs.length > 0 ) {
			for(int i = 0 ; i <declareImgs.length ; i ++) {
				if(i > 0 ) {
					declareImgs_+=",";
				}
				declareImgs_+=declareImgs[i];
			}
		} 
		m.put("declareImgs", declareImgs_);
		
		String [] evidenceImgs = request.getParameterValues("evidenceImgs");//证据图片
		String evidenceImgs_ = "";
		if(null != evidenceImgs && evidenceImgs.length > 0 ) {
			for(int i = 0 ; i <evidenceImgs.length ; i ++) {
				if(i > 0 ) {
					evidenceImgs_+=",";
				}
				evidenceImgs_+=evidenceImgs[i];
			}
		} 
		m.put("evidenceImgs", evidenceImgs_);
		
		
		m.put("isOutJoin", 0);
		m.put("likeNum", 0);
		m.put("collectionNum", 0);
		m.put("articleState", 0); // 0 :  基本状态 默认 1： 是今日推荐
		m.put("isDelete", 0);
		m.put("columnCode", SendArticleType.SENDARTICLETYPE_9);
		try {
			articleService.saveArticleAndDeclare(m);
			out.print(WebUtils.responseData("申报成功.", 1, m));
		} catch (Exception e) { 
			e.printStackTrace();
			ExceptionLogger.writeLog(e,e.getClass()+"------------------------------------------");
			throw new BusinessException("申报失败.", -1);
		}
	}
	
	
	/**
	 * 发表百姓吐槽留言
	 * @param request
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping("sendCommonPeopleMessage")
	public void sendCommonPeopleMessage(HttpServletRequest request,PrintWriter out) throws Exception{
		
		Map m = new HashMap();
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		m.put("userId", Integer.parseInt(userId));
		
		String articleId = request.getParameter("articleId");
		if(articleId == null || articleId.equals("")){
			throw new BusinessException("文章编号不能为空.", -1);
		}
		m.put("articleId", Integer.parseInt(articleId));
		
		String messageContent = request.getParameter("messageContent");
		if(messageContent == null || messageContent.equals("")){
			throw new BusinessException("留言内容不能为空.", -1);
		}
		m.put("messageContent", messageContent);
		//添加
		out.print(WebUtils.responseData(m));
	}
	
	
	/**
	 * 发表百姓吐槽留言
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping("sendAliAdjudicationMessage")
	public void sendAliAdjudicationMessage(HttpServletRequest request,PrintWriter out) throws Exception{
		Map m = new HashMap();
		String userId = request.getParameter("userId");
		if(userId == null || userId.equals("")){
			throw new BusinessException("用户编号不能为空.", -1);
		}
		m.put("userId", Integer.parseInt(userId));
		
		String messageContent = request.getParameter("messageContent");
		if(messageContent == null || messageContent.equals("")){
			throw new BusinessException("留言内容不能为空.", -1);
		}
		m.put("messageContent", messageContent);
		//添加
		out.print(WebUtils.responseData(m));
	}
	
	/**
	 * 百姓吐槽列表
	 * @param request
	 * @param out
	 * @param articleId
	 * @throws Exception
	 */
	@RequestMapping("commonPeopleMessageList/{model}/{columnCode}/{pageNo}/{pageSize}")
	public void commonPeopleMessageList(
			HttpServletRequest request,
			PrintWriter out,
			@PathVariable("model") int model,
			@PathVariable("columnCode") int columnCode,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize
			) throws Exception{
		String type = SendArticleType.getType(columnCode);
		if(type.equals("")) {
			throw new BusinessException("查询的类型不能为空.", -1);
		} 
		Map m = new HashMap<String,Map>();
		m.put("columnCode", type);  
		
		//如果没有传入查询类型，那就是查询未删除的
		String isDelete = request.getParameter("isDelete");
		if(null == isDelete || isDelete.equals("")) {
			isDelete = "0";
		}
		m.put("isDelete", isDelete);
		
		
		//如果未传入发布的状态，就传入已发布的  0 : 【普通用户 待审核 || 官方 待发布】   1：已发布  2：未通过
		//如果model ==1 说明前端请求，那只能查询出已经审核通过的数据；
		if(model == 1) {
			m.put("isPush", "1");
		}
		
		Paging paging = new Paging(pageSize, pageNo, true);
		try {
			List<Map> list =  this.articleService.commonPeopleMessageListByTotal(m,paging);
			out.print(WebUtils.webResponsePage(list, model));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("查询失败.", -1);
		}
		 
	}
	
	@RequestMapping("aliAdjudicationMessageList/{articleId}")
	public void aliAdjudicationMessageList(HttpServletRequest request,PrintWriter out,@PathVariable("articleId") int articleId) throws Exception{
		
	}
	
	/**
	 * 获取文章内容
	 * @param txtPath
	 * @return
	 * @throws Exception
	 */
	public String getArticleContent(String txtPath){
		StringBuffer buffer = new StringBuffer();
		
		File file = new File(this.txtPath, txtPath);
		if(file.exists()){
			
			BufferedReader buffReader = null;
			FileReader fileReader = null;
			
			try {
				fileReader = new FileReader(file);
				buffReader = new BufferedReader(fileReader);
				String line = null;
				while((line=buffReader.readLine())!=null){
					buffer.append(line);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(buffReader!=null){
						buffReader.close();
					}
					if(fileReader!=null){
						fileReader.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			
		}
		
		return buffer.toString();
	}
	
	@RequestMapping("counts/{userId}")
	public void listATT_COLL_CONS(
			@PathVariable("userId") int userId,
			PrintWriter out
			) {
		 try {
			Map m = this.articleService.listATT_COLL_CONS(userId);
			out.print(WebUtils.responseData("查询成功.",1, m));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping("deleteArticle/{articleId}")
	public void deleteArticle(
			@PathVariable("articleId") int articleId,
			HttpServletRequest request,
			PrintWriter out
			) {
		 try {
			int  num = this.articleService.deleteArticle(articleId);
			Map map = getMap(request);
			if(num == 0 ) {
				 map.put("ok",false);
				out.print(WebUtils.responseData("删除失败，已经有回复。", 1, map));
			}else {
				map.put("ok",true);
				out.print(WebUtils.responseData("删除成功。", 1,  map));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("删除失败.", -1);
		}
	}
	
	
	/**
	 * 查询我收藏的文章
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param out
	 */
	@RequestMapping("listCollectionJoinArticle/{userId}/{pageNo}/{pageSize}")
	public void listCollectionJoinArticle(
			@PathVariable("userId") int userId,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			HttpServletRequest request,
			PrintWriter out
			) {
		  Paging paging = new Paging(pageSize, pageNo,true);
		 try {
			List list =   this.articleService.listCollectionJoinArticle(getMap(request), paging);
			out.print(WebUtils.webResponsePage(list));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("查询失败.", -1);
		}
		
		
	}
	
}
