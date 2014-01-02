package com.easyfun.eclipse.performance.appframe.monitor.mon.common.session;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.datasource.DataSourceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.datasource.LogicConnection;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.UUID;

public class LocalMutilTransactionImpl implements Session {
	private static transient Log log = LogFactory.getLog(LocalMutilTransactionImpl.class);

	private static ThreadLocal tx = new ThreadLocal();

	public boolean isStartTransaction() {
		ThreadInfo objThreadInfo = getThreadInfo();
		return objThreadInfo != null;
	}

	public void startTransaction() throws Exception {
		if (isStartTransaction()) {
			throw new Exception("已经开始一个事务,事务信息" + getThreadInfo());
		}
		setThreadInfo(new ThreadInfo());

		if (log.isDebugEnabled()) {
			log.debug("开始一个事务,事务信息" + getThreadInfo());
		}
	}

	public void rollbackTransaction() throws Exception {
		if (!isStartTransaction()) {
			throw new Exception("必须开始一个事务,才能回滚事务");
		}

		ThreadInfo objThreadInfo = getThreadInfo();
		try {
			if (objThreadInfo.txConnections != null) {
				Connection conn = objThreadInfo.txConnections;
				try {
					conn.rollback();
					if (log.isDebugEnabled()) {
						log.debug("连接回滚,连接:" + conn + ",事务信息" + getThreadInfo());
					}
				} catch (SQLException ex) {
					throw ex;
				} finally {
					if (!conn.isClosed()) {
						conn.close();
					}
				}

				if (log.isDebugEnabled()) {
					log.debug("事务回滚成功,事务信息" + getThreadInfo());
				}

			} else if (log.isDebugEnabled()) {
				log.debug("事务回滚成功,当前事务中没有关联的事务连接,事务信息" + getThreadInfo());
			}
		} finally {
			afterCompletion();
		}
	}

	public void commitTransaction() throws Exception {
		if (!isStartTransaction()) {
			throw new Exception("必须开始一个事务,才能提交事务");
		}

		ThreadInfo objThreadInfo = getThreadInfo();
		try {
			if (objThreadInfo.txConnections != null) {
				Connection conn = objThreadInfo.txConnections;
				try {
					conn.commit();
					if (log.isDebugEnabled()) {
						log.debug("连接提交,连接:" + conn + ",事务信息" + getThreadInfo());
					}
				} catch (SQLException ex) {
					throw ex;
				} finally {
					if (!conn.isClosed()) {
						conn.close();
					}
				}

				if (log.isDebugEnabled()) {
					log.debug("事务提交成功,事务信息" + getThreadInfo());
				}

			} else if (log.isDebugEnabled()) {
				log.debug("事务提交成功,当前事务中没有关联的事务连接,事务信息" + getThreadInfo());
			}
		} finally {
			afterCompletion();
		}
	}

	public Connection getNewConnection() throws SQLException {
		return getConnection(true);
	}

	public Connection getConnection() throws SQLException {
		return getConnection(false);
	}

	public Connection getConnection(boolean isNew) throws SQLException {
		Connection conn = null;
		try {
			conn = _getConnection(isNew);
		} catch (Exception ex) {
			log.error("获得连接失败", ex);
			throw new SQLException(ex.getMessage());
		}
		return conn;
	}

	public Connection _getConnection(boolean isNew) throws Exception {
		Connection rtn = null;

		if (isNew) {
			rtn = DataSourceFactory.getDataSource().getConnection();
		} else if (isStartTransaction()) {
			ThreadInfo obj = getThreadInfo();
			if (obj.txConnections != null) {
				rtn = new LogicConnection(obj.txConnections);
			} else {
				Connection objConnection = DataSourceFactory.getDataSource().getConnection();
				obj.txConnections = objConnection;
				rtn = new LogicConnection(obj.txConnections);
			}
		} else {
			rtn = DataSourceFactory.getDataSource().getConnection();
		}

		return rtn;
	}

	private void afterCompletion() {
		setThreadInfo(null);
	}

	private void setThreadInfo(ThreadInfo objThreadInfo) {
		tx.set(objThreadInfo);
	}

	private ThreadInfo getThreadInfo() {
		return (ThreadInfo) tx.get();
	}

	private static class ThreadInfo {
		private static Boolean SHOW_DETAIL = null;
		String txid = null;
		Connection txConnections = null;
		Date start = null;
		String curDataSource = null;
		String callPath = null;
		String threadName = null;

		ThreadInfo() {
			this.txid = UUID.getID();
			this.start = new Date();
		}

		public String toString() {
			String rtn = null;
			if ((SHOW_DETAIL != null) && (SHOW_DETAIL.equals(Boolean.TRUE))) {
				rtn = "[事务ID:" + this.txid + ",开始时间:" + this.start.toString() + ",线程:" + this.threadName + ",CallPath:]" + this.callPath;
			} else {
				rtn = "[事务ID:" + this.txid + ",开始时间:" + this.start.toString() + "]";
			}
			return rtn;
		}
	}
}