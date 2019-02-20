package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.Advice;
import com.legal.app.model.AdviceSelectParam;
import com.legal.app.service.IAdviceService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/advice")
@Api(tags = "咨询管理-咨询列表")
public class AdviceController extends SuperController{
    @Autowired
    private IAdviceService adviceService;

    @RequestMapping("/index")
    public ModelAndView show() {
        ModelAndView mav = new ModelAndView("advice");
        return mav;
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{\n" +
            "  \"caseType\": 1,\n" +
            "  \"content\": \"大\",\n" +
            "  \"sendTimeEnd\": \"2018-07-09\",\n" +
            "  \"sendTimeStart\": \"2018-07-07\"\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(AdviceSelectParam adviceSelectParam) {
        Integer iDisplayLength = adviceSelectParam.getLength();
        int currentPage = adviceSelectParam.getStart() / iDisplayLength + 1;
        List<Advice> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = adviceService.list(adviceSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Advice> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, adviceSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{\n" +
            "  \"caseType\": 1,\n" +
            "  \"content\": \"大\",\n" +
            "  \"sendTimeEnd\": \"2018-07-09\",\n" +
            "  \"sendTimeStart\": \"2018-07-07\"\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody AdviceSelectParam adviceSelectParam) {
        List<Advice> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(adviceSelectParam.getSize(), adviceSelectParam.getPage(), true);
        try {
            list = adviceService.list(adviceSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Advice> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        Integer iDisplayLength = adviceSelectParam.getLength();
        pagingResult.setLimit(iDisplayLength);
        int currentPage = adviceSelectParam.getStart() / iDisplayLength + 1;
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, adviceSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "详情", responseContainer = "Map")
    @RequestMapping(value = "info/{adviceId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void info(@PathVariable Integer adviceId, PrintWriter out) {
        try {
            Advice info = adviceService.info(adviceId);
            out.print(WebUtils.responseData(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{adviceId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer adviceId, PrintWriter out) {
        try {
            int result = adviceService.remove(adviceId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
