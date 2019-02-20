package com.legal.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.dbutil.DaoMybatisImpl;
import com.common.dbutil.Paging;
import com.common.log.ExceptionLogger;
import com.common.utils.SystemConfigUtil;
import com.legal.app.controller.model.Address;
import com.legal.app.controller.model.City;
import com.legal.app.model.Article;
import com.legal.app.service.ArticleService;
import com.legal.app.service.UserService;
import com.legal.app.utils.SendArticleType;


@SuppressWarnings({"rawtypes","unchecked"})
@Service
public class ArticleServiceImpl extends DaoMybatisImpl<Article> implements ArticleService{
	@Autowired
	private UserService userService;
	

	@Override
	public List listArticle(Map map,Paging paging) throws Exception {
		 return super.executeQuery("listArticle",paging,map);
	}

	@Override
	public List<Map> findArticleByColumnId(Map<String, Object> map,Paging paging) throws Exception {
		return super.executeQuery("findArticleListByColumnCode", paging, map);
	}
	
	@Override
	public List<Map> commonPeopleMessageListByTotal(Map<String, Object> map,Paging paging) throws Exception {
 	   return  super.executeQuery("commonPeopleMessageList", paging,map); 
	}

	@Override
	public List<Map> legalList(Map<String, Object> map, Paging paging) throws Exception {
		List<Map>   listM =  super.executeQuery("legalList", paging, map);
		 if(null != listM && listM.size() > 0 ) {
			 for(Object m : listM) {
				 if(m instanceof  Map) {
					 String unitName="";
					 if(null!=((Map)m).get("sendUnit")&&!((Map)m).get("sendUnit").equals("")){
					 if(((Map)m).get("effectLevel").toString().equals("5")){
					 	 unitName = SystemConfigUtil.getValue(Integer.parseInt((String)((Map)m).get("sendUnit")), SystemConfigUtil.TYPE_UNIT_ADDRESS);
					 }else{
						 unitName = SystemConfigUtil.getValue(Integer.parseInt((String)((Map)m).get("sendUnit")), SystemConfigUtil.TYPE_UNIT);
					 } 
					 }
					((Map)m).put("unitName", unitName); //发文单位 
					((Map)m).put("egName", ((Map)m).get("effectLevel")); // 效力等级 
				 }
			 }
		 }
		
		return listM;
	}

	@Override
	public List<Map> findAliAdjudicationList(Map<String, Object> map,
			Paging paging) throws Exception {
		return super.executeQuery("findAliAdjudicationList", paging,map);
	}

