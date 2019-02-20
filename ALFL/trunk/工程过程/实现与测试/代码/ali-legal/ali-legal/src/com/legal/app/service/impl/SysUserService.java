package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.SysUser;
import com.legal.app.service.Constant;
import com.legal.app.service.ISysUserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserService extends DaoMybatisImpl<SysUser> implements ISysUserService {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<SysUser> list(SysUser sysUser, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List list = super.executeQuery("selectPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}

	@Override
	public SysUser info(Integer sysUserId) throws Exception {
		SysUser info = new SysUser();
		info.setSys_userId(sysUserId);
		info = (SysUser) super.executeQueryOne("queryInfo", info);
		return info;
	}

	@Override
	public SysUser isExist(SysUser sysUser) throws Exception {
		sysUser = (SysUser) super.executeQueryOne("queryInfo", sysUser);
		return sysUser;
	}

	@Override
	public void add(SysUser sysUser) {
		super.executeUpdate("insert", sysUser);
	}

	@Override
	public int update(SysUser sysUser) {
		return super.executeUpdate("update", sysUser);
	}

	@Override
	public int remove(Integer sysUserId) throws Exception {
		SysUser removeUser = new SysUser();
		removeUser.setSys_userId(sysUserId);
		removeUser.setIsDelete(Constant.DELETED);
		return super.executeUpdate("update", removeUser);
	}

	@Override
	public int lock(Integer sysUserId) throws Exception {
		SysUser lockSU = new SysUser();
		lockSU.setSys_userId(sysUserId);
		lockSU.setSys_state(Constant.LOCKED);
		return super.executeUpdate("update", lockSU);
	}

	@Override
	public int unLock(Integer sysUserId) throws Exception {
		SysUser unLockUS = new SysUser();
		unLockUS.setSys_userId(sysUserId);
		unLockUS.setSys_state(Constant.UN_LOCKED);
		return super.executeUpdate("update", unLockUS);
	}
}
