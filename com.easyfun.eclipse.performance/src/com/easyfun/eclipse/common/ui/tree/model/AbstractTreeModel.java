package com.easyfun.eclipse.common.ui.tree.model;

import java.util.Collection;

/**
 * @author linzhaoming
 * Create Date: 2010-8-14
 */
public abstract class AbstractTreeModel<T> implements ITreeModel{
	
	private T type;
	
	public AbstractTreeModel(T type){
		this.type = type;
	}
	
	/**
	 * Ĭ�Ϸ���toString()
	 */
	public String getDisplayName() {
		return toString();
	}

	/**
	 * Ĭ�Ϸ���null
	 */
	public Collection getChildren() {
		return null;
	}

	/**
	 * Ĭ�Ϸ���null
	 */
	public Object getInstance() {
		return null;
	}

	/**
	 * Ĭ�Ϸ���null
	 */
	public ITreeModel getParent() {
		return null;
	}

	/**
	 * Ĭ�Ϸ���false
	 */
	public boolean isDirectory() {
		return false;
	}
	
	
	public void setType(T type){
		this.type = type;
	}
	
	public T getType(){
		return type;
	}
}
