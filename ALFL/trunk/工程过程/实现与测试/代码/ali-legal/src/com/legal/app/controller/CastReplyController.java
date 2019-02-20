package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.CastReply;
import com.legal.app.model.CastReplySelectParam;
import com.legal.app.service.ICastReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cast/reply")
@Api(tags = "订单管理-VIP免费申报")
public class CastReplyController extends SuperController{
    @Autowired
    private ICastReplyService CastReplyService;

    @RequestMapping("/index")
    public ModelAndView show() {
        ModelAndView mav = new ModelAndView("castReply");
        return mav;
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "不传replyState为法律文书申报 replyState传1为交通事故案件申报", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(CastReplySelectParam CastReplySelectParam) {
        Integer iDisplayLength = CastReplySelectParam.getLength();
        int currentPage = CastReplySelectParam.getStart() / iDisplayLength + 1;
        List<CastReply> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = CastReplyService.list(CastReplySelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<CastReply> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, CastReplySelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "不传replyState为法律文书申报 replyState传1为交通事故案件申报", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody CastReplySelectParam CastReplySelectParam, @RequestBody CastReply CastReply) {
        List<CastReply> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(CastReplySelectParam.getSize(), CastReplySelectParam.getPage(), true);
        try {
            list = CastReplyService.list(CastReplySelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<CastReply> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        Integer iDisplayLength = CastReplySelectParam.getLength();
        pagingResult.setLimit(iDisplayLength);
        int currentPage = CastReplySelectParam.getStart() / iDisplayLength + 1;
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, CastReplySelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "详情", responseContainer = "Map")
    @RequestMapping(value = "info/{replyId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void info(@PathVariable Integer replyId, PrintWriter out) {
        try {
            CastReply info = CastReplyService.info(replyId);
            out.print(WebUtils.responseData(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "审核", responseContainer = "Map")
    @RequestMapping(value = "check/{replyId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void check(@PathVariable Integer replyId, PrintWriter out) {
        try {
            int result = CastReplyService.check(replyId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "审核", responseContainer = "Map")
    @RequestMapping(value = "uncheck/{replyId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void uncheck(@PathVariable Integer replyId, PrintWriter out) {
        try {
            int result = CastReplyService.uncheck(replyId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}