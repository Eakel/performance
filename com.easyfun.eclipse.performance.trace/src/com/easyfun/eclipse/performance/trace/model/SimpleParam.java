package com.easyfun.eclipse.performance.trace.model;

import java.io.Serializable;

public class SimpleParam implements Serializable {
	private String name;
	private String type = null;

	private String value = null;

	private String index = "";
	private String picture;

	public SimpleParam() {
	}

	public SimpleParam(String type, String value) {
		this.type = type;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndex() {
		return this.index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}