package com.easyfun.eclipse.performance.trace.model;

import java.util.Collection;

import com.easyfun.eclipse.component.tree.model.ITreeModel;


public class WsTrace extends DefaultTrace implements ITrace, ITreeModel {
	private String url;
	private String methodName;
	private String center;
	private String timeOut;
	private String outParam;
	private String inParam;

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getCenter() {
		return this.center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getTimeOut() {
		return this.timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public String getOutParam() {
		return this.outParam;
	}

	public void setOutParam(String outParam) {
		this.outParam = outParam;
	}

	
	public void addChild(ITrace objITrace) {
	}

	public boolean isNode() {
		return false;
	}
	
	public String getDisplay(){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr><td>" + getType().toString().toLowerCase() + "</td><td>" + url + "</td></tr>");
		return sb.toString();
	}
	
	
	// ITreeModelµÄUIÐÅÏ¢

	public String getInParam() {
		return inParam;
	}

	public void setInParam(String inParam) {
		this.inParam = inParam;
	}

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