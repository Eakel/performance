package com.easyfun.eclipse.performance.trace.ui;

/**
 * Trace文件的UI描述
 * 
 * 
 * @author linzhaoming
 * 
 *         2013-4-16
 * 
 */
public class TraceUIDesc {
	/** 父目录的描述信息*/
	private String parentDesc = "";
	
	/** 文件名 */
	private String fileName;

	/** 文件大小 */
	private long fileSize;

	/** 文件修改时间 */
	private String fileTime;

	/** Trace花费时间 */
	private String traceTime;

	/** 搜索的关键字 */
	private String searchText = "";

//	/** 过滤类型 */
//	private int filterType = -1;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileTime() {
		return fileTime;
	}

	public void setFileTime(String fileTime) {
		this.fileTime = fileTime;
	}

	public String getTraceTime() {
		return traceTime;
	}

	public void setTraceTime(String traceTime) {
		this.traceTime = traceTime;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getParentDesc() {
		return parentDesc;
	}

	public void setParentDesc(String parentDesc) {
		this.parentDesc = parentDesc;
	}


}
