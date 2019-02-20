package com.legal.app.model;

import java.io.Serializable;

import com.common.dbutil.MyBatisEntity;

/**
 * 敏感词
 * @author admin
 *
 */
@MyBatisEntity(namespace="Sensitive")
public class Sensitive implements Serializable{

	private Integer sensitiveId;
	private String  sensitiveValue;
	public Integer getSensitiveId() {
		return sensitiveId;
	}
	public void setSensitiveId(Integer sensitiveId) {
		this.sensitiveId = sensitiveId;
	}
	public String getSensitiveValue() {
		return sensitiveValue;
	}
	public void setSensitiveValue(String sensitiveValue) {
		this.sensitiveValue = sensitiveValue;
	}
	
	
}
