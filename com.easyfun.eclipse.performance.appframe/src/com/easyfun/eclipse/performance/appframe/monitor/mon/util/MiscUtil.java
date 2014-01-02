package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ��ȡ����config.properties�ļ�
 * @author linzhaoming
 * 
 * Created at 2012-9-19
 */
public final class MiscUtil {
	private static transient Log log = LogFactory.getLog(MiscUtil.class);

	/** TRACE�ļ�·����config.properties�ļ��ж��壬KEYΪ"trace.path" ������Ĭ��Ϊ/tmp/aitrc*/
	private static String TRACE_PATH = "/tmp/aitrc";
	/** config.properties�ļ��ж��壬"userinfo.regionid.key" ������Ĭ��ΪRegionId*/
	private static String REGION_ID_KEY = "RegionId";
	/** AWR�ļ�·����config.properties�ļ��ж��壬"awr.path" ������Ĭ��Ϊ/tmp/awr*/
	private static String AWR_PATH = "/tmp/awr";
	
	private static String DB_DIALECT = "Oracle";

	static {
		try {
			Properties p = Resource.loadPropertiesFromClassPath("config.properties");
			String path = p.getProperty("trace.path");
			if (!StringUtils.isBlank(path)) {
				TRACE_PATH = path;
			}

			String key = p.getProperty("userinfo.regionid.key");
			if (!StringUtils.isBlank(key)) {
				REGION_ID_KEY = key;
			}

			String awrPath = p.getProperty("awr.path");
			if (!StringUtils.isBlank(awrPath)) {
				AWR_PATH = awrPath;
			}
			
			String dbDialect = p.getProperty("db.dialect");
			if (!StringUtils.isBlank(dbDialect)) {
				DB_DIALECT = dbDialect;
			}
		} catch (Throwable ex) {
			log.error("��ʼ������", ex);
		}
	}

	/** TRACE�ļ�·����config.properties�ļ��ж��壬KEYΪ"trace.path" ������Ĭ��Ϊ/tmp/aitrc*/
	public static String getTracePathPrefix() throws Exception {
		return TRACE_PATH;
	}

	/** config.properties�ļ��ж��壬"userinfo.regionid.key" ������Ĭ��ΪRegionId*/
	public static String getUserInfoRegionIdKey() throws Exception {
		return REGION_ID_KEY;
	}

	/** AWR�ļ�·����config.properties�ļ��ж��壬"awr.path" ������Ĭ��Ϊ/tmp/awr*/
	public static String getAwrPath() {
		return AWR_PATH;
	}
	
	public static String getDBDialect(){
		return DB_DIALECT;
	}

}