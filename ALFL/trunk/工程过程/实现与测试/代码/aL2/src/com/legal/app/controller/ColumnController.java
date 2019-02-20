package com.legal.app.controller;

import com.common.log.BusinessException;
import com.common.web.WebUtils;
import com.legal.app.service.ColumnService;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/column"})
public class ColumnController
  extends SuperController
{
  @Autowired
  private ColumnService columnService;
  
  
  
  @RequestMapping({"/queryColumn/{columnType}"})
  public void queryColumn(@PathVariable("columnType") int columnType, PrintWriter out)
  {
	  
	  try {
		List<Map> list = columnService.findColumnByType(columnType);
		out.print(WebUtils.responseData(list));
	} catch (Exception e) {
		e.printStackTrace();
		throw new BusinessException("栏目类型获取失败.",-1);
	}
  }
}
