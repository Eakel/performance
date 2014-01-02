package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

/**
 * [MON_P_EXEC]±í
 * @author linzhaoming
 * 
 * Created at 2012-9-22
 */
public class MonPExec implements Serializable {
	private static final long serialVersionUID = -9002815409198500111L;
	private long execId;
	private String name;
	private String type;
	private String expr;

	public void setExecId(long execId) {
		this.execId = execId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public long getExecId() {
		return this.execId;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	/** [MON_P_EXEC.EXPR]*/
	public String getExpr() {
		return this.expr;
	}
}