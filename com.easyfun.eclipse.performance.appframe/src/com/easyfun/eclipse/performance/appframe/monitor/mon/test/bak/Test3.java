package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class Test3 {
	public static void main(String[] args) {
		String hostname = "10.96.20.187";
		String username = "was";
		String password = "MidServer-1234";
		try {
			Connection conn = new Connection(hostname);
			conn.connect();

			boolean isAuthenticated = conn.authenticateWithPassword(username, password);
			if (!isAuthenticated) {
				throw new IOException("Authentication failed.");
			}

			Session sess = conn.openSession();
			sess.execCommand("echo \"============\" && uname -a && echo \"============\" && date && echo \"============\" && ls -alrt && echo \"============\" && ps -ef | grep sshd && echo \"============\" && uptime && echo \"============\" && who && echo \"============\" ");
			System.out.println("Here is some information about the remote host:");
			InputStream stdout = new StreamGobbler(sess.getStdout());

			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				System.out.println(line);
			}

			System.out.println("ExitCode: " + sess.getExitStatus());
			sess.close();
			conn.close();
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(2);
		}
	}
}