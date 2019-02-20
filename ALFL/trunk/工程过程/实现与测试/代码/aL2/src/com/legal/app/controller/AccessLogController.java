package com.legal.app.controller;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.dbutil.Paging;
import com.common.log.BusinessException;
import com.common.web.WebUtils;
import com.legal.app.service.AccessLogService;

@Controller
@RequestMapping("accessLog")
public class AccessLogController extends SuperController{
	@Autowired
	private AccessLogService  accSer;
 
 @RequestMapping("listAccessLog")
  public void  listAccessLog(PrintWriter out,HttpServletRequest request) {
	 Paging  paging = new Paging(5, 1,true);   
	 Map<String, Object> map = getMap(request);
	 Object userId = map.get("userId");
	 if(null == userId || userId.equals("")) {
		 throw new BusinessException("用户编号不能为空.", -1);
	 }
	 try {
		List<Map> listM= accSer.listAccessLog(map, paging);
		 out.print(WebUtils.webResponsePage(listM));
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		 out.print(WebUtils.responseMsg("查询失败.",-1));
	}
  }
}
