package com.easyfun.eclipse.performance.appframe.monitor.mon.common.datasource;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

public class LogicConnection implements Connection {
	private Connection physicalConnection = null;

	private boolean isClosed = false;

	public LogicConnection(Connection physicalConnection) {
		if (physicalConnection == null) {
			throw new RuntimeException("传入的物理连接为空");
		}
		this.physicalConnection = physicalConnection;
	}

	public Connection getPhysicalConnection() {
		return this.physicalConnection;
	}

	public String toString() {
		return "物理连接:" + this.physicalConnection + ",逻辑连接:" + super.toString();
	}

	public Statement createStatement() throws SQLException {
		return this.physicalConnection.createStatement();
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return this.physicalConnection.prepareStatement(sql);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return this.physicalConnection.prepareCall(sql);
	}

	public String nativeSQL(String sql) throws SQLException {
		return this.physicalConnection.nativeSQL(sql);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		this.physicalConnection.setAutoCommit(autoCommit);
	}

	public boolean getAutoCommit() throws SQLException {
		return this.physicalConnection.getAutoCommit();
	}

	public void commit() throws SQLException {
		throw new SQLException("逻辑连接不能commit");
	}

	public void rollback() throws SQLException {
		this.physicalConnection.rollback();
	}

	public void close() throws SQLException {
		this.isClosed = true;
	}

	public boolean isClosed() throws SQLException {
		return this.isClosed;
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return this.physicalConnection.getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		this.physicalConnection.setReadOnly(readOnly);
	}

	public boolean isReadOnly() throws SQLException {
		return this.physicalConnection.isReadOnly();
	}

	public void setCatalog(String catalog) throws SQLException {
		this.physicalConnection.setCatalog(catalog);
	}

	public String getCatalog() throws SQLException {
		return this.physicalConnection.getCatalog();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		this.physicalConnection.setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException {
		return this.physicalConnection.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		return this.physicalConnection.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		this.physicalConnection.clearWarnings();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return this.physicalConnection.createStatement(resultSetType,
				resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return this.physicalConnection.prepareStatement(sql, resultSetType,
				resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return this.physicalConnection.prepareCall(sql, resultSetType,
				resultSetConcurrency);
	}

	public Map getTypeMap() throws SQLException {
		return this.physicalConnection.getTypeMap();
	}

	public void setTypeMap(Map map) throws SQLException {
		this.physicalConnection.setTypeMap(map);
	}

	public void setHoldability(int holdability) throws SQLException {
		this.physicalConnection.setHoldability(holdability);
	}

	public int getHoldability() throws SQLException {
		return this.physicalConnection.getHoldability();
	}

	public Savepoint setSavepoint() throws SQLException {
		return this.physicalConnection.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return this.physicalConnection.setSavepoint(name);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		this.physicalConnection.rollback(savepoint);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		this.physicalConnection.releaseSavepoint(savepoint);
	}

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return this.physicalConnection.createStatement(resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return this.physicalConnection.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return this.physicalConnection.prepareCall(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		return this.physicalConnection.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		return this.physicalConnection.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		return this.physicalConnection.prepareStatement(sql, columnNames);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}
}