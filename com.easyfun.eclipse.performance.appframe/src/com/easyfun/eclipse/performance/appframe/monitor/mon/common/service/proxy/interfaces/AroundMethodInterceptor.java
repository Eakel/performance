package com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.proxy.interfaces;

public interface AroundMethodInterceptor {
	public void beforeInterceptor(Object obj, String methodName, Object[] params) throws Exception;

	public void afterInterceptor(Object obj, String methodName, Object[] params) throws Exception;

	public void exceptionInterceptor(Object obj, String methodName, Object[] params) throws Exception;
}