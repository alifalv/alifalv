package com.legal.app.controller;

import com.common.log.BusinessException;
import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.Column;
import com.legal.app.model.ResearchParameter;
import com.legal.app.service.ColumnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/column/sys")
@Api(tags = "内容管理-所属栏目")
public class ColumnSysController extends SuperController {
    @Autowired
    private ColumnService columnService;

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{ \"addTimeEnd\": \"2018-08-30\", \"addTimeStart\": \"2018-08-28\", \"type\": 1, \"content\": \"张\"}", responseContainer = "Map")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(ResearchParameter researchParameter) {
        Integer iDisplayLength = researchParameter.getLength();
        int currentPage = researchParameter.getStart() / iDisplayLength + 1;
        List<Column> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = columnService.queryAll(paging);
	    		if (null != list && list.size() > 0) {
	    			list.remove(list.size()-1);
	    		}
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Column> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, researchParameter.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{columnId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer columnId, PrintWriter out) {
        try {
            int result = columnService.remove(columnId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "列表", responseContainer = "Map")
    public void pageList(PrintWriter out) {
        try {
            List<Column> list = columnService.queryAll(new com.common.dbutil.Paging(1000, 1, true));
            out.print(WebUtils.webResponsePage(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("栏目信息获取异常", -1);
        }
    }

    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "列表", responseContainer = "Map")
    public void param(@RequestBody Column column, PrintWriter out) {
        try {
            List<Column> list = columnService.queryAll(new com.common.dbutil.Paging(1000, 1, true));
            out.print(WebUtils.webResponsePage(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("栏目信息获取异常", -1);
        }
    }
}
