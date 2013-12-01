package com.easyfun.eclipse.performance.awr.model;

/**
 * 保存查询Oracle的AWR的信息
 * @author linzhaoming
 *
 * 2012-9-23
 *
 */
public class SnapShot {
	private long snapId;
	private String beginDate;
	private String endDate;
	private long dbId;
	private long instanceNumber;
	private String instanceName;
	private String version;
	private String hostname;

	public SnapShot() {
	}

	public long getSnapId() {
		return snapId;
	}

	public void setSnapId(long snapId) {
		this.snapId = snapId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public long getDbId() {
		return dbId;
	}

	public void setDbId(long dbId) {
		this.dbId = dbId;
	}

	public long getInstanceNumber() {
		return instanceNumber;
	}

	public void setInstanceNumber(long instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String toString() {
		return snapId + " " + beginDate + " " + endDate + " " + dbId + " " + instanceNumber + " " + instanceName + " " + version + " " + getHostname();
	}
	
}