package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ai.appframe2.complex.mbean.standard.IControl;
import com.ai.appframe2.complex.mbean.standard.action.ActionMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.action.ActionSummary;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class ActionMonitorAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(ActionMonitorAction.class);

	@ActionPermission(type="READ")
	public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		String condition = request.getParameter("condition");

		ActionMonitorMBean objActionMonitorMBean = null;
		try {
			objActionMonitorMBean = (ActionMonitorMBean) ClientProxy.getObject(serverId, ActionMonitorMBean.class);
			ActionSummary[] objActionSummary = objActionMonitorMBean.fetchActionSummary(condition);

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < objActionSummary.length; i++) {
				sb.append("<data>");
				sb.append("<className>" + objActionSummary[i].getClassName() + "</className>");
				sb.append("<methodName>" + objActionSummary[i].getMethodName() + "</methodName>");
				sb.append("<min>" + objActionSummary[i].getMin() + "</min>");
				sb.append("<max>" + objActionSummary[i].getMax() + "</max>");
				sb.append("<avg>" + objActionSummary[i].getAvg() + "</avg>");

				sb.append("<totalCount>" + objActionSummary[i].getTotalCount() + "</totalCount>");
				sb.append("<successCount>" + objActionSummary[i].getSuccessCount() + "</successCount>");
				sb.append("<failCount>" + objActionSummary[i].getFailCount() + "</failCount>");

				sb.append("<lastUseTime>" + objActionSummary[i].getLastUseTime() + "</lastUseTime>");
				sb.append("<totalUseTime>" + objActionSummary[i].getTotalUseTime() + "</totalUseTime>");
				sb.append("<last>" + new Date(objActionSummary[i].getLast()) + "</last>");
				sb.append("</data>");
			}

			showInfo(response, sb.toString());
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objActionMonitorMBean != null) {
				ClientProxy.destroyObject(objActionMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "READ")
	public void status(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		ActionMonitorMBean objActionMonitorMBean = null;
		try {
			objActionMonitorMBean = (ActionMonitorMBean) ClientProxy.getObject(serverId, ActionMonitorMBean.class);
			IControl objIControl = objActionMonitorMBean;
			showInfo(response, Boolean.toString(objIControl.fetchStatus()));
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objActionMonitorMBean != null) {
				ClientProxy.destroyObject(objActionMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "WRITE")
	public void enable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		long timeout = Long.parseLong(request.getParameter("timeout"));
		ActionMonitorMBean objActionMonitorMBean = null;
		try {
			objActionMonitorMBean = (ActionMonitorMBean) ClientProxy.getObject(serverId, ActionMonitorMBean.class);
			IControl objIControl = objActionMonitorMBean;
			objIControl.enable(timeout);
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objActionMonitorMBean != null) {
				ClientProxy.destroyObject(objActionMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "WRITE")
	public void disable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		ActionMonitorMBean objActionMonitorMBean = null;
		try {
			objActionMonitorMBean = (ActionMonitorMBean) ClientProxy.getObject(serverId, ActionMonitorMBean.class);
			IControl objIControl = objActionMonitorMBean;
			showInfo(response, Boolean.toString(objIControl.fetchStatus()));
			objIControl.disable();
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objActionMonitorMBean != null) {
				ClientProxy.destroyObject(objActionMonitorMBean);
			}
		}
	}
}