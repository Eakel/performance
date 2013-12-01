package com.easyfun.eclipse.performance.http.model.impl;

import java.beans.Introspector;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.easyfun.eclipse.performance.http.model.interfaces.IHttpMethod;
import com.easyfun.eclipse.performance.http.model.interfaces.IPostMethod;

/**
 * 
 * @author linzhaoming
 *
 */
public class HttpMethodInvocationHandler implements InvocationHandler {
	private final static String REQUEST_HEADERS_PROP = "requestHeaders";
	private final static String REQUEST_HEADER_PROP = "requestHeader".intern();
	private final static String PARAMETER_PROP = "parameter".intern();
	private final static String PARAMETERS_PROP = "parameters";

	// preloaded Method objects for the methods in java.lang.Object
	private static Method hashCodeMethod;
	private static Method equalsMethod;
	private static Method toStringMethod;
	
	static {
		try {
			hashCodeMethod = Object.class.getMethod("hashCode", new Class[0]);
			equalsMethod = Object.class.getMethod("equals", new Class[] { Object.class });
			toStringMethod = Object.class.getMethod("toString", new Class[0]);
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodError(e.getMessage());
		}
	}

	final IHttpMethod httpMethod;

	public HttpMethodInvocationHandler(IHttpMethod method) {
		this.httpMethod = method;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class declaringClass = method.getDeclaringClass();
		if (declaringClass == Object.class) {
			if (method.equals(hashCodeMethod)) {
				return proxyHashCode(proxy);
			} else if (method.equals(equalsMethod)) {
				return proxyEquals(proxy, args[0]);
			} else if (method.equals(toStringMethod)) {
				return proxyToString(proxy);
			} else {
				throw new InternalError("unexpected Object method dispatched: " + method);
			}
		} else {
			Object rtn = null;
			try {
				rtn = method.invoke(httpMethod, args);
				if (isModifyMethod(method.getName())) {
					sendPropertyChangeEvent(method, args);
				}
			} catch (InvocationTargetException e) {
				throw e.getCause();
			}
			return rtn;
		}
	}

	/**
	 * Get the property name from the given Method.
	 * <p>Currently support the following methods, others will return null.
	 * <li>setXXX: xxx</li>
	 * <li>addXXX: xxx</li>
	 * <li>removeXXX: xxx</li>
	 * <li>executeXXX: xxx</li>
	 * <li>addXXX: "response"</li>
	 * @param method
	 * @return
	 */
	private String method2PropertyName(Method method) {
		if (method.getName().startsWith("set") || method.getName().startsWith("add")) {
			String propertyName = method.getName().substring(3);
			return Introspector.decapitalize(propertyName);
		} else if (method.getName().startsWith("remove")) {
			String propertyName = method.getName().substring(6);
			return Introspector.decapitalize(propertyName);
		} else if (method.getName().equals("execute")) {
			return IHttpMethod.RESPONSE_PROP;
		}
		return null;
	}

	private void sendPropertyChangeEvent(Method method, Object[] args) {
		String propertyName = method2PropertyName(method);
		if (propertyName == null) {
			return;
		}
		if (propertyName.intern() == REQUEST_HEADER_PROP) {
			httpMethod.firePropertyChange(REQUEST_HEADERS_PROP, null, httpMethod.getRequestHeaders());
		} else if (propertyName.intern() == PARAMETER_PROP) {
			httpMethod.firePropertyChange(PARAMETERS_PROP, null, ((IPostMethod) httpMethod).getParameters());
		} else {
			httpMethod.firePropertyChange(propertyName, null, args[0]);
		}
	}

	/** 从方法名判断是否为修改方法，目前支持以下方法为修改方法
	 * <li>setXXX</li>
	 * <li>addXXX</li>
	 * <li>removeXXX</li>
	 * <li>executeXXX</li>
	 */
	private boolean isModifyMethod(String methodName) {
		return methodName.startsWith("set") || methodName.startsWith("add") || methodName.startsWith("remove") || methodName.startsWith("execute");
	}
	
	// ------------------- Object proxy Methods
	/**
	 * Proxy hashCode Method.
	 */
	private Integer proxyHashCode(Object proxy) {
		return new Integer(System.identityHashCode(proxy));
	}

	/**
	 * Proxy equals Method
	 */
	private Boolean proxyEquals(Object proxy, Object other) {
		return (proxy == other ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Proxy toString Method.
	 */
	private String proxyToString(Object proxy) {
		return proxy.getClass().getName() + '@' + Integer.toHexString(proxy.hashCode());
	}
}
