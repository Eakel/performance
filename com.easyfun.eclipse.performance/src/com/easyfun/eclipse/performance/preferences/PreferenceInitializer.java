package com.easyfun.eclipse.performance.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.easyfun.eclipse.common.util.ui.PreferenceUtil;

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
		IPreferenceStore store = PreferenceUtil.getPreferenceStore();
	}

}
