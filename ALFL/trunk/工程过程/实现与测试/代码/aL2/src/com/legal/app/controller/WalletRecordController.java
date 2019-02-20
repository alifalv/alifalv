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
import com.legal.app.service.WalletRecordService;

/**
 * 保存钱包流水记录
 * @author 军山依旧在
 *
 */
@Controller
@RequestMapping("walletRecord")
public class WalletRecordController  extends SuperController {
   
		@Autowired
	     WalletRecordService wallSer;
	/**
	 * 保存钱包流水记录
	 */
	@RequestMapping("saveWalletRecord")
	 public void saveWalletRecord(
			 PrintWriter out,
			 HttpServletRequest request
			 ) {
		 Map<String, Object> m = getMap(request);
		Object ob =  m.get("userId");
		if(null == ob || ob.equals("")) {
			 throw new BusinessException("用户编号不能为空.", -1);
		}
		
		Object businessOrder =  m.get("businessOrder");
		if(null == businessOrder || businessOrder.equals("")) {
			 throw new BusinessException("订单编号不能为空.", -1);
		}
		
		 try {
			wallSer.saveWalletRecord(m);
			out.print(WebUtils.responseData("保存成功.",1, m));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 throw new BusinessException("保存失败.", -1);
		}
	 }
	
	
	@RequestMapping("listWalletRecord/{pageNo}/{pageSize}")
	public void listWalletRecord( 
			@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize,
			PrintWriter out,
			 HttpServletRequest request
			) {
		Map m = getMap(request);
		Object userId = m.get("userId");
		if(null == userId || userId.equals("")) {
			 throw new BusinessException("userId不能为空.", -1);
		}
		Paging paging = new Paging(pageSize, pageNo, true);
		try {
			List list = this.wallSer.listWalletRecord(m, paging);
			out.print(WebUtils.webResponsePage(list));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 throw new BusinessException("查询失败.", -1);
		}
	}
	 
	 
}
