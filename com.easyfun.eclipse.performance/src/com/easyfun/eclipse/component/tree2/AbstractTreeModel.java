package com.easyfun.eclipse.component.tree2;

import java.util.Collection;

/**
 * @author linzhaoming
 * Create Date: 2010-8-14
 */
public abstract class AbstractTreeModel implements ITreeModel{

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
}
