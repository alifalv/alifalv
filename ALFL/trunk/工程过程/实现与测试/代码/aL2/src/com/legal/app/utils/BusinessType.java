package com.legal.app.utils;

/**
 * 回复帖子的类型
 * @author Administrator
 *
 */
public class BusinessType {
	 
//	 * 业务类型咨询(回复)
//	 
//	public final static int REPLY_ADVICE = 1;
//	
//	 
//	 * 业务类型竞拍(回复)
//	 
//	public final static int REPLY_OFFER= 2;
//	
// 
//	 * 业务类型百姓吐槽(回复)
//	 
//	public final static int REPLY_MAKE_COMPLAINTS= 3;
//	
//	 
//	 * 业务类型阿里裁决(回复)
//	 
//	public final static int REPLY_ALI_ADJUDUCATION= 4;
//	
// 
	

	
	/**
	 * 业务类型百姓吐槽(回复)
	 */
	public final static int REPLY_MAKE_COMPLAINTS = 1;
	
	 /**
	 * 业务类型阿里裁决(回复)
	 */
	public final static int REPLY_ALI_ADJUDUCATION = 2;
	
	/**
	 * 业务类型法律咨询(回复)
	 **/
	public final static int REPLY_ADVICE = 3;
	
	/** 
	 * 业务类型案件竞拍(回复)
	 **/
	public final static int REPLY_OFFER =4;
	
	/**
	 * 业务类型文书制作（回复）
	 */
	public final static int REPLY_DECOUMENT = 5;
	 
	/** 
	 * 业务类型注册(系统)
	 **/
	public final static int SYS_REGISTER =6;
	
	
	private static final String []  REPLY_TYPENAME= {"","百姓吐槽","阿里裁决","法律咨询","案件委托","文书制作"};

	/**
	 * 这里与SendArticleType的 typeName 不一样。SendArticleType 里面主针对订单的类型；此处是针对回复时的类型
	 * 获取统一回复类型的中文名称；
	 * @param index 回复类型编号
	 * @return
	 */
	public static String getReplayTypeName(Integer index) {
		String typeName="";
		if(null != index) {
			typeName = REPLY_TYPENAME[index];
		}
		return typeName;
	} 
	 
	
}
