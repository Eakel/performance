package com.easyfun.eclipse.performance.appframe.monitor.mon.client;

import java.util.HashMap;

import org.jboss.remoting.transporter.TransporterClient;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonServer;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;

/**
 * ����JBOSS RemoteԶ�̵���
 * @author linzhaoming
 * 
 * Created at 2012-9-16
 */
public final class ClientProxy {
	/**HashMap&lt;String, String> 
	 * <li>keyΪSERVER_ID��valueΪLOCATOR*/
	private static final HashMap LOCATOR = new HashMap();

	/** ����MON_SERVER.SERVER_ID�ֶκͽӿ�Class��ȡLocator��ʵ����*/
	public static Object getObject(long serverId, Class interfaceClass) throws Exception {
		Object rtn = null;

		if (!LOCATOR.containsKey(interfaceClass)) {
			synchronized (LOCATOR) {
				if (!LOCATOR.containsKey(interfaceClass)) {
					IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
					MonServer objMonMbeanServer = objIMonSV.getMonServerByServerId(serverId);
					LOCATOR.put(new Long(serverId), objMonMbeanServer.getLocator());
				}
			}
		}

		String locator = (String) LOCATOR.get(new Long(serverId));
		rtn = TransporterClient.createTransporterClient(locator, interfaceClass);
		return rtn;
	}

	public static void destroyObject(Object obj) {
		TransporterClient.destroyTransporterClient(obj);
	}
}