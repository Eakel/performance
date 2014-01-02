package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.util;

import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.ftl.DataGrid;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.util.LengthLimitContainer;

public class DataGridHelper {
	private static transient Log log = LogFactory.getLog(DataGridHelper.class);
	public static final String DATA_GRID_SESSION_ID = "DATA_GRID_SESSION_ID";

	public static void setDataGrid2Session(HttpServletRequest request, String uuid, DataGrid dataGrid) throws Exception {
		Object obj = request.getSession().getAttribute(DATA_GRID_SESSION_ID);

		LengthLimitContainer container = null;
		if (obj == null) {
			container = new LengthLimitContainer(20);
			container.put(uuid, dataGrid);
		} else {
			container = (LengthLimitContainer) obj;
			container.put(uuid, dataGrid);
		}
		request.getSession().setAttribute(DATA_GRID_SESSION_ID, container);
	}

	public static DataGrid getDataGrid4Session(HttpServletRequest request, String uuid) throws Exception {
		DataGrid rtn = null;
		Object obj = request.getSession().getAttribute(DATA_GRID_SESSION_ID);
		if (obj != null) {
			rtn = (DataGrid) ((LengthLimitContainer) obj).get(uuid);
			if ((!StringUtils.isBlank(request.getParameter("condition"))) && (BooleanUtils.toBoolean(request.getParameter("condition").trim()))) {
				HashMap map = new HashMap();
				if (log.isDebugEnabled()) {
					log.debug("查询条件:" + StringUtils.substringAfter(request.getQueryString(), "condition=true"));
				}

				Hashtable table = HttpUtils.parseQueryString(StringUtils.substringAfter(request.getQueryString(), "condition=true"));
				Enumeration enu = table.keys();
				while (enu.hasMoreElements()) {
					String name = (String) enu.nextElement();
					Object tmp = table.get(name);
					if ((tmp.getClass().isArray()) && (Array.getLength(tmp) == 1)) {
						map.put(name, Array.get(tmp, 0));
					} else {
						map.put(name, tmp);
					}
				}
				rtn.setCondition(map);
			}
		}

		if (obj == null) {
			throw new Exception("无法获得DataGrid");
		}
		return rtn;
	}
}