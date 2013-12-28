package com.easyfun.eclipse.performance.other.memcached.model;

import com.easyfun.eclipse.common.ui.kv.KeyValue;

/**
 * MemModel
 * 
 * @author linzhaoming
 *
 * 2013-12-22
 *
 */
public class MemModel extends KeyValue{
	private String desc;

	public MemModel() {
		this("", "");
	}

	public MemModel(String key, String value) {
		super(key, value);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String toString() {
		return super.toString();
	}
}
