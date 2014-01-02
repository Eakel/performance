package com.easyfun.eclipse.performance.navigator.cfg.model;

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
public class Navigator {
	private List<Folder> list = new ArrayList<Folder>();
	
	private List<Item> itemList = new ArrayList<Item>();
	
	public void addFolder(Folder cache) {
		this.list.add(cache);
	}
	
	public List<Item> getAllItems() {
		return itemList;
	}
	
	public void addtem(Item item) {
		itemList.add(item);
	}

	public List<Folder> getFolders() {
		List<Folder> retList = new ArrayList<Folder>();
		for(int i=0; i<this.list.size(); i++){
			Folder folder = (Folder)this.list.get(i);
			if(folder.isVisible()){
				retList.add((Folder)this.list.get(i));
			}
		}
		
		//按字母排序
		Collections.sort(retList, new Comparator<Folder>(){
			public int compare(Folder folder1, Folder folder2) {
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
