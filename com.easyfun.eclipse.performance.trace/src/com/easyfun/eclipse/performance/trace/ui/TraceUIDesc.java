package com.easyfun.eclipse.performance.trace.ui;

/**
 * Trace�ļ���UI����
 * 
 * 
 * @author linzhaoming
 * 
 *         2013-4-16
 * 
 */
public class TraceUIDesc {
	/** ��Ŀ¼��������Ϣ*/
	private String parentDesc = "";
	
	/** �ļ��� */
	private String fileName;

	/** �ļ���С */
	private long fileSize;

	/** �ļ��޸�ʱ�� */
	private String fileTime;

	/** Trace����ʱ�� */
	private String traceTime;

	/** �����Ĺؼ��� */
	private String searchText = "";

//	/** �������� */
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
