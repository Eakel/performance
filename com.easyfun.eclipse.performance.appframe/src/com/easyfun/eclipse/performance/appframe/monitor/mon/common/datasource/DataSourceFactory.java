package com.easyfun.eclipse.performance.appframe.monitor.mon.common.datasource;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.datasource.impl.LocalMutilDataSourceImpl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.datasource.interfaces.IDataSource;

public class DataSourceFactory {
	private static IDataSource dataSource = null;

	static {
		try {
			dataSource = new LocalMutilDataSourceImpl();
		} catch (Exception ex) {
			throw new RuntimeException("≥ı ºªØ ß∞‹", ex);
		}
	}

	public static IDataSource getDataSource() {
		return dataSource;
	}

}