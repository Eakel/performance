package com.easyfun.eclipse.performance.other.memcached.prefs;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;

import com.easyfun.eclipse.performance.other.OtherActivator;


public class OtherPrefUtil {
	private static final String OTHER_PREFIX = "Other_";
	
	/** JDBC URL key*/
	public static final String OTHER_MEMCACHED_URL = OTHER_PREFIX + "memcachedUrl";
	
	/** 获取首选项IPreferenceStore*/
	public static IPreferenceStore getPreferenceStore(){
		IPreferenceStore store = OtherActivator.getDefault().getPreferenceStore();
		return store;
	}
	
	public static String getMemcachedUrl(){
		String addr = getPreferenceStore().getString(OTHER_MEMCACHED_URL);
		if(StringUtils.isNotEmpty(addr)){
			return addr;
		}else{
			return "";
		}
	}
	
	public static void setMemcachedUrl(String url){
		 getPreferenceStore().setValue(OTHER_MEMCACHED_URL, url);
	}
}
