package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.SysMapping;
import com.legal.app.model.SysRole;
import com.legal.app.service.ISysMappingService;
import com.legal.app.service.ISysRoleService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/system/role")
@Api(tags = "系统管理-权限设置")
public class SysRoleController extends SuperController{
    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysMappingService sysMappingService;

    @ResponseBody
    @ApiOperation(value = "列表", notes = "", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(SysRole sysRole) {
        Integer iDisplayLength = sysRole.getLength();
        int currentPage = sysRole.getStart() / iDisplayLength + 1;
        List<SysRole> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(sysRole.getLength(), currentPage, true);
        try {
            list = sysRoleService.list(sysRole, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<SysRole> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, sysRole.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody SysRole sysRole) {
        Integer iDisplayLength = sysRole.getLength();
        int currentPage = sysRole.getStart() / iDisplayLength + 1;
        List<SysRole> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(sysRole.getLength(), currentPage, true);
        try {
            list = sysRoleService.list(sysRole, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<SysRole> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, sysRole.getsEcho());
    }

	@ResponseBody
    @ApiOperation(value = "详情", responseContainer = "Map")
    @RequestMapping(value = "info/{sys_roleId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void info(@PathVariable Integer sys_roleId, PrintWriter out) {
        try {
        	SysRole sysRole = new SysRole();
        	sysRole.setSys_roleId(sys_roleId);
            SysRole info = sysRoleService.info(sysRole);
            List<SysMapping> mappingInfos = sysMappingService.info(sys_roleId);
            String sysMappings = "";
            if (null!=mappingInfos && !mappingInfos.isEmpty()) {
            	for (SysMapping mappingInfo : mappingInfos) {
            		String premissonId = mappingInfo.getPremissonId().toString();
            		sysMappings += premissonId+",";
            	}
                sysMappings=sysMappings.substring(0, sysMappings.length()-1);
            }
            info.setSysMappings(sysMappings);
            out.print(WebUtils.responseData(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "新增", notes = "{\n" +
            "  \"sys_roleId\": 0,\n" +
            "  \"sys_roleName\": \"1\",\n" +
            "  \"sys_state\": 0\n" +
            "  \"sys_desc\": \"1\",\n" +
            "  \"isDelete\": 0,\n" +
            "  \"sysMappings\": \"1,2,74,3,5,6\",\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void add(@RequestBody SysRole sysRole, PrintWriter out) {
        System.out.println("sysRole = " + sysRole);
        try {
            sysRoleService.add(sysRole);
            String sysMappings = sysRole.getSysMappings();
            SysRole info = sysRoleService.info(sysRole);
            SysMapping sysMapping = new SysMapping();
            sysMapping.setRoleId(info.getSys_roleId());
            if (null!=sysMappings && ""!=sysMappings) {
            	String[] premissonIds = sysMappings.split(",");
                for (String premissonId : premissonIds) {
                	sysMapping.setPremissonId(Integer.parseInt(premissonId));
                    sysMappingService.add(sysMapping);
                }
            }
            sysRole.setSys_state(0);
            CORSFilter.rolemappingList.clear();
            out.print(WebUtils.responseData(sysRole));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "编辑", notes = "{\n" +
            "  \"sys_roleId\": 0,\n" +
            "  \"sys_roleName\": \"1\",\n" +
            "  \"sys_state\": 0\n" +
            "  \"sys_desc\": \"1\",\n" +
            "  \"isDelete\": 0,\n" +
            "  \"sysMappings\": \"1,2,74,3,5,6\",\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void update(@RequestBody SysRole sysRole, PrintWriter out) {
        try {
            int result = sysRoleService.update(sysRole);
            sysMappingService.delete(sysRole.getSys_roleId());//删除原有的权限
            SysMapping sysMapping = new SysMapping();
            sysMapping.setRoleId(sysRole.getSys_roleId());
            String sysMappings = sysRole.getSysMappings();
            if (null!=sysMappings && ""!=sysMappings) {
            	String[] premissonIds = sysMappings.split(",");
                for (String premissonId : premissonIds) {
                	sysMapping.setPremissonId(Integer.parseInt(premissonId));
                    sysMappingService.add(sysMapping);
                }
            }
            CORSFilter.rolemappingList.clear();
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{sys_roleId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer sys_roleId, PrintWriter out) {
        try {
            int result = sysRoleService.remove(sys_roleId);
            CORSFilter.rolemappingList.clear();
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "禁用 1 成功", responseContainer = "Map")
    @RequestMapping(value = "disabled/{sys_roleId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void disabled(@PathVariable Integer sys_roleId, PrintWriter out) {
        try {
            int result = sysRoleService.disabled(sys_roleId);
            CORSFilter.rolemappingList.clear();
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "启用 1 成功", responseContainer = "Map")
    @RequestMapping(value = "enable/{sys_roleId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void enable(@PathVariable Integer sys_roleId, PrintWriter out) {
        try {
            int result = sysRoleService.enable(sys_roleId);
            CORSFilter.rolemappingList.clear();
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
