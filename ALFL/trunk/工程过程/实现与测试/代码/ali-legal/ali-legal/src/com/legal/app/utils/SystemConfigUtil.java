package com.legal.app.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;

import com.common.log.ExceptionLogger;
import com.legal.app.controller.SystemConfigController;
import com.legal.app.controller.model.Address;
import com.legal.app.controller.model.Case;
import com.legal.app.controller.model.City;
import com.legal.app.controller.model.Declare;
import com.legal.app.controller.model.Driver;
import com.legal.app.controller.model.Education;
import com.legal.app.controller.model.EffectivenessGrade;
import com.legal.app.controller.model.Experience;
import com.legal.app.controller.model.Job;
import com.legal.app.controller.model.Level;
import com.legal.app.controller.model.Sal;
import com.legal.app.controller.model.Unit;
import com.legal.app.service.ColumnService;

/**
 * 加载所有的系统配置信息及其字典信息
 * @author huangzh
 *
 */
@SuppressWarnings({"unchecked","unused","rawtypes"})
public class SystemConfigUtil {

	public static Map<String, Level> levelMap = new HashMap<String, Level>();
	public static Map<String, Driver> driverMap = new HashMap<String, Driver>();
	public static Map<String, Case> caseMap = new HashMap<String, Case>();
	public static Map<String, Job> jobMap = new HashMap<String, Job>();
	public static List<Level> levelList = new ArrayList<Level>();
	public static List<Driver> driverList = new ArrayList<Driver>();
	public static List<Case> caseList = new ArrayList<Case>();
	public static List<Job> jobList = new ArrayList<Job>();
	public static List<Declare> declareList = new ArrayList<Declare>();
	public static Map<String,Declare> declareMap = new HashMap<String,Declare>();
	
	public static List<Education> educationList = new ArrayList<Education>();
	public static Map<String, Education> educationMap = new HashMap<String, Education>();
	
	public static List<Experience> experienceList = new ArrayList<Experience>();
	public static Map<String, Experience> experienceMap = new HashMap<String, Experience>();
	
	public static List<Sal> salList = new ArrayList<Sal>();
	public static Map<String, Sal> salMap = new HashMap<String, Sal>();
	
	
	//发文单位
	public static List<Unit> unitList = new ArrayList<Unit>();
	public static Map<String, Unit> unitMap = new HashMap<String, Unit>();
	
	//效力等级
	public static List<EffectivenessGrade> egList = new ArrayList<EffectivenessGrade>();
	public static Map<String, EffectivenessGrade> egMap = new HashMap<String, EffectivenessGrade>();
	
	//省份与城市 
	public static List<Address> addressList = new ArrayList<Address>();
	public static Map<String,Address> addressMap = new HashMap<String,Address>();
	
	
	
	public final static String TYPE_LEVEL = "level";
	public final static String TYPE_CASE = "case";
	public final static String TYPE_JOB = "job";
	public final static String TYPE_DRIVER = "driver";
	public final static String TYPE_DECLARE = "declare";
	public final static String TYPE_ADDRESS = "address";
	public final static String TYPE_CITY = "city";
	public final static String TYPE_UNIT = "unit";
	public final static String TYPE_EG = "effectivenessGrade";
	
	public static Map<String, Map> columnMap = new HashMap<String, Map>(); 
	
	
	private static ColumnService columnService;
	
	
	@Autowired
	public void setColumnService(ColumnService columnService) {
		SystemConfigUtil.columnService = columnService;
	}

	public void init(){
		
	}
	
	public static String getValue(int id,String type){
		
		String value = null;
		
		if(type.equals(TYPE_CASE)){
			value = caseMap.get("case"+id).getCaseName();
		}else if(type.equals(TYPE_DRIVER)){
			value = driverMap.get("driver"+id).getDriverName();
		}else if(type.equals(TYPE_JOB)){
			value = jobMap.get("job"+id).getJobName();
		}else if(type.equals(TYPE_LEVEL)){
			value = levelMap.get("level"+id).getLevelName();
		}else if(type.equals(TYPE_DECLARE)) {
			value = declareMap.get("declare" +id).getDeclareName();
		}else if(type.equals(TYPE_UNIT)) {
			value = unitMap.get(TYPE_UNIT+id).getUnitName();
		}else if(type.equals(TYPE_EG)) {
			value = egMap.get(TYPE_EG+id).getEffectivenessGradeName();
		}
		
		return value;
	}
	
	
	/**
	 * 方法 不使用， 军山依旧  2018-09-17
	 */
	//public static String getColumnCode(int id){
		
