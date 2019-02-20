package com.common.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图片压缩工具
 * 
 * @author zengyao
 * @version $Id: ImageZipUtil.java, v 0.1 2015年9月17日 上午11:26:13 zengyao Exp $
 */
public class ImageZipUtil {

	public static Logger logger = LoggerFactory.getLogger(ImageZipUtil.class);

	/**
	 * 图片压缩主方法
	 * 
	 * @param newDir
	 *            图片所在的文件夹路径
	 * @param file
	 *            图片路径
	 * @param name
	 *            图片名
	 * @param w
	 *            目标宽
	 * @param h
	 *            目标高
	 * @param JPEGcompression
	 *            压缩质量/百分比
	 * @author zhouqz
	 */
	public static String ImgCompress(String filePath, InputStream fileInput,
			String name, int w, int h, float JPEGcompression) {
		FileOutputStream fos = null;
		try {
			BufferedImage bufferedImage = ImageIO.read(fileInput);

			int new_w = w;
			int new_h = h;

			BufferedImage image_to_save = new BufferedImage(new_w, new_h,
					bufferedImage.getType());
			image_to_save.getGraphics().drawImage(
					bufferedImage.getScaledInstance(new_w, new_h,
							Image.SCALE_SMOOTH), 0, 0, null);
			fos = new FileOutputStream(filePath); // 输出到文件流
			// 新的方法
			int dpi = 600;// 分辨率
			saveAsJPEG(dpi, image_to_save, JPEGcompression, fos);

		} catch (IOException e) {
		} finally {
			// 关闭输出流
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
			}
		}
		// 返回压缩后的图片地址
		return filePath;

	}

	/**
	 * 以JPEG编码保存图片
	 * 
	 * @param dpi
	 *            分辨率
	 * @param image_to_save
	 *            要处理的图像图片
	 * @param JPEGcompression
	 *            压缩比
	 * @param fos
	 *            文件输出流
	 * @throws IOException
	 */
	public static void saveAsJPEG(Integer dpi, BufferedImage image_to_save,
			float JPEGcompression, FileOutputStream fos) {

		// useful documentation at
		// http://docs.oracle.com/javase/7/docs/api/javax/imageio/metadata/doc-files/jpeg_metadata.html
		// useful example program at
		// http://johnbokma.com/java/obtaining-image-metadata.html to output
		// JPEG data

		// old jpeg class
		// com.sun.image.codec.jpeg.JPEGImageEncoder jpegEncoder =
		// com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(fos);
		// com.sun.image.codec.jpeg.JPEGEncodeParam jpegEncodeParam =
		// jpegEncoder.getDefaultJPEGEncodeParam(image_to_save);

		// Image writer
		ImageWriter imageWriter = ImageIO.getImageWritersBySuffix("jpg").next();
		ImageOutputStream ios = null;
		try {
			ios = ImageIO.createImageOutputStream(fos);

			imageWriter.setOutput(ios);
			// and metadata
			IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(
					new ImageTypeSpecifier(image_to_save), null);

			if (JPEGcompression >= 0 && JPEGcompression <= 1f) {

				JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter
						.getDefaultWriteParam();
				jpegParams
						.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
				jpegParams.setCompressionQuality(JPEGcompression);

			}

			// new Write and clean up
			imageWriter.write(imageMetaData, new IIOImage(image_to_save, null,
					null), null);

		} catch (IOException e) {
		} finally {
			try {
				if (ios != null) {
					ios.close();
				}
				if (imageWriter != null) {
					imageWriter.dispose();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 保存文件到服务器临时路径
	 * 
	 * @param fileName
	 * @param is
	 * @return 文件全路径
	 */
	public static String writeFile(String fileName, InputStream is) {
		if (fileName == null || fileName.trim().length() == 0) {
			return null;
		}
		try {
			/** 首先保存到临时文件 */
			FileOutputStream fos = new FileOutputStream(fileName);
			byte[] readBytes = new byte[512];// 缓冲大小
			int readed = 0;
			while ((readed = is.read(readBytes)) > 0) {
				fos.write(readBytes, 0, readed);
			}
			fos.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}
}
