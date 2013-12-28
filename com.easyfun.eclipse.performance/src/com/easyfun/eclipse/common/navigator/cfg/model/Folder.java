package com.easyfun.eclipse.common.navigator.cfg.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * µ¼º½Ê÷Ä¿Â¼
 * @author linzhaoming
 *
 * 2013-12-1
 *
 */
public class Folder {
	private List list = new ArrayList();

	private String title;
	private String type;
	private String visible;
	
	private String id;
	
	private int index;
	
	private Navigator navigator;

	public void addItem(Item item) {
		this.list.add(item);
	}

	public List<Item> getItems() {
		List<Item> retList = new ArrayList<Item>();
		for(int i=0; i<this.list.size(); i++){
			Item item = (Item)this.list.get(i);
			if(("true").equalsIgnoreCase(item.getVisible())){
				retList.add((Item)this.list.get(i));
			}
		}
		
		//°´×ÖÄ¸ÅÅÐò
		Collections.sort(retList, new Comparator<Item>(){
			public int compare(Item item1, Item item2) {
				if(item2.getIndex() == -1 && item1.getIndex() == -1){
					return item1.getTitle().compareTo(item2.getTitle());
				}else if(item2.getIndex() == -1){
					return 1;
				}else if(item1.getIndex() == -1){
					return -1;
				}else{
					return 0;
				}
			}
		});
		
		return retList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String toString() {
		return this.title;
	}

}
