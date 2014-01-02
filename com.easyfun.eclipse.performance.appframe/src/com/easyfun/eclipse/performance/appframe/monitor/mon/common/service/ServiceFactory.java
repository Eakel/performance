package com.easyfun.eclipse.performance.appframe.monitor.mon.common.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ServiceFactory {
	private static transient Log log = LogFactory.getLog(ServiceFactory.class);
	private static IServiceInvoke objIServiceInvoke = null;
	
	static {
		try {
			objIServiceInvoke = new LocalServiceInvokeImpl();
		} catch (Exception ex) {
			throw new RuntimeException("服务工厂失败", ex);
		}
	}

	public static Object getService(Class interfaceClass) {
		return objIServiceInvoke.getService(interfaceClass);
	}

}