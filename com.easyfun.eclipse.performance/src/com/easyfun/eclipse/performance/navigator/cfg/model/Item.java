package com.easyfun.eclipse.performance.navigator.cfg.model;

/**
 * ���������ڵ� Item��չ�㶨��
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
