package com.legal.app.utils;
 
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.common.log.BusinessException;

/**
 * 检测评分的标准
 * @author Administrator
 *
 */
public class CheckEvaluate {

	
	public static  void check(Map<String, Object> map) throws Exception{
		   String evaluateType = (String)map.get("evaluateType");
		   if(evaluateType  == null || evaluateType.equals("")) {
			   throw new BusinessException("评价的类型不能为空.",-1);
		   }    
		   int goodNum = 0;//好评数量;
		   int middleNum = 0;//中评数量
		   int badNum = 0;//差评数量
		   int counts = 0 ;//统计用了多少个
		   
		   Double sourceNum = 0.0D;//社会资源评分（单项）
		   Double scaleNum = 0.0D;//收费标准评分（单项）
		   Double levelNum = 0.0D;//发律水平评分（单项）
		   Double mannerNum = 0.0D;//服务态度评分（单项）
		   
		   
		   Double allScore = 0D;
		   
		   map.put("goodNum", goodNum); //
		   map.put("middleNum", middleNum);
		   map.put("badNum", badNum);
		   map.put("allScore", allScore);
		   
		 //如果等于案件委托，评价类型
		   if(evaluateType.equals(PayOrder.TYPE_AJWT)) {
			   //判断四个评分项是否都有；
			   Object sourceScore_ =map.get("sourceScore");
			   if(sourceScore_  == null) {
				   throw new BusinessException("社会资源评分不能为空.",-1);
			   }  
			   Integer sourceScore = Integer.valueOf((String)sourceScore_);
			   if((int)sourceScore  == 0 ||  (int)sourceScore  > 5) {
				   throw new BusinessException("社会资源评分不合规则.",-1);
			   }
			   CheckEvaluate.getGrade(sourceScore, map);
			   sourceNum = CheckEvaluate.getScore(sourceScore, map);
			   counts++;
			   
			   Object scaleScore_ = map.get("scaleScore");
			   if(scaleScore_  == null) {
				   throw new BusinessException("收费标准评分不能为空.",-1);
			   }  
			   Integer scaleScore =  Integer.valueOf((String)scaleScore_);
			   if((int)scaleScore  == 0 ||  (int)scaleScore  > 5) {
				   throw new BusinessException("收费标准评分不合规则.",-1);
			   }
			   CheckEvaluate.getGrade(scaleScore, map);
			   scaleNum = CheckEvaluate.getScore(scaleScore, map);
			   counts++;
		   }
		   
		   Object levelScore_ = map.get("levelScore");
		   if(levelScore_  == null) {
			   throw new BusinessException("法律水平评分不能为空.",-1);
		   }  
		   Integer levelScore = Integer.valueOf((String)levelScore_);
		   if((int)levelScore  == 0 ||  (int)levelScore  > 5) {
			   throw new BusinessException("法律水平评分不合规则.",-1);
		   }
		  CheckEvaluate.getGrade(levelScore, map);
		  levelNum = CheckEvaluate.getScore(levelScore, map);
		   counts++;
		   
		   
		   Object mannerScore_ =  map.get("mannerScore");
		   if(mannerScore_  == null) {
			   throw new BusinessException("服务态度评分不能为空.",-1);
		   }  
		   Integer mannerScore =  Integer.valueOf((String)mannerScore_);
		   if((int)mannerScore  == 0 ||  (int)mannerScore  > 5) {
			   throw new BusinessException("服务态度评分不合规则.",-1);
		   }  
		    CheckEvaluate.getGrade(mannerScore, map);
		    mannerNum = CheckEvaluate.getScore(mannerScore, map);
		   counts++;
		   

		   map.put("sourceNum",sourceNum);
		   map.put("scaleNum",scaleNum);
		   map.put("levelNum",levelNum);
		   map.put("mannerNum",mannerNum); 
		   map.put("counts",counts); 
	}
	
