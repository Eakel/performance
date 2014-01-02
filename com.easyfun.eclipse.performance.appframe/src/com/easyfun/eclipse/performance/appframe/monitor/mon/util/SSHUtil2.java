package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

/**
 * collect_trc.sh�Ĺ��ܣ���APP��WEB��trace�ļ��ƶ���/tmp/aitrc_tmp
 * @author linzhaoming
 * 
 * Created at 2012-9-22
 */
public class SSHUtil2 {
	/** �ռ�Trace�ļ�������ƶ���Trace�ļ���¼��
	 *  ��Trace�ļ����ص�����Ŀ¼
	 * <li>TRACE�ļ�·��/collect_trc.sh
	 * */
	public static int collectTraceFile(String ip, int sshPort, String username, String password) throws Exception {
		int rtn = 0;
		Connection conn = null;
		Session sess = null;
		try {
			conn = new Connection(ip, sshPort);
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(username, password);

			if (!isAuthenticated) {
//				throw new IOException("��֤ʧ��");
				throw new IOException("��֤ʧ��" + "ip=" + ip + ", port=" + sshPort + ", username="+ username);
			}

			String fileName = "collect_trc.sh";
			byte[] bb = FileUtils.readFileToByteArray(new File(MiscUtil.getTracePathPrefix() + "/collect_trc.sh"));
			upload(conn, ip, sshPort, username, password, bb, "/tmp/", fileName);
			sess = conn.openSession();

			sess.execCommand(" chmod +x /tmp/" + fileName + " && /tmp/" + fileName + " && rm -rf /tmp/" + fileName);

			List list = new ArrayList();
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				if (StringUtils.contains(line, "trc")) {
					list.add(line);
				}
			}

			rtn = list.size();
			download(conn, ip, sshPort, username, password, (String[]) list.toArray(new String[0]), MiscUtil.getTracePathPrefix() + "/data/");
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (sess != null) {
				sess.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return rtn;
	}

	/** �ϴ��ļ���Զ������*/
	public static void upload(Connection conn, String ip, int sshPort, String username, String password, byte[] bytes, String destPath, String fileName) throws Exception {
		try {
			SCPClient scp = conn.createSCPClient();
			scp.put(bytes, fileName, destPath);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/** ����Trace�ļ�������Ŀ¼*/
	public static void download(Connection conn, String ip, int sshPort, String username, String password, String[] remoteFileName, String localPath) throws Exception {
		try {
			SCPClient scp = conn.createSCPClient();
			scp.get(remoteFileName, localPath);
		} catch (Exception ex) {
			throw ex;
		}
	}
}