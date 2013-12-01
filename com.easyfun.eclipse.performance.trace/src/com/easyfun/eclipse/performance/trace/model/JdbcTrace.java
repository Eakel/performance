package com.easyfun.eclipse.performance.trace.model;

import java.util.Collection;

import com.easyfun.eclipse.common.tree.model.ITreeModel;


public class JdbcTrace extends DefaultTrace implements ITrace, ITreeModel {
	private String username = "";

	private String sql = null;
	private String inParam;
	private String picture;
	
	/** P为 使用PrepareStatement，绑定变量 U为使用Statement 非绑定变量*/
	private String jdbcType;

	public void addChild(ITrace objITrace) {
	}

	public String getSql() {
		return this.sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setInParam(String inParam) {
		this.inParam = inParam;
	}

	public String getInParam() {
		return this.inParam;
	}

	public boolean isNode() {
		return false;
	}

	public String getPicture() {
		return this.picture;
	}
	
	public String getJdbcType() {
		return jdbcType;
	}

	/** P为 使用PrepareStatement，绑定变量 U为使用Statement 非绑定变量*/
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	/** P为 使用PrepareStatement，绑定变量 U为使用Statement 非绑定变量*/
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getDisplay(){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr><td>" + getType().toString().toLowerCase() + "</td><td>" + username + " " + sql + "</td></tr>");
		return sb.toString();
	}
	
	
	// ITreeModel的UI信息

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