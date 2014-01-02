package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonDbAcct;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonDbUrl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;

/**
 * 从表[MON_DB_ACCT]和[MON_DB_ACCT.DB_ACCT_CODE]获取Connection。
 * <li>会Cache
 * @author linzhaoming
 * 
 * Created at 2012-9-26
 */
public final class MonTableDataSourceUtil {
	private static final HashMap CACHE = new HashMap();

	/** 根据[MON_DB_ACCT]
	 * <li>dbUrl为对应[MON_DB_URL.NAME]
	 * <li>dbAcctCode对应[MON_DB_ACCT.DB_ACCT_CODE] 
	 * */
	public static Connection getConnection(String dbUrl, String dbAcctCode) throws Exception {
		Connection conn = null;
		String key = getKey(dbUrl, dbAcctCode);
		if (!CACHE.containsKey(key)) {
			synchronized (CACHE) {
				if (!CACHE.containsKey(key)) {
					IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
					MonDbUrl objMonDbUrl = objIMonSV.getMonDbUrlByUrlName(dbUrl);
					MonDbAcct objMonDbAcct = objIMonSV.getMonDbAcctByDbAcctCode(dbAcctCode);

					Properties prop = new Properties();
					prop.setProperty("initialSize", String.valueOf(objMonDbAcct.getConnMin()));
					prop.setProperty("maxActive", String.valueOf(objMonDbAcct.getConnMax()));
					prop.setProperty("maxIdle", String.valueOf(objMonDbAcct.getConnMin()));
					prop.setProperty("maxWait", "2000");
					prop.setProperty("removeAbandoned", "true");
					prop.setProperty("removeAbandonedTimeout", "180");
					prop.setProperty("logAbandoned", "true");
					prop.setProperty("validationQuery", "select 1 from dual");
					prop.setProperty("testWhileIdle", "true");
					prop.setProperty("testOnBorrow", "false");
					prop.setProperty("testOnReturn", "false");
					prop.setProperty("timeBetweenEvictionRunsMillis", "10000");
					prop.setProperty("numTestsPerEvictionRun", "2");
					prop.setProperty("driverClassName", "oracle.jdbc.OracleDriver");
					prop.setProperty("url", "jdbc:oracle:thin:@" + objMonDbUrl.getUrl());
					prop.setProperty("username", objMonDbAcct.getUsername());
					prop.setProperty("password", objMonDbAcct.getPassword());
					CACHE.put(key, createDataSource(prop));
				}
			}
		}

		DataSource ds = (DataSource) CACHE.get(key);
		conn = ds.getConnection();
		conn.setAutoCommit(false);
		return conn;
	}

	private static DataSource createDataSource(Properties prop) throws Exception {
		return BasicDataSourceFactory.createDataSource(prop);
	}

	/** KEY为[dbUrl]_[dbAcctCode]*/
	private static String getKey(String dbUrl, String dbAcctCode) {
		return dbUrl + "|" + dbAcctCode;
	}
}