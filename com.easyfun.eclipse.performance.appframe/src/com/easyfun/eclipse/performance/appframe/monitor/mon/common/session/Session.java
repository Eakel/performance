package com.easyfun.eclipse.performance.appframe.monitor.mon.common.session;

import java.sql.Connection;
import java.sql.SQLException;

public interface Session {
	public boolean isStartTransaction();

	public void startTransaction() throws Exception;

	public void commitTransaction() throws Exception;

	public void rollbackTransaction() throws Exception;

	public Connection getConnection() throws SQLException;

	public Connection getNewConnection() throws SQLException;
}