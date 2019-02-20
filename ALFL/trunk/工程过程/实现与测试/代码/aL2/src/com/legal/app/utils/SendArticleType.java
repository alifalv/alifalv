package com.legal.app.utils;

/**
 * 发布文章时，类型的标记；
 * 针对写 ali_article 表中的   columnCode
 * @author Administrator
 *
 */
public class SendArticleType {
	/**
	 * 百姓吐槽
	 */
   public static final String SENDARTICLETYPE_0="0"  ; //"bxtc";
   
   /**
	 * 法律疑难问题与观点
	 */
  public static final String SENDARTICLETYPE_1="1";//"flynwtygd";
  
  /**
	 * 公务员招聘
	 */
 public static final String SENDARTICLETYPE_2="2";//"gwyzp";
 
 /**
	 * 常用文书模板
	 */
public static final String SENDARTICLETYPE_3="3";//"cywsmb";

/**
	 * 文书制作知识
	 */
public static final String SENDARTICLETYPE_4="4";//"wszzzs";

/**
	 * 法律人信息
	 */
public static final String SENDARTICLETYPE_5="5";//"flrdxx";

/**
	 * 常见咨询问题
	 */
public static final String SENDARTICLETYPE_6="6";//"cjzxwt";

/**
 * 法律培训
 */
public static final String SENDARTICLETYPE_7="7";//"flpx";

/**
 * 法律法规
 */
public static final String SENDARTICLETYPE_8="8";//"flfg";

/**
 * 阿里裁决
 */
public static final String SENDARTICLETYPE_9="9";//"alcj";

/**
 * 我的文采
 */
public static final String SENDARTICLETYPE_10="10";//"wdfc";

/**
 * 工作动态
 */
public static final String SENDARTICLETYPE_11="11";//"gzdt";

/**
 * 成功案例
 */
public static final String SENDARTICLETYPE_12="12";//"cgal";


/**
 * 我的随笔
 */
public static final String SENDARTICLETYPE_13="13";//"wdsb";

/**
 * 法律咨询
 */
public static final String SENDARTICLETYPE_14="14";//"wdsb";

/**
 * 案件委托
 */
public static final String SENDARTICLETYPE_15="15";//"wdsb";

private static final String []  typeName= {"百姓吐槽","法律疑难问题与观点","公务员招聘","常用文书模板","文书制作知识","法律人信息","常见咨询问题","法律培训","法律法规","阿里裁决","我的风采","工作动态","成功案例","我的随笔","法律咨询","案件委托","个人主页"};


/**
 * 传入columnCode 返回相对应的中文名称；
 * @param code
 * @return
 */
public static String getTypeName(int code) {
	return SendArticleType.typeName[code];
}

/**
 * 返回统一 code;
 * @param columnCode
 * @return
 */
public  static   String getType(int columnCode) {
	String type = "";
	switch (columnCode) {
		case 0:
			type = SendArticleType.SENDARTICLETYPE_0;
			break;
		case 1:
			type = SendArticleType.SENDARTICLETYPE_1;
			break; 
		case 2:
			type = SendArticleType.SENDARTICLETYPE_2;
			break; 
		case 3:
			type = SendArticleType.SENDARTICLETYPE_3;
			break; 
		case 4:
			type = SendArticleType.SENDARTICLETYPE_4;
			break; 
		case 5:
			type = SendArticleType.SENDARTICLETYPE_5;
			break; 
		case 6:
			type = SendArticleType.SENDARTICLETYPE_6;
			break; 
		case 7:
			type = SendArticleType.SENDARTICLETYPE_7;
			break;  
		case 8:
			type = SendArticleType.SENDARTICLETYPE_8;
			break;  
		case 9:
			type = SendArticleType.SENDARTICLETYPE_9;
			break;  
		case 10:
			type = SendArticleType.SENDARTICLETYPE_10;
			break;  
		case 11:
			type = SendArticleType.SENDARTICLETYPE_11;
			break;  
		case 12:
			type = SendArticleType.SENDARTICLETYPE_12;
			break;  
		case 13:
			type = SendArticleType.SENDARTICLETYPE_13;
			break;  
		case 14:
			type = SendArticleType.SENDARTICLETYPE_14;
			break; 
		case 15:
			type = SendArticleType.SENDARTICLETYPE_15;
			break; 
		default:
			type = "";
			break;
		}
	
	return type;
}

/**
 根据
 **/
public  static String getIspush(String sendArticleType,String isofficial) {
	String  isPush = "0";
	if(null != isofficial && isofficial.equals("0")) {
		//官方发布；都是需要审核的；
		return isPush;
	}else { 
		if( 
				//百姓吐槽
				sendArticleType .equals(SendArticleType.SENDARTICLETYPE_0) ||
				//阿里裁决
				sendArticleType .equals(SendArticleType.SENDARTICLETYPE_9) ||
				//法律培训
				sendArticleType .equals(SendArticleType.SENDARTICLETYPE_7) 
		  ) {
			return isPush;
		  } else {
			  return "1";
		  }
	}
}
 
}
