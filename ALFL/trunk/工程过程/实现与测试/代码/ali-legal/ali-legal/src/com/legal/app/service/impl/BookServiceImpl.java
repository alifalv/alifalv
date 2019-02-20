package com.legal.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.common.dbutil.DaoMybatisImpl;
import com.legal.app.model.Book;
import com.legal.app.service.BookService;
@Service
@SuppressWarnings("unchecked")
public class BookServiceImpl extends DaoMybatisImpl<Book> implements BookService{

	@Override
	public List<Book> queryAll() throws Exception {
		return super.executeQuery("queryAll");
	}

}
