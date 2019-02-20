package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.SysMapping;
import com.legal.app.service.ISysMappingService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMappingService extends DaoMybatisImpl<SysMapping> implements ISysMappingService {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<SysMapping> list(SysMapping sysMapping, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List list = super.executeQuery("selectPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SysMapping> info(Integer roleId) throws Exception {
		SysMapping info = new SysMapping();
		info.setRoleId(roleId);
		List<SysMapping> list = super.executeQuery("queryInfo", info);
		return list;
	}

	@Override
	public void add(SysMapping sysMapping) {
		super.executeUpdate("insert", sysMapping);
	}

	@Override
	public void delete(Integer roleId) throws Exception {
		SysMapping info = new SysMapping();
		info.setRoleId(roleId);
		super.executeUpdate("delete", info);
	}

	@SuppressWarnings("unchecked")
	public List<SysMapping> rolemapping() throws Exception {
		return super.executeQuery("rolemapping");
	}
}