	/**
	 * 根据星星的数量，得到是好评还是中评，还是差评 
	 * @param num 星星的数量 
	 * @return 
	 */
	private static void getGrade(int num,Map m) {
		//根据星星算出相对应的【好评，中评，差评】
		if(num == 5 ) {
			int goodNum = (int)m.get("goodNum");
			m.put("goodNum", goodNum+1);
		}else if(num < 5 && num > 1) {
			int middleNum = (int)m.get("middleNum");
			m.put("middleNum", middleNum+1);
		}else if(num == 1) {
			int badNum = (int)m.get("badNum");
			m.put("badNum", badNum+1);
		} 
	}
	
	
	/**
	 * 	 * 并且算出根据星星要对应该的分数；并且累计在总得分中（Map 字段）
	 * @param num 星星的数量
	 * @param m
	 * @return  返回 这项的分数是多少，
	 */
	private static Double   getScore(int num,Map m) {
		//根据星星算出相对应的分数；
		Double allScore = (Double)m.get("allScore");
		Double score = 0D;
		switch (num) {
				case 5:
					score = 0.2D;
					break;
				case 4:
					score = 0.1D;
					break;
				case 3:
					score = 0.0D;
					break;
				case 2:
					score = -0.1D;
					break;
				case 1:
					score = -0.2D;
					break;
				default:
					break;
		}
		m.put("allScore",allScore +score) ;
	   return score;
	}
   
