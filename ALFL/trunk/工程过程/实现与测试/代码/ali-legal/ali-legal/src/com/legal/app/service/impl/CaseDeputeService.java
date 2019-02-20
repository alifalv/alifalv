package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.CaseDepute;
import com.legal.app.service.Constant;
import com.legal.app.service.ICaseDeputeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CaseDeputeService extends DaoMybatisImpl<CaseDepute> implements ICaseDeputeService {
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public List<CaseDepute> list(CaseDepute caseDepute, Paging paging) throws Exception {
        Map<String, Object> map = new HashMap<>();
		if(caseDepute!=null) {
	        map.put("caseId", caseDepute.getCaseId());
	        map.put("caseType", caseDepute.getCaseType());
	        map.put("city", caseDepute.getCity());
	        map.put("deputeTimeStart", caseDepute.getDeputeTimeStart());
	        map.put("deputeTimeEnd", caseDepute.getDeputeTimeEnd());
	        map.put("content", caseDepute.getContent());
		}
        List list = super.executeQuery("selectCaseDeputePageList", paging, map);

        if (null != list && list.size() > 0) {
            paging = (Paging) list.get(list.size() - 1);
            list.remove(list.size()-1);
        }

        return list;
    }

    @Override
    public CaseDepute info(int caseId) throws Exception {
        CaseDepute info = new CaseDepute();
        info.setCaseId(caseId);
        return (CaseDepute) super.executeQueryOne("queryInfo", info);
    }

    @Override
    public int remove(int caseId) {
        CaseDepute caseDepute = new CaseDepute();
        caseDepute.setCaseId(caseId);
        caseDepute.setIsDelete(Constant.DELETED);
        return super.executeUpdate("update", caseDepute);
    }

    @Override
    public int numbersOfCaseDepute() throws Exception {
        return (Integer) super.executeQueryOne("numbersOfCaseDepute");
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,String>> numbersOfCaseType() throws Exception {
		return super.executeQuery("numbersOfCaseType");
	}
}
