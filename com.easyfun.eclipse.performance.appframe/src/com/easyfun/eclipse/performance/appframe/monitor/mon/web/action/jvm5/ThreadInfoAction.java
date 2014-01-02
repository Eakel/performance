package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.jvm5;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ai.appframe2.complex.mbean.standard.jvm5.JVM5MonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class ThreadInfoAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(ThreadInfoAction.class);

	@ActionPermission(type = "READ")
	public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		JVM5MonitorMBean objJVM5MontiorMBean = null;
		try {
			objJVM5MontiorMBean = (JVM5MonitorMBean) ClientProxy.getObject(serverId, JVM5MonitorMBean.class);
			showInfo(response, objJVM5MontiorMBean.getAllThreadInfo());
		} catch (Exception ex) {
			log.error("“Ï≥£", ex);
		} finally {
			if (objJVM5MontiorMBean != null) {
				ClientProxy.destroyObject(objJVM5MontiorMBean);
			}
		}
	}
}