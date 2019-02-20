package com.legal.app.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.common.log.BusinessException;
import com.common.log.ExceptionLogger;
import com.common.utils.MD5Util;
import com.common.utils.StringUtil;
import com.common.utils.SystemConfigUtil;
import com.common.utils.SystemUtil;
import com.legal.app.controller.AddressController;
import com.legal.app.controller.model.Address;
import com.legal.app.controller.model.City;
import com.legal.app.model.ConselorInfo;
import com.legal.app.model.User;
import com.legal.app.service.AccessLogService;
import com.legal.app.service.UserService;
import com.legal.app.utils.SendArticleType;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class UserServiceImpl extends DaoMybatisImpl<User> implements UserService{
	@Autowired
	private AccessLogService accSer;
	
	@Override
	public int updateCounselorBalance(int userId, Double money) throws Exception {
		 Map m = new HashMap<String,Object>();
		 m.put("userId",userId);
		 m.put("userBalance", money);
		 super.executeUpdate("updateCounselorMakeNumInfo", m);
		 return super.executeUpdate("updateCounselorBalance", m);
	}

	@Override
	public Map findCounselorByUserId(int userId) throws Exception {
		return  (Map<String, Object>)super.executeQueryOne("findCounselorByUserIdForOrder", userId);
	}

	@Override
	public Map<String, Object> userLogin(String userName, String password)
			throws Exception {
		
		ExceptionLogger.writeLog("用户登录，用户名："+userName);
		
		Map<String, Object> userInfo = (Map<String, Object>) super.executeQueryOne("findUserByUserNameOrMobile", userName);
		
		if(userInfo!=null){
			if(!userInfo.get("userPwd").toString().equals(MD5Util.MD5(password))){
				throw new BusinessException("账号或密码错误，请检查后重试", -1);
			}
			//获取用户其他信息
			int userType = (int)userInfo.get("userType");
			Map<String, Object> otherInfo = null;
			if(userType == 1){
				otherInfo = (Map<String, Object>) super.executeQueryOne("findConsultantByUserId", userInfo.get("userId"));
			}else if(userType == 2){
				otherInfo = (Map<String, Object>) super.executeQueryOne("findCounselorByUserId", userInfo.get("userId"));
			}else if(userType == 3){
				otherInfo = (Map<String, Object>) super.executeQueryOne("findCompanyByUserId", userInfo.get("userId"));
			}
			
			userInfo.putAll(otherInfo);
		}else{
			throw new BusinessException("账号或密码错误，请检查后重试", -1);
		}
		return userInfo;
	}

	@Override
	public int consultantRegister(Map<String, Object> paramters)
			throws Exception {
		ExceptionLogger.writeLog("个人咨询者注册");
		
		/*int f = (int)super.executeQueryOne("findUserCountByUserName", paramters.get("userName"));
		
		if(f>0){
			throw new BusinessException("此用户名已注册过.", -1);
		}
		
		f = (int)super.executeQueryOne("findUserCountByMobile", paramters.get("mobile"));
		wenshu
		if(f>0){
			throw new BusinessException("此手机号码已注册过.", -1);
		}*/
		//添加统一登录信息
		super.executeUpdate("addCommonUser", paramters);
		//添加个人咨询者信息
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("userId", paramters.get("userId"));
		m.put("adviceNum", 0);//咨询数量
		m.put("followingNum", 0);
		m.put("collectionNum", 0);
		m.put("freeNum", SystemUtil.getFreeNum());//免费咨询次数
		super.executeUpdate("addConsultantInfo", m);
		return 1;
	}

	@Override
	public int openVipByConsultant(Map<String, Object> paramters)
			throws Exception {
		ExceptionLogger.writeLog("个人咨询者提交认证资料");
		//更新个人咨询者信息
		super.executeUpdate("editConsultantInfo", paramters);
		//修改会员时间信息
		//super.executeUpdate("editUserVipTime", paramters);
		//更新个人咨询者信息
 		paramters.put("isAuthentication", "2");
		super.executeUpdate("editUserInfo", paramters);
		return 1;
	}

	@Override
	public int companyRegister(Map<String, Object> paramters) throws Exception {
		
		/*int f = (int)super.executeQueryOne("findUserCountByUserName", paramters.get("userName"));
		
		if(f>0){
			throw new BusinessException("此用户名已注册过.", -1);
		}
		
		f = (int)super.executeQueryOne("findUserCountByMobile", paramters.get("mobile"));
		
		if(f>0){
			throw new BusinessException("此手机号码已注册过.", -1);
		}*/
		//添加统一登录信息
		super.executeUpdate("addCommonUser", paramters);
		//addCompanyInfo
		//添加个人咨询者信息
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("userId", paramters.get("userId"));
		m.put("adviceNum", 0);//咨询数量
		m.put("followingNum", 0);
		m.put("collectionNum", 0);
		m.put("freeNum", "0");//免费咨询次数
		m.put("companyCode", paramters.get("companyCode"));
		m.put("companyName", paramters.get("companyName"));
		super.executeUpdate("addCompanyInfo", m);
		return 1;
	}

	@Override
	public int openVipByCompany(Map<String, Object> paramters) throws Exception {
		
		ExceptionLogger.writeLog("企业咨询者开通会员");
		//修改会员时间信息
		super.executeUpdate("editUserVipTime", paramters);
		paramters.put("isAuthentication", "1");
		super.executeUpdate("editUserInfo", paramters);
		return 1;
	}

	@Override
	public int companyAuthentication(Map<String, Object> paramters)
			throws Exception {
		ExceptionLogger.writeLog("企业咨询者认证信息保存...");
		super.executeUpdate("editCompany", paramters);
		paramters.put("isAuthentication", "1");
		super.executeUpdate("editUserInfo", paramters);
		return 1;
	}

	@Override
	public int counselorRegister(Map<String, Object> paramters)
			throws Exception {
		ExceptionLogger.writeLog("咨询师注册...");
		
		/*int f = (int)super.executeQueryOne("findUserCountByUserName", paramters.get("userName"));
		
		if(f>0){
			throw new BusinessException("此用户名已注册过.", -1);
		}
		
		f = (int)super.executeQueryOne("findUserCountByMobile", paramters.get("mobile"));
		
		if(f>0){
			throw new BusinessException("此手机号码已注册过.", -1);
		}*/
		
		//添加统一登录信息
		super.executeUpdate("addCommonUser", paramters);
		//addCompanyInfo
		//添加个人咨询者信息
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("userId", paramters.get("userId"));
		m.put("adviceNum", 0);//咨询数量
		m.put("followingNum", 0);
		m.put("collectionNum", 0);
		m.put("collectionNum", 0);
		m.put("freeNum", SystemUtil.getFreeNum());//免费咨询次数
		m.put("makeNum", 0);
		m.put("readNum", 0);
		m.put("goodNum", 0);
		m.put("complateAdviceNum", 0);
		m.put("entrustNum", 0);
		m.put("levelScore", SystemUtil.getBasicScore());//水平
		m.put("attitudeScore", SystemUtil.getBasicScore());//服务
		m.put("sourceScore", SystemUtil.getBasicScore());//社会资源
		m.put("chargeScore", SystemUtil.getBasicScore());//收费
		m.put("commonScore", SystemUtil.getBasicScore());//综合
		m.put("goodNum", 0);//好评个数
		m.put("middleNum", 0);//中评个数
		m.put("badNum", 0);//差评个数
		m.put("userScore", 0);//积分
		m.put("userBalance", 0);//余额
		m.put("job", paramters.get("job"));
		m.put("province", paramters.get("province"));//省份
		m.put("city", paramters.get("city"));//城市
		super.executeUpdate("addCounselorInfo", m);
		super.executeUpdate("addCounselorCaseList", paramters);
		return 1;
	}
	
	

	@Override
	public int updateCounselor(Map map) throws Exception {
		return super.executeUpdate("counselorAuthentication", map);
	}

	@Override
	public int counselorAuthentication(Map<String, Object> paramters)
			throws Exception {
		ExceptionLogger.writeLog("保存咨询师认证信息");
		//保存添加信息
		super.executeUpdate("counselorAuthentication", paramters);
		//删除  咨询师专长信息
		super.executeUpdate("deleteCasesByUserId", paramters.get("userId"));
		//添加更新后的案件专长
		super.executeUpdate("addCounselorCaseList", paramters);
		paramters.put("isAuthentication", "1");
		paramters.put("userImg",paramters.get("personImg"));
		super.executeUpdate("editUserInfo", paramters);
		return 0;
	}

	@Override
	public int authenticateCounselor(Map<String, Object> paramters)
			throws Exception {
		ExceptionLogger.writeLog("咨询者转变成咨询师认证信息");
		List counselorInfo = (List) super.executeQuery("findCounselorByUserId", paramters);
		if(counselorInfo.isEmpty() || counselorInfo==null) {
			//添加咨询师信息
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("userId", paramters.get("userId"));
			m.put("adviceNum", 0);//咨询数量
			m.put("followingNum", 0);
			m.put("collectionNum", 0);
			m.put("collectionNum", 0);
			m.put("freeNum", SystemUtil.getFreeNum());//免费咨询次数
			m.put("makeNum", 0);
			m.put("readNum", 0);
			m.put("goodNum", 0);
			m.put("complateAdviceNum", 0);
			m.put("entrustNum", 0);
			m.put("levelScore", SystemUtil.getBasicScore());//水平
			m.put("attitudeScore", SystemUtil.getBasicScore());//服务
			m.put("sourceScore", SystemUtil.getBasicScore());//社会资源
			m.put("chargeScore", SystemUtil.getBasicScore());//收费
			m.put("commonScore", SystemUtil.getBasicScore());//综合
			m.put("goodNum", 0);//好评个数
			m.put("middleNum", 0);//中评个数
			m.put("badNum", 0);//差评个数
			m.put("userScore", 0);//积分
			m.put("userBalance", 0);//余额
			m.put("job", paramters.get("job"));
			m.put("province", paramters.get("province"));//省份
			m.put("city", paramters.get("city"));//城市
			super.executeUpdate("addCounselorInfo", m);
		}
		//保存添加信息
		super.executeUpdate("counselorAuthentication", paramters);
		//删除  咨询师专长信息
		super.executeUpdate("deleteCasesByUserId", paramters.get("userId"));
		//添加更新后的案件专长
		super.executeUpdate("addCounselorCaseList", paramters);
		paramters.put("isAuthentication", "1");
		paramters.put("typeState", "1");//转变状态
		paramters.put("userImg",paramters.get("personImg"));
		super.executeUpdate("editUserInfo", paramters);
		return 0;
	}

	
	
	@Override
	public List listCounselor(Map map) throws Exception { 
		return super.executeQuery("listCounselor",map);
	}

	@Override
	public List<Map>  findUserCompanyInfo(Map map) throws Exception { 
		return  super.executeQuery("findUserCompanyInfo", map);
	}

	@Override
	public Map<String, Object> getUserInfo(int userId) throws Exception {
		
		ExceptionLogger.writeLog("获取用户信息，userId="+userId);
		
		//获取用户信息
		Map<String, Object> user = (Map<String, Object>)super.executeQueryOne("findUserByUserId", userId);
		//移除密码
		user.remove("userPwd");
		
		int userType = (int)user.get("userType");
		Map<String, Object> otherInfo = null;
		if(userType == 1){
			otherInfo = (Map<String, Object>) super.executeQueryOne("findConsultantByUserId", userId);
			if(null!=otherInfo.get("province")){
			String provinceName="";
			Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+otherInfo.get("province").toString());
			provinceName = add.name;
			otherInfo.put("provinceName", provinceName);
			Map cityM = add.getCity();
			City city = (City) cityM.get(SystemConfigUtil.TYPE_CITY+ otherInfo.get("city").toString());
			String cityName = city.getCityName();
			otherInfo.put("cityName", cityName);}
			
		}else if(userType == 2){
			otherInfo = (Map<String, Object>) super.executeQueryOne("findCounselorByUserId", userId);
			List<Map> specialityList = super.executeQuery("findCasesByUserId", userId);
			//咨询师案件专长
			for(int j=0;j<specialityList.size();j++){
				specialityList.get(j).put("typeName", SystemConfigUtil.getValue(Integer.parseInt(specialityList.get(j).get("typeId").toString()), SystemConfigUtil.TYPE_CASE));
			}
			otherInfo.put("specialityList", specialityList);
			if(null!=otherInfo.get("province")){
				String provinceName="";
				Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+otherInfo.get("province").toString());
				provinceName = add.name;
				otherInfo.put("provinceName", provinceName);
				if(null!=otherInfo.get("city")) {
					Map cityM = add.getCity();
					City city = (City) cityM.get(SystemConfigUtil.TYPE_CITY+ otherInfo.get("city").toString());
					String cityName = city.getCityName();
					otherInfo.put("cityName", cityName);
				}else {
					otherInfo.put("cityName", "");
				}
			}else {
				otherInfo.put("provinceName", "");
			}
		}else if(userType == 3){
			otherInfo = (Map<String, Object>) super.executeQueryOne("findCompanyByUserId", userId);
			if(null!=otherInfo.get("province")){
			String provinceName="";
			Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+otherInfo.get("province").toString());
			provinceName = add.name;
			otherInfo.put("provinceName", provinceName);
			Map cityM = add.getCity();
			City city = (City) cityM.get(SystemConfigUtil.TYPE_CITY+ otherInfo.get("city").toString());
			String cityName = city.getCityName();
			otherInfo.put("cityName", cityName);
			}
		}
		user.putAll(otherInfo);
		
		String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());	
 
		int isExpire = 0;  //0:正常会员 1：过期会员 2 不是会员
 		 if(null!=user.get("expireTime")){
 			 if(user.get("expireTime").toString().compareTo(now)<0){
 				 isExpire=1;
 			 }
		 }else{
				 isExpire=2;
		 }
		 user.put("isExpire", isExpire);
		 return user;
	}
	
	@Override
	public Map getUserInfo(String userId) throws Exception {
		 List<Map> list =  super.executeQuery("getUserInfo",userId);
		 if(null == list || list.size() == 0) {
			 return null;
		 }else {
			 return list.get(0);
		 }
	}
	
	
	
	@Override
	public Map getSysUserInfo(String userId) throws Exception {
	  List<Map> list = super.executeQuery("getSysUserInfo",userId);
	  if(null == list || list.size() == 0) {
			 return null;
		 }else {
			 return list.get(0);
		 }
	}

	@Override
	public Map<String, Object> findUserBasicInfoByUserId(int userId) throws Exception {
		return (Map<String, Object>) super.executeQueryOne("findUserBasicInfoByUserId", userId);
	}
	@Override
	public int findFreeNumByUserId(int userId) throws Exception {
		return (int)super.executeQueryOne("findFreeCount", userId);
	}
	
	
	
	
	@Override
	public int updateFree(int userId, int userType,int   freeNum ) throws Exception {
		int num =0;
		if(userType == 1) {
			num=this.updateFreeNum(userId,freeNum);
		}else if(userType ==2) {
			num =this.updateCounselorFreeCount(userId,freeNum);
		}else {
			num =this.updateCompanyFreeCount(userId,freeNum);
		}
		return num;
	}

	@Override
	public int updateFreeNum(int userId,int freeNum) throws Exception { 
		Map m = new HashMap<String,Object>();
		m.put("userId", userId);
		m.put("freeNum", freeNum);
		return super.executeUpdate("updateFreeCount", m);
	}
	

	@Override
	public int updateCompanyFreeCount(int userId,int freeNum) throws Exception { 
		Map m = new HashMap<String,Object>();
		m.put("userId", userId);
		m.put("freeNum", freeNum);
		return super.executeUpdate("updateCompanyFreeCount", m);
	}

	@Override
	public int updateCounselorFreeCount(int userId,int freeNum) throws Exception {
		Map m = new HashMap<String,Object>();
		m.put("userId", userId);
		m.put("freeNum", freeNum);
		return super.executeUpdate("updateCounselorFreeCount", m);
	}

	@Override
	public Timestamp findUserVipOutTime(int userId) throws Exception {
		return null;
	}

	
	
	@Override
	public int addBrowse(Map map) throws Exception { 
		Map accessMap = null; //访问对象；
		Map byAccessMAp = null;//被访问对象；
		
		
		//查询记录；
		List<Map> list =  accSer.queryVisitAndBeVisited(map);
		if(null != list && list.size() > 0) {
			Map mapL = list.get(0);
			Object visit = mapL.get("visit");
			Object beVisited = mapL.get("beVisited");
			if(visit.toString().equals("0")) {
				accessMap = map;
			}
			if(beVisited.toString().equals("0")) {
				byAccessMAp = new HashMap<String,Object>();
				byAccessMAp.put("userId",map.get("counselorId"));
				byAccessMAp.put("counselorId",map.get("userId"));
			}
		}else {
			accessMap = map;
			byAccessMAp = new HashMap<String,Object>();
			byAccessMAp.put("userId",map.get("counselorId"));
			byAccessMAp.put("counselorId",map.get("userId"));
		}
		if(null != accessMap) {
			super.executeUpdate("addBrowse", accessMap);
		}
		if(null !=byAccessMAp ) {
			super.executeUpdate("addBrowse", byAccessMAp);
		}
	    return 1;
	}

	@Override
	public List<Map> findBrowseList(Paging p,Map map) throws Exception {
		List<Map> listMap = super.executeQuery("findBrowseList",p,map);
		  //的内容擅长转换为文字；
		  if(null !=listMap && listMap.size() > 0 ) {
			  for(int i = 0 ; i < listMap.size(); i ++) {
				  Object obj = listMap.get(i);
				  if(obj instanceof Map) {
					  Map m = (Map)obj;
					  m.remove("password");
					  Object specialityType_tmp =  m.get("specialityType");
					  List<String> type = new ArrayList<String>();
					  if(null != specialityType_tmp) {
						  String [] arr = specialityType_tmp.toString().split(","); 
						  for(int b = 0 ; b < arr.length; b++) {
							  String typeName = SystemConfigUtil.getValue(Integer.valueOf(arr[b]), SystemConfigUtil.TYPE_CASE);
							  type.add(typeName);
						  }
					  }
					  m.put("specialityTypeName", type);
					  m.remove("specialityType");
				  }
			  }
		  }
		return listMap;
	}

	@Override
	public List<Map> findFollowList(int userId) throws Exception { 
		return super.executeQuery("findFollowList", userId);
	}

	@Override
	public List<Map> findMyAdviceConsultant(Paging p,Map userMap) throws Exception {
	  List<Map> listMap = super.executeQuery("findMyAdviceConsultant", p,userMap);
	  //的内容擅长转换为文字；
	  if(null !=listMap && listMap.size() > 0 ) {
		  for(int i = 0 ; i < listMap.size(); i ++) {
			  Object obj = listMap.get(i);
			  if(obj instanceof Map) {
				  Map m = (Map)obj;
				  m.remove("password");
				  Object specialityType_tmp =  m.get("specialityType");
				  List<String> type = new ArrayList<String>();
				  if(null != specialityType_tmp) {
					  String [] arr = specialityType_tmp.toString().split(","); 
					  for(int b = 0 ; b < arr.length; b++) {
						  String typeName = SystemConfigUtil.getValue(Integer.valueOf(arr[b]), SystemConfigUtil.TYPE_CASE);
						  type.add(typeName);
					  }
				  }
				  m.put("specialityTypeName", type);
				  m.remove("specialityType");
			  }
		  }
	  }
	  return listMap;
	}

	@Override
	public int bindThreeInfo(Map map) throws Exception {
		super.executeUpdate("updateThreeInfo",map);
		if(map.get("openType").toString().equals("WEIBO")){
			super.executeUpdate("addThreeInfo_weibo", map);
			map.put("isWeiBo", "1");
			super.executeUpdate("updateUseThreeLoginrWeiBo", map);
		}else if(map.get("openType").toString().equals("WECHAT")){
			 super.executeUpdate("addThreeInfo_wechat", map);
			  map.put("isWeChat", "1");
			  super.executeUpdate("updateUseThreeLoginrWeChat", map);
		}else if(map.get("openType").toString().equals("QQ")){
			super.executeUpdate("addThreeInfo_qq", map);
			  map.put("isQQ", "1");
			  super.executeUpdate("updateUseThreeLoginrQQ", map);
		}
		
		return 1;
	}
	
	@Override
	public int addCompanyInfo(Map map) throws Exception {
		if(map.get("type").toString().equals("1")){ 
			 List<Map> list =  super.executeQuery("getUserInfo",map.get("userId").toString());
			String isAuthentication = list.get(0).get("isAuthentication").toString();
			if(isAuthentication.equals("2")){ //如果认证了只修改公司网址，公司规模 否者修改传入字段
				Map m = new HashMap<String, String>();
				m.put("companyUrl", map.get("companyUrl").toString());
				m.put("companySize", map.get("companySize").toString());
				m.put("companyDesc", map.get("companyDesc").toString());
				super.executeUpdate("editCompany", m);
			}else{
				super.executeUpdate("editCompany", map);
			}
		}else{
			Object obj = super.executeQueryOne("getCompanyInfo", map.get("userId").toString());
			if(obj!=null){
				super.executeUpdate("editcompanyInfo", map);
			}else{
				super.executeUpdate("addUserCompanyInfo", map);
			}
		}
		return 1;
	}

	@Override
	public Map getCompanyInfo(int userId) throws Exception {
		Map map = null;
		Object obj = super.executeQueryOne("getCompanyInfo", String.valueOf(userId));
		if(obj!=null){
			map = (Map)obj;
			//注入省份  城市 名称
			map.put("provinceName", AddressController.provinceMap.get("province"+map.get("province")));
			map.put("cityName", AddressController.cityMap.get("city"+map.get("city")));
		}
		
		return map;
	}

	@Override
	public Map findResumeInfoByUserId(int userId) throws Exception {
		
		ExceptionLogger.writeLog("获取简历信息:"+userId);
		
		Map m = null;
		
		Object obj = super.executeQueryOne("findResumeInfoByUserId", userId);
		if(obj!=null){
			m = (Map)obj;
		}
		
		return m;
	}

	@Override
	public int deliverResume(Map map) throws Exception {
		
		ExceptionLogger.writeLog("用户投递简历");
		
		return super.executeUpdate("deliverResume", map);
	}

	@Override
	public int addResumeInfo(Map map) throws Exception {
		ExceptionLogger.writeLog("保存用户简历信息："+map);
		//添加简历信息
		super.executeUpdate("addResumeInfo", map);
		//添加用户工作经历信息
		if(map.containsKey("experienceList")){
			super.executeUpdate("addWorkExperience", map.get("experienceList"));
		}
		
		//添加用户教育经历信息
		if(map.containsKey("eduList")){
			System.out.println(map.get("eduList"));
			super.executeUpdate("addEdu", map.get("eduList"));
		}
		
		//添加用户简历上传资料
		if(map.containsKey("files")){
			super.executeUpdate("addResumeFile", map.get("files"));
		}
		
		return 1;
	}

	@Override
	public int getUserIdByPersonUrl(String url) throws Exception {
		
		ExceptionLogger.writeLog("根据个人主页地址获取用户编号："+url);
		
		Object obj = super.executeQueryOne("getUserIdByPersonUrl", url);
		
		int userId = 0;
		
		if(obj!=null){
			userId = (int)obj;
		}
		
		return userId;
	}

	@Override
	public int changeUserProfile(Map map) throws Exception {
		ExceptionLogger.writeLog("用户更改头像");
		return super.executeUpdate("changeUserProfile", map);
	}

	@Override
	public int updateUserPushCode(Map map) throws Exception {
		
		if(!map.get("pushCode").toString().equals("pc")){
			super.executeQueryOne("updateUserPushCode",map);
		}else{
			//获取用户推送码
			Object obj = super.executeQueryOne("getPushCodeByUserId",map.get("userId"));
			
			if(obj==null||obj.toString().equals("")){
				super.executeQueryOne("updateUserPushCode",map);
			}
		}
		
		return 1;
	}

	@Override
	public int getThreeLoginFlag(Map map) throws Exception {
		ExceptionLogger.writeLog("获取第三方凭证"); 
		
		int userId = 0;
		
		Object obj = super.executeQueryOne("getThreeLoginFlag", map);
		if(obj!=null){
			userId = (int)obj;
		}
		
		return userId;
	}
	
	 

	@Override
	public Map getResumeInfoByUserId(int userId) throws Exception {
		ExceptionLogger.writeLog("获取用户简历信息："+userId);
		Object obj = super.executeQueryOne("getResumeInfoByUserId", userId);
		Map<String,Object> m = (Map<String,Object>)obj;

		if(obj==null){
			//throw new BusinessException("缺失用户简历信息", -1);
			return m;
		}
		//Map<String,Object> m = (Map<String,Object>)obj;
		m.put("qualificationName", getQualification(m.get("qualification").toString()));
		m.put("salName", SystemConfigUtil.getValue(Integer.parseInt(m.get("sal").toString()), SystemConfigUtil.TYPE_SAL));
		m.put("workExperienceName", SystemConfigUtil.getValue(Integer.parseInt(m.get("workExperience").toString()), SystemConfigUtil.TYPE_EXPERIENCE));
	
		
		String provinceName="";
		Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+m.get("province").toString());
		provinceName = add.name;
		m.put("provinceName", provinceName);
		Map cityM = add.getCity();
		City city = (City) cityM.get(SystemConfigUtil.TYPE_CITY+ m.get("city").toString());
		String cityName = city.getCityName();
		m.put("cityName", cityName);
		//获取用户  工作经历 getExperienceListByUserId
		List<Map> experienceList = super.executeQuery("getExperienceListByUserId",userId);
		m.put("experienceList", experienceList);
		
		//获取用户教育经历 getEduListByUserId
		List<Map> eduList = super.executeQuery("getEduListByUserId",userId);
		
		for(int i=0;i<eduList.size();i++){
			eduList.get(i).put("qualificationName", getQualification(eduList.get(i).get("qualification").toString()));
		
		}
		m.put("eduList", eduList);
		
		//获取用户简历附件信息
		List<Map> fileList = super.executeQuery("getResumeFileListByUserId",userId);
		m.put("fileList", fileList);
		
		
		return m;
	}

	@Override
	public int editPassword(Map map) throws Exception {
		ExceptionLogger.writeLog("修改密码");
		//获取用户原密码
		String oldPwd = super.executeQueryOne("getPasswordByUserId", map.get("userId")).toString();
		
		if(MD5Util.MD5(map.get("oldPassword").toString()).equals(oldPwd)){
			//新密码加密
			String newPassword = MD5Util.MD5(map.get("newPassword").toString());
			map.put("newPassword", newPassword);
			super.executeUpdate("editPassword", map);
		}else{
			throw new BusinessException("原密码不正确.", -1);
		}
		return 1;
	}

	@Override
	public int editMobile(Map map) throws Exception {
		ExceptionLogger.writeLog("重新绑定手机");
		return super.executeUpdate("editMobile", map);
	}

	@Override
	public int editPasswordByMobile(Map map) throws Exception {
		ExceptionLogger.writeLog("根据手机号码修改密码");
		//新密码  加密
		map.put("newPassword",MD5Util.MD5(map.get("newPassword").toString()));
		return super.executeUpdate("editPasswordByMobile", map);
	}

	@Override
	public List<Map>  voucherList(Map map,Paging paging) throws Exception {
		List<Map> eduList = super.executeQuery("getVoucherListByUserId",paging,map);
		return eduList;
	}

	@Override
	public int updateUserInfo(Map userMap) throws Exception {
		return super.executeUpdate("updateUserInfo", userMap);	
	}

	@Override
	public int insertCounselorInfo(Map userMap) throws Exception  {
		return super.executeUpdate("insertCounselorInfo", userMap);
		
	}

	@Override
	public List<Map> myCollectionConsultant(Paging p,Map userMap) throws Exception{
		List<Map> listMap = super.executeQuery("getMyCollectionConsultant",p,userMap);
		 
		
		/**
		for(int i=0;i<list.size()-1;i++){
			ExceptionLogger.writeLog("城市:"+list.get(i).get("city").toString()+"省："+list.get(i).get("province").toString());
			String provinceName="";
			Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+list.get(i).get("province").toString());
			provinceName = add.name;
			Map cityM = add.getCity();
			City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+list.get(i).get("city").toString());
			String cityName = city.getCityName();
			list.get(i).put("provinceName", provinceName);
			list.get(i).put("cityName", cityName);
			list.get(i).put("jobName", SystemConfigUtil.getValue(Integer.parseInt(list.get(i).get("job").toString()), SystemConfigUtil.TYPE_JOB));
			
			Map m = new HashMap<String, String>();
			m.put("userId", list.get(i).get("userId"));
			List<Map> specialityList = super.executeQuery("specialityList",m);
			for(int j=0;j<specialityList.size();j++){
				specialityList.get(j).put("typeName", SystemConfigUtil.getValue(Integer.parseInt(specialityList.get(j).get("typeId").toString()), SystemConfigUtil.TYPE_CASE));
			}
			list.get(i).put("specialityName", specialityList);
		}
			return list;
		**/
	
		//的内容擅长转换为文字；
		  if(null !=listMap && listMap.size() > 0 ) {
			  for(int i = 0 ; i < listMap.size(); i ++) {
				  Object obj = listMap.get(i);
				  if(obj instanceof Map) {
					  Map m = (Map)obj;
					  m.remove("password");
					  Object specialityType_tmp =  m.get("specialityType");
					  List<String> type = new ArrayList<String>();
					  if(null != specialityType_tmp) {
						  String [] arr = specialityType_tmp.toString().split(","); 
						  for(int b = 0 ; b < arr.length; b++) {
							  String typeName = SystemConfigUtil.getValue(Integer.valueOf(arr[b]), SystemConfigUtil.TYPE_CASE);
							  type.add(typeName);
						  }
					  }
					  m.put("specialityTypeName", type);
					  m.remove("specialityType");
				  }
			  }
		  }
		  return listMap;
		
 	}

	@Override
	public List<Map> collectionResumeList(Paging paging ,int userId) throws Exception{
		Map map = new HashMap<String, ArrayList<Map>>();
		map.put("userId", userId);
		List<Map> collectionResumeList = super.executeQuery("collectionResumeList",paging,map);
		for(int i=0;i<collectionResumeList.size()-1;i++){
			ExceptionLogger.writeLog("城市:"+collectionResumeList.get(i).get("city").toString()+"省："+collectionResumeList.get(i).get("province").toString());
			String provinceName="";
			Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+collectionResumeList.get(i).get("province").toString());
			provinceName = add.name;
			Map cityM = add.getCity();
			City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+collectionResumeList.get(i).get("city").toString());
			String cityName = city.getCityName();
			collectionResumeList.get(i).put("provinceName", provinceName);
			collectionResumeList.get(i).put("cityName", cityName);
		}
		return collectionResumeList;
	}

	@Override
	public int sendJob(Map mapJob) throws Exception {
		return  super.executeUpdate("sendJob", mapJob);   
	}

	@Override
	public List<Map> collectionJobList(int userId, Paging paging) throws Exception {
		Map map = new HashMap<String, ArrayList<Map>>();
		map.put("userId", userId);
		List<Map> collectionJobList = super.executeQuery("collectionJobList",paging,map);
		return collectionJobList;
	}
 
	
	@Override
	public List<Map> collecResumeList(int userId, Paging paging) throws Exception {
		Map map = new HashMap<String, String>();
		map.put("userId", userId);
		List<Map> collecResumeList = super.executeQuery("collecResumeList",paging,map);
		for(int i=0;i<collecResumeList.size()-1;i++){
			ExceptionLogger.writeLog("城市:"+collecResumeList.get(i).get("city").toString()+"省："+collecResumeList.get(i).get("province").toString());
			String provinceName="";
			Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+collecResumeList.get(i).get("province").toString());
			provinceName = add.name;
			Map cityM = add.getCity();
			City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+collecResumeList.get(i).get("city").toString());
			String cityName = city.getCityName();
			collecResumeList.get(i).put("qualificationName", getQualification(collecResumeList.get(i).get("qualification").toString()));
			collecResumeList.get(i).put("provinceName", provinceName);
			collecResumeList.get(i).put("cityName", cityName);
		}
		return collecResumeList;
	}

	@Override
	public Map<String, Object>  businessOrderList(Map map) throws Exception {
		List<Map> businessOrderList = super.executeQuery("businessOrderList",map);
		Map m = new HashMap<String, ArrayList<Map>>();
		m.put("fileList", businessOrderList);
		return m;
	}

	@Override
	public List<Map> newJobList(Paging paging,Map map) throws Exception {
		List<Map> seacherJobDetailList = super.executeQuery("newJobList",paging,map);
		for(int i=0;i<seacherJobDetailList.size()-1;i++){
			seacherJobDetailList.get(i).put("salName", SystemConfigUtil.getValue(Integer.parseInt(seacherJobDetailList.get(i).get("sal").toString()), SystemConfigUtil.TYPE_SAL));
 			String provinceName="";
			Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+seacherJobDetailList.get(i).get("province").toString());
			provinceName = add.name;
			Map cityM = add.getCity();
			City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+seacherJobDetailList.get(i).get("city").toString());
			String cityName = city.getCityName();
			seacherJobDetailList.get(i).put("provinceName", provinceName);
			seacherJobDetailList.get(i).put("cityName", cityName);
			Map m1 = new HashMap<String, String>();
			m1.put("userId", map.get("loginUserId"));
			m1.put("advertiseId", seacherJobDetailList.get(i).get("advertiseId"));
			Map m2 = (Map) super.executeQueryOne("seacherisCollectionJob",m1);
			if(m2!=null){
				seacherJobDetailList.get(i).put("collectionId", m2.get("collectionId"));
			}else{
				seacherJobDetailList.get(i).put("collectionId", "0");
			}
		}
		return seacherJobDetailList;
	}

	@Override
	public int freeCaseDeclare(String userId) throws Exception { 
		Map map = (Map)super.executeQueryOne("getConsultantOrCounselorInfo", userId);
		String driverName= "";
		if(null!=map){
			driverName = SystemConfigUtil.getValue(Integer.parseInt(map.get("carType").toString()),SystemConfigUtil.TYPE_DRIVER);
		}
		Map m = new HashMap<String, String>();
		m.put("userId", userId);
		m.put("replyState", "0");//0:申请状态 1：审核状态
		m.put("replyTime", new Date());
		m.put("type", "免费交通案件(车辆类型："+driverName+")");
 		return super.executeUpdate("freeCaseDeclare", m);

  	}

	@Override
	public int publishResumeInfo(Map m) throws Exception{
 		return super.executeUpdate("publishResumeInfo", m);
	}
	
	@Override
	public int updateResumeInfo(Map map) throws Exception{
		ExceptionLogger.writeLog("修改用户简历信息："+map);
		//修改简历信息
		super.executeUpdate("updateResumeInfo", map);
		//修改用户工作经历信息
		if(map.containsKey("experienceList")){
			ExceptionLogger.writeLog("修改用户工作经历信息："+map.get("experienceList"));
			super.executeUpdate("updateWorkExperience", map.get("experienceList"));
		}
		
		//修改用户教育经历信息
		if(map.containsKey("eduList")){
			ExceptionLogger.writeLog("修改用户教育经历信息："+map.get("eduList"));
			super.executeUpdate("updateEdu", map.get("eduList"));
		}
		
		//修改用户简历上传资料
		if(map.containsKey("files")){
			ExceptionLogger.writeLog("修改用户简历上传资料："+map.get("files"));
			super.executeUpdate("deleteUser_file", Integer.parseInt(map.get("userId").toString()));
			super.executeUpdate("addResumeFile", map.get("files"));
			//super.executeUpdate("updateResumeFile", map.get("files"));
		}
		return 1;
	}

	@Override
	public int deleteResumeInfo(int userId)  throws Exception{
		super.executeUpdate("deleteUser_resume", userId);
		super.executeUpdate("deleteUser_experience", userId);
		super.executeUpdate("deleteUser_edu", userId);
		super.executeUpdate("deleteUser_file", userId);
		return 1;
	}

	@Override
	public int insertAliFollow(Map map) {
 		return super.executeUpdate("insertAliFollow", map);
	}

	@Override
	public int getUserConsultantState(int userId) throws Exception {
		List<Map> userList = super.executeQuery("getUserConsultantState",userId);
		int i = 0;
		Map map = userList.get(0);
		String vipLevel = map.get("vipLevel").toString();
		//非会员如果免费次数用完则返回0
		if(vipLevel.equals("0")){
			String freeNum = map.get("freeNum").toString();
			if(!freeNum.equals("0")){
				i = 1;
			}
		}else{//会员用户判断是否到期
  			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 			String expireTime = map.get("expireTime").toString().substring(0, map.get("expireTime").toString().length()-2);
			int res = expireTime.compareTo(sf.format(new Date()));
			ExceptionLogger.writeLog("到期时间："+expireTime+"当前时间："+sf.format(new Date()));
			if(res>=0){
				i=1;
			}
		}
		return i;
	}
	
	@Override
	public int realNameAuthConsultant(Map<String, Object> paramters) throws Exception {
		//更新个人咨询者信息
		ExceptionLogger.writeLog("个人咨询者实名认证");
		paramters.put("isAuthentication", "2");
		super.executeUpdate("editUserInfo", paramters);
		return super.executeUpdate("editConsultantInfo", paramters);
 	}

	@Override
	public List<Map> seacherJob(Paging paging, Map map) throws Exception {
		List list = super.executeQuery("seacherJob",paging,map);
		return list;
	}

	@Override
	public int updateJob(Map map) throws Exception {
		map.put("sendTime", new Date());
		return super.executeUpdate("updateJob", map);
	}

	@Override
	public int deleteJob(Map map) {
		return super.executeUpdate("deleteJob", map);
	}

	@Override
	public Map seacherJobDetail(Map map) throws Exception {
		List<Map> seacherJobDetailList = super.executeQuery("seacherJobDetail",map);
		for(int i=0;i<seacherJobDetailList.size();i++){
			seacherJobDetailList.get(i).put("salName", SystemConfigUtil.getValue(Integer.parseInt(seacherJobDetailList.get(i).get("sal").toString()), SystemConfigUtil.TYPE_SAL));
			seacherJobDetailList.get(i).put("qualificationName", getQualification(seacherJobDetailList.get(i).get("qualification").toString()));
			seacherJobDetailList.get(i).put("workExperience", SystemConfigUtil.getValue(Integer.parseInt(seacherJobDetailList.get(i).get("workExperience").toString()), SystemConfigUtil.TYPE_EXPERIENCE));
			
 			String provinceName="";
			Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+seacherJobDetailList.get(i).get("province").toString());
			provinceName = add.name;
			Map cityM = add.getCity();
			City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+seacherJobDetailList.get(i).get("city").toString());
			String cityName = city.getCityName();
			seacherJobDetailList.get(i).put("provinceName", provinceName);
			seacherJobDetailList.get(i).put("cityName", cityName);
		}
		
		if(null !=seacherJobDetailList && seacherJobDetailList.size() > 0 ) {
			return seacherJobDetailList.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int collectionJob(Map map) {
		return super.executeUpdate("collectionJob", map);
	}

	@Override
	public int collectionResume(Map map) {
		return super.executeUpdate("collectionResume", map);
	}

	@Override
	public List<Map> recommendResume() throws Exception {
		List<Map> recommendResumeList = super.executeQuery("recommendResume","");
		return recommendResumeList;
	}

	@Override
	public List<Map>  userSendAdvertise(int userId, Paging paging) throws Exception {
		Map map = new HashMap<String, String>();
		map.put("consultantId", userId);
		return  super.executeQuery("userSendAdvertise",paging,map);
	 
	}

	@Override
	public int userDelSendAdvertise(String Id) {
		return super.executeUpdate("userDelSendAdvertise", Id);
	}

	@Override
	public int deleteCollectionJobList(String collectionId) {
		return super.executeUpdate("deleteCollectionJobList", collectionId);
	}

	@Override
	public int companyDelSendAdvertise(String Id) {
		return super.executeUpdate("companyDelSendAdvertise", Id);
	}

	@Override
	public int msgSetting(Map map) {
		String type = map.get("type").toString();
		if(type.equals("0")){
			return super.executeUpdate("msgSettingReplylMsg", map);
		}else{ 
			return super.executeUpdate("msgSettingSysMsg", map);
		}
	}

	
 
	
	
	@Override
	public List<Map> counselorRecommendList(Map map) throws Exception {
		List<Map> list = super.executeQuery("counselorRecommendList",map);
		for(int i=0;i<list.size();i++){
			//ExceptionLogger.writeLog("城市:"+list.get(i).get("city").toString()+"省："+list.get(i).get("province").toString());
			String provinceName="";
			if(null!=list.get(i).get("province")){
				Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+list.get(i).get("province").toString());
				provinceName = add.name;
				list.get(i).put("provinceName", provinceName);
				if(null!=list.get(i).get("city")){
					Map cityM = add.getCity();
					City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+list.get(i).get("city").toString());
					String cityName = city.getCityName();
					list.get(i).put("cityName", cityName);
				}else{
					list.get(i).put("cityName", "");
				}
				
			}else{
				list.get(i).put("provinceName", "");
				list.get(i).put("cityName", "");
			}
			if(null!=list.get(i).get("job")){
				list.get(i).put("jobName", SystemConfigUtil.getValue(Integer.parseInt(list.get(i).get("job").toString()), SystemConfigUtil.TYPE_JOB));
			}else{
				list.get(i).put("jobName", "");
			}
			Map m = new HashMap<String, String>();
			m.put("userId", list.get(i).get("userId"));
			List<Map> specialityList = super.executeQuery("specialityList",m);
			for(int j=0;j<specialityList.size();j++){
				specialityList.get(j).put("typeName", SystemConfigUtil.getValue(Integer.parseInt(specialityList.get(j).get("typeId").toString()), SystemConfigUtil.TYPE_CASE));
			}
			list.get(i).put("specialityName", specialityList);
		}
		return list;
	}
	@Override
	public List<Map> adminRecommendCounselorList() throws Exception {
		List<Map> list = super.executeQuery("adminRecommendCounselorList","");
		for(int i=0;i<list.size();i++){
			//ExceptionLogger.writeLog("城市:"+list.get(i).get("city").toString()+"省："+list.get(i).get("province").toString());
			if(null!=list.get(i).get("province")&&!list.get(i).get("province").equals("")){
				String provinceName="";
				Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+list.get(i).get("province").toString());
				provinceName = add.name;
				Map cityM = add.getCity();
				City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+list.get(i).get("city").toString());
				String cityName = city.getCityName();
				list.get(i).put("provinceName", provinceName);
				list.get(i).put("cityName", cityName);
			}else{
				list.get(i).put("provinceName", "");
				list.get(i).put("cityName", "");
			}
			ExceptionLogger.writeLog("工作名称ID:"+list.get(i).get("job"));
			if(null!=list.get(i).get("job")&&!list.get(i).get("job").equals("")){
			ExceptionLogger.writeLog("工作名称:"+SystemConfigUtil.getValue(Integer.parseInt(list.get(i).get("job").toString()), SystemConfigUtil.TYPE_JOB));
			list.get(i).put("jobName", SystemConfigUtil.getValue(Integer.parseInt(list.get(i).get("job").toString()), SystemConfigUtil.TYPE_JOB));
			}else{
				list.get(i).put("jobName", "");
			}
			
			Map m = new HashMap<String, String>();
			m.put("userId", list.get(i).get("userId"));
			List<Map> specialityList = super.executeQuery("specialityList",m);
			for(int j=0;j<specialityList.size();j++){
				specialityList.get(j).put("typeName", SystemConfigUtil.getValue(Integer.parseInt(specialityList.get(j).get("typeId").toString()), SystemConfigUtil.TYPE_CASE));
			}
			list.get(i).put("specialityName", specialityList);
			
			if(null!=list.get(i).get("realName")&&!list.get(i).get("realName").equals("")){
				list.get(i).put("userName", list.get(i).get("realName"));
			}
		}
		return list;
	}

	@Override
	public int publishOrCloseJob(Map map) throws Exception  {
		return super.executeUpdate("publishOrCloseJob", map);

	}

	@Override
	public int deleteCollectionResumeList(String collectionId) {
		return super.executeUpdate("deleteCollectionResumeList", collectionId);
	}
	
	@Override
	public int userSuggest(Map map)  throws Exception {
		return super.executeUpdate("userSuggest", map);
	}

	@Override
	public List<Map> advertiseSearch(Map map, Paging paging)  throws Exception {
		List<Map> list = super.executeQuery("advertiseSearch",paging,map);
		for(int i=0;i<list.size()-1;i++){
				String provinceName="";
				Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+list.get(i).get("province").toString());
				provinceName = add.name;
				Map cityM = add.getCity();
				City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+list.get(i).get("city").toString());
				String cityName = city.getCityName();
				list.get(i).put("provinceName", provinceName);
				list.get(i).put("cityName", cityName);
		}
		return list;
	}
	
	

	
	@Override
	public List<Map> userResumeSearch(Map map, Paging paging)  throws Exception {
		List<Map> list = super.executeQuery("userResumeSearch",paging,map);
		for(int i=0;i<list.size()-1;i++){
				String provinceName="";
				Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+list.get(i).get("province").toString());
				provinceName = add.name;
				Map cityM = add.getCity();
				City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+list.get(i).get("city").toString());
				String cityName = city.getCityName();
				list.get(i).put("provinceName", provinceName);
				list.get(i).put("cityName", cityName);
				list.get(i).put("salName",  SystemConfigUtil.getValue(Integer.parseInt(list.get(i).get("sal").toString()), SystemConfigUtil.TYPE_SAL));

				list.get(i).put("typeName", getTypeName(list.get(i).get("type").toString()));
				list.get(i).put("qualificationName", getQualification(list.get(i).get("qualification").toString()));
				list.get(i).put("workExperience", SystemConfigUtil.getValue(Integer.parseInt(list.get(i).get("workExperience").toString()), SystemConfigUtil.TYPE_EXPERIENCE));

				ExceptionLogger.writeLog("城市:"+cityName+"省："+provinceName);
		}
		return list;
	}

	@Override
	public Map getAdvertisingById(String id)  throws Exception {
		 List<Map> list = super.executeQuery("getAdvertisingById", id);
		 return list.get(0);
	}
	
	

	@Override
	public int saveVoucher(Map map) throws Exception { 
		return super.executeUpdate("saveVoucher", map);
	}
	
	

	@Override
	public List listVoucher(Map map) throws Exception {
		return super.executeQuery("listVoucher",map);
	}

	@Override
	public int updateVoucher(Map map) throws Exception { 
		return super.executeUpdate("updateVoucher",map);
	}

	@Override
	public int freeMakevoucher(Map map) throws Exception {
		//int i=1;
		String userId = map.get("userId").toString();
		String voucherId = map.get("voucherId").toString();
		
		//我的文书券
		List<Map> list = super.executeQuery("getVoucherIdById", voucherId);
		if(null == list || list.size() == 0 ) {
			throw new BusinessException("文券不存在.",-1);
		} 
		Map m = list.get(0);
		String endTime =  m.get("endTime").toString();
		String state = m.get("state").toString();
		String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		//判断该文书券的状态
		 if(!state.equals("0")){
			 String str = "";
			 if(state.equals("1")) {
				 str="正在申请中";
			 }else if(state.equals("2")) {
				 str="已过期";
			 }else if(state.equals("3")) {
				 str="已经使用过";
			 }
			 throw new BusinessException("文券"+str+".",-1); 
		 }
		 String retrunStr = "当前时间："+now+"过期时间"+endTime+"比较结果"+endTime.compareTo(now);
		 ExceptionLogger.writeLog(retrunStr);
		 if(endTime.compareTo(now)<0){
			 throw new BusinessException(retrunStr,-1); 
		 }
		 //修改该文书券的状态为正在使用中
		 m.put("state", 1);
		 this.updateVoucher(m);

		Map m2 = new HashMap<String, String>();
		m2.put("userId", userId);
		m2.put("replyState", "0");// 0:申请状态 1：审核状态
		m2.put("replyTime", new Date());
		m2.put("type", "免费文书制作");
		m2.put("voucherId", voucherId);
		return super.executeUpdate("freeMakevoucher", m2);
 	}

	@Override
	public  List<Map>  localSearch(Map map, Paging paging) throws Exception {
		List<Map>   list;
//		Map m = new HashMap<String, Object>();
//		  String type = (String) map.get("type");
		  if(null!=map.get("type")&&map.get("type").equals("1")){
			      list =  super.executeQuery("localAdviceSearch",paging,map); 
				   for(int i=0;i<list.size()-1;i++){
						  if(null!=list.get(i).get("columnCode")&&!list.get(i).get("columnCode").equals("")){
								String articleTypeName="";
								articleTypeName =   SendArticleType.getTypeName(Integer.parseInt(list.get(i).get("columnCode").toString()));
								list.get(i).put("articleTypeName", articleTypeName);
								list.get(i).put("type","1");
								ExceptionLogger.writeLog("文章类型:"+articleTypeName);
						  }
					  }
		  }else if(null!=map.get("type")&&map.get("type").equals("2")){
			  list =super.executeQuery("localLegislationSearch",paging,map);
			  for(int i=0;i<list.size()-1;i++){
					String sendUnitName="";
				  if(null!=list.get(i).get("sendUnit")&&!list.get(i).get("sendUnit").equals("")){
							if(list.get(i).get("effectLevel").toString().equals("5")){
								sendUnitName = SystemConfigUtil.getValue(Integer.parseInt((String)list.get(i).get("sendUnit")), SystemConfigUtil.TYPE_UNIT_ADDRESS);
							 }else{
								sendUnitName =   SystemConfigUtil.getValue(Integer.parseInt(list.get(i).get("sendUnit").toString()), SystemConfigUtil.TYPE_UNIT);
							 }  }
							list.get(i).put("sendUnitName", sendUnitName);
							list.get(i).put("type","2");
							ExceptionLogger.writeLog("发布单位:"+sendUnitName);
				 
			  }
		  }else if(null!=map.get("type")&&map.get("type").equals("3")){
			  list =super.executeQuery("localSearchLvshi",paging,map);
			  for(int i=0;i<list.size()-1;i++){
					String sendUnitName="个人主页";
							list.get(i).put("sendUnitName", sendUnitName);
							list.get(i).put("type","3");
							ExceptionLogger.writeLog("发布单位:"+sendUnitName);
			  }
		  }
		  
		  else{
			   list =  super.executeQuery("localSearch",paging,map);
			   for(int i=0;i<list.size()-1;i++){
					  if(null!=list.get(i).get("sendUnit")&&!list.get(i).get("sendUnit").equals("")){
						  ExceptionLogger.writeLog("发文单位编号："+list.get(i).get("sendUnit"));
							String sendUnitName="";
							if(null!=list.get(i).get("sendUnit")&&!list.get(i).get("sendUnit").equals("")){
								if(list.get(i).get("effectLevel").toString().equals("5")){
									sendUnitName = SystemConfigUtil.getValue(Integer.parseInt((String)list.get(i).get("sendUnit")), SystemConfigUtil.TYPE_UNIT_ADDRESS);
								 }else{
									//sendUnitName =   SystemConfigUtil.getValue(Integer.parseInt(list.get(i).get("sendUnit").toString()), SystemConfigUtil.TYPE_UNIT);
									 sendUnitName =   "未知";
								 } 	
							}						
							list.get(i).put("sendUnitName", sendUnitName);
							ExceptionLogger.writeLog("发布单位:"+sendUnitName+"type=2");
					  }
					  if(null!=list.get(i).get("columnCode")&&!list.get(i).get("columnCode").equals("")){
							String articleTypeName="";
							articleTypeName =   SendArticleType.getTypeName(Integer.parseInt(list.get(i).get("columnCode").toString()));
							list.get(i).put("articleTypeName", articleTypeName);
							ExceptionLogger.writeLog("文章类型:"+articleTypeName);
					  }
				  }
			   
 		  }
	
		  return list;
	}

	@Override
	public List<Map> messageSearch(Map map, Paging paging) throws Exception {
		return super.executeQuery("messageSearch",paging,map);
		 
	}

	@Override
	public int insertSchedule(Map map) throws Exception {
		return super.executeUpdate("insertSchedule", map);
	}

	@Override
	public int insertScheduleReminderTime(Map scheduleReminderTimeMap)throws Exception  {
		return super.executeUpdate("insertScheduleReminderTime", scheduleReminderTimeMap);

	}

	@Override
	public List<Map> scheduleSearch(Map<String, Object> map, Paging paging)throws Exception  {
		List list = super.executeQuery("scheduleSearch",paging,map);
		return list;
	}
	
	@Override
	public List<Map> scheduleSearchALL(Map<String, Object> map, Paging paging)throws Exception  {
		List list = super.executeQuery("scheduleSearchALL",paging,map);
		return list;
	}

	@Override
	public int deleteSchedule(String id) throws Exception{
	 super.executeUpdate("deleteSchedule", id);
	 super.executeUpdate("deleteScheduleReminderTimeByScheduleId", id);
		return 1;
	}

	@Override
	public int deleteScheduleReminderTime(String id)throws Exception {
		return super.executeUpdate("deleteScheduleReminderTimeById", id);
	}

	@Override
	public int deleteAliFollow(String followId) throws Exception{
		return super.executeUpdate("deleteAliFollow", followId);
	}

	@Override
	public int updateSchedule(Map map) throws Exception {
		return super.executeUpdate("updateSchedule", map);
	}
	

	@Override
	public Map getConsultantInfo(Map<String, Object> map) throws Exception{
		 List<Map> list =  super.executeQuery("getConsultantInfo",map);
		 if(list.size()>0){
			 return list.get(0);
		 }else{
			 return null;
		 }
	}
	
	@Override
	public Map getCounselorInfo(Map<String, Object> map) throws Exception{
		 List<Map> list =  super.executeQuery("getCounselorInfo",map);
		 if(list.size()>0){
			 return list.get(0);
		 }else{
			 return null;
		 }
	}
	
	

	@Override
	public int addConsultantInfo(Map<String, Object> map) throws Exception {
		map.put("isAuthentication", "1");
		super.executeUpdate("editUserInfo", map);
		return super.executeUpdate("addConsultantInfo", map);
		
	}

	@Override
	public int updateCounselorInfo(Map userMap) throws Exception  {
		return super.executeUpdate("updateCounselorInfo", userMap);
	}
	
	@Override
	public int updateConsultantInfo(Map userMap) throws Exception  {
		return super.executeUpdate("updateConsultantInfo", userMap);
	}
	
	

	@Override
	public List<Map> getScheduleRemindertime() throws Exception {
		return  super.executeQuery("getScheduleRemindertime","");
	}

	@Override
	public void updateScheduleRemindertime(String id) throws Exception {
		 Map map = new HashMap<String, String>();
		 map.put("id", id);
		 super.executeUpdate("updateScheduleRemindertime", map);
	}

	@Override
	public int deleteMessage(Map<String, Object> map) throws Exception {
		return super.executeUpdate("deleteMessage", map);

	}

	@Override
	public int updateMessage(Map map) {
		return super.executeUpdate("updateMessage", map);

	}

	@Override
	public List<Map> getUserInfoByMobileOruserName(Map map) throws Exception {
		 List<Map> list =  super.executeQuery("getUserInfoByMobileOruserName",map);
		 return list;
	}
	
	
	private static String getQualification(String qualification){
		String qualificationName="";
		if(qualification.equals("1")){
			qualificationName = "不限";
		}else if (qualification.equals("2")){
			qualificationName = "初中及以下";
		}else if (qualification.equals("3")){
			qualificationName = "高中";
		}else if (qualification.equals("4")){
			qualificationName = "中技";
		}else if (qualification.equals("5")){
			qualificationName = "中专";
		}
		else if (qualification.equals("6")){
			qualificationName = "大专";
		}
		else if (qualification.equals("7")){
			qualificationName = "本科";
		}
		else if (qualification.equals("8")){
			qualificationName = "硕士";
		}
		else if (qualification.equals("9")){
			qualificationName = "MBA";
		}
		else if (qualification.equals("10")){
			qualificationName = "博士";
		}
		return qualificationName;
	} 
	

	
	private static String getTypeName (String type){
		String typeName="";
		if(type.equals("0")){
			typeName = "兼职";
		}else if (type.equals("1")){
			typeName = "全职";
		}
		return typeName;
	}

	@Override
	public int messageSend(Map map) throws Exception{
		return super.executeUpdate("messageSend", map);
	}

	/*@Override
	public List<Map> counselorList(Paging p, Map map) throws Exception {
		List<Map> list = super.executeQuery("counselorList",p,map);
		for(int i=0;i<list.size()-1;i++){
			String provinceName="";
			Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+list.get(i).get("province").toString());
			provinceName = add.name;
			Map cityM = add.getCity();
			City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+list.get(i).get("city").toString());
			String cityName = city.getCityName();
			list.get(i).put("provinceName", provinceName);
			list.get(i).put("cityName", cityName);
			list.get(i).put("jobName", SystemConfigUtil.getValue(Integer.parseInt(list.get(i).get("job").toString()), SystemConfigUtil.TYPE_JOB));
			Map m = new HashMap<String, String>();
			m.put("userId", list.get(i).get("userId"));
			List<Map> specialityList = super.executeQuery("specialityList",m);
			for(int j=0;j<specialityList.size();j++){
				specialityList.get(j).put("typeName", SystemConfigUtil.getValue(Integer.parseInt(specialityList.get(j).get("typeId").toString()), SystemConfigUtil.TYPE_CASE));
			}
			list.get(i).put("specialityName", specialityList);
			ExceptionLogger.writeLog("城市:"+cityName+"省："+provinceName); 
			if(null!=list.get(i).get("realName")&&!list.get(i).get("realName").equals("")){
				list.get(i).put("userName", list.get(i).get("realName"));
			}
		}
		return list;
	}*/
	
	@Override
	public List<ConselorInfo> counselorList(Paging p, Map map) throws Exception {
		List<ConselorInfo> list = null;
		  String sortType = (String)map.get("sortType");
		 if(sortType.equals("1")){
			    list = super.executeQuery("counselorListcommonScore",p,map);
		    }else if (sortType.equals("2")){
			    list = super.executeQuery("counselorListreadNum",p,map);
		    }else if (sortType.equals("3")){
			    list = super.executeQuery("counselorListlevelScore",p,map);
		    }else if (sortType.equals("4")){
			    list = super.executeQuery("counselorListchargeScore",p,map);
		    }
		
		for(int i=0;i<list.size()-1;i++){
			String provinceName="";
			if(null!=list.get(i).getProvince()){
			Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+list.get(i).getProvince().toString());
			provinceName = add.name;
			Map cityM = add.getCity();
			if(null!=list.get(i).getCity()){
				City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+list.get(i).getCity().toString());
				String cityName = city.getCityName();
				list.get(i).setCityName(cityName);
			}else{
				list.get(i).setCityName("");
			}
			list.get(i).setProvince(provinceName);
			}else{
				list.get(i).setProvince("");
				list.get(i).setCityName("");	
			}
			if(null!=list.get(i).getJob()){
				list.get(i).setJobName(SystemConfigUtil.getValue(Integer.parseInt(list.get(i).getJob().toString()), SystemConfigUtil.TYPE_JOB));
			}else{
				list.get(i).setJobName("");
			}
			Map m = new HashMap<String, String>();
			m.put("userId", list.get(i).getUserId());
			List<Map> specialityList = super.executeQuery("specialityList",m);
			for(int j=0;j<specialityList.size();j++){
				specialityList.get(j).put("typeName", SystemConfigUtil.getValue(Integer.parseInt(specialityList.get(j).get("typeId").toString()), SystemConfigUtil.TYPE_CASE));
			}
			list.get(i).setSpecialityName(specialityList);
			//ExceptionLogger.writeLog("城市:"+cityName+"省："+provinceName); 
			if(null!=list.get(i).getRealName()&&!list.get(i).getRealName().equals("")){
				list.get(i).setUserName(list.get(i).getRealName());
			}
		}
		return list;
	}

	@Override
	public List<Map> bannerSearch(Map<String, Object> map) {
		try {
			return  super.executeQuery("bannerSearch",map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public Map messageDetailsSearch(Map msg) {
		try {
			Map m =  (Map)super.executeQueryOne("messageDetailsSearch",msg);
			return m;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map messageTypeCount(Map map) {
		try {
			List<Map> mList =  super.executeQuery("messageTypeCount",map);
			return mList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;	}

	@Override
	public Map voucherCount(Map map) {
		try {
			List<Map> mList =  super.executeQuery("voucherCount",map);
			return mList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;	}

	@Override
	public Map seacherAliFollow(Map map) {
		try {
			Map m =  (Map) super.executeQueryOne("seacherAliFollow",map);
			return m;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateCounselorPersonInfo(Map map) {
		return super.executeUpdate("updateCounselorPersonInfo", map);
	}

	@Override
	public Map getUserConsultantInfo(Map map) {
		try {
			Map m =  (Map) super.executeQueryOne("getUserConsultantInfo",map);
			return m;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String formatName(String name) {
		
		if(StringUtil.isNotEmpty(name)) {
			name=name.substring(0, 1);
			name+="**";
		}
		return name;
	}
	@Override
	public List<Map> getUserEvaluate(String userId) throws Exception {
		 List<Map> list = super.executeQuery("getUserEvaluate",userId);
		 for(int i=0;i<list.size();i++){
			//获取用户信息
			 if(null!=list.get(i).get("evaluateUserId")){
				 Map<String, Object> user = (Map<String, Object>)super.executeQueryOne("findUserByUserId", Integer.parseInt(list.get(i).get("evaluateUserId").toString()));
					int userType = (int)user.get("userType");
					//区分咨询者和律师
					if(userType == 1||userType == 2){
						//是否以认证0：未认证 1：待审核 2：通过 3 : 未通过
						if(String.valueOf(user.get("isAuthentication")).equals("2")){
							Map m = new HashMap<String, String>();
							m.put("userId", list.get(i).get("evaluateUserId"));
							Map map = (Map)super.executeQueryOne("getConsultantOrCounselorInfo", m);
							list.get(i).put("realName", formatName(map.get("realName").toString()));
						}else{
							list.get(i).put("realName",  formatName(user.get("userName").toString()));
						}
					}else if(userType == 3){
						if(String.valueOf(user.get("isAuthentication")).equals("2")){
						Map<String, Object> otherInfo  = (Map<String, Object>) super.executeQueryOne("findCompanyByUserId", Integer.parseInt((String)list.get(i).get("evaluateUserId")));
						list.get(i).put("realName", otherInfo.get("companyName"));
						}else{
							list.get(i).put("realName", formatName(user.get("userName").toString()));
						}

					}  
			 }
				
		 }
		 return list;
	}

	@Override
	public void bindThreeLoginFlag(Map map) {
		String openType =map.get("openType").toString();
		if(openType.equals("WEIBO")){
			  super.executeUpdate("bindThreeLoginFlagweibo", map);
			  map.put("isWeiBo", "1");
			  super.executeUpdate("updateUseThreeLoginrWeiBo", map);
		}else if(openType.equals("WECHAT")){
			  super.executeUpdate("bindThreeLoginFlagwechat", map);
			  map.put("isWeChat", "1");
			  super.executeUpdate("updateUseThreeLoginrWeChat", map);
		}
		else if(openType.equals("QQ")){
			  super.executeUpdate("bindThreeLoginFlagqq", map);
			  map.put("isQQ", "1");
			  super.executeUpdate("updateUseThreeLoginrQQ", map);
		}
	}
	
	
	@Override
	public void unbindThreeLoginFlag(Map map) {
		String openType =map.get("openType").toString();
		if(openType.equals("WEIBO")){
			  super.executeUpdate("unbindThreeLoginFlagweibo", map);
			  map.put("isWeiBo", "0");
			  super.executeUpdate("updateUseThreeLoginrWeiBo", map);
		}else if(openType.equals("WECHAT")){
			  super.executeUpdate("unbindThreeLoginFlagwechat", map);
			  map.put("isWeChat", "0");
			  super.executeUpdate("updateUseThreeLoginrWeChat", map);
		}
		else if(openType.equals("QQ")){
			  super.executeUpdate("unbindThreeLoginFlagqq", map);
			  map.put("isQQ", "0");
			  super.executeUpdate("updateUseThreeLoginrQQ", map);

		}
	}

	@Override
	public int countThreeLoginFlag(Map map) throws Exception {
		return (int) super.executeQueryOne("countThreeLoginFlag", map);
	}

	@Override
	public List<Map> queryVoucher() throws Exception {
		return   super.executeQuery("queryVoucher", "");

	}

	@Override
	public Map seacherisCollectionJob(Map map) throws Exception{
		return  (Map) super.executeQueryOne("seacherisCollectionJob", map);
	}

	@Override
	public Map seacherisResumeJob(Map map) throws Exception {
		return  (Map) super.executeQueryOne("seacherisResumeJob", map);

	}
	@Override
	public Map seacherisCollectionResume(Map map) throws Exception {
		return   (Map) super.executeQueryOne("seacherisCollectionResume", map);

	}

	@Override
	public Map upmessageDetailsSearch(Map msg) {
		try {
			Map m =  (Map)super.executeQueryOne("upmessageDetailsSearch",msg);
			return m;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map nextmessageDetailsSearch(Map msg) {
		try {
			Map m =  (Map)super.executeQueryOne("nextmessageDetailsSearch",msg);
			return m;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int complainJob(Map map) {
		 return super.executeUpdate("complainJob", map);

	}

	@Override
	public void insertReport(Map m) {
		  super.executeUpdate("insertReport", m);
		
	}

	@Override
	public List<Map> checkNickNameisReg(Map nickNameMap) throws Exception {
		int userType = Integer.parseInt(nickNameMap.get("userType").toString());
		List<Map> otherInfo = null;
		if(userType == 1){
			otherInfo = super.executeQuery("findConsultantByUserNickName", nickNameMap);
		}else if(userType == 2){
			otherInfo = super.executeQuery("findCounselorByUserNickName", nickNameMap);
		}
		else if(userType == 3){
			otherInfo = super.executeQuery("findCompanyByUserNickName", nickNameMap);
		}
		return otherInfo;
	}

	@Override
	public int updateUserIntegral(Map<String, Object> map) throws Exception {
		return  super.executeUpdate("updateUserIntegral", map);
	}
	
	 
}
