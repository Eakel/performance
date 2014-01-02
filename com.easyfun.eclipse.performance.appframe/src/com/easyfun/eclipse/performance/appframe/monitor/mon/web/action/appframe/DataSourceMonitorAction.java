package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ai.appframe2.complex.mbean.standard.datasource.DataSourceMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.datasource.DataSourceRuntime;
import com.ai.appframe2.complex.mbean.standard.datasource.DataSourceSummary;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class DataSourceMonitorAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(DataSourceMonitorAction.class);

	@ActionPermission(type = "READ")
	public void showConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		DataSourceMonitorMBean objDataSourceMonitorMBean = null;
		try {
			objDataSourceMonitorMBean = (DataSourceMonitorMBean) ClientProxy.getObject(serverId, DataSourceMonitorMBean.class);
			DataSourceSummary[] objDataSourceSummary = objDataSourceMonitorMBean.fetchAllDataSourceConfig();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < objDataSourceSummary.length; i++) {
				if (objDataSourceSummary[i] != null) {
					sb.append("<data>");
					sb.append("<ds>" + objDataSourceSummary[i].getDataSource() + "</ds>");
					sb.append("<driverClassName>" + objDataSourceSummary[i].getDriverClassName() + "</driverClassName>");
					sb.append("<url>" + objDataSourceSummary[i].getUrl() + "</url>");
					sb.append("<username>" + objDataSourceSummary[i].getUsername() + "</username>");
					sb.append("<initialSize>" + objDataSourceSummary[i].getInitialSize() + "</initialSize>");
					sb.append("<maxActive>" + objDataSourceSummary[i].getMaxActive() + "</maxActive>");

					sb.append("<maxIdle>" + objDataSourceSummary[i].getMaxIdle() + "</maxIdle>");
					sb.append("<minIdle>" + objDataSourceSummary[i].getMinIdle() + "</minIdle>");
					sb.append("<maxWait>" + objDataSourceSummary[i].getMaxWait() + "</maxWait>");
					sb.append("</data>");
				}
			}

			showInfo(response, sb.toString());
		} catch (Exception ex) {
			StringBuffer sb = new StringBuffer();
			sb.append("<data>");
			sb.append("<ds></ds>");
			sb.append("<driverClassName></driverClassName>");
			sb.append("<url></url>");
			sb.append("<username></username>");
			sb.append("<initialSize></initialSize>");
			sb.append("<maxActive></maxActive>");

			sb.append("<maxIdle></maxIdle>");
			sb.append("<minIdle></minIdle>");
			sb.append("<maxWait></maxWait>");
			sb.append("</data>");
			showInfo(response, sb.toString());
			log.error("“Ï≥£", ex);
		} finally {
			if (objDataSourceMonitorMBean != null) {
				ClientProxy.destroyObject(objDataSourceMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "READ")
	public void showRuntime(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		DataSourceMonitorMBean objDataSourceMonitorMBean = null;
		try {
			objDataSourceMonitorMBean = (DataSourceMonitorMBean) ClientProxy.getObject(serverId, DataSourceMonitorMBean.class);
			DataSourceRuntime[] objDataSourceRuntime = objDataSourceMonitorMBean.fetchAllDataSourceRuntime();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < objDataSourceRuntime.length; i++) {
				if (objDataSourceRuntime[i] != null) {
					sb.append("<data>");
					sb.append("<ds>" + objDataSourceRuntime[i].getDataSource() + "</ds>");
					sb.append("<numPhysical>" + objDataSourceRuntime[i].getNumPhysical() + "</numPhysical>");
					sb.append("<numIdle>" + objDataSourceRuntime[i].getNumIdle() + "</numIdle>");
					sb.append("<numActive>" + objDataSourceRuntime[i].getNumActive() + "</numActive>");
					sb.append("</data>");
				}
			}

			showInfo(response, sb.toString());
		} catch (Exception ex) {
			StringBuffer sb = new StringBuffer();
			sb.append("<data>");
			sb.append("<ds></ds>");
			sb.append("<numPhysical></numPhysical>");
			sb.append("<numIdle></numIdle>");
			sb.append("<numActive></numActive>");
			sb.append("</data>");

			showInfo(response, sb.toString());
			log.error("“Ï≥£", ex);
		} finally {
			if (objDataSourceMonitorMBean != null) {
				ClientProxy.destroyObject(objDataSourceMonitorMBean);
			}
		}
	}
}