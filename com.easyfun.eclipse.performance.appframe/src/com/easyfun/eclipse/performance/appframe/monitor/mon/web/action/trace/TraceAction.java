package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.trace;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPHost;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MiscUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class TraceAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(TraceAction.class);

	/** 收集Trace的Callable */
	@ActionPermission(type = "WRITE")
	public void collect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		IMonSV objTreeSrv = (IMonSV) ServiceFactory.getService(IMonSV.class);
		MonPHost[] objMonPHost = objTreeSrv.getMonPHostForTrace();
		int rtn = ParallelUtil.collectTraceInfo(30, 6, objMonPHost);
		super.showInfo(response, "收集了" + rtn + "个trace文件");
	}

	@ActionPermission(type = "WRITE")
	public void copyTmpTraceFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filename = request.getParameter("filename");
		String tcdir = request.getParameter("tcdir");

		if ((!StringUtils.isBlank(filename)) && (!StringUtils.isBlank(tcdir))) {
			String dir = null;
			if (tcdir.equalsIgnoreCase("TRC_DATA")) {
				dir = MiscUtil.getTracePathPrefix() + "/data";
			} else if (tcdir.equalsIgnoreCase("TRC_BAK")) {
				dir = MiscUtil.getTracePathPrefix() + "/bak";
			}
			File file = new File(dir + "/" + filename);

			String clsAsResource = TraceAction.class.getName().replace('.', '/').concat(".class");
			String p1 = TraceAction.class.getClassLoader().getResource(clsAsResource).getPath();

			String p2 = StringUtils.substringBefore(p1, "WEB-INF") + "/mon/trace/tmp";

			log.debug("p2=" + p2);
			File f = new File(p2);
			File[] tmp = f.listFiles();
			for (int i = 0; i < tmp.length; i++) {
				tmp[i].delete();
			}

			FileUtils.writeByteArrayToFile(new File(p2 + "/" + file.getName()), FileUtils.readFileToByteArray(file));
			super.showInfo(response, "OK");
		}
	}

	@ActionPermission(type = "WRITE")
	public void moveBak(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tmp = request.getParameter("filename");
		if (!StringUtils.isBlank(tmp)) {
			String[] tmp2 = StringUtils.split(tmp, ",");
			String data = MiscUtil.getTracePathPrefix() + "/data";
			String bak = MiscUtil.getTracePathPrefix() + "/bak";

			for (int i = 0; i < tmp2.length; i++) {
				File file = new File(data + "/" + tmp2[i]);
				File dir = new File(bak);
				file.renameTo(new File(dir, file.getName()));
			}
		}

		super.showInfo(response, "转移到备份目录成功");
	}

	@ActionPermission(type = "WRITE")
	public void deleteBak(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean isAll = false;
		String bak = MiscUtil.getTracePathPrefix() + "/bak";
		File f = new File(bak);
		File[] tmp = f.listFiles();
		for (int i = 0; i < tmp.length; i++) {
			isAll = tmp[i].delete();
		}

		if (isAll) {
			super.showInfo(response, "删除备份目录完全成功");
		} else
			super.showInfo(response, "删除备份目录部分成功");
	}
}