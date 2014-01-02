package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;

public class TestCPUTime {
	public static void main(String[] args) throws Exception {
		ThreadMXBean objThreadMXBean = ManagementFactory.getThreadMXBean();
		System.out.println("isThreadCpuTimeSupported=" + objThreadMXBean.isThreadCpuTimeSupported());

		long start = objThreadMXBean.getCurrentThreadCpuTime();

		HashMap map = new HashMap();
		Thread.currentThread();
		Thread.sleep(3000L);
		for (int i = 0; i < 1000000; i++) {
			map.put(new Integer(i), new Integer(i));
		}

		System.out.println("ºÄÊ±:" + (objThreadMXBean.getCurrentThreadCpuTime() - start) + ":ns");
	}
}