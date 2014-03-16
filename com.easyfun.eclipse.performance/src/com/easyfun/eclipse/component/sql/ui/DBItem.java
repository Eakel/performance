package com.easyfun.eclipse.component.sql.ui;

/**
 * 代表导航树节点 Item扩展点定义
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
