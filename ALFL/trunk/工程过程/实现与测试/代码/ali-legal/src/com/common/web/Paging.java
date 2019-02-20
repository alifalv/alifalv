package com.common.web;

import java.util.List;
import java.util.Map;

/**
 * 类、接口说明
 *
 * @author zhujun
 * @version 1.0
 */
public class Paging<T> {
	//Fields
	private Integer rowCount;//总行数
	private Integer start;//起始行
	private Integer limit;//每页大小
	private Integer currentPage;//当前页数
	private List<T> list;//分页列表	
	private Map<String, Object> myprop;//附加统计属性3
	private List<Map<String,Object>> listMap;//分页列表
		
	public List<Map<String, Object>> getListMap() {
		return listMap;
	}

	public void setListMap(List<Map<String, Object>> listMap) {
		this.listMap = listMap;
	}

	// Constructors
	public Paging(){
		
	}
	
	public Paging(Integer start, Integer limit){
		this.start = start;
		this.limit = limit;
	}
	
	public Paging(Integer start, Integer limit, List<T> list){
		this.start = start;
		this.limit = limit;
		this.list = list;
	}
	
	
	//Property accessors
	/**
	 * 
	 * @return 总记录数
	 */
	public Integer getRowCount() {
		return rowCount;
	}
	/**
	 * @param rowCount 总记录数
	 */
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
	/**
	 * 
	 * @return 返回分页列表的起始记录数
	 */
	public Integer getStart() {
		return start;
	}
	/**
	 * @param start 返回分页列表的起始记录数
	 */
	public void setStart(Integer start) {
		this.start = start;
	}
	/**
	 * 
	 * @return 每页记录数
	 */
	public Integer getLimit() {
		return limit;
	}
	/**
	 * @param limit 每页记录数
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	/**
	 * 
	 * @return 分页记录列表
	 */
	public List<T> getList() {
		return list;
	}
	/**
	 * @param list 分页记录列表
	 */
	public void setList(List<T> list) {
		this.list = list;
	}	
	
	
	/**
	 * @return 当前页数
	 */
	public Integer getCurrentPage(){
		if( this.limit != null && this.start != null ){
			return this.start/this.limit+1;
		}
		return this.currentPage;
	}
	
	/**
	 * @param currentPage 当前页数
	 */
	public void setCurrentPage(Integer currentPage){
		this.currentPage = currentPage;
	}
	
	/**
	 * @return 附加统计属性
	 */
	public Map<String, Object> getMyprop() {
		return myprop;
	}
	
	
	/**
	 * @param myprop 附加统计属性
	 */
	public void setMyprop(Map<String, Object> myprop) {
		this.myprop = myprop;
	}
}
