package com.legal.app.service;

import com.common.dbutil.Paging;
import com.legal.app.model.Article;
import com.legal.app.model.ResearchParameter;

import java.util.List;

public interface IArticleService {
    List<Article> list(Article article) throws Exception ;

	int remove(Integer articleId) throws Exception ;

	List<Article> list(ResearchParameter researchParameter, Paging paging) throws Exception;

	int numberOfComplaints() throws Exception;

	int numberOfAliReferee() throws Exception;

	int numberOfLawTrain() throws Exception;
}