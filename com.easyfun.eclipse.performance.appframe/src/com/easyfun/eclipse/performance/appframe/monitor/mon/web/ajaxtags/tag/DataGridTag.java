package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.ftl.DataGrid;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.util.DataGridHelper;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.util.FtlProcess;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.util.UUID;

public class DataGridTag extends BodyTagSupport {
	private static transient Log log = LogFactory.getLog(DataGridTag.class);

	private List columnHeaderList = new ArrayList();

	private String tableid = null;
	private Long pagesize = new Long(20L);
	private Long width = new Long(800L);
	private Long height = new Long(500L);
	private String ftl = "datagrid.ftl";
	private String model = null;
	private String select = null;
	private boolean rownum = false;

	public int doEndTag() throws JspException {
		try {
			if (StringUtils.isBlank(this.tableid)) {
				throw new Exception("tableid不能为空!");
			}
			if (StringUtils.isBlank(this.model)) {
				throw new Exception("model不能为空!");
			}
			DataGrid dataGrid = new DataGrid();
			dataGrid.setTableid(this.tableid);
			dataGrid.setPagesize(this.pagesize);
			dataGrid.setWidth(this.width);
			dataGrid.setHeight(this.height);
			dataGrid.setFtl(this.ftl);
			dataGrid.setModel(this.model);
			dataGrid.setSelect(this.select);
			dataGrid.setRownum(this.rownum);
			dataGrid.setColumnList(this.columnHeaderList);

			this.pageContext.getOut().write("<div style='OVERFLOW: hidden;'>");

			int page = -1;
			HttpServletRequest req = (HttpServletRequest) this.pageContext.getRequest();
			String uuid = UUID.randomUUID().toString();
			FtlProcess.process(req, this.pageContext.getOut(), dataGrid, page, 0L, uuid);
			this.pageContext.getOut().write("</div>");
			DataGridHelper.setDataGrid2Session(req, uuid, dataGrid);
		} catch (Exception ex) {
			log.error("错误:", ex);
			throw new JspException(ex);
		}

		return 6;
	}

	public int doStartTag() throws JspException {
		this.columnHeaderList.clear();
		return 1;
	}

	public void addTag(Object obj) {
		this.columnHeaderList.add(obj);
	}

	public void setTableid(String tableid) {
		this.tableid = tableid;
	}

	public void setPagesize(String pagesize) {
		if ((!StringUtils.isBlank(pagesize)) && (StringUtils.isNumeric(pagesize)))
			this.pagesize = Long.valueOf(pagesize);
	}

	public void setWidth(String width) {
		if ((!StringUtils.isBlank(width)) && (StringUtils.isNumeric(width))) {
			this.width = Long.valueOf(width);
		}
	}

	public void setHeight(String height) {
		if ((!StringUtils.isBlank(height)) && (StringUtils.isNumeric(height))) {
			this.height = Long.valueOf(height);
		}
	}

	public void setFtl(String ftl) {
		this.ftl = ftl;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setRownum(String rownum) {
		if (BooleanUtils.toBoolean(rownum) == true) {
			this.rownum = BooleanUtils.toBoolean(rownum);
		}
	}

	public void setSelect(String select) {
		this.select = select;
	}
}