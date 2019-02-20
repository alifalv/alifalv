package com.legal.app.service.impl;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.legal.app.model.Article;
import com.legal.app.model.ResearchParameter;
import com.legal.app.service.Constant;
import com.legal.app.service.IArticleService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleService extends DaoMybatisImpl<Article> implements IArticleService {
	@SuppressWarnings("unchecked")
	@Override
	public List<Article> list(Article article) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("title", article.getTitle());
		map.put("columnCode", article.getColumnCode());
		return super.executeQuery("selectPageList", map);
	}

	@Override
	public int remove(Integer articleId) throws Exception {
		Article article = new Article();
		article.setArticleId(articleId);
		article.setIsDelete(Constant.DELETED);
		return super.executeUpdate("update", article);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Article> list(ResearchParameter researchParameter, Paging paging) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("type", researchParameter.getType());
		map.put("content", researchParameter.getContent());
		map.put("addTimeStart", researchParameter.getAddTimeStart());
		map.put("addTimeEnd", researchParameter.getAddTimeEnd());
		List list = super.executeQuery("selectPageList", paging, map);
		if (null != list && list.size() > 0) {
			paging = (Paging) list.get(list.size() - 1);
			list.remove(list.size()-1);
		}
		return list;
	}

	@Override
	public int numberOfComplaints() throws Exception {
		return (Integer) super.executeQueryOne("numberOfComplaints");
	}

	@Override
	public int numberOfAliReferee() throws Exception {
		return (Integer) super.executeQueryOne("numberOfAliReferee");
	}

	@Override
	public int numberOfLawTrain() throws Exception {
		return (Integer) super.executeQueryOne("numberOfLawTrain");
	}
}
