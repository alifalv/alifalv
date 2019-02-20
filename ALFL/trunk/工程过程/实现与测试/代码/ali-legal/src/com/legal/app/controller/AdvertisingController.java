package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.Advertising;
import com.legal.app.model.AdvertisingSelectParam;
import com.legal.app.service.IAdvertisingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/advertising")
@Api(tags = "内容管理-广告管理")
public class AdvertisingController extends SuperController{
    @Autowired
    private IAdvertisingService advertisingService;

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{\n" +
            "  \"columnType\": 1,\n" +
            "  \"iDisplayLength\": 10,\n" +
            "  \"iDisplayStart\": 0\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(AdvertisingSelectParam advertisingSelectParam) {
        Integer iDisplayLength = advertisingSelectParam.getLength();
        int currentPage = advertisingSelectParam.getStart() / iDisplayLength + 1;
        List<Advertising> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = advertisingService.list(advertisingSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Advertising> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, advertisingSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{\n" +
            "  \"columnType\": 1,\n" +
            "  \"iDisplayLength\": 10,\n" +
            "  \"iDisplayStart\": 0\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody AdvertisingSelectParam advertisingSelectParam) {
        List<Advertising> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(advertisingSelectParam.getSize(), advertisingSelectParam.getPage(), true);
        try {
            list = advertisingService.list(advertisingSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Advertising> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        Integer iDisplayLength = advertisingSelectParam.getLength();
        pagingResult.setLimit(iDisplayLength);
        int currentPage = advertisingSelectParam.getStart() / iDisplayLength + 1;
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, advertisingSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "增加", responseContainer = "Map")
    @RequestMapping(value = "add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void add(@RequestBody Advertising advertising, PrintWriter out) {
        try {
            advertisingService.add(advertising);
            out.print(WebUtils.responseData(advertising));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "详情", responseContainer = "Map")
    @RequestMapping(value = "info/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void info(@PathVariable Integer id, PrintWriter out) {
        try {
            Advertising info = new Advertising();
            info.setId(id);
            info = advertisingService.info(info);
            out.print(WebUtils.responseData(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "编辑", notes = "{\n" +
            "  \"columnType\": 2,\n" +
            "  \"content\": \"2\",\n" +
            "  \"image\": \"2\",\n" +
            "  \"name\": \"2\",\n" +
            "  \"place\": \"2\",\n" +
            "  \"url\": \"2\",\n" +
            "  \"id\": 1\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void update(@RequestBody Advertising advertising, PrintWriter out) {
        try {
        		int result = advertisingService.update(advertising);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer id, PrintWriter out) {
        try {
        		int result = advertisingService.remove(id);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
