package com.easyfun.eclipse.performance.appframe.monitor.mon.common.service;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.proxy.ProxyInvocationHandler;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.proxy.impl.TransactionInterceptorImpl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MiscHelper;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ProxyUtil;

public class LocalServiceInvokeImpl implements IServiceInvoke {
	private static transient Log log = LogFactory.getLog(LocalServiceInvokeImpl.class);

	private static HashMap SERVICES_DEFINE = new HashMap();
	private static HashMap DAOS_DEFINE = new HashMap();

	public Object getService(Class interfaceClass) {
		Object rtn = null;
		try {
			if (StringUtils.lastIndexOf(interfaceClass.getName(), "DAO") != -1) {
				rtn = getDAOObject(interfaceClass, Class.forName(MiscHelper.getImplClassNameByInterClassName(interfaceClass)));
			} else if (StringUtils.lastIndexOf(interfaceClass.getName(), "SV") != -1) {
				rtn = getSVObject(interfaceClass, Class.forName(MiscHelper.getImplClassNameByInterClassName(interfaceClass)));
			} else {
				throw new Exception("无法获取对象,接口:" + interfaceClass.getName() + ",(注意大小写)");
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return rtn;
	}

	private Object getDAOObject(Class interfaceClass, Class implClass) throws Exception {
		Object rtn = implClass.newInstance();
		return rtn;
	}

	private Object getSVObject(Class interfaceClass, Class implClass) throws Exception {
		Object rtn = implClass.newInstance();
		ProxyInvocationHandler handler = new ProxyInvocationHandler(rtn, new Class[] { TransactionInterceptorImpl.class });
		rtn = ProxyUtil.getProxyObject(implClass.getClassLoader(), new Class[] { interfaceClass }, handler);
		return rtn;
	}
}