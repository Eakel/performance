package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;
import java.util.Date;

public class MonControlLog implements Serializable {
	private static final long serialVersionUID = -900115409198375105L;
	private long controlId;
	private long logId;
	private String actionScript;
	private String results;
	private Date startDate;
	private Date endDate;

	public void setControlId(long controlId) {
		this.controlId = controlId;
	}

	public long getControlId() {
		return this.controlId;
	}

	public void setLogId(long logId) {
		this.logId = logId;
	}

	public long getLogId() {
		return this.logId;
	}

	public void setActionScript(String actionScript) {
		this.actionScript = actionScript;
	}

	public String getActionScript() {
		return this.actionScript;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getResults() {
		return this.results;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}
}