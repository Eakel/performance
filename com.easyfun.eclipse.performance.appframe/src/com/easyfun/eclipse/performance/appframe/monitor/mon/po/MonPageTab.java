package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

public class MonPageTab implements Serializable {
	private static final long serialVersionUID = -900115409198265100L;
	private String state;
	private String tabTitle;
	private String tabUrl;
	private long tabId;
	private String remarks;

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}

	public void setTabTitle(String tabTitle) {
		this.tabTitle = tabTitle;
	}

	public String getTabTitle() {
		return this.tabTitle;
	}

	public void setTabUrl(String tabUrl) {
		this.tabUrl = tabUrl;
	}

	public String getTabUrl() {
		return this.tabUrl;
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
}