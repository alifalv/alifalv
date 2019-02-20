package com.legal.app.controller;

import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.Company;
import com.legal.app.model.Consultant;
import com.legal.app.model.Counselor;
import com.legal.app.model.Speciality;
import com.legal.app.model.User;
import com.legal.app.service.ICounselorService;
import com.legal.app.service.IUserService;
import com.legal.app.service.impl.CompanyService;
import com.legal.app.service.impl.ConsultantService;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@Api(value = "user", tags = "会员管理-会员列表")
public class UserController extends SuperController{
    @Autowired
    private IUserService userService;

    @Autowired
    private ConsultantService consultantService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ICounselorService counselorService;

    @Resource
    private AddressController addressController;

    @ResponseBody
    @ApiOperation(value = "获取所有会员 ", notes = "{\n" +
            "  \"iDisplayLength\": 1,\n" +
            "  \"iDisplayStart\": 1\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(User user) {
        List<User> list = new ArrayList<>();
        int iDisplayLength = user.getLength();
        int currentPage = user.getStart() / iDisplayLength + 1;
        System.out.println("user.getStart() = " + user.getStart());
        System.out.println("user.getLength() = " + user.getLength());
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(iDisplayLength, currentPage, true);
        try {
            list = userService.queryAll(user, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<User> pagingResult = new Paging<>();
        for (User city : list) {
            String cityDescription = AddressController.getCity(city.getCity());
            city.setCityDescription(cityDescription);
            if(city.getTypeState() != null && city.getUserType() != null) {
                if(city.getTypeState().equals("1") && city.getUserType().equals(1)) {
                		city.setUserType(2);
                }
            }
        }
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);
        return Pagination4Datatable.getInstanceMap(pagingResult, user.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "获取所有会员 ", notes = "{\n" +
            "  \"iDisplayLength\": 1,\n" +
            "  \"iDisplayStart\": 1\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "param", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable param(@RequestBody User user) {
        List<User> list = new ArrayList<>();
        int size = user.getLength();
        int currentPage = user.getStart() / size + 1;
        System.out.println("user.getStart() = " + user.getStart());
        System.out.println("user.getLength() = " + user.getLength());
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(size, currentPage, true);
        try {
            list = userService.queryAll(user, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<User> pagingResult = new Paging<>();
        for (User city : list) {
            String cityDescription = AddressController.getCity(city.getCity());
            city.setCityDescription(cityDescription);
            if(city.getTypeState() != null && city.getUserType() != null) {
                if(city.getTypeState().equals("1") && city.getUserType().equals(1)) {
                		city.setUserType(2);
                }
            }
        }
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        Integer iDisplayLength = user.getLength();
        pagingResult.setLimit(iDisplayLength);
        pagingResult.setStart(currentPage * iDisplayLength);
        pagingResult.setCurrentPage(currentPage);
        return Pagination4Datatable.getInstanceMap(pagingResult, user.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "获取会员信息（个人咨询者）", responseContainer = "Map")
    @RequestMapping(value = "info/consultant/{userId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void infoCounselor(@PathVariable int userId, PrintWriter out) {
        try {
            User info = userService.info(userId);
            Consultant consultant = new Consultant();
            consultant.setUserId(info.getUserId());
            consultant = consultantService.info(consultant);
            //info.setRealName(consultant.getRealName());
            out.print(WebUtils.responseData(consultant));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "获取会员信息（企业咨询者）", responseContainer = "Map")
    @RequestMapping(value = "info/company/{userId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void infoCompany(@PathVariable int userId, PrintWriter out) {
        try {
            User info = userService.info(userId);
            Company company = new Company();
            company.setUserId(info.getUserId());
            company = companyService.info(company);
            //info.setRealName(consultant.getRealName());
            out.print(WebUtils.responseData(company));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@ResponseBody
    @ApiOperation(value = "获取会员信息（咨询师信息）", responseContainer = "Map")
    @RequestMapping(value = "info/counselor/{userId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void infoConsultant(@PathVariable int userId, PrintWriter out) {
        try {
            User info = userService.info(userId);
            Counselor counselor =  new Counselor();
            counselor.setUserId(info.getUserId());
            counselor = counselorService.info(counselor);
            List<Speciality> list = counselorService.counselorSpeciality(userId);
//            String userSpeciality = "";
//            if(!list.isEmpty() && list!=null) {
//                for(Speciality speciality : list) {
//                		userSpeciality += SystemConfigUtil.getValue(speciality.getTypeId(), SystemConfigUtil.TYPE_CASE)+"、";
//                }
//                userSpeciality = userSpeciality.substring(0, userSpeciality.length()-1);
//            }
            counselor.setSpeciality(list);
            out.print(WebUtils.responseData(counselor));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@ResponseBody
    @ApiOperation(value = "修改会员信息", responseContainer = "Map")
    @RequestMapping(value = "updateInfo/{userType}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void updateInfo(@PathVariable int userType, HttpServletRequest request,PrintWriter out) {
        try {
            Map<?, ?> map = getMap(request);
            int result = 0;
            if(userType==1) {
            		result = consultantService.update(map);
            }else if(userType==2) {
            		counselorService.deleteSpeciality(Integer.parseInt((String) map.get("userId")));
            		String[] speciality = (String[]) map.get("speciality[]");
            		if(speciality==null) {
            			
            		}else {
            			if(speciality.length>0) {
        					Speciality info = new Speciality();
        					info.setUserId(Integer.parseInt((String) map.get("userId")));
            				for(String typeId : speciality) {
            					info.setTypeId(Integer.parseInt(typeId));
            					counselorService.addSpeciality(info);
            				}
            			}
            		}
        			result = counselorService.update(map);
            }else if(userType==3) {
        			result = companyService.update(map);
            }
            if(result==1) {
                out.print(WebUtils.responseData(map));
            }else {
                out.print(WebUtils.responseError("修改失败", -1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "审核 1 审核通过", responseContainer = "Map")
    @RequestMapping(value = "check/{userId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void check(@PathVariable int userId, PrintWriter out) {
        try {
            int result = userService.check(userId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "审核 2 审核不通过", responseContainer = "Map")
    @RequestMapping(value = "uncheck/{userId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void uncheck(@PathVariable int userId, PrintWriter out) {
        try {
            int result = userService.uncheck(userId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "禁用", responseContainer = "Map")
    @RequestMapping(value = "disabled/{userId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void disabled(@PathVariable int userId, PrintWriter out) {
        try {
            int result = userService.disabled(userId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "启用", responseContainer = "Map")
    @RequestMapping(value = "enable/{userId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void enable(@PathVariable int userId, PrintWriter out) {
        try {
            int result = userService.enable(userId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "签约", responseContainer = "Map")
    @RequestMapping(value = "contract/{userId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void contract(@PathVariable int userId, PrintWriter out) {
        try {
            int result = userService.contract(userId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "取消签约", responseContainer = "Map")
    @RequestMapping(value = "uncontract/{userId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void unContract(@PathVariable int userId, PrintWriter out) {
        try {
            int result = userService.unContract(userId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "推荐", responseContainer = "Map")
    @RequestMapping(value = "push/{userId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void push(@PathVariable int userId, PrintWriter out) {
        try {
            int result = userService.push(userId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "撤销", responseContainer = "Map")
    @RequestMapping(value = "unpush/{userId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void unPush(@PathVariable int userId, PrintWriter out) {
        try {
            int result = userService.unPush(userId);
            out.print(WebUtils.responseData(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
