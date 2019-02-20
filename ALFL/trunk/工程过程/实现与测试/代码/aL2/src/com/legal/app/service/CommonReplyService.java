package com.legal.app.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.legal.app.model.CommonReply;

public interface CommonReplyService extends Dao<CommonReply>{

	/**
	 * 统一回复(咨询、案件委托、百姓吐槽、阿里裁决)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int commonReply(Map map) throws Exception;
	
	/**
	 * 修改统计回复
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateReplay(Map map) throws Exception;
	
	/**
	 * 根据业务类型 和 关联id获取回复列表信息
	 * @param relativeId
	 * @param businessType
	 * @param  pageNo
	 * @param  pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map> findReplyList(int relativeId,int businessType,int pageNo, int pageSize) throws Exception;
	
}
