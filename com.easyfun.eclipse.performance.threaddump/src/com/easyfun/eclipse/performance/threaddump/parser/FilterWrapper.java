package com.easyfun.eclipse.performance.threaddump.parser;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.graphics.Image;

public class FilterWrapper {
	private ViewerFilter[] filters;
	private Image image;

	public FilterWrapper(ViewerFilter[] filters, Image image) {
		this.filters = filters;
		this.image = image;
	}

	public ViewerFilter[] getFilters() {
		return filters;
	}

	public Image getImage() {
		return image;
	}
}