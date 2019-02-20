package com.legal.app.service;

import com.common.dbutil.Dao;
import com.common.dbutil.Paging;
import com.legal.app.model.CaseDepute;

import java.util.List;
import java.util.Map;

/**
 * 案源
 * @author
 *
 */
public interface ICaseDeputeService extends Dao<CaseDepute>{
	List<CaseDepute> list(CaseDepute caseDepute, Paging paging) throws Exception;

	CaseDepute info(int caseId) throws Exception;

	int remove(int caseId);

	int numbersOfCaseDepute() throws Exception;

	List<Map<String,String>> numbersOfCaseType() throws Exception;
}