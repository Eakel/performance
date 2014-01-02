package com.easyfun.eclipse.performance.navigator.cfg.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 代表导航树目录 Folder扩展点定义，包括以下属性
 * <li>title 显示在导航树上的名称
 * <li>type	类型
 * <li>visible 是否可视，为false不可视(忽略大小写)，其它都可视
 * <li>id	id
 * <li>index	排序
 * <li>folder	父节点的Folder ID
 * 
 * @author linzhaoming
 *
 * 2013-12-1
 *
 */
public class Folder {
	private List list = new ArrayList();

	/** Folder扩展点属性 ["title"]*/
	private String title;
	/** Folder扩展点属性 ["type"]*/
	private String type;
	/** Folder扩展点属性 ["visible"]*/
	private boolean visible;
	/** Folder扩展点属性 ["id"]*/
	private String id;
	/** Folder扩展点属性 ["index"]*/
	private int index;
	/** Folder扩展点属性 ["folder"]*/
	private String folder;
	
	private Navigator navigator;

	public void addItem(Item item) {
		this.list.add(item);
	}

	public List<Item> getItems() {
		List<Item> retList = new ArrayList<Item>();
		for(int i=0; i<this.list.size(); i++){
			Item item = (Item)this.list.get(i);
			if(item.isVisible()){
				retList.add((Item)this.list.get(i));
			}
		}
		
		//按字母排序
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

	/** Folder扩展点属性 ["title"]*/
	public String getTitle() {
		return title;
	}

	/** Folder扩展点属性 ["title"]*/
	public void setTitle(String title) {
		this.title = title;
	}

	/** Folder扩展点属性 ["type"]*/
	public String getType() {
		return type;
	}

	/** Folder扩展点属性 ["type"]*/
	public void setType(String type) {
		this.type = type;
	}

	/** Folder扩展点属性 ["visible"]*/
	public boolean isVisible() {
		return visible;
	}

	/** Folder扩展点属性 ["visible"]*/
	public void setVisible(boolean visible) {
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
	
	/** Folder扩展点属性 ["id"]*/
	public String getId() {
		return id;
	}

	/** Folder扩展点属性 ["id"]*/
	public void setId(String id) {
		this.id = id;
	}

	/** Folder扩展点属性 ["index"]*/
	public int getIndex() {
		return index;
	}

	/** Folder扩展点属性 ["index"]*/
	public void setIndex(int index) {
		this.index = index;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String toString() {
		return this.title;
	}

}
