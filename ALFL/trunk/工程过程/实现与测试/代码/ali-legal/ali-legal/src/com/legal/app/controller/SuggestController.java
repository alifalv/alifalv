package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.Suggest;
import com.legal.app.service.ISuggestService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/suggest")
@Api(tags = "系统管理-意见反馈")
public class SuggestController extends SuperController{

    @Autowired
    private ISuggestService suggestService;

    @ResponseBody
    @ApiOperation(value = "列表", notes = "", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(Suggest suggest) {
        Integer iDisplayLength = suggest.getLength();
        int currentPage = suggest.getStart() / iDisplayLength + 1;
        List<Suggest> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = suggestService.list(suggest, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Suggest> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);
        return Pagination4Datatable.getInstanceMap(pagingResult, suggest.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody Suggest suggest) {
        Integer iDisplayLength = suggest.getLength();
        int currentPage = suggest.getStart() / iDisplayLength + 1;
        List<Suggest> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = suggestService.list(suggest, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Suggest> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);
        return Pagination4Datatable.getInstanceMap(pagingResult, suggest.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "详情", responseContainer = "Map")
    @RequestMapping(value = "info/{suggestId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void info(@PathVariable Integer suggestId, PrintWriter out) {
        try {
            Suggest info = suggestService.info(suggestId);
            out.print(WebUtils.responseData(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "reply", notes = "{\n" +
            "  \"suggestId\": 1,\n" +
            "  \"suggestContent\": \"1\",\n" +
            "  \"userId\": 0,\n" +
            "  \"suggestTime\": \"1\",\n" +
            "  \"isComplate\": 0,\n" +
            "  \"replyContent\": \"1\",\n" +
            "  \"complateTime\": \"1\"\n" +
            "  \"mobile\": \"1\"\n" +
            "  \"isDelete\": 0,\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "reply", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void reply(@RequestBody Suggest suggest, PrintWriter out) {
        try {
            suggestService.reply(suggest);
            out.print(WebUtils.responseData(suggest));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{suggestId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer suggestId, PrintWriter out) {
        try {
            int result = suggestService.remove(suggestId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
