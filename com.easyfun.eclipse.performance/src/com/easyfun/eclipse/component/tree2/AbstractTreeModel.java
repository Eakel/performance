package com.easyfun.eclipse.component.tree2;

import java.util.Collection;

/**
 * @author linzhaoming
 * Create Date: 2010-8-14
 */
public abstract class AbstractTreeModel implements ITreeModel{

	/**
	 * 默认返回toString()
	 */
	public String getDisplayName() {
		return toString();
	}

	/**
	 * 默认返回null
	 */
	public Collection getChildren() {
		return null;
	}

	/**
	 * 默认返回null
	 */
	public Object getInstance() {
		return null;
	}

	/**
	 * 默认返回null
	 */
	public ITreeModel getParent() {
		return null;
	}

	/**
	 * 默认返回false
	 */
	public boolean isDirectory() {
		return false;
	}
}
