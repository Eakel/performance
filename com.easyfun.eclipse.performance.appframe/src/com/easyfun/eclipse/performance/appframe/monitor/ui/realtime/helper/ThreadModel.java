package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.helper;


public class ThreadModel {
	private int rowNum;
	private long threadId;
	private String state;
	private String threadName;
	private String threadInfo;

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String getThreadInfo() {
		return threadInfo;
	}

	public void setThreadInfo(String threadInfo) {
		this.threadInfo = threadInfo;
	}

}
