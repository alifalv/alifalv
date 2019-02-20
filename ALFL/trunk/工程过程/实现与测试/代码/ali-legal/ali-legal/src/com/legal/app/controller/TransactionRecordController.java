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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/transaction/record")
@Api(tags = "财务管理-交易记录")
public class TransactionRecordController extends SuperController{

    @Autowired
    private IOrderService orderService;

    @ResponseBody
    @ApiOperation(value = "列表 ", notes = "{ \"content\": \"ddbh2018\", \"businessType\": 1, \"createTimeStart\": null, \"createTimeEnd\": null}", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(BusinessOrderSelectParam businessOrderSelectParam) {
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
    /**
     * 站内消息发送
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	@RequestMapping("totalCount")
    public void totalCount(HttpServletRequest request,PrintWriter out)throws Exception{
		Map<String,Object> map = getMap(request);
		Map<String,Object> resultmap = new HashMap<String,Object>();
		int totalone = 0;//已提现总金额|收入总金额
		int totaltwo = 0;//待提现总金额|支出总金额
		if(request.getParameter("businessType").equals("提现") && request.getParameter("payment")!=null) {
			if(request.getParameter("isPay").equals("")) {//全部
				map.put("isPay","1");
				map.put("orderState","0");
				totalone = orderService.totalCount(map);
				map.put("isPay","6");
				map.put("orderState","1");
				totaltwo = orderService.totalCount(map);
			}else if(request.getParameter("isPay").equals("6")) {//待审核
				map.put("orderState","1");
				totaltwo = orderService.totalCount(map);
			}else if(request.getParameter("isPay").equals("1")) {//提现成功
				map.put("orderState","0");
				totalone = orderService.totalCount(map);
			}else if(request.getParameter("isPay").equals("4")) {//提现失败
				
			}
		}else {//交易记录
			map.put("orderState","0");//已完成
			map.put("orderType","1");
			totalone = orderService.totalCount(map);
			map.put("orderType","2");
			totaltwo = orderService.totalCount(map);
		}
		resultmap.put("totalone", totalone);
		resultmap.put("totaltwo", totaltwo);
		out.print(WebUtils.responseData(resultmap));
    }
}
