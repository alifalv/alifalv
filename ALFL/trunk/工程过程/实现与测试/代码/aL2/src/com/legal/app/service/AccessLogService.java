package com.legal.app.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.common.dbutil.Paging;
import com.legal.app.model.AccessLog;

public interface AccessLogService extends Dao<AccessLog> {
	
	
	/**
	 * 查询出最近访问过我的人，
	 * @param map
	 * @param paging
	 * @return
	 * @throws Exception
	 */
	public  List<Map> listAccessLog(Map map,Paging paging) throws Exception;
	
	
	/**
	 * 查询访问者与被访问的记录，没有连表查询
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map> queryVisitAndBeVisited (Map map) throws Exception;
 	

}
