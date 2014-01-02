package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.trace.TraceAction;

public class Test77 {
	public static void main(String[] args) throws Exception {
		String clsAsResource = TraceAction.class.getName().replace('.', '/').concat(".class");
		String p1 = TraceAction.class.getClassLoader().getResource(clsAsResource).getPath();

		String p2 = StringUtils.substringBefore(p1, "WEB-INF") + "/mon/trace/tmp";
		System.out.println(p2);
	}
}