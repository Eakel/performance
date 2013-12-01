package com.easyfun.eclipse.performance.trace.item.trace;



/**
 * [文件] Trace目录节点
 * 
 * @author linzhaoming
 *
 * 2013-4-12
 *
 * @param <T>
 */
public class FileTraceDirectory<T> extends TraceDirectory<T>{
	
	public FileTraceDirectory(T type){
		super(type);
	}
	
	public String getDisplayName() {
		if (getChildren().size() == 0) {
			return "文件";
		} else {
			return "文件 " + " (" + getChildren().size() + ")";
		}
	}
	
	
}
