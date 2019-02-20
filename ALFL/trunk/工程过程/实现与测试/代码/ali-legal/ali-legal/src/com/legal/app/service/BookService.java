package com.legal.app.service;

import java.util.List;

import com.common.dbutil.Dao;
import com.legal.app.model.Book;

/**
 * 案例  测试代码
 * @author hzh
 *
 */
public interface BookService extends Dao<Book>{

	public List<Book> queryAll() throws Exception;
} 
