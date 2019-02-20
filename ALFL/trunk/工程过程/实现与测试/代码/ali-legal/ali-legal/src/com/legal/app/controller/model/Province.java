package com.legal.app.controller.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;


/**
 * 省份数据
 * @author huangzh
 *
 */
public class Province {
	private int provinceId;
	private String provinceName;
	
	private List<City> citys;
	
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public List<City> getCitys() {
		return citys;
	}
	public void setCitys(List<City> citys) {
		this.citys = citys;
	}
	
	
	public static void main(String[] args) {
		
		StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
		buffer.append("<address>");
	 	String driver = "com.mysql.jdbc.Driver";  
        String URL = "jdbc:mysql://localhost:3306/ali";  
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        String sql = "select * from provincial";
        try  
        {  
            Class.forName(driver);  
        }  
        catch(java.lang.ClassNotFoundException e)  
        {  
           // System.out.println("Connect Successfull.");  
            System.out.println("Cant't load Driver");  
        }  
        try     
        {                                                                                 
            con=DriverManager.getConnection(URL,"root","123456");  
            st=con.createStatement();
            rs=st.executeQuery(sql);
            int i = 1;
            if(rs!=null) {
            	
            	while(rs.next()){
            		int id = rs.getInt("pid");
            		buffer.append("\r\n  <province>");
            		buffer.append("\r\n    <provinceId>"+id+"</provinceId>");
            		buffer.append("\r\n    <provinceName>"+rs.getString("Provincial")+"</provinceName>");
            		buffer.append("\r\n    <citys>");
            		
            			String s = "select * from city where pid = "+id;
            			Connection conn1 = DriverManager.getConnection(URL,"root","123456"); 
            			Statement stmt1 = conn1.createStatement();
            			ResultSet rs1 = stmt1.executeQuery(s);
            			
            			while(rs1.next()){
            				buffer.append("\r\n      <city>");
                    		buffer.append("\r\n        <cityId>"+i+"</cityId>");
                    		buffer.append("\r\n        <cityName>"+rs1.getString("city")+"</cityName>");
                    		buffer.append("\r\n      </city>");
                    		i++;
            			}
            		
            			rs1.close();
            			stmt1.close();
            			conn1.close();
            			
            		buffer.append("\r\n    </citys>");
            		buffer.append("\r\n  </province>");
            	}
            	
            }
            //System.out.println("Connect Successfull.");  
            System.out.println("ok");
            rs.close();
            st.close();
            con.close();
        }   
        catch(Exception e)  
        {  
            System.out.println("Connect fail:" + e.getMessage());  
        }  

        buffer.append("</address>");
        System.out.println(buffer);
	}
}
