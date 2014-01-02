package com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.javacommand;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Telnet
 * @author linzhaoming
 * 
 * Created at 2012-9-22
 */
public class TelnetPort implements IJavaCommand {
	private static transient Log log = LogFactory.getLog(TelnetPort.class);

	/** [host]:[port]
	 * <li>以":"为分割 
	 * <li>返回"CONNECT:1"代表成功
	 * <li>返回"CONNECT:0"代表失败
	 * */
	public String execute(String in) throws Exception {
		String rtn = "CONNECT:0";
		String[] tmp = StringUtils.split(in, ":");
		String host = tmp[0].trim();
		int port = Integer.parseInt(tmp[1]);

		Socket socket = null;
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(host, port), 2000);
			rtn = "CONNECT:1";
		} catch (Throwable ex) {
			rtn = "CONNECT:0";
			log.error("连接:" + host + ":" + port + "失败", ex);
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
		return rtn;
	}

	public static void main(String[] args) throws Exception {
		TelnetPort a = new TelnetPort();
		System.out.println(a.execute("10.96.19.70:22"));
	}
}