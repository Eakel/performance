package com.easyfun.eclipse.performance.trace.model;

import java.util.Collection;

import com.easyfun.eclipse.common.ui.tree.model.ITreeModel;


public class MdbTrace extends DefaultTrace implements ITrace, ITreeModel {
	private String host;
	private String center;
	private String inParam;
	private String outParam;

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getCenter() {
		return this.center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getOutParam() {
		return this.outParam;
	}

	public void setOutParam(String outParam) {
		this.outParam = outParam;
	}

	public String getInParam() {
		return this.inParam;
	}

	public void setInParam(String inParam) {
		this.inParam = inParam;
	}

	public void addChild(ITrace objITrace) {
	}

	public boolean isNode() {
		return false;
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