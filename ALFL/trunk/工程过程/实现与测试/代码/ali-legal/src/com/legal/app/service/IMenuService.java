package com.legal.app.service;

import com.common.dbutil.Dao;
import com.legal.app.model.SysPremisson;
import com.legal.app.model.SysUser;

import java.util.List;

/**
 * 菜单
 * @author
 *
 */
public interface IMenuService extends Dao<SysPremisson>{
	public List<SysPremisson> queryAll() throws Exception;

	List<SysPremisson> userList(SysUser sysUser) throws Exception;

	List<SysPremisson> parentSysPremissonList() throws Exception;

	List<SysPremisson> childSysPremissonList() throws Exception;

	List<SysPremisson> getChildSysPremissonList(Integer parentId) throws Exception;

	List<SysPremisson> roleList(SysUser sysUser) throws Exception;
}