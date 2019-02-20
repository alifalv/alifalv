package com.legal.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.Column;
import com.legal.app.service.ColumnService;

@Service
public class ColumnServiceImpl extends DaoMybatisImpl<Column> implements ColumnService {

	@Override
	public List<Map> findColumnByType(int type) throws Exception {
		// TODO Auto-generated method stub
		
		Map m = new HashMap();
		m.put("columnType",type);
		return super.executeQuery("findColumnByType", m);
	}

	

}
