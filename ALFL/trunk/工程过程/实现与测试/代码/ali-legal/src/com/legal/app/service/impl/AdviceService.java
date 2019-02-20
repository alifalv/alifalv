package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.Advice;
import com.legal.app.model.AdviceSelectParam;
import com.legal.app.service.Constant;
import com.legal.app.service.IAdviceService;
import com.legal.app.utils.SystemConfigUtil;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class AdviceService extends DaoMybatisImpl<Advice> implements IAdviceService {
	@Override
	public List<Advice> list(AdviceSelectParam adviceSelectParam, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		if(adviceSelectParam!=null) {
			map.put("caseType", adviceSelectParam.getCaseType());
			map.put("sendTimeStart", adviceSelectParam.getSendTimeStart());
			map.put("sendTimeEnd", adviceSelectParam.getSendTimeEnd());
			map.put("content", adviceSelectParam.getContent());
		}
		List list = super.executeQuery("selectPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}

	@Override
	public Advice info(Integer adviceId) throws Exception {
		Advice info = new Advice();
		info.setAdviceId(adviceId);
		info = (Advice) super.executeQueryOne("queryInfo", info);
		return info;
	}

	@Override
	public int remove(Integer adviceId) throws Exception {
		Advice advice = new Advice();
		advice.setAdviceId(adviceId);
		advice.setIsDelete(Constant.DELETED);
		return super.executeUpdate("update", advice);
	}

	@Override
	public int numbersOfAdvice() throws Exception {
		return (Integer) super.executeQueryOne("numbersOfAdvice");
	}

	@Override
	public List<Map<String,String>> numbersOfAdviceType() throws Exception {
		return super.executeQuery("numbersOfAdviceType");
	}

	@Override
	public List numbersOfCounselorsReply(Map map,Paging paging) throws Exception {
		return super.executeQuery("numbersOfCounselorsReply",paging,map);
	}

	@Override
	public List detailsOfCounselorsReply(Map map, Paging paging) throws Exception {
		List list = super.executeQuery("detailsOfCounselorsReply",paging,map);
		if(null != list && list.size() > 0 ) {
			for(Object m : list) {
				if( m instanceof Map) {
					Map ma = (Map)m;
					String caseTypeName = SystemConfigUtil.getValue((int)ma.get("caseType"), SystemConfigUtil.TYPE_CASE);
					ma.put("caseTypeName", caseTypeName);
				}
			}
		} 
		return list;
	}
}
