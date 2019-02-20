package com.legal.app.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.common.dbutil.DaoMybatisImpl;
import com.legal.app.model.Company;
import com.legal.app.service.ICompanyService;

@Service
public class CompanyService extends DaoMybatisImpl<Company> implements ICompanyService {

	@Override
	public Company info(Company company) throws Exception {
        return (Company) super.executeQueryOne("queryInfo", company);
	}

	public int update(Map<?, ?> map) throws Exception {
        return super.executeUpdate("update", map);
	}

}
