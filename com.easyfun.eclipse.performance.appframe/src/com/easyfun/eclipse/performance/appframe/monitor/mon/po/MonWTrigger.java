package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.sql.Timestamp;

public class MonWTrigger {
	private long triggerId;
	private long infoId;
	private String infoName;
	private String phonenum;
	private String content;
	private String warnLevel;
	private Timestamp createDate;
	private Timestamp doneDate;
	private String state;
	private String ip;
	private long recordId;

	public void setTriggerId(long triggerId) {
		this.triggerId = triggerId;
	}

	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setWarnLevel(String warnLevel) {
		this.warnLevel = warnLevel;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public void setDoneDate(Timestamp doneDate) {
		this.doneDate = doneDate;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public long getTriggerId() {
		return this.triggerId;
	}

	public long getInfoId() {
		return this.infoId;
	}

	public String getInfoName() {
		return this.infoName;
	}

	public String getPhonenum() {
		return this.phonenum;
	}

	public String getContent() {
		return this.content;
	}

	public String getWarnLevel() {
		return this.warnLevel;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public Timestamp getDoneDate() {
		return this.doneDate;
	}

	public String getState() {
		return this.state;
	}

	public String getIp() {
		return this.ip;
	}

	public long getRecordId() {
		return this.recordId;
	}
}