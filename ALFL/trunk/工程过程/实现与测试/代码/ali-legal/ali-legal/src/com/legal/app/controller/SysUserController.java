package com.legal.app.controller;

import com.common.utils.MD5Util;
import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.SysUser;
import com.legal.app.service.ISysUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/system/user")
@Api(value = "系统管理-用户管理", tags = "系统管理-用户管理")
public class SysUserController extends SuperController{

    @Autowired
    private ISysUserService sysUserService;

    @ResponseBody
    @ApiOperation(value = "用户列表", notes = "", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Pagination4Datatable execute(SysUser sysUser) {
        Integer iDisplayLength = sysUser.getLength();
        int currentPage = sysUser.getStart() / iDisplayLength + 1;
        System.out.println("sysUser.getStart() = " + sysUser.getStart());
        List<SysUser> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = sysUserService.list(sysUser, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<SysUser> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);
        return Pagination4Datatable.getInstanceMap(pagingResult, sysUser.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "用户列表", notes = "", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST)
    public Pagination4Datatable param(@RequestBody SysUser sysUser) {
        Integer iDisplayLength = sysUser.getLength();
        int currentPage = sysUser.getStart() / iDisplayLength + 1;
        System.out.println("sysUser.getStart() = " + sysUser.getStart());
        List<SysUser> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = sysUserService.list(sysUser, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<SysUser> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);
        return Pagination4Datatable.getInstanceMap(pagingResult, sysUser.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "用户详情", responseContainer = "Map")
    @RequestMapping(value = "info/{sys_userId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void info(@PathVariable Integer sys_userId, PrintWriter out) {
        try {
            SysUser info = sysUserService.info(sys_userId);
            out.print(WebUtils.responseData(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@ResponseBody
    @ApiOperation(value = "新增", notes = "{\n" +
            "  \"sys_userId\": 1,\n" +
            "  \"sys_userName\": \"1\",\n" +
            "  \"sys_userPassword\": \"1\"\n" +
            "  \"sys_roleId\": 0,\n" +
            "  \"sys_nickName\": \"1\",\n" +
            "  \"sys_state\": 0,\n" +
            "  \"isDelete\": 0,\n" +
            "  \"mobile\": \"1\",\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void add(@RequestBody SysUser sysUser, PrintWriter out) {
        try {
        		if(sysUser.getSys_userName()==null || sysUser.getSys_userName().equals("")) {
        			out.print(WebUtils.responseError("用户名不能为空", -1));
        		}else{
            		if(sysUser.getSys_userPassword()==null || sysUser.getSys_userPassword().equals("")) {
            			out.print(WebUtils.responseError("密码不能为空", -1));
            		}else{
                    SysUser systemUser = new SysUser();
                    systemUser.setSys_userName(sysUser.getSys_userName());
                    systemUser = sysUserService.isExist(systemUser);
                		if(systemUser!=null && systemUser.getSys_userPassword()!="") {
                			out.print(WebUtils.responseError("该用户名已存在", -1));
                		}else {
                			sysUser.setSys_userPassword(MD5Util.MD5(sysUser.getSys_userPassword()));
                        sysUserService.add(sysUser);
                        sysUser.setSys_state(1);
                        out.print(WebUtils.responseData(sysUser));
                		}
            		}
        		}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "编辑", notes = "{\n" +
            "  \"sys_userId\": 1,\n" +
            "  \"sys_userName\": \"1\",\n" +
            "  \"sys_userPassword\": \"1\"\n" +
            "  \"sys_roleId\": 0,\n" +
            "  \"sys_nickName\": \"1\",\n" +
            "  \"sys_state\": 0,\n" +
            "  \"isDelete\": 0,\n" +
            "  \"mobile\": \"1\",\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void update(@RequestBody SysUser sysUser, PrintWriter out) {
        try {
			sysUser.setSys_userPassword(MD5Util.MD5(sysUser.getSys_userPassword()));
            int result = sysUserService.update(sysUser);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{sys_userId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer sys_userId, PrintWriter out) {
        try {
            int result = sysUserService.remove(sys_userId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "锁定 1 成功", responseContainer = "Map")
    @RequestMapping(value = "lock/{sys_userId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void lock(@PathVariable Integer sys_userId, PrintWriter out) {
        try {
            int result = sysUserService.lock(sys_userId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "解锁 1 成功", responseContainer = "Map")
    @RequestMapping(value = "unlock/{sys_userId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void unLock(@PathVariable Integer sys_userId, PrintWriter out) {
        try {
            int result = sysUserService.unLock(sys_userId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 修改后台账号密码
     * @throws Exception
     */
	@RequestMapping("changePassword")
    public void changePassword(HttpServletRequest request,PrintWriter out)throws Exception{
    		String sys_userId = request.getParameter("sys_userId");
    		String oldpassword = request.getParameter("oldpassword");
    		String newpassword = request.getParameter("newpassword");
    		String ensurepassword = request.getParameter("ensurepassword");
    		if(sys_userId.equals(null) || sys_userId.equals("")) {
    			out.print(WebUtils.responseError("用户ID不能为空", -1));
    		}else {
        		if(oldpassword.equals(null) || oldpassword.equals("")) {
        			out.print(WebUtils.responseError("原密码不能为空", -1));
        		}else {
            		if(newpassword.equals(null) || newpassword.equals("")) {
            			out.print(WebUtils.responseError("新密码不能为空", -1));
            		}else {
                		if(ensurepassword.equals(null) || ensurepassword.equals("")) {
                			out.print(WebUtils.responseError("确认密码不能为空", -1));
                		}else {
                    		if(!ensurepassword.equals(newpassword)) {
                    			out.print(WebUtils.responseError("确认密码与新密码不一样", -1));
                    		}else {
                        		SysUser user = new SysUser();
                        		user.setSys_userId(Integer.parseInt(sys_userId));
                        		user.setSys_userPassword(oldpassword);
                            SysUser systemUser = sysUserService.isExist(user);
                            if (null == systemUser) {
                            		out.print(WebUtils.responseError("用户密码不正确", -1));
                            }else {
                                systemUser.setSys_userPassword(newpassword);
                                int result = sysUserService.update(systemUser);
                            		out.print(WebUtils.responseData(result));
                            }
                    		}
                		}
            		}
        		}
    		}
    }
}
