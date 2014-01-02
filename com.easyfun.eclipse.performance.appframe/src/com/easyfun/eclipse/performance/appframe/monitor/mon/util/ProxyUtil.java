package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;

public final class ProxyUtil {
	private static final Class[] CONSTRUCT_PARAM = { InvocationHandler.class };
	private static final HashMap CLAZZ_CACHE = new HashMap();

	public static final Object getProxyObject(ClassLoader loader, Class[] interfaces, InvocationHandler h) {
		String key = interfaces[0].getName();
		Class clazz = (Class) CLAZZ_CACHE.get(key);
		if (clazz == null) {
			synchronized (CLAZZ_CACHE) {
				if (!CLAZZ_CACHE.containsKey(key)) {
					Class c = Proxy.getProxyClass(loader, interfaces);
					CLAZZ_CACHE.put(key, c);
				}
				clazz = (Class) CLAZZ_CACHE.get(key);
			}
		}
		try {
			Constructor cons = clazz.getConstructor(CONSTRUCT_PARAM);
			return cons.newInstance(new Object[] { h });
		} catch (NoSuchMethodException e) {
			throw new InternalError(e.toString());
		} catch (IllegalAccessException e) {
			throw new InternalError(e.toString());
		} catch (InstantiationException e) {
			throw new InternalError(e.toString());
		} catch (InvocationTargetException e) {
			throw new InternalError(e.toString());
		}

	}
}