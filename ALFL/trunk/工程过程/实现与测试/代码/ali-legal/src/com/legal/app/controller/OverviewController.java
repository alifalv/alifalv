package com.legal.app.controller;

import com.common.web.WebUtils;
import com.legal.app.service.*;
import com.legal.app.utils.SystemConfigUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/overview")
@Api(tags = "总览")
public class OverviewController extends SuperController{
    @Autowired
    private IReportService reportService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IAdviceService adviceService;
    @Autowired
    private ICaseDeputeService caseDeputeService;
    @Autowired
    private ICastReplyService castReplyService;
    @Autowired
    private ISuggestService suggestService;
    @Autowired
    private IArticleService articleService;

    @RequestMapping("/index")
    public ModelAndView show() {
        ModelAndView mav = new ModelAndView("overview");
        return mav;
    }

    @ResponseBody
    @ApiOperation(value = "首页统计", responseContainer = "Map")
    @RequestMapping(value = "indexCount", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void indexCount(PrintWriter out) {
        try {
            Map<String, Object> map = new HashMap<>();
        		//会员认证待审核的数量
            int numberOfNotAuthentication = userService.numberOfNotAuthentication();
            map.put("numberOfNotAuthentication", numberOfNotAuthentication);
        		//法律文书制作的数量
            int numbersOfLegalDocument = castReplyService.numbersOfLegalDocumentProduction();
            map.put("numberOfLegalDocument", numbersOfLegalDocument);
        		//交通事故案件的数量
            int numbersOfTrafficCase = castReplyService.numbersOfTrafficCase();
            map.put("numberOfTrafficCase", numbersOfTrafficCase);
        		//意见反馈的数量
            int numberOfSuggest = suggestService.numberOfSuggest();
            map.put("numberOfSuggest", numberOfSuggest);
        		//举报信息的数量
            int numberOfReport = reportService.numberOfReport();
            map.put("numberOfReport", numberOfReport);
        		//百姓吐槽的数量
            int numberOfComplaints = articleService.numberOfComplaints();
            map.put("numberOfComplaints", numberOfComplaints);
        		//阿里裁判的数量
            int numberOfAliReferee = articleService.numberOfAliReferee();
            map.put("numberOfAliReferee", numberOfAliReferee);
        		//法律培训的数量
            int numberOfLawTrain = articleService.numberOfLawTrain();
            map.put("numberOfLawTrain", numberOfLawTrain);
            out.print(WebUtils.responseData(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "总览统计", responseContainer = "Map")
    @RequestMapping(value = "overviewCount", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void overviewCount(PrintWriter out) {
        try {
            Map<String, Object> map = new HashMap<>();
        		//会员人数
            int numberOfMembers = userService.numberOfMembers();
            map.put("numberOfMembers", numberOfMembers);
        		//当前续费VIP
            int currentRenewalFeeVip = userService.currentRenewalFeeVip();
            map.put("currentRenewalFeeVip", currentRenewalFeeVip);
        		//咨询问题
            int numbersOfAdvice = adviceService.numbersOfAdvice();
            map.put("numbersOfAdvice", numbersOfAdvice);
        		//案件委托
            int numbersOfCaseDepute = caseDeputeService.numbersOfCaseDepute();
            map.put("numbersOfCaseDepute", numbersOfCaseDepute);
        		//法律文书制作
            int numbersOfLegalDocumentProduction = castReplyService.numbersOfLegalDocumentProduction();
            map.put("numbersOfLegalDocumentProduction", numbersOfLegalDocumentProduction);
            out.print(WebUtils.responseData(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "会员统计", responseContainer = "Map")
    @RequestMapping(value = "member/statistics", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void memberStatistics(PrintWriter out) {
        try {
            Map<String, Object> map = new HashMap<>();
            Integer numberOfMembers = userService.numberOfMembers();
            map.put("total_num", numberOfMembers);
            Integer numbersOfBusinessConsultant = userService.numbersOfBusinessConsultant();
            map.put("company_num", numbersOfBusinessConsultant);
            Integer numbersOfConsultant = userService.numbersOfConsultant();
            map.put("counselor_num", numbersOfConsultant);
            Integer numbersOfPersonalConsultant = userService.numbersOfPersonalConsultant();
            map.put("personal_num", numbersOfPersonalConsultant);
            out.print(WebUtils.responseData(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "VIP统计", responseContainer = "Map")
    @RequestMapping(value = "vip/statistics", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void VIPStatistics(PrintWriter out) {
        try {
            Map<String, Object> map = new HashMap<>();
            Integer VIPMember = userService.VIPMember();
            map.put("VIPMember", VIPMember);
            Integer goldMember = userService.goldMember();
            map.put("goldMember", goldMember);
            Integer platinumMember = userService.platinumMember();
            map.put("platinumMember", platinumMember);
            Integer diamondMembership = userService.diamondMembership();
            map.put("diamondMembership", diamondMembership);
            out.print(WebUtils.responseData(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "咨询问题统计", responseContainer = "Map")
    @RequestMapping(value = "consulting/problem/statistics", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void consultingProblemStatistics(PrintWriter out) {
        try {
    			List<Map<String,String>> advicelist = adviceService.numbersOfAdviceType();
        		//咨询问题
            int numbersOfAdvice = adviceService.numbersOfAdvice();
            Map<String, String> map = new HashMap<>();
            map.put("caseType", "0");
            map.put("caseTypeDesc", "全部");
            map.put("caseTypeNum", String.valueOf(numbersOfAdvice));
            List<Map<String,String>> advicelists = new ArrayList<Map<String,String>>();
            advicelists.add(map);
            for(int i=1;i<=24;i++) {
                Map<String, String> map1 = new HashMap<>();
                map1.put("caseType", String.valueOf(i));
                map1.put("caseTypeDesc", SystemConfigUtil.getValue(i, SystemConfigUtil.TYPE_CASE));
                map1.put("caseTypeNum", "0");
                for(Map<String,String> advicemap : advicelist) {
                		if(String.valueOf(i).equals(String.valueOf(advicemap.get("caseType")))) {
                			map1.put("caseTypeNum", String.valueOf(advicemap.get("caseTypeNum")));
                			break;
                		}
                }
                advicelists.add(map1);
            }
            out.print(WebUtils.responseData(advicelists));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@ResponseBody
    @ApiOperation(value = "案件委托统计", responseContainer = "Map")
    @RequestMapping(value = "case/commission/statistics", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void caseCommissionStatistics(PrintWriter out) {
        try {
        		List<Map<String,String>> caselist = caseDeputeService.numbersOfCaseType();
        		//案件委托
            int numbersOfCaseDepute = caseDeputeService.numbersOfCaseDepute();
            Map<String, String> map = new HashMap<>();
            map.put("caseType", "0");
            map.put("caseTypeDesc", "全部");
            map.put("caseTypeNum", String.valueOf(numbersOfCaseDepute));
            List<Map<String,String>> caselists = new ArrayList<Map<String,String>>();
            caselists.add(map);
            for(int i=1;i<=24;i++) {
                Map<String, String> map1 = new HashMap<>();
                map1.put("caseType", String.valueOf(i));
                map1.put("caseTypeDesc", SystemConfigUtil.getValue(i, SystemConfigUtil.TYPE_CASE));
                map1.put("caseTypeNum", "0");
                for(Map<String,String> casemap : caselist) {
                		if(String.valueOf(i).equals(String.valueOf(casemap.get("caseType")))) {
                			map1.put("caseTypeNum", String.valueOf(casemap.get("caseTypeNum")));
                			break;
                		}
                }
                caselists.add(map1);
            }
            out.print(WebUtils.responseData(caselists));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "会员人数", responseContainer = "Map")
    @RequestMapping(value = "number/of/members", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void numberOfMembers(PrintWriter out) {
        try {
            Integer numbers = userService.numberOfMembers();
            out.print(WebUtils.responseData(numbers));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "当前续费VIP", responseContainer = "Map")
    @RequestMapping(value = "current/renewal/fee/VIP", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void currentRenewalFeeVIP(PrintWriter out) {
        try {
            Integer numbers = userService.currentRenewalFeeVip();
            out.print(WebUtils.responseData(numbers));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "咨询问题", responseContainer = "Map")
    @RequestMapping(value = "counseling", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void counseling(PrintWriter out) {
        try {
            Integer numbersOfAdvice = adviceService.numbersOfAdvice();
            out.print(WebUtils.responseData(numbersOfAdvice));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "案件委托", responseContainer = "Map")
    @RequestMapping(value = "case/commission", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void caseCommission(PrintWriter out) {
        try {
            Integer numbersOfCaseDepute = caseDeputeService.numbersOfCaseDepute();
            out.print(WebUtils.responseData(numbersOfCaseDepute));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "法律文书制作", responseContainer = "Map")
    @RequestMapping(value = "legal/document/production", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void legalDocumentProduction(PrintWriter out) {
        try {
            Integer numbersOfLegalDocumentProduction = castReplyService.numbersOfLegalDocumentProduction();
            out.print(WebUtils.responseData(numbersOfLegalDocumentProduction));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
