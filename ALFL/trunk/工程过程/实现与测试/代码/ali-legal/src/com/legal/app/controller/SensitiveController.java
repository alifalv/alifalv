package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.Sensitive;
import com.legal.app.service.ISensitiveService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sensitive")
@Api(tags = "系统管理-敏感词库")
public class SensitiveController extends SuperController{
    @Autowired
    private ISensitiveService sensitiveService;

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{\n" +
            "  \"sensitiveValue\": \"傻\"\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(Sensitive sensitive) {
        Integer iDisplayLength = sensitive.getLength();
        int currentPage = sensitive.getStart() / iDisplayLength + 1;
        List<Sensitive> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = sensitiveService.list(sensitive, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Sensitive> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, sensitive.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{\n" +
            "  \"sensitiveValue\": \"傻\"\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody Sensitive sensitive) {
        Integer iDisplayLength = sensitive.getLength();
        int currentPage = sensitive.getStart() / iDisplayLength + 1;
        List<Sensitive> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = sensitiveService.list(sensitive, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Sensitive> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, sensitive.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "详情", responseContainer = "Map")
    @RequestMapping(value = "info/{sensitiveId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void info(@PathVariable Integer sensitiveId, PrintWriter out) {
        try {
            Sensitive info = sensitiveService.info(sensitiveId);
            out.print(WebUtils.responseData(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "新增", notes = "{\n" +
            "  \"sensitiveId\": 1,\n" +
            "  \"sensitiveValue\": \"1\",\n" +
            "  \"is_delete\": 0,\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void add(@RequestBody Sensitive sensitive, PrintWriter out) {
        try {
        		sensitiveService.add(sensitive);
            out.print(WebUtils.responseData(sensitive));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "编辑", notes = "{\n" +
            "  \"sensitiveId\": 1,\n" +
            "  \"sensitiveValue\": \"1\",\n" +
            "  \"is_delete\": 0,\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void update(@RequestBody Sensitive sensitive, PrintWriter out) {
        try {
            int result = sensitiveService.update(sensitive);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{sensitiveId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer sensitiveId, PrintWriter out) {
        try {
            int result = sensitiveService.remove(sensitiveId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
