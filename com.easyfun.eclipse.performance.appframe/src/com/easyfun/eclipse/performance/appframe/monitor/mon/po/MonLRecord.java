package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * [MON_L_RECORD]
 * @author linzhaoming
 * 
 * Created at 2012-9-26
 */
public class MonLRecord implements Serializable {
	private static final long serialVersionUID = -930115409198359103L;
	private long recordId;
	private long infoId;
	private String busiArea;
	private String monType;
	private String hostname;
	private String infoName;
	private String infoValue;
	private Timestamp createDate;
	private Timestamp doneDate;
	private String isTriggerWarn;
	private String ip;
	private String warnLevel;

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}

	public void setBusiArea(String busiArea) {
		this.busiArea = busiArea;
	}

	public void setMonType(String monType) {
		this.monType = monType;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}

	public void setInfoValue(String infoValue) {
		this.infoValue = infoValue;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public void setDoneDate(Timestamp doneDate) {
		this.doneDate = doneDate;
	}

	public void setIsTriggerWarn(String isTriggerWarn) {
		this.isTriggerWarn = isTriggerWarn;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setWarnLevel(String warnLevel) {
		this.warnLevel = warnLevel;
	}

	public long getRecordId() {
		return this.recordId;
	}

	public long getInfoId() {
		return this.infoId;
	}

	public String getBusiArea() {
		return this.busiArea;
	}

	public String getMonType() {
		return this.monType;
	}

	public String getHostname() {
		return this.hostname;
	}

	public String getInfoName() {
		return this.infoName;
	}

	public String getInfoValue() {
		return this.infoValue;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public Timestamp getDoneDate() {
		return this.doneDate;
	}

	public String getIsTriggerWarn() {
		return this.isTriggerWarn;
	}

	public String getIp() {
		return this.ip;
	}

	public String getWarnLevel() {
		return this.warnLevel;
	}
}