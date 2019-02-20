package com.legal.app.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.common.dbutil.Paging;
import com.legal.app.model.ConselorInfo;
import com.legal.app.model.User;

@SuppressWarnings("rawtypes")
public interface UserService extends Dao<User>{

	/**
	 * 用户登录
	 * @param userName 账号或者手机号码
	 * @param password 密码
	 * @return 用户信息
	 * @throws Exception
	 */
	public Map<String, Object> userLogin(String userName,String password) throws Exception;
	
	
	
	public int updateUserIntegral(Map<String,Object>  map) throws Exception;
	
	/**
	 * 修改咨询师的钱包余额；
	 * @param userId  用户Id
	 * @param money  最后的结果，不是再去与数据库相加减，如果传入100 ，那这位咨询者的余额就是 100 元了。
	 * @return
	 * @throws Exception
	 */
	public int updateCounselorBalance(int userId ,Double money) throws Exception;
	
	
	/**
	 * 查询出咨询师
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List listCounselor(Map map) throws Exception;
	
	
	/**
	 * 根据userId 查询出咨询师 ali_counselor 的属性对象
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map findCounselorByUserId(int userId) throws Exception;
	
	/**
	 * 获取用户信息 （联表查询）
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getUserInfo(int userId) throws Exception;
	
	/**
	 * 查询用户信息 (不联表查询)
	 * @param userId
	 * @return
	 */
	public Map getUserInfo(String userId) throws Exception;
	
	/**
	 * 获取用户的企业信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map>  findUserCompanyInfo(Map map) throws Exception;
	
	/**
	 * 查询后台登陆用户的信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map getSysUserInfo(String userId) throws Exception;
	
	/**
	 * 个人咨询者注册
	 * @param paramters
	 * @return
	 * @throws Exception
	 */
	public int consultantRegister(Map<String, Object> paramters) throws Exception;
	
	
	/**
	 * 个人咨询者   开通会员
	 * @param paramters
	 * @return
	 * @throws Exception
	 */
	public int openVipByConsultant(Map<String, Object> paramters)throws Exception;
	
	/**
	 * 企业咨询者注册
	 * @return
	 * @throws Exception
	 */
	public int companyRegister(Map<String, Object> paramters) throws Exception;
	
	/**
	 * 企业咨询者开通会员
	 * @param paramters
	 * @return
	 * @throws Exception
	 */
	public int openVipByCompany(Map<String, Object> paramters)throws Exception;
	 
	/**
	 * 查询赠送的文书券
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List listVoucher(Map map) throws Exception;
	
	/**
	 * 企业认证
	 * @param paramters
	 * @return
	 * @throws Exception
	 */
	public int companyAuthentication(Map<String, Object> paramters) throws Exception;
	
	
	/**
	 * 咨询师注册
	 * @param paramters
	 * @return
	 * @throws Exception
	 */
	public int counselorRegister(Map<String, Object> paramters) throws Exception;
	
	
	/**
	 * 修改咨询师的属性信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateCounselor(Map map) throws Exception;
	
	/**
	 * 保存咨询师认职业认证信息
	 * @param paramters
	 * @return
	 * @throws Exception
	 */
	public int counselorAuthentication(Map<String, Object> paramters) throws Exception;
	
	/**
	 * 咨询者认证成为咨询师
	 * @param paramters
	 * @return
	 * @throws Exception
	 */
	public int authenticateCounselor(Map<String, Object> paramters) throws Exception;
	
	
	/**
	 * 根据用户id 获取  用户基本信息       昵称，用户头像
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findUserBasicInfoByUserId(int userId) throws Exception;
	
	
	/**
	 * 获取此用户的免费咨询次数
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public int findFreeNumByUserId(int userId) throws Exception;
	
	/**
	 * 根据userType 类型来修改这个用户的免费咨询次数
	 * @param userId
	 * @param userType
	 * @param freeNum  1 或者 -1
	 * @return
	 * @throws Exception
	 */
	public int updateFree(int userId,int userType,int  freeNum ) throws Exception;
	
	/**
	 * 更新咨询者的免费次数
	 * @param userId
	 * @param freeNum  1 : -1
	 * @return
	 * @throws Exception
	 */
	public int updateFreeNum(int userId,int  freeNum) throws Exception;
	/**
	 * 更新企业的免费次数
	 * @param userId
	 * @param freeNum  1 : -1
	 * @return
	 * @throws Exception
	 */
	public int updateCompanyFreeCount(int userId,int  freeNum) throws Exception;
	/**
	 * 更新咨询师的免费次数
	 * @param userId
	 * @param freeNum  1 : -1
	 * @return
	 * @throws Exception
	 */
	public int updateCounselorFreeCount(int userId,int  freeNum) throws Exception;
	
