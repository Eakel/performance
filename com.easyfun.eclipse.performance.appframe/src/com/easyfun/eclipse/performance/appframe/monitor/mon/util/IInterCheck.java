package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.util.Map;

public interface IInterCheck {
	public static final String EXCEPTION = "EXCEPTION";
	public static final String TIMEOUT = "TIMEOUT";
	public static final String OK = "OK";

	public CheckStatus check(Map map) throws Exception;
}