package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import com.ai.appframe2.complex.mbean.standard.jvm5.JVM5Monitor;

public class Test2 {
	public static void main(String[] args) throws Exception {
		JVM5Monitor a = new JVM5Monitor();
		System.out.println(a.getAllThreadInfo());
	}
}