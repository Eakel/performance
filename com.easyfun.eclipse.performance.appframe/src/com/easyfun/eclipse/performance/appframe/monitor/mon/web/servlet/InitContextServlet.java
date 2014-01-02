package com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InitContextServlet implements ServletContextListener {
	private static transient Log log = LogFactory.getLog(InitContextServlet.class);

	public void contextInitialized(ServletContextEvent event) {
	}

	public void contextDestroyed(ServletContextEvent event) {
	}
}