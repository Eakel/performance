package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ai.appframe2.complex.mbean.standard.datasource.UnclosedNewConnRuntime;
import com.ai.appframe2.complex.mbean.standard.datasource.UnclosedNewConneMonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

/**
 * 
 * @author linzhaoming
 * 
 * Created at 2013-7-17
 */
public class UnClosedNewConnectionAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(SQLMonitorAction.class);

	@ActionPermission(type = "READ")
	public void showUnClosedNewConnection(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tmp = request.getParameter("server_ids");
		String[] ids = StringUtils.split(tmp, ",");
		StringBuffer all = new StringBuffer();

		long[] tmpIds = new long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			tmpIds[i] = Long.parseLong(ids[i]);
		}

		List rtn = ParallelUtil.getUnClosedNewConnection(20, 8, tmpIds);
		for (Iterator iter = rtn.iterator(); iter.hasNext();) {
			HashMap map = (HashMap) iter.next();
			try {
				StringBuffer sb = new StringBuffer();

				String serverName = (String) map.get("SERVER_NAME");
				String serverId = (String) map.get("SERVER_ID");
				UnclosedNewConnRuntime[] runtimes = (UnclosedNewConnRuntime[]) map.get("UNCLOSED_LIST");

				if ((runtimes != null) && (runtimes.length > 0)) {
					for (int i = 0; i < runtimes.length; i++) {
						sb.append("<data>");
						sb.append("<NAME>" + serverName + "</NAME>");
						sb.append("<SERVER_ID>" + serverId + "</SERVER_ID>");
						sb.append("<START_TIME>" + runtimes[i].getStartTime() + "</START_TIME>");
						sb.append("<DURATION>" + (System.currentTimeMillis() - runtimes[i].getStartTime().getTime()) / 1000L + "</DURATION>");
						sb.append("<DS>" + runtimes[i].getDataSource() + "</DS>");
						sb.append("<UUID>" + runtimes[i].getUuid() + "</UUID>");
						sb.append("<ADDR><![CDATA[" + runtimes[i].getAddr() + "]]></ADDR>");
						sb.append("</data>");
					}

					all.append(sb.toString());
				}
			} catch (Throwable ex) {
				log.error("“Ï≥£", ex);
			}
		}

		if (all.length() != 0) {
			showInfo(response, all.toString(), "UTF-8");
		}
	}

	@ActionPermission(type = "WRITE")
	public void forceClose(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String server_id = request.getParameter("server_id");
		String uuid = request.getParameter("UUID");

		long serverId = Long.parseLong(server_id);
		UnclosedNewConneMonitorMBean objUnclosedNewConneMonitorMBean = null;
		try {
			objUnclosedNewConneMonitorMBean = (UnclosedNewConneMonitorMBean) ClientProxy.getObject(serverId, UnclosedNewConneMonitorMBean.class);
			objUnclosedNewConneMonitorMBean.forceCloseNewConn(uuid);
		} catch (Exception ex) {
			log.error("“Ï≥£", ex);
		} finally {
			if (objUnclosedNewConneMonitorMBean != null) {
				ClientProxy.destroyObject(objUnclosedNewConneMonitorMBean);
			}
		}
	}
}