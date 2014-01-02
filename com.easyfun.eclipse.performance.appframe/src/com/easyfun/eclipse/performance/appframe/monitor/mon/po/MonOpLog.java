package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;
import java.util.Date;

public class MonOpLog implements Serializable {
	private String code;
	private String ip;
	private String className;
	private String methodName;
	private String url;
	private Date opDate;
	private long opId;

	public void setCode(String code) {
		this.code = code;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

	public void setOpId(long opId) {
		this.opId = opId;
	}

	public String getCode() {
		return this.code;
	}

	public String getIp() {
		return this.ip;
	}

	public String getClassName() {
		return this.className;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public String getUrl() {
		return this.url;
	}

	public Date getOpDate() {
		return this.opDate;
	}

	public long getOpId() {
		return this.opId;
	}
}