package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.ftl.Column;

public class ColumnTag extends TagSupport {
	private static transient Log log = LogFactory.getLog(ColumnTag.class);

	private String title = null;
	private String property = null;
	private String width = "100";
	private String sort = "none";
	private String filter = null;
	private String decorator = null;
	private String cell;

	public int doStartTag() throws JspException {
		try {
			DataGridTag parent = (DataGridTag) getParent();
			parent.addTag(new Column(this.width, this.property, this.title, this.sort, this.filter, this.decorator));
			clear();
		} catch (Exception ex) {
			log.error("´íÎó:", ex);
			throw new JspException(ex);
		}
		return 0;
	}

	private void clear() {
		this.title = null;
		this.property = null;
		this.width = "100";
		this.sort = "none";
		this.filter = null;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setFilter(String filter) {
		if ((!StringUtils.isBlank(filter)) && (BooleanUtils.toBoolean(filter)) && (BooleanUtils.toBoolean(filter) == true))
			this.filter = filter;
	}

	public void setDecorator(String decorator) {
		this.decorator = decorator;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}
}