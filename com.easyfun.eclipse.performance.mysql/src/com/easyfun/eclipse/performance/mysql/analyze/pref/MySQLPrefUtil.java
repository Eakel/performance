package com.easyfun.eclipse.performance.mysql.analyze.pref;

import org.eclipse.jface.preference.IPreferenceStore;

import com.easyfun.eclipse.component.db.ConnectionModel;
import com.easyfun.eclipse.performance.mysql.MySQLActivator;

public class MySQLPrefUtil {
	
	/** 获取JDBC设置 */
	public static ConnectionModel getConnectionModel(){
		ConnectionModel model = new ConnectionModel();
		IPreferenceStore store = getPreferenceStore();
		model.setDriver(store.getString(MySQLPrefConstants.MYSQL_JDBC_DRIVER));
		model.setUrl(store.getString(MySQLPrefConstants.MYSQL_JDBC_URL));
		model.setName(store.getString(MySQLPrefConstants.MYSQL_JDBC_USER));
		model.setPassword(store.getString(MySQLPrefConstants.MYSQL_JDBC_PASSWORD));		
		return model;
	}
	
	/** 获取表格过滤器*/
	public static String getTableFilter(){
		return getPreferenceStore().getString(MySQLPrefConstants.MYSQL_TABLE_PREFIX_FILTER);
	}
	
	/** 获取首选项IPreferenceStore*/
	public static IPreferenceStore getPreferenceStore(){
		IPreferenceStore store = MySQLActivator.getDefault().getPreferenceStore();
		return store;
	}

}
