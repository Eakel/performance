package com.easyfun.eclipse.performance.trace.model;

import com.easyfun.eclipse.utils.lang.StringUtil;


public abstract class DefaultTrace implements ITrace {
	private String id;
	private String name;
	private TraceTypeEnum type;
	private String createTime;
	private String useTime;
	private String code;
	private String success;
	
	private boolean visible = true;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TraceTypeEnum getType() {
		return this.type;
	}

	public void setType(TraceTypeEnum type) {
		this.type = type;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return this.id;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public String getUseTime() {
		return this.useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}

	protected String getDisplayCost() {
		if (StringUtil.isNotEmpty(getUseTime())) {
			return " (" + getUseTime() + "ms)";
		} else {
			return "";
		}
	}

	public abstract void addChild(ITrace objITrace);

	public abstract boolean isNode();

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSuccess() {
		return this.success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	
}