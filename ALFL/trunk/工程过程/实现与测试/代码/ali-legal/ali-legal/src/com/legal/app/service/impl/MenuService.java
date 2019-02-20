package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.legal.app.model.SysPremisson;
import com.legal.app.model.SysUser;
import com.legal.app.service.IMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class MenuService extends DaoMybatisImpl<SysPremisson> implements IMenuService {
	@Override
	public List<SysPremisson> queryAll() throws Exception {
		return super.executeQuery("queryAll");
	}

	@Override
	public List<SysPremisson> userList(SysUser sysUser) throws Exception {
		return super.executeQuery("userList", sysUser);
	}

	@Override
	public List<SysPremisson> parentSysPremissonList() throws Exception{
		return super.executeQuery("parentSysPremissonList");

	}

	@Override
	public List<SysPremisson> childSysPremissonList() throws Exception {
		return super.executeQuery("childSysPremissonList");
	}

	@Override
	public List<SysPremisson> getChildSysPremissonList(Integer parentId) throws Exception {
		return super.executeQuery("getChildSysPremissonList", parentId);
	}

	@Override
	public List<SysPremisson> roleList(SysUser sysUser) throws Exception {
		return super.executeQuery("roleList", sysUser);
	}
}
