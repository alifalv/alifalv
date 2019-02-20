package com.legal.app.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.common.dbutil.DaoMybatisImpl;
import com.legal.app.model.Evaluate;
import com.legal.app.service.EvaluateService;

/**
 * 军山依旧在
 * @author Administrator
 *
 */
@Service
public class EvaluateServiceImpl  extends DaoMybatisImpl<Evaluate> implements EvaluateService{

	@Override
	public int saveEvaluate(Map map) throws Exception {
		// TODO Auto-generated method stub
		return super.executeUpdate("saveEvaluate", map);
	}

	@Override
	public List<Map> listEvaluate(Map map) throws Exception {
		// TODO Auto-generated method stub
		return super.executeQuery("listEvaluate", map);
	}

	@Override
	public int updateEvaluate(Map map) throws Exception {
		// TODO Auto-generated method stub
		return super.executeUpdate("updateEvaluate", map);
	}

	@Override
	public int deleteEvaluate(Map map) throws Exception {
		// TODO Auto-generated method stub
		return super.executeUpdate("deleteEvaluate", map);
	}

	@Override
	public List<Map> countsCounselorScore(Map map) throws Exception {
		// TODO Auto-generated method stub
		return  super.executeQuery("countsCounselorScore", map);
	}

	
	
}
