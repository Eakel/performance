package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.helper;

import org.jboss.remoting.transporter.TransporterClient;

/**
 * 利用JBOSS Remote远程调用
 * @author linzhaoming
 * 
 * Created at 2012-9-16
 */
public final class JmxUrlClientProxy {

	/** 根据MON_SERVER.SERVER_ID字段和接口Class获取Locator的实现类*/
	public static Object getObject(String locator, Class interfaceClass) throws Exception {
		Object rtn = null;
		rtn = TransporterClient.createTransporterClient(locator, interfaceClass);
		return rtn;
	}

	public static void destroyObject(Object obj) {
		TransporterClient.destroyTransporterClient(obj);
	}
}