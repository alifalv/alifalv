package com.legal.app.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.common.dbutil.Paging;
import com.legal.app.model.WalletRecord; 

public interface WalletRecordService extends Dao<WalletRecord> {
	
	/**
	 * 保存个人钱包流水
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public int saveWalletRecord(Map<String, Object> m) throws Exception;
	
	
	/**
	 * 
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public List  listWalletRecord(Map<String,Object> m , Paging paging  ) throws Exception;

}
