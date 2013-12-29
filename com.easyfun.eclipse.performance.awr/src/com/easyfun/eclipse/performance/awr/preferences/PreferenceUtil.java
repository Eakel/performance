package com.easyfun.eclipse.performance.awr.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.easyfun.eclipse.component.db.ConnectionModel;
import com.easyfun.eclipse.performance.awr.AwrActivator;

public class PreferenceUtil {
	
	/** 获取JDBC设置 */
	public static ConnectionModel getConnectionModel(){
		ConnectionModel model = new ConnectionModel();
		IPreferenceStore store = getPreferenceStore();
		model.setDriver(store.getString(AwrPrefConstants.AWR_JDBC_DRIVER));
		model.setUrl(store.getString(AwrPrefConstants.AWR_JDBC_URL));
		model.setName(store.getString(AwrPrefConstants.AWR_JDBC_USER));
		model.setPassword(store.getString(AwrPrefConstants.AWR_JDBC_PASSWORD));		
		return model;
	}
	
	/** 获取首选项IPreferenceStore*/
	public static IPreferenceStore getPreferenceStore(){
		IPreferenceStore store = AwrActivator.getDefault().getPreferenceStore();
		return store;
	}

}
