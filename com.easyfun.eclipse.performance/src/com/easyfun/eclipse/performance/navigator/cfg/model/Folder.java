package com.easyfun.eclipse.performance.navigator.cfg.model;

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
public class Folder extends Node{
	private List<Node> list = new ArrayList<Node>();
	
	private Navigator navigator;

	public void addNode(Node node) {
		this.list.add(node);
	}

	/** 获取所有子节点，包括目录节点*/
	public List<Node> getNodes() {
		List<Node> retList = new ArrayList<Node>();
		for(int i=0; i<this.list.size(); i++){
			Node node = (Node)this.list.get(i);
			if(node.isVisible()){
				retList.add(this.list.get(i));
			}
		}
		
		//按字母排序
		Collections.sort(retList, new Comparator<Node>(){
			public int compare(Node node1, Node node2) {
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
	public List<Folder> getFolderNodes() {
		List<Folder> retList = new ArrayList<Folder>();
		for(int i=0; i<this.list.size(); i++){
			Node node = (Node)this.list.get(i);
			if(node.isVisible() && node.isFolder()){
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
	
	/** 获取所有Item子节点*/
	public List<Item> getItemNodes() {
		List<Item> retList = new ArrayList<Item>();
		for(int i=0; i<this.list.size(); i++){
			Item node = (Item)this.list.get(i);
			if(node.isVisible() && !node.isFolder()){
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

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

	public boolean isFolder() {
		return true;
	}

	public String toString() {
		return this.getTitle();
	}

}
