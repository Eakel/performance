package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ai.appframe2.complex.mbean.standard.cache.CacheMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.cache.CacheSummary;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonServer;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class CacheMonitorAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(CacheMonitorAction.class);

	@ActionPermission(type = "READ")
	public static String[] getAllCaches(long serverId) throws Exception {
		String[] rtn = null;
		CacheMonitorMBean objCacheMonitorMBean = null;
		try {
			objCacheMonitorMBean = (CacheMonitorMBean) ClientProxy.getObject(serverId, CacheMonitorMBean.class);
			rtn = objCacheMonitorMBean.listAllCache();
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objCacheMonitorMBean != null) {
				ClientProxy.destroyObject(objCacheMonitorMBean);
			}
		}
		return rtn;
	}

	@ActionPermission(type = "WRITE")
	public static HashMap refreshCache(long[] serverIds, String[] cacheIds) throws Exception {
		HashMap map = new HashMap();

		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		MonServer[] objMonServer = objIMonSV.getMonServerByServerId(serverIds);

		for (int i = 0; i < serverIds.length; i++) {
			CacheMonitorMBean objCacheMonitorMBean = null;

			List l = new ArrayList();
			try {
				objCacheMonitorMBean = (CacheMonitorMBean) ClientProxy.getObject(serverIds[i], CacheMonitorMBean.class);
				for (int j = 0; j < cacheIds.length; j++)
					try {
						objCacheMonitorMBean.forceRefresh(cacheIds[j]);
					} catch (Throwable ex) {
						l.add(cacheIds[j] + "失败");
						log.error("刷新异常", ex);
					}
			} catch (Throwable ex) {
				map.put(new Long(serverIds[i]), objMonServer[i].getName() + ",连接失败,该server上的所有cache刷新失败");
				log.error("获得服务异常", ex);
			} finally {
				if (objCacheMonitorMBean != null) {
					ClientProxy.destroyObject(objCacheMonitorMBean);
				}
			}

			if (!l.isEmpty()) {
				map.put(new Long(serverIds[i]), objMonServer[i].getName() + ",刷新cache失败列表," + StringUtils.join(l.iterator(), ","));
			}
		}
		return map;
	}

	@ActionPermission(type = "WRITE")
	public void refreshCache(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String serverIdsStr = request.getParameter("server_ids");
		String[] tmp = StringUtils.split(serverIdsStr, ",");
		long[] serverIds = new long[tmp.length];
		for (int i = 0; i < serverIds.length; i++) {
			serverIds[i] = Long.parseLong(tmp[i]);
		}

		String cacheIdsStr = request.getParameter("cache_ids");
		String[] cacheIds = StringUtils.split(cacheIdsStr, ",");

		HashMap map = refreshCache(serverIds, cacheIds);
		if (map.isEmpty()) {
			showInfo(response, "全部刷新成功");
		} else {
			StringBuffer sb = new StringBuffer();
			Collection c = map.values();
			for (Iterator iter = c.iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sb.append(item + "\n");
			}
			showInfo(response, "部分刷新成功\n" + sb.toString());
		}
	}

	@ActionPermission(type = "READ")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		CacheMonitorMBean objCacheMonitorMBean = null;
		try {
			objCacheMonitorMBean = (CacheMonitorMBean) ClientProxy.getObject(serverId, CacheMonitorMBean.class);
			String[] str = objCacheMonitorMBean.listAllCache();

			StringBuffer sb = new StringBuffer();
			sb.append("<data>");
			sb.append("<name>ALL</name>");
			sb.append("</data>");

			for (int i = 0; i < str.length; i++) {
				sb.append("<data>");
				sb.append("<name>" + str[i] + "</name>");
				sb.append("</data>");
			}

			showInfo(response, sb.toString());
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objCacheMonitorMBean != null) {
				ClientProxy.destroyObject(objCacheMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "READ")
	public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		String condition = request.getParameter("condition");
		if ((condition != null) && (condition.equalsIgnoreCase("ALL"))) {
			condition = "";
		}
		CacheMonitorMBean objCacheMonitorMBean = null;
		try {
			objCacheMonitorMBean = (CacheMonitorMBean) ClientProxy.getObject(serverId, CacheMonitorMBean.class);
			CacheSummary[] objCacheSummary = objCacheMonitorMBean.fetchCache(condition);

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < objCacheSummary.length; i++) {
				sb.append("<data>");
				sb.append("<className>" + objCacheSummary[i].getClassName() + "</className>");
				sb.append("<lastRefreshStartTime>" + new Date(objCacheSummary[i].getLastRefreshStartTime()) + "</lastRefreshStartTime>");
				sb.append("<lastRefreshEndTime>" + new Date(objCacheSummary[i].getLastRefreshEndTime()) + "</lastRefreshEndTime>");
				sb.append("<oldCount>" + objCacheSummary[i].getOldCount() + "</oldCount>");
				sb.append("<newCount>" + objCacheSummary[i].getNewCount() + "</newCount>");
				sb.append("</data>");
			}

			showInfo(response, sb.toString());
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objCacheMonitorMBean != null) {
				ClientProxy.destroyObject(objCacheMonitorMBean);
			}
		}
	}

	@ActionPermission(type = "WRITE")
	public void forceRefresh(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		String condition = request.getParameter("condition");
		CacheMonitorMBean objCacheMonitorMBean = null;
		try {
			objCacheMonitorMBean = (CacheMonitorMBean) ClientProxy.getObject(serverId, CacheMonitorMBean.class);
			objCacheMonitorMBean.forceRefresh(condition);
			showInfo(response, "刷新完成");
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objCacheMonitorMBean != null) {
				ClientProxy.destroyObject(objCacheMonitorMBean);
			}
		}
	}
}