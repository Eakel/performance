package com.easyfun.eclipse.component.tree2;

/**
 * Mock �ӽڵ�
 * @author linzhaoming
 * Create Date: 2010-11-27
 */
public class MockNode extends AbstractTreeModel{
	private String name;
	
	public MockNode(String name){
		this.name = name;
	}
	

	public String getDisplayName() {
		return name;
	}
	
}
