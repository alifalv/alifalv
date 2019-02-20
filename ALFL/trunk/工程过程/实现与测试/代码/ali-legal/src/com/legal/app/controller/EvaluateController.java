package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.Evaluate;
import com.legal.app.model.EvaluateSelectParam;
import com.legal.app.service.IEvaluateService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/evaluate")
@Api(tags = "回复与评价管理-订单评价")
public class EvaluateController extends SuperController{
    @Autowired
    private IEvaluateService evaluateService;

    @RequestMapping("/index")
    public ModelAndView show() {
        ModelAndView mav = new ModelAndView("evaluate");
        return mav;
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(EvaluateSelectParam evaluateSelectParam) {
        Integer iDisplayLength = evaluateSelectParam.getLength();
        int currentPage = evaluateSelectParam.getStart() / iDisplayLength + 1;
        List<Evaluate> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = evaluateService.list(evaluateSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Evaluate> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, evaluateSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody EvaluateSelectParam evaluateSelectParam) {
        List<Evaluate> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(evaluateSelectParam.getSize(), evaluateSelectParam.getPage(), true);
        try {
            list = evaluateService.list(evaluateSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Evaluate> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        Integer iDisplayLength = evaluateSelectParam.getLength();
        pagingResult.setLimit(iDisplayLength);
        int currentPage = evaluateSelectParam.getStart() / iDisplayLength + 1;
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, evaluateSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "详情", responseContainer = "Map")
    @RequestMapping(value = "info/{replyId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void info(@PathVariable Integer replyId, PrintWriter out) {
        try {
            Evaluate info = evaluateService.info(replyId);
            out.print(WebUtils.responseData(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{replyId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer replyId, PrintWriter out) {
        try {
            int result = evaluateService.remove(replyId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}