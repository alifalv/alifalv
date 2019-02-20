package com.legal.app.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.legal.app.model.Counselor;
import com.legal.app.model.Speciality;

/**
 * 咨询师信息
 * @author
 *
 */
public interface ICounselorService extends Dao<Counselor>{
	Counselor info(Counselor counselor) throws Exception;
	List<Speciality> counselorSpeciality(Integer userId) throws Exception;
	int update(Map<?, ?> map) throws Exception;
	void addSpeciality(Speciality speciality) throws Exception;
	void deleteSpeciality(Integer userId) throws Exception;
} 
