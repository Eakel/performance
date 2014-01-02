package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

public class MonTree implements Serializable {
	private static final long serialVersionUID = -900115409198484110L;
	private String state;
	private String remarks;
	private String name;
	private long treeId;
	private String treeType;
	private long parentId;

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setTreeId(long treeId) {
		this.treeId = treeId;
	}

	public long getTreeId() {
		return this.treeId;
	}

	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}

	public String getTreeType() {
		return this.treeType;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public long getParentId() {
		return this.parentId;
	}
}