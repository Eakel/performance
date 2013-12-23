package com.easyfun.eclipse.performance.mysql.analyze.pref;

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
public class MySQLPrefInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = MySQLPrefUtil.getPreferenceStore();
		//Oracle
		store.setDefault(MySQLPrefConstants.MYSQL_JDBC_URL, "jdbc:mysql://127.0.0.1:3306/information_schema");
		store.setDefault(MySQLPrefConstants.MYSQL_JDBC_DRIVER, "com.mysql.jdbc.Driver");
		store.setDefault(MySQLPrefConstants.MYSQL_JDBC_USER, "base");
		store.setDefault(MySQLPrefConstants.MYSQL_JDBC_PASSWORD, "base");
		
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
		store.setDefault(MySQLPrefConstants.MYSQL_TABLE_PREFIX_FILTER, sb.toString());
		
	}

}
