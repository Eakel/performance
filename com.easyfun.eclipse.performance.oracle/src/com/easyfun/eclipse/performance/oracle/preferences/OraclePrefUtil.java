package com.easyfun.eclipse.performance.oracle.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.easyfun.eclipse.component.db.ConnectionModel;
import com.easyfun.eclipse.performance.oracle.OracleActivator;

public class OraclePrefUtil {
	
	/** 获取JDBC设置 */
	public static ConnectionModel getConnectionModel(){
		ConnectionModel model = new ConnectionModel();
		IPreferenceStore store = getPreferenceStore();
		model.setDriver(store.getString(OraclePrefConstants.ORACLE_JDBC_DRIVER));
		model.setUrl(store.getString(OraclePrefConstants.ORACLE_JDBC_URL));
		model.setName(store.getString(OraclePrefConstants.ORACLE_JDBC_USER));
		model.setPassword(store.getString(OraclePrefConstants.ORACLE_JDBC_PASSWORD));		
		return model;
	}
	
	/** 获取表格过滤器*/
	public static String getTableFilter(){
		return getPreferenceStore().getString(OraclePrefConstants.ORACLE_TABLE_PREFIX_FILTER);
	}
	
	/** 获取首选项IPreferenceStore*/
	public static IPreferenceStore getPreferenceStore(){
		IPreferenceStore store = OracleActivator.getDefault().getPreferenceStore();
		return store;
	}

}
