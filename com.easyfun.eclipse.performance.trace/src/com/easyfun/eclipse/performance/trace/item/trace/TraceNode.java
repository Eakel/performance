package com.easyfun.eclipse.performance.trace.item.trace;

import com.easyfun.eclipse.common.ui.tree.model.Node;
import com.easyfun.eclipse.performance.trace.model.AppTrace;

/**
 * TraceÊ÷½Úµã
 * @author linzhaoming
 *
 * 2011-12-16
 *
 * @param <T>
 */
public abstract class TraceNode<T> extends  Node<T>{
	private AppTrace appTrace = null;
	
	public TraceNode(T type,String name){
		super(type, name);
	}

	public AppTrace getAppTrace() {
		return appTrace;
	}

	public void setAppTrace(AppTrace appTrace) {
		this.appTrace = appTrace;
	}

}
