package com.legal.app.controller;

import com.common.web.WebUtils;
import com.legal.app.model.SysPremisson;
import com.legal.app.model.SysUser;
import com.legal.app.service.IMenuService;
import com.legal.app.utils.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/menu")
@Api(value = "menu", tags = "菜单相关接口")
public class MenuController extends SuperController{
    @Autowired
    private IMenuService iMenuService;

    @Autowired
    private SessionUtils sessionUtils;

    @RequestMapping(value="testReturnw", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ModelAndView testReturn(PrintWriter out) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            List<SysPremisson> list = iMenuService.queryAll();

            out.print(WebUtils.responseData(list));
            //out.println("111111111111");

        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.setViewName("index");
        //modelAndView.setViewName("hello");
        modelAndView.addObject("menu", "list");
        modelAndView.addObject("param", "1wxyz");

        return modelAndView;
    }

    @ResponseBody
    @ApiOperation(value = "获取所有菜单", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void execute(PrintWriter out) {
        try {
            List<SysPremisson> list = iMenuService.queryAll();
            out.print(WebUtils.responseData(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "获取数据结构菜单", responseContainer = "Map")
    @RequestMapping(value="order/list", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void orderMenu(PrintWriter out) {
        try {
            List<SysPremisson> orderList = new ArrayList<>();
            List<SysPremisson> parentSysPremissonList = iMenuService.parentSysPremissonList();
            List<SysPremisson> childSysPremissonList = iMenuService.childSysPremissonList();
            for (SysPremisson parentSysPremisson :
                    parentSysPremissonList) {
                setChild(parentSysPremisson, childSysPremissonList, orderList);
            }
            out.print(WebUtils.responseData(orderList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setChild(SysPremisson parentSysPremisson, List<SysPremisson> list, List<SysPremisson> orderList) {
        orderList.add(parentSysPremisson);
        Integer parentId = parentSysPremisson.getPremissonId();
        List<SysPremisson> childs = new ArrayList<>();
        for (SysPremisson sysPremisson :
                list) {
            if (sysPremisson.getParentId().equals(parentId)) {
                childs.add(sysPremisson);
                setChild(sysPremisson, list, orderList);
            }
        }
        if (null != childs && 0 < childs.size()) {
            parentSysPremisson.setChilds(childs);
        }
    }

    @ResponseBody
    @ApiOperation(value = "获取用户菜单", responseContainer = "Map")
    @RequestMapping(value="user/list", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public List<SysPremisson> userList() {
        return sessionUtils.getUserSysPremissonList();
    }

    @ResponseBody
    @ApiOperation(value = "获取用户菜单", responseContainer = "Map")
    @RequestMapping(value="userlist/{sys_userId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void userList(@PathVariable Integer sys_userId, PrintWriter out) {
        try {
        		SysUser sysUser = new SysUser();
        		sysUser.setSys_userId(sys_userId);
        		List<SysPremisson> sysPremissonList =  iMenuService.userList(sysUser);
            out.print(WebUtils.responseData(sysPremissonList));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @ResponseBody
    @ApiOperation(value = "获取角色菜单", responseContainer = "Map")
    @RequestMapping(value="roleList/{sys_roleId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void roleList(@PathVariable Integer sys_roleId, PrintWriter out) {
        try {
        		SysUser sysUser = new SysUser();
        		sysUser.setSys_roleId(sys_roleId);
        		List<SysPremisson> sysPremissonList =  iMenuService.roleList(sysUser);
            out.print(WebUtils.responseData(sysPremissonList));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @ResponseBody
    @ApiOperation(value = "获取用户菜单对应按钮", responseContainer = "Map")
    @RequestMapping(value="/button", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public List<SysPremisson> button(SysPremisson sysPremisson) {
        try {
            return iMenuService.getChildSysPremissonList(sysPremisson.getPremissonId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}