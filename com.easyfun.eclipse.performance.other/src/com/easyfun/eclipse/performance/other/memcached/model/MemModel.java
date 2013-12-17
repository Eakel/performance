package com.easyfun.eclipse.performance.other.memcached.model;

/**
 * Key Value pair.
 * 
 * @author linzhaoming
 *
 * 2011-4-16
 *
 */
public class MemModel {
	private String key;
	private String value;
	private String desc;

	public MemModel() {
		this("", "");
	}

	public MemModel(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String toString() {
		return key + " = " + value;
	}
}
