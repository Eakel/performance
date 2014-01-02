package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

public class MonPTable {
	private long tableId;
	private String name;
	private String sql;
	private String dbAcctCode;
	private String dbUrlname;

	public void setTableId(long tableId) {
		this.tableId = tableId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setDbAcctCode(String dbAcctCode) {
		this.dbAcctCode = dbAcctCode;
	}

	public void setDbUrlname(String dbUrlname) {
		this.dbUrlname = dbUrlname;
	}

	public long getTableId() {
		return this.tableId;
	}

	public String getName() {
		return this.name;
	}

	public String getSql() {
		return this.sql;
	}

	public String getDbAcctCode() {
		return this.dbAcctCode;
	}

	public String getDbUrlname() {
		return this.dbUrlname;
	}
}