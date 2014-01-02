package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;

public class Test909 {
	public static void main(String[] args) throws Exception {
		String a = "a|||||cadasdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
		if (a.indexOf("||") != -1) {
			a = StringUtils.replace(a, "||", "| |");
		}
		if (a.indexOf("||") != -1) {
			a = StringUtils.replace(a, "||", "| |");
		}

		System.out.println(a);
		String[] tmp = StringUtils.split(a, "|");
		for (int i = 0; i < tmp.length; i++)
			System.out.println(tmp[i]);
	}

	public static void main2(String[] args) throws Exception {
		try {
			Connection conn = new Connection("10.96.19.146");
			conn.connect();
			boolean isAuth = conn.authenticateWithPassword("was", "1MidServer-1234");
			if (!conn.isAuthenticationComplete()) {
				throw new IOException("Authentication failed");
			}
			Session sess = null;
			try {
				sess = conn.openSession();
			} catch (IOException e2) {
				System.out.println("Session failed" + e2);
			}
			SCPClient scp = conn.createSCPClient();
			System.out.println("Client created");
			scp.put("C:/a.txt", "/tmp/");
			sess.close();
			conn.close();
		} catch (Exception e1) {
			System.out.println(e1.toString());
			System.exit(1);
		}
	}
}