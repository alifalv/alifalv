package com.legal.app.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.common.dbutil.Paging;
import com.legal.app.model.Advice;


/**
 * 咨询相关
 * @author huangzh
 *
 */
public interface AdviceService extends Dao<Advice>{
	
	/**
	 * 根据文章Id 查询出有多少人回复过；
	 * @param adviceId
	 * @return
	 * @throws Exception
	 */
	public List<Map> adviceReplyList(int adviceId) throws Exception;
	
	/**
	 * 修改
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateAdvice(Map map) throws Exception;
	
	/**
	 * 删除法律咨询
	 * @param adviceId
	 * @return
	 * @throws Exception
	 */
	public int deleteAdvice(int adviceId) throws Exception;
	
	
	/**
	 * 最近回复我的法律咨询和案件委托的 人 ，和统计；
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public  Map myReplyRecently(int userId) throws Exception;
	
	/**
	 * 我的咨询列表；
	 * 军山依旧
	 * @param map
	 * @param paging
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>>  myConsultList(Map<String,Object> map,Paging paging) throws Exception;
	

	/**
	 * 获取咨询列表信息
	 * @param parameters
	 * @param paging
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findAdviceList(Map<String,Object> parameters,Paging paging) throws Exception;
	
	/**
	 * 发布咨询问题
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public int sendAdvice(Map<String, Object> parameters) throws Exception;
	
	/**
	 * 获取咨询详情（加载上一咨询问题和下一咨询问题信息）
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Map findAdviceDetails(Map<String, Object> parameters) throws Exception;
	
	/**
	 * 获取咨询回复信息
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public List<Map> findAdviceReplyList(Map<String, Object> parameters,Paging paging) throws Exception;
	

	public Map findAdviceById(int  Id)  throws Exception;
	
	
	/**
	 * 查询出查询当前帖子的解答排名与登陆者是否已经解答
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public List<Map> findRankValueAndIsReply(Map<String,Object> parameters)  throws Exception;
	
	/**
	 * 保存法律咨询的解答
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int saveAdviceReply(Map<String,Object> map) throws Exception;
	
	/**
	 * 修改法律咨询的回复
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateAdviceReply(Map<String,Object>  map) throws Exception;
	
	/**
	 * 查询出点赞人对这个文章是否点赞过；
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map> findLikeNumList(Map<String,Object> map) throws Exception; 
	
	/**
	 * 删除点赞的记录
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int  deleteReplyLike(Map<String,Object> map) throws Exception;
	
	/**
	 * 回复点赞
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public int replyLike(Map<String, Object> parameters) throws Exception;
	
	/**
	 * 根据userId  获取 用户咨询问题
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map> findAdviceByUserId(int userId) throws Exception;
	
	/**
	 * 我解答的咨询
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map> myAnswerAdviceList(Map map,Paging paging) throws Exception;
}
