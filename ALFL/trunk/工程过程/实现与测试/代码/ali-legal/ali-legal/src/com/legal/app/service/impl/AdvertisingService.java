package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.Advertising;
import com.legal.app.model.AdvertisingSelectParam;
import com.legal.app.service.Constant;
import com.legal.app.service.IAdvertisingService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdvertisingService extends DaoMybatisImpl<Advertising> implements IAdvertisingService {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Advertising> list(AdvertisingSelectParam advertisingSelectParam, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("columnType", advertisingSelectParam.getColumnType());
		map.put("name", advertisingSelectParam.getName());
		List list = super.executeQuery("selectPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}

	@Override
	public Advertising info(Advertising advertising) {
		try {
			advertising = (Advertising) super.executeQueryOne("queryInfo", advertising);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return advertising;
	}

	@Override
	public void add(Advertising advertising) {
		super.executeUpdate("insert", advertising);
	}

	@Override
	public int update(Advertising advertising) {
		return super.executeUpdate("update", advertising);
	}

	@Override
	public int remove(Integer id) throws Exception {
		Advertising Advertising = new Advertising();
		Advertising.setId(id);
		Advertising.setIsAvailable(Constant.DELETED);
		return super.executeUpdate("update", Advertising);
	}
}
