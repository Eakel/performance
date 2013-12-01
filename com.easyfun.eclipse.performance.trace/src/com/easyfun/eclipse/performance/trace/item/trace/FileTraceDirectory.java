package com.easyfun.eclipse.performance.trace.item.trace;



/**
 * [�ļ�] TraceĿ¼�ڵ�
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
			return "�ļ�";
		} else {
			return "�ļ� " + " (" + getChildren().size() + ")";
		}
	}
	
	
}
