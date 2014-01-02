package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

public class MonTraceFile implements Serializable, Comparable {
	private static final long serialVersionUID = -9001154098925100L;
	private String fileName;
	private long date;
	private long size;
	private long et;

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setEt(long et) {
		this.et = et;
	}

	public String getFileName() {
		return this.fileName;
	}

	public long getDate() {
		return this.date;
	}

	public long getSize() {
		return this.size;
	}

	public long getEt() {
		return this.et;
	}

	public int compareTo(Object o) {
		int i = 0;
		if (((MonTraceFile) o).getDate() < this.date) {
			i = -1;
		} else if (((MonTraceFile) o).getDate() > this.date) {
			i = 1;
		} else {
			i = 0;
		}
		return i;
	}
}