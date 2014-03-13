package com.easyfun.eclipse.component.db;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import com.easyfun.eclipse.performance.PerformanceActivator;

/**
 * Class for handling DB
 * @since 2013-12-30
 * 
 * @author linzhaoming
 *
 */
public class DBUtil {
	
	/** The Preference Key for all the JDBC urls*/
	private static String JDBC_URLS_KEY = "JDBC_URLS";
	
	/** The Preference Key for the select JDBC Url*/
	private static String SELECT_JDBC_KEY = "SELECT_JDBC";
	
	/** Get the current select DB Model*/
	public static ConnectionModel getConnectionModel(){
		ConnectionModel model = new ConnectionModel();
		DBUrlBean bean = getSelectBean();
		if (bean != null) {
			model.setDriverClass(bean.getDriverClass());
			model.setUrl(bean.getUrl());
			model.setUsername(bean.getUsername());
			model.setPassword(bean.getPassword());
			return model;
		} else {
			return null;
		}
	}
	
	public static ConnectionModel getConnectionModel(DBUrlBean bean){
		ConnectionModel model = new ConnectionModel();
		if (bean != null) {
			model.setDriverClass(bean.getDriverClass());
			model.setUrl(bean.getUrl());
			model.setUsername(bean.getUsername());
			model.setPassword(bean.getPassword());
			return model;
		} else {
			return null;
		}
	}
	
	/** Get all the DBs from Prefrence*/
	public static List<DBUrlBean> getDBUrlBeans(){
		List<DBUrlBean> list = null;
		ObjectMapper mapper = new ObjectMapper();
		String value = PerformanceActivator.getDefault().getPluginPreferences().getString(JDBC_URLS_KEY);
		if(StringUtils.isNotEmpty(value)){
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, DBUrlBean.class);
			try {
				list = mapper.readValue(value, javaType);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else{
			list = new ArrayList<DBUrlBean>();
		}
		
		return list;
	}
	
	/** Get the current selected DB from Preference*/
	public static DBUrlBean getSelectBean(){
		List<DBUrlBean> list = getDBUrlBeans();
		String select = PerformanceActivator.getDefault().getPreferenceStore().getString(SELECT_JDBC_KEY);
		if(StringUtils.isNotEmpty(select)){
			for (DBUrlBean bean : list) {
				if(bean.getName().equals(select)){
					return bean;				
				}
			}
		}
		return  null;
	}
	
	/** Save all the DBs to the Preference*/
	public static void saveDBUrlBeans(List<DBUrlBean> list){
		ObjectMapper mapper = new ObjectMapper();
		try {
			String value = mapper.writeValueAsString(list);
			PerformanceActivator.getDefault().getPreferenceStore().setValue(JDBC_URLS_KEY, value);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Save the current select DB to the Preference*/
	public static void saveSelectUrlBean(String name){
		PerformanceActivator.getDefault().getPreferenceStore().setValue(SELECT_JDBC_KEY, name);
	}
}
