package com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.proxy.interfaces.AroundMethodInterceptor;

public final class ProxyInvocationHandler implements InvocationHandler {
	private static transient Log log = LogFactory.getLog(ProxyInvocationHandler.class);

	private Object _obj = null;
	private Class[] _interceptors_class = null;

	public ProxyInvocationHandler(Object _obj, Class[] _interceptors_class) {
		this._obj = _obj;
		this._interceptors_class = _interceptors_class;
	}

	public Object invoke(Object object, Method method, Object[] objectArray) throws Throwable {
		Object[] _interceptors = new Object[this._interceptors_class.length];
		for (int i = 0; i < this._interceptors_class.length; i++) {
			_interceptors[i] = this._interceptors_class[i].newInstance();
		}

		boolean[] isBeforeSuccess = new boolean[_interceptors.length];
		try {
			for (int i = 0; i < _interceptors.length; i++)
				if ((_interceptors[i] instanceof AroundMethodInterceptor)) {
					((AroundMethodInterceptor) _interceptors[i]).beforeInterceptor(this._obj, method.getName(), objectArray);
					isBeforeSuccess[i] = true;
				}
		} catch (Throwable ex) {
			log.fatal("�ڷ�������ǰ�������������ʧ��", ex);

			for (int i = _interceptors.length - 1; i >= 0; i--) {
				if (isBeforeSuccess[i] != true)
					continue;
				if ((_interceptors[i] instanceof AroundMethodInterceptor)) {
					((AroundMethodInterceptor) _interceptors[i]).exceptionInterceptor(this._obj, method.getName(), objectArray);
				}
			}

			throw ex;
		}

		Object rtn = null;
		try {
			rtn = method.invoke(this._obj, objectArray);
		} catch (Throwable ex) {
			try {
				for (int i = _interceptors.length - 1; i >= 0; i--) {
					if ((_interceptors[i] instanceof AroundMethodInterceptor)) {
						((AroundMethodInterceptor) _interceptors[i]).exceptionInterceptor(this._obj, method.getName(), objectArray);
					}
				}
			} catch (Throwable ex2) {
				log.fatal("�������쳣���ط�������ʧ���쳣", ex2);
			}

			Throwable root = null;
			try {
				root = ExceptionUtils.getRootCause(ex);
			} catch (Throwable ex3) {
				log.error("ExceptionUtils.getRootCause�����쳣,�����ӳ�ԭʼ�쳣", ex3);
				throw ex;
			}

			if (root != null) {
				log.error("�����쳣:", root);
				throw root;
			}

			log.error("�����쳣:", ex);
			throw ex;
		}

		try {
			for (int i = _interceptors.length - 1; i >= 0; i--) {
				if ((_interceptors[i] instanceof AroundMethodInterceptor)) {
					((AroundMethodInterceptor) _interceptors[i]).afterInterceptor(this._obj, method.getName(), objectArray);
				}
			}
		} catch (Throwable ex) {
			log.fatal("�ڷ������ú��������������ʧ��", ex);
		}

		return rtn;
	}
}