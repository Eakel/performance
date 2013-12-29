package com.easyfun.eclipse.performance.mysql.analyze.pref;

import org.eclipse.jface.preference.IPreferenceStore;

import com.easyfun.eclipse.component.db.ConnectionModel;
import com.easyfun.eclipse.performance.mysql.MySQLActivator;

public class MySQLPrefUtil {
	
	/** ��ȡJDBC���� */
	public static ConnectionModel getConnectionModel(){
		ConnectionModel model = new ConnectionModel();
		IPreferenceStore store = getPreferenceStore();
		model.setDriver(store.getString(MySQLPrefConstants.MYSQL_JDBC_DRIVER));
		model.setUrl(store.getString(MySQLPrefConstants.MYSQL_JDBC_URL));
		model.setName(store.getString(MySQLPrefConstants.MYSQL_JDBC_USER));
		model.setPassword(store.getString(MySQLPrefConstants.MYSQL_JDBC_PASSWORD));		
		return model;
	}
	
	/** ��ȡ��������*/
	public static String getTableFilter(){
		return getPreferenceStore().getString(MySQLPrefConstants.MYSQL_TABLE_PREFIX_FILTER);
	}
	
	/** ��ȡ��ѡ��IPreferenceStore*/
	public static IPreferenceStore getPreferenceStore(){
		IPreferenceStore store = MySQLActivator.getDefault().getPreferenceStore();
		return store;
	}

}
