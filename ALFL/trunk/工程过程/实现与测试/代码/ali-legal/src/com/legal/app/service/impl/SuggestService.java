package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.Suggest;
import com.legal.app.service.Constant;
import com.legal.app.service.ISuggestService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SuggestService extends DaoMybatisImpl<Suggest> implements ISuggestService {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Suggest> list(Suggest suggest, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("suggestTimeStart", suggest.getSuggestTimeStart());
		map.put("suggestTimeEnd", suggest.getSuggestTimeEnd());
		map.put("isComplate", suggest.getIsComplate());
		map.put("suggestContent", suggest.getSuggestContent());
		List list = super.executeQuery("selectPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}
		return list;
	}

	@Override
	public Suggest info(Integer suggestId) throws Exception {
		Suggest info = new Suggest();
		info.setSuggestId(suggestId);
		info = (Suggest) super.executeQueryOne("queryInfo", info);
		return info;
	}

	@Override
	public int remove(Integer suggestId) throws Exception {
		Suggest suggest = new Suggest();
		suggest.setSuggestId(suggestId);
		suggest.setIsDelete(Constant.DELETED);
		return super.executeUpdate("update", suggest);
	}

	@Override
	public int reply(Suggest suggest) throws Exception {
		suggest.setIsComplate(Constant.SENDED);
		suggest.setComplateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		return super.executeUpdate("update", suggest);
	}

	@Override
	public int numberOfSuggest() throws Exception {
		return (Integer) super.executeQueryOne("numberOfSuggest");
	}
}
