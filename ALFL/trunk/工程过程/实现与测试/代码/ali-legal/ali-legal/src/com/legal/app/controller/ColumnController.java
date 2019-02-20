package com.legal.app.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.common.dbutil.Paging;
import com.common.log.BusinessException;
import com.common.web.WebUtils;
import com.legal.app.model.Column;
import com.legal.app.service.ColumnService;

@Controller
@RequestMapping("/column")
public class ColumnController extends SuperController {

    @Autowired
    private ColumnService columnService;

    @ResponseBody
    @ApiOperation(value = "", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void pageList(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            PrintWriter out) {
        try {
            List<Column> list = columnService.queryAll(new Paging(size, page, true));
            out.print(WebUtils.webResponsePage(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("栏目信息获取异常", -1);
        }
    }

    @ResponseBody
    @ApiOperation(value = "", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void param(@RequestBody Column column,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            PrintWriter out) {
        try {
            List<Column> list = columnService.queryAll(new Paging(size, page, true));
            out.print(WebUtils.webResponsePage(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("栏目信息获取异常", -1);
        }
    }
}
