package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.legal.app.model.Resources;
import com.legal.app.service.IResourcesService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourcesService extends DaoMybatisImpl<Resources> implements IResourcesService {
    @Override
    public void update(List<Resources> resources, Integer belongId, String type) throws Exception {
        Resources delete = new Resources();
        delete.setBelongId(belongId);
        delete.setType(type);
        super.executeUpdate("delete", delete);

        for (Resources add :
                resources) {
            add.setBelongId(belongId);
            add.setType(type);
            super.executeUpdate("insert", add);
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<Resources> list(Integer belongId, String type) {
        List<Resources> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("belongId", belongId);
        map.put("type", type);
        try {
            list = super.executeQuery("selectList", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
