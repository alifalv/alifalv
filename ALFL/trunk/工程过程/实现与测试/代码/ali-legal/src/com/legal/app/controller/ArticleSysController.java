package com.legal.app.controller;

import com.common.log.BusinessException;
import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.Article;
import com.legal.app.model.ResearchParameter;
import com.legal.app.service.IArticleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/article/sys")
@Api(tags = "内容管理-会员文章")
public class ArticleSysController extends SuperController {
    @Autowired
    private IArticleService articleService;

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{ \"addTimeEnd\": \"2018-08-30\", \"addTimeStart\": \"2018-08-28\", \"type\": 1, \"content\": \"张\"}", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(ResearchParameter researchParameter) {
        Integer iDisplayLength = researchParameter.getLength();
        int currentPage = researchParameter.getStart() / iDisplayLength + 1;
        List<Article> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = articleService.list(researchParameter, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Article> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, researchParameter.getsEcho());
    }

    /*@RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "列表", responseContainer = "Map")
    public void list(Article article, PrintWriter out) {
        try {
            List<Article> list = articleService.list(article);
            out.print(WebUtils.webResponsePage(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("栏目信息获取异常", -1);
        }
    }*/

    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "列表", responseContainer = "Map")
    public void param(@RequestBody Article article, PrintWriter out) {
        try {
            List<Article> list = articleService.list(article);
            out.print(WebUtils.webResponsePage(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("栏目信息获取异常", -1);
        }
    }

    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{articleId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer articleId, PrintWriter out) {
        try {
            int result = articleService.remove(articleId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
