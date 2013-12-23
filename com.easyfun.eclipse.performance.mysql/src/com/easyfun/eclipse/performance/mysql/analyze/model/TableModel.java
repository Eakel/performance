package com.easyfun.eclipse.performance.mysql.analyze.model;

public class TableModel {
	private String tableName;
	private long count;
	private String tableSpace;
	private String sumSize;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getTableSpace() {
		return tableSpace;
	}

	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}

	public String getSumSize() {
		return sumSize;
	}

	public void setSumSize(String sumSize) {
		this.sumSize = sumSize;
	}

	public String toString() {
		return tableName + " [" + count + "]";
	}
}