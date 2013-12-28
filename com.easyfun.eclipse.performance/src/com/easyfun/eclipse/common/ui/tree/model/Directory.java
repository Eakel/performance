package com.easyfun.eclipse.common.ui.tree.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Ä¿Â¼½Úµã
 * @author linzhaoming
 * Create Date: 2010-11-27
 */
public class Directory<T> extends AbstractTreeModel<T>{
	private String name;
	private List children = new ArrayList();
	
	public Directory(T type, String name){
		super(type);
		this.name = name;
	}
	
	public void setChildren(List children){
		this.children = children;
	}
	
	
	public void addChild(Node node){
		this.children.add(node);
	}
	
	public Collection getChildren() {
		return children;
	}
	
	public String getDisplayName() {
		return name;
	}

	public boolean isDirectory() {
		return true;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
}
