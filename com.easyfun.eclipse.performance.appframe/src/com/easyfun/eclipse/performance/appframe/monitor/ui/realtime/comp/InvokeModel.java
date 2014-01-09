package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp;

import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.helper.JmxUrlClientProxy;

public class InvokeModel {
	private static final int TYPE_SERVERID = 1;
	private static final int TYPE_JMXURL = 2;
	
	private long serverId = -1;
	
	private String jmxUrl = "";
	
	private int invokeType = 0;

	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
		this.invokeType = TYPE_SERVERID;
	}

	public String getJmxUrl() {
		return jmxUrl;
		
	}

	public void setJmxUrl(String jmxUrl) {
		this.jmxUrl = jmxUrl;
		this.invokeType = TYPE_JMXURL;
	}
	
	public Object getObject(Class clazz) throws Exception{
		if(this.invokeType == TYPE_SERVERID){
			return ClientProxy.getObject(serverId, clazz);
		}else if(this.invokeType == TYPE_JMXURL){
			return JmxUrlClientProxy.getObject(jmxUrl, clazz);
		}else{
			return null;
		}
	}
	
}
