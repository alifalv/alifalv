package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.Report;
import com.legal.app.model.ReportSelectParam;
import com.legal.app.service.Constant;
import com.legal.app.service.IReportService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService extends DaoMybatisImpl<Report> implements IReportService {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Report> list(ReportSelectParam reportSelectParam, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("reportTimeStart", reportSelectParam.getReportTimeStart());
		map.put("reportTimeEnd", reportSelectParam.getReportTimeEnd());
		map.put("reportType", reportSelectParam.getReportType());
		map.put("content", reportSelectParam.getContent());
		List list = super.executeQuery("selectPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}

	@Override
	public Report info(Integer reportId) throws Exception {
		Report info = new Report();
		info.setReportId(reportId);
		info = (Report) super.executeQueryOne("queryInfo", info);
		return info;
	}

	@Override
	public int remove(Map<?, ?> map) throws Exception {
		int result = 0;
		Report Report = new Report();
		Report.setReportId((Integer) map.get("reportId"));
		Report.setIsDelete(Constant.DELETED);
		result = super.executeUpdate("update", Report);
		if(map.get("type").equals("S")) {
			if(map.get("reportType").equals("1")) {
				result = super.executeUpdate("deletetrain", map);
			}else {
				result = super.executeUpdate("deleteadvertise", map);
			}
		}
		return result;
	}

	@Override
	public int numberOfReport() throws Exception {
		return (Integer) super.executeQueryOne("numberOfReport");
	}
}