	/**
	 * 获取用户过期时间
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Timestamp findUserVipOutTime(int userId) throws Exception;
	
	/**
	 * 添加访问记录，
	 * 可以添加我访问谁，也可以添加谁访问我 
	 *  
	 * @param Map 【userId 我访问人的Id；counselorId ：被访问人的Id】
	 * @return
	 * @throws Exception
	 */
	public int addBrowse(Map map) throws Exception;
	
	/**123456qwe
	 * 我浏览的咨询师
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map> findBrowseList(Paging p,Map map) throws Exception;
	
	/**
	 * 我关注的咨询师
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map> findFollowList(int userId) throws Exception;
	
	/**
	 * 我咨询过的咨询师
	 * @return
	 * @throws Exception
	 */
	public List<Map> findMyAdviceConsultant(Paging p,Map userMap) throws Exception;
	
	/**
	 * 第三方绑定
	 * @param openType 登录类型
	 * @param openId 授权id
	 * @return
	 * @throws Exception
	 */
	public int bindThreeInfo(Map map) throws Exception;
	
	/**
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int addCompanyInfo(Map map) throws Exception;
	
	/**
	 * 根据用户编号获取公司信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map getCompanyInfo(int userId) throws Exception;
	
	/**
	 * 根据用户id 获取 用户简历信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map findResumeInfoByUserId(int userId) throws Exception;
	
	/**
	 * 用户投递简历
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int deliverResume(Map map)throws Exception;
	
	/**
	 * 保存用户简历
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int addResumeInfo(Map map) throws Exception;
	
	
	/**
	 * 根据咨询师主页地址 获取  用户  编号
	 * @return
	 * @throws Exception
	 */
	public int getUserIdByPersonUrl(String url) throws Exception;
	
	
	/**
	 * 更新用户头像
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int changeUserProfile(Map map)throws Exception;
	
	/**
	 * 登录成功更新推送码
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateUserPushCode(Map map) throws Exception; 
	
	
	/**
	 * 获取第三方登录凭证
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getThreeLoginFlag(Map map)throws Exception; 
	
	/**
	 * 根据用户id  获取用户简历信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map getResumeInfoByUserId(int userId) throws Exception;
	
	/**
	 * 修改密码
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int editPassword(Map map)throws Exception;
	
	/**
	 * 修改手机号码(用于重新绑定手机号码)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int editMobile(Map map)throws Exception;
	
	/**
	 * 根据手机号码修改密码
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int editPasswordByMobile(Map map) throws Exception;
	/**
	 * 根据用户Id查询我的文书劵
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	public List<Map>  voucherList(Map map,Paging paging) throws Exception;
	/**
	 * 根据用户Id更新用户名称，昵称
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	public int updateUserInfo(Map userMap)  throws Exception;
	/**
	 * 插入咨询师信息
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	public int insertCounselorInfo(Map userMap)  throws Exception;
	
	/**
	 * 根据用户id获取我收藏的咨询师
	 * @throws Exception
	 */
	public List<Map> myCollectionConsultant(Paging p,Map userMap) throws Exception;
	
	/**
	 * 用户收藏的岗位信息列表
	 * @throws Exception
	 */
	public List<Map> collectionResumeList(Paging paging,int userId) throws Exception;
	/**
	 * 发布岗位
	 * @throws Exception
	 */
	public int sendJob(Map mapJob) throws Exception;
	/**
	 * 用户岗位收藏
	 * @throws Exception
	 */
	public List<Map> collectionJobList(int userId, Paging paging) throws Exception;
	/**
	 * 根据用户id获取用户收到简历投递列表
	 * @throws Exception
	 */
	public List<Map> collecResumeList(int userId, Paging paging) throws Exception;
	/**
	 * 根据用户id获取用户订单列表
	 * @throws Exception
	 */
	public Map<String, Object>  businessOrderList(Map map) throws Exception;
	/**
	 * 最新招聘列表
	 * @throws Exception
	 */
	public List<Map> newJobList(Paging paging,Map map) throws Exception;
	/**
	 * 免费案件申报
	 * @throws Exception
	 */
	public int freeCaseDeclare(String userId) throws Exception;
	/**
	 * 发布/关闭简历信息
	 * @throws Exception
	 */
	public int publishResumeInfo(Map m) throws Exception;
	
	/**
	 * 修改简历信息
	 * @throws Exception
	 */
	public int updateResumeInfo(Map map) throws Exception;
	/**
	 * 根据用户id获取用户简历信息
	 * @throws Exception
	 */
	public int deleteResumeInfo(int userId) throws Exception;

	/**
	 * 用户关注咨询师
	 * @throws Exception
	 */
	public int insertAliFollow(Map map) throws Exception;
	
