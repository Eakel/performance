package com.easyfun.eclipse.component.sql.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 代表导航树目录 Folder扩展点定义
 * 
 * @author linzhaoming
 *
 * 2013-12-1
 *
 */
public class DBFolder extends DBNode{
	private List<DBNode> list = new ArrayList<DBNode>();
	
	private DBNavigator navigator;
	
	private FolderType folderType = FolderType.Unknown;

	public void addNode(DBNode node) {
		this.list.add(node);
	}

	/** 获取所有子节点，包括目录节点*/
	public List<DBNode> getNodes() {
		List<DBNode> retList = new ArrayList<DBNode>();
		for(int i=0; i<this.list.size(); i++){
			DBNode node = (DBNode)this.list.get(i);
			if(node.isVisible()){
				retList.add(this.list.get(i));
			}
		}
		
		//按字母排序
		Collections.sort(retList, new Comparator<DBNode>(){
			public int compare(DBNode node1, DBNode node2) {
				if(node1 instanceof DBFolder && node2 instanceof DBItem){
					return -1;
				}else if(node1 instanceof DBItem && node2 instanceof DBFolder){
					return 1;
				}
				
				if(node2.getIndex() == -1 && node1.getIndex() == -1){
					return node1.getTitle().compareTo(node2.getTitle());
				}else if(node2.getIndex() == -1){
					return 1;
				}else if(node1.getIndex() == -1){
					return -1;
				}else{
					return 0;
				}
			}
		});
		
		return retList;
	}
	
	/** 获取所有目录Folder子节点*/
	public List<DBFolder> getFolderNodes() {
		List<DBFolder> retList = new ArrayList<DBFolder>();
		for(int i=0; i<this.list.size(); i++){
			DBNode node = (DBNode)this.list.get(i);
			if(node.isVisible() && node.isFolder()){
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
	
	/** 获取所有Item子节点*/
	public List<DBItem> getItemNodes() {
		List<DBItem> retList = new ArrayList<DBItem>();
		for(int i=0; i<this.list.size(); i++){
			DBItem node = (DBItem)this.list.get(i);
			if(node.isVisible() && !node.isFolder()){
				retList.add((DBItem)this.list.get(i));
			}
		}
		
		//按字母排序
		Collections.sort(retList, new Comparator<DBItem>(){
			public int compare(DBItem item1, DBItem item2) {
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

	public DBNavigator getNavigator() {
		return navigator;
	}

	public void setNavigator(DBNavigator navigator) {
		this.navigator = navigator;
	}

	public boolean isFolder() {
		return true;
	}

	public String toString() {
		return this.getTitle();
	}

	public FolderType getFolderType() {
		return folderType;
	}

	public void setFolderType(FolderType folderType) {
		this.folderType = folderType;
	}

}
