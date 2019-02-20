package com.common.web;

import java.util.List;

@SuppressWarnings("rawtypes")
public class DatatablesJson {
	private List aaData;

	public List getAaData() {
		return aaData;
	}

	public void setAaData(List aaData) {
		this.aaData = aaData;
	}

	public DatatablesJson(List aaData)
	{
		super();
		this.aaData = aaData;
	}

}
