package com.easyfun.eclipse.performance.navigator.cfg.model;

/**
 * 代表导航树节点 Item扩展点定义
 * 
 * @author linzhaoming
 *
 * 2013-12-1
 *
 */
public class Item extends Node{
	
	public boolean isFolder() {
		return false;
	}
	
	public String toString() {
		return this.getTitle();
	}
	
}
