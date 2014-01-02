package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;

public final class HttpUtil {
	public static String curl(String url, int timeout) throws Exception {
		String rtn = null;

		HttpClient client = null;
		GetMethod method = null;
		try {
			client = new HttpClient();
			client.setConnectionTimeout(timeout * 1000);

			method = new GetMethod(url);
			method.setRequestHeader("Connection", "close");
			method.getParams().setParameter("http.socket.timeout", Integer.valueOf(timeout * 1000));
			int statusCode = client.executeMethod(method);
			if (statusCode == 200) {
				rtn = new String(method.getResponseBody(), "gb2312");
			} else {
				throw new Exception("不能正确获得, statusCode=" + statusCode);
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
			if (client != null) {
				client.getHttpConnectionManager().closeIdleConnections(0);
				((SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
			}
		}

		return rtn;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(curl("http://10.96.20.132:21000/version.txt", 1));
	}
}