package com.easyfun.eclipse.performance.trace.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.easyfun.eclipse.common.tree.model.ITreeModel;

public class SvrTrace extends DefaultTrace implements ITrace, ITreeModel {
	private String className = "";

	private String methodName = "";

	private String appIp = "";

	private String appServerName = "";

	private String center = "";

	private List<ITrace> child = new ArrayList<ITrace>();

	private String inParam = "";

	public String getAppIp() {
		return this.appIp;
	}

	public String getAppServerName() {
		return this.appServerName;
	}

	public String getCenter() {
		return this.center;
	}

	public String getClassName() {
		return this.className;
	}

	public void setAppIp(String appIp) {
		this.appIp = appIp;
	}

	public void setAppServerName(String appServerName) {
		this.appServerName = appServerName;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getInParam() {
		return inParam;
	}

	public void setInParam(String inParam) {
		this.inParam = inParam;
	}

	public void addChild(ITrace objITrace) {
		this.child.add(objITrace);
	}

	public boolean isNode() {
		return (this.child != null) && (this.child.size() > 0);
	}
	
	public String getDisplay(){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr><td>" + getType().toString().toLowerCase() + "</td><td>" + className + "." + methodName + "</td></tr>");
		
		for (ITrace trace : child) {
			sb.append(trace.getDisplay());
		}
		
		return sb.toString();
	}

	// ITreeModel的UI信息

	public Collection getChildren() {
		return child;
	}

	public String getDisplayName() {
		return "服务 " + getCreateTime() + getDisplayCost();
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