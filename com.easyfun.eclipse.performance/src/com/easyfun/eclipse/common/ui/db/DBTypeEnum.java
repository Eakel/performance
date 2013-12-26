package com.easyfun.eclipse.common.ui.db;

/**
 * �������ݿ�URL������
 * 
 * @author linzhaoming
 *
 * 2013-12-26
 *
 */
public enum DBTypeEnum {
	Oracle("Oracle"),
	MySQL("MySQL");
	
	private String str;

	DBTypeEnum(String str) {
		this.str = str;
	}

	public String toString() {
		return str;
	}
	
	public String getName(){
		return str;
	}
}
