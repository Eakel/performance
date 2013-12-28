package com.easyfun.eclipse.common.ui.kv;

/**
 * Key Value pair.
 * 
 * @author linzhaoming
 *
 * 2011-4-16
 *
 */
public class KeyValue {
	private String key;
	private String value;

	public KeyValue() {
		this("", "");
	}

	public KeyValue(String key, String value) {
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

	public String toString() {
		return key + " = " + value;
	}
}
