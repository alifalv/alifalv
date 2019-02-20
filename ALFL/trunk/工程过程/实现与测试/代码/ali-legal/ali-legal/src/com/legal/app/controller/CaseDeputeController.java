package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.CaseDepute;
import com.legal.app.service.ICaseDeputeService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/case/depute")
@Api(value = "CaseDepute", tags = "案源管理-案源列表")
public class CaseDeputeController extends SuperController{

    @Autowired
    private ICaseDeputeService caseDeputeService;

    @Resource
    private AddressController addressController;

    @RequestMapping("/index")
    public ModelAndView show() {
        ModelAndView mav = new ModelAndView("caseDepute");
        return mav;
    }

    @ResponseBody
    @ApiOperation(value = "获取所有案源 ", notes = "{ \"caseType\": 1, \"cityDescription\": \"长沙\", \"deputeTimeEnd\": \"2018-08-17\", \"deputeTimeStart\": \"2018-08-18\", \"content\": \"张三\", \"iDisplayStart\": 1, \"iDisplayLength\": 2}", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(CaseDepute caseDepute) {
        Integer iDisplayLength = caseDepute.getLength();
        int currentPage = caseDepute.getStart() / iDisplayLength + 1;
        List<CaseDepute> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = caseDeputeService.list(caseDepute, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<CaseDepute> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, caseDepute.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "获取所有案源 ", notes = "{ \"caseType\": 1, \"cityDescription\": \"长沙\", \"deputeTimeEnd\": \"2018-08-17\", \"deputeTimeStart\": \"2018-08-18\", \"content\": \"张三\", \"iDisplayStart\": 1, \"iDisplayLength\": 2}", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody CaseDepute caseDepute) {
        List<CaseDepute> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(caseDepute.getSize(), caseDepute.getPage(), true);
        try {
            list = caseDeputeService.list(caseDepute, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<CaseDepute> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        Integer iDisplayLength = caseDepute.getLength();
        pagingResult.setLimit(iDisplayLength);
        int currentPage = caseDepute.getStart() / iDisplayLength + 1;
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, caseDepute.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "案源详情", responseContainer = "Map")
    @RequestMapping(value = "info/{caseId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void infoConsultant(@PathVariable int caseId, PrintWriter out) {
        try {
            CaseDepute caseDepute = caseDeputeService.info(caseId);
            out.print(WebUtils.responseData(caseDepute));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "删除 1 删除成功", responseContainer = "Map")
    @RequestMapping(value = "remove/{caseId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void check(@PathVariable int caseId, PrintWriter out) {
        try {
            int result = caseDeputeService.remove(caseId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
