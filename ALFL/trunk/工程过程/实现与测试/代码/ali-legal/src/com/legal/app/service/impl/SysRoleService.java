package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.SysRole;
import com.legal.app.service.Constant;
import com.legal.app.service.ISysRoleService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleService extends DaoMybatisImpl<SysRole> implements ISysRoleService {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<SysRole> list(SysRole sysRole, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List list = super.executeQuery("selectPageList", paging, map);

		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}

		return list;
	}

	@Override
	public SysRole info(SysRole sysRole) throws Exception {
		SysRole info = new SysRole();
		info = (SysRole) super.executeQueryOne("queryInfo", sysRole);
		return info;
	}

	@Override
	public void add(SysRole sysRole) {
		super.executeUpdate("insert", sysRole);
	}

	@Override
	public int update(SysRole sysRole) {
		return super.executeUpdate("update", sysRole);
	}

	@Override
	public int remove(Integer sys_roleId) throws Exception {
		SysRole removeUser = new SysRole();
		removeUser.setSys_roleId(sys_roleId);
		removeUser.setIs_delete(Constant.DELETED);
		return super.executeUpdate("update", removeUser);
	}

	@Override
	public int disabled(Integer sys_roleId) throws Exception {
		SysRole disabledSR = new SysRole();
		disabledSR.setSys_roleId(sys_roleId);
		disabledSR.setSys_state(Constant.DISABLED);
		return super.executeUpdate("update", disabledSR);
	}

	@Override
	public int enable(Integer sys_roleId) throws Exception {
		SysRole enableSR = new SysRole();
		enableSR.setSys_roleId(sys_roleId);
		enableSR.setSys_state(Constant.ENABLE);
		return super.executeUpdate("update", enableSR);
	}
}
