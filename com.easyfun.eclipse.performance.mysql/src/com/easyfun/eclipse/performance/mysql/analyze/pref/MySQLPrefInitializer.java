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
		store.setDefault(MySQLPrefConstants.ORACLE_JDBC_URL, "jdbc:oracle:thin:@127.0.0.1:1521:orcl");
		store.setDefault(MySQLPrefConstants.ORACLE_JDBC_DRIVER, "oracle.jdbc.driver.OracleDriver");
		store.setDefault(MySQLPrefConstants.ORACLE_JDBC_USER, "scott");
		store.setDefault(MySQLPrefConstants.ORACLE_JDBC_PASSWORD, "tiger");
		
		
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
		store.setDefault(MySQLPrefConstants.TABLE_PREFIX_FILTER, sb.toString());
		
	}

}
