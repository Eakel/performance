package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.ftl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class DataGrid implements Serializable {
	private static final long serialVersionId = -89812312312312L;
	private String tableid = null;
	private Long pagesize = null;
	private Long width = null;
	private Long height = null;
	private String ftl = null;
	private String model = null;
	private String select = null;
	private boolean rownum = false;
	private List columnList = null;
	private HashMap condition = null;

	public String getFtl() {
		return this.ftl;
	}

	public Long getHeight() {
		return this.height;
	}

	public String getModel() {
		return this.model;
	}

	public boolean isRownum() {
		return this.rownum;
	}

	public Long getPagesize() {
		return this.pagesize;
	}

	public String getSelect() {
		return this.select;
	}

	public String getTableid() {
		return this.tableid;
	}

	public Long getWidth() {
		return this.width;
	}

	public void setWidth(Long width) {
		this.width = width;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public void setTableid(String tableid) {
		this.tableid = tableid;
	}

	public void setPagesize(Long pagesize) {
		this.pagesize = pagesize;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setRownum(boolean rownum) {
		this.rownum = rownum;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public void setFtl(String ftl) {
		this.ftl = ftl;
	}

	public List getColumnList() {
		return this.columnList;
	}

	public void setColumnList(List columnList) {
		this.columnList = columnList;
	}

	public HashMap getCondition() {
		return this.condition;
	}

	public void setCondition(HashMap condition) {
		this.condition = condition;
	}
}