package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

/**
 * [MON_DB_ACCT]
 * @author linzhaoming
 * 
 * Created at 2012-9-26
 */
public class MonDbAcct {
	private String dbAcctCode;
	private String username;
	private String password;
	private String host;
	private int port;
	private String sid;
	private int connMin;
	private int connMax;
	private String state;

	public void setDbAcctCode(String dbAcctCode) {
		this.dbAcctCode = dbAcctCode;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public void setConnMin(int connMin) {
		this.connMin = connMin;
	}

	public void setConnMax(int connMax) {
		this.connMax = connMax;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDbAcctCode() {
		return this.dbAcctCode;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public String getSid() {
		return this.sid;
	}

	public int getConnMin() {
		return this.connMin;
	}

	public int getConnMax() {
		return this.connMax;
	}

	public String getState() {
		return this.state;
	}
}