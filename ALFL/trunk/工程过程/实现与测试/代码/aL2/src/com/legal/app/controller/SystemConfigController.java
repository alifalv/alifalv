package com.legal.app.controller;

import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.utils.SystemConfigUtil;
import com.common.web.WebUtils;
@RequestMapping("systemConfig")
@Controller
public class SystemConfigController extends SuperController{
	
	
	@RequestMapping("levelList")
	public void getLevelList(PrintWriter out){
		out.print(WebUtils.responseData(SystemConfigUtil.levelList));
	}
	
	@RequestMapping("driverList")
	public void getDriverList(PrintWriter out){
		out.print(WebUtils.responseData(SystemConfigUtil.driverList));
	}
	@RequestMapping("caseList")
	public void getCaseList(PrintWriter out){
		out.print(WebUtils.responseData(SystemConfigUtil.caseList));
	}
	
	@RequestMapping("jobList")
	public void getJobList(PrintWriter out){
		out.print(WebUtils.responseData(SystemConfigUtil.jobList));
	}
	
	@RequestMapping("declareList")
	public void getDeclareList(PrintWriter out){
		out.print(WebUtils.responseData(SystemConfigUtil.declareList));
	}
	
	@RequestMapping("salList")
	public void getSalList(PrintWriter out){
		out.print(WebUtils.responseData(SystemConfigUtil.salList));
	}
	
	@RequestMapping("educationList")
	public void getEducationList(PrintWriter out){
		out.print(WebUtils.responseData(SystemConfigUtil.educationList));
	}
	
	@RequestMapping("experienceList")
	public void getExperienceList(PrintWriter out){
		out.print(WebUtils.responseData(SystemConfigUtil.experienceList));
	}
	
	@RequestMapping("unitList")
	public void getUnitList(PrintWriter out) throws Exception{
		out.print(WebUtils.responseData(SystemConfigUtil.unitList));
	}
	
	@RequestMapping("effectivenessGradeList")
	public void getEffectivenessGradeList(PrintWriter out) throws Exception{
		out.print(WebUtils.responseData(SystemConfigUtil.egList));
	}
	
}
