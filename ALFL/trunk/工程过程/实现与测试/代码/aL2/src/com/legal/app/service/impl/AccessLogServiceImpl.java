package com.legal.app.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.AccessLog;
import com.legal.app.service.AccessLogService;
@Service
public class AccessLogServiceImpl   extends  DaoMybatisImpl<AccessLog> implements AccessLogService{

	@Override
	public List<Map> listAccessLog(Map map, Paging paging) throws Exception {
		return super.executeQuery("listAccessLog", paging, map); 
	}

	@Override
	public List<Map> queryVisitAndBeVisited(Map map) throws Exception {
		// TODO Auto-generated method stub
		return  super.executeQuery("queryVisitAndBeVisited",map);
	}

	
	 
}