	/**
	 * 根据现要计划的数据，和历史统计的数据，算出咨询师需要修改的值 。如果 四种各项的分数；好评，中评，差评，综合评分等；
	 * @param nowMap
	 * @param backMap
	 * @throws Exception
	 */
	public static void scoreCount(Map nowMap,Map backMap,Map c_user) throws Exception {
		//统计好评的个数；
		 int goodNum =c_user.get("goodNum") == null  ? 0 : (int)c_user.get("goodNum");
		 c_user.put("goodNum",goodNum+(int)nowMap.get("goodNum"));
		 //统计中评的个数；
		 int middleNum =c_user.get("middleNum")== null ? 0 : (int)c_user.get("middleNum");
		 c_user.put("middleNum",middleNum+(int)nowMap.get("middleNum"));
		 //统计差评的个数；
		 int badNum =c_user.get("badNum") == null ? 0 : (int)c_user.get("badNum");
		 c_user.put("badNum",badNum+(int)nowMap.get("badNum"));

        //汇总，总综合分数； 公式 = （历史总分数 +当前总分数 ）/ （历史总次数 + 当前总次数） + 4.8;
		Double allScore = ((BigDecimal) backMap.get("allScore")).doubleValue();//历史总分数
		Double nowAllScore = (Double)nowMap.get("allScore");//当前的分数；
		Double sumScore =  allScore +nowAllScore; //总分数 ；
		Long counts = ((BigDecimal)backMap.get("counts")).longValue(); //历史总次数；
		Long nowCounts = ((Integer)nowMap.get("counts")).longValue();//当前总次数；
		Long sumCounts = counts + nowCounts;//总次数；
		BigDecimal bigA = new BigDecimal(sumScore).setScale(2, BigDecimal.ROUND_DOWN);
		BigDecimal bigB = new BigDecimal(sumCounts).setScale(2, BigDecimal.ROUND_DOWN);
		BigDecimal  total =  bigA.divide(bigB,2,BigDecimal.ROUND_DOWN).add(new BigDecimal("4.8"));
		//保留一位小数四舍五入；
		double result = total.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
		c_user.put("commonScore", result);//总综合评分；
		
		//社会资源 :  公式 = （历史总分数 +当前总分数 ）/ （历史总次数 + 当前总次数） + 4.8; 
		Double nowSourceNum = (Double)nowMap.get("sourceNum");//当前分数； 
		if(nowSourceNum != 0) {  //如果分数不等于0 ；就说明当前项目参与了评分；
			Double sourceNum = ((BigDecimal)backMap.get("sourceNum")).doubleValue();//历史总分数； 
			Double  sumSourceNum = sourceNum+nowSourceNum;//总分数；
			Long sourceCount = ((BigDecimal)backMap.get("sourceCount")).longValue();//历史次数
			sourceCount  +=  1; 
			BigDecimal a_total = new BigDecimal(sumSourceNum).setScale(2, BigDecimal.ROUND_DOWN).divide(new BigDecimal(sourceCount)).add(new BigDecimal("4.8"));
			double a_result = a_total.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			c_user.put("sourceScore", a_result);
		}
		
		//收费标准 :  公式 = （历史总分数 +当前总分数 ）/ （历史总次数 + 当前总次数） + 4.8; 
		Double nowScaleNum = (Double)nowMap.get("scaleNum");//当前分数； 
		if(nowScaleNum != 0) {  //如果分数不等于0 ；就说明当前项目参与了评分；
			Double scaleNum = ((BigDecimal)backMap.get("scaleNum")).doubleValue();//历史总分数； 
			Double  sumScaleNum = scaleNum+nowScaleNum;//总分数；
			Long scaleCount = ((BigDecimal)backMap.get("scaleCount")).longValue();//历史次数
			scaleCount  +=  1; 
			BigDecimal a_total = new BigDecimal(sumScaleNum).setScale(2, BigDecimal.ROUND_DOWN).divide(new BigDecimal(scaleCount)).add(new BigDecimal("4.8"));
			double b_result = a_total.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			c_user.put("chargeScore", b_result);
		}
		
		//法律水平 :  公式 = （历史总分数 +当前总分数 ）/ （历史总次数 + 当前总次数） + 4.8; 
		Double nowLevelNum = (Double)nowMap.get("levelNum");//当前分数； 
		if(nowLevelNum != 0) {  //如果分数不等于0 ；就说明当前项目参与了评分；
			Double levelNum = ((BigDecimal)backMap.get("levelNum")).doubleValue();//历史总分数； 
			Double  sumLevelNum = levelNum+nowLevelNum;//总分数；
			Long levelCount = ((BigDecimal)backMap.get("levelCount")).longValue();//历史次数
			levelCount  +=  1; 
			BigDecimal a_total = new BigDecimal(sumLevelNum).setScale(2, BigDecimal.ROUND_DOWN).divide(new BigDecimal(levelCount)).add(new BigDecimal("4.8"));
			double b_result = a_total.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			c_user.put("levelScore", b_result);
		}
		
		//服务态度 :  公式 = （历史总分数 +当前总分数 ）/ （历史总次数 + 当前总次数） + 4.8; 
		Double nowMannerNum = (Double)nowMap.get("mannerNum");//当前分数； 
		if(nowMannerNum != 0) {  //如果分数不等于0 ；就说明当前项目参与了评分；
			Double mannerNum = ((BigDecimal)backMap.get("mannerNum")).doubleValue();//历史总分数； 
			Double  sumMannerNum = mannerNum+nowMannerNum;//总分数；
			Long mannerCount = ((BigDecimal)backMap.get("mannerCount")).longValue();//历史次数
			mannerCount  +=  1; 
			BigDecimal a_total = new BigDecimal(sumMannerNum).setScale(2, BigDecimal.ROUND_DOWN).divide(new BigDecimal(mannerCount),2, BigDecimal.ROUND_DOWN).add(new BigDecimal("4.8"));
			double b_result = a_total.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			c_user.put("attitudeScore", b_result);
		}

		
		
	}
	
		public static void main(String[] args) {
			double result = new BigDecimal("4.24444456").setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			System.out.println(result);
			double result1 = new BigDecimal("4.2455556").setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			System.out.println(result1);
			double result2 = new BigDecimal("4.2555556").setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			System.out.println(result2);
		}
	
}
