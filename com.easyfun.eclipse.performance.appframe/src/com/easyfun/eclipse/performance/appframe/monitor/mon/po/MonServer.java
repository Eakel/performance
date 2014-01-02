package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

/**
 * ´ú±í[MON_SER]¼ÇÂ¼
 * @author linzhaoming
 * 
 * Created at 2012-9-16
 */
public class MonServer implements Serializable {
	private static final long serialVersionUID = -900115409198375104L;
	private String state;
	private long serverId;
	private String remarks;
	private String name;
	private String locator;
	private String serverType;

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

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getLocator() {
		return this.locator;
	}

	public String getServerType() {
		return this.serverType;
	}

	public void setLocator(String locator) {
		this.locator = locator;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
}