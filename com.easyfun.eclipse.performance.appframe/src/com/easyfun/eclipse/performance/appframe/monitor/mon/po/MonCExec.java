package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

/**
 * [MON_C_EXEC]±í
 * @author linzhaoming
 * 
 * Created at 2012-9-23
 */
public class MonCExec implements Serializable {
	private static final long serialVersionUID = -9008815409198359103L;
	private long execId;
	private String name;
	private String execType;
	private String expr;
	private String state;

	public void setExecId(long execId) {
		this.execId = execId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExecType(String execType) {
		this.execType = execType;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getExecId() {
		return this.execId;
	}

	public String getName() {
		return this.name;
	}

	public String getExecType() {
		return this.execType;
	}

	public String getExpr() {
		return this.expr;
	}

	public String getState() {
		return this.state;
	}
}