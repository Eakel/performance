package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

public class MonTemplate implements Serializable {
	private static final long serialVersionUID = -900115609198515112L;
	private String state;
	private String remarks;
	private long templateId;
	private long tabId;
	private String templateName;

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

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public long getTemplateId() {
		return this.templateId;
	}

	public void setTabId(long tabId) {
		this.tabId = tabId;
	}

	public long getTabId() {
		return this.tabId;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateName() {
		return this.templateName;
	}
}