package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ai.appframe2.complex.mbean.standard.trace.AppTraceMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.trace.WebTraceMonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MidServerControl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonCExec;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPHost;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonServer;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.HttpUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.SSHUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ibm.ByteArray;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class MidServerControlAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(MidServerControlAction.class);

	@ActionPermission(type = "READ")
	public void check(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String serverList = request.getParameter("server_list");
		if (StringUtils.isBlank(serverList)) {
			super.showInfo(response, "传入的server为空");
			return;
		}

		List list = new ArrayList();
		String[] servers = StringUtils.split(serverList, ",");

		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		for (int i = 0; i < servers.length; i++) {
			String serverName = servers[i].trim();
			try {
				MidServerControl objMidServerServerControl = objIMonSV.getMidServerServerControlByServerName(serverName);
				if (objMidServerServerControl != null) {
					String info = HttpUtil.curl(objMidServerServerControl.getUrl(), 10);
					list.add(serverName + " " + info);
				}
			} catch (Exception ex) {
				list.add(serverName + "执行失败:" + ex.getMessage());
				ex.printStackTrace();
			}
		}
		super.showInfo(response, StringUtils.join(list.iterator(), "\n"));
	}

	@ActionPermission(type = "WRITE")
	public void stop(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String serverList = request.getParameter("server_list");
		if (StringUtils.isBlank(serverList)) {
			super.showInfo(response, "传入的server为空");
			return;
		}

		String serverThreadCount = request.getParameter("serverThreadCount");
		if (StringUtils.isBlank(serverThreadCount)) {
			super.showInfo(response, "传入的server并行执行线程为空");
			return;
		}
		if (!StringUtils.isNumeric(serverThreadCount)) {
			super.showInfo(response, "传入的server并行执行线程不是数字");
			return;
		}

		String serverTimeout = request.getParameter("serverTimeout");
		if (StringUtils.isBlank(serverTimeout)) {
			super.showInfo(response, "传入的server并行执行超时为空");
			return;
		}
		if (!StringUtils.isNumeric(serverTimeout)) {
			super.showInfo(response, "传入的serverb并行执行超时不是数字");
			return;
		}

		List list = new ArrayList();
		String[] servers = StringUtils.split(serverList, ",");

		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		for (int i = 0; i < servers.length; i++) {
			String serverName = servers[i].trim();
			try {
				MidServerControl objMidServerServerControl = objIMonSV.getMidServerServerControlByServerName(serverName);
				if (objMidServerServerControl != null) {
					MonCExec objMonCExec = objIMonSV.getMonCExecByExecId(objMidServerServerControl.getStopExecId());
					MonPHost objMonPHost = objIMonSV.getMonPHostByHostname(objMidServerServerControl.getHostname());

					ParallelUtil.MidServerTask objMidServerTask = new ParallelUtil.MidServerTask();
					objMidServerTask.ip = objMonPHost.getIp();
					objMidServerTask.port = (int) objMonPHost.getSshport();
					objMidServerTask.username = objMonPHost.getUsername();
					objMidServerTask.password = objMonPHost.getPassword();
					objMidServerTask.serverName = objMidServerServerControl.getServerName();
					objMidServerTask.path = (objMidServerServerControl.getPfPath() + " " + objMidServerServerControl.getServerName());
					objMidServerTask.shell = objMonCExec.getExpr();
					list.add(objMidServerTask);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		String[] rtn = ParallelUtil.computeMidServer(Integer.parseInt(serverThreadCount), Integer.parseInt(serverTimeout),
				(ParallelUtil.MidServerTask[]) list.toArray(new ParallelUtil.MidServerTask[0]));

		super.showInfo(response, StringUtils.join(rtn, "\n"));
	}

	@ActionPermission(type = "WRITE")
	public void start(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String serverList = request.getParameter("server_list");
		if (StringUtils.isBlank(serverList)) {
			super.showInfo(response, "传入的server为空");
			return;
		}

		String serverThreadCount = request.getParameter("serverThreadCount");
		if (StringUtils.isBlank(serverThreadCount)) {
			super.showInfo(response, "传入的server并行执行线程为空");
			return;
		}
		if (!StringUtils.isNumeric(serverThreadCount)) {
			super.showInfo(response, "传入的server并行执行线程不是数字");
			return;
		}

		String serverTimeout = request.getParameter("serverTimeout");
		if (StringUtils.isBlank(serverTimeout)) {
			super.showInfo(response, "传入的server并行执行超时为空");
			return;
		}
		if (!StringUtils.isNumeric(serverTimeout)) {
			super.showInfo(response, "传入的serverb并行执行超时不是数字");
			return;
		}

		List list = new ArrayList();
		String[] servers = StringUtils.split(serverList, ",");

		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		for (int i = 0; i < servers.length; i++) {
			String serverName = servers[i].trim();
			try {
				MidServerControl objMidServerServerControl = objIMonSV.getMidServerServerControlByServerName(serverName);
				if (objMidServerServerControl != null) {
					MonCExec objMonCExec = objIMonSV.getMonCExecByExecId(objMidServerServerControl.getStartExecId());
					MonPHost objMonPHost = objIMonSV.getMonPHostByHostname(objMidServerServerControl.getHostname());

					ParallelUtil.MidServerTask objMidServerTask = new ParallelUtil.MidServerTask();
					objMidServerTask.ip = objMonPHost.getIp();
					objMidServerTask.port = (int) objMonPHost.getSshport();
					objMidServerTask.username = objMonPHost.getUsername();
					objMidServerTask.password = objMonPHost.getPassword();
					objMidServerTask.serverName = objMidServerServerControl.getServerName();
					objMidServerTask.path = (objMidServerServerControl.getPfPath() + " " + objMidServerServerControl.getServerName());
					objMidServerTask.shell = objMonCExec.getExpr();
					list.add(objMidServerTask);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		String[] rtn = ParallelUtil.computeMidServer(Integer.parseInt(serverThreadCount), Integer.parseInt(serverTimeout),
				(ParallelUtil.MidServerTask[]) (ParallelUtil.MidServerTask[]) list.toArray(new ParallelUtil.MidServerTask[0]));

		super.showInfo(response, StringUtils.join(rtn, "\n"));
	}

	@ActionPermission(type = "WRITE")
	public void restart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String serverList = request.getParameter("server_list");
		if (StringUtils.isBlank(serverList)) {
			super.showInfo(response, "传入的server为空");
			return;
		}

		List list = new ArrayList();
		String[] servers = StringUtils.split(serverList, ",");

		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		for (int i = 0; i < servers.length; i++) {
			String serverName = servers[i].trim();
			try {
				MidServerControl objMidServerServerControl = objIMonSV.getMidServerServerControlByServerName(serverName);
				MonPHost objMonPHost = objIMonSV.getMonPHostByHostname(objMidServerServerControl.getHostname());
				if (objMidServerServerControl != null) {
					MonCExec objMonCExec = objIMonSV.getMonCExecByExecId(objMidServerServerControl.getStopExecId());
					SSHUtil.ssh4Shell(objMonPHost.getIp(), (int) objMonPHost.getSshport(), objMonPHost.getUsername(), objMonPHost.getPassword(),
							objMidServerServerControl.getServerName(), objMidServerServerControl.getPfPath() + " " + objMidServerServerControl.getServerName(),
							objMonCExec.getExpr());
				}

				if (objMidServerServerControl != null) {
					MonCExec objMonCExec = objIMonSV.getMonCExecByExecId(objMidServerServerControl.getStartExecId());
					SSHUtil.ssh4Shell(objMonPHost.getIp(), (int) objMonPHost.getSshport(), objMonPHost.getUsername(), objMonPHost.getPassword(),
							objMidServerServerControl.getServerName(), objMidServerServerControl.getPfPath() + " " + objMidServerServerControl.getServerName(),
							objMonCExec.getExpr());
				}

				list.add(serverName + "执行成功");
			} catch (Exception ex) {
				list.add(serverName + "执行失败:" + ex.getMessage());
				ex.printStackTrace();
			}
		}
		super.showInfo(response, StringUtils.join(list.iterator(), "\n"));
	}

	@ActionPermission(type = "READ")
	public void comptuerOrbId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String serverList = request.getParameter("server_list");
		if (StringUtils.isBlank(serverList)) {
			super.showInfo(response, "传入的server为空");
			return;
		}

		List list = new ArrayList();
		String[] servers = StringUtils.split(serverList, ",");

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < servers.length; i++) {
			String serverName = servers[i].trim();

			ByteArray bytearray = new ByteArray(serverName.getBytes());
			int j = bytearray.hashCode();
			sb.append(serverName + "=" + Integer.toHexString(j & 0x7FFFFFFF) + "\n");
		}
		super.showInfo(response, sb.toString());
	}

	@ActionPermission(type = "WRITE")
	public void setAppTrace(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String serverList = request.getParameter("server_list");
		if (StringUtils.isBlank(serverList)) {
			super.showInfo(response, "传入的server为空");
			return;
		}

		String enable = request.getParameter("enable");
		if (StringUtils.isBlank(enable)) {
			super.showInfo(response, "传入的enable为空");
			return;
		}
		if (!StringUtils.isNumeric(enable)) {
			super.showInfo(response, "传入的enable不是数字");
			return;
		}

		String code = request.getParameter("code");

		if ((enable.equalsIgnoreCase("1")) && (StringUtils.isBlank(code))) {
			super.showInfo(response, "传入的code为空");
			return;
		}

		String className = request.getParameter("className");
		String methodName = request.getParameter("methodName");

		List list = new ArrayList();

		String[] servers = StringUtils.split(serverList, ",");
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		for (int i = 0; i < servers.length; i++) {
			String serverName = servers[i].trim();
			try {
				MonServer objMonServer = objIMonSV.getMonServerByServerName(serverName);
				if (objMonServer != null) {
					AppTraceMonitorMBean objAppTraceMonitor = null;
					try {
						objAppTraceMonitor = (AppTraceMonitorMBean) ClientProxy.getObject(objMonServer.getServerId(), AppTraceMonitorMBean.class);
						if (enable.equalsIgnoreCase("1")) {
							objAppTraceMonitor.enable(code, className, methodName);
						} else if (enable.equalsIgnoreCase("0")) {
							objAppTraceMonitor.disable();
						} else {
							throw new Exception("无法识别的类型:" + enable);
						}

						list.add(serverName);
					} catch (Exception ex) {
						log.error("异常", ex);
					} finally {
						if (objAppTraceMonitor != null) {
							ClientProxy.destroyObject(objAppTraceMonitor);
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		super.showInfo(response, StringUtils.join(list.iterator(), "\n"));
	}

	@ActionPermission(type = "WRITE")
	public void setWebTrace(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String serverList = request.getParameter("server_list");
		if (StringUtils.isBlank(serverList)) {
			super.showInfo(response, "传入的server为空");
			return;
		}

		String enable = request.getParameter("enable");
		if (StringUtils.isBlank(enable)) {
			super.showInfo(response, "传入的enable为空");
			return;
		}
		if (!StringUtils.isNumeric(enable)) {
			super.showInfo(response, "传入的enable不是数字");
			return;
		}

		String code = request.getParameter("code");

		if ((enable.equalsIgnoreCase("1")) && (StringUtils.isBlank(code))) {
			super.showInfo(response, "传入的code为空");
			return;
		}

		String url = request.getParameter("url");
		String client_ip = request.getParameter("client_ip");

		String duration = request.getParameter("duration");

		if ((enable.equalsIgnoreCase("1")) && (StringUtils.isBlank(duration))) {
			super.showInfo(response, "传入的duration为空");
			return;
		}
		if ((!StringUtils.isBlank(duration)) && (!StringUtils.isNumeric(duration))) {
			super.showInfo(response, "传入的duration不是数字");
			return;
		}

		List list = new ArrayList();

		String[] servers = StringUtils.split(serverList, ",");
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		for (int i = 0; i < servers.length; i++) {
			String serverName = servers[i].trim();
			try {
				MonServer objMonServer = objIMonSV.getMonServerByServerName(serverName);
				if (objMonServer != null) {
					WebTraceMonitorMBean objWebTraceMonitorMBean = null;
					try {
						objWebTraceMonitorMBean = (WebTraceMonitorMBean) ClientProxy.getObject(objMonServer.getServerId(), WebTraceMonitorMBean.class);
						if (enable.equalsIgnoreCase("1")) {
							objWebTraceMonitorMBean.enable(code, url, client_ip, Integer.parseInt(duration));
						} else if (enable.equalsIgnoreCase("0")) {
							objWebTraceMonitorMBean.disable();
						} else {
							throw new Exception("无法识别的类型:" + enable);
						}

						list.add(serverName);
					} catch (Exception ex) {
						log.error("异常", ex);
					} finally {
						if (objWebTraceMonitorMBean != null)
							ClientProxy.destroyObject(objWebTraceMonitorMBean);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		super.showInfo(response, StringUtils.join(list.iterator(), "\n"));
	}
}