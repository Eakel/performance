package com.easyfun.eclipse.performance.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * ����Preference��Ĭ��ֵ������plugin.xml��
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
		sb.append("#�û����б�����÷ֺŸ���").append("\n");
		sb.append("schemes=base;channel").append("\n");
		sb.append("#���˱�����").append("\n");
		sb.append("#so1.filter=%SMS%").append("\n");
		sb.append("#so2.filter=%SMS%").append("\n");
		sb.append("#base.filter=%SMS%").append("\n");
		sb.append("base.mode=2").append("\n");
		sb.append("log=true");
		store.setDefault(PreferenceConstants.TABLE_PREFIX_FILTER, sb.toString());
	}

}
