package com.easyfun.eclipse.performance.navigator.cfg.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ��������Ŀ¼ Folder��չ�㶨�壬������������
 * <li>title ��ʾ�ڵ������ϵ�����
 * <li>type	����
 * <li>visible �Ƿ���ӣ�Ϊfalse������(���Դ�Сд)������������
 * <li>id	id
 * <li>index	����
 * <li>folder	���ڵ��Folder ID
 * 
 * @author linzhaoming
 *
 * 2013-12-1
 *
 */
public class Folder {
	private List list = new ArrayList();

	/** Folder��չ������ ["title"]*/
	private String title;
	/** Folder��չ������ ["type"]*/
	private String type;
	/** Folder��չ������ ["visible"]*/
	private boolean visible;
	/** Folder��չ������ ["id"]*/
	private String id;
	/** Folder��չ������ ["index"]*/
	private int index;
	/** Folder��չ������ ["folder"]*/
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
		
		//����ĸ����
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

	/** Folder��չ������ ["title"]*/
	public String getTitle() {
		return title;
	}

	/** Folder��չ������ ["title"]*/
	public void setTitle(String title) {
		this.title = title;
	}

	/** Folder��չ������ ["type"]*/
	public String getType() {
		return type;
	}

	/** Folder��չ������ ["type"]*/
	public void setType(String type) {
		this.type = type;
	}

	/** Folder��չ������ ["visible"]*/
	public boolean isVisible() {
		return visible;
	}

	/** Folder��չ������ ["visible"]*/
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
	
	/** Folder��չ������ ["id"]*/
	public String getId() {
		return id;
	}

	/** Folder��չ������ ["id"]*/
	public void setId(String id) {
		this.id = id;
	}

	/** Folder��չ������ ["index"]*/
	public int getIndex() {
		return index;
	}

	/** Folder��չ������ ["index"]*/
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
