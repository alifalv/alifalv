package com.legal.app.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.legal.app.model.City;
import com.legal.app.model.Province;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.log.ExceptionLogger;
import com.common.web.WebUtils;
@SuppressWarnings("unchecked")
@Controller
@RequestMapping("address")
public class AddressController extends SuperController{

	/**
	 * 存储所有省份城市数据
	 */
	private static List<Province> provinceList = new ArrayList<Province>();
	private static List<City> cityList = new ArrayList<City>();
	
	private static Map<String, String> provinceMap = new HashMap<String, String>();
	private static Map<String, String> cityMap = new HashMap<String, String>();
	static{
		ExceptionLogger.writeLog("加载所有的省份和城市信息 start...");
		SAXReader saxReader = new SAXReader(); 
		try {
			
			Document doc = saxReader.read(AddressController.class.getClassLoader().getResourceAsStream("com/legal/app/utils/address.xml"));
			
			Element root = doc.getRootElement();
			
			List<Element> list =  root.elements("province");
			
			for (Element p : list) {
				Province province = new Province();
				province.setProvinceId(Integer.parseInt(p.elementText("provinceId")));
				province.setProvinceName(p.elementText("provinceName"));
				//以province+id 为键  ，省份名称 为值
				provinceMap.put("province"+province.getProvinceId(), province.getProvinceName());
				
				List<Element> elist = p.element("citys").elements("city");
				List<City> citys = new ArrayList<City>();
				for (Element c : elist) {
					City city = new City();
					city.setCityId(Integer.parseInt(c.elementText("cityId")));
					city.setCityName(c.elementText("cityName"));
					citys.add(city);
					cityMap.put("city"+city.getCityId(), city.getCityName());
					cityList.add(city);
				}
				province.setCitys(citys);
				provinceList.add(province);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionLogger.writeLog("解析address.xml失败...");
		}
		ExceptionLogger.writeLog("加载所有的省份和城市信息 end...");
	}
	
	@RequestMapping("provinceList")
	public void getProvinceList(PrintWriter out){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Province p : provinceList) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("provinceId", p.getProvinceId());
			m.put("provinceName", p.getProvinceName());
			list.add(m);
		}
		out.print(WebUtils.responseData(list));
	}
	
	@RequestMapping("cityList")
	public void getCityList(PrintWriter out){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (City p : cityList) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("cityId", p.getCityId());
			m.put("cityName", p.getCityName());
			list.add(m);
		}
		out.print(WebUtils.responseData(list));
	}
	
	@RequestMapping("cityListByProvince/{pid}")
	public void getCityList(
			@PathVariable("pid") int pid,
			PrintWriter out){
		
		List<City> list = null;
		
		for (Province p : provinceList) {
			if(p.getProvinceId() == pid){
				list = p.getCitys();
				break;
			}
		}
		out.print(WebUtils.responseData(list));
	}

	public static String getCity(Integer code) {
		return cityMap.get("city"+code);
	}

	public static String getProvince(Integer code) {
		return provinceMap.get("province"+code);
	}
}
