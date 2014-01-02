package com.easyfun.eclipse.performance.appframe.monitor.mon.common.session;

public class SessionManager {
	private static Session session = null;

	static {
		try {
			session = new LocalMutilTransactionImpl();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Session getSession() {
		return session;
	}

}