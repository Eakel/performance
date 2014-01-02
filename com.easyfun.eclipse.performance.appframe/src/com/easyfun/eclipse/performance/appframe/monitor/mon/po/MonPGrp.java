package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

public class MonPGrp {
	private long grpId;
	private String name;
	private long parentGrpId;
	private String state;
	private String transformClass;
	private long sortBy;
	private String showType;

	public void setGrpId(long grpId) {
		this.grpId = grpId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParentGrpId(long parentGrpId) {
		this.parentGrpId = parentGrpId;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setTransformClass(String transformClass) {
		this.transformClass = transformClass;
	}

	public void setSortBy(long sortBy) {
		this.sortBy = sortBy;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public long getGrpId() {
		return this.grpId;
	}

	public String getName() {
		return this.name;
	}

	public long getParentGrpId() {
		return this.parentGrpId;
	}

	public String getState() {
		return this.state;
	}

	public String getTransformClass() {
		return this.transformClass;
	}

	public long getSortBy() {
		return this.sortBy;
	}

	public String getShowType() {
		return this.showType;
	}
}