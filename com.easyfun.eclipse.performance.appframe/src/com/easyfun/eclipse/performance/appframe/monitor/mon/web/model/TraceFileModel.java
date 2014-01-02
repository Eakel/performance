package com.easyfun.eclipse.performance.appframe.monitor.mon.web.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonTraceFile;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MiscUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.IDataGridModel;

public class TraceFileModel implements IDataGridModel {
	private String traceDir = null;

	public void setRequest(HttpServletRequest request) throws Exception {
	}

	public void setCondition(HashMap condition) throws Exception {
		this.traceDir = ((String) condition.get("TRC_DIR"));
	}

	public long count() throws Exception {
		int count = 0;
		if (this.traceDir != null) {
			String dir = null;
			
			if (this.traceDir.equalsIgnoreCase("TRC_DATA")) {
				dir = MiscUtil.getTracePathPrefix() + "/data";	//数据目录
			} else if (this.traceDir.equalsIgnoreCase("TRC_BAK")) {
				dir = MiscUtil.getTracePathPrefix() + "/bak";	//备份目录
			}

			File f = new File(dir);
			File[] tmp = f.listFiles();
			for (int i = 0; i < tmp.length; i++) {
				if (tmp[i].isFile()) {
					count++;
				}
			}
		}
		return count;
	}

	public Object[] getData(long startRowIndex, long endRowIndex) throws Exception {
		List list = new ArrayList();
		if (this.traceDir != null) {
			String dir = null;
			if (this.traceDir.equalsIgnoreCase("TRC_DATA")) {
				dir = MiscUtil.getTracePathPrefix() + "/data";
			} else if (this.traceDir.equalsIgnoreCase("TRC_BAK")) {
				dir = MiscUtil.getTracePathPrefix() + "/bak";
			}

			File f = new File(dir);
			File[] tmp = f.listFiles();
			for (int i = 0; i < tmp.length; i++) {
				if (tmp[i].isFile()) {
					MonTraceFile objMonTraceFile = new MonTraceFile();
					objMonTraceFile.setFileName(tmp[i].getName());
					objMonTraceFile.setDate(tmp[i].lastModified());
					objMonTraceFile.setSize(tmp[i].length());
					
			          long et = 0L;
			          if (!StringUtils.isBlank(tmp[i].getName())) {
			            String[] str = StringUtils.split(tmp[i].getName(), "_");
			            if ((str != null) && (str.length >= 2) && (StringUtils.isNumeric(str[1]))) {
			              et = Long.parseLong(str[1]);
			            }
			          }

			          objMonTraceFile.setEt(et);
					
					list.add(objMonTraceFile);
				}
			}
		}

		MonTraceFile[] yh = (MonTraceFile[]) list.toArray(new MonTraceFile[0]);
		Arrays.sort(yh);
		return yh;
	}
}