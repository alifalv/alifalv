package com.legal.app.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.WalletRecord;
import com.legal.app.service.WalletRecordService;
@Service
public class WalletRecordServiceImpl extends DaoMybatisImpl<WalletRecord> implements WalletRecordService {

	@Override
	public int saveWalletRecord(Map<String, Object> m) throws Exception { 
		return super.executeUpdate("saveWalletRecord", m);
	}

	@Override
	public List listWalletRecord(Map<String, Object> m,Paging paging) throws Exception {
		// TODO Auto-generated method stub
		return super.executeQuery("listWalletRecord", paging, m);
	}

}
