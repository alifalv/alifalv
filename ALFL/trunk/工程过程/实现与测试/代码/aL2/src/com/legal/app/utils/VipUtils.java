package com.legal.app.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.common.log.BusinessException;
   

/**
 * 计算VIP 的工具
 * @author 军山依旧在
 *
 */
public class VipUtils {
	
	/**
	 * 公司的名称
	 */
	public static final String companyName="阿里法律";
	
	/**
	 * 普通会员
	 */
	public static final  int VIP_LEVLE_0 = 0;
	
	/**
	 *黄金会员
	 */
	public static final  int VIP_LEVLE_1 = 1;
	
	/**
	 * 白金会员
	 */
	public static final  int VIP_LEVLE_2 = 2;
	
	/**
	 * 钻石会员
	 */
	public static final  int VIP_LEVLE_3 =3;
	
	private static final String[] levelName = {"普通会员","黄金会员","白金会员","钻石会员"};
	
	/**
	 * 初次充值会员时，个人等级赠送文书券的情况；
	 * 等级为0 ，赠送0 次，等级为 1：0次，等级为2 ：1次，等级为3 ：3次；
	 */
	private static final int[]  person_voucher= {0,0,1,3};
	
	/**
	 * 企业等级每年赠送的文书券的情况；
	 *  等级为0 ，赠送0 次，等级为 1：4次，等级为2 ：12次，等级为3 ：50次；
	 */
	private static final int[] company_voucher= {0,4,12,50};
	
	/**
	 *根据个人的会员等级获取赠送的文书券个数；
	 * @param level  个人的会员等级；
	 * @return
	 */
	public static int getPersonVoucher(int level) {
		return person_voucher[level];
	}
	
	/**
	 * 根据企业的会员等级获取赠送的文书券个数；
	 * @param level 企业会员的等级；
	 * @return
	 */
	public static int getCompanyVoucher(int level) {
		return company_voucher[level];
	}
	
	/**
	 * 会员等级 数字转 汉字 ；
	 * @param level  会员等级
	 * @return
	 * @throws Exception
	 */
	public static String getLevelName(int level) throws Exception {
		try {
			return levelName[level];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("获取会员等级失败",-1);
		}
	}
	
	/**
	 * 
	 * 根据会员等级名称转为数字；
	 * @param levelName 会员等级；
	 * @return
	 * @throws Exception
	 */
	public static int getLevelCode(String level_name) throws Exception{
		int level = 0 ;
		for(int i = 0 ; i < levelName.length; i ++) {
			if(levelName[i].equals(level_name)) {
				level = i;
				break;
			}
		}
		return level; 
	}
	 
	/**
	 * 
	 * 车辆类型：  0：其它   1:货车  2:客车
	 * 根据车的类型返回每年的费用；
	 * @param carType
	 * @return
	 */
	public static int getMoney(int carType) throws Exception {
		int money = 0;
		switch (carType) {
		case 0:
			money = 200;
			break; 
		case 1:
			money = 400;
			break; 
		case 2:
			money = 500;
			break; 
		default:
			break;
		}
		if(money == 0 ) {
			throw new BusinessException("车辆类型不匹配.",-1);
		}
		return money;
	}
	
	/**
	 * 企业根据选择的等级获取基本费用；
	 * @param level  企业的会员等级；
	 * @return
	 * @throws Exception
	 */
	public static int getMoneyCompany(int level) throws Exception{
		int money = 0 ;
		switch (level) {
		case 1:
			money =  2000;
			break;
		case 2:
			money =  5000;
			break;
		case 3:
			money =  20000;
			break;

		default:
			break;
		}
		return money;
	}
	
	
	/**
	 * 根据 车的类型与会员年数，计算出费用；
	 * @param year  使用年数
	 * @param carType  车的类型；
	 * @return
	 */
	public static int  sumVipMoney(int year,int carType) throws Exception {
		int money = VipUtils.getMoney(carType);
		return money * year; 
	}

