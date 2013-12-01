package com.easyfun.eclipse.performance.http.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.easyfun.eclipse.performance.http.model.impl.HeadMethodImpl;
import com.easyfun.eclipse.performance.http.model.impl.HttpDeleteMethod;
import com.easyfun.eclipse.performance.http.model.impl.HttpGetMethod;
import com.easyfun.eclipse.performance.http.model.impl.HttpMethodInvocationHandler;
import com.easyfun.eclipse.performance.http.model.impl.HttpOptionsMethod;
import com.easyfun.eclipse.performance.http.model.impl.HttpPostMethod;
import com.easyfun.eclipse.performance.http.model.impl.HttpPutMethod;
import com.easyfun.eclipse.performance.http.model.impl.HttpTraceMethod;
import com.easyfun.eclipse.performance.http.model.interfaces.IDeleteMethod;
import com.easyfun.eclipse.performance.http.model.interfaces.IGetMethod;
import com.easyfun.eclipse.performance.http.model.interfaces.IHeadMethod;
import com.easyfun.eclipse.performance.http.model.interfaces.IHttpMethod;
import com.easyfun.eclipse.performance.http.model.interfaces.IOptionsMethod;
import com.easyfun.eclipse.performance.http.model.interfaces.IPostMethod;
import com.easyfun.eclipse.performance.http.model.interfaces.IPutMethod;
import com.easyfun.eclipse.performance.http.model.interfaces.ITraceMethod;


/**
 * 创建HttpMethod的工厂类
 * 增加代理功能，从而支持 PropertyChangeSupport.
 * 
 * @author linzhaoming
 *
 */
public class MethodFactory {
	
	/** Create the Proxy method for the delegate. */
	private static IHttpMethod createProxy(IHttpMethod delegate) {
		InvocationHandler handler = new HttpMethodInvocationHandler(delegate);
		IHttpMethod method = (IHttpMethod) Proxy.newProxyInstance(delegate.getClass().getClassLoader(), delegate.getClass().getInterfaces(), handler);
		return method;
	}
	
	/** 根据类型创建实际对应的HttpMethod*/
	public static IHttpMethod createHttpMethod(HttpMethodEnum method) {
		switch (method) {
		case GET:
			return (IGetMethod) createProxy(new HttpGetMethod());
		case POST:
			return (IPostMethod) createProxy(new HttpPostMethod());
		case HEAD:
			return (IHeadMethod) createProxy(new HeadMethodImpl());
		case OPTIONS:
			return (IOptionsMethod) createProxy(new HttpOptionsMethod());
		case TRACE:
			return (ITraceMethod) createProxy(new HttpTraceMethod());
		case PUT:
			return (IPutMethod) createProxy(new HttpPutMethod());
		case DELETE:
			return (IDeleteMethod) createProxy(new HttpDeleteMethod());
		default:
			return (IGetMethod) createProxy(new HttpGetMethod());
		}
	}
}
