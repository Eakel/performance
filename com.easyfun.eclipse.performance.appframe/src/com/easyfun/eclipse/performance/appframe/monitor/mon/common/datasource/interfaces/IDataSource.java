package com.easyfun.eclipse.performance.appframe.monitor.mon.common.datasource.interfaces;

import java.sql.Connection;

public interface IDataSource {
	public Connection getConnection() throws Exception;
}