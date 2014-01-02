package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ai.appframe2.complex.mbean.standard.IControl;
import com.ai.appframe2.complex.mbean.standard.sql.SQLMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.sql.SQLSummary;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class SQLMonitorAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(SQLMonitorAction.class);

	@ActionPermission(type = "READ")
	public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		String condition = request.getParameter("condition");
		SQLMonitorMBean objSQLMonitorMBean = null;
		try {
			objSQLMonitorMBean = (SQLMonitorMBean) ClientProxy.getObject(serverId, SQLMonitorMBean.class);
			SQLSummary[] objSQLSummary = objSQLMonitorMBean.fetchSQLSummary(condition);

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < objSQLSummary.length; i++) {
				sb.append("<data>");
				sb.append("<sql><![CDATA[" + objSQLSummary[i].getSql() + "]]></sql>");

				sb.append("<type>" + objSQLSummary[i].getType() + "</type>");
				sb.append("<min>" + objSQLSummary[i].getMin() + "</min>");
				sb.append("<max>" + objSQLSummary[i].getMax() + "</max>");
				sb.append("<avg>" + objSQLSummary[i].getAvg() + "</avg>");

				sb.append("<count>" + objSQLSummary[i].getCount() + "</count>");

				sb.append("<lastUseTime>" + objSQLSummary[i].getLastUseTime() + "</lastUseTime>");
				sb.append("<totalUseTime>" + objSQLSummary[i].getTotalUseTime() + "</totalUseTime>");
				sb.append("<last>" + new Date(objSQLSummary[i].getLast()) + "</last>");
				sb.append("</data>");
			}

			showInfo(response, sb.toString());
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objSQLMonitorMBean != null) {
				ClientProxy.destroyObject(objSQLMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "READ")
	public void status(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		SQLMonitorMBean objSQLMonitorMBean = null;
		try {
			objSQLMonitorMBean = (SQLMonitorMBean) ClientProxy.getObject(serverId, SQLMonitorMBean.class);
			IControl objIControl = objSQLMonitorMBean;
			showInfo(response, Boolean.toString(objIControl.fetchStatus()));
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objSQLMonitorMBean != null) {
				ClientProxy.destroyObject(objSQLMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "WRITE")
	public void enable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		long timeout = Long.parseLong(request.getParameter("timeout"));
		SQLMonitorMBean objSQLMonitorMBean = null;
		try {
			objSQLMonitorMBean = (SQLMonitorMBean) ClientProxy.getObject(serverId, SQLMonitorMBean.class);
			IControl objIControl = objSQLMonitorMBean;
			objIControl.enable(timeout);
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objSQLMonitorMBean != null) {
				ClientProxy.destroyObject(objSQLMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "WRITE")
	public void disable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		SQLMonitorMBean objSQLMonitorMBean = null;
		try {
			objSQLMonitorMBean = (SQLMonitorMBean) ClientProxy.getObject(serverId, SQLMonitorMBean.class);
			IControl objIControl = objSQLMonitorMBean;
			showInfo(response, Boolean.toString(objIControl.fetchStatus()));
			objIControl.disable();
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objSQLMonitorMBean != null) {
				ClientProxy.destroyObject(objSQLMonitorMBean);
			}
		}
	}
}