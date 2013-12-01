package com.easyfun.eclipse.performance.awr.preferences;

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
public class AwrPrefInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = AwrPrefUtil.getPreferenceStore();
		//AWR
		store.setDefault(AwrPrefConstants.AWR_JDBC_URL, "jdbc:oracle:thin:@127.0.0.1:1521:orcl");
		store.setDefault(AwrPrefConstants.AWR_JDBC_DRIVER, "oracle.jdbc.driver.OracleDriver");
		store.setDefault(AwrPrefConstants.AWR_JDBC_USER, "scott");
		store.setDefault(AwrPrefConstants.AWR_JDBC_PASSWORD, "tiger");
	}

}
