package com.easyfun.eclipse.performance.appframe.monitor.mon.common.datasource.impl;

import java.sql.Connection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.datasource.interfaces.IDataSource;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.Resource;
import com.easyfun.eclipse.performance.appframe.webtool.util.appf.K;

/**
 * 读取config。properties配置文件，密码支持RC2的加密方式
 * @author linzhaoming
 * 
 * Created at 2012-9-26
 */
public class LocalMutilDataSourceImpl implements IDataSource {
	private static transient Log log = LogFactory.getLog(LocalMutilDataSourceImpl.class);

	protected static DataSource CUR_DATASOURCE = null;

	public LocalMutilDataSourceImpl() throws Exception {
		Properties prefetchPoolProperties = Resource.loadPropertiesFromClassPath("config.properties", "db.pool", true);

		Set key = prefetchPoolProperties.keySet();
		for (Iterator iter = key.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			prefetchPoolProperties.setProperty(item, K.k_s(prefetchPoolProperties.getProperty(item)));
		}

		if (log.isDebugEnabled()) {
			log.debug("mon连接池属性:" + maskPassword(prefetchPoolProperties) + "设置完毕");
		}

		CUR_DATASOURCE = BasicDataSourceFactory.createDataSource(k(prefetchPoolProperties));
		if (log.isDebugEnabled()) {
			log.debug("mon数据源创建成功");
		}

		initConnection(CUR_DATASOURCE);
	}

	public Connection getConnection() throws Exception {
		Connection rtn = null;
		try {
			rtn = CUR_DATASOURCE.getConnection();
			rtn.setAutoCommit(false);
		} catch (Exception ex) {
			log.error("无法获得mon的数据库连接", ex);
			throw ex;
		}

		return rtn;
	}

	private void initConnection(DataSource dataSource) throws Exception {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	private Properties k(Properties p) throws Exception {
		Set keys = p.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			p.setProperty(item, K.k_s(p.getProperty(item)));
		}
		return p;
	}

	private Properties maskPassword(Properties p) throws Exception {
		Properties rtn = new Properties();

		Set keys = p.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			if (!StringUtils.contains(item, "password")) {
				rtn.setProperty(item, p.getProperty(item));
			}
		}
		return rtn;
	}
}