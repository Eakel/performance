package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.helper;

import org.jboss.remoting.transporter.TransporterClient;

/**
 * ����JBOSS RemoteԶ�̵���
 * @author linzhaoming
 * 
 * Created at 2012-9-16
 */
public final class JmxUrlClientProxy {

	/** ����MON_SERVER.SERVER_ID�ֶκͽӿ�Class��ȡLocator��ʵ����*/
	public static Object getObject(String locator, Class interfaceClass) throws Exception {
		Object rtn = null;
		rtn = TransporterClient.createTransporterClient(locator, interfaceClass);
		return rtn;
	}

	public static void destroyObject(Object obj) {
		TransporterClient.destroyTransporterClient(obj);
	}
}