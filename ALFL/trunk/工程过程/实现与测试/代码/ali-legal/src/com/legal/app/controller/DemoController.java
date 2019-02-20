package com.legal.app.controller;

import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.web.WebUtils;
import com.legal.app.model.Book;
import com.legal.app.service.BookService;

@Controller
@RequestMapping("/demo")
public class DemoController extends SuperController{

	@Autowired
	private BookService bookService;
	
	@RequestMapping("test")
	public void test(PrintWriter out){
		
		try {
			List<Book> list = bookService.queryAll();
			out.print(WebUtils.responseData(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
