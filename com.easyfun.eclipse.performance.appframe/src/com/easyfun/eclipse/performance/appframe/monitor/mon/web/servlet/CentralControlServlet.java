package com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CentralControlServlet extends HttpServlet {
	private static transient Log log = LogFactory.getLog(CentralControlServlet.class);

	private static RequestProcessor rp = new RequestProcessor();

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			rp.process(req, resp);
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			rp.process(req, resp);
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
}