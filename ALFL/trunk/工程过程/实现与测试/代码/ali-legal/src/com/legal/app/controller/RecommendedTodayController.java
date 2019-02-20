package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.RecommendedToday;
import com.legal.app.model.RecommendedTodaySelectParam;
import com.legal.app.service.IRecommendedTodayService;
import com.legal.app.utils.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/recommended/today")
@Api(tags = "内容管理-今日推荐")
public class RecommendedTodayController extends SuperController{
    @Autowired
    private IRecommendedTodayService recommendedTodayService;

    @Autowired
    SessionUtils sessionUtils;

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{ \"addTimeEnd\": \"2018-08-30\", \"addTimeStart\": \"2018-08-28\", \"columnType\": 1, \"content\": \"张\", \"iDisplayLength\": 1, \"iDisplayStart\": 10 }", responseContainer = "Map")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(RecommendedTodaySelectParam recommendedTodaySelectParam) {
        Integer iDisplayLength = recommendedTodaySelectParam.getLength();
        int currentPage = recommendedTodaySelectParam.getStart() / iDisplayLength + 1;
        List<RecommendedToday> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = recommendedTodayService.list(recommendedTodaySelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<RecommendedToday> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, recommendedTodaySelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{ \"addTimeEnd\": \"2018-08-30\", \"addTimeStart\": \"2018-08-28\", \"columnType\": 1, \"content\": \"张\", \"iDisplayLength\": 1, \"iDisplayStart\": 10 }", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody RecommendedTodaySelectParam recommendedTodaySelectParam) {
        List<RecommendedToday> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(recommendedTodaySelectParam.getSize(), recommendedTodaySelectParam.getPage(), true);
        try {
            list = recommendedTodayService.list(recommendedTodaySelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<RecommendedToday> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        Integer iDisplayLength = recommendedTodaySelectParam.getLength();
        pagingResult.setLimit(iDisplayLength);
        int currentPage = recommendedTodaySelectParam.getStart() / iDisplayLength + 1;
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, recommendedTodaySelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "增加", notes = "{\n" +
            "  \"articleId\": 1,\n" +
            "  \"columnType\": 1,\n" +
            "  \"order\": 1,\n" +
            "  \"title\": \"三\"\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public RecommendedToday add(@RequestBody RecommendedToday recommendedToday) {
        try {
            recommendedTodayService.add(recommendedToday);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recommendedToday;
    }

    @ResponseBody
    @ApiOperation(value = "详情", notes = "{\n" +
            "  \"columnType\": 1,\n" +
            "  \"articleId\": 1,\n" +
            "  \"order\": 2\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "info/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public RecommendedToday info(@PathVariable Integer id) {
        RecommendedToday info = new RecommendedToday();
        try {
            info.setId(id);
            info = recommendedTodayService.info(info);
            System.out.println("info = " + info.getColumnType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    @ResponseBody
    @ApiOperation(value = "编辑", notes = "{\n" +
            "  \"articleId\": 2,\n" +
            "  \"columnType\": 2,\n" +
            "  \"createUser\": 2,\n" +
            "  \"order\": 2,\n" +
            "  \"title\": \"2\",\n" +
            "  \"id\":1\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public int update(@RequestBody RecommendedToday recommendedToday) {
    		int result = 0;
        try {
            result = recommendedTodayService.update(recommendedToday);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer id, PrintWriter out) {
        try {
            int result = recommendedTodayService.remove(id);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