  /**
   * 咨询者与咨询师，初次升级为VIP时，算出他的等级；
   * @param year 购买年数
   * @param integral  所有积分
   * @return  会员等级
   * @throws Exception
   */
	public static int getVipLevelToInitial(int year,int integral) throws Exception { 
		if(year >=1  && year < 3) {
			//如果大于1 小于3 返回黄金会员
			return VIP_LEVLE_1;
		}else if(year >=3 && year< 6) {
			//如果年数 大于 等于 3 年 并且 小于6 年；
			return  VIP_LEVLE_2;
		}else if(year >=6) {
			//如果 大于 6 年；
			if(integral <2000) {
				//如果积分 小于2000 还是白金会员 
				return  VIP_LEVLE_2;
			}else {
				//否则为钻石会员；
				return  VIP_LEVLE_3;
			} 
		} 
		return 0;
	}
	
	/**
	 * 咨询者和咨询师续充时返回的等级
	 * @param inVipTime      会员生效的时间
	 * @param expireTime    会员到期的时间
	 * @param openYear      此次开能会员的年限
	 * @param carType        车辆类型 
	 * @param oldIntegral    历史积分；
	 * @param newIntegral   此次产生的积分
	 * @return
	 * @throws Exception
	 */
	public static int getVipLevelToConsultant(Date inVipTime,Date expireTime,int openYear , int carType , int oldIntegral ,int newIntegral) throws Exception{
		//总积分；
		int integral = oldIntegral +newIntegral;
		//历史产生的年数；
		int  year =  returnYear(inVipTime,expireTime) + openYear;
		
		return getVipLevelToInitial(year, integral);
	}
	
	/**
	 * 算出二个日期相差多少年；
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int returnYear(Date beginDate,Date endDate) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(beginDate);
		c2.setTime(endDate);
		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR); 
		return Math.abs(year1 - year2);
	} 
	
 
	
	/**
	 * 根据金额算出可以得到多少积分； 
	 * @param money  金额 
	 * @return
	 * @throws Exception
	 */
	public static int getIntegral(int money) throws Exception{
		//一元钱一分；
		return money * 1;
	}
	
	/**
	 * 检测会员是否过期
	 * @param nowData 当前日期
	 * @param expireTime 会员的过期时间；
	 * @return 过期返回 true ; 没有过期 返回false; 
	 * @throws Exception
	 */
	public static boolean checkVipExpires(Date nowData,Date expireTime) throws Exception{
		boolean val = true;
		if((nowData.getTime()<expireTime.getTime())) {
			val = false;
		}
		return val;
	}
	
	public static void main(String[] args) {
		try {
			Date d1 = new Date();
			System.out.println(d1.getTime());
			Date d2 = new Date();
			System.out.println(d2.getTime());
			d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2005-01-01 00:00:00");
			d2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2005-12-31 23:59:59");
			int  in = VipUtils.returnYear(d1, d2);
			System.out.println(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 分配奖金
	 *  
	 * @param index 当前第几位回复 
	 * @param reward  总赏金；
	 * @return
	 */
	public static  double getReward(Long index,Double reward) {
		BigDecimal b = new BigDecimal(reward); 
		int indexs = Integer.valueOf(index.toString());
		BigDecimal big = null;
		switch (indexs) {
		case 1: 
			big = b.multiply(new BigDecimal("0.45"));
			break;
		case 2: 
			big = b.multiply(new BigDecimal("0.225"));
			break;
		case 3: 
			big = b.multiply(new BigDecimal("0.225"));
			break;
		default:
			big = new BigDecimal("0");
			break;
		}
		return big.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();  
	} 
	
	/**
	 * 扣除平台系统费用后，返回应付多少钱
	 * @param type 类型；如果为空默认是扣除10%，如果有特殊的类型，就按传入的类型扣除
	 * @param feeTotal 总费用，没有扣除平台系统费用之前的金额 
	 * @return 
	 * @throws Exception
	 */
	public static Double sysTemCost(String type,Double feeTotal)throws Exception{
		Double money = 0.0d;
		if(null == type || type.equals("")) {
			money = feeTotal - feeTotal *0.1;
		}else {
			money = feeTotal - feeTotal *0.1;
		}
		
		return  Tools.rwipeOffDecimals(money);
	}
	
}
