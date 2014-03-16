package com.easyfun.eclipse.component.sql.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 代表左侧导航的内容
 * 
 * @author linzhaoming
 *
 * 2013-12-23
 *
 */
public class DBNavigator {
	private List<DBFolder> list = new ArrayList<DBFolder>();
	
	private List<DBItem> itemList = new ArrayList<DBItem>();
	
	public void addFolder(DBFolder cache) {
		this.list.add(cache);
	}
	
	public List<DBItem> getAllItems() {
		return itemList;
	}
	
	public void addtem(DBItem item) {
		itemList.add(item);
	}

	public List<DBFolder> getFolders() {
		List<DBFolder> retList = new ArrayList<DBFolder>();
		for(int i=0; i<this.list.size(); i++){
			DBFolder folder = (DBFolder)this.list.get(i);
			if(folder.isVisible()){
				retList.add((DBFolder)this.list.get(i));
			}
		}
		
		//按字母排序
		Collections.sort(retList, new Comparator<DBFolder>(){
			public int compare(DBFolder folder1, DBFolder folder2) {
				if(folder2.getIndex() == -1 && folder1.getIndex() == -1){
					return folder1.getTitle().compareTo(folder2.getTitle());
				}else if(folder2.getIndex() == -1){
					return 1;
				}else if(folder1.getIndex() == -1){
					return -1;
				}else{
					return 0;
				}
			}
		});
		return retList;
	}
}