	/**
	 * 用户取消关注咨询师
	 * @throws Exception
	 */
	public int deleteAliFollow(String followId) throws Exception;
	/**
	 * 根据用户id获取用户免费咨询资格 (0:无 1:有)
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	public int getUserConsultantState(int userId) throws Exception;
	/**
	 *  个人咨询者管理中心的认证接口
	 * @param userId 用户id
	 * @param realName 真实姓名
	 * @param idCard   证件号码
	 * @param idCardFront 证件正面照片
	 * @param idCardBack  证件反面照片
	 * @param out
	 * @throws Exception 
	 */
	public int realNameAuthConsultant(Map<String, Object> map) throws Exception;
	/**
	 * 查询岗位
	 * @throws Exception
	 */
	public List<Map> seacherJob(Paging paging, Map map) throws Exception;
	/**
	 * 修改岗位
	 * @throws Exception
	 */
	public int updateJob(Map map) throws Exception;
	/**
	 * 删除岗位
	 * @throws Exception
	 */
	public int deleteJob(Map map);
	/**
	 * 查询岗位详情
	 * @throws Exception
	 */
	public Map seacherJobDetail(Map map) throws Exception;
	
	/**
	 * 用户收藏岗位
	 * @throws Exception
	 */
	public int collectionJob(Map map);
	/**
	 * 用户收藏简历
	 * @throws Exception
	 */
	public int collectionResume(Map map);
	/**
	 * 用户推荐简历
	 * @throws Exception
	 */
	public List<Map> recommendResume() throws Exception;
	/**
	 *	用户投递过的岗位列表
	 * @throws Exception
	 */
	public List<Map>  userSendAdvertise(int userId, Paging paging) throws Exception;
	/**
	 *  删除用户投递过的岗位列表
	 * @throws Exception
	 */
	public int userDelSendAdvertise(String id);
	
	/**
	 *	删除用户投收藏的岗位列表
	 * @throws Exception
	 */
	public int deleteCollectionJobList(String collectionId);
	
	/**
	 *	删除用户投收藏的简历列表
	 * @throws Exception
	 */
	public int deleteCollectionResumeList(String collectionId);

	/**
	 *	企业删除用户投递过的岗位列表
	 * @throws Exception
	 */
	public int companyDelSendAdvertise(String id);
	/**
	 *	消息设置
	 * @throws Exception
	 */
	public int msgSetting(Map map);
	/**
	 * 咨询师推荐列表
	 * @return
	 */
	public List<Map> counselorRecommendList(Map map) throws Exception ;
	
	/**
	 * 管理员推荐咨询师列表
	 * @return
	 */
	public List<Map> adminRecommendCounselorList() throws Exception ;
	
	/**
	 * 发布/关闭岗位
	 * @throws Exception
	 */
	public int publishOrCloseJob(Map map) throws Exception ;
	/**
	 *用户意见反馈
	 * @throws Exception
	 */
	public int userSuggest(Map map)  throws Exception;
	/**
	 *
	 *招聘列表查询(招聘岗位高级搜索)
	 * @throws Exception
	 */
	public List<Map> advertiseSearch(Map map, Paging paging)  throws Exception;
	/**
	 * 简历列表查询（人才简历高级搜索）
	 * @throws Exception
	 */
	public List<Map> userResumeSearch(Map map, Paging paging) throws Exception;
	/**
	 * 获取广告详情
	 * @throws Exception
	 */
	public Map getAdvertisingById(String id)  throws Exception ;
	
	/**
	 * 保存我的卡券
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int saveVoucher(Map map) throws Exception;
	
	/**
	 * 修改我的卡券
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateVoucher(Map map) throws Exception;
	/**
	 * 免费文书制作申请
	 * @throws Exception
	 */
	public int freeMakevoucher(Map map) throws Exception ;
	/**
	 * 站内搜索
	 * @throws Exception
	 */
	public   List<Map>  localSearch(Map map, Paging paging) throws Exception;
	/**
	 * 站内消息查询
	 * @throws Exception
	 */
	public List<Map> messageSearch(Map map, Paging paging) throws Exception;
	/**
	 *	添加日程
	 * @throws Exception
	 */
	public int insertSchedule(Map map) throws Exception ;
	/**
	 *	添加日程时间
	 * @throws Exception
	 */
	public int insertScheduleReminderTime(Map scheduleReminderTimeMap) throws Exception ;
	
	/**
	 * 日程查询
	 * @throws Exception
	 */
	public List<Map> scheduleSearch(Map<String, Object> map, Paging paging) throws Exception ;
	/**
	 * 日程删除
	 * @throws Exception
	 */
	public int deleteSchedule(String id) throws Exception;
	/**
	 * 日程时间删除
	 * @throws Exception
	 */
	public int deleteScheduleReminderTime(String id) throws Exception;

