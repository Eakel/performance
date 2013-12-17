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
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.other.memcached.model.MemModel;

public class MemcachedStatUtil {
	public static final byte[] BYTE_CRLF = { 13, 10 };
	public static final String SERVER_STATUS_END = "END";
	
	private static Properties p = new Properties();
	//TODO: �����������ļ��У��ﵽ������
	static {
		p.setProperty("curr_connections", "��ǰ��������");
		p.setProperty("bytes", "�ڴ�����ֽ���");
		p.setProperty("total_items", "��ǰ��������");
		p.setProperty("cmd_set", "���ö������");
		p.setProperty("uptime", "Memcached����ʱ��");
		p.setProperty("get_hits", "hint��ѯ����");
		p.setProperty("limit_maxbytes", "�����ڴ��ֽ���");
		p.setProperty("bytes_written", "����ֽ���");
		p.setProperty("bytes_read", "�����ֽ���");
		p.setProperty("pid", "����ID");
		p.setProperty("version", "Memcached�汾");
		p.setProperty("pointer_size", "����ϵͳָ���С(32λϵͳһ����32bit,64����64λ����ϵͳ)");
		p.setProperty("curr_items", "Memcached��ǰ�洢����������(PCEΪ��ǰ��������)");
		p.setProperty("threads", "��ǰ�߳���");
		p.setProperty("accepting_conns", "�������Ƿ�ﵽ���������(0/1)");
		p.setProperty("cmd_flush", "Flush��������");
		p.setProperty("cmd_get", "��ѯ��������");
		p.setProperty("get_misses", "��ѯ�ɹ�δ��ȡ�����ݵ��ܴ���");
		p.setProperty("rusage_user", "�ý����ۼƵ��û�ʱ�䣬��λ����");
		p.setProperty("rusage_system", "�ý����ۼƵ�ϵͳʱ�䣬��λ����");
		p.setProperty("total_connections", "Memcached�����������ܵ���������");
		p.setProperty("", "");
		p.setProperty("", "");
		p.setProperty("", "");
		p.setProperty("", "");
	}

	/** ִ��stats�����ȡMemcache��Ϣ*/
	public static List<MemModel> getStat(String host, int port) throws Exception {
		HashMap rtn = new HashMap();
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
	
	private static String getDescByKey(String key){
		if(p.containsKey(key)){
			return p.getProperty(key);
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