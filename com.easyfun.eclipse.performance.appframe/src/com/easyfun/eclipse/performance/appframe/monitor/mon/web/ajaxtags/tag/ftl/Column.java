package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.ftl;

import java.io.Serializable;

public class Column implements Serializable {
	private static final long serialVersionId = -12312314572L;
	private String width;
	private String property;
	private String title;
	private String sort;
	private String filter;
	private String decorator;

	public Column(String width, String property, String title, String sort, String filter, String decorator) {
		this.width = width;
		this.property = property;
		this.title = title;
		this.sort = sort;
		this.filter = filter;
		this.decorator = decorator;
	}

	public String getWidth() {
		return this.width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getProperty() {
		return this.property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSort() {
		return this.sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getFilter() {
		return this.filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getDecorator() {
		return this.decorator;
	}

	public void setDecorator(String decorator) {
		this.decorator = decorator;
	}
}