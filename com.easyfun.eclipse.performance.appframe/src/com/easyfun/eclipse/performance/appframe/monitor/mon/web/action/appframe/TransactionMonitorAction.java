package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ai.appframe2.complex.mbean.standard.tm.TmSummary;
import com.ai.appframe2.complex.mbean.standard.tm.TransactionMonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class TransactionMonitorAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(TransactionMonitorAction.class);

	@ActionPermission(type = "READ")
	public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		TransactionMonitorMBean objTransactionMonitorMBean = null;
		try {
			objTransactionMonitorMBean = (TransactionMonitorMBean) ClientProxy.getObject(serverId, TransactionMonitorMBean.class);

			TmSummary objTmSummary = objTransactionMonitorMBean.fetchTmSummary();

			StringBuffer sb = new StringBuffer();
			sb.append("<data>");
			sb.append("<start>" + objTmSummary.getStartCount() + "</start>");
			sb.append("<commit>" + objTmSummary.getCommitCount() + "</commit>");
			sb.append("<rollback>" + objTmSummary.getRollbackCount() + "</rollback>");
			sb.append("<suspend>" + objTmSummary.getSuspendCount() + "</suspend>");
			sb.append("<resume>" + objTmSummary.getResumeCount() + "</resume>");
			sb.append("</data>");

			showInfo(response, sb.toString());
		} catch (Exception ex) {
			log.error("“Ï≥£", ex);
		} finally {
			if (objTransactionMonitorMBean != null) {
				ClientProxy.destroyObject(objTransactionMonitorMBean);
			}
		}
	}
}