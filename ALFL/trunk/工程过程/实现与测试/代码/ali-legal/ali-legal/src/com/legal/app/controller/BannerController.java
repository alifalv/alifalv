package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.Banner;
import com.legal.app.model.BannerSelectParam;
import com.legal.app.service.IBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/banner")
@Api(tags = "内容管理-banner")
public class BannerController extends SuperController{
    @Autowired
    private IBannerService bannerService;

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{\n" +
            "  \"columnType\": 2,\n" +
            "  \"iDisplayLength\": 10,\n" +
            "  \"iDisplayStart\": 1\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(BannerSelectParam bannerSelectParam) {
        Integer iDisplayLength = bannerSelectParam.getLength();
        int currentPage = bannerSelectParam.getStart() / iDisplayLength + 1;
        List<Banner> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = bannerService.list(bannerSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Banner> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, bannerSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{\n" +
            "  \"columnType\": 2,\n" +
            "  \"iDisplayLength\": 10,\n" +
            "  \"iDisplayStart\": 1\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody BannerSelectParam bannerSelectParam) {
        List<Banner> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(bannerSelectParam.getSize(), bannerSelectParam.getPage(), true);
        try {
            list = bannerService.list(bannerSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Banner> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        Integer iDisplayLength = bannerSelectParam.getLength();
        pagingResult.setLimit(iDisplayLength);
        int currentPage = bannerSelectParam.getStart() / iDisplayLength + 1;
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, bannerSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "增加", notes = "{\n" +
            "  \"columnType\": 2,\n" +
            "  \"content\": \"2\",\n" +
            "  \"images\": [\n" +
            "    {\n" +
            "      \"fileId\": \"2\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"name\": \"2\",\n" +
            "  \"place\": \"2\",\n" +
            "  \"type\": 2,\n" +
            "  \"url\": \"2\"\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void add(Banner banner, PrintWriter out) {
        try {
            bannerService.add(banner);
            out.print(WebUtils.responseData(banner));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "详情", responseContainer = "Map")
    @RequestMapping(value = "info/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void info(@PathVariable Integer id, PrintWriter out) {
        try {
            Banner info = new Banner();
            info.setId(id);
            info = bannerService.info(info);
            out.print(WebUtils.responseData(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "编辑", notes = "{ \"columnType\": 2, \"content\": \"2\", \"images\": [ { \"fileId\": \"2\" } ], \"name\": \"2\", \"place\": \"2\", \"type\": 2, \"url\": \"2\", \"id\": 1}", responseContainer = "Map")
    @RequestMapping(value = "update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void update(@RequestBody Banner banner, PrintWriter out) {
        try {
        		if(banner.getImages()==null) {
        			banner.setImages("");
        		}
            bannerService.update(banner);
            out.print(WebUtils.responseData(banner));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer id, PrintWriter out) {
        try {
            int result = bannerService.remove(id);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
