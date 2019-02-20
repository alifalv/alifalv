package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.Evaluate;
import com.legal.app.model.EvaluateSelectParam;
import com.legal.app.service.IEvaluateService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluateService extends DaoMybatisImpl<Evaluate> implements IEvaluateService {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Evaluate> list(EvaluateSelectParam evaluateSelectParam, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("evaluateId", evaluateSelectParam.getEvaluateId());
		map.put("userType", evaluateSelectParam.getUserType());
		map.put("evaluateType", evaluateSelectParam.getBusinessType());
		map.put("content", evaluateSelectParam.getContent());
		map.put("addTimeStart", evaluateSelectParam.getAddTimeStart());
		map.put("addTimeEnd", evaluateSelectParam.getAddTimeEnd());
		List list = super.executeQuery("selectPageList", paging, map);
		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}

	@Override
	public Evaluate info(Integer evaluateId) throws Exception {
		Evaluate info = new Evaluate();
		info.setEvaluateId(evaluateId);
		info = (Evaluate) super.executeQueryOne("queryInfo", info);
		return info;
	}

	@Override
	public int remove(Integer evaluateId) throws Exception {
		Evaluate evaluate = new Evaluate();
		evaluate.setEvaluateId(evaluateId);
		evaluate.setEvaluateldContent("");
		//evaluate.setIsDelete(Constant.DELETED);
		return super.executeUpdate("update", evaluate);
	}
}
