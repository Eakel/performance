package com.easyfun.eclipse.performance.trace.model;

import java.util.Collection;

import com.easyfun.eclipse.common.ui.tree.model.ITreeModel;


public class BccMemTrace extends DefaultTrace implements ITrace, ITreeModel {
	private String host = null;
	private String center = null;
	private String code = null;
	private boolean success = false;
	private String getTime = "";
	private int count = 0;
	private String processMethod = null;
	
	private String inParam;
	private String resultCount;

	public void addChild(ITrace objITrace) {
	}

	public String getCenter() {
		return this.center;
	}

	public String getCode() {
		return this.code;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getGetTime() {
		return this.getTime;
	}

	public String getProcessMethod() {
		return this.processMethod;
	}

	public void setGetTime(String getTime) {
		this.getTime = getTime;
	}

	public void setProcessMethod(String processMethod) {
		this.processMethod = processMethod;
	}
	
	public String getResultCount() {
		return this.resultCount;
	}

	public void setResultCount(String resultCount) {
		this.resultCount = resultCount;
	}

	public boolean isNode() {
		return true;
	}
	
	public String getDisplay(){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr><td>" + getType().toString() + "</td><td>" + host + "</td></tr>");
		return sb.toString();
	}
	public String getInParam() {
		return inParam;
	}

	public void setInParam(String inParam) {
		this.inParam = inParam;
	}
	
	
	// ITreeModelµÄUIÐÅÏ¢

	public Collection getChildren() {
		return null;
	}

	public String getDisplayName() {
		return getType().toString().toLowerCase() + " " + getCreateTime() + getDisplayCost();
	}

	public Object getInstance() {
		return null;
	}

	public ITreeModel getParent() {
		return null;
	}

	public boolean isDirectory() {
		return false;
	}
}