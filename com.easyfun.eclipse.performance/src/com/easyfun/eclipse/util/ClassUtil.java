package com.easyfun.eclipse.util;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class ClassUtil {
	/** 加载指定Bundle的Class*/
	public static Class loadBundleClass(String bundleId, String className) throws Exception{
		Bundle bundle = Platform.getBundle(bundleId);
		Class clazz = bundle.loadClass(className);
		return clazz;
	}
}
