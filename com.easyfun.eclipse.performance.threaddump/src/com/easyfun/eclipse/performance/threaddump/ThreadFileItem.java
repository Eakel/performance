package com.easyfun.eclipse.performance.threaddump;

public class ThreadFileItem {
	private ThreadFileEnum typeEnum;
	
	public ThreadFileItem(ThreadFileEnum typeEnum){
		this.typeEnum = typeEnum;
	}
	
	public ThreadFileEnum getType(){
		return typeEnum;
	}
}
