package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.Report;
import com.legal.app.model.ReportSelectParam;
import com.legal.app.service.IReportService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/report")
@Api(tags = "系统管理-举报管理")
public class ReportController extends SuperController{
    @Autowired
    private IReportService reportService;

    @RequestMapping("/index")
    public ModelAndView show() {
        ModelAndView mav = new ModelAndView("report");
        return mav;
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{\n" +
            "  \"ReportValue\": \"傻\"\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(ReportSelectParam reportSelectParam) {
        Integer iDisplayLength = reportSelectParam.getLength();
        int currentPage = reportSelectParam.getStart() / iDisplayLength + 1;
        List<Report> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = reportService.list(reportSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Report> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, reportSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{\n" +
            "  \"ReportValue\": \"傻\"\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody ReportSelectParam reportSelectParam) {
        Integer iDisplayLength = reportSelectParam.getLength();
        int currentPage = reportSelectParam.getStart() / iDisplayLength + 1;
        List<Report> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = reportService.list(reportSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Report> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, reportSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{reportId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer reportId, HttpServletRequest request,PrintWriter out) {
        try {
            Map<?, ?> map = getMap(request);
            int result = reportService.remove(map);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