	/**
	 * 日程内容修改
	 * @throws Exception
	 */
	public int updateSchedule(Map map) throws Exception;

	/**
	 * 查询咨询者信息
	 * @param map
	 * @return
	 */
	public Map getConsultantInfo(Map<String, Object> map) throws Exception;
	/**
	 * 查询咨询师信息
	 * @param map
	 * @return
	 */
	public Map getCounselorInfo(Map<String, Object> map) throws Exception;
	/**
	 * 添加咨询师信息
	 * @param map
	 */
	public int addConsultantInfo(Map<String, Object> map) throws Exception;
	/**
	 * 更新咨询师信息
	 * @param map
	 */
	public int updateCounselorInfo(Map userMap) throws Exception;
	/**
	 * 更新咨询者信息
	 * @param map
	 */
	public int updateConsultantInfo(Map userMap) throws Exception;	
	
	/**
	 * 查询日程提醒时间
	 * @return
	 */
	public List<Map> getScheduleRemindertime() throws Exception;
	/**
	 * 更新提醒时间状态
	 * @param string
	 */
	public void updateScheduleRemindertime(String id) throws Exception;
	/**
	 * 删除消息列表
	 * @param ids
	 * @return
	 */
	public int deleteMessage(Map<String, Object> map) throws Exception;
	/**
	 * 标记消息列表已读
	 * @param map
	 * @return
	 */
	public int updateMessage(Map map) throws Exception;
	
	/**
	 * 根据手机号码查询用户信息
	 * @param userId
	 * @return
	 */
	public List<Map> getUserInfoByMobileOruserName(Map map) throws Exception;
	/**
	 * 站内消息发送
	 * @param map
	 * @return
	 */
	public int messageSend(Map map)throws Exception;
	/**
	 * 查询咨询师列表
	 * @param queryType 查询类型   1：综合排名 2：人气排名3：法律水平4：收费标准
	 * @param pageNo 页码
	 * @param pageSize 每页的记录数
	 * @param province 省份id
	 * @param city     城市id
	 * @param speciality 专业领域id
	 * @param job      职业
	 * @param name 姓名
	 * @param out
	 * @throws Exception
	 */
	public List<ConselorInfo> counselorList(Paging p, Map map) throws Exception;
	/**
	 * 头条查询
	 * @throws Exception
	 */
	public List<Map> bannerSearch(Map<String, Object> map);
	/**
	 * 站内消息详情
	 * @throws Exception
	 */
	public Map messageDetailsSearch(Map msg);
	
	/**
	 * 站内消息详情
	 * @throws Exception
	 */
	public Map upmessageDetailsSearch(Map msg);
	
	/**
	 * 站内消息详情
	 * @throws Exception
	 */
	public Map nextmessageDetailsSearch(Map msg);

	/**
	 * 站内消息类型计数（消息列表）
	 * @throws Exception
	 */
	public Map messageTypeCount(Map map);
	/**
	 * 文书劵列表计数
	 * @throws Exception
	 */
	public Map voucherCount(Map map);
    
	public Map seacherAliFollow(Map map);
	/**
	 * 个人主页开通
	 * @throws Exception
	 */
	public int updateCounselorPersonInfo(Map map);
	/**
	 * 根据个人主页地址查询咨询师信息，判断是否唯一
	 * @param map
	 * @return
	 */
	public Map getUserConsultantInfo(Map map);
	/*
	 * 获取用户评价
	 */
	public List<Map> getUserEvaluate(String userId) throws Exception;
	/**
	 * 绑定第三方登录凭证
	 * @param map
	 */
	public void bindThreeLoginFlag(Map map);
	/**
	 * 解除第三方登录凭证绑定
	 * @param map
	 */
	public void unbindThreeLoginFlag(Map map);

	public int countThreeLoginFlag(Map map) throws Exception;

	public List<Map> queryVoucher() throws Exception;
	/**
	 * 查询用户是否收藏岗位
	 * @throws Exception
	 */
	public Map seacherisCollectionJob(Map map) throws Exception;
	/**
	 * 查询用户是否投递岗位
	 * @throws Exception
	 */
	public Map seacherisResumeJob(Map map) throws Exception;
	
	/**
	 * 查询企业是否收藏简历
	 * @throws Exception
	 */
	public Map seacherisCollectionResume(Map map) throws Exception;
	/**
	 * 举报岗位
	 * @throws Exception
	 */
	public int complainJob(Map map);

	public void insertReport(Map m);
	/**
	 * 检查昵称是否注册
	 * @param nickNameMap
	 * @return
	 */
	public List<Map> checkNickNameisReg(Map nickNameMap) throws Exception ;

	public List<Map> scheduleSearchALL(Map<String, Object> map, Paging paging)  throws Exception ;


}
