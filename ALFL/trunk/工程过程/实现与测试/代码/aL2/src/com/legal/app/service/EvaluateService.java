package com.legal.app.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.legal.app.model.Evaluate;
/**
 * 军山依旧在
 * @author Administrator
 * 订单评价
 *
 */
public interface EvaluateService extends Dao<Evaluate> {
	
	public int saveEvaluate(Map map) throws Exception;
	
	public List<Map> listEvaluate(Map map) throws Exception;
	
	public int updateEvaluate(Map map) throws Exception;
	
	public int deleteEvaluate(Map map) throws Exception;
	
	/**
	 * 统计咨询师的历史评分分数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map> countsCounselorScore(Map map) throws Exception;
	

}
