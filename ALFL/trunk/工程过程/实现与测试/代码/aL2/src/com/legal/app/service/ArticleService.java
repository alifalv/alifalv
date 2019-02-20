package com.legal.app.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.common.dbutil.Paging;
import com.legal.app.model.Article;

public interface ArticleService extends Dao<Article>{
	
	public List<Map> findArticleByColumnId(Map<String, Object> map,Paging paging) throws Exception;
	
	public List<Map> findAliAdjudicationList(Map<String, Object> map,Paging paging) throws Exception;
	
	public List<Map> findLegalTrainListByApp(Map<String, Object> map,Paging paging) throws Exception;
	
	public List<Map> findLegalTrainListByManage(Map<String, Object> map,Paging paging) throws Exception;
	
	public int  deleteArticle(int articleId) throws Exception;
	
	public List listCollectionJoinArticle(Map map,Paging paging) throws Exception;
	
	/**
	 * 查询所有文章的基础表数据，没有联表查询的； 只查 Article 表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List listArticle(Map map,Paging paging) throws Exception;
	
	/**
	 * 获取现在最大的置顶排序数;
	 * @return
	 * @throws Exception
	 */
	public int getMaxIndexs() throws Exception;
	
	/**
	 * 法律法规表查询
	 * @param map
	 * @param paging
	 * @return
	 * @throws Exception
	 */
	public List<Map> legalList(Map<String, Object> map,Paging paging) throws Exception;
	
	
	
	/**
	 * 保存点赞的明细
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int saveArticleLike(Map map) throws Exception;
	
	/**
	 * 修改点赞的数量 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateLikeNum(Map map) throws Exception; 
	
	/**
	 * 根据点赞Id 删除 点赞数据；
	 * @param articleLikeId
	 * @return
	 * @throws Exception
	 */
	public int deleteArticleLike(int articleLikeId) throws Exception;
	
	/**
	 * 查询文章的点赞明细，没有联表查询；
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map> listArticleLike(Map map) throws Exception;
	
	/**
	 * 修改文章的浏览数量
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateLookNum(Map map) throws Exception;
	
	
	/**
	 * 修改文章的收藏数量
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateCollectionNum(Map map) throws Exception;
	
	/**
	 * 保存收藏记录 
	 * PS: 处理的过程，当登陆者已经收藏过这篇文章，再次点击的时候，就会取消这次收藏，
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int saveArticleCollection(Map map) throws Exception;
	
	/**
	 *  根据收藏的Id 删除收藏记录
	 * @param collectionId 
	 * @return
	 * @throws Exception
	 */
	public int deleteArticleCollection(int collectionId) throws Exception;
	
	/**
	 * 根据map 中的值 查询出收藏的记录，没有联表查询；
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map> listArticleCollection(Map map) throws Exception;
	
	public Map findArticleById(int articleId) throws Exception;
	
	
	/**
	 * 文章的详情，包括了上一页与下一页，用户是否收藏与点赞；
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public Map  articleDetails(Map m) throws Exception;
	
	/**
	 * 判断是否点赞
	 * @param userId
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	public int isCollection(int userId,int articleId) throws Exception;
	
	public int isLike(int userId,int articleId) throws Exception;
	
	/**
	 * 我收藏的文章
	 * @return
	 * @throws Exception
	 */
	public List<Map> findCollectionArticle(int userId) throws Exception;
	
	/**
	 * 保存(百姓吐槽)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int saveArticle(Map map) throws Exception;
	
	/**
	 * 修改百姓吐槽
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateArticle(Map map) throws Exception;
	
	
	/**
	 * 设置和取消今日推荐
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateRecommend(Map map) throws Exception;
	
	/**
	 * 修改法律法规的扩展表数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateLegal(Map map) throws Exception;
	
	
	/**
	 * 保存文章和申报二个表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int saveArticleAndDeclare (Map map) throws Exception;
	
	
	/**
	 * 修改文章和申报的二个表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int  updateArticleAndDeclare(Map map) throws Exception;
	
	/**
	 * 发布法律法规
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public int sendLegal(Map m) throws Exception;
	/**
	 * 法律培训
	 * @return
	 */
	public int sendLegalTrain(Map m) throws Exception;
	
	/**
	 * 修改法律培训
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public int updateTrain(Map m) throws Exception;
	
	
	/**
	 * 查询文章类型列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map> commonPeopleMessageListByTotal(Map<String, Object> map,Paging paging) throws Exception;
	
	/**
	 * 法律法规查询
	 * @param m 查询参数
	 * @param paging 分页对象
	 * @return
	 * @throws Exception 
	 */
	public List<Map> findLegal(Map m,Paging paging) throws Exception;
	
	/**
	 * 统计 个人的关注、咨询、收藏数量 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map listATT_COLL_CONS(int userId) throws Exception;
	/**
	 * 最新法律法规查询
	 * @return
	 */
	public List newestLegalList();
	 
	/**
	 ** 首页获取所有今日推荐文章的
	 * @return
	 */
	public List todayPushlistArticle();
	/**
	 * 最新法律法规查询200
	 * @return
	 */
	public List newest200LegalList(Map m,Paging paging);
	
	
	
}
