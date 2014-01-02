package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class SecframeMonitorAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(SecframeMonitorAction.class);

	@ActionPermission(type = "READ")
	public void showLocalCache(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tmp = request.getParameter("server_ids");
		String[] ids = StringUtils.split(tmp, ",");
		StringBuffer all = new StringBuffer();

		long[] tmpIds = new long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			tmpIds[i] = Long.parseLong(ids[i]);
		}

		List rtn = ParallelUtil.getSecMemLocalCacheInfo(20, 8, tmpIds);
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

	@ActionPermission(type = "WRITE")
	public void clearLocalCache(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tmp = request.getParameter("server_ids");
		String[] ids = StringUtils.split(tmp, ",");

		long[] tmpIds = new long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			tmpIds[i] = Long.parseLong(ids[i]);
		}

		Map map = ParallelUtil.clearSecMemLocalCache(20, 8, tmpIds);

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
}