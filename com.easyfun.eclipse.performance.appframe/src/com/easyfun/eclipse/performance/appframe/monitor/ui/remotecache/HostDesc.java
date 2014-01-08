package com.easyfun.eclipse.performance.appframe.monitor.ui.remotecache;

public class HostDesc {
	private long serverId;
	private String desc;

	public HostDesc(long serverId, String desc) {
		this.serverId = serverId;
		this.desc = desc;
	}

	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}