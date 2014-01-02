package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ai.appframe2.complex.mbean.standard.cc.ClientControlMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonServer;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class AppSwitchAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(AppSwitchAction.class);

	@ActionPermission(type = "WRITE")
	public void switchApp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tmp = request.getParameter("condition");
		String[] tmp1 = StringUtils.split(tmp, "^");
		List error = new ArrayList();
		List success = new ArrayList();

		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);

		for (int i = 0; i < tmp1.length; i++) {
			String[] tmp2 = StringUtils.split(tmp1[i], ":");
			long id = Long.parseLong(tmp2[0]);
			String destAppCluster = tmp2[1].trim();
			MonServer objMonMbeanServer = objIMonSV.getMonServerByServerId(id);
			ClientControlMBean objClientControlMBean = null;
			try {
				objClientControlMBean = (ClientControlMBean) ClientProxy.getObject(id, ClientControlMBean.class);
				objClientControlMBean.connect(destAppCluster);
				success.add(objMonMbeanServer.getName());
			} catch (Exception ex) {
				error.add(objMonMbeanServer.getName());
				log.error("Òì³£", ex);
			} finally {
				if (objClientControlMBean != null) {
					ClientProxy.destroyObject(objClientControlMBean);
				}
			}
		}

		StringBuffer sb = new StringBuffer();
		for (Iterator iter = success.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			sb.append(item + " ³É¹¦\n");
		}

		for (Iterator iter = error.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			sb.append(item + " Ê§°Ü\n");
		}

		showInfo(response, sb.toString());
	}
}