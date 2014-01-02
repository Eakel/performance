package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.ftl.DataGrid;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.util.DataGridHelper;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.util.FtlProcess;

public class TurnPageServlet extends HttpServlet {
	private static transient Log log = LogFactory.getLog(TurnPageServlet.class);

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		turn(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		turn(req, resp);
	}

	private void turn(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String page = req.getParameter("page");
			String strRowCount = req.getParameter("rowcount");
			String uuid = req.getParameter("uuid");

			long rowcount = 0L;

			if (!StringUtils.isBlank(strRowCount)) {
				rowcount = Long.parseLong(strRowCount);
			}

			DataGrid dataGrid = DataGridHelper.getDataGrid4Session(req, uuid);

			resp.setContentType("text/xml; charset=GBK");
			FtlProcess.process(req, resp.getWriter(), dataGrid, Integer.parseInt(page), rowcount, uuid);
		} catch (Exception ex) {
			log.error("´íÎó:", ex);
			throw new ServletException(ex);
		}
	}
}