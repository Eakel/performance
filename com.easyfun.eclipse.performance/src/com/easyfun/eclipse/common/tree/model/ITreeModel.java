package com.easyfun.eclipse.common.tree.model;

import java.util.Collection;


/**
 * 树节点
 * @author linzhaoming
 * Create Date: 2010-8-12
 */
public interface ITreeModel<T> {
	
	/** 获取孩子节点 */
	public Collection getChildren();
	
	/** 获取父节点*/
	public ITreeModel getParent();
	
	/** 获取关联实际对象*/
	public Object getInstance();
	
	/** 是否为目录节点*/
	public boolean isDirectory();
	
	/** 获取显示节点名字*/
	public String getDisplayName();
	
	public T getType();
	
}
