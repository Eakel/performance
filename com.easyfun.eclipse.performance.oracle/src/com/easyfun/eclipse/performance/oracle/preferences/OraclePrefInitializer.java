package com.easyfun.eclipse.performance.oracle.preferences;

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
public class OraclePrefInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = OraclePrefUtil.getPreferenceStore();
		//Oracle
		store.setDefault(OraclePrefConstants.ORACLE_JDBC_URL, "jdbc:oracle:thin:@127.0.0.1:1521:orcl");
		store.setDefault(OraclePrefConstants.ORACLE_JDBC_DRIVER, "oracle.jdbc.driver.OracleDriver");
		store.setDefault(OraclePrefConstants.ORACLE_JDBC_USER, "scott");
		store.setDefault(OraclePrefConstants.ORACLE_JDBC_PASSWORD, "tiger");
		
		
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
		store.setDefault(OraclePrefConstants.TABLE_PREFIX_FILTER, sb.toString());
		
	}

}
