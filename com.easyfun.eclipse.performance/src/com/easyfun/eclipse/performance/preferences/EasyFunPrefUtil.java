package com.easyfun.eclipse.performance.preferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.preference.IPreferenceStore;

import com.easyfun.eclipse.performance.PerformanceActivator;

public class EasyFunPrefUtil {
	private static Log log = LogFactory.getLog(EasyFunPrefUtil.class);
	
	/** 获取首选项IPreferenceStore*/
	public static IPreferenceStore getPreferenceStore(){
		IPreferenceStore store = PerformanceActivator.getDefault().getPreferenceStore();
		return store;
	}
	
	/** 获取表格过滤信息*/
	public static String getTableFilter(){
		return getPreferenceStore().getString(PreferenceConstants.TABLE_PREFIX_FILTER);
	}

}