		/**
		if(columnMap.isEmpty()){
			List<Map> columnList;
			try {
				//加载全部
				
				columnList = columnService.findColumnByType(0);
				
				for (Map map : columnList) {
					columnMap.put("column"+map.get("columnId"), map);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return columnMap.get("column"+id).get("columnCode").toString();
		**/
	//}
	
	
	static{
		try {
			//加载会员等级
			loadLevelInfo();
			//加载驾驶证等级
			loadDriverInfo();
			//加载案件类型
			loadCaseInfo();
			//加载职业信息
			loadJobInfo();
			//申报类型
			loadDeclareInfo();
			//加载待遇类型
			loadSalInfo();
			//加载
			loadExperienceInfo();
			loadEducationInfo();
			//发文单位
			loadUnitInfo();
			//效力等级
			loadEffectivenessGrade(); 
			//效力等级
			loadAddress();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(1);
		}
	}
	
	
	private static void loadLevelInfo() throws Exception{
		SAXReader saxReader = new SAXReader(); 
		Document doc = saxReader.read(SystemConfigController.class.getClassLoader().getResourceAsStream("com/legal/app/utils/config/level.xml"));
		
		Element root = doc.getRootElement();
		
		List<Element> levels = root.elements("level");
		
		for (Element l : levels) {
			Level level = new Level();
			level.setLevelId(Integer.parseInt(l.elementText("levelId")));
			level.setLevelName(l.elementText("levelName"));
			levelMap.put("level"+level.getLevelId(), level);
			levelList.add(level);
		}
	}
	
	
	private static void loadDriverInfo() throws Exception{
		SAXReader saxReader = new SAXReader(); 
		Document doc = saxReader.read(SystemConfigController.class.getClassLoader().getResourceAsStream("com/legal/app/utils/config/driver.xml"));
		
		Element root = doc.getRootElement();
		
		List<Element> drivers = root.elements("driver");
		
		for (Element l : drivers) {
			Driver driver = new Driver();
			driver.setDriverId(Integer.parseInt(l.elementText("driverId")));
			driver.setDriverName(l.elementText("driverName"));
			driver.setMoney(new BigDecimal(l.elementText("money")));
			driverMap.put("driver"+driver.getDriverId(), driver);
			driverList.add(driver);
		}
	}
	
	private static void loadJobInfo() throws Exception{
		SAXReader saxReader = new SAXReader(); 
		Document doc = saxReader.read(SystemConfigController.class.getClassLoader().getResourceAsStream("com/legal/app/utils/config/job.xml"));
		
		Element root = doc.getRootElement();
		
		List<Element> jobs = root.elements("job");
		
		for (Element l : jobs) {
			Job j = new Job();
			j.setJobId(Integer.parseInt(l.elementText("jobId")));
			j.setJobName(l.elementText("jobName"));
			jobMap.put("job"+j.getJobId(), j);
			jobList.add(j);
		}
	}
	
	private static void loadCaseInfo() throws Exception{
		SAXReader saxReader = new SAXReader(); 
		Document doc = saxReader.read(SystemConfigController.class.getClassLoader().getResourceAsStream("com/legal/app/utils/config/caseType.xml"));
		
		Element root = doc.getRootElement();
		
		List<Element> cases = root.elements("case");
		
		for (Element l : cases ) {
			Case c = new Case();
			c.setCaseId(Integer.parseInt(l.elementText("caseId")));
			c.setCaseName(l.elementText("caseName"));
			caseMap.put("case"+c.getCaseId(), c);
			caseList.add(c);
		}
	}
	
	private static void loadDeclareInfo() throws Exception{
		SAXReader saxReader = new SAXReader(); 
		Document doc = saxReader.read(SystemConfigController.class.getClassLoader().getResourceAsStream("com/legal/app/utils/config/declare.xml"));
		
		Element root = doc.getRootElement();
		
		List<Element> declares = root.elements("declare");
		
		for (Element l : declares ) {
			Declare c = new Declare();
			c.setDeclareId(Integer.parseInt(l.elementText("declareId")));
			c.setDeclareName(l.elementText("declareName"));
			declareMap.put("declare"+c.getDeclareId(), c);
			declareList.add(c);
		}
	}
	
	
	
