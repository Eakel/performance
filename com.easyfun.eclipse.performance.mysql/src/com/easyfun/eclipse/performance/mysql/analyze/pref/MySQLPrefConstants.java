package com.easyfun.eclipse.performance.mysql.analyze.pref;

/**
 * Constant definitions for plug-in preferences
 */
public class MySQLPrefConstants {
	
	private static final String ORACLE_PREFIX = "Oracle_";
	
	private static final String TABLE_PREFIX = "Table_";


	/** JDBC URL key*/
	public static final String ORACLE_JDBC_URL = ORACLE_PREFIX + "jdbcUrl";
	/** JDBC Driver key*/
	public static final String ORACLE_JDBC_DRIVER = ORACLE_PREFIX + "jdbcDriver";
	/** JDBC User key*/
	public static final String ORACLE_JDBC_USER = ORACLE_PREFIX + "jdbcUser";
	/** JDBC Password key*/
	public static final String ORACLE_JDBC_PASSWORD = ORACLE_PREFIX + "jdbcPassword";
	
	/** Table Filter Key*/
	public static final String TABLE_PREFIX_FILTER = TABLE_PREFIX + "filter";
}