	@Override
	public List<Map> findLegalTrainListByApp(Map<String, Object> map,
			Paging paging) throws Exception {
		List list =  super.executeQuery("findLegalTrainListByApp", paging,map);
		if(null != list && list.size() > 0 ) {
			for(Object obj : list) {
				if(obj instanceof Map){
					Map m = (Map)obj;
					//如果是法律培训
					String provinceName="";
					Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+m.get("province"));
					provinceName = add.name;
					Map cityM = add.getCity();
					City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+m.get("city"));
					String cityName = city.getCityName();
					m.put("provinceName", provinceName);
					m.put("cityName", cityName);
				}
			}
		}
		return list ;
	}

	@Override
	public List<Map> findLegalTrainListByManage(Map<String, Object> map,
			Paging paging) throws Exception {
		return null;
	}

	@Override
	public Map findArticleById(int articleId) throws Exception {
		return (Map)super.executeQueryOne("findArticleById", articleId);
	}

	@Override
	public int saveArticleCollection(Map map) throws Exception {  
		
		return super.executeUpdate("saveArticleCollection", map);
	}

	@Override
	public int deleteArticleCollection(int collectionId) throws Exception {  
		return super.executeUpdate("deleteArticleCollection", collectionId);
	}

	@Override
	public List<Map> listArticleCollection(Map map) throws Exception {
		return super.executeQuery("listArticleCollection", map);
	}

	@Override
	public int isCollection(int userId, int articleId) throws Exception {
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("userId", userId);
		m.put("articleId", articleId);
		return (int)super.executeQueryOne("isCollection", m);
	}
	
	

	@Override
	public int isLike(int userId, int articleId) throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("userId", userId);
		m.put("articleId", articleId);
		return (int)super.executeQueryOne("isLike", m);
	}

	@Override
	public List<Map> findCollectionArticle(int userId) throws Exception {
		return super.executeQuery("findCollectionArticle", userId);
	}

	@Override
	public int saveArticle(Map map) throws Exception {
		super.executeUpdate("saveArticle", map);
		return 1;
	} 
	
	@Override
	public int updateRecommend(Map map) throws Exception { 
		return  super.executeUpdate("updateRecommend", map);
	}

	public  int updateArticle(Map map) throws Exception{
	   super.executeUpdate("updateArticle", map);
	   return 1;
	}
	
	
	

	@Override
	public int updateLegal(Map map) throws Exception {
	   super.executeUpdate("updateLegal", map);
		return 1;
	}

	@Override
	public int saveArticleAndDeclare(Map map) throws Exception {
		super.executeUpdate("saveArticle", map); 
		super.executeUpdate("saveDeclare", map); 
		return 1;
	}
	

	@Override
	public int updateArticleAndDeclare(Map map) throws Exception {
		super.executeUpdate("updateArticle", map);
		super.executeUpdate("updateDeclare", map);
		return 1;
	}
	

	@Override
	public int getMaxIndexs() throws Exception {
		List list = super.executeQuery("getMaxIndexs");
		Integer indexs = Integer.valueOf((((Map)list.get(0)).get("indexs")).toString());
		return indexs;
	}

	@Override
	public int sendLegal(Map m) throws Exception {
		super.executeUpdate("sendLegal", m);
		return 1;
	}

	@Override
	public int sendLegalTrain(Map m) throws Exception {
		super.executeUpdate("sendLegalTrain", m);
		return 1;
	}
	
	
	
	

	@Override
	public int updateTrain(Map m) throws Exception { 
		return  super.executeUpdate("updateTrain", m);
	}

	@Override
	public  Map  articleDetails(Map m) throws Exception { 
		List<Map> listM = super.executeQuery("articleDetails", m);
		Map<String,Object> map= new HashMap<String,Object>();
		if(null != listM && listM.size() >0) {
			map=listM.get(0);
			//根据文章是官方以布还是用户，查询出用户的头像；
			Object isofficial = map.get("isofficial");
			Object userId = map.get("userId");
			if(isofficial.toString() == "0" || isofficial.toString().equals("0")) {
				//查询这一用户信息. 返回官方图片；
				String userImg = "aliImgs/systemImg/ali.png";
				map.put("userImg",userImg);
			}else {
				//查询出这个用户的图片；
				Map userMap = this.userService.getUserInfo(userId.toString());
				map.put("userImg", userMap.get("userImg"));
			}
			
			
			//把文章的栏目 转换为中文
			String code = (String)map.get("columnCode");
			String columnCodeName =SendArticleType.getTypeName(Integer.valueOf(code));
			map.put("columnCodeName", columnCodeName);
			if(code.equals(SendArticleType.SENDARTICLETYPE_9)) {
				//如果是查询阿里裁决的，需要把裁决类型展示出来；
				String declareName = SystemConfigUtil.getValue((int)map.get("declareType"), SystemConfigUtil.TYPE_DECLARE);
				map.put("declareName", declareName);
			}else if(code.equals(SendArticleType.SENDARTICLETYPE_7)) {
				//如果是法律培训
				String provinceName="";
				Address add =  SystemConfigUtil.addressMap.get(SystemConfigUtil.TYPE_ADDRESS+map.get("province"));
				provinceName = add.name;
				Map cityM = add.getCity();
				City city = (City)cityM.get(SystemConfigUtil.TYPE_CITY+map.get("city"));
				String cityName = city.getCityName();
				map.put("provinceName", provinceName);
				map.put("cityName", cityName);
			}else if(code.equals(SendArticleType.SENDARTICLETYPE_8)) {
				//法律法规查询
				// String unitName = SystemConfigUtil.getValue(Integer.parseInt((String)((Map)map).get("sendUnit")), SystemConfigUtil.TYPE_UNIT);
				
				 String unitName="";    //xiug 
				 if(null!=((Map)map).get("sendUnit")){
				 if(((Map)map).get("effectLevel").toString().equals("5")){
				 	 unitName = SystemConfigUtil.getValue(Integer.parseInt((String)((Map)map).get("sendUnit")), SystemConfigUtil.TYPE_UNIT_ADDRESS);
				 }else{
					 unitName = SystemConfigUtil.getValue(Integer.parseInt((String)((Map)map).get("sendUnit")), SystemConfigUtil.TYPE_UNIT);
				 } 
				 }
	

				//String unitName = (String)map.get("sendUnit");
				String egName = SystemConfigUtil.getValue((int)map.get("effectLevel"), SystemConfigUtil.TYPE_EG);
				map.put("unitName", unitName); //发文单位 
				map.put("egName", egName); // 效力等级 
			}
			
			String lastArticleId = "";
			String lastArticleTitle="";
			String nextArticleId = "";
			String nextArticleTitle="";
			
			String gt = (String)map.get("gt");
			if(null != gt) {
				String [] gt_tmp = gt.split("8m_m_8");
				nextArticleId = gt_tmp[0];
				if(gt_tmp.length > 1) {
					nextArticleTitle = null == gt_tmp[1] ? ""  : gt_tmp[1];
				}
			}  
			
			String lt = (String)map.get("lt"); 
			if(null != lt) {
				String  [] lt_tmp = lt.split("8m_m_8");
				lastArticleId = lt_tmp[0];
				if(lt_tmp.length > 1) {
					lastArticleTitle =null ==  lt_tmp[1] ? "" : lt_tmp[1];
				}
			}
			
			map.put("lastArticleId", lastArticleId);
			map.put("lastArticleTitle", lastArticleTitle);
			map.put("nextArticleId", nextArticleId);
			map.put("nextArticleTitle", nextArticleTitle);
			
		 
			
		}		 
		return map;
	}

	@Override
	public int saveArticleLike(Map map) throws Exception {
		return super.executeUpdate("saveArticleLike", map);
	}
	

	@Override
	public int updateLikeNum(Map map) throws Exception { 
		return  super.executeUpdate("updateLikeNum", map);
	}

	@Override
	public int deleteArticleLike(int articleLikeId) throws Exception {
		return   super.executeUpdate("deleteArticleLike", articleLikeId);
	}

	@Override
	public List<Map> listArticleLike(Map map) throws Exception {
		return super.executeQuery("listArticleLike",map);
	}

	@Override
	public int updateLookNum(Map map) throws Exception {
		super.executeUpdate("updateLookNum", map);
		return 1;
	}

	@Override
	public int updateCollectionNum(Map map) throws Exception {
		return super.executeUpdate("updateCollectionNum", map);
	}

	@Override
	public List<Map> findLegal(Map m, Paging paging) throws Exception {
		return super.executeQuery("findLegalList", paging, m);
	}

	@Override
	public Map listATT_COLL_CONS(int userId) throws Exception {
	  List<Map> m =  super.executeQuery("listATT_COLL_CONS", userId);
	  Long advice = 0L;
	  Long followCount = 0L;
	  Long collectionCount = 0L;
	  Map<String,Object> map = new HashMap<String,Object>();
	  if(null != m && m.size() > 0 ) {
		  collectionCount = (Long) m.get(0).get("counts");
		  followCount = (Long) m.get(1).get("counts");
		  advice = (Long) m.get(2).get("counts");
	  }
	  map.put("adviceCount", advice);
	  map.put("followCount", followCount);
	  map.put("collectionCount", collectionCount);
	  return map;
	}

	@Override
	public int deleteArticle(int articleId) throws Exception { 
		return  super.executeUpdate("deleteArticle", articleId); 
	}

	@Override
	public List listCollectionJoinArticle(Map maps,Paging paging) throws Exception {
		List<Map> list  = super.executeQuery("listCollectionJoinArticle", paging, maps);
		if(null != list && list.size() > 0) {
			 for(Object m : list) {
				 if(m instanceof Map) {
					 Map map = (Map)m;
					 String columnName = "";
					 System.out.println(map);
					 columnName = SendArticleType.getTypeName(Integer.parseInt(map.get("columnCode").toString()));
					 map.put("columnName",columnName);
				 }
			 }
		}
		return list;
	}

	@Override
	public List newestLegalList() {
		try {
			return super.executeQuery("newestLegalList");
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionLogger.writeLog(e, e.getClass());
		}
		return null;

	}

	@Override
	public List todayPushlistArticle() {
		try {
			return super.executeQuery("todayPushlistArticle");
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionLogger.writeLog(e, e.getClass());
		}
		return null;
	}

	@Override
	public List newest200LegalList(Map m,Paging paging) {
		try {
			return super.executeQuery("newest200LegalList", paging, m);

		} catch (Exception e) {
			e.printStackTrace();
			ExceptionLogger.writeLog(e, e.getClass());
		}
		return null;
	} 
	
}
