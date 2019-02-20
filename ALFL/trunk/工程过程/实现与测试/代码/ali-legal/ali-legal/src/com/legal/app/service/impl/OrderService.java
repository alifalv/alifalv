package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.BusinessOrder;
import com.legal.app.model.BusinessOrderSelectParam;
import com.legal.app.service.Constant;
import com.legal.app.service.IOrderService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService extends DaoMybatisImpl<BusinessOrder> implements IOrderService {
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public List<BusinessOrder> list(BusinessOrderSelectParam businessOrderSelectParam, Paging paging) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("createTimeStart", businessOrderSelectParam.getCreateTimeStart());
        map.put("createTimeEnd", businessOrderSelectParam.getCreateTimeEnd());
        map.put("content", businessOrderSelectParam.getContent());
        map.put("businessType", businessOrderSelectParam.getBusinessType());
        map.put("payment", businessOrderSelectParam.getPayment());
        List list = super.executeQuery("selectPageList", paging, map);

        if (null != list && list.size() > 0) {
            paging = (Paging) list.get(list.size() - 1);
            list.remove(list.size()-1);
        }

        return list;
    }

    @Override
    public BusinessOrder info(String businessOrder) throws Exception {
        BusinessOrder info = new BusinessOrder();
        info.setBusinessOrder(businessOrder);
        info = (BusinessOrder) super.executeQueryOne("queryInfo", info);
        return info;
    }

    @Override
    public int check(String businessOrder) throws Exception {
        BusinessOrder checkBO = new BusinessOrder();
        checkBO.setBusinessOrder(businessOrder);
        checkBO.setOrderState(Constant.BUSINESS_CHECKED);
        checkBO.setCheckTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return super.executeUpdate("update", checkBO);
    }

    @Override
    public int checkUn(String businessOrder) throws Exception {
        BusinessOrder checkBO = new BusinessOrder();
        checkBO.setBusinessOrder(businessOrder);
        checkBO.setOrderState(Constant.BUSINESS_CHECKED_UN);
        return super.executeUpdate("update", checkBO);
    }

	@SuppressWarnings("rawtypes")
	@Override
	public int totalCount(Map<String, Object> map) throws Exception {
		List list = super.executeQuery("totalCount", map);
		if(list.get(0)==null) {
			return 0;
		}else {
			return (int) list.get(0);
		}
	}
}
