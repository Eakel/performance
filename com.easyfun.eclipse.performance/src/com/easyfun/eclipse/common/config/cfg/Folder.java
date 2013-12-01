package com.easyfun.eclipse.common.config.cfg;

import java.util.ArrayList;
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
	
	private Navigator navigator;

	public void addItem(Item item) {
		this.list.add(item);
	}

	public Item[] getItems() {
		List<Item> retList = new ArrayList<Item>();
		for(int i=0; i<this.list.size(); i++){
			Item item = (Item)this.list.get(i);
			if(("true").equalsIgnoreCase(item.getVisible())){
				retList.add((Item)this.list.get(i));
			}
		}
		return (Item[]) retList.toArray(new Item[0]);
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

	public String toString() {
		return this.title;
	}

}
