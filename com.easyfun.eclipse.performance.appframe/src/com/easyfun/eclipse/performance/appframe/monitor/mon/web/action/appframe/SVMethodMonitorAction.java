package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ai.appframe2.complex.mbean.standard.IControl;
import com.ai.appframe2.complex.mbean.standard.sv.SVMethodMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.sv.SVMethodSummary;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class SVMethodMonitorAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(SVMethodMonitorAction.class);

	@ActionPermission(type = "READ")
	public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		String condition = request.getParameter("condition");
		SVMethodMonitorMBean objSVMethodMonitorMBean = null;
		try {
			objSVMethodMonitorMBean = (SVMethodMonitorMBean) ClientProxy.getObject(serverId, SVMethodMonitorMBean.class);

			SVMethodSummary[] objSVMethodSummary = objSVMethodMonitorMBean.fetchSVMethodSummary(condition);

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < objSVMethodSummary.length; i++) {
				sb.append("<data>");
				sb.append("<className>" + objSVMethodSummary[i].getClassName() + "</className>");
				sb.append("<methodName>" + objSVMethodSummary[i].getMethodName() + "</methodName>");
				sb.append("<min>" + objSVMethodSummary[i].getMin() + "</min>");
				sb.append("<max>" + objSVMethodSummary[i].getMax() + "</max>");
				sb.append("<avg>" + objSVMethodSummary[i].getAvg() + "</avg>");

				sb.append("<totalCount>" + objSVMethodSummary[i].getTotalCount() + "</totalCount>");
				sb.append("<successCount>" + objSVMethodSummary[i].getSuccessCount() + "</successCount>");
				sb.append("<failCount>" + objSVMethodSummary[i].getFailCount() + "</failCount>");

				sb.append("<lastUseTime>" + objSVMethodSummary[i].getLastUseTime() + "</lastUseTime>");
				sb.append("<totalUseTime>" + objSVMethodSummary[i].getTotalUseTime() + "</totalUseTime>");
				sb.append("<last>" + new Date(objSVMethodSummary[i].getLast()) + "</last>");
				sb.append("</data>");
			}

			showInfo(response, sb.toString());
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objSVMethodMonitorMBean != null) {
				ClientProxy.destroyObject(objSVMethodMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "READ")
	public void status(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		SVMethodMonitorMBean objSVMethodMonitorMBean = null;
		try {
			objSVMethodMonitorMBean = (SVMethodMonitorMBean) ClientProxy.getObject(serverId, SVMethodMonitorMBean.class);

			IControl objIControl = objSVMethodMonitorMBean;
			showInfo(response, Boolean.toString(objIControl.fetchStatus()));
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objSVMethodMonitorMBean != null) {
				ClientProxy.destroyObject(objSVMethodMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "WRITE")
	public void enable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		long timeout = Long.parseLong(request.getParameter("timeout"));
		SVMethodMonitorMBean objSVMethodMonitorMBean = null;
		try {
			objSVMethodMonitorMBean = (SVMethodMonitorMBean) ClientProxy.getObject(serverId, SVMethodMonitorMBean.class);
			IControl objIControl = objSVMethodMonitorMBean;
			objIControl.enable(timeout);
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objSVMethodMonitorMBean != null) {
				ClientProxy.destroyObject(objSVMethodMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "WRITE")
	public void disable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		SVMethodMonitorMBean objSVMethodMonitorMBean = null;
		try {
			objSVMethodMonitorMBean = (SVMethodMonitorMBean) ClientProxy.getObject(serverId, SVMethodMonitorMBean.class);

			IControl objIControl = objSVMethodMonitorMBean;
			showInfo(response, Boolean.toString(objIControl.fetchStatus()));
			objIControl.disable();
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objSVMethodMonitorMBean != null) {
				ClientProxy.destroyObject(objSVMethodMonitorMBean);
			}
		}
	}
}