	private static void loadSalInfo() throws Exception{
		SAXReader saxReader = new SAXReader(); 
		Document doc = saxReader.read(SystemConfigController.class.getClassLoader().getResourceAsStream("com/legal/app/utils/config/sal.xml"));
		Element root = doc.getRootElement();
		
		List<Element> sals = root.elements("sal");
		
		for (Element l : sals ) {
			Sal c = new Sal();
			c.setSalId(Integer.parseInt(l.elementText("salId")));
			c.setSalName(l.elementText("salName"));
			salMap.put("sal"+c.getSalId(), c);
			salList.add(c);
		}
	}
	
	
	private static void loadEducationInfo() throws Exception{
		SAXReader saxReader = new SAXReader(); 
		Document doc = saxReader.read(SystemConfigController.class.getClassLoader().getResourceAsStream("com/legal/app/utils/config/education.xml"));
		Element root = doc.getRootElement();
		
		List<Element> sals = root.elements("education");
		
		for (Element l : sals ) {
			Education c = new Education();
			c.setEducationId(Integer.parseInt(l.elementText("educationId")));
			c.setEducationName(l.elementText("educationName"));
			educationMap.put("education"+c.getEducationId(), c);
			educationList.add(c);
		}
	}
	
	
	private static void loadExperienceInfo() throws Exception{
		SAXReader saxReader = new SAXReader(); 
		Document doc = saxReader.read(SystemConfigController.class.getClassLoader().getResourceAsStream("com/legal/app/utils/config/experience.xml"));
		Element root = doc.getRootElement();
		
		List<Element> sals = root.elements("experience");
		
		for (Element l : sals ) {
			Experience c = new Experience();
			c.setExperienceId(Integer.parseInt(l.elementText("experienceId")));
			c.setExperienceName(l.elementText("experienceName"));
			experienceMap.put("experience"+c.getExperienceId(), c);
			experienceList.add(c);
		}
	}
	
	
	private static void loadUnitInfo() throws Exception{
		SAXReader saxReader = new SAXReader(); 
		Document doc = saxReader.read(SystemConfigController.class.getClassLoader().getResourceAsStream("com/legal/app/utils/config/unit.xml"));
		Element root = doc.getRootElement();
		
		List<Element> sals = root.elements("unit");
		
		for (Element l : sals ) {
			Unit c = new Unit();
			c.setUnitId(Integer.parseInt(l.elementText("unitId")));
			c.setUnitName(l.elementText("unitName"));
			unitMap.put("unit"+c.getUnitId(), c);
			unitList.add(c);
		}
	}
	
	
	private static void loadEffectivenessGrade() throws Exception{
		SAXReader saxReader = new SAXReader(); 
		Document doc = saxReader.read(SystemConfigController.class.getClassLoader().getResourceAsStream("com/legal/app/utils/config/effectivenessGrade.xml"));
		Element root = doc.getRootElement();
		
		List<Element> sals = root.elements("effectivenessGrade");
		
		for (Element l : sals ) {
			EffectivenessGrade c = new EffectivenessGrade();
			c.setEffectivenessGradeId(Integer.parseInt(l.elementText("effectivenessGradeId")));
			c.setEffectivenessGradeName(l.elementText("effectivenessGradeName"));
			egMap.put("effectivenessGrade"+c.getEffectivenessGradeId(), c);
			egList.add(c);
		}
	}
	
	private static void loadAddress() throws Exception{
		SAXReader saxReader = new SAXReader(); 
		Document doc = saxReader.read(SystemConfigController.class.getClassLoader().getResourceAsStream("com/legal/app/utils/config/address.xml"));
		Element root = doc.getRootElement();
		
		List<Element> sals = root.elements("province");
		
		for (Element l : sals ) {
			Address c = new Address();
			c.setId(l.elementText("provinceId"));
			c.setName(l.elementText("provinceName"));  
			List<Element> citys =l.element("citys").elements("city");
			Map citysMap = new HashMap<String,City>();
			for(Element  cit : citys) {
				City city = new City();
				city.setCityId(Integer.parseInt(cit.elementText("cityId")));
				city.setCityName(cit.elementText("cityName")); 
				citysMap.put("city"+city.getCityId(), city);
			}
			c.setCity(citysMap);
			addressMap.put(SystemConfigUtil.TYPE_ADDRESS+c.id, c);
			addressList.add(c);
		}
	}
	
	public static void main(String[] args){
		
		System.out.println(getValue(10, TYPE_UNIT));
		
	}
	
}
