package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class ConfigAction extends BaseAction {
	@ActionPermission(type = "WRITE")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			long templateId = Long.parseLong(getParameter(request, "templateId"));
			long parentTreeId = Long.parseLong(getParameter(request, "parentTreeId"));

			String nodeName = getParameter(request, "nodeName");
			String serverName = getParameter(request, "serverName");
			String serverImpl = getParameter(request, "serverImpl");
			String mapping = getParameter(request, "mapping");

			HashMap map = new HashMap();
			String[] tmp1 = StringUtils.split(mapping, ";");
			for (int i = 0; i < tmp1.length; i++) {
				String[] tmp2 = StringUtils.split(tmp1[i], ",");
				map.put(tmp2[0], tmp2[1]);
			}

			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
			objIMonSV.addServer(templateId, parentTreeId, nodeName, serverName, serverImpl, map);
			showHtmlInfo(response, "保存成功");
		} catch (Exception ex) {
			ex.printStackTrace();
			showHtmlInfo(response, "保存失败:\n" + ex.getMessage());
		}
	}
}