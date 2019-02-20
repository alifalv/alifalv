package com.common.web;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JavaPropertiesUtils {

	/**
	 * 获取配置文件中的值 例如 config.properties
	 * 
	 * @param name
	 *            例如 cryptionkey
	 * @return value
	 */
	public static String getValue(String fileNamePath, String key)
			throws IOException {
		Properties props = null;
		InputStream in = null;
		try {
			// in = new FileInputStream(fileNamePath);
			// 如果将in改为下面的方法，必须要将.Properties文件和此class类文件放在同一个包中
			// in = ClassLoader.getSystemResourceAsStream(fileNamePath);
			props = PropertiesLoaderUtils.loadProperties(new ClassPathResource(
					fileNamePath));
			// props.load(in);
			String value = new String(props.getProperty(key).getBytes(
					"ISO-8859-1"), "UTF-8");
			// 有乱码时要进行重新编码
			// new String(props.getProperty("name").getBytes("ISO-8859-1"),
			// "GBK");
			return value;

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		} finally {
			if (null != in)
				in.close();
		}
	}
}
