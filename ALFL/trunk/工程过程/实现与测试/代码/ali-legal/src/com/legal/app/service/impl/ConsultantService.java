package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.legal.app.model.Consultant;
import com.legal.app.service.IConsultantService;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ConsultantService extends DaoMybatisImpl<Consultant> implements IConsultantService {
    @Override
    public Consultant info(Consultant consultant) throws Exception {
        return (Consultant) super.executeQueryOne("queryInfo", consultant);
    }

    @Override
    public int update(Map<?, ?> map) throws Exception {
        return super.executeUpdate("update", map);
    }
}
