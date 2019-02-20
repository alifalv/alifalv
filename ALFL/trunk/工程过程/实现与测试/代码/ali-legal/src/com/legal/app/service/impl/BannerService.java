package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.Banner;
import com.legal.app.model.BannerSelectParam;
//import com.legal.app.model.Resources;
import com.legal.app.service.Constant;
import com.legal.app.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BannerService extends DaoMybatisImpl<Banner> implements IBannerService {
	@Autowired
	ResourcesService resourcesService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Banner> list(BannerSelectParam bannerSelectParam, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("type", bannerSelectParam.getType());
		List list = super.executeQuery("selectPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}

	@Override
	public Banner info(Banner banner) {
		try {
			banner = (Banner) super.executeQueryOne("queryInfo", banner);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		List<Resources> list = resourcesService.list(banner.getId(), Constant.RESOURCES_BANNER);
//		banner.setImages(list);
		return banner;
	}

	@Override
	public void add(Banner banner) {
		super.executeUpdate("insert", banner);
//		try {
//			resourcesService.update(banner.getImages(), banner.getId(), Constant.RESOURCES_BANNER);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public int update(Banner banner) {
		int result = super.executeUpdate("update", banner);
//		try {
//			resourcesService.update(banner.getImages(), banner.getId(), Constant.RESOURCES_BANNER);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return result;
	}

	@Override
	public int remove(Integer id) throws Exception {
		Banner banner = new Banner();
		banner.setId(id);
		banner.setIsAvailable(Constant.DELETED);
		return super.executeUpdate("update", banner);
	}
}
