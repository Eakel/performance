package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import org.jboss.remoting.transporter.TransporterClient;

import com.ai.appframe2.complex.mbean.standard.system.SystemMonitorMBean;

public class Test1 {
	public static void main(String[] args) throws Exception {
		SystemMonitorMBean objAppframeSessionMonitorMBean = (SystemMonitorMBean) TransporterClient.createTransporterClient("socket://10.3.3.213:64111", SystemMonitorMBean.class);
		System.out.println(objAppframeSessionMonitorMBean.printSystemProperties(""));
	}
}