package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

/**
 * 
 * @author linzhaoming
 * 
 * Created at 2012-9-26
 */
public class MonPage implements Serializable {
	private static final long serialVersionUID = -900115409198515112L;
	private String state;
	private long serverId;
	private String remarks;
	private long pageId;
	private String pageTitle;
	private String pageUrl;

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	public long getServerId() {
		return this.serverId;
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

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getPageTitle() {
		return this.pageTitle;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getPageUrl() {
		return this.pageUrl;
	}
}