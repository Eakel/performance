package com.easyfun.eclipse.common.ui.tree.model;



/**
 * вс╫з╣Ц
 * @author linzhaoming
 * Create Date: 2010-11-27
 */
public class Node<T> extends AbstractTreeModel<T>{
	protected String name;
	
	
	public Node(T type,String name){
		super(type);
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDisplayName() {
		return name;
	}
	
	public int hashCode() {
		return super.hashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Node){
			Node node = (Node)obj;
			if(node.getName().equals(name)){
				return true;
			}
		}
		return super.equals(obj);
	}
	
}
