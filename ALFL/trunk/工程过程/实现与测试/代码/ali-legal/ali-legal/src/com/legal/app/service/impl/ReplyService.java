package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.CommonReply;
import com.legal.app.model.CommonReplySelectParam;
import com.legal.app.service.Constant;
import com.legal.app.service.IReplyService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReplyService extends DaoMybatisImpl<CommonReply> implements IReplyService {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<CommonReply> list(CommonReplySelectParam commonReplySelectParam, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("replyId", commonReplySelectParam.getReplyId());
		map.put("businessType", commonReplySelectParam.getBusinessType());
		map.put("userType", commonReplySelectParam.getUserType());
		map.put("content", commonReplySelectParam.getContent());
		map.put("addTimeStart", commonReplySelectParam.getAddTimeStart());
		map.put("addTimeEnd", commonReplySelectParam.getAddTimeEnd());
		List list = super.executeQuery("selectPageList", paging, map);
		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}
		return list;
	}

	@Override
	public CommonReply info(Integer replyId) throws Exception {
		CommonReply info = new CommonReply();
		info.setReplyId(replyId);
		info = (CommonReply) super.executeQueryOne("queryInfo", info);
		return info;
	}

	@Override
	public int reply(CommonReply commonReply) throws Exception {
		return 0;
	}

	@Override
	public int remove(Integer replyId,Integer origin) throws Exception {
		CommonReply commonReply = new CommonReply();
		commonReply.setReplyId(replyId);
		commonReply.setIsDelete(Constant.DELETED);
		if(origin.equals(3)) {
			return super.executeUpdate("updatethree", commonReply);
		}else if(origin.equals(4)) {
			return super.executeUpdate("updatefour", commonReply);
		}else {
			return super.executeUpdate("update", commonReply);
		}
	}
}
