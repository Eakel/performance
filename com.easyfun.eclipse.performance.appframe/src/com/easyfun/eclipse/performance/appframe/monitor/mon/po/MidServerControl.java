package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

/**
 * [MON_C_SERVER]
 * @author linzhaoming
 * 
 * Created at 2012-9-26
 */
public class MidServerControl implements Serializable {
	private static final long serialVersionUID = -9024715409198500111L;
	private String url;
	private String hostname;
	private String serverName;
	private String pfPath;
	private long stopExecId;
	private long startExecId;
	private String grpName;
	private String status;
	private String info;

	public void setUrl(String url) {
		this.url = url;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setPfPath(String pfPath) {
		this.pfPath = pfPath;
	}

	public void setStopExecId(long stopExecId) {
		this.stopExecId = stopExecId;
	}

	public void setStartExecId(long startExecId) {
		this.startExecId = startExecId;
	}

	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getUrl() {
		return this.url;
	}

	public String getHostname() {
		return this.hostname;
	}

	public String getServerName() {
		return this.serverName;
	}

	public String getPfPath() {
		return this.pfPath;
	}

	public long getStopExecId() {
		return this.stopExecId;
	}

	public long getStartExecId() {
		return this.startExecId;
	}

	public String getGrpName() {
		return this.grpName;
	}

	public String getStatus() {
		return this.status;
	}

	public String getInfo() {
		return this.info;
	}
}