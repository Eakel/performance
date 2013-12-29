package com.easyfun.eclipse.component.tree.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linzhaoming
 * Create Date: 2010-11-27
 */
public class TreeExampleFactory {
	public static Directory createDirectory(){
		return new Directory(null, "只有一个节点");
	}
	
	public static List createDirectoryList(int size){
		List list = new ArrayList();
		
		for(int i=0; i<size; i++){
			Directory dir = new Directory(null, "目录" + i);
			list.add(dir);
			
			List children = new ArrayList();
			for(int j=0; j<5; j++){
				Node node = new Node(null, "Node" + i);
				children.add(node);
			}
			dir.setChildren(children);
		}
		return list;
	}
//	
//	public static Directory[] createDirectoryArray(int size){
//		return (Directory[])createDirectoryList(size).toArray();
//	}
	
}
