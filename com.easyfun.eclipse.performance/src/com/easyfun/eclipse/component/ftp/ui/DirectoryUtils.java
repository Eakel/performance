package com.easyfun.eclipse.component.ftp.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.easyfun.eclipse.component.ftp.ui.ImageConstants;

public class DirectoryUtils {
	/** 文件类型与图片路径对应关系*/
	private static Properties imgProperties = new Properties();
	
	/** 本地文件夹忽略的文件名列表*/
	private static List<String> ignoreList = new ArrayList<String>();
	
	static {
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/easyfun/eclipse/component/ftp/ui/localDirectory.properties");
		
		try {
			Properties p = new Properties();
			p.load(input);
			for (Object obj : p.keySet()) {
				String key = (String)obj;
				if(key.equals("ignore_files")){
					String value = p.getProperty(key);
					String[] files = StringUtils.split(value, ",");
					for (String s : files) {
						ignoreList.add(s.toUpperCase());
					}
				}else{
					String value = p.getProperty(key);
					String[] keys = StringUtils.split(key, ",");
					for (String s : keys) {
						imgProperties.put(s, value);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getIconPathByExtension(String extension){
		return imgProperties.getProperty(extension);
	}
	
	/** 根据文件类型获取图标路径*/
	public static Image getImageByFile(File file){
		String extension = FilenameUtils.getExtension(file.getName());
		String filePath = DirectoryUtils.getIconPathByExtension(extension);
		if(StringUtils.isNotEmpty(filePath)){
			return ImageDescriptor.createFromFile(null, filePath).createImage();
		}else{
			return ImageDescriptor.createFromFile(null, ImageConstants.ICON_FILE).createImage();
		}
	}
	
	/** 根据文件类型获取图标路径*/
	public static Image getImageByFile(FTPFile file){
		String extension = FilenameUtils.getExtension(file.getName());
		String filePath = DirectoryUtils.getIconPathByExtension(extension);
		if(StringUtils.isNotEmpty(filePath)){
			return ImageDescriptor.createFromFile(null, filePath).createImage();
		}else{
			return ImageDescriptor.createFromFile(null, ImageConstants.ICON_FILE).createImage();
		}
	}
	
	/** 是否忽略该文件名*/
	public static boolean isIgnore(String fileName){
		return ignoreList.contains(fileName.toUpperCase());
	}
}
