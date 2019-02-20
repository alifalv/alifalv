package com.legal.app.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.common.dbutil.Paging;
import com.legal.app.model.CaseDepute;

public interface CaseService extends Dao<CaseDepute>{
	
	/**
	 * 查询出案件委托的,没有联表查询
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List findCaseDepute(Map map) throws Exception;
	
	
	/**
	 * 案源委托完成
	 * 当案源发布者在订单列表中点确认完成
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int caseDeputeAccomplish(Map map) throws Exception;
	
	
	/**
	 * 删除案源委托
	 * @param caseId
	 * @return
	 * @throws Exception
	 */
	public int deleteCase(int caseId) throws Exception;
	
	
	/**
	 * 根据案件Id 查询出竞拍数据
	 * @param caseId
	 * @return
	 * @throws Exception
	 */
	public List listCaseOffer(int caseId) throws Exception;
	
	
	/**
	 * 我的案件委托
	 * @param m
	 * @param paging
	 * @return
	 * @throws Exception
	 */
	public List myCaseList(Map m,Paging paging) throws Exception;

	/**
	 * 发布案源委托
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int saveCaseDepute(Map map) throws Exception;
	
	/**
	 * 获取案件竞拍列表
	 * @param map
	 * @param paging
	 * @return
	 * @throws Exception
	 */
	public List<Map> findCaseList(Map map,Paging paging) throws Exception;
	
	/**
	 * 获取案件详情信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map findCaseDetailsInfo(Map map) throws Exception;
	
	/**
	 * 咨询师竞拍案件
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int offerCase(Map map) throws Exception;
	
	
	/**
	 * 查询竞拍的数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List    list(Map map) throws Exception;
	
	/**
	 * 聘请
	 * @param offerId 竞价id 
	 * @return
	 * @throws Exception
	 */
	public Map  employ(int offerId) throws Exception;
	
	/**
	 * 修改案件委托 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateCase(Map map) throws Exception;
	
	/**
	 * 获取我的案件委托
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map> findCaseListByUserId(int userId) throws Exception;
	
	/**
	 * 获取竞拍列表信息
	 * @param caseId
	 * @return
	 * @throws Exception
	 */
	public List<Map> findOfferList(Map map,Paging paging) throws Exception;
	
	/**
	 * 我竞拍的案件信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map> myOfferCaseList(Map map,Paging paging) throws Exception;
	
	/**
	 * 案源委托时，私下成交，关闭这一订单
	 * 1，要检查这个案件委托有没有产生支付订单，如果有，就把支付订单取消
	 * 2.   把发布的案件委托的状态改为已成交；
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void casePrivateDeal(Map map)throws Exception;
}
