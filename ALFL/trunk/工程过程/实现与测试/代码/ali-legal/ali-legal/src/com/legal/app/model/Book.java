package com.legal.app.model;

import java.io.Serializable;

import com.common.dbutil.MyBatisEntity;
@MyBatisEntity(namespace="book")
public class Book implements Serializable{
	private static final long serialVersionUID = -1332240447131786228L;

	private Integer bookId;
	
	private String bookName;

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	
}
