package com.legal.app.controller.model;

import java.math.BigDecimal;

/**
 * 驾驶证信息
 * @author admin
 *
 */
public class Driver {

	private int driverId;
	private String driverName;
	private BigDecimal money;
	public int getDriverId() {
		return driverId;
	}
	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	
}
