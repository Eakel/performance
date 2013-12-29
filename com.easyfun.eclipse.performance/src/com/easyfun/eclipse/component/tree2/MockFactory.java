package com.easyfun.eclipse.component.tree2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linzhaoming
 * Create Date: 2010-11-27
 */
public class MockFactory {
	public static MockDirectory createDirectory(){
		return new MockDirectory("只有一个节点");
	}
	
	public static List createDirectoryList(int size){
		List list = new ArrayList();
		for(int i=0; i<size; i++){
			MockDirectory dir = new MockDirectory("目录" + i);
			list.add(dir);
		}
		return list;
	}
	
	public static MockDirectory[] createDirectoryArray(int size){
		return (MockDirectory[])createDirectoryList(size).toArray();
	}
	
}
