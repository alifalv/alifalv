package com.legal.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.Column;
import com.legal.app.service.ColumnService;
import com.legal.app.service.Constant;

@Service
public class ColumnServiceImpl extends DaoMybatisImpl<Column> implements ColumnService {
	@SuppressWarnings("unchecked")
	@Override
	public List<Column> queryAll(Paging paging) throws Exception {
		return super.executeQuery("queryAll", paging);
	}

	@Override
	public int remove(Integer columnId) throws Exception {
		Column column = new Column();
		column.setColumnId(columnId);
		column.setIsDelete(Constant.DELETED);
		return super.executeUpdate("update", column);
	}
}
