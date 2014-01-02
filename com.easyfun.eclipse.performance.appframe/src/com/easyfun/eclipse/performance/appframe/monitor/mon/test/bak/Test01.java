package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.SSHUtil;

public class Test01 extends Thread {
	public void run() {
		try {
			String[] shellName = new String[5];
			String[] shell = new String[5];
			for (int i = 0; i < shellName.length; i++) {
				shell[i] = "echo `vmstat 1 2| tail -1 | awk '{print$22}'`";
				shellName[i] = ("yh" + Thread.currentThread().getName() + i + ".sh");
			}
			String[] rtn = SSHUtil.ssh4Shell("10.3.3.216", 22, "linzm",
					"aaa", shellName, shell);
			for (int i = 0; i < rtn.length; i++) {
				System.out.println(rtn[i]);
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 1; i++) {
			Test01 a = new Test01();
			a.start();
		}
	}
}