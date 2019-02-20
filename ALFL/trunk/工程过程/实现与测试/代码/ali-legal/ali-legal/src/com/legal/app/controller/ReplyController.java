package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.CommonReply;
import com.legal.app.model.CommonReplySelectParam;
import com.legal.app.service.IReplyService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reply")
@Api(tags = "回复与评价管理-回复管理")
public class ReplyController extends SuperController{
    @Autowired
    private IReplyService replyService;

    @ResponseBody
    @ApiOperation(value = "列表", notes = "", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(CommonReplySelectParam commonReplySelectParam) {
        Integer iDisplayLength = commonReplySelectParam.getLength();
        int currentPage = commonReplySelectParam.getStart() / iDisplayLength + 1;
        List<CommonReply> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = replyService.list(commonReplySelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<CommonReply> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, commonReplySelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody CommonReplySelectParam commonReplySelectParam) {
        List<CommonReply> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(commonReplySelectParam.getSize(), commonReplySelectParam.getPage(), true);
        try {
            list = replyService.list(commonReplySelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<CommonReply> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        Integer iDisplayLength = commonReplySelectParam.getLength();
        pagingResult.setLimit(iDisplayLength);
        int currentPage = commonReplySelectParam.getStart() / iDisplayLength + 1;
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, commonReplySelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "详情", responseContainer = "Map")
    @RequestMapping(value = "info/{replyId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void info(@PathVariable Integer replyId, PrintWriter out) {
        try {
            CommonReply info = replyService.info(replyId);
            out.print(WebUtils.responseData(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{replyId}/{origin}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer replyId,@PathVariable Integer origin, PrintWriter out) {
        try {
            int result = replyService.remove(replyId,origin);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}