package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.Sensitive;
import com.legal.app.service.Constant;
import com.legal.app.service.ISensitiveService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SensitiveService extends DaoMybatisImpl<Sensitive> implements ISensitiveService {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Sensitive> list(Sensitive sensitive, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("sensitiveValue", sensitive.getSensitiveValue());
		List list = super.executeQuery("selectPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}

	@Override
	public Sensitive info(Integer sensitiveId) throws Exception {
		Sensitive info = new Sensitive();
		info.setSensitiveId(sensitiveId);
		info = (Sensitive) super.executeQueryOne("queryInfo", info);
		return info;
	}

	@Override
	public void add(Sensitive sensitive) {
		super.executeUpdate("insert", sensitive);
	}

	@Override
	public int update(Sensitive sensitive) {
		return super.executeUpdate("update", sensitive);
	}

	@Override
	public int remove(Integer sensitiveId) throws Exception {
		Sensitive Sensitive = new Sensitive();
		Sensitive.setSensitiveId(sensitiveId);
		Sensitive.setIs_delete(Constant.DELETED);
		return super.executeUpdate("update", Sensitive);
	}
}
