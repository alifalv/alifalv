package com.legal.app.controller.model;

import java.util.Map;

/**
 * 省份与城市 
 * @author Administrator
 *
 */
public class Address {
	
	public String id;
	public String name;
	//用于封装城市
	public Map<String,City> city;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, City> getCity() {
		return city;
	}
	public void setCity(Map<String, City> city) {
		this.city = city;
	}
	

}
