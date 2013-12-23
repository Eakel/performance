package com.easyfun.eclipse.performance.mysql.analyze.model;

public class TableSettingBean {
	private String schemes = "base;channel";
	private String filters = "#so1.filter=%SMS%";
	private String fileName = "tableAnayyse_0505.xls";
	private boolean isLog = false;
	
	public TableSettingBean(){
		
	}

	public String getSchemes() {
		return schemes;
	}

	public void setSchemes(String schemes) {
		this.schemes = schemes;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isLog() {
		return isLog;
	}

	public void setLog(boolean isLog) {
		this.isLog = isLog;
	}
	
	public String toString() {
		return "schemes= " + schemes + ", filters=" +filters + ", fileName" + fileName + ", isLog=" + isLog;
	}
	
}
