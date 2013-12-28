package com.easyfun.eclipse.performance.threaddump;

import java.util.ArrayList;
import java.util.List;

import com.easyfun.eclipse.common.ui.tree.model.Directory;

/**
 * 用于保存打开的线程文件
 * 
 * @author zhaoming
 *
 * 2011-9-19
 *
 */
public class ThreadFileMem {
	private static List<Directory> list = null;

	public static List getThreadFileInput(){
		if(list != null){
			return list;
		}
		
		init();
		return list;
	}
	
	private static void init(){
		list = new ArrayList<Directory>();
		
		ThreadFileItem fileItem = new ThreadFileItem(ThreadFileEnum.FILE);
		Directory fileDir = new Directory(fileItem, "文件");
		list.add(fileDir);
		
		ThreadFileItem ftpItem = new ThreadFileItem(ThreadFileEnum.FTP);
		Directory ftpDir = new Directory(ftpItem, "FTP");
		list.add(ftpDir);
		
		ThreadFileItem memItem = new ThreadFileItem(ThreadFileEnum.LOCAL_JVM);
		Directory jvmDir = new Directory(memItem, "本地JVM");
		list.add(jvmDir);
		
	}
	
	/**
	 * 
	 * @param node
	 * @return true：添加成功，false：没有添加
	 */
	public static boolean addChild(FileNode<ThreadFileEnum> node){
		boolean ret= false;
		switch (node.getType()) {
		case FILE:
			for (Directory<ThreadFileItem> ele : list) {
				if (ele.getType().getType() == ThreadFileEnum.FILE){
					boolean exist = false;	//文件是否已经存在
					for (Object obj : ele.getChildren()) {
						FileNode<ThreadFileEnum> element = (FileNode<ThreadFileEnum>)obj;
						if(element.equals(node)){	
							exist = true;
							break;
						}
					}
					
					//TODO: 存在的时候，再刷新一遍
					if(exist == false){
						ele.addChild(node);
						return true;
					}else{
						return false;
					}
				}
			}
			ret = true;
			break;
		default:
			break;
		}
		
		return ret;
	}
	
	public static boolean removeChild(FileNode<ThreadFileEnum> node){
		boolean ret= false;
		switch (node.getType()) {
		case FILE:
			for (Directory<ThreadFileItem> ele : list) {
				if (ele.getType().getType() == ThreadFileEnum.FILE){
					boolean removed = false;	//文件是否已经存在
					for (Object obj : ele.getChildren()) {
						FileNode<ThreadFileEnum> element = (FileNode<ThreadFileEnum>)obj;
						if(element.equals(node)){	
							removed = ele.getChildren().remove(obj);
							break;
						}
					}
					return removed;
				}
			}
		default:
			break;
		}
		
		return ret;
	}
}
