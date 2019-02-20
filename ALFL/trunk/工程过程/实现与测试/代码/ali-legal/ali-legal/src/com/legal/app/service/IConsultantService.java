package com.legal.app.service;

import java.util.Map;

import com.common.dbutil.Dao;
import com.legal.app.model.Consultant;

/**
 * 个人咨询者用户信息
 * @author
 *
 */
public interface IConsultantService extends Dao<Consultant>{
	Consultant info(Consultant consultant) throws Exception ;

	int update(Map<?, ?> map) throws Exception;
}