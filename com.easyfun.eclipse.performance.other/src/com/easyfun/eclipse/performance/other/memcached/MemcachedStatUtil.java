package com.easyfun.eclipse.performance.other.memcached;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.other.memcached.model.MemModel;

public class MemcachedStatUtil {
	public static final byte[] BYTE_CRLF = { 13, 10 };
	public static final String SERVER_STATUS_END = "END";
	
	private static Properties p = new Properties();
	static {
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("memhelp.properties");
		try {
			p.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** ִ��stats�����ȡMemcache��Ϣ*/
	public static List<MemModel> getStat(String host, int port) throws Exception {
		HashMap<String, String> rtn = new HashMap<String, String>();
		List<MemModel> list = new ArrayList<MemModel>();
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
				String[] strs = StringUtils.split(cmd, " ");
				MemModel model = new MemModel(strs[1], strs[2]);
				String desc = getDescByKey(model.getKey());
				model.setDesc(desc);
				list.add(model);
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
	
	/** ִ��stats items�����ȡMemcache��Ϣ*/
	//TODO:�������slab�е�item��Ϣ��s
	public static List<MemModel> getStatItems(String host, int port) throws Exception {
		HashMap<String, String> rtn = new HashMap<String, String>();
		List<MemModel> list = new ArrayList<MemModel>();
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			out.write("stats items".getBytes());
			out.write(BYTE_CRLF);

			out.flush();

			rtn.put("host", socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
			String cmd = readLine(in);
			while (!"END".equals(cmd)) {
				String[] strs = StringUtils.split(cmd, " ");
				MemModel model = new MemModel(strs[1], strs[2]);
				String desc = getDescByKey(model.getKey());
				model.setDesc(desc);
				list.add(model);
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
	
	/** ִ��stats slabs�����ȡMemcache��Ϣ*/
	//TODO: ���slab�и���ϸ��item��Ϣ
	public static List<MemModel> getStatSlabs(String host, int port) throws Exception {
		HashMap<String, String> rtn = new HashMap<String, String>();
		List<MemModel> list = new ArrayList<MemModel>();
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			out.write("stats slabs".getBytes());
			out.write(BYTE_CRLF);

			out.flush();

			rtn.put("host", socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
			String cmd = readLine(in);
			while (!"END".equals(cmd)) {
				String[] strs = StringUtils.split(cmd, " ");
				MemModel model = new MemModel(strs[1], strs[2]);
				String desc = getDescByKey(model.getKey());
				model.setDesc(desc);
				list.add(model);
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
	
	/** ִ��stats sizes�����ȡMemcache��Ϣ*/
	//TODO: �������item�Ĵ�С�͸���
	public static List<MemModel> getStatSizes(String host, int port) throws Exception {
		HashMap<String, String> rtn = new HashMap<String, String>();
		List<MemModel> list = new ArrayList<MemModel>();
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			out.write("stats sizes".getBytes());
			out.write(BYTE_CRLF);

			out.flush();

			rtn.put("host", socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
			String cmd = readLine(in);
			while (!"END".equals(cmd)) {
				String[] strs = StringUtils.split(cmd, " ");
				MemModel model = new MemModel(strs[0], strs[1]);
				String desc = getDescByKey(model.getKey());
				model.setDesc(desc);
				list.add(model);
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
	
	private static String getDescByKey(String key){
		if(p.containsKey(key)){
			String value = p.getProperty(key);
			try {
				return new String(value.getBytes("ISO-8859-1"), "GBK");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return value;
			}
//			return p.getProperty(key);
		}else{
			return "";
		}
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