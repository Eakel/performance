package com.easyfun.eclipse.performance.trace.model;

import java.util.Collection;

import com.easyfun.eclipse.common.tree.model.ITreeModel;


public class SecMemTrace extends DefaultTrace implements ITrace, ITreeModel {
	public static final String PROCESS_METHOD_GET = "GET";
	public static final String PROCESS_METHOD_SET = "SET";

	private String host = "";
	private Object[] in = null;
	private String center = "";
	private String code = "";
	private boolean success = false;
	private String getTime = "";
	private String count = "";
	private String processMethod = "";
	
	private String resultCount;
	
	private String inParam;

	public SecMemTrace() {
	}

	public void addChild(ITrace objITrace) {
	}

	public String getCenter() {
		return center;
	}

	public String getCode() {
		return code;
	}

	public Object[] getIn() {
		return in;
	}

	public boolean isSuccess() {
		return success;
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
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getGetTime() {
		return getTime;
	}

	public String getProcessMethod() {
		return processMethod;
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
