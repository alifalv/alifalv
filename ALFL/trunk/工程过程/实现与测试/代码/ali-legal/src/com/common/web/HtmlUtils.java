package com.common.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import com.common.log.BusinessException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class HtmlUtils {

	/**
	 * 使用Freemarker生成静态HTML文件
	 * 
	 * @param data
	 *            页面数据
	 * @param templatePath
	 *            模板文件所在目录
	 * @param templateName
	 *            模板文件名称
	 * @param targetPath
	 *            目标HTML文件路径
	 * @param targetName
	 *            目标HTML文件名称
	 */
	public static void createHtml(final Map<String, Object> data,
			final String templatePath, final String templateName,
			final String targetPath, final String targetName) {
		Configuration cfg = new Configuration();
		try (FileOutputStream fos = new FileOutputStream(targetPath
				+ File.separator + targetName);
				Writer out = new OutputStreamWriter(fos,
						HttpUtils.DEFAULT_CHARSET);) {
			cfg.setDirectoryForTemplateLoading(new File(templatePath));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			Template template = cfg.getTemplate(templateName,
					HttpUtils.DEFAULT_CHARSET);
			template.setEncoding(HttpUtils.DEFAULT_CHARSET);

			template.process(data, out);
			fos.close();
			out.close();
		} catch (Exception e) {
			throw new BusinessException("生成HTML文件错误：" + e.getMessage(), -1);
		}
	}

}
