package com.easyfun.eclipse.component.ftp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import com.easyfun.eclipse.component.db.ConnectionModel;
import com.easyfun.eclipse.component.db.DBUrlBean;
import com.easyfun.eclipse.performance.PerformanceActivator;

/**
 * FTP util
 * @author linzhaoming
 *
 * 2011-4-16
 *
 */
public class FTPUtil {
	
	/** ¶ÁÈ¡FTPÁ÷*/
	public static InputStream getInputStream(FTPBean ftpBean) throws Exception{
		FTPClient client = new FTPClient();
		client.setConnectTimeout(1000);
		client.connect(ftpBean.getHost(), ftpBean.getPort());
		client.login(ftpBean.getUserName(), ftpBean.getPasswd());
		client.setFileType(FTP.ASCII_FILE_TYPE);
		InputStream is = client.retrieveFileStream(ftpBean.getRemotePath());
		return is;
	}
	
	/** The Key for JDBC urls*/
	private static String JDBC_URLS_KEY = "JDBC_URLS";
	
	private static String SELECT_JDBC_KEY = "SELECT_JDBC";
	
	public static ConnectionModel getConnectionModel() {
		return null;
	}
	
	public static List<FTPBean> getFTPBeans(){
		List<FTPBean> list = null;
		ObjectMapper mapper = new ObjectMapper();
		String value = PerformanceActivator.getDefault().getPluginPreferences().getString(JDBC_URLS_KEY);
		if(StringUtils.isNotEmpty(value)){
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, FTPBean.class);
			try {
				list = mapper.readValue(value, javaType);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else{
			list = new ArrayList<FTPBean>();
		}
		
		return list;
	}
	
	public static FTPBean getSelectBean(){
		List<FTPBean> list = getFTPBeans();
		String select = PerformanceActivator.getDefault().getPreferenceStore().getString(SELECT_JDBC_KEY);
		if(StringUtils.isNotEmpty(select)){
			for (FTPBean bean : list) {
				if(bean.getName().equals(select)){
					return bean;				
				}
			}
		}
		return  null;
	}
	
	public static void saveFTPBeans(List<FTPBean> list){
		ObjectMapper mapper = new ObjectMapper();
		try {
			String value = mapper.writeValueAsString(list);
			PerformanceActivator.getDefault().getPreferenceStore().setValue(JDBC_URLS_KEY, value);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveSelectUrlBean(String name){
		PerformanceActivator.getDefault().getPreferenceStore().setValue(SELECT_JDBC_KEY, name);
	}
}
