package com.easyfun.eclipse.performance.other.memcached;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.common.kv.KeyValue;

public class MemcachedStatUtil {
	public static final byte[] BYTE_CRLF = { 13, 10 };
	public static final String SERVER_STATUS_END = "END";

	/** 执行stats命令，获取Memcache信息*/
	public static List<KeyValue> getStat(String host, int port) throws Exception {
		HashMap rtn = new HashMap();
		List<KeyValue> list = new ArrayList<KeyValue>();
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			out.write("stats".getBytes());
			out.write(BYTE_CRLF);

			out.flush();

			rtn.put("host", socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
			String cmd = readLine(in);
			while (!"END".equals(cmd)) {
				String[] yh = StringUtils.split(cmd, " ");
				KeyValue kv = new KeyValue(yh[1], yh[2]);
				list.add(kv);
//				rtn.put(yh[1], yh[2]);
				cmd = readLine(in);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (socket != null) {
				socket.close();
				socket = null;
			}
		}

		return list;
	}

	private static String readLine(InputStream in) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		boolean eol = false;
		byte[] b = new byte[1];
		while (in.read(b, 0, 1) != -1) {
			if (b[0] == 13) {
				eol = true;
			} else {
				if ((eol) && (b[0] == 10)) {
					break;
				}
				eol = false;
			}

			bos.write(b, 0, 1);
		}

		if (bos.size() == 0) {
			return null;
		}
		return bos.toString().trim();
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getStat("10.96.19.157", 37777));
	}
}