package com.easyfun.eclipse.component.sql.ui;

/**
 * ���������ڵ� Item��չ�㶨��
 * 
 * @author linzhaoming
 *
 * 2013-12-1
 *
 */
public class DBItem extends DBNode{
	
	public boolean isFolder() {
		return false;
	}
	
	public String toString() {
		return this.getTitle();
	}
	
}
