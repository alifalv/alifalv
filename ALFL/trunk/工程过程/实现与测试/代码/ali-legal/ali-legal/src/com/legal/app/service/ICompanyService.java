package com.legal.app.service;

import java.util.Map;

import com.common.dbutil.Dao;
import com.legal.app.model.Company;
/**
 * 企业咨询者信息
 * @author wuyong
 *
 */
public interface ICompanyService extends Dao<Company> {
	Company info(Company company) throws Exception;
	int update(Map<?, ?> map) throws Exception;
}
