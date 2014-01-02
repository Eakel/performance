package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

public class MonPageTabRelat implements Serializable {
	private static final long serialVersionUID = -900115409198515113L;
	private String state;
	private long relatId;
	private long tabId;
	private String remarks;
	private long pageId;

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}

	public void setRelatId(long relatId) {
		this.relatId = relatId;
	}

	public long getRelatId() {
		return this.relatId;
	}

	public void setTabId(long tabId) {
		this.tabId = tabId;
	}

	public long getTabId() {
		return this.tabId;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setPageId(long pageId) {
		this.pageId = pageId;
	}

	public long getPageId() {
		return this.pageId;
	}
}