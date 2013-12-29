package com.easyfun.eclipse.performance.trace;

import com.easyfun.eclipse.util.StringUtil;


public class TraceUtil {
	/** 根据文件名获取trace调用时间*/
	public static long getCostByFileName(String fileName){
		long et = 0L;
		if (!StringUtil.isBlank(fileName)) {
			String[] str = StringUtil.split(fileName, "_");
			if ((str != null) && (str.length >= 2) && (StringUtil.isNumeric(str[1]))) {
				et = Long.parseLong(str[1]);
			}
		}
		return et;
	}
}
