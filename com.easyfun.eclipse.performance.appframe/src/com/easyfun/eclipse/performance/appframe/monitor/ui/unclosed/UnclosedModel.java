package com.easyfun.eclipse.performance.appframe.monitor.ui.unclosed;

import com.ai.appframe2.complex.mbean.standard.datasource.UnclosedNewConnRuntime;

public class UnclosedModel {
	private String serverName;
	private String serverId;
	private UnclosedNewConnRuntime[] runtimes;

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public UnclosedNewConnRuntime[] getRuntimes() {
		return runtimes;
	}

	public void setRuntimes(UnclosedNewConnRuntime[] runtimes) {
		this.runtimes = runtimes;
	}

}
