package com.easyfun.eclipse.performance.mysql.analyze.pref;

import org.eclipse.jface.preference.IPreferenceStore;

import com.easyfun.eclipse.performance.mysql.MySQLActivator;

public class MySQLPrefUtil {
	
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
