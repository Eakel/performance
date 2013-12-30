package com.easyfun.eclipse.performance.trace.item.trace;

import com.easyfun.eclipse.performance.trace.item.TraceTreeEnum;



/**
 * [�ļ�] TraceĿ¼�ڵ�
 * 
 * @author linzhaoming
 *
 * 2013-4-12
 *
 * @param <T>
 */
public class FileTraceDirectory extends TraceDirectory<TraceTreeEnum>{
	
	public FileTraceDirectory(TraceTreeEnum type){
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
