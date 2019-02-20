package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.legal.app.model.Counselor;
import com.legal.app.model.Speciality;
import com.legal.app.service.ICounselorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CounselorService extends DaoMybatisImpl<Counselor> implements ICounselorService {
    @Override
    public Counselor info(Counselor counselor) throws Exception {
        return (Counselor) super.executeQueryOne("queryInfo", counselor);
    }
	@SuppressWarnings("unchecked")
	@Override
    public List<Speciality> counselorSpeciality(Integer userId) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
        return (List<Speciality>) super.executeQuery("querySpeciality", map);
    }
	@Override
	public int update(Map<?, ?> map) throws Exception {
        return super.executeUpdate("update", map);
	}

	@Override
	public void addSpeciality(Speciality speciality) {
		super.executeUpdate("insertSpeciality", speciality);
	}

	@Override
	public void deleteSpeciality(Integer userId) throws Exception {
		Speciality info = new Speciality();
		info.setUserId(userId);
		super.executeUpdate("deleteSpeciality", info);
	}
}
