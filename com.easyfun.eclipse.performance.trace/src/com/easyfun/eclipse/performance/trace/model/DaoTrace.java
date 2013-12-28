package com.easyfun.eclipse.performance.trace.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.easyfun.eclipse.common.ui.tree.model.ITreeModel;


public class DaoTrace extends DefaultTrace implements ITrace, ITreeModel {
	private String className = null;

	private String methodName = null;

	private String center = null;

	private List<ITrace> child = new ArrayList<ITrace>();

	public String getCenter() {
		return this.center;
	}

	public String getClassName() {
		return this.className;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public boolean isNode() {
		return (this.child != null) && (this.child.size() > 0);
	}

	public void addChild(ITrace objITrace) {
		this.child.add(objITrace);
	}
	
	public String getDisplay(){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr><td>" + getType().toString().toLowerCase() + "</td><td>" + className + "." + methodName + "</td></tr>");
		
		for (ITrace trace : child) {
			sb.append(trace.getDisplay());
		}
		return sb.toString();
	}
	
	// ITreeModelµÄUIÐÅÏ¢

	public Collection getChildren() {
		return child;
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
		return true;
	}
}