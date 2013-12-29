package com.easyfun.eclipse.uiutil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.preference.IPreferenceStore;

import com.easyfun.eclipse.performance.PerformanceActivator;

public class PreferenceUtil {
	private static Log log = LogFactory.getLog(PreferenceUtil.class);
	
	/** 获取首选项IPreferenceStore*/
	public static IPreferenceStore getPreferenceStore(){
		IPreferenceStore store = PerformanceActivator.getDefault().getPreferenceStore();
		return store;
	}

}
