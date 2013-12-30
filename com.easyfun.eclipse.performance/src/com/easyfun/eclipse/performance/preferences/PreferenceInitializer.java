package com.easyfun.eclipse.performance.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * 设置Preference的默认值，配置plugin.xml中
 * 
 * @author linzhaoming
 *
 * 2011-5-7
 *
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = EasyFunPrefUtil.getPreferenceStore();
		
		//Table
		StringBuffer sb = new StringBuffer();
		sb.append("#用户名列表，多个用分号隔开").append("\n");
		sb.append("schemes=base;channel").append("\n");
		sb.append("#过滤表名字").append("\n");
		sb.append("#so1.filter=%SMS%").append("\n");
		sb.append("#so2.filter=%SMS%").append("\n");
		sb.append("#base.filter=%SMS%").append("\n");
		sb.append("base.mode=2").append("\n");
		sb.append("log=true");
		store.setDefault(PreferenceConstants.TABLE_PREFIX_FILTER, sb.toString());
	}

}
