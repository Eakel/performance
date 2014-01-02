package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;

public class DefaultSocketCheckImpl implements IInterCheck {
	public CheckStatus check(Map map) throws Exception {
		CheckStatus cs = new CheckStatus();
		try {
			String host = (String) map.get("HOST");
			String port = (String) map.get("PORT");
			String timeout = (String) map.get("TIMEOUT");

			Socket socket = new Socket();
			SocketAddress sa = new InetSocketAddress(host, Integer.parseInt(port));
			socket.connect(sa, Integer.parseInt(timeout) * 1000);
			cs.setStatus("OK");
			cs.setInfo("OK");
		} catch (Exception ex) {
			cs.setStatus("EXCEPTION");
		}

		return cs;
	}
}