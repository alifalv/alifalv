package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.User;
import com.legal.app.service.Constant;
import com.legal.app.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends DaoMybatisImpl<User> implements IUserService {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<User> queryAll(User user, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", user.getUserId());
		map.put("userType", user.getUserType());
		map.put("isAuthentication", user.getIsAuthentication());
		map.put("userState", user.getUserState());
		map.put("mobile", user.getMobile());
		map.put("realName", user.getRealName());
		List list = super.executeQuery("selectUserPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<User> queryUser(User user) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", user.getUserId());
		map.put("userType", user.getUserType());
		map.put("isAuthentication", user.getIsAuthentication());
		map.put("userState", user.getUserState());
		map.put("mobile", user.getMobile());
		map.put("realName", user.getRealName());
		List<User> list = super.executeQuery("queryUser", map);
		return list;
	}

	@Override
	public User info(int userId) throws Exception {
		User info = new User();
		info.setUserId(userId);
		info = (User) super.executeQueryOne("queryInfo", info);
		return info;
	}

	@Override
	public int check(int userId) throws Exception {
		User checkUser = new User();
		checkUser.setUserId(userId);
		checkUser = (User) super.executeQueryOne("queryInfo", checkUser);
		if(checkUser.getTypeState()!=null && checkUser.getTypeState().equals("1")) {
    			//咨询者转变会员类型为咨询师
			checkUser.setUserType(2);
			checkUser.setTypeState("0");
		}
		checkUser.setIsAuthentication(Constant.USER_IS_AUTHENTICATION_CHECK);
		return super.executeUpdate("update", checkUser);
	}

	@Override
	public int uncheck(int userId) throws Exception {
		User checkUser = new User();
		checkUser.setUserId(userId);
		checkUser = (User) super.executeQueryOne("queryInfo", checkUser);
		if(checkUser.getTypeState()!=null && checkUser.getTypeState().equals("1")) {
    			//咨询者转变会员类型为咨询师
			checkUser.setTypeState("0");
		}
		checkUser.setIsAuthentication(Constant.USER_IS_AUTHENTICATION_UNCHECK);
		return super.executeUpdate("update", checkUser);
	}

	@Override
	public int disabled(int userId) {
		User disabledUser = new User();
		disabledUser.setUserId(userId);
		disabledUser.setUserState(Constant.USER_USER_STATE_DISABLED);
		return super.executeUpdate("update", disabledUser);
	}

	@Override
	public int enable(int userId) {
		User enableUser = new User();
		enableUser.setUserId(userId);
		enableUser.setUserState(Constant.USER_USER_STATE_ENABLE);
		return super.executeUpdate("update", enableUser);
	}

	@Override
	public int contract(int userId) {
		User contractUser = new User();
		contractUser.setUserId(userId);
		contractUser.setIsContract(Constant.USER_IS_CONTRACT_CONTRACT);
		return super.executeUpdate("update", contractUser);
	}

	@Override
	public int unContract(int userId) {
		User unContractUser = new User();
		unContractUser.setUserId(userId);
		unContractUser.setIsContract(Constant.USER_IS_CONTRACT_UN_CONTRACT);
		return super.executeUpdate("update", unContractUser);
	}

	@Override
	public int push(int userId) {
		User pushUser = new User();
		pushUser.setUserId(userId);
		pushUser.setIsPush(Constant.USER_IS_PUSH_PUSHED);
		return super.executeUpdate("update", pushUser);
	}

	@Override
	public int unPush(int userId) {
		User unPushUser = new User();
		unPushUser.setUserId(userId);
		unPushUser.setIsPush(Constant.USER_IS_PUSH_UN_PUSH);
		return super.executeUpdate("update", unPushUser);
	}

	@Override
	public int numberOfMembers() throws Exception{
		return (Integer) super.executeQueryOne("numberOfMembers");
	}

	@Override
	public int currentRenewalFeeVip() throws Exception {
		return (Integer) super.executeQueryOne("currentRenewalFeeVip");
	}

	@Override
	public int numbersOfPersonalConsultant() throws Exception {
		return (Integer) super.executeQueryOne("numbersOfPersonalConsultant");
	}

	@Override
	public int numbersOfConsultant() throws Exception {
		return (Integer) super.executeQueryOne("numbersOfConsultant");
	}

	@Override
	public int numbersOfBusinessConsultant() throws Exception {
		return (Integer) super.executeQueryOne("numbersOfBusinessConsultant");
	}

	@Override
	public int VIPMember() throws Exception {
		return (Integer) super.executeQueryOne("currentRenewalFeeVip");
	}

	@Override
	public int goldMember() throws Exception {
		return (Integer) super.executeQueryOne("goldMember");
	}

	@Override
	public int platinumMember() throws Exception {
		return (Integer) super.executeQueryOne("platinumMember");
	}

	@Override
	public int diamondMembership() throws Exception {
		return (Integer) super.executeQueryOne("diamondMembership");
	}

	@Override
	public int numberOfNotAuthentication() throws Exception {
		return (Integer) super.executeQueryOne("numberOfNotAuthentication");
	}
}
