package com.legal.app.service;

import java.util.List;

import com.common.dbutil.Dao;
import com.common.dbutil.Paging;
import com.legal.app.model.Column;

public interface ColumnService extends Dao<Column>{
	public List<Column> queryAll(Paging paging) throws Exception;

	public int remove(Integer columnId) throws Exception;
}
