package com.easyfun.eclipse.utils.model.tree;

/**
 * Mock вс╫з╣Ц
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
