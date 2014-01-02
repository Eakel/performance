package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

public final class MiscHelper {
	/** HashMap&lt;String, String >*/
	private static final HashMap SERVICES_CACHE = new HashMap();
	/** HashMap&lt; >*/
	private static final HashMap CLIENT_CONSTRUCTOR_CACHE = new HashMap();
	/** HashMap&lt;Class, String>*/
	private static final HashMap JNDI_CACHE = new HashMap();
	/** HashMap&lt;Class, Class>*/
	private static final HashMap HOMECLASS_CACHE = new HashMap();

	/** 根据接口类获取实现类名字*/
	public static String getImplClassNameByInterClassName(Class interClass) {
		String rtn = (String) SERVICES_CACHE.get(interClass.getName());
		if (StringUtils.isBlank(rtn)) {
			synchronized (SERVICES_CACHE) {
				if (!SERVICES_CACHE.containsKey(interClass)) {
					String packageName = StringUtils.replace(ClassUtils.getPackageName(interClass), "interfaces", "impl");
					char[] className = (ClassUtils.getShortClassName(interClass) + "Impl").toCharArray();
					rtn = packageName + "." + new String(className, 1, className.length - 1);
					SERVICES_CACHE.put(interClass.getName(), rtn);
				}
				rtn = (String) SERVICES_CACHE.get(interClass.getName());
			}
		}
		return rtn;
	}

	/** 根据接口类名字获取实现类名字*/
	public static String getImplClassNameByInterClassName(String interClass) {
		String rtn = (String) SERVICES_CACHE.get(interClass);
		if (StringUtils.isBlank(rtn)) {
			synchronized (SERVICES_CACHE) {
				if (!SERVICES_CACHE.containsKey(interClass)) {
					String[] tmp = StringUtils.split(interClass, ".");
					String[] packageTmp = new String[tmp.length - 1];
					System.arraycopy(tmp, 0, packageTmp, 0, tmp.length - 1);

					String packageName = StringUtils.replace(StringUtils.join(packageTmp, "."), "interfaces", "impl");
					char[] className = (tmp[(tmp.length - 1)] + "Impl").toCharArray();
					rtn = packageName + "." + new String(className, 1, className.length - 1);
					SERVICES_CACHE.put(interClass, rtn);
				}
				rtn = (String) SERVICES_CACHE.get(interClass);
			}
		}
		return rtn;
	}

	/** 根据接口类获取JNDI名字*/
	public static String getJndiNameByInterClassName(Class interfaceClass) throws Exception {
		String jndi = null;
		if (!JNDI_CACHE.containsKey(interfaceClass)) {
			synchronized (JNDI_CACHE) {
				if (!JNDI_CACHE.containsKey(interfaceClass)) {
					jndi = StringUtils.replace(interfaceClass.getName(), "interfaces", "ejb");
					if (jndi == null) {
						throw new Exception("无法为接口类:" + interfaceClass + ",找到jndi");
					}
					JNDI_CACHE.put(interfaceClass, jndi);
				}
				jndi = (String) JNDI_CACHE.get(interfaceClass);
			}
		} else {
			jndi = (String) JNDI_CACHE.get(interfaceClass);
		}
		return jndi;
	}

	/** 根据接口类获取Home Class*/
	public static Class getHomeClassNameByInterClassName(Class interfaceClass) throws Exception {
		Class home = null;
		if (!HOMECLASS_CACHE.containsKey(interfaceClass)) {
			synchronized (HOMECLASS_CACHE) {
				if (!HOMECLASS_CACHE.containsKey(interfaceClass)) {
					home = Class.forName(StringUtils.replace(interfaceClass.getName(), "interfaces", "ejb") + "RemoteHome");
					if (home == null) {
						throw new Exception("无法为接口类:" + interfaceClass + ",找到home类");
					}
					HOMECLASS_CACHE.put(interfaceClass, home);
				}
				home = (Class) HOMECLASS_CACHE.get(interfaceClass);
			}
		} else {
			home = (Class) HOMECLASS_CACHE.get(interfaceClass);
		}

		return home;
	}

	/** 根据接口类获取实现EJB的客户端Constructor*/
	public static Constructor getEJBClientConstructor(Class interfaceClass) throws Exception {
		Constructor objConstructor = null;
		if (!CLIENT_CONSTRUCTOR_CACHE.containsKey(interfaceClass)) {
			synchronized (CLIENT_CONSTRUCTOR_CACHE) {
				if (!CLIENT_CONSTRUCTOR_CACHE.containsKey(interfaceClass)) {
					Class client = Class.forName(StringUtils.replace(interfaceClass.getName(), "interfaces", "ejb") + "Client");
					Constructor[] constructors = client.getConstructors();
					for (int i = 0; i < constructors.length; i++) {
						if ((constructors[i].getParameterTypes() != null) && (constructors[i].getParameterTypes().length == 1)) {
							objConstructor = constructors[i];
							break;
						}
					}

					if (objConstructor == null) {
						throw new Exception("无法为接口类:" + interfaceClass + ",找到ejb的客户端类");
					}
					CLIENT_CONSTRUCTOR_CACHE.put(interfaceClass, objConstructor);
				}
				objConstructor = (Constructor) CLIENT_CONSTRUCTOR_CACHE.get(interfaceClass);
			}
		} else {
			objConstructor = (Constructor) CLIENT_CONSTRUCTOR_CACHE.get(interfaceClass);
		}
		return objConstructor;
	}

	public static String getCallPath() {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stack = new Throwable().getStackTrace();
		for (int i = 0; i < stack.length; i++) {
			sb.append(stack[i].getClassName() + "." + stack[i].getMethodName() + "() 行数:" + stack[i].getLineNumber() + "\n");
		}
		return sb.toString();
	}
}