package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.BusinessOrder;
import com.legal.app.model.BusinessOrderSelectParam;
import com.legal.app.service.IOrderService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/bank/card/withdraw")
@Api(tags = "财务管理-银行卡提现/退款")
public class BankCardWithdrawController extends SuperController{
    @Autowired
    private IOrderService orderService;

    @RequestMapping("/index")
    public ModelAndView show() {
        ModelAndView mav = new ModelAndView("bankCardWithdraw");
        return mav;
    }

    @ResponseBody
    @ApiOperation(value = "列表 ", notes = "{ \"content\": \"ddbh2018\", \"businessType\": 1, \"createTimeStart\": null, \"createTimeEnd\": null}", responseContainer = "Map")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(BusinessOrderSelectParam businessOrderSelectParam) {
		businessOrderSelectParam.setPayment("银行卡支付");
        Integer iDisplayLength = businessOrderSelectParam.getLength();
        int currentPage = businessOrderSelectParam.getStart() / iDisplayLength + 1;
        List<BusinessOrder> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = orderService.list(businessOrderSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<BusinessOrder> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, businessOrderSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表 ", notes = "{ \"content\": \"ddbh2018\", \"businessType\": 1, \"createTimeStart\": null, \"createTimeEnd\": null}", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody BusinessOrderSelectParam businessOrderSelectParam) {
		businessOrderSelectParam.setPayment("银行卡支付");
        List<BusinessOrder> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(businessOrderSelectParam.getSize(), businessOrderSelectParam.getPage(), true);
        try {
            list = orderService.list(businessOrderSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<BusinessOrder> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        Integer iDisplayLength = businessOrderSelectParam.getLength();
        pagingResult.setLimit(iDisplayLength);
        int currentPage = businessOrderSelectParam.getStart() / iDisplayLength + 1;
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, businessOrderSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "审核通过 1 审核成功", responseContainer = "Map")
    @RequestMapping(value = "check/{businessOrder}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void check(@PathVariable String businessOrder, PrintWriter out) {
        try {
            int result = orderService.check(businessOrder);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "审核不通过 1 审核成功", responseContainer = "Map")
    @RequestMapping(value = "check/un/{businessOrder}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void checkUn(@PathVariable String businessOrder, PrintWriter out) {
        try {
            int result = orderService.checkUn(businessOrder);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
