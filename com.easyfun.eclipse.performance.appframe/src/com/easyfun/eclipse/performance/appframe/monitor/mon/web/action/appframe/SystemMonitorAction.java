package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ai.appframe2.complex.mbean.standard.system.SystemMonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class SystemMonitorAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(SystemMonitorAction.class);

	@ActionPermission(type = "READ")
	public void prop(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		String condition = request.getParameter("condition");

		SystemMonitorMBean objSystemMonitorMBean = null;
		try {
			objSystemMonitorMBean = (SystemMonitorMBean) ClientProxy.getObject(serverId, SystemMonitorMBean.class);
			HashMap map = objSystemMonitorMBean.fetchSystemProperties(condition);
			Set keys = map.keySet();

			StringBuffer sb = new StringBuffer();
			for (Iterator iter = keys.iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sb.append("<data>");
				sb.append("<key>" + item + "</key>");
				sb.append("<value>" + map.get(item) + "</value>");
				sb.append("</data>");
			}

			showInfo(response, sb.toString());
		} catch (Exception ex) {
			log.error("“Ï≥£", ex);
		} finally {
			if (objSystemMonitorMBean != null) {
				ClientProxy.destroyObject(objSystemMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "READ")
	public void file(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		String condition = request.getParameter("condition");

		SystemMonitorMBean objSystemMonitorMBean = null;
		try {
			objSystemMonitorMBean = (SystemMonitorMBean) ClientProxy.getObject(serverId, SystemMonitorMBean.class);
			String file = objSystemMonitorMBean.fetchResourceFromClassPath(condition);
			showInfo(response, "<![CDATA[" + file + "]]>");
		} catch (Exception ex) {
			log.error("“Ï≥£", ex);
		} finally {
			if (objSystemMonitorMBean != null) {
				ClientProxy.destroyObject(objSystemMonitorMBean);
			}
		}
	}
	
	@ActionPermission(type = "READ")
	public void path(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		String condition = request.getParameter("condition");

		SystemMonitorMBean objSystemMonitorMBean = null;
		try {
			objSystemMonitorMBean = (SystemMonitorMBean) ClientProxy.getObject(serverId, SystemMonitorMBean.class);
			String path = objSystemMonitorMBean.fetchResourcePath(condition);
			showInfo(response, "<![CDATA[" + path + "]]>");
		} catch (Exception ex) {
			log.error("“Ï≥£", ex);
		} finally {
			if (objSystemMonitorMBean != null)
				ClientProxy.destroyObject(objSystemMonitorMBean);
		}
	}
}