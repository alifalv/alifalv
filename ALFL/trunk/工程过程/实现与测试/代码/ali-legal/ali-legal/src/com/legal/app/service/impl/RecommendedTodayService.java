package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.*;
import com.legal.app.service.Constant;
import com.legal.app.service.IRecommendedTodayService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendedTodayService extends DaoMybatisImpl<RecommendedToday> implements IRecommendedTodayService {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<RecommendedToday> list(RecommendedTodaySelectParam recommendedTodaySelectParam, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("columnType", recommendedTodaySelectParam.getColumnType());
		map.put("content", recommendedTodaySelectParam.getContent());
		map.put("addTimeStart", recommendedTodaySelectParam.getAddTimeStart());
		map.put("addTimeEnd", recommendedTodaySelectParam.getAddTimeEnd());
		List list = super.executeQuery("selectPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}

	@Override
	public RecommendedToday info(RecommendedToday recommendedToday) {
		try {
			recommendedToday = (RecommendedToday) super.executeQueryOne("queryInfo", recommendedToday);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recommendedToday;
	}

	@Override
	public void add(RecommendedToday recommendedToday) {
		super.executeUpdate("insert", recommendedToday);
	}

	@Override
	public int update(RecommendedToday recommendedToday) {
		return super.executeUpdate("update", recommendedToday);
	}

	@Override
	public int remove(Integer id) throws Exception {
		RecommendedToday RecommendedToday = new RecommendedToday();
		RecommendedToday.setId(id);
		RecommendedToday.setIsAvailable(Constant.DELETED);
		return super.executeUpdate("update", RecommendedToday);
	}
}
