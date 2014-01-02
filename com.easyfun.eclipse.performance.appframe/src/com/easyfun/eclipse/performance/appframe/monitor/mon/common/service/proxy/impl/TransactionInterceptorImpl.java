package com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.proxy.impl;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.proxy.interfaces.AroundMethodInterceptor;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.session.SessionManager;

public class TransactionInterceptorImpl implements AroundMethodInterceptor {
	private static transient Log log = LogFactory.getLog(TransactionInterceptorImpl.class);
	private static final int REQUIRED = 1;
	private static final int REQUIRES_NEW = 2;
	private static final int SUPPORTS = 3;
	private static final int NOT_SUPPORTED = 4;
	private static final int NEVER = 5;
	private static final int MANDATORY = 6;
	private boolean isCreate = false;
	private boolean isSuspend = false;

	private static int DEFAULT_TRANSACTION_ATTRIBUTE = REQUIRED;

	/**HashMap&lt;ClassMethod,int> */
	private static HashMap METHOD_TX_MAP = new HashMap();

	public void beforeInterceptor(Object obj, String methodName, Object[] objectArray) throws Exception {
		int methodTransactionAttribute = getMethodTransactionAttribute(obj.getClass(), methodName);

		if (log.isDebugEnabled()) {
			log.debug("事务属性:" + int2tx(methodTransactionAttribute));
		}

		if (methodTransactionAttribute == REQUIRED) {
			if (SessionManager.getSession().isStartTransaction()) {
				if (log.isDebugEnabled())
					log.debug("类:" + obj.getClass().getName() + ",方法:" + methodName + "参与事务");
			} else {
				if (log.isDebugEnabled()) {
					log.debug("类:" + obj.getClass().getName() + ",方法:" + methodName + "开始事务");
				}
				SessionManager.getSession().startTransaction();
				this.isCreate = true;
			}
		} else {
			throw new Exception("不能识别的事务类型:" + methodTransactionAttribute);
		}
	}

	public void afterInterceptor(Object obj, String methodName, Object[] objectArray) throws Exception {
		if (this.isCreate) {
			SessionManager.getSession().commitTransaction();
			if (log.isDebugEnabled()) {
				log.debug("类:" + obj.getClass().getName() + ",方法:" + methodName + "提交事务");
			}
		}
	}

	public void exceptionInterceptor(Object obj, String methodName, Object[] objectArray) throws Exception {
		if (this.isCreate) {
			SessionManager.getSession().rollbackTransaction();
			if (log.isDebugEnabled()) {
				log.debug("类:" + obj.getClass().getName() + ",方法:" + methodName + "回滚事务");
			}
		}
	}

	private int getMethodTransactionAttribute(Class implClass, String methodName) throws Exception {
		int rtn = -1;
		if (METHOD_TX_MAP.containsKey(new ClassMethod(implClass, methodName))) {
			rtn = ((Integer) METHOD_TX_MAP.get(new ClassMethod(implClass, methodName))).intValue();
		} else {
			rtn = DEFAULT_TRANSACTION_ATTRIBUTE;
		}
		return rtn;
	}

	private static String int2tx(int i) {
		String rtn = null;
		if (i == REQUIRED) {
			rtn = "Required";
		} else if (i == REQUIRES_NEW) {
			rtn = "RequiresNew";
		} else if (i == SUPPORTS) {
			rtn = "Supports";
		} else if (i == NOT_SUPPORTED) {
			rtn = "NotSupported";
		} else if (i == NEVER) {
			rtn = "Never";
		} else if (i == MANDATORY) {
			rtn = "Mandatory";
		} else {
			throw new RuntimeException("无法认识的事务属性:" + i);
		}
		return rtn;
	}

	private static int tx2int(String tx) {
		int rtn = -1;
		if (tx.equalsIgnoreCase("Required")) {
			rtn = REQUIRED;
		} else if (tx.equalsIgnoreCase("RequiresNew")) {
			rtn = REQUIRES_NEW;
		} else if (tx.equalsIgnoreCase("Supports")) {
			rtn = SUPPORTS;
		} else if (tx.equalsIgnoreCase("NotSupported")) {
			rtn = NOT_SUPPORTED;
		} else if (tx.equalsIgnoreCase("Never")) {
			rtn = NEVER;
		} else if (tx.equalsIgnoreCase("Mandatory")) {
			rtn = MANDATORY;
		} else {
			throw new RuntimeException("无法认识的事务属性:" + tx);
		}
		return rtn;
	}

	private static class ClassMethod {
		Class implClass = null;
		String methodName = null;

		public ClassMethod(Class clazz, String str) {
			this.implClass = clazz;
			this.methodName = str;
		}

		public String toString() {
			return "类:" + this.implClass.getName() + ",方法:" + this.methodName;
		}

		public boolean equals(Object obj) {
			boolean rtn = false;
			if (obj == null) {
				return false;
			}

			if ((obj instanceof ClassMethod)) {
				ClassMethod objMethodTX = (ClassMethod) obj;
				if ((objMethodTX.implClass.equals(this.implClass)) && (objMethodTX.methodName.equals(this.methodName))) {
					rtn = true;
				}
			}

			return rtn;
		}

		public int hashCode() {
			return this.implClass.hashCode() + this.methodName.hashCode();
		}
	}
}