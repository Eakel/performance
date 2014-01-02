package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;

public class TestCPUTime2 extends Thread {
	public void run() {
		try {
			ThreadMXBean objThreadMXBean = ManagementFactory.getThreadMXBean();
			System.out.println("isThreadCpuTimeSupported=" + objThreadMXBean.isThreadCpuTimeSupported());
			long start = objThreadMXBean.getCurrentThreadCpuTime();

			HashMap map = new HashMap();
			Thread.currentThread();
			Thread.sleep(3000L);
			for (int i = 0; i < 100000; i++) {
				map.put(new Integer(i), new Integer(i));
			}

			System.out.println(Thread.currentThread().getName() + "ºÄÊ±:" + (objThreadMXBean.getCurrentThreadCpuTime() - start) + ":ns");
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		ThreadMXBean objThreadMXBean = ManagementFactory.getThreadMXBean();
		for (int i = 0; i < 10000000; i++) {
			objThreadMXBean.getCurrentThreadCpuTime();
		}
		System.out.println("ºÄÊ±:" + (System.currentTimeMillis() - start) + ":ms");
	}
}