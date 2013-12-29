package com.easyfun.eclipse.common.ui.tree2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Mock Ä¿Â¼½Úµã
 * @author linzhaoming
 * Create Date: 2010-11-27
 */
public class MockDirectory extends AbstractTreeModel{
	private String name;
	private List children = new ArrayList();
	
	public MockDirectory(String name){
		this(name, 5);
	}
	
	public MockDirectory(String name, int childSize){
		this.name = name;
		List list = new ArrayList();
		for(int i=0; i<childSize; i++){
			MockNode node = new MockNode("Node" + i);
			list.add(node);
		}
		this.children = list;
	}
	
	public void setChildren(List children){
		this.children = children;
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
}
