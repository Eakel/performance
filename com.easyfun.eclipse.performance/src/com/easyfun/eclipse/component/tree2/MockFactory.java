package com.easyfun.eclipse.component.tree2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linzhaoming
 * Create Date: 2010-11-27
 */
public class MockFactory {
	public static MockDirectory createDirectory(){
		return new MockDirectory("ֻ��һ���ڵ�");
	}
	
	public static List createDirectoryList(int size){
		List list = new ArrayList();
		for(int i=0; i<size; i++){
			MockDirectory dir = new MockDirectory("Ŀ¼" + i);
			list.add(dir);
		}
		return list;
	}
	
	public static MockDirectory[] createDirectoryArray(int size){
		return (MockDirectory[])createDirectoryList(size).toArray();
	}
	
}
