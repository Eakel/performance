package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import java.sql.Timestamp;
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

import com.ai.appframe2.complex.mbean.standard.session.AppframeSessionMonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MiscUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class AppframeSessionMonitorAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(AppframeSessionMonitorAction.class);

	private static final HashMap REGION_MAP = new HashMap();

	static {
		REGION_MAP.put("A", "郑州");
		REGION_MAP.put("R", "南阳");
		REGION_MAP.put("C", "洛阳");
		REGION_MAP.put("N", "商丘");
		REGION_MAP.put("G", "新乡");
		REGION_MAP.put("P", "周口");
		REGION_MAP.put("Q", "驻马店");
		REGION_MAP.put("D", "平顶山");
		REGION_MAP.put("S", "信阳");
		REGION_MAP.put("E", "安阳");
		REGION_MAP.put("K", "许昌");
		REGION_MAP.put("H", "焦作");
		REGION_MAP.put("B", "开封");
		REGION_MAP.put("J", "濮阳");
		REGION_MAP.put("M", "三门峡");
		REGION_MAP.put("L", "漯河");
		REGION_MAP.put("U", "济源");
		REGION_MAP.put("F", "鹤壁");
		REGION_MAP.put("X", "省中心");
	}

	@ActionPermission(type="READ")
	public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long serverId = Long.parseLong(request.getParameter("server_id"));
		AppframeSessionMonitorMBean objAppframeSessionMonitorMBean = null;
		try {
			objAppframeSessionMonitorMBean = (AppframeSessionMonitorMBean) ClientProxy.getObject(serverId, AppframeSessionMonitorMBean.class);

			HashMap[] objUserInfo = objAppframeSessionMonitorMBean.fetchLogedUsers();

			int count = 0;
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < objUserInfo.length; i++) {
				if (objUserInfo[i] != null) {
					sb.append("<data>");
					sb.append("<SERVER_NAME>" + (String) objUserInfo[i].get("SERVER_NAME") + "</SERVER_NAME>");
					sb.append("<IP>" + (String) objUserInfo[i].get("IP") + "</IP>");
					sb.append("<CODE>" + (String) objUserInfo[i].get("CODE") + "</CODE>");
					sb.append("<NAME>" + (String) objUserInfo[i].get("NAME") + "</NAME>");
					sb.append("<ORG_NAME>" + (String) objUserInfo[i].get("ORG_NAME") + "</ORG_NAME>");
					sb.append("<LOGIN_TIME>" + ((Timestamp) objUserInfo[i].get("LOGIN_TIME")).toString() + "</LOGIN_TIME>");
					sb.append("<SESSION_ID><![CDATA[" + (String) objUserInfo[i].get("SESSION_ID") + "]]></SESSION_ID>");
					sb.append("<ATTRS><![CDATA[" + objUserInfo[i].get("ATTRS").toString() + "]]></ATTRS>");
					sb.append("</data>");
					count++;
				}
			}
			sb.append("<size>" + count + "</size>");

			showInfo(response, sb.toString(), "UTF-8");
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objAppframeSessionMonitorMBean != null)
				ClientProxy.destroyObject(objAppframeSessionMonitorMBean);
		}
	}

	@ActionPermission(type = "WRITE")
	public void forceLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tmp = request.getParameter("condition");
		System.out.println(tmp);
		String[] tmp1 = StringUtils.split(tmp, "^");
		String serialId = tmp1[1].trim();

		long serverId = Long.parseLong(tmp1[0]);
		AppframeSessionMonitorMBean objAppframeSessionMonitorMBean = null;
		try {
			objAppframeSessionMonitorMBean = (AppframeSessionMonitorMBean) ClientProxy.getObject(serverId, AppframeSessionMonitorMBean.class);

			objAppframeSessionMonitorMBean.logoutBySerialId(new String[] { serialId });
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objAppframeSessionMonitorMBean != null)
				ClientProxy.destroyObject(objAppframeSessionMonitorMBean);
		}
	}

	@ActionPermission(type="READ")
	public void showAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int count = 0;
		HashMap ip = new HashMap();
		HashMap regionSize = new HashMap();
		HashMap regionIP = new HashMap();

		String tmp = request.getParameter("server_ids");
		String[] ids = StringUtils.split(tmp, ",");
		StringBuffer all = new StringBuffer();

		long[] tmpIds = new long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			tmpIds[i] = Long.parseLong(ids[i]);
		}

		List rtn = ParallelUtil.getSessionUserInfo(20, 8, tmpIds);
		for (Iterator iter = rtn.iterator(); iter.hasNext();) {
			HashMap[] objUserInfo = (HashMap[]) iter.next();
			try {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < objUserInfo.length; i++) {
					if (objUserInfo[i] != null) {
						sb.append("<data>");
						sb.append("<SERVER_NAME>" + (String) objUserInfo[i].get("SERVER_NAME") + "</SERVER_NAME>");
						sb.append("<IP>" + (String) objUserInfo[i].get("IP") + "</IP>");
						sb.append("<CODE>" + (String) objUserInfo[i].get("CODE") + "</CODE>");
						sb.append("<NAME>" + (String) objUserInfo[i].get("NAME") + "</NAME>");
						sb.append("<ORG_NAME>" + (String) objUserInfo[i].get("ORG_NAME") + "</ORG_NAME>");
						sb.append("<LOGIN_TIME>" + ((Timestamp) objUserInfo[i].get("LOGIN_TIME")).toString() + "</LOGIN_TIME>");
						sb.append("<SESSION_ID><![CDATA[" + (String) objUserInfo[i].get("SESSION_ID") + "]]></SESSION_ID>");
						sb.append("<SERIAL_ID>" + (String) objUserInfo[i].get("SERIAL_ID") + "</SERIAL_ID>");
						sb.append("<SERVER_ID>" + ((Long) objUserInfo[i].get("SERVER_ID")).longValue() + "</SERVER_ID>");
						Boolean isLogout = (Boolean) objUserInfo[i].get("IS_LOGOUTED");
						if (isLogout == null) {
							sb.append("<IS_LOGOUTED>正常</IS_LOGOUTED>");
						} else {
							sb.append("<IS_LOGOUTED>已注销</IS_LOGOUTED>");
						}

						sb.append("</data>");

						count++;
						ip.put((String) objUserInfo[i].get("IP"), null);

						Object attrs = objUserInfo[i].get("ATTRS");
						if ((attrs != null) && ((attrs instanceof Map))) {
							Map map = (Map) attrs;
							String regionId = (String) map.get(MiscUtil.getUserInfoRegionIdKey());

							if (regionSize.containsKey(regionId)) {
								Integer tmpSize = (Integer) regionSize.get(regionId);
								regionSize.put(regionId, new Integer(tmpSize.intValue() + 1));
							} else {
								Integer tmpSize = new Integer(1);
								regionSize.put(regionId, new Integer(tmpSize.intValue()));
							}

							if (regionIP.containsKey(regionId)) {
								HashMap tmpMap = (HashMap) regionIP.get(regionId);
								tmpMap.put((String) objUserInfo[i].get("IP"), null);
								regionIP.put(regionId, tmpMap);
							} else {
								HashMap tmpMap = new HashMap();
								tmpMap.put((String) objUserInfo[i].get("IP"), null);
								regionIP.put(regionId, tmpMap);
							}
						}
					}
				}

				all.append(sb.toString());
			} catch (Throwable ex) {
				log.error("异常", ex);
			}

		}

		all.append("<size>" + count + "</size>");
		all.append("<ip_size>" + ip.size() + "</ip_size>");
		if (!regionSize.isEmpty()) {
			all.append("<region_size>");
			Set key = regionSize.keySet();
			for (Iterator iter = key.iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				all.append("<data>");
				if (REGION_MAP.containsKey(item.toUpperCase())) {
					all.append("<real_region_id>" + REGION_MAP.get(item.toUpperCase()).toString() + "(" + item + ")</real_region_id>");
				} else {
					all.append("<real_region_id>" + item + "</real_region_id>");
				}
				all.append("<size>" + ((Integer) regionSize.get(item)).intValue() + "</size>");
				all.append("<ip_size>" + ((HashMap) regionIP.get(item)).size() + "</ip_size>");
				all.append("</data>");
			}
			all.append("</region_size>");
		}

		if (all.length() != 0)
			showInfo(response, all.toString(), "UTF-8");
	}

}