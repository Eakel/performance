package com.easyfun.eclipse.performance.trace.model;

import java.util.Collection;

import com.easyfun.eclipse.component.tree.model.ITreeModel;


public class MemTrace extends DefaultTrace implements ITrace, ITreeModel {
	private String host;
	private String getTime;
	private String center;
	private String inParam;
	private String resultCount;

	public void addChild(ITrace objITrace) {
	}

	public boolean isNode() {
		return false;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getGetTime() {
		return this.getTime;
	}

	public void setGetTime(String getTime) {
		this.getTime = getTime;
	}

	public String getCenter() {
		return this.center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getResultCount() {
		return this.resultCount;
	}

	public void setResultCount(String resultCount) {
		this.resultCount = resultCount;
	}

	public String getInParam() {
		return this.inParam;
	}

	public void setInParam(String inParam) {
		this.inParam = inParam;
	}
	
	public String getDisplay(){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr><td>" + getType().toString().toLowerCase() + "</td><td>" + host + "</td></tr>");
		return sb.toString();
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