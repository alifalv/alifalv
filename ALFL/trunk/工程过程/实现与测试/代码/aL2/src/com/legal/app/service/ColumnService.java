package com.legal.app.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.common.dbutil.Paging;
import com.legal.app.model.Column;

public interface ColumnService extends Dao<Column>{

	public List<Map> findColumnByType(int type) throws Exception;
	
}
