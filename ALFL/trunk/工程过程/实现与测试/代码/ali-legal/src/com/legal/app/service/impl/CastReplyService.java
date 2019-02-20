package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.CastReply;
import com.legal.app.model.CastReplySelectParam;
import com.legal.app.service.ICastReplyService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CastReplyService extends DaoMybatisImpl<CastReply> implements ICastReplyService {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<CastReply> list(CastReplySelectParam castReplySelectParam, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("replyState", castReplySelectParam.getReplyState());
		map.put("type", castReplySelectParam.getType());
		map.put("content", castReplySelectParam.getContent());
		map.put("createTimeStart", castReplySelectParam.getCreateTimeStart());
		map.put("createTimeEnd", castReplySelectParam.getCreateTimeEnd());
		List list = super.executeQuery("selectPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}

	@Override
	public CastReply info(Integer replyId) {
		CastReply info = new CastReply();
		info.setId(replyId);
		try {
			info = (CastReply) super.executeQueryOne("queryInfo", info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	@Override
	public void add(CastReply CastReply) {
		super.executeUpdate("insert", CastReply);
	}

	@Override
	public int update(CastReply CastReply) {
		int result = super.executeUpdate("update", CastReply);
		return result;
	}

	@Override
	public int check(Integer castId) throws Exception {
		CastReply check = new CastReply();
		check.setId(castId);
		check.setReplyState(1);
		check.setCompleteTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		int result = super.executeUpdate("update", check);
		CastReply info = info(castId);
		if(info.getType()==null) {
			
		}else {
			if(info.getType().equals("免费文书制作")) {
				Map<String, Object> map = new HashMap<>();
				map.put("voucherId", info.getVoucherId());
				map.put("state", 3);
				result = super.executeUpdate("updateVoucher", map);
			}
		}
		return result;
	}

	@Override
	public int uncheck(Integer castId) throws Exception {
		CastReply check = new CastReply();
		check.setId(castId);
		check.setReplyState(2);
		check.setCompleteTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		int result = super.executeUpdate("update", check);
		CastReply info = info(castId);
		if(info.getType().equals("免费文书制作")) {
			Map<String, Object> map = new HashMap<>();
			map.put("voucherId", info.getVoucherId());
			map.put("state", 0);
			result = super.executeUpdate("updateVoucher", map);
		}
		return result;
	}

	@Override
	public int numbersOfLegalDocumentProduction() throws Exception {
		return (Integer) super.executeQueryOne("numbersOfLegalDocumentProduction");
	}

	@Override
	public int numbersOfTrafficCase() throws Exception {
		return (Integer) super.executeQueryOne("numbersOfTrafficCase");
	}
}
