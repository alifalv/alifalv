package com.legal.app.service;

import com.common.dbutil.Dao;
import com.common.dbutil.Paging;
import com.legal.app.model.User;

import java.util.List;

/**
 * 用户
 * @author
 *
 */
public interface IUserService extends Dao<User>{
	List<User> queryAll(User user, Paging paging) throws Exception;

	int check(int userId) throws Exception;

	int uncheck(int userId) throws Exception;

	User info(int userId) throws Exception;

	int disabled(int userId);

	int enable(int userId);

	int contract(int userId);

	int unContract(int userId);

	int push(int userId);

	int unPush(int userId);

	int numberOfMembers() throws Exception;

	int currentRenewalFeeVip() throws Exception;

	int numbersOfPersonalConsultant() throws Exception;
	int numbersOfConsultant() throws Exception;
	int numbersOfBusinessConsultant() throws Exception;

	int VIPMember() throws Exception;
	int goldMember() throws Exception;
	int platinumMember() throws Exception;
	int diamondMembership() throws Exception;

	int numberOfNotAuthentication() throws Exception;

	List<User> queryUser(User user) throws Exception;
}
