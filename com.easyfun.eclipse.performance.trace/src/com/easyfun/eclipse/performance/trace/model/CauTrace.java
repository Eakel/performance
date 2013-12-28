package com.easyfun.eclipse.performance.trace.model;

import java.util.Collection;

import com.easyfun.eclipse.common.ui.tree.model.ITreeModel;


public class CauTrace extends DefaultTrace implements ITrace, ITreeModel {
	public static final String PROCESS_METHOD_GET = "GET";
	public static final String PROCESS_METHOD_SET = "SET";
	private String host = null;
	private Object[] in = null;
	private String center = null;
	private String code = null;
	private boolean success = false;
	private String getTime = "";
	private String count = "";
	private String processMethod = null;
	
	private String resultCount;
	
	private String inParam;

	public void addChild(ITrace objITrace) {
	}

	public String getCenter() {
		return this.center;
	}

	public String getCode() {
		return this.code;
	}

	public Object[] getIn() {
		return this.in;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setIn(Object[] in) {
		this.in = in;
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

	public String getGetTime() {
		return this.getTime;
	}

	public String getProcessMethod() {
		return this.processMethod;
	}

	public String getCount() {
		return this.count;
	}

	public void setGetTime(String getTime) {
		this.getTime = getTime;
	}

	public void setProcessMethod(String processMethod) {
		this.processMethod = processMethod;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public boolean isNode() {
		return false;
	}
	
	public String getResultCount() {
		return this.resultCount;
	}

	public void setResultCount(String resultCount) {
		this.resultCount = resultCount;
	}
	
	public String getInParam() {
		return inParam;
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