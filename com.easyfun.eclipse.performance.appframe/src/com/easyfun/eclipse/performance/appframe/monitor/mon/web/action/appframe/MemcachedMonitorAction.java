package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asiainfo.appframe.ext.memcached.mbean.MemcachedMonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonServer;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MemcachedStatUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

/**
 * 监控Memcache，仅仅执行stats命令
 * 
 * @author linzhaoming
 * 
 * Created at 2012-9-16
 */
public class MemcachedMonitorAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(MemcachedMonitorAction.class);
	
	@ActionPermission(type = "WRITE")
	public void clearLocalCache(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tmp = request.getParameter("server_ids");
		String[] ids = StringUtils.split(tmp, ",");

		long[] tmpIds = new long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			tmpIds[i] = Long.parseLong(ids[i]);
		}

		Map map = ParallelUtil.clearLocalCache(20, 8, tmpIds);

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
	public void showLocalCache(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tmp = request.getParameter("server_ids");
		String[] ids = StringUtils.split(tmp, ",");
		StringBuffer all = new StringBuffer();

		long[] tmpIds = new long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			tmpIds[i] = Long.parseLong(ids[i]);
		}

		List rtn = ParallelUtil.getMemcachedLocalCacheInfo(20, 8, tmpIds);
		for (Iterator iter = rtn.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("<data>");
				sb.append("<SERVER_ID>" + ((Long) map.get("SERVER_ID")).longValue() + "</SERVER_ID>");
				sb.append("<NAME>" + (String) map.get("NAME") + "</NAME>");
				sb.append("<HIT_RATE>" + (String) map.get("HIT_RATE") + "</HIT_RATE>");
				sb.append("<SIZE>" + (String) map.get("SIZE") + "</SIZE>");
				sb.append("<CURRENT_BYTE_SIZE>" + (String) map.get("CURRENT_BYTE_SIZE") + "</CURRENT_BYTE_SIZE>");
				sb.append("<LIMIT_BYTES>" + (String) map.get("LIMIT_BYTES") + "</LIMIT_BYTES>");
				sb.append("<HIT>" + (String) map.get("HIT") + "</HIT>");
				sb.append("<MISS>" + (String) map.get("MISS") + "</MISS>");
				sb.append("<EVICT>" + (String) map.get("EVICT") + "</EVICT>");
				sb.append("<OVERLOAD>" + (String) map.get("OVERLOAD") + "</OVERLOAD>");
				sb.append("<UPTIME>" + (String) map.get("UPTIME") + "</UPTIME>");
				sb.append("<BUCKET>" + (String) map.get("BUCKET") + "</BUCKET>");
				sb.append("</data>");

				all.append(sb.toString());
			} catch (Throwable ex) {
				log.error("异常", ex);
			}
		}

		if (all.length() != 0)
			showInfo(response, all.toString(), "UTF-8");
	}
	
	@ActionPermission(type = "READ")
	public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		try {
			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
			MonServer objMonMbeanServer = objIMonSV.getMonServerByServerId(serverId);
			String str = objMonMbeanServer.getLocator();

			StringBuffer sb = new StringBuffer();

			String[] tmp = StringUtils.split(str, ",");
			for (int i = 0; i < tmp.length; i++) {
				try {
					String[] tmp2 = StringUtils.split(tmp[i], ":");
					HashMap map = MemcachedStatUtil.getStat(tmp2[0].trim(), Integer.parseInt(tmp2[1]));

					sb.append("<data>");
					Set key = map.keySet();
					for (Iterator iter = key.iterator(); iter.hasNext();) {
						String item = (String) iter.next();
						sb.append("<" + item + ">" + map.get(item).toString() + "</" + item + ">");
					}
					sb.append("<stat>" + map.toString() + "</stat>");
					sb.append("</data>");
				} catch (Exception ex) {
					log.error("获取信息出错", ex);
				}
			}
			showInfo(response, sb.toString());
		} catch (Exception ex) {
			log.error("异常", ex);
		}
	}
	
	@ActionPermission(type = "WRITE")
	public void downloadKeyFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int keyNum = 2000;
		String strServerId = request.getParameter("server_id");
		if ((StringUtils.isBlank(strServerId)) && (!StringUtils.isNumeric(strServerId))) {
			throw new Exception("strServerId不能为空或者不是数字");
		}

		String strKeyNum = request.getParameter("keyNum");
		if ((!StringUtils.isBlank(strKeyNum)) && (StringUtils.isNumeric(strKeyNum))) {
			keyNum = Integer.parseInt(strKeyNum);
		}

		String[] keys = null;
		MemcachedMonitorMBean objMemcachedMonitorMBean = null;
		try {
			objMemcachedMonitorMBean = (MemcachedMonitorMBean) ClientProxy.getObject(Long.parseLong(strServerId), MemcachedMonitorMBean.class);
			keys = objMemcachedMonitorMBean.getHotKeys(keyNum);
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objMemcachedMonitorMBean != null) {
				ClientProxy.destroyObject(objMemcachedMonitorMBean);
			}
		}

		if ((keys != null) && (keys.length != 0)) {
			response.setContentType("text/plain");
			response.setHeader("Content-disposition", "attachment;filename=" + strServerId + "_key.dat");
			OutputStream out = response.getOutputStream();
			for (int i = 0; i < keys.length; i++) {
				out.write((keys[i] + "\n").getBytes());
			}
			out.flush();
			out.close();
		}
	}
}