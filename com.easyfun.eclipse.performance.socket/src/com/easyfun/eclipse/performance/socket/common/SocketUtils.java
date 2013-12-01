package com.easyfun.eclipse.performance.socket.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 
 * @author linzhaoming
 * 
 *         2011-5-15
 * 
 */
public class SocketUtils {
	public static boolean checkHost(String host) {
		try {
			InetAddress.getByName(host);
			return (true);
		} catch (UnknownHostException uhe) {
			return (false);
		}
	}
}
