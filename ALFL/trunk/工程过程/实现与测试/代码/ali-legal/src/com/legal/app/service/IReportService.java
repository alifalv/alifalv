package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.Report;
import com.legal.app.model.ReportSelectParam;

import java.util.List;
import java.util.Map;

public interface IReportService {
    List<Report> list(ReportSelectParam reportSelectParam, Paging paging) throws Exception ;

    Report info(Integer reportId) throws Exception;

    void add(Report report) throws Exception;

    int remove(Map<?, ?> map) throws Exception;

	int numberOfReport() throws Exception;
}