package com.easyfun.eclipse.component.ftp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import com.easyfun.eclipse.component.db.ConnectionModel;
import com.easyfun.eclipse.performance.PerformanceActivator;

/**
 * FTP util
 * @author linzhaoming
 *
 * 2011-4-16
 *
 */
public class FTPUtil {
	
	/** The Key for FTP host*/
	private static String FTP_HOST_KEY = "FTP_HOST_URLS";
	
	private static String SELECT_FTP_KEY = "SELECT_FTP";
	
	public static ConnectionModel getConnectionModel() {
		return null;
	}
	
	public static List<FTPHostBean> getFTPBeans(){
		List<FTPHostBean> list = null;
		ObjectMapper mapper = new ObjectMapper();
		String value = PerformanceActivator.getDefault().getPluginPreferences().getString(FTP_HOST_KEY);
		if(StringUtils.isNotEmpty(value)){
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, FTPHostBean.class);
			try {
				list = mapper.readValue(value, javaType);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else{
			list = new ArrayList<FTPHostBean>();
		}
		
		return list;
	}
	
	public static FTPHostBean getSelectBean(){
		List<FTPHostBean> list = getFTPBeans();
		String select = PerformanceActivator.getDefault().getPreferenceStore().getString(SELECT_FTP_KEY);
		if (StringUtils.isNotEmpty(select) && list != null) {
			for (FTPHostBean bean : list) {
				if(bean.getName().equals(select)){
					return bean;				
				}
			}
		}
		return  null;
	}
	
	public static void saveFTPBeans(List<FTPHostBean> list){
		ObjectMapper mapper = new ObjectMapper();
		try {
			String value = mapper.writeValueAsString(list);
			PerformanceActivator.getDefault().getPreferenceStore().setValue(FTP_HOST_KEY, value);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveSelectUrlBean(String name){
		PerformanceActivator.getDefault().getPreferenceStore().setValue(SELECT_FTP_KEY, name);
	}
}
