package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

/**
 * [MON_CONTROL]
 * @author linzhaoming
 * 
 * Created at 2012-9-26
 */
public class MonControl implements Serializable {
	private static final long serialVersionUID = -900115409198359103L;
	private String state;
	private long controlId;
	private String scirptStart;
	private String connectType;
	private String scriptStop;
	private long tabId;
	private String remarks;
	private String scriptShowStatus;
	private String host;
	private long port;

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}

	public void setControlId(long controlId) {
		this.controlId = controlId;
	}

	public long getControlId() {
		return this.controlId;
	}

	public void setScirptStart(String scirptStart) {
		this.scirptStart = scirptStart;
	}

	public String getScirptStart() {
		return this.scirptStart;
	}

	public void setConnectType(String connectType) {
		this.connectType = connectType;
	}

	public String getConnectType() {
		return this.connectType;
	}

	public void setScriptStop(String scriptStop) {
		this.scriptStop = scriptStop;
	}

	public String getScriptStop() {
		return this.scriptStop;
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

	public void setScriptShowStatus(String scriptShowStatus) {
		this.scriptShowStatus = scriptShowStatus;
	}

	public String getScriptShowStatus() {
		return this.scriptShowStatus;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return this.host;
	}

	public void setPort(long port) {
		this.port = port;
	}

	public long getPort() {
		return this.port;
	}
